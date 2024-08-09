import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { ResultSkeleton } from "@/components/main-tabs/ResultSkeleton";
import { Entailment } from "@/types";
import { NoResults } from "./NoResults";

interface SummaryProps {
  isLoading: boolean;
  rationalEntailment: Entailment | null;
  lexicalEntailment: Entailment | null;
}

function Summary({
  isLoading,
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
        {isLoading && <ResultSkeleton />}
        {!(rationalEntailment && lexicalEntailment) && <NoResults />}
      </CardContent>
      <CardFooter>
        {rationalEntailment && lexicalEntailment && (
          <p className="uppercase text-muted-foreground italic font-semibold">
            End of summary!
          </p>
        )}
      </CardFooter>
    </Card>
  );
}

export { Summary };
