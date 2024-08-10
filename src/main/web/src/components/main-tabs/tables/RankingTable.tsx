import { kb } from "@/components/latex/helpers";
import { TexFormula } from "@/components/latex/TexFormula";
import { Button } from "@/components/ui/button";
import { DataTable } from "@/components/ui/data-table";
import { Ranking } from "@/types";
import { ColumnDef } from "@tanstack/react-table";
import { ArrowUpDown } from "lucide-react";

const MAX_VALUE = 2147483647;

const formulas: ColumnDef<Ranking> = {
  accessorKey: "formulas",
  header: "Formulas",
  cell: ({ row }) => {
    const formulas = row.getValue<string[]>("formulas");
    return kb({ formulas });
  },
  meta: {
    headerClassName: "min-w-max w-full", // min-width, taking
    cellClassName: "whitespace-nowrap",
  },
  filterFn: "includesString",
};

const rankColumns: ColumnDef<Ranking>[] = [
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
  formulas,
];

const sequenceColumns: ColumnDef<Ranking>[] = [
  {
    accessorKey: "elementNumber",
    header: ({ column }) => (
      <Button
        variant="ghost"
        onClick={() => column.toggleSorting(column.getIsSorted() === "asc")}
      >
        Index
        <ArrowUpDown className="ml-2 h-4 w-4" />
      </Button>
    ),
    meta: {
      headerClassName: "max-w-[200px] text-center",
      cellClassName: "text-center",
    },
    cell: ({ row }) => {
      const idx = row.getValue("elementNumber") as number;
      return (
        <TexFormula>{idx == MAX_VALUE ? "\\infty" : idx.toString()}</TexFormula>
      );
    },
    filterFn: "weakEquals",
  },
  formulas,
];

function RankingTable({
  ranking,
  caption = "",
}: {
  ranking: Ranking[];
  caption?: string;
}) {
  return (
    <DataTable
      columns={rankColumns}
      data={ranking}
      filter={ranking.length != 0}
      caption={caption}
    />
  );
}

function SequenceTable({
  ranking,
  caption = "",
}: {
  ranking: Ranking[];
  caption?: string;
}) {
  <DataTable
    columns={sequenceColumns}
    data={ranking}
    filter={ranking.length != 0}
    caption={caption}
    filters={[
      { id: "elementNumber", search: "Search index..." },
      { id: "formulas", search: "Search formulas..." },
    ]}
  />;
}

export { RankingTable, SequenceTable };
