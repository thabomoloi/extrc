import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { BaseRanking, Entailment } from "@/types";
import { NoResults } from "./NoResults";
import { ResultSkeleton } from "./ResultSkeleton";
import { EntailmentTable, TimesTable } from "./tables/other-tables";
import { RankingTable } from "./tables/RankingTable";
import { QueryInputContainer } from "./common/query-input";

interface SummaryProps {
  isLoading: boolean;
  baseRank: BaseRanking | null;
  rationalEntailment: Entailment | null;
  lexicalEntailment: Entailment | null;
}

function Summary({
  isLoading,
  baseRank,
  rationalEntailment,
  lexicalEntailment,
}: SummaryProps): JSX.Element {
  return (
    <Card className="w-full h-full">
      <CardHeader>
        <CardTitle className="text-lg font-bold">Summary</CardTitle>
        <CardDescription>Summary of entailment algorithms.</CardDescription>
      </CardHeader>
      <CardContent>
        {!isLoading && baseRank && rationalEntailment && lexicalEntailment && (
          <div className="space-y-6">
            <QueryInputContainer
              knowledgeBase={rationalEntailment.knowledgeBase}
              queryFormula={rationalEntailment.queryFormula}
            />
            <div>
              <h4 className="scroll-m-20 font-medium tracking-tight">
                Entailment Results
              </h4>
              <EntailmentTable
                rationalEntailment={rationalEntailment}
                lexicalEntailment={lexicalEntailment}
              />
            </div>
            <div>
              <h4 className="scroll-m-20 font-medium tracking-tight">
                Initial Ranks
              </h4>
              <RankingTable ranking={baseRank.ranking} />
            </div>
            <div>
              <h4 className="scroll-m-20 font-medium tracking-tight">
                Removed Ranks
              </h4>
              <h5 className="text-sm text-muted-foreground mt-2 font-medium">
                Rational Closure
              </h5>
              <RankingTable ranking={rationalEntailment.removedRanking} />
              <h5 className="text-sm text-muted-foreground mt-2 font-medium">
                Lexicographic Closure
              </h5>
              <RankingTable ranking={lexicalEntailment.removedRanking} />
            </div>
            <div>
              <h4 className="scroll-m-20 font-medium tracking-tight">
                Time Taken
              </h4>
              <TimesTable
                baseRank={baseRank}
                rationalEntailment={rationalEntailment}
                lexicalEntailment={lexicalEntailment}
              />
            </div>
          </div>
        )}
        {!isLoading &&
          !(baseRank && rationalEntailment && lexicalEntailment) && (
            <NoResults />
          )}
        {isLoading && <ResultSkeleton />}
      </CardContent>
    </Card>
  );
}

export { Summary };
