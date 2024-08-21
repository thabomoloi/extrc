import { texFormula } from "@/lib/latex";
import { TexFormula } from "./TexFormula";
import { EntailmentModel } from "@/lib/models";

function kb({
  name = "\\mathcal{K}",
  formulas,
  set = false,
}: {
  name?: string;
  formulas: string[];
  set?: boolean;
}) {
  return (
    <div className="line-clamp-1">
      {set && <TexFormula>{`${name}=\\{\\;`}</TexFormula>}
      {formulas.map((formula, index, array) => (
        <span key={index}>
          <TexFormula>{texFormula(formula)}</TexFormula>
          {index < array.length - 1 && <TexFormula>{",\\;"}</TexFormula>}
        </span>
      ))}
      {set && <TexFormula>{"\\;\\}"}</TexFormula>}
    </div>
  );
}

function entailResult({ entailed, queryFormula }: EntailmentModel) {
  return (
    <TexFormula>
      {entailed
        ? texFormula("\\mathcal{K} \\vapprox " + queryFormula)
        : texFormula("\\mathcal{K} \\nvapprox " + queryFormula)}
    </TexFormula>
  );
}

export { kb, entailResult };
