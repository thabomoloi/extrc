import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { ResultSkeleton } from "@/components/main-tabs/ResultSkeleton";
import { BaseRanking } from "@/types";
import { NoResults } from "./NoResults";
import { kb } from "../latex/helpers";
import { TexFormula } from "../latex/TexFormula";
import { RankingTable, SequenceTable } from "./tables/RankingTable";
import { QueryInputContainer } from "./common/query-input";

interface BaseRankProps {
  isLoading: boolean;
  baseRank: BaseRanking | null;
}

function BaseRank({ isLoading, baseRank }: BaseRankProps): JSX.Element {
  return (
    <Card className="w-full h-full">
      <CardHeader>
        <CardTitle className="text-lg font-bold">Base Rank</CardTitle>
        <CardDescription>
          Using base rank algorithm to create initial ranks.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {!isLoading && baseRank && (
          <div>
            <QueryInputContainer
              knowledgeBase={baseRank.queryInput.knowledgeBase}
              queryFormula={baseRank.queryInput.queryFormula}
              queryFormulaHidden
            />
            <div className="my-6 space-y-3">
              <p>
                Let <TexFormula>{"\\mathcal{K}_C"}</TexFormula> be the knowledge
                base of all classical statements. Then,{" "}
              </p>
              <div className="text-center">
                {kb({
                  name: "\\mathcal{K}_C",
                  set: true,
                  formulas:
                    baseRank.ranking[baseRank.ranking.length - 1].formulas,
                })}
              </div>
              <div>
                <p>
                  The exceptionality sequence <TexFormula>{"*"}</TexFormula> for
                  knowledge base <TexFormula>{"\\mathcal{K}"}</TexFormula> is
                  given by:
                </p>
                <ul className="ml-8 list-disc ">
                  <li>
                    <TexFormula>
                      {
                        "*_0^\\mathcal{K}=\\{\\alpha\\vsim\\beta\\in\\mathcal{K}\\}"
                      }
                    </TexFormula>{" "}
                    and
                  </li>
                  <li>
                    <TexFormula>
                      {
                        "*_{i+1}^\\mathcal{K}=\\{\\alpha\\vsim\\beta \\in *_{i}^\\mathcal{K} \\mid \\mathcal{K}_C\\cup \\overrightarrow{*_{i}^\\mathcal{K}}\\models\\neg\\alpha\\}"
                      }
                    </TexFormula>
                  </li>
                  <li>
                    for <TexFormula>{"0\\leq i < n"}</TexFormula>, where{" "}
                    <TexFormula>{"n"}</TexFormula> is the smallest integer such
                    that{" "}
                    <TexFormula>
                      {"*_n^\\mathcal{K}=*_{n+1}^\\mathcal{K}"}
                    </TexFormula>
                    .
                  </li>
                  <li>
                    Final element <TexFormula>{"*_n^\\mathcal{K}"}</TexFormula>{" "}
                    is usually denoted as{" "}
                    <TexFormula>{"*_\\infty^\\mathcal{K}"}</TexFormula>. (It is
                    unique.)
                  </li>
                </ul>
                <SequenceTable ranking={baseRank.sequence} />
              </div>
              <div>
                <p>
                  The initial ranks for knowledge base{" "}
                  <TexFormula>{"\\mathcal{K}"}</TexFormula> are defined by:
                </p>
                <ul className="ml-8 list-disc ">
                  <li>
                    Finite rank:{" "}
                    <TexFormula>
                      {"R_i = *_i^\\mathcal{K}\\setminus *_{i+1}^\\mathcal{K}"}
                    </TexFormula>{" "}
                    <span className="ml-4">
                      (for <TexFormula>{"0\\leq i < n"}</TexFormula>)
                    </span>
                  </li>
                  <li>
                    Infinite rank:{" "}
                    <TexFormula>
                      {
                        "R_\\infty = \\mathcal{K}_C \\cup *_\\infty^\\mathcal{K}"
                      }
                    </TexFormula>
                  </li>
                </ul>
                <RankingTable ranking={baseRank.ranking} />
              </div>
            </div>
          </div>
        )}
        {isLoading && <ResultSkeleton />}
        {!isLoading && !baseRank && <NoResults />}
      </CardContent>
    </Card>
  );
}

export { BaseRank };
