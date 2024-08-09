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
        <CardTitle>Rational Closure</CardTitle>
        <CardDescription>
          Using rational closure algorithm to check for entailment.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {isLoading && <ResultSkeleton />}
        {!rationalEntailment && <NoResults />}
      </CardContent>
      <CardFooter>
        {rationalEntailment && (
          <p className="uppercase text-muted-foreground italic font-semibold">
            End of Rational Closure!
          </p>
        )}
      </CardFooter>
    </Card>
  );
}

export { RationalClosure };
