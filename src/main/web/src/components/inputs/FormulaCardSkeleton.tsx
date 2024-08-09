import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { TexFormula } from "../latex/TexFormula";
import { Skeleton } from "../ui/skeleton";

export function FormulaCardSkeleton() {
  return (
    <div className="h-full">
      <Card>
        <CardHeader className="space-y-0 pb-4">
          <CardTitle className="text-base text-center font-medium">
            Query Formula <TexFormula>{"(\\alpha)"}</TexFormula>
          </CardTitle>
        </CardHeader>
        <CardContent className="flex justify-center">
          <div className="w-full flex flex-col gap-4 items-center">
            <Skeleton className="w-full h-8" />
            <div className="grid grid-cols-2 gap-4 w-full max-w-sm">
              <Skeleton className="h-9" />
              <Skeleton className="h-9" />
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
