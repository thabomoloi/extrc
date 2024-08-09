import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { TexFormula } from "../latex/TexFormula";
import { kb } from "../latex/helpers";
import { KbForm } from "./kb-form";

interface KbCardProps {
  knowledgeBase: string[];
  submitKnowledgeBase: (knowledgeBase: string[]) => void;
  uploadKnowledgeBase: (data: FormData) => void;
}

function KbCard({
  knowledgeBase,
  submitKnowledgeBase,
  uploadKnowledgeBase,
}: KbCardProps) {
  const [state, setState] = useState({ editing: false, fromFile: false });

  const handleEdit = () => {
    setState((prev) => ({ ...prev, editing: true, fromFile: false }));
  };

  const handleUpload = () => {
    setState((prev) => ({ ...prev, editing: true, fromFile: true }));
  };

  const handleReset = () => {
    setState((prev) => ({ ...prev, editing: false, fromFile: false }));
  };

  return (
    <Card className="h-full">
      <CardHeader className="space-y-0 pb-4">
        <CardTitle className="text-base text-center font-medium">
          Knowledge Base <TexFormula>{"(\\mathcal{K})"}</TexFormula>
        </CardTitle>
      </CardHeader>
      <CardContent>
        {!state.editing && (
          <div className="w-full flex flex-col gap-4 items-center">
            {kb({ formulas: knowledgeBase })}
            <div className="grid grid-cols-2 gap-4 w-full max-w-sm">
              <Button variant="outline" onClick={handleUpload}>
                Upload
              </Button>
              <Button variant="secondary" onClick={handleEdit}>
                Edit
              </Button>
            </div>
          </div>
        )}
        {state.editing && (
          <KbForm
            defaultFormulas={knowledgeBase.join(",")}
            fromFile={state.fromFile}
            submitKnowledgeBase={submitKnowledgeBase}
            handleReset={handleReset}
            uploadKnowledgeBase={uploadKnowledgeBase}
          />
        )}
      </CardContent>
    </Card>
  );
}

export { KbCard };
