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
  formula: z.string().min(1, {
    message: "Formula is required.",
  }),
});

export default function QueryCard() {
  const { fetchQueryFormula, validateQueryFormula } = useKnowledgeBase();

  const [state, setState] = useState<{
    formula: string;
    operation: string;
    loading: boolean;
  }>({
    operation: "view",
    formula: "",
    loading: false,
  });

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: { formula: state.formula },
  });

  useEffect(() => {
    fetchQueryFormula().then((formula) => {
      if (formula != undefined) {
        setState((prevState) => ({ ...prevState, formula }));
      }
    });
  }, [fetchQueryFormula]);

  useEffect(() => {
    form.reset({ formula: state.formula });
  }, [form, state.formula]);

  const onSubmit = async (values: z.infer<typeof formSchema>) => {
    setState((prevState) => ({ ...prevState, loading: true }));

    const isValid = await validateQueryFormula(values.formula);

    setState((prevState) => ({ ...prevState, loading: false }));

    if (isValid) {
      fetchQueryFormula().then((formula) => {
        if (formula != undefined) {
          setState((prevState) => ({
            ...prevState,
            formula,
            operation: "view",
          }));
        }
      });
    } else {
      form.setError("formula", {
        type: "manual",
        message: "The query formula is invalid",
      });
    }
  };

  const handleCancel = () => {
    form.reset({ formula: state.formula });
    setState((prevState) => ({ ...prevState, operation: "view" }));
  };

  const handleEdit = () => {
    setState((prevState) => ({ ...prevState, operation: "edit" }));
  };

  return (
    <div className="h-full">
      <Card>
        <CardHeader className="space-y-0 pb-4">
          <CardTitle className="text-base text-center font-medium">
            Query Formula
          </CardTitle>
        </CardHeader>
        <CardContent className="flex justify-center">
          {state.operation == "view" && (
            <div className="w-full flex flex-col gap-4 items-center">
              <TexFormula>{texFormula(state.formula)}</TexFormula>
              <div className="grid grid-cols-2 gap-4 w-full max-w-sm">
                <Button
                  variant="secondary"
                  onClick={handleEdit}
                  disabled={state.loading}
                >
                  Edit
                </Button>
                <Button type="submit" disabled={state.loading}>
                  Query
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
                <FormField
                  control={form.control}
                  name="formula"
                  render={({ field }) => (
                    <FormItem>
                      <FormControl>
                        <Input
                          placeholder="formula"
                          {...field}
                          className="text-center"
                        />
                      </FormControl>
                      <FormMessage />
                    </FormItem>
                  )}
                />
                <div className="grid grid-cols-2 gap-4">
                  <Button
                    type="button"
                    variant="secondary"
                    onClick={handleCancel}
                    disabled={state.loading}
                  >
                    Cancel
                  </Button>
                  <Button type="submit" disabled={state.loading}>
                    Update
                  </Button>
                </div>
              </form>
            </Form>
          )}
        </CardContent>
      </Card>
    </div>
  );
}
