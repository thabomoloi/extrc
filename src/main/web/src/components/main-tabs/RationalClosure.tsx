import { EntailmentResult } from "@/hooks/use-entailment";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "../ui/card";
import NoResults from "../no-results";
import { texFormula } from "@/lib/utils";
import KnowledgeBase from "../reasoning/KnowledgeBase";
import TexFormula from "../TexFormula";
import RankingTable from "../reasoning/RankingTable";

interface RationalClosureProps {
  rationalEntailment: EntailmentResult | undefined;
}
export default function RationalClosure({
  rationalEntailment,
}: RationalClosureProps) {
  const getResult = ({ entailed, queryFormula }: EntailmentResult) => {
    return entailed
      ? texFormula("\\mathcal{K} \\vapprox " + queryFormula)
      : texFormula("\\mathcal{K} \\nvapprox " + queryFormula);
  };

  return (
    <Card className="w-full h-full">
      <CardHeader>
        <CardTitle>Rational Closure</CardTitle>
        <CardDescription>
          Using rational closure algorithm to check for entailment.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {rationalEntailment && (
          <div>
            <KnowledgeBase knowledgeBase={rationalEntailment.knowledgeBase} />
            <div className="mb-6">
              <TexFormula>{`\\alpha = ${texFormula(
                rationalEntailment.queryFormula
              )}`}</TexFormula>
            </div>
            <div className="mb-6">
              <p>
                Rational closure starts with the initial rankings constructed by
                the Base Rank algorithm.
              </p>
              <RankingTable ranking={rationalEntailment.baseRanking} />
            </div>
            <div className="mb-6">
              <p className="mb-4">
                Check if the ranks entail{" "}
                <TexFormula>
                  {texFormula(rationalEntailment.negation)}
                </TexFormula>
                . If they do remove the lowest rank, otherwise check if the
                remaining ranks entail the formula{" "}
                <TexFormula>
                  {texFormula(rationalEntailment.queryFormula)}
                </TexFormula>
                .
              </p>
              <div className="ml-8 mb-2">
                {rationalEntailment.removedRanking.map(
                  (value, index, array) => (
                    <div key={value.rankNumber + array.length}>
                      {index == 2 && index < array.length - 1 && (
                        <TexFormula>{"\\qquad\\vdots"}</TexFormula>
                      )}
                      {(index < 2 || index == array.length - 1) && (
                        <span>
                          <TexFormula>
                            {`R_\\infty\\cup \\left(\\bigcup_{j=${
                              value.rankNumber
                            }}^{j< ${
                              rationalEntailment.baseRanking.length - 1
                            }}\\overline{R_j}\\right)\\models ${texFormula(
                              rationalEntailment.negation
                            )}`}
                          </TexFormula>
                          . Remove <TexFormula>{`R_{${index}}`}</TexFormula>.
                        </span>
                      )}
                    </div>
                  )
                )}
                {rationalEntailment.removedRanking.length <
                  rationalEntailment.baseRanking.length - 1 && (
                  <div>
                    <TexFormula>
                      {`R_\\infty\\cup \\left(\\bigcup_{j=${
                        rationalEntailment.removedRanking.length
                      }}^{j< ${
                        rationalEntailment.baseRanking.length - 1
                      }}\\overline{R_j}\\right)\\not\\models ${texFormula(
                        rationalEntailment.negation
                      )}`}
                    </TexFormula>
                    .
                  </div>
                )}
              </div>
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
                  {`R_\\infty\\cup \\left(\\bigcup_{j=${
                    rationalEntailment.removedRanking.length
                  }}^{j< ${
                    rationalEntailment.baseRanking.length - 1
                  }}\\overline{R_j}\\right)`}
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
                  {`R_\\infty\\cup \\left(\\bigcup_{j=${
                    rationalEntailment.removedRanking.length
                  }}^{j< ${
                    rationalEntailment.baseRanking.length - 1
                  }}\\overline{R_j}\\right)${
                    rationalEntailment.entailed ? "\\models" : "\\not\\models"
                  }${texFormula(rationalEntailment.queryFormula)}`}
                </TexFormula>
                .
              </p>
              <p>
                Therefore,{" "}
                <TexFormula>{getResult(rationalEntailment)}</TexFormula>.
              </p>
            </div>
          </div>
        )}
        {!rationalEntailment && <NoResults />}
      </CardContent>
      <CardFooter>
        {rationalEntailment && (
          <p className="uppercase text-muted-foreground italic font-semibold">
            End of rational closure!
          </p>
        )}
      </CardFooter>
    </Card>
  );
}
