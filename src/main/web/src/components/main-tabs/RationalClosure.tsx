import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { ResultSkeleton } from "@/components/main-tabs/ResultSkeleton";
import { NoResults } from "./NoResults";
import { TexFormula } from "../latex/TexFormula";
import { RankingTable } from "./tables/RankingTable";
import { texFormula, unionRanks } from "@/lib/latex";
import { entailResult, kb } from "../latex/helpers";
import { Entailment } from "@/types";

interface RationalClosureProps {
  isLoading: boolean;
  rationalEntailment: Entailment | null;
}

function RationalClosure({
  isLoading,
  rationalEntailment,
}: RationalClosureProps): JSX.Element {
  return (
    <Card className="w-full h-full">
      <CardHeader>
        <CardTitle className="text-lg font-bold">Rational Closure</CardTitle>
        <CardDescription>
          Using rational closure algorithm to check for entailment.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {!isLoading && rationalEntailment && (
          <div>
            {kb({ formulas: rationalEntailment.knowledgeBase, set: true })}
            <div className="mb-6">
              <TexFormula>
                {texFormula("\\alpha = " + rationalEntailment.queryFormula)}
              </TexFormula>
            </div>
            <div className="mb-6">
              <p>
                Rational closure starts with the initial rankings constructed by
                the Base Rank algorithm.
              </p>
              <RankingTable ranking={rationalEntailment.baseRanking} />
            </div>
            <div className="mb-6 space-y-4">
              <p>
                Begin by verifying whether the ranks entail{" "}
                <TexFormula>
                  {texFormula(rationalEntailment.negation)}
                </TexFormula>
                . If they do, remove the lowest rank, then move to the next rank
                and repeat the process. If they do not, check if the remaining
                ranks entail the formula{" "}
                <TexFormula>
                  {texFormula(rationalEntailment.queryFormula)}
                </TexFormula>
                .
              </p>

              <div className="text-center space-y-4">
                {rationalEntailment.removedRanking.map(
                  (value, index, array) => (
                    <div key={value.rankNumber} className="space-y-3">
                      {index == 2 && index < array.length - 1 && (
                        <TexFormula>{"\\vdots"}</TexFormula>
                      )}
                      {(index < 2 || index == array.length - 1) && (
                        <p>
                          <TexFormula>
                            {`R_\\infty${unionRanks({
                              start: value.rankNumber,
                              ranks: rationalEntailment.baseRanking,
                            })}\\models ${texFormula(
                              rationalEntailment.negation
                            )}`}
                          </TexFormula>
                          . Remove <TexFormula>{`R_{${index}}`}</TexFormula>.
                        </p>
                      )}
                    </div>
                  )
                )}
                {rationalEntailment.removedRanking.length <
                  rationalEntailment.baseRanking.length - 1 && (
                  <p>
                    <TexFormula>
                      {`R_\\infty${unionRanks({
                        start: rationalEntailment.removedRanking.length,
                        ranks: rationalEntailment.baseRanking,
                      })}\\not\\models ${texFormula(
                        rationalEntailment.negation
                      )}`}
                    </TexFormula>
                  </p>
                )}
                <div>
                  <RankingTable
                    ranking={rationalEntailment.removedRanking}
                    caption="Ranks removed by rational closure."
                  />
                </div>
              </div>

              <div className="space-y-4">
                <p>
                  Now we check if{" "}
                  <TexFormula>
                    {`R_\\infty${unionRanks({
                      start: rationalEntailment.removedRanking.length,
                      ranks: rationalEntailment.baseRanking,
                    })}`}
                  </TexFormula>{" "}
                  entails the formula{" "}
                  <TexFormula>
                    {texFormula(rationalEntailment.queryFormula)}
                  </TexFormula>
                  .
                </p>
                <p>
                  If follows that{" "}
                  <TexFormula>
                    {`R_\\infty${unionRanks({
                      start: rationalEntailment.removedRanking.length,
                      ranks: rationalEntailment.baseRanking,
                    })} ${
                      rationalEntailment.entailed ? "\\models" : "\\not\\models"
                    }${texFormula(rationalEntailment.queryFormula)}`}
                  </TexFormula>
                  .
                </p>
                <p>Therefore {entailResult(rationalEntailment)}.</p>
              </div>
            </div>
          </div>
        )}
        {isLoading && <ResultSkeleton />}
        {!isLoading && !rationalEntailment && <NoResults />}
      </CardContent>
    </Card>
  );
}

export { RationalClosure };
