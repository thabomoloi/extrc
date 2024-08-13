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
    try {
      clearData();
      const response = await axios.get(QUERY_URL);
      const data = response.data as QueryInput;
      setQueryInput(data);
      localStorage.setItem("queryInput", JSON.stringify(data));
    } catch (error) {
      console.error(error);
      serverErrorToast();
    }
  }, [serverErrorToast]);

  const updateQueryInput = async (data: QueryInput) => {
    setQueryInputPending(true);
    try {
      const response = await axios.post(QUERY_URL, {
        ...data,
        knowledgeBase: data.knowledgeBase.filter((item) => item.trim() != ""),
      });
      const updatedData = response.data as QueryInput;
      clearData();
      setQueryInput(updatedData);
      localStorage.setItem("queryInput", JSON.stringify(updatedData));
    } catch (error) {
      console.error(error);
      toast({
        variant: "destructive",
        title: "Invalid inputs.",
        description: "Invalid query formula or knowledge base.",
        action: <ToastAction altText="Try again">Try again</ToastAction>,
      });
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
      clearData();

      setQueryInput(updatedData);
      localStorage.setItem("queryInput", JSON.stringify(updatedData));
    } catch (error) {
      console.error(error);
      toast({
        variant: "destructive",
        title: "Invalid knowledge base.",
        description: "At least one formula in your knowledge base is invalid.",
        action: <ToastAction altText="Try again">Try again</ToastAction>,
      });
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
      localStorage.setItem("baseRank", JSON.stringify(brData));
      localStorage.setItem("rationalEntailment", JSON.stringify(rcData));
      localStorage.setItem("lexicalEntailment", JSON.stringify(lcData));
    } catch (error) {
      console.error(error);
      // clearData()
      serverErrorToast();
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
