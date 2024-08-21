import { TexFormula } from "@/components/latex/TexFormula";
import { texFormula, unionRanks } from "@/lib/latex";
import { remainingRanks } from "@/lib/utils";
import { RankingTable } from "../tables/RankingTable";
import { entailResult } from "@/components/latex/helpers";
import { Ranking, RationalEntailmentModel } from "@/lib/models";

interface RatEntailmentProps {
  entailment: RationalEntailmentModel;
  className?: string;
}

interface RankSubsetCheckProps {
  value: Ranking;
  index: number;
  array: Ranking[];
  entailment: RationalEntailmentModel;
  remainingRanking: Ranking[];
}

function RankSubsetCheck({
  value,
  index,
  array,
  entailment,
  remainingRanking,
}: RankSubsetCheckProps) {
  return (
    <div className="space-y-4">
      {index === 1 && index < array.length - 1 && (
        <div>
          <TexFormula>{"\\vdots"}</TexFormula>
        </div>
      )}
      {(index === 0 || index === array.length - 1) && (
        <div>
          <RankRemoval
            value={value}
            index={index}
            entailment={entailment}
            remainingRanking={remainingRanking}
          />
        </div>
      )}
    </div>
  );
}

interface RankRemovalProps {
  value: Ranking;
  index: number;
  entailment: RationalEntailmentModel;
  remainingRanking: Ranking[];
}

function RankRemoval({
  value,
  index,
  entailment,
  remainingRanking,
}: RankRemovalProps) {
  return (
    <div className="my-4">
      <div>
        <TexFormula>{`R_{\\infty}\\cup \\left(\\bigcup_{j={${
          value.rankNumber
        }}}^{j<${
          entailment.baseRanking.length - 1
        }} \\overrightarrow{R_j} \\right)\\models ${texFormula(
          entailment.negation
        )}`}</TexFormula>
      </div>
      <p>
        Remove rank <TexFormula>{`R_{${value.rankNumber}}`}</TexFormula>
      </p>
      {index === entailment.removedRanking.length - 1 &&
        remainingRanking.length > 1 && (
          <div className="my-8">
            <TexFormula>{`R_{\\infty}\\cup \\left(\\bigcup_{j={${
              remainingRanking[0].rankNumber
            }}}^{j<${
              entailment.baseRanking.length - 1
            }} \\overrightarrow{R_j} \\right)\\not\\models ${texFormula(
              entailment.negation
            )}`}</TexFormula>
          </div>
        )}
    </div>
  );
}

interface RankTableSectionProps {
  // entailment: RationalEntailmentModel;
  remainingRanking: Ranking[];
}

function RankTableSection({
  // entailment,
  remainingRanking,
}: RankTableSectionProps) {
  return (
    <div>
      {/* <p className="font-medium">Removed ranks</p>
      <RankingTable
        ranking={entailment.removedRanking}
        caption="Ranks removed by Lexicographic Closure"
      /> */}
      <p className="font-medium">Final ranks</p>
      <RankingTable ranking={remainingRanking} caption="Final ranks" />
    </div>
  );
}

interface EntailmentCheckProps {
  entailment: RationalEntailmentModel;
}

function EntailmentCheck({ entailment }: EntailmentCheckProps) {
  return (
    <div className="space-y-4">
      <p>
        Now we check if the final ranks{" "}
        <TexFormula>
          {`R_\\infty${unionRanks({
            start: entailment.removedRanking.length,
            ranks: entailment.baseRanking,
          })}`}
        </TexFormula>{" "}
        entail the formula{" "}
        <TexFormula>
          {texFormula(entailment.queryFormula.replaceAll("~>", "=>"))}
        </TexFormula>
        .
      </p>
      <p>
        If follows that{" "}
        <TexFormula>
          {`R_\\infty${unionRanks({
            start: entailment.removedRanking.length,
            ranks: entailment.baseRanking,
          })} ${entailment.entailed ? "\\models" : "\\not\\models"}${texFormula(
            entailment.queryFormula.replaceAll("~>", "=>")
          )}`}
        </TexFormula>
        .
      </p>
      <p>Therefore {entailResult(entailment)}.</p>
    </div>
  );
}

function RatEntailment({ entailment, className }: RatEntailmentProps) {
  const remainingRanking = remainingRanks(
    entailment.baseRanking,
    entailment.removedRanking
  );

  return (
    <div className={className}>
      <p>
        Check whether the ranks entail{" "}
        <TexFormula>{texFormula(entailment.negation)}</TexFormula>. If they do,
        Remove the lowest rank finite <TexFormula>{"R_i"}</TexFormula>. If they
        don't we stop the process of removing ranks.
      </p>
      <div className="text-center">
        {entailment.removedRanking.map((value, index, array) => (
          <RankSubsetCheck
            key={index}
            value={value}
            index={index}
            array={array}
            entailment={entailment}
            remainingRanking={remainingRanking}
          />
        ))}
        {entailment.baseRanking.length > 1 && remainingRanking.length === 1 && (
          <p>All finite ranks are removed.</p>
        )}
        {entailment.baseRanking.length == 1 && (
          <p className="text-sm text-muted-foreground">
            No finite ranks to remove.
          </p>
        )}
        {entailment.removedRanking.length == 0 && (
          <p className="text-sm text-muted-foreground">
            The ranks do not entail{" "}
            <TexFormula>{texFormula(entailment.negation)}</TexFormula>.
          </p>
        )}
      </div>
      <RankTableSection
        // entailment={entailment}
        remainingRanking={remainingRanking}
      />
      <EntailmentCheck entailment={entailment} />
    </div>
  );
}

export { RatEntailment };
