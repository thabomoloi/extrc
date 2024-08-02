import { zodResolver } from "@hookform/resolvers/zod";
import axios from "axios";
import { useState } from "react";
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
import { Button } from "@/components/ui/button";
import { useToast } from "@/components/ui/use-toast";
import { ToastAction } from "@/components/ui/toast";

const formSchema = z.object({
  formula: z.string().min(1, {
    message: "Formula is required.",
  }),
});

export default function QueryCard() {
  const { toast } = useToast();

  const [state, setState] = useState<{
    formula: string;
    operation: Operation;
    updating: boolean;
  }>({
    operation: Operation.View,
    formula: "p~>f",
    updating: false,
  });

  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: { formula: state.formula },
  });

  const onSubmit = async (values: z.infer<typeof formSchema>) => {
    setState((prevState) => ({
      ...prevState,
      updating: true,
    }));

    const isValid = await validateFormula(values.formula);

    setState((prevState) => ({
      ...prevState,
      updating: false,
    }));

    if (isValid == false) {
      form.setError("formula", {
        type: "manual",
        message: "The query formula is invalid",
      });
    } else if (isValid == true) {
      setState((prevState) => ({
        ...prevState,
        operation: Operation.View,
        formula: values.formula,
      }));
    }
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

  const validateFormula = async (formula: string) => {
    try {
      const response = await axios.get(`/api/validate/query/${formula}`);
      console.log("response", response.data.valid);
      return response.data.valid as boolean;
    } catch (error) {
      console.error(error);
      toast({
        variant: "destructive",
        title: "Uh oh! Something went wrong.",
        description: "There was a problem with your request.",
        action: <ToastAction altText="Try again">Try again</ToastAction>,
      });
      return null;
    }
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
          {state.operation == Operation.View && (
            <div className="w-full flex flex-col gap-4 items-center">
              <TexFormula>{texFormula(state.formula)}</TexFormula>
              <div className="grid grid-cols-2 gap-4 w-full max-w-sm">
                <Button
                  variant="secondary"
                  onClick={handleEdit}
                  disabled={state.updating}
                >
                  Edit
                </Button>
                <Button type="submit" disabled={state.updating}>
                  Query
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
                    disabled={state.updating}
                  >
                    Cancel
                  </Button>
                  <Button type="submit" disabled={state.updating}>
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
