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
          loading={reasoner.inputPending || reasoner.resultPending}
        />
      </div>
      <div className="flex flex-col gap-6 w-full">
        <QueryInputs
          isLoading={reasoner.inputPending}
          queryInput={reasoner.queryInput}
          submitKnowledgeBase={reasoner.updateKnowledgeBase}
          uploadKnowledgeBase={reasoner.uploadKnowledgeBase}
          submitQuery={reasoner.fetchQueryResult}
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
            {reasoner.queryResult && (
              <Summary
                isLoading={reasoner.resultPending}
                baseRank={reasoner.queryResult.baseRank}
                lexicalEntailment={reasoner.queryResult.lexicalEntailment}
                rationalEntailment={reasoner.queryResult.rationalEntailment}
              />
            )}
          </TabsContent>
          <TabsContent value="baseRank">
            {reasoner.queryResult && (
              <BaseRank
                isLoading={reasoner.resultPending}
                baseRank={reasoner.queryResult.baseRank}
              />
            )}
          </TabsContent>
          <TabsContent value="rationaClosure">
            {reasoner.queryResult && (
              <RationalClosure
                isLoading={reasoner.resultPending}
                rationalEntailment={reasoner.queryResult.rationalEntailment}
              />
            )}
          </TabsContent>
          <TabsContent value="lexicographicClosure">
            {reasoner.queryResult && (
              <LexicographicClosure
                isLoading={reasoner.resultPending}
                lexicalEntailment={reasoner.queryResult.lexicalEntailment}
              />
            )}
          </TabsContent>
        </Tabs>
      </div>
    </>
  );
}
