import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { ResultSkeleton } from "@/components/main-tabs/ResultSkeleton";
import { NoResults } from "./NoResults";
import { TexFormula } from "../latex/TexFormula";
import { RankingTable } from "./tables/RankingTable";
import { texFormula } from "@/lib/latex";
import { kb } from "../latex/helpers";
import { Entailment } from "@/types";
import { RatEntailment } from "./entailment/RatEntailment";

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
        <CardTitle className="text-lg font-bold">Rational Closure</CardTitle>
        <CardDescription>
          Using rational closure algorithm to check for entailment.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {!isLoading && rationalEntailment && (
          <div>
            {kb({ formulas: rationalEntailment.knowledgeBase, set: true })}
            <div className="mb-6">
              <TexFormula>
                {texFormula("\\alpha = " + rationalEntailment.queryFormula)}
              </TexFormula>
            </div>
            <div className="mb-6">
              <p className="mb-3">
                Rational closure starts with the initial rankings constructed by
                the Base Rank algorithm.
              </p>
              <p className="font-medium">Initial ranks</p>
              <RankingTable
                ranking={rationalEntailment.baseRanking}
                caption="Ranks constructed by the Base Rank algorithm"
              />
            </div>
            <RatEntailment
              entailment={rationalEntailment}
              className="mb-6 space-y-4"
            />
          </div>
        )}
        {isLoading && <ResultSkeleton />}
        {!isLoading && !rationalEntailment && <NoResults />}
      </CardContent>
    </Card>
  );
}

export { RationalClosure };
