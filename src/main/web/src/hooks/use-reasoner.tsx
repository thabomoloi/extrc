import { ToastAction } from "@/components/ui/toast";
import { useToast } from "@/components/ui/use-toast";
import { BaseRanking, Entailment, QueryInput } from "@/types";
import axios from "axios";
import { useCallback, useEffect, useState } from "react";

const QUERY_URL = "/api/query";
const KBFILE_URL = "/api/query/file";
const BASE_RANK_URL = "/api/base-rank";
const ENTAILMENT_URL = (reasoner: string) => `/api/entailment/${reasoner}`;

export function useReasoner() {
  const { toast } = useToast();
  const [queryInputPending, setQueryInputPending] = useState(false);
  const [resultsPending, setResultsPending] = useState(false);

  const [queryInput, setQueryInput] = useState<QueryInput | null>(
    initializeState("queryInput")
  );
  const [baseRank, setBaseRank] = useState<BaseRanking | null>(
    initializeState("baseRank")
  );
  const [rationalEntailment, setRationalEntailment] =
    useState<Entailment | null>(initializeState("rationalEntailment"));
  const [lexicalEntailment, setLexicalEntailment] = useState<Entailment | null>(
    initializeState("lexicalEntailment")
  );

  function initializeState<T>(key: string): T | null {
    const storedValue = localStorage.getItem(key);
    return storedValue ? JSON.parse(storedValue) : null;
  }

  const serverErrorToast = useCallback(() => {
    toast({
      variant: "destructive",
      title: "Uh oh! Something went wrong.",
      description: "There was a problem with your request.",
      action: <ToastAction altText="Try again">Try again</ToastAction>,
    });
  }, [toast]);

  const clearData = () => {
    setBaseRank(null);
    setRationalEntailment(null);
    setLexicalEntailment(null);
    localStorage.removeItem("baseRank");
    localStorage.removeItem("rationalEntailment");
    localStorage.removeItem("lexicalEntailment");
  };

  const fetchQueryInput = useCallback(async () => {
    clearData();
    try {
      const response = await axios.get(QUERY_URL);
      const data = response.data as QueryInput;
      setQueryInput(data);
      localStorage.setItem("queryInput", JSON.stringify(data));
    } catch (error) {
      console.error(error);
      setQueryInput(null);
      localStorage.removeItem("queryInput");
      serverErrorToast();
    }
  }, [serverErrorToast]);

  const updateQueryInput = async (data: QueryInput) => {
    setQueryInputPending(true);
    clearData();
    try {
      const response = await axios.post(QUERY_URL, data);
      const updatedData = response.data as QueryInput;
      setQueryInput(updatedData);
      localStorage.setItem("queryInput", JSON.stringify(updatedData));
    } catch (error) {
      console.error(error);
      setQueryInput(null);
      localStorage.removeItem("queryInput");
      serverErrorToast();
    }
    setQueryInputPending(false);
  };

  const uploadKnowledgeBase = async (data: FormData) => {
    setQueryInputPending(true);
    try {
      const response = await axios.post(KBFILE_URL, data, {
        headers: { "Content-Type": "multipart/form-data" },
      });
      const updatedData = response.data as QueryInput;
      setQueryInput(updatedData);
      localStorage.setItem("queryInput", JSON.stringify(updatedData));
    } catch (error) {
      console.error(error);
      setQueryInput(null);
      localStorage.removeItem("queryInput");
      clearData();
      serverErrorToast();
    }
    setQueryInputPending(false);
  };

  const submitKnowledgeBase = (knowledgeBase: string[]) => {
    if (queryInput) {
      const data: QueryInput = { ...queryInput, knowledgeBase };
      updateQueryInput(data);
    }
  };

  const updateFormula = (queryFormula: string) => {
    if (queryInput) {
      const data: QueryInput = { ...queryInput, queryFormula };
      updateQueryInput(data);
    }
  };

  const submitQuery = async () => {
    if (!queryInput) return;
    setResultsPending(true);
    try {
      let response = await axios.post(BASE_RANK_URL, queryInput);
      const brData = response.data as BaseRanking;
      response = await axios.post(ENTAILMENT_URL("rational"), brData);
      const rcData = response.data as Entailment;
      response = await axios.post(ENTAILMENT_URL("lexical"), brData);
      const lcData = response.data as Entailment;
      setBaseRank(brData);
      setRationalEntailment(rcData);
      setLexicalEntailment(lcData);
    } catch (error) {
      console.error(error);
      clearData();
    }
    setResultsPending(false);
  };

  useEffect(() => {
    if (queryInput == null) {
      fetchQueryInput();
    }
  }, [fetchQueryInput, queryInput]);

  return {
    queryInputPending,
    resultsPending,
    queryInput,
    baseRank,
    rationalEntailment,
    lexicalEntailment,
    clearData,
    submitKnowledgeBase,
    uploadKnowledgeBase,
    submitQuery,
    updateFormula,
  };
}
