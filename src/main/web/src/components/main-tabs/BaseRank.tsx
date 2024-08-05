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
import KnowledgeBase from "../reasoning/KnowledgeBase";
import TexFormula from "../TexFormula";
import { texFormula } from "@/lib/utils";
import { ColumnDef } from "@tanstack/react-table";
import { Button } from "../ui/button";
import { ArrowUpDown } from "lucide-react";
import { DataTable } from "../ui/data-table";
import RankingTable from "../reasoning/RankingTable";

const seqColumns: ColumnDef<{ index: string; formulas: string }>[] = [
  {
    accessorKey: "index",
    header: ({ column }) => (
      <Button
        variant="ghost"
        onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
      >
        Index&nbsp;<TexFormula>{"(i)"}</TexFormula>
        <ArrowUpDown className="ml-2 h-4 w-4" />
      </Button>
    ),
    meta: {
      headerClassName: "max-w-[200px] text-center",
      cellClassName: "text-center",
    },
    cell: ({ row }) => <TexFormula>{row.getValue("index")}</TexFormula>,
  },
  {
    accessorKey: "formulas",
    header: () => (
      <span>
        Element&nbsp;<TexFormula>{"(*_i^\\mathcal{K})"}</TexFormula>
      </span>
    ),
    cell: ({ row }) => {
      const rowFormulas = row.getValue("formulas");
      const formulas = typeof rowFormulas === "string" ? rowFormulas : "";
      return formulas.split(",").map((formula, index, array) => (
        <span key={index}>
          <TexFormula>{texFormula(formula)}</TexFormula>
          {index < array.length - 1 && <TexFormula>{",\\;"}</TexFormula>}
        </span>
      ));
    },
    meta: {
      headerClassName: "min-w-max w-full", // min-width, taking
      cellClassName: "whitespace-nowrap",
    },
    filterFn: "includesString",
  },
];

interface BaseRankProps {
  rationalEntailment: EntailmentResult | undefined;
  lexicalEntailment: EntailmentResult | undefined;
}
export default function BaseRank({
  lexicalEntailment,
  rationalEntailment,
}: BaseRankProps) {
  return (
    <Card className="w-full h-full">
      <CardHeader>
        <CardTitle>Base Rank</CardTitle>
        <CardDescription>
          Using base rank algorithm to create initial ranks.
        </CardDescription>
      </CardHeader>
      <CardContent>
        {rationalEntailment && lexicalEntailment && (
          <div>
            <KnowledgeBase knowledgeBase={rationalEntailment.knowledgeBase} />
            <div className="mb-6">
              {/* <TexFormula>{`\\alpha = ${texFormula(
                rationalEntailment.queryFormula
              )}`}</TexFormula> */}
            </div>
            <div className="mb-6">
              <p>
                Let <TexFormula>{"\\mathcal{K}_C"}</TexFormula> contain all
                classical formulas. Then,{" "}
              </p>
              <div className="text-center">
                <KnowledgeBase
                  name={"\\mathcal{K}_C"}
                  knowledgeBase={rationalEntailment.knowledgeBase
                    .split(",")
                    .filter((formula) => formula.includes("=>"))
                    .join(",")}
                />
              </div>
            </div>
            <div className="mb-6">
              <p>
                The exceptionality sequence <TexFormula>{"*"}</TexFormula> for
                knowledge base <TexFormula>{"\\mathcal{K}"}</TexFormula> is
                given by:
              </p>
              <ul className="ml-8 list-disc [&>li]:">
                <li>
                  <TexFormula>
                    {
                      "*_0^\\mathcal{K}=\\{\\alpha\\vsim\\beta\\in\\mathcal{K}\\}"
                    }
                  </TexFormula>{" "}
                  and
                </li>
                <li>
                  <TexFormula>
                    {
                      "*_{i+1}^\\mathcal{K}=\\left\\{\\alpha\\vsim\\beta \\in *_{i}^\\mathcal{K} \\mid \\mathcal{K}_C\\cup \\overline{*_{i}^\\mathcal{K}}\\models\\neg\\alpha\\right\\}"
                    }
                  </TexFormula>
                </li>
                <li>
                  for <TexFormula>{"0\\leq i < n"}</TexFormula>, where{" "}
                  <TexFormula>{"n"}</TexFormula> is the smallest integer such
                  that{" "}
                  <TexFormula>
                    {"*_n^\\mathcal{K}=*_{n+1}^\\mathcal{K}"}
                  </TexFormula>
                  .
                </li>
                <li>
                  Final element <TexFormula>{"*_n^\\mathcal{K}"}</TexFormula> is
                  usually denoted as{" "}
                  <TexFormula>{"*_\\infty^\\mathcal{K}"}</TexFormula>.
                </li>
              </ul>
              <DataTable
                columns={seqColumns}
                data={rationalEntailment.sequence.map(
                  (value, index, array) => ({
                    index: index == array.length - 1 ? "∞" : index.toString(),
                    formulas: value,
                  })
                )}
                filter
                filters={[
                  { id: "index", search: "Search position..." },
                  { id: "formulas", search: "Search formulas..." },
                ]}
              />
            </div>
            <div className="mb-6">
              <p>
                Finite rank is given by{" "}
                <TexFormula>
                  {
                    "\\mathcal{R}_i=*_i^\\mathcal{K}\\setminus *_{i+1}^\\mathcal{K}"
                  }
                </TexFormula>{" "}
                and, infinie rank is given by{" "}
                <TexFormula>
                  {"\\mathcal{R}_i=*_\\infty^\\mathcal{K}\\cup\\mathcal{K}_C"}
                </TexFormula>
                .
              </p>
              <RankingTable ranking={rationalEntailment.baseRanking} />
            </div>
          </div>
        )}
        {!(rationalEntailment && lexicalEntailment) && <NoResults />}
      </CardContent>
      <CardFooter>
        {rationalEntailment && lexicalEntailment && (
          <p className="uppercase text-muted-foreground italic font-semibold">
            End of base rank algorithm!
          </p>
        )}
      </CardFooter>
    </Card>
  );
}
