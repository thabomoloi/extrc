import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { useReasoner } from "@/hooks/use-reasoner";
import { Summary } from "./main-tabs/Summary";
import { BaseRank } from "./main-tabs/BaseRank";
import { RationalClosure } from "./main-tabs/RationalClosure";
import { LexicographicClosure } from "./main-tabs/LexicographicClosure";
import { QueryInputs } from "./inputs/QueryInputs";
import BarLoader from "react-spinners/BarLoader";
export function MainContent() {
  const reasoner = useReasoner();

  return (
    <>
      <div className="fixed top-0 left-0 w-screen">
        <BarLoader
          cssOverride={{ width: "100vw" }}
          color="#0ea5e9"
          loading={reasoner.queryInputPending || reasoner.resultsPending}
        />
      </div>
      <div className="flex flex-col gap-6">
        <QueryInputs
          isLoading={reasoner.queryInputPending}
          queryInput={reasoner.queryInput}
          submitKnowledgeBase={reasoner.submitKnowledgeBase}
          uploadKnowledgeBase={reasoner.uploadKnowledgeBase}
          submitQuery={reasoner.submitQuery}
          updateFormula={reasoner.updateFormula}
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
              isLoading={reasoner.resultsPending}
              baseRank={reasoner.baseRank}
              lexicalEntailment={reasoner.lexicalEntailment}
              rationalEntailment={reasoner.rationalEntailment}
            />
          </TabsContent>
          <TabsContent value="baseRank">
            <BaseRank
              isLoading={reasoner.resultsPending}
              baseRank={reasoner.baseRank}
            />
          </TabsContent>
          <TabsContent value="rationaClosure">
            <RationalClosure
              isLoading={reasoner.resultsPending}
              rationalEntailment={reasoner.rationalEntailment}
            />
          </TabsContent>
          <TabsContent value="lexicographicClosure">
            <LexicographicClosure
              isLoading={reasoner.resultsPending}
              lexicalEntailment={reasoner.lexicalEntailment}
            />
          </TabsContent>
        </Tabs>
      </div>
    </>
  );
}
