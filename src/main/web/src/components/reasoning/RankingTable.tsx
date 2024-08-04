import { Ranking } from "@/hooks/use-entailment";
import { ColumnDef } from "@tanstack/react-table";

import TexFormula from "../TexFormula";
import { texFormula } from "@/lib/utils";
import { DataTable } from "../ui/data-table";
import { ArrowUpDown } from "lucide-react";
import { Button } from "../ui/button";

const columns: ColumnDef<Ranking>[] = [
  {
    accessorKey: "rankNumber",
    header: ({ column }) => (
      <Button
        variant="ghost"
        onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
      >
        Rank Number
        <ArrowUpDown className="ml-2 h-4 w-4" />
      </Button>
    ),
    meta: {
      headerClassName: "max-w-[200px] text-center",
      cellClassName: "text-center",
    },
    cell: ({ row }) => <TexFormula>{row.getValue("rankNumber")}</TexFormula>,
  },
  {
    accessorKey: "formulas",
    header: "Formulas",
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

export default function RankingTable({
  ranking,
  caption = "",
}: {
  ranking: Ranking[];
  caption?: string;
}) {
  return (
    <DataTable
      columns={columns}
      data={ranking}
      filter={ranking.length != 0}
      caption={caption}
    />
  );
}
