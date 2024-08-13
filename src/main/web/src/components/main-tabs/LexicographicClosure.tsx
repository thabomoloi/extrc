import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { ResultSkeleton } from "@/components/main-tabs/ResultSkeleton";
import { Entailment } from "@/types";
import { NoResults } from "./NoResults";
import { TexFormula } from "../latex/TexFormula";
import { texFormula, unionRanks } from "@/lib/latex";
import { entailResult } from "../latex/helpers";
import { RankingTable } from "./tables/RankingTable";
import { SubsetTable } from "./tables/SubsetTable";
import { QueryInputContainer } from "./common/query-input";
import { LexEntailment } from "./lex/LexEntailment";

interface LexicographicClosureProps {
  isLoading: boolean;
  lexicalEntailment: Entailment | null;
}

function LexicographicClosure({
  isLoading,
  lexicalEntailment,
}: LexicographicClosureProps): JSX.Element {
  const subset = lexicalEntailment?.subsets
    .map((subset, index) => ({ ...subset, subsetNumber: index }))
    .filter((subset) => {
      if (subset.rankNumber == lexicalEntailment.removedRanking.length - 1) {
        const set1 = new Set(
          lexicalEntailment.removedRanking[subset.rankNumber].formulas
        );
        for (let i = 0; i < subset.formulas.length; i++) {
          if (set1.has(subset.formulas[i])) return false;
        }
        return true;
      }
      return false;
    });

  return (
    <Card className="w-full h-full">
      <CardHeader>
        <CardTitle className="text-lg font-bold">
          Lexicographic Closure
        </CardTitle>
        <CardDescription>
          Using lexicographic closure algorithm to check for entailment.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {!isLoading && lexicalEntailment && (
          <div>
            <QueryInputContainer
              knowledgeBase={lexicalEntailment.knowledgeBase}
              queryFormula={lexicalEntailment.queryFormula}
            />
            <div className="my-6">
              <p>
                Lexicographic closure starts with the initial rankings
                constructed by the Base Rank algorithm.
              </p>
              <RankingTable ranking={lexicalEntailment.baseRanking} />
            </div>
            <LexEntailment
              entailment={lexicalEntailment}
              className="mb-6 space-y-4"
            />

            <div className="mb-6 space-y-4">
              {/* <p>
                Begin by verifying whether the ranks entail{" "}
                <TexFormula>
                  {texFormula(lexicalEntailment.negation)}
                </TexFormula>
                . If they do, start removing formulas one at a time until there
                are no inconsistencies. If all formulas from the lowest rank
                have been removed, move to the next rank and repeat the process.
                If the ranks do not entail{" "}
                <TexFormula>
                  {texFormula(lexicalEntailment.negation)}
                </TexFormula>
                , proceed to check if the remaining ranks entail{" "}
                <TexFormula>
                  {texFormula(lexicalEntailment.queryFormula)}
                </TexFormula>
                .
              </p>
              <div className="text-center space-y-4">
                {lexicalEntailment.removedRanking.map((value, index, array) => (
                  <div key={value.rankNumber} className="space-y-3">
                    {index == 2 && index < array.length - 1 && (
                      <TexFormula>{"\\vdots"}</TexFormula>
                    )}
                    {(index < 1 || index == array.length - 1) && (
                      <div>
                        <div className="mb-3">
                          <TexFormula>
                            {`R_\\infty${unionRanks({
                              start: value.rankNumber,
                              ranks: lexicalEntailment.baseRanking,
                            })}\\models ${texFormula(
                              lexicalEntailment.negation
                            )}`}
                          </TexFormula>
                          {". "}
                          {value.formulas.length == 1 && (
                            <span>
                              Remove <TexFormula>{`R_{${index}}`}</TexFormula>.
                            </span>
                          )}
                        </div>

                        {value.formulas.length > 0 && (
                          <div className="text-left ml-8">
                            <p>
                              Consider the possible formulas of{" "}
                              <TexFormula>{`R_{${index}}`}</TexFormula> denoted
                              by set{" "}
                              <TexFormula>{`S_k^{R_{${index}}}`}</TexFormula>{" "}
                              where{" "}
                              <TexFormula>{`S_{k}^{R_{${index}}} \\subset R_{${index}}`}</TexFormula>
                              :
                            </p>
                            <SubsetTable
                              negation={lexicalEntailment.negation}
                              rankNumber={value.rankNumber}
                              baseRanks={lexicalEntailment.baseRanking}
                              removedRanks={lexicalEntailment.removedRanking}
                              subsets={lexicalEntailment.subsets}
                            />
                          </div>
                        )}
                      </div>
                    )}
                  </div>
                ))}
                {subset &&
                  subset.length == 0 &&
                  lexicalEntailment.removedRanking.length <
                    lexicalEntailment.baseRanking.length - 1 && (
                    <p>
                      <TexFormula>
                        {`R_\\infty${unionRanks({
                          start: lexicalEntailment.removedRanking.length - 1,
                          ranks: lexicalEntailment.baseRanking,
                        })}\\not\\models ${texFormula(
                          lexicalEntailment.negation
                        )}`}
                      </TexFormula>
                    </p>
                  )}
                {subset && subset.length == 1 && (
                  <p>
                    <TexFormula>
                      {`R_\\infty\\cup S_{${subset[0].subsetNumber}}^{{R_{${
                        subset[0].rankNumber
                      }}}}${unionRanks({
                        start: lexicalEntailment.removedRanking.length,
                        ranks: lexicalEntailment.baseRanking,
                      })}\\not\\models ${texFormula(
                        lexicalEntailment.negation
                      )}`}
                    </TexFormula>
                  </p>
                )}
                <div>
                  <RankingTable
                    ranking={lexicalEntailment.removedRanking}
                    caption="Ranks removed by lexicographic closure."
                  />
                </div>
              </div>

              <div className="space-y-4">
                <p>
                  Now we check if{" "}
                  <TexFormula>
                    {`R_\\infty${unionRanks({
                      start: lexicalEntailment.removedRanking.length,
                      ranks: lexicalEntailment.baseRanking,
                    })}`}
                  </TexFormula>{" "}
                  entails the formula{" "}
                  <TexFormula>
                    {texFormula(lexicalEntailment.queryFormula)}
                  </TexFormula>
                  .
                </p>
                <p>
                  If follows that{" "}
                  <TexFormula>
                    {`R_\\infty${unionRanks({
                      start: lexicalEntailment.removedRanking.length,
                      ranks: lexicalEntailment.baseRanking,
                    })} ${
                      lexicalEntailment.entailed ? "\\models" : "\\not\\models"
                    }${texFormula(lexicalEntailment.queryFormula)}`}
                  </TexFormula>
                  .
                </p>
                <p>Therefore {entailResult(lexicalEntailment)}.</p>
              </div> */}
            </div>
          </div>
        )}
        {isLoading && <ResultSkeleton />}
        {!isLoading && !lexicalEntailment && <NoResults />}
      </CardContent>
    </Card>
  );
}

export { LexicographicClosure };
