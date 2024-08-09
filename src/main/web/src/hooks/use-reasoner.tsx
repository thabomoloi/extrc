import { ToastAction } from "@/components/ui/toast";
import { useToast } from "@/components/ui/use-toast";
import { BaseRanking, Entailment, QueryInput } from "@/types";
import axios from "axios";
import { useCallback, useEffect, useState, useTransition } from "react";

const QUERY_URL = "/api/query";
const KBFILE_URL = "/api/query/file";
const BASE_RANK_URL = "/api/base-rank";
const ENTAILMENT_URL = (reasoner: string) => `/api/entailment/${reasoner}`;

export function useReasoner() {
  const { toast } = useToast();

  const [isPending, startTransition] = useTransition();
  const [queryInput, setQueryInput] = useState<QueryInput | null>(() => {
    const storedQueryInput = localStorage.getItem("queryInput");
    return storedQueryInput ? JSON.parse(storedQueryInput) : null;
  });

  const [baseRank, setBaseRank] = useState<BaseRanking | null>(() => {
    const storedBaseRank = localStorage.getItem("baseRank");
    return storedBaseRank ? JSON.parse(storedBaseRank) : null;
  });

  const [rationalEntailment, setRationalEntailment] =
    useState<Entailment | null>(() => {
      const storedRationalEntailment =
        localStorage.getItem("rationalEntailment");
      return storedRationalEntailment
        ? JSON.parse(storedRationalEntailment)
        : null;
    });

  const [lexicalEntailment, setLexicalEntailment] = useState<Entailment | null>(
    () => {
      const storedLexicalEntailment = localStorage.getItem("lexicalEntailment");
      return storedLexicalEntailment
        ? JSON.parse(storedLexicalEntailment)
        : null;
    }
  );

  const serverErrorToast = useCallback(() => {
    toast({
      variant: "destructive",
      title: "Uh oh! Something went wrong.",
      description: "There was a problem with your request.",
      action: <ToastAction altText="Try again">Try again</ToastAction>,
    });
  }, [toast]);

  const clearData = useCallback(() => {
    setBaseRank(null);
    setRationalEntailment(null);
    setLexicalEntailment(null);
    localStorage.removeItem("baseRank");
    localStorage.removeItem("rationalEntailment");
    localStorage.removeItem("lexicalEntailment");
  }, []);

  const fetchQueryInput = useCallback(() => {
    startTransition(() => {
      axios
        .get(QUERY_URL)
        .then((response) => {
          const data = response.data as QueryInput;
          setQueryInput(data);
          localStorage.setItem("queryInput", JSON.stringify(data));
        })
        .catch((error) => {
          console.error(error);
          setQueryInput(null);
          localStorage.removeItem("queryInput");
          serverErrorToast();
        });
    });
  }, [serverErrorToast]);

  useEffect(() => {
    if (queryInput == null) {
      fetchQueryInput();
    }
  }, [fetchQueryInput, queryInput]);

  const updateQueryInput = useCallback(
    (data: QueryInput) => {
      startTransition(() => {
        axios
          .post(QUERY_URL, data)
          .then((response) => {
            const data = response.data as QueryInput;
            setQueryInput(data);
            localStorage.setItem("queryInput", JSON.stringify(data));
          })
          .catch((error) => {
            console.error(error);
            setQueryInput(null);
            localStorage.removeItem("queryInput");
            serverErrorToast();
          });
      });
    },
    [serverErrorToast]
  );

  const uploadKnowledgeBase = useCallback(
    (data: FormData) => {
      startTransition(() => {
        axios
          .post(KBFILE_URL, data, {
            headers: { "Content-Type": "multipart/form-data" },
          })
          .then((response) => {
            const data = response.data as QueryInput;
            setQueryInput(data);
            localStorage.setItem("queryInput", JSON.stringify(data));
          })
          .catch((error) => {
            console.error(error);
            setQueryInput(null);
            localStorage.removeItem("queryInput");
            clearData();
            serverErrorToast();
          });
      });
    },
    [clearData, serverErrorToast]
  );

  const fetchBaseRank = useCallback(() => {
    startTransition(() => {
      axios
        .post(BASE_RANK_URL, queryInput)
        .then((response) => {
          const data = response.data as BaseRanking;
          setBaseRank(data);
          localStorage.setItem("baseRank", JSON.stringify(data));
        })
        .catch((error) => {
          console.error(error);
          setBaseRank(null);
          localStorage.removeItem("baseRank");
          serverErrorToast();
        });
    });
  }, [queryInput, serverErrorToast]);

  const fetchRationalEntailment = useCallback(() => {
    startTransition(() => {
      axios
        .post(ENTAILMENT_URL("rational"), baseRank)
        .then((response) => {
          const data = response.data as Entailment;
          setRationalEntailment(data);
          localStorage.setItem("rationalEntailment", JSON.stringify(data));
        })
        .catch((error) => {
          console.error(error);
          setRationalEntailment(null);
          localStorage.removeItem("rationalEntailment");
          serverErrorToast();
        });
    });
  }, [baseRank, serverErrorToast]);

  const fetchLexicalEntailment = useCallback(() => {
    startTransition(() => {
      axios
        .post(ENTAILMENT_URL("lexical"), baseRank)
        .then((response) => {
          const data = response.data as Entailment;
          setLexicalEntailment(data);
          localStorage.setItem("lexicalEntailment", JSON.stringify(data));
        })
        .catch((error) => {
          console.error(error);
          setLexicalEntailment(null);
          localStorage.removeItem("lexicalEntailment");
          serverErrorToast();
        });
    });
  }, [baseRank, serverErrorToast]);

  const submitKnowledgeBase = useCallback(
    (knowledgeBase: string[]) => {
      if (queryInput) {
        const data: QueryInput = {
          ...queryInput,
          knowledgeBase,
        };
        updateQueryInput(data);
      }
    },
    [queryInput, updateQueryInput]
  );

  const updateFormula = useCallback(
    (queryFormula: string) => {
      if (queryInput) {
        const data: QueryInput = {
          ...queryInput,
          queryFormula,
        };
        updateQueryInput(data);
      }
    },
    [queryInput, updateQueryInput]
  );

  const submitQuery = useCallback(() => {
    if (!isPending && queryInput) {
      fetchBaseRank();
      if (!isPending && baseRank) {
        fetchRationalEntailment();
        fetchLexicalEntailment();
      }
    }
  }, [
    baseRank,
    fetchBaseRank,
    fetchLexicalEntailment,
    fetchRationalEntailment,
    isPending,
    queryInput,
  ]);

  useEffect(() => {
    fetchQueryInput();
  }, [fetchQueryInput]);

  return {
    isPending,
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
