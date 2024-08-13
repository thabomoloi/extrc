import { KnowledgeBase, QueryFormula } from "@/components/latex/formulas";

interface QueryInputContainerProps {
  queryFormula: string;
  knowledgeBase: string[];
  queryFormulaHidden?: boolean;
}

function QueryInputContainer({
  queryFormula,
  knowledgeBase,
  queryFormulaHidden = false,
}: QueryInputContainerProps) {
  return (
    <div className="space-y-4">
      <KnowledgeBase formulas={knowledgeBase} set />
      {!queryFormulaHidden && <QueryFormula formula={queryFormula} />}
    </div>
  );
}

export { QueryInputContainer };
