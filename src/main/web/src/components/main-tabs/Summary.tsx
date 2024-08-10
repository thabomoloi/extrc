import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { BaseRanking, Entailment } from "@/types";
import { NoResults } from "./NoResults";
import { kb } from "../latex/helpers";
import { ResultSkeleton } from "./ResultSkeleton";

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
        <CardTitle>Summary</CardTitle>
        <CardDescription>Summary of entailment algorithms.</CardDescription>
      </CardHeader>
      <CardContent>
        {!isLoading && baseRank && rationalEntailment && lexicalEntailment && (
          <div>{kb({ formulas: rationalEntailment.knowledgeBase })}</div>
        )}
        {!isLoading &&
          !(baseRank && rationalEntailment && lexicalEntailment) && (
            <NoResults />
          )}
        {isLoading && <ResultSkeleton />}
      </CardContent>
      <CardFooter>
        {baseRank && rationalEntailment && lexicalEntailment && (
          <p className="uppercase text-muted-foreground italic font-semibold">
            End of summary!
          </p>
        )}
      </CardFooter>
    </Card>
  );
}

export { Summary };
