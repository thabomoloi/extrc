import { Ranking } from "./models";

function texFormula(formula: string): string {
  return formula
    .replaceAll("~>", " \\vsim ")
    .replaceAll("=>", " \\to ")
    .replaceAll("->", " \\to ")
    .replaceAll("!", " \\neg ")
    .replaceAll("&&", " \\land ")
    .replaceAll("||", " \\lor ")
    .replaceAll("<=>", " \\leftrightarrow ")
    .replaceAll("<->", " \\leftrightarrow ");
}

function unionRanks({ ranks, start }: { start: number; ranks: Ranking[] }) {
  return start == ranks.length - 1
    ? ""
    : `\\cup\\left(\\bigcup_{j=${start}}^{j<${
        ranks.length - 1
      }}\\overrightarrow{R_j}\\right)`;
}

function mathcal(formula: string) {
  return `\\mathcal{${formula}}`;
}

function braces(formula: string) {
  return `\\left\\{${formula}\\right\\}`;
}

export { texFormula, unionRanks, mathcal, braces };
