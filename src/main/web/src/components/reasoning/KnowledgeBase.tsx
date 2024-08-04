import { texFormula } from "@/lib/utils";
import TexFormula from "../TexFormula";

export default function KnowledgeBase({
  knowledgeBase,
  name = "\\mathcal{K}",
}: {
  knowledgeBase: string;
  name?: string;
}) {
  return (
    <div className="line-clamp-1">
      <TexFormula>{`${name}=\\{\\;`}</TexFormula>
      {knowledgeBase.split(",").map((formula, index, array) => (
        <span key={index}>
          <TexFormula>{texFormula(formula)}</TexFormula>
          {index < array.length - 1 && <TexFormula>{",\\;"}</TexFormula>}
        </span>
      ))}
      <TexFormula>{"\\;\\}"}</TexFormula>
    </div>
  );
}
