import { kb } from "@/components/latex/helpers";
import { TexFormula } from "@/components/latex/TexFormula";
import { Button } from "@/components/ui/button";
import { DataTable } from "@/components/ui/data-table";
import { texFormula, unionRanks } from "@/lib/latex";
import { Ranking } from "@/lib/models";
import { ColumnDef } from "@tanstack/react-table";
import { ArrowUpDown } from "lucide-react";

const MAX_VALUE = 2147483647;

interface Subset extends Ranking {
  subsetNumber: number;
  results: string;
  decision: string;
}
const rankColumns: ColumnDef<Subset>[] = [
  {
    accessorKey: "subsetNumber",
    header: ({ column }) => (
      <Button
        variant="ghost"
        onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
      >
        Subset Number
        <ArrowUpDown className="ml-2 h-4 w-4" />
      </Button>
    ),
    meta: {
      headerClassName: "max-w-[200px] text-center",
      cellClassName: "text-center",
    },
    cell: ({ row }) => {
      const idx = row.getValue("subsetNumber") as number;
      return (
        <TexFormula>{idx == MAX_VALUE ? "\\infty" : idx.toString()}</TexFormula>
      );
    },
    filterFn: "weakEquals",
  },
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
    cell: ({ row }) => {
      const idx = row.getValue("rankNumber") as number;
      return (
        <TexFormula>{idx == MAX_VALUE ? "\\infty" : idx.toString()}</TexFormula>
      );
    },
    filterFn: "weakEquals",
  },
  {
    accessorKey: "formulas",
    header: "Fomulas",
    cell: ({ row }) => {
      const formulas = row.getValue<string[]>("formulas");
      return kb({ formulas });
    },
    meta: {
      headerClassName: "min-w-[200px] w-full", // min-width, taking
      cellClassName: "whitespace-nowrap",
    },
    filterFn: "includesString",
  },
  {
    accessorKey: "results",
    header: "Results",
    cell: ({ row }) => {
      const results = row.getValue<string>("results");
      return <TexFormula>{results}</TexFormula>;
    },
    meta: {
      headerClassName: "min-w-[200px]",
      cellClassName: "whitespace-nowrap",
    },
    // filterFn: "includesString",
  },
  {
    accessorKey: "decision",
    header: "",
    meta: {
      headerClassName: "min-w-[200px]",
      cellClassName: "whitespace-nowrap",
    },
    // filterFn: "includesString",
  },
];

function SubsetTable({
  negation,
  subsets,
  rankNumber,
  removedRanks,
  baseRanks,
  caption = "",
}: {
  negation: string;
  rankNumber: number;
  subsets: Ranking[];
  removedRanks: Ranking[];
  baseRanks: Ranking[];
  caption?: string;
}) {
  const subsetRemoved = (formulas: string[], rankNumber: number) => {
    if (rankNumber < removedRanks.length) {
      const set1 = new Set(removedRanks[rankNumber].formulas);
      for (let i = 0; i < formulas.length; i++) {
        if (set1.has(formulas[i])) return true;
      }
    }
    return false;
  };
  const allSubset: Subset[] = subsets.map((value, index) => ({
    ...value,
    subsetNumber: index,
    results: `R_\\infty\\cup S_{${index}}^{R_{${rankNumber}}}${unionRanks({
      start: rankNumber + 1,
      ranks: baseRanks,
    })} ${
      subsetRemoved(value.formulas, rankNumber) ? "\\models" : "\\not\\models"
    } ${texFormula(negation)}.`,
    decision: subsetRemoved(value.formulas, rankNumber)
      ? "(Discard subset)"
      : "(Do not discard subset)",
  }));
  const data: Subset[] = allSubset.filter(
    (value) => value.rankNumber == rankNumber
  );
  return (
    <DataTable
      columns={rankColumns}
      data={data}
      filter={data.length != 0}
      filters={[
        { id: "subsetNumber", search: "Search subset..." },
        { id: "formulas", search: "Search formulas..." },
      ]}
      caption={caption}
    />
  );
}

export { SubsetTable };
