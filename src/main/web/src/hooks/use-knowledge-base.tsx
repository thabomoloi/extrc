import { ToastAction } from "@/components/ui/toast";
import { useToast } from "@/components/ui/use-toast";
import {
  GET_KB_URL,
  GET_QUERY_URL,
  VALIDATE_DEFAULT_KB_URL,
  VALIDATE_DEFAULT_QUERY_URL,
  VALIDATE_FILE_URL,
  VALIDATE_KB_URL,
  VALIDATE_QUERY_URL,
} from "@/lib/api-urls";
import axios from "axios";
import { useCallback } from "react";

export function useKnowledgeBase() {
  const { toast } = useToast();

  const serverErrorToast = useCallback(() => {
    toast({
      variant: "destructive",
      title: "Uh oh! Something went wrong.",
      description: "There was a problem with your request.",
      action: <ToastAction altText="Try again">Try again</ToastAction>,
    });
  }, [toast]);

  const fetchQueryFormula = useCallback(async () => {
    try {
      // Get formula in server session
      let response = await axios.get(GET_QUERY_URL);
      if (response.data.formula == null) {
        // Set default formula
        await axios.get(VALIDATE_DEFAULT_QUERY_URL);
        response = await axios.get(GET_QUERY_URL);
      }
      return response.data.formula as string;
    } catch {
      serverErrorToast();
    }
  }, [serverErrorToast]);

  const validateQueryFormula = useCallback(
    async (formula: string) => {
      try {
        // Validate the formula
        const response = await axios.get(`${VALIDATE_QUERY_URL}/${formula}`);
        if (response.data.valid == true) {
          return true;
        }
        return false;
      } catch {
        serverErrorToast();
      }
    },
    [serverErrorToast]
  );

  const fetchKnowledgeBase = useCallback(async () => {
    try {
      // Get knowledge base in server session
      let response = await axios.get(GET_KB_URL);
      if (response.data.formulas == null) {
        // Set default knowledge base
        await axios.get(VALIDATE_DEFAULT_KB_URL);
        response = await axios.get(GET_KB_URL);
      }
      return response.data.formulas as string;
    } catch {
      serverErrorToast();
    }
  }, [serverErrorToast]);

  const validateKnowledgeBase = useCallback(
    async (formulas: string) => {
      try {
        // Validate the formula
        const response = await axios.get(`${VALIDATE_KB_URL}/${formulas}`);
        if (response.data.valid == true) {
          return true;
        }
        return false;
      } catch {
        serverErrorToast();
      }
    },
    [serverErrorToast]
  );

  const validateKnowledgeBaseFile = useCallback(
    async (formData: FormData) => {
      try {
        // Validate the fike
        const response = await axios.post(VALIDATE_FILE_URL, formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        if (response.data.valid == true) {
          return true;
        }
        return false;
      } catch {
        serverErrorToast();
      }
    },
    [serverErrorToast]
  );

  return {
    fetchKnowledgeBase,
    fetchQueryFormula,
    validateQueryFormula,
    validateKnowledgeBase,
    validateKnowledgeBaseFile,
  };
}
