import { QueryInput } from "@/types";
import { KbCard } from "./KbCard";
import { KbCardSkeleton } from "./KbCardSkeleton";

interface QueryInputProps {
  isLoading: boolean;
  queryInput: QueryInput | null;
  submitKnowledgeBase: (knowledgeBase: string[]) => void;
  uploadKnowledgeBase: (data: FormData) => void;
}
function QueryInputs({
  isLoading,
  queryInput,
  submitKnowledgeBase,
  uploadKnowledgeBase,
}: QueryInputProps) {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-[45%,_55%] md:grid-cols-[40%,_60%] gap-4">
      {!isLoading && queryInput && (
        <KbCard
          knowledgeBase={queryInput.knowledgeBase}
          submitKnowledgeBase={submitKnowledgeBase}
          uploadKnowledgeBase={uploadKnowledgeBase}
        />
      )}
      {!isLoading && !queryInput && <KbCardSkeleton />}
    </div>
  );
}

export { QueryInputs };
