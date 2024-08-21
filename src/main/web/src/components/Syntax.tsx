import {
  Table,
  TableBody,
  TableCaption,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { texFormula } from "@/lib/latex";
import { TexFormula } from "./latex/TexFormula";

const symbols = [
  {
    name: "Negation",
    plainTextSymbol: "!",
    mathSymbol: texFormula("!"),
    plainTextExample: "!p",
    mathExample: texFormula("!p"),
  },
  {
    name: "Conjunction",
    plainTextSymbol: "&&",
    mathSymbol: texFormula("&&"),
    plainTextExample: "p && q",
    mathExample: texFormula("p && q"),
  },
  {
    name: "Disjunction",
    plainTextSymbol: "||",
    mathSymbol: texFormula("||"),
    plainTextExample: "p || q",
    mathExample: texFormula("p || q"),
  },
  {
    name: "Implication",
    plainTextSymbol: "=>",
    mathSymbol: texFormula("=>"),
    plainTextExample: "p => q",
    mathExample: texFormula("p => q"),
  },
  {
    name: "Defeasible Implication",
    plainTextSymbol: "~>",
    mathSymbol: texFormula("~>"),
    plainTextExample: "p ~> q",
    mathExample: texFormula("p ~> q"),
  },
  {
    name: "Equivalence",
    plainTextSymbol: "<=>",
    mathSymbol: texFormula("<=>"),
    plainTextExample: "p <=> q",
    mathExample: texFormula("p <=> q"),
  },
];

export function Syntax() {
  return (
    <Table className="overflow-x-auto border-collapse border">
      <TableCaption>Boolean connectives.</TableCaption>
      <TableHeader>
        <TableRow>
          <TableHead rowSpan={2} className="min-w-40 border">
            Name
          </TableHead>
          <TableHead colSpan={2} className="text-center border">
            Symbol
          </TableHead>
          <TableHead colSpan={2} className="text-center border">
            Example
          </TableHead>
        </TableRow>
        <TableRow>
          <TableHead className="text-center min-w-24 border">Text</TableHead>
          <TableHead className="text-center min-w-24 border">Math</TableHead>
          <TableHead className="text-center min-w-24 border">Text</TableHead>
          <TableHead className="text-center min-w-24 border">Math</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {symbols.map((symbol) => (
          <TableRow key={symbol.name}>
            <TableCell className="border">{symbol.name}</TableCell>
            <TableCell className="text-center border">
              <code>{symbol.plainTextSymbol}</code>
            </TableCell>
            <TableCell className="text-center border">
              <TexFormula>{symbol.mathSymbol}</TexFormula>
            </TableCell>
            <TableCell className="text-center border">
              <code>{symbol.plainTextExample}</code>
            </TableCell>
            <TableCell className="text-center border">
              <TexFormula>{symbol.mathExample}</TexFormula>
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
