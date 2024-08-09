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
        <CardTitle>Lexicographic Closure</CardTitle>
        <CardDescription>
          Using lexicographic closure algorithm to check for entailment.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {isLoading && <ResultSkeleton />}
        {!lexicalEntailment && <NoResults />}
      </CardContent>
      <CardFooter>
        {lexicalEntailment && (
          <p className="uppercase text-muted-foreground italic font-semibold">
            End of Lexicographic Closure!
          </p>
        )}
      </CardFooter>
    </Card>
  );
}

export { LexicographicClosure };
