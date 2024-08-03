import { zodResolver } from "@hookform/resolvers/zod";
import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { texFormula } from "@/lib/utils";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormMessage,
} from "@/components/ui/form";
import { Input } from "@/components/ui/input";
import TexFormula from "@/components/TexFormula";
import { Button } from "@/components/ui/button";
import { useKnowledgeBase } from "@/hooks/use-knowledge-base";

const formSchema = z.object({
  formulas: z.string().min(1, {
    message: "Knowledge base is required.",
  }),
  file: z
    .any()
    .refine((file) => file instanceof File, {
      message: "File is required.",
    })
    .optional(),
});

export default function KnowledgeBaseCard() {
  const {
    fetchKnowledgeBase,
    validateKnowledgeBase,
    validateKnowledgeBaseFile,
  } = useKnowledgeBase();

  const [state, setState] = useState<{
    formulas: string;
    loadFromFile: boolean;
    operation: string;
    loading: boolean;
  }>({
    operation: "view",
    loadFromFile: false,
    formulas: "",
    loading: false,
  });

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: { formulas: state.formulas },
  });

  useEffect(() => {
    fetchKnowledgeBase().then((formulas) => {
      if (formulas != undefined) {
        setState((prevState) => ({ ...prevState, formulas }));
      }
    });
  }, [fetchKnowledgeBase]);

  useEffect(() => {
    form.reset({ formulas: state.formulas });
  }, [form, state.formulas]);

  const onSubmit = async (values: z.infer<typeof formSchema>) => {
    setState((prevState) => ({ ...prevState, loading: true }));

    let isValid: boolean | undefined;
    let target: "formulas" | "file" = "formulas";
    if (!state.loadFromFile) {
      isValid = await validateKnowledgeBase(values.formulas);
      target = "formulas";
    } else {
      if (values.file) {
        const formData = new FormData();
        formData.append("file", values.file);
        isValid = await validateKnowledgeBaseFile(formData);
        target = "file";
      } else {
        form.setError("file", {
          type: "manual",
          message: "File is required",
        });
      }
    }
    setState((prevState) => ({ ...prevState, loading: false }));
    if (isValid) {
      fetchKnowledgeBase().then((formulas) => {
        if (formulas != undefined) {
          setState((prevState) => ({
            ...prevState,
            formulas,
            operation: "view",
            loadFromFile: false,
          }));
        }
      });
    } else {
      form.setError(target, {
        type: "manual",
        message: "The knowledge base is invalid",
      });
    }
  };

  const handleCancel = () => {
    form.reset({ formulas: state.formulas });
    setState((prevState) => ({
      ...prevState,
      loadFromFile: false,
      operation: "view",
    }));
  };

  const handleEdit = () => {
    setState((prevState) => ({
      ...prevState,
      loadFromFile: false,
      operation: "edit",
    }));
  };

  const handleLoadFile = () => {
    setState((prevState) => ({
      ...prevState,
      loadFromFile: true,
      operation: "edit",
    }));
  };

  return (
    <div>
      <Card className="h-full">
        <CardHeader className="space-y-0 pb-4">
          <CardTitle className="text-base text-center font-medium">
            Knowledge Base <TexFormula>{"(\\mathcal{K})"}</TexFormula>
          </CardTitle>
        </CardHeader>
        <CardContent className="flex justify-center">
          {state.operation == "view" && (
            <div className="w-full flex flex-col gap-4 items-center">
              <div className="line-clamp-1">
                {state.formulas.split(",").map((formula, index, array) => (
                  <span key={index}>
                    <TexFormula>{texFormula(formula)}</TexFormula>
                    {index < array.length - 1 && (
                      <TexFormula>{",\\;"}</TexFormula>
                    )}
                  </span>
                ))}
              </div>
              <div className="grid grid-cols-2 gap-4 w-full max-w-sm">
                <Button variant="outline" onClick={handleLoadFile}>
                  Upload
                </Button>
                <Button variant="secondary" onClick={handleEdit}>
                  Edit
                </Button>
              </div>
            </div>
          )}
          {state.operation == "edit" && (
            <Form {...form}>
              <form
                onSubmit={form.handleSubmit(onSubmit)}
                className="space-y-4 w-full max-w-sm"
              >
                {!state.loadFromFile && (
                  <FormField
                    control={form.control}
                    name="formulas"
                    render={({ field }) => (
                      <FormItem>
                        <FormControl>
                          <Input
                            placeholder="formula1, formula2, ..."
                            {...field}
                            className="text-center"
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                )}

                {state.loadFromFile && (
                  <FormField
                    control={form.control}
                    name="file"
                    render={({ field }) => (
                      <FormItem>
                        <FormControl>
                          <Input
                            className="text-center"
                            type="file"
                            accept=".txt"
                            onChange={(e) => {
                              field.onChange(
                                e.target.files ? e.target.files[0] : null
                              );
                            }}
                          />
                        </FormControl>
                        <FormMessage />
                      </FormItem>
                    )}
                  />
                )}
                <div className="grid grid-cols-2 gap-4">
                  <Button
                    type="button"
                    variant="secondary"
                    onClick={handleCancel}
                  >
                    Cancel
                  </Button>
                  <Button type="submit">Update</Button>
                </div>
              </form>
            </Form>
          )}
        </CardContent>
      </Card>
    </div>
  );
}
