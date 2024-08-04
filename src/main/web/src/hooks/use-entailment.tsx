import { ToastAction } from "@/components/ui/toast";
import { useToast } from "@/components/ui/use-toast";
import {
  LEXICAL_ENTAILMENT_URL,
  RATIONAL_ENTAILMENT_URL,
} from "@/lib/api-urls";
import axios from "axios";
import { useCallback, useState } from "react";

export interface EntailmentResult {
  queryFormula: string;
  negation: string;
  knowledgeBase: string;
  entailed: boolean;
  sequence: string[];
  baseRanking: Ranking[];
  removedRanking: Ranking[];
  subsets: Ranking[];
  times: TimeEntry[];
}

export interface Ranking {
  rankNumber: string;
  formulas: string;
}

export interface TimeEntry {
  title: string;
  timeTaken: number;
}

export function useEntailment() {
  const { toast } = useToast();
  const [rationaEntailment, setRationalEntailment] =
    useState<EntailmentResult>();

  const [lexicalEntailment, setLexicalEntailment] =
    useState<EntailmentResult>();

  const serverErrorToast = useCallback(() => {
    toast({
      variant: "destructive",
      title: "Uh oh! Something went wrong.",
      description: "There was a problem with your request.",
      action: <ToastAction altText="Try again">Try again</ToastAction>,
    });
  }, [toast]);

  const fetchRationalEntailment = useCallback(async () => {
    try {
      const response = await axios.get(RATIONAL_ENTAILMENT_URL);
      setRationalEntailment(response.data as EntailmentResult);
    } catch {
      serverErrorToast();
    }
  }, [serverErrorToast]);

  const fetchLexicalEntailment = useCallback(async () => {
    try {
      const response = await axios.get(LEXICAL_ENTAILMENT_URL);
      setLexicalEntailment(response.data as EntailmentResult);
    } catch {
      serverErrorToast();
    }
  }, [serverErrorToast]);

  return {
    fetchRationalEntailment,
    fetchLexicalEntailment,
    rationaEntailment,
    lexicalEntailment,
  };
}
