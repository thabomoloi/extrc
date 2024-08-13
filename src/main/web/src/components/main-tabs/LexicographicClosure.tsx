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
import { RankingTable } from "./tables/RankingTable";
import { QueryInputContainer } from "./common/query-input";
import { LexEntailment } from "./entailment/LexEntailment";

interface LexicographicClosureProps {
  isLoading: boolean;
  lexicalEntailment: Entailment | null;
}

function LexicographicClosure({
  isLoading,
  lexicalEntailment,
}: LexicographicClosureProps): JSX.Element {
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
              <p className="mb-3">
                Lexicographic closure starts with the initial rankings
                constructed by the Base Rank algorithm.
              </p>
              <p className="font-medium">Initial ranks</p>
              <RankingTable
                ranking={lexicalEntailment.baseRanking}
                caption="Ranks constructed by the Base Rank algorithm"
              />
            </div>
            <LexEntailment
              entailment={lexicalEntailment}
              className="mb-6 space-y-4"
            />
          </div>
        )}
        {isLoading && <ResultSkeleton />}
        {!isLoading && !lexicalEntailment && <NoResults />}
      </CardContent>
    </Card>
  );
}

export { LexicographicClosure };
