import { QueryInput } from "@/types";
import { KbCard } from "./KbCard";
import { KbCardSkeleton } from "./KbCardSkeleton";
import { FormulaCard } from "./FormulaCard";
import { FormulaCardSkeleton } from "./FormulaCardSkeleton";

interface QueryInputProps {
  isLoading: boolean;
  queryInput: QueryInput | null;
  submitKnowledgeBase: (knowledgeBase: string[]) => void;
  uploadKnowledgeBase: (data: FormData) => void;
  submitQuery: () => void;
  updateFormula: (formula: string) => void;
}
function QueryInputs({
  isLoading,
  queryInput,
  submitKnowledgeBase,
  uploadKnowledgeBase,
  submitQuery,
  updateFormula,
}: QueryInputProps) {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-[45%,_55%] md:grid-cols-[40%,_60%] gap-4">
      {!isLoading && queryInput && (
        <FormulaCard
          handleQuerySubmit={submitQuery}
          queryFormula={queryInput.queryFormula}
          updateFormula={updateFormula}
        />
      )}
      {(isLoading || !queryInput) && <FormulaCardSkeleton />}
      {!isLoading && queryInput && (
        <KbCard
          knowledgeBase={queryInput.knowledgeBase}
          submitKnowledgeBase={submitKnowledgeBase}
          uploadKnowledgeBase={uploadKnowledgeBase}
        />
      )}
      {(isLoading || !queryInput) && <KbCardSkeleton />}
    </div>
  );
}

export { QueryInputs };
