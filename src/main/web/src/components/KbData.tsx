import { useReasoner } from "@/hooks/use-reasoner";
import { TexFormula } from "./latex/TexFormula";
import { texFormula } from "@/lib/latex";

export function KbData() {
  const reasoner = useReasoner();
  return (
    <div>
      <h1 className="mb-4 text-lg font-bold">Knowledge Base</h1>
      <div className="grid grid-cols-2 sm:grid-cols-4 md:grid-cols-8 items-center">
        {reasoner.queryInput?.knowledgeBase.map((formula, index) => (
          <TexFormula key={index}>{texFormula(formula)}</TexFormula>
        ))}
      </div>
    </div>
  );
}
