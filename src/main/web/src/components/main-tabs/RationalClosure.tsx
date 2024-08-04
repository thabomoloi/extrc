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

interface RationalClosureProps {
  rationalEntailment: EntailmentResult | undefined;
}
export default function RationalClosure({
  rationalEntailment,
}: RationalClosureProps) {
  return (
    <Card className="w-full h-full">
      <CardHeader>
        <CardTitle>Rational Closure</CardTitle>
        <CardDescription>
          Using rational closure algorithm to check for entailment.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {rationalEntailment && <div></div>}
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
