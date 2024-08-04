import { EntailmentResult } from "@/hooks/use-entailment";
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "../ui/card";
import { ColumnDef } from "@tanstack/react-table";

import KnowledgeBase from "../reasoning/KnowledgeBase";
import TexFormula from "../TexFormula";
import { texFormula } from "@/lib/utils";
import RankingTable from "../reasoning/RankingTable";
import { DataTable } from "../ui/data-table";
import NoResults from "../no-results";

interface SummaryProps {
  rationalEntailment: EntailmentResult | undefined;
  lexicalEntailment: EntailmentResult | undefined;
}

interface AlgorithmResult {
  algorithm: string;
  result: string;
}

interface TimesResult {
  algorithm: string;
  timeTaken: string;
}

const entailmentColumns: ColumnDef<AlgorithmResult>[] = [
  {
    accessorKey: "algorithm",
    header: "Algorithm",
    cell: ({ row }) => row.getValue("algorithm"),
    meta: {
      cellClassName: "whitespace-nowrap",
    },
  },
  {
    accessorKey: "result",
    header: "Result",
    cell: ({ row }) => <TexFormula>{row.getValue("result")}</TexFormula>,
    meta: {
      headerClassName: "w-full",
      cellClassName: "whitespace-nowrap",
    },
  },
];

const timesColumns: ColumnDef<TimesResult>[] = [
  {
    accessorKey: "algorithm",
    header: "Algorithm",
    cell: ({ row }) => row.getValue("algorithm"),
    meta: {
      headerClassName: "min-w-[180px]",
      cellClassName: "whitespace-nowrap",
    },
  },
  {
    accessorKey: "timeTaken",
    header: "Time Taken",
    cell: ({ row }) => {
      const value = row.getValue("timeTaken") as string;
      return (
        <span>
          <TexFormula>{value}</TexFormula>&nbsp;&nbsp;seconds
        </span>
      );
    },
    meta: {
      headerClassName: "w-full",
      cellClassName: "whitespace-nowrap",
    },
  },
];

export default function Summary({
  rationalEntailment,
  lexicalEntailment,
}: SummaryProps) {
  const getResult = ({ entailed, queryFormula }: EntailmentResult) => {
    return entailed
      ? texFormula("\\mathcal{K} \\vapprox " + queryFormula)
      : texFormula("\\mathcal{K} \\nvapprox " + queryFormula);
  };

  const getTimes = ({ times }: EntailmentResult) => {
    const largestTime = times.reduce((max, obj) =>
      obj.timeTaken > max.timeTaken ? obj : max
    );
    const rounded = Math.round(largestTime.timeTaken * 10000) / 10000;
    const results: TimesResult[] = [];
    for (let i = 0; i < times.length; i++) {
      const time = Math.round(times[i].timeTaken * 10000) / 10000;
      const diff = rounded.toString().length - time.toString().length;
      results.push({
        algorithm: times[i].title,
        timeTaken: "\\;\\;".repeat(diff) + time.toString(),
      });
    }
    return results;
  };
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
            <div className="mb-6">
              <TexFormula>{`\\alpha = ${texFormula(
                rationalEntailment.queryFormula
              )}`}</TexFormula>
            </div>
            <div className="mb-6">
              <h4 className="scroll-m-20 font-medium tracking-tight">
                Entailment Results
              </h4>
              <DataTable
                columns={entailmentColumns}
                data={[
                  {
                    algorithm: "Rational Closure",
                    result: getResult(rationalEntailment),
                  },
                  {
                    algorithm: "Lexicographic Closure",
                    result: getResult(rationalEntailment),
                  },
                ]}
              />
            </div>

            <div className="mb-6">
              <h4 className="scroll-m-20 font-medium tracking-tight">
                Initial Ranks
              </h4>
              <RankingTable ranking={rationalEntailment.baseRanking} />
            </div>
            <div className="mb-6">
              <h4 className="scroll-m-20 font-medium tracking-tight">
                Removed Ranks{" "}
                <span className="text-muted-foreground">
                  (Rational Closure)
                </span>
              </h4>
              <RankingTable ranking={rationalEntailment.removedRanking} />
            </div>
            <div className="mb-6">
              <h4 className="scroll-m-20 font-medium tracking-tight">
                Removed Ranks{" "}
                <span className="text-muted-foreground">
                  (Lexicographic Closure)
                </span>
              </h4>
              <RankingTable ranking={lexicalEntailment.removedRanking} />
            </div>
            <div className="mb-6">
              <h4 className="scroll-m-20 font-medium tracking-tight">
                Time Taken{" "}
                <span className="text-muted-foreground">
                  (Rational Closure)
                </span>
              </h4>
              <DataTable
                columns={timesColumns}
                data={getTimes(rationalEntailment)}
              />
            </div>
            <div className="mb-6">
              <h4 className="scroll-m-20 font-medium tracking-tight">
                Time Taken{" "}
                <span className="text-muted-foreground">
                  (Lexicographic Closure){" "}
                </span>
              </h4>
              <DataTable
                columns={timesColumns}
                data={getTimes(lexicalEntailment)}
              />
            </div>
          </div>
        )}
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
