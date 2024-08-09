import { texFormula } from "@/lib/latex";
import { TexFormula } from "./TexFormula";

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

export { kb };
