import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { z } from "zod";
import { Operation, texFormula } from "@/lib/utils";
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
import { useEffect, useState } from "react";
import { Button } from "./ui/button";
import axios from "axios";

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
  const [state, setState] = useState<{
    formulas: string;
    loadFromFile: boolean;
    operation: Operation;
  }>({
    operation: Operation.View,
    loadFromFile: false,
    formulas: "p=>b, b~>f, b~>w, p~>!f",
  });

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: { formulas: state.formulas },
  });

  const onSubmit = (values: z.infer<typeof formSchema>) => {
    setState((prevState) => ({
      ...prevState,
      operation: Operation.Update,
    }));
    console.log(values);
  };

  const handleCancel = () => {
    form.reset({ formulas: state.formulas });
    setState((prevState) => ({
      ...prevState,
      operation: Operation.View,
    }));
  };

  const handleEdit = () => {
    setState((prevState) => ({
      ...prevState,
      operation: Operation.Edit,
    }));
  };

  const handleLoadFile = () => {
    setState((prevState) => ({
      ...prevState,
      loadFromFile: true,
      operation: Operation.Edit,
    }));
  };

  useEffect(() => {
    axios
      .get(`/api/validate/formulas/${state.formulas}`)
      .then((response) => console.log(response.data));
  }, [state.formulas]);

  return (
    <div>
      <Card className="h-full">
        <CardHeader className="space-y-0 pb-4">
          <CardTitle className="text-base text-center font-medium">
            Knowledge Base
          </CardTitle>
        </CardHeader>
        <CardContent className="flex justify-center">
          {state.operation == Operation.View && (
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
          {state.operation == Operation.Edit && (
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
