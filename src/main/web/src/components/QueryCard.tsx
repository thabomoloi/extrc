import { zodResolver } from "@hookform/resolvers/zod";
import { useForm } from "react-hook-form";
import { string, z } from "zod";
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
import { useState } from "react";
import { Button } from "./ui/button";

const formSchema = z.object({
  formula: z.string().min(1, {
    message: "Formula is required.",
  }),
});
export default function QueryCard() {
  const [state, setState] = useState<{ formula: string; operation: Operation }>(
    {
      operation: Operation.View,
      formula: "p=>f",
    }
  );

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: { formula: state.formula },
  });

  const onSubmit = (values: z.infer<typeof formSchema>) => {
    setState((prevState) => ({
      ...prevState,
      operation: Operation.Update,
    }));
    console.log(values);
  };

  const handleCancel = () => {
    form.reset({ formula: state.formula });
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

  return (
    <div>
      <Card>
        <CardHeader className="space-y-0 pb-4">
          <CardTitle className="text-base text-center font-medium">
            Query Formula
          </CardTitle>
        </CardHeader>
        <CardContent>
          {state.operation == Operation.View && (
            <div className="flex flex-col gap-4 items-center">
              <TexFormula>{texFormula(state.formula)}</TexFormula>
              <Button variant="secondary" onClick={handleEdit}>
                Edit formula
              </Button>
            </div>
          )}
          {state.operation == Operation.Edit && (
            <Form {...form}>
              <form
                onSubmit={form.handleSubmit(onSubmit)}
                className="space-y-4"
              >
                <FormField
                  control={form.control}
                  name="formula"
                  render={({ field }) => (
                    <FormItem>
                      {/* <FormLabel>Formula</FormLabel> */}
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
                  >
                    Cancel
                  </Button>
                  <Button type="submit">Query</Button>
                </div>
              </form>
            </Form>
          )}
        </CardContent>
      </Card>
    </div>
  );
}
