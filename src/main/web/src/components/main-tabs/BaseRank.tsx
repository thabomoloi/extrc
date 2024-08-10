import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { ResultSkeleton } from "@/components/main-tabs/ResultSkeleton";
import { BaseRanking } from "@/types";
import { NoResults } from "./NoResults";

interface BaseRankProps {
  isLoading: boolean;
  baseRank: BaseRanking | null;
}

function BaseRank({ isLoading, baseRank }: BaseRankProps): JSX.Element {
  return (
    <Card className="w-full h-full">
      <CardHeader>
        <CardTitle>Base Rank</CardTitle>
        <CardDescription>
          Using base rank algorithm to create initial ranks.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {isLoading && <ResultSkeleton />}
        {!baseRank && <NoResults />}
      </CardContent>
    </Card>
  );
}

export { BaseRank };
