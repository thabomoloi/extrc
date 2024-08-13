import { mathcal, texFormula } from "@/lib/latex";
import { TexFormula } from "./TexFormula";

interface KnowledgeBaseProps {
  name?: string;
  formulas: string[];
  set?: boolean;
}

interface QueryFormulaProps {
  formula: string;
}

function KnowledgeBase({
  formulas,
  name = mathcal("K"),
  set = false,
}: KnowledgeBaseProps) {
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

function QueryFormula({ formula }: QueryFormulaProps) {
  return <TexFormula>{"\\alpha = " + texFormula(formula)}</TexFormula>;
}
export { KnowledgeBase, QueryFormula };
