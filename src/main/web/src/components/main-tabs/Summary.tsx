import { EntailmentResult } from "@/hooks/use-entailment";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "../ui/card";
import KnowledgeBase from "../reasoning/KnowledgeBase";
import TexFormula from "../TexFormula";
import { texFormula } from "@/lib/utils";
import RankingTable from "../reasoning/RankingTable";

interface SummaryProps {
  rationalEntailment: EntailmentResult | undefined;
  lexicalEntailment: EntailmentResult | undefined;
}
export default function Summary({
  rationalEntailment,
  lexicalEntailment,
}: SummaryProps) {
  return (
    <Card className="w-full h-full">
      <CardHeader>
        <CardTitle>Summary</CardTitle>
        <CardDescription>Summary of entailment algorithms.</CardDescription>
      </CardHeader>
      <CardContent>
        {rationalEntailment && lexicalEntailment && (
          <div>
            <KnowledgeBase knowledgeBase={rationalEntailment.knowledgeBase} />
            <div>
              <TexFormula>{`\\alpha = ${texFormula(
                rationalEntailment.queryFormula
              )}`}</TexFormula>
            </div>
            <div>
              <RankingTable ranking={rationalEntailment.baseRanking} />
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  );
}
