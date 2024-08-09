export interface QueryInput {
  queryFormula: string;
  knowledgeBase: string[];
}

export interface BaseRanking {
  queryInput: QueryInput;
  ranking: string[];
  sequence: string[];
  timeTaken: number;
}

export interface Entailment {
  queryFormula: string;
  knowledgeBase: string[];
  entailed: boolean;
  baseRanking: string[];
  removedRanking: string[];
  subsets: string[];
  timeTaken: number;
}
