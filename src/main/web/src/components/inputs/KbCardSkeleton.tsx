import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "../ui/skeleton";
import { TexFormula } from "../latex/TexFormula";

function KbCardSkeleton() {
  return (
    <Card className="h-full">
      <CardHeader className="space-y-0 pb-4">
        <CardTitle className="text-base text-center font-medium">
          Knowledge Base <TexFormula>{"(\\mathcal{K})"}</TexFormula>
        </CardTitle>
      </CardHeader>
      <CardContent>
        <div className="w-full flex flex-col gap-4 items-center">
          <Skeleton className="w-full h-8" />
          <div className="grid grid-cols-2 gap-4 w-full max-w-sm">
            <Skeleton className="h-9" />
            <Skeleton className="h-9" />
          </div>
        </div>
      </CardContent>
    </Card>
  );
}

export { KbCardSkeleton };
