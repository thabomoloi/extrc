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
      <div className="text-center">
        <Button
          variant="ghost"
          onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
        >
          Rank Number
          <ArrowUpDown className="ml-2 h-4 w-4" />
        </Button>
      </div>
    ),

    cell: ({ row }) => (
      <div className="text-center">
        <TexFormula>{row.getValue("rankNumber")}</TexFormula>
      </div>
    ),
  },
  {
    accessorKey: "formulas",
    header: () => <div className="text-center">Formulas</div>,
    cell: ({ row }) => {
      const rowFormulas = row.getValue("formulas");
      const formulas = typeof rowFormulas === "string" ? rowFormulas : "";
      return (
        <div className="text-center">
          {formulas.split(",").map((formula, index, array) => (
            <span key={index}>
              <TexFormula>{texFormula(formula)}</TexFormula>
              {index < array.length - 1 && <TexFormula>{",\\;"}</TexFormula>}
            </span>
          ))}
        </div>
      );
    },
    filterFn: "includesString",
  },
];

export default function RankingTable({ ranking }: { ranking: Ranking[] }) {
  return <DataTable columns={columns} data={ranking} />;
}
