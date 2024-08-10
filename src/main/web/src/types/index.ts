export interface QueryInput {
  queryFormula: string;
  knowledgeBase: string[];
}

export interface Ranking {
  rankNumber: number;
  formulas: string[];
}
export interface BaseRanking {
  queryInput: QueryInput;
  ranking: Ranking[];
  sequence: Ranking[];
  timeTaken: number;
}

export interface Entailment {
  queryFormula: string;
  knowledgeBase: string[];
  entailed: boolean;
  baseRanking: Ranking[];
  removedRanking: Ranking[];
  subsets: Ranking[];
  timeTaken: number;
}
