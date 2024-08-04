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

interface LexicographicClosureProps {
  lexicalEntailment: EntailmentResult | undefined;
}
export default function LexicographicClosure({
  lexicalEntailment,
}: LexicographicClosureProps) {
  return (
    <Card className="w-full h-full">
      <CardHeader>
        <CardTitle>Lexicographic Closure</CardTitle>
        <CardDescription>
          Using lexicographic closure algorithm to check for entailment.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {lexicalEntailment && <div></div>}
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
