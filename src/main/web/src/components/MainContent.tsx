import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import QueryCard from "@/components/QueryCard";
import KnowledgeBaseCard from "@/components/KnowledgeBaseCard";
import Summary from "@/components/main-tabs/Summary";
import BaseRank from "@/components/main-tabs/BaseRank";
import RationalClosure from "@/components/main-tabs/RationalClosure";
import LexicographicClosure from "@/components/main-tabs/LexicographicClosure";
import { useEntailment } from "@/hooks/use-entailment";
import { useEffect } from "react";
import axios from "axios";

export default function MainContent() {
  // const {
  //   rationaEntailment,
  //   lexicalEntailment,
  //   fetchLexicalEntailment,
  //   fetchRationalEntailment,
  // } = useEntailment();

  // const computeEntailment = async () => {
  //   await Promise.all([fetchLexicalEntailment(), fetchRationalEntailment()]);
  // };

  useEffect(() => {
    const data = { queryFormula: "p~>f", knowledgeBase: ["p~>f", "p=>b"] };
    axios.post("/api/query", data).then((resp) => console.log(resp.data));
  }, []);

  return (
    <div className="flex flex-col gap-6">
      {/* <div className="grid grid-cols-1 sm:grid-cols-[45%,_55%] md:grid-cols-[40%,_60%] gap-4">
        <QueryCard handleQuerySubmit={() => computeEntailment()} />
        <KnowledgeBaseCard />
      </div>
      <Tabs defaultValue="summary">
        <TabsList className="flex items-center justify-start flex-wrap h-auto space-y-1'">
          <TabsTrigger value="summary">Summary</TabsTrigger>
          <TabsTrigger value="baseRank">Base Rank</TabsTrigger>
          <TabsTrigger value="rationaClosure">Rational Closure</TabsTrigger>
          <TabsTrigger value="lexicographicClosure">
            Lexicographic Closure
          </TabsTrigger>
        </TabsList>
        <TabsContent value="summary">
          <Summary
            lexicalEntailment={lexicalEntailment}
            rationalEntailment={rationaEntailment}
          />
        </TabsContent>
        <TabsContent value="baseRank">
          <BaseRank
            lexicalEntailment={lexicalEntailment}
            rationalEntailment={rationaEntailment}
          />
        </TabsContent>
        <TabsContent value="rationaClosure">
          <RationalClosure rationalEntailment={rationaEntailment} />
        </TabsContent>
        <TabsContent value="lexicographicClosure">
          <LexicographicClosure lexicalEntailment={lexicalEntailment} />
        </TabsContent>
      </Tabs> */}
    </div>
  );
}
