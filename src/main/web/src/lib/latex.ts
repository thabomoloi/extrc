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

export { texFormula };
