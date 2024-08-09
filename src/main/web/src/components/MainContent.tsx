import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { useReasoner } from "@/hooks/use-reasoner";
import { Summary } from "./main-tabs/Summary";
import { BaseRank } from "./main-tabs/BaseRank";
import { RationalClosure } from "./main-tabs/RationalClosure";
import { LexicographicClosure } from "./main-tabs/LexicographicClosure";
import { QueryInputs } from "./inputs/QueryInputs";

export function MainContent() {
  const reasoner = useReasoner();

  return (
    <div className="flex flex-col gap-6">
      <QueryInputs
        isLoading={reasoner.isPending}
        queryInput={reasoner.queryInput}
        submitKnowledgeBase={reasoner.submitKnowledgeBase}
        uploadKnowledgeBase={reasoner.uploadKnowledgeBase}
      />
      <Tabs defaultValue="summary">
        <TabsList className="grid grid-cols-2 sm:grid-cols-4 flex-wrap h-auto space-y-1'">
          <TabsTrigger value="summary">Summary</TabsTrigger>
          <TabsTrigger value="baseRank">Base Rank</TabsTrigger>
          <TabsTrigger value="rationaClosure">Rational Closure</TabsTrigger>
          <TabsTrigger value="lexicographicClosure">
            Lexicographic Closure
          </TabsTrigger>
        </TabsList>
        <TabsContent value="summary">
          <Summary
            isLoading={reasoner.isPending}
            lexicalEntailment={reasoner.lexicalEntailment}
            rationalEntailment={reasoner.rationalEntailment}
          />
        </TabsContent>
        <TabsContent value="baseRank">
          <BaseRank
            isLoading={reasoner.isPending}
            baseRank={reasoner.baseRank}
          />
        </TabsContent>
        <TabsContent value="rationaClosure">
          <RationalClosure
            isLoading={reasoner.isPending}
            rationalEntailment={reasoner.rationalEntailment}
          />
        </TabsContent>
        <TabsContent value="lexicographicClosure">
          <LexicographicClosure
            isLoading={reasoner.isPending}
            lexicalEntailment={reasoner.lexicalEntailment}
          />
        </TabsContent>
      </Tabs>
    </div>
  );
}
