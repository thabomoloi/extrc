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
import TexFormula from "../TexFormula";
import { texFormula } from "@/lib/utils";
import KnowledgeBase from "../reasoning/KnowledgeBase";
import RankingTable from "../reasoning/RankingTable";

interface LexicographicClosureProps {
  lexicalEntailment: EntailmentResult | undefined;
}
export default function LexicographicClosure({
  lexicalEntailment,
}: LexicographicClosureProps) {
  const getResult = ({ entailed, queryFormula }: EntailmentResult) => {
    return entailed
      ? texFormula("\\mathcal{K} \\vapprox " + queryFormula)
      : texFormula("\\mathcal{K} \\nvapprox " + queryFormula);
  };
  return (
    <Card className="w-full h-full">
      <CardHeader>
        <CardTitle>Lexicographic Closure</CardTitle>
        <CardDescription>
          Using lexicographic closure algorithm to check for entailment.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {lexicalEntailment && (
          <div>
            <KnowledgeBase knowledgeBase={lexicalEntailment.knowledgeBase} />
            <div className="mb-6">
              <TexFormula>{`\\alpha = ${texFormula(
                lexicalEntailment.queryFormula
              )}`}</TexFormula>
            </div>
            <div className="mb-6">
              <p>
                Lexicographic closure starts with the initial rankings
                constructed by the Base Rank algorithm.
              </p>
              <RankingTable ranking={lexicalEntailment.baseRanking} />
            </div>
            <div className="mb-6">
              <p className="mb-4">
                Check if the ranks entail{" "}
                <TexFormula>
                  {texFormula(lexicalEntailment.negation)}
                </TexFormula>
                . If they do remove the lowest rank, otherwise check if the
                remaining ranks entail the formula{" "}
                <TexFormula>
                  {texFormula(lexicalEntailment.queryFormula)}
                </TexFormula>
                .
              </p>
              <div className="ml-8 mb-2">
                {lexicalEntailment.removedRanking.map((value, index, array) => (
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
                            lexicalEntailment.baseRanking.length - 1
                          }}\\overline{R_j}\\right)\\models ${texFormula(
                            lexicalEntailment.negation
                          )}`}
                        </TexFormula>
                        . Remove <TexFormula>{`R_{${index}}`}</TexFormula>.
                      </span>
                    )}
                  </div>
                ))}
                {lexicalEntailment.removedRanking.length <
                  lexicalEntailment.baseRanking.length - 1 && (
                  <div>
                    <TexFormula>
                      {`R_\\infty\\cup \\left(\\bigcup_{j=${
                        lexicalEntailment.removedRanking.length
                      }}^{j< ${
                        lexicalEntailment.baseRanking.length - 1
                      }}\\overline{R_j}\\right)\\not\\models ${texFormula(
                        lexicalEntailment.negation
                      )}`}
                    </TexFormula>
                    .
                  </div>
                )}
              </div>
              <div>
                <RankingTable
                  ranking={lexicalEntailment.removedRanking}
                  caption="Ranks removed by lexicograhic closure."
                />
              </div>
            </div>
            <div className="space-y-4">
              <p>
                Now we check if{" "}
                <TexFormula>
                  {`R_\\infty\\cup \\left(\\bigcup_{j=${
                    lexicalEntailment.removedRanking.length
                  }}^{j< ${
                    lexicalEntailment.baseRanking.length - 1
                  }}\\overline{R_j}\\right)`}
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
                  {`R_\\infty\\cup \\left(\\bigcup_{j=${
                    lexicalEntailment.removedRanking.length
                  }}^{j< ${
                    lexicalEntailment.baseRanking.length - 1
                  }}\\overline{R_j}\\right)${
                    lexicalEntailment.entailed ? "\\models" : "\\not\\models"
                  }${texFormula(lexicalEntailment.queryFormula)}`}
                </TexFormula>
                .
              </p>
              <p>
                Therefore,{" "}
                <TexFormula>{getResult(lexicalEntailment)}</TexFormula>.
              </p>
            </div>
          </div>
        )}
        {!lexicalEntailment && <NoResults />}
      </CardContent>
      <CardFooter>
        {lexicalEntailment && (
          <p className="uppercase text-muted-foreground italic font-semibold">
            End of lexicographic closure!
          </p>
        )}
      </CardFooter>
    </Card>
  );
}
