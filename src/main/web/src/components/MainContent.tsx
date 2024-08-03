import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import QueryCard from "@/components/QueryCard";
import KnowledgeBaseCard from "@/components/KnowledgeBaseCard";
import Summary from "@/components/main-tabs/Summary";
import BaseRank from "@/components/main-tabs/BaseRank";
import RationalClosure from "@/components/main-tabs/RationalClosure";
import LexicographicClosure from "@/components/main-tabs/LexicographicClosure";

export default function MainContent() {
  return (
    <div className="space-y-6">
      <div className="grid grid-cols-1 sm:grid-cols-[45%,_55%] md:grid-cols-[40%,_60%] gap-4">
        <QueryCard />
        <KnowledgeBaseCard />
      </div>
      <Tabs defaultValue="summary">
        <TabsList className="grid w-full grid-cols-4">
          <TabsTrigger value="summary">Summary</TabsTrigger>
          <TabsTrigger value="baseRank">Base Rank</TabsTrigger>
          <TabsTrigger value="rationaClosure">Rational Closure</TabsTrigger>
          <TabsTrigger value="lexicographicClosure">
            Lexicographic Closure
          </TabsTrigger>
        </TabsList>
        <TabsContent value="summary">
          <Summary />
        </TabsContent>
        <TabsContent value="baseRank">
          <BaseRank />
        </TabsContent>
        <TabsContent value="rationaClosure">
          <RationalClosure />
        </TabsContent>
        <TabsContent value="lexicographicClosure">
          <LexicographicClosure />
        </TabsContent>
      </Tabs>
    </div>
  );
}
