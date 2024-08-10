/* eslint-disable @typescript-eslint/no-explicit-any */
import { BaseRanking, Entailment, QueryInput, Ranking } from "@/types";

const createQueryInput = (data: any): QueryInput => {
  return {
    queryFormula: data?.queryFormula,
    knowledgeBase: data?.knowledgeBase,
  };
};

const createRanking = (data: any): Ranking[] => {
  return data?.map((rank: { rankNumber: number; formulas: string[] }) => {
    return {
      rankNumber:
        rank.rankNumber == -1 ? Number.MAX_SAFE_INTEGER : rank.rankNumber,
      formulas: rank.formulas,
    };
  });
};

const createBaseRanking = (data: any): BaseRanking => {
  return {
    queryInput: createQueryInput(data?.queryInput),
    ranking: createRanking(data?.ranking),
    sequence: createRanking(data?.sequence),
    timeTaken: data?.timeTaken,
  };
};

const createEntailment = (data: any): Entailment => {
  return {
    queryFormula: data?.queryFormula,
    knowledgeBase: data?.knowledgeBase,
    entailed: data?.entailed,
    baseRanking: createRanking(data?.baseRanking),
    removedRanking: createRanking(data?.removedRanking),
    subsets: createRanking(data?.subsets),
    timeTaken: data?.timeTaken,
  };
};

export { createQueryInput, createRanking, createBaseRanking, createEntailment };
