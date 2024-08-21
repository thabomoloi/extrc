import { TexFormula } from "@/components/latex/TexFormula";
import { texFormula, unionRanks } from "@/lib/latex";
import { remainingRanks } from "@/lib/utils";
import { RankingTable } from "../tables/RankingTable";
import { entailResult } from "@/components/latex/helpers";
import { Ranking, LexicalEntailmentModel } from "@/lib/models";

interface LexEntailmentProps {
  entailment: LexicalEntailmentModel;
  className?: string;
}

interface RankSubsetCheckProps {
  value: Ranking;
  index: number;
  array: Ranking[];
  entailment: LexicalEntailmentModel;
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
      {(index == 0 || index === array.length - 1) && (
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
  entailment: LexicalEntailmentModel;
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
        Refine rank <TexFormula>{`R_{${value.rankNumber}}`}</TexFormula>
      </p>
      {index === entailment.weakenedRanking.length - 1 &&
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
  entailment: LexicalEntailmentModel;
}

function RankTableSection({ entailment }: RankTableSectionProps) {
  return (
    <div>
      <p className="font-medium">Final ranks</p>
      <RankingTable
        ranking={entailment.remainingRanks}
        caption="Ranks refined by Lexicographic Closure"
      />
    </div>
  );
}

interface EntailmentCheckProps {
  entailment: LexicalEntailmentModel;
}

function EntailmentCheck({ entailment }: EntailmentCheckProps) {
  return (
    <div className="space-y-4">
      <p>
        Now we check if the final ranks{" "}
        <TexFormula>
          {`R_\\infty${unionRanks({
            start: entailment.weakenedRanking.length,
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
            start: entailment.weakenedRanking.length,
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

function LexEntailment({ entailment, className }: LexEntailmentProps) {
  const remainingRanking = remainingRanks(
    entailment.baseRanking,
    entailment.weakenedRanking
  );

  return (
    <div className={className}>
      <p>
        Check whether the ranks entail{" "}
        <TexFormula>{texFormula(entailment.negation)}</TexFormula>. If they do,
        Refine the lowest rank finite <TexFormula>{"R_i"}</TexFormula>. If they
        don't we stop the process of refining ranks.
      </p>
      <ul className="ml-8 list-disc">
        <li>
          To refine a rank <TexFormula>{"R_i"}</TexFormula>, we start by looking
          at a list of sets <TexFormula>{"S_{i,k}"}</TexFormula>, which contain
          subsets of <TexFormula>{"R_i"}</TexFormula> of size{" "}
          <TexFormula>{"k"}</TexFormula>. For each subset, we join the
          statements using the <TexFormula>{"\\land"}</TexFormula> connective,
          and then join the resulting statements using the{" "}
          <TexFormula>{"\\lor"}</TexFormula> connective.
        </li>
        <li>
          We start from <TexFormula>{"k = |R_i| - 1"}</TexFormula> and decrease
          to <TexFormula>{"k = 1"}</TexFormula>. We stop when the refined ranks
          do not entail{" "}
          <TexFormula>{texFormula(entailment.negation)}</TexFormula>; otherwise,
          we continue refining ranks until all finite ranks have been refined.
        </li>
      </ul>
      <div className="text-center">
        {entailment.weakenedRanking.map((value, index, array) => (
          <RankSubsetCheck
            key={index}
            value={value}
            index={index}
            array={array}
            entailment={entailment}
            remainingRanking={remainingRanking}
          />
        ))}
        {entailment.baseRanking.length == 1 && (
          <p className="text-muted-foreground text-sm">
            No finite ranks to refine.
          </p>
        )}
        {entailment.weakenedRanking.length == 0 && (
          <p className="text-muted-foreground text-sm">
            The ranks do not entail{" "}
            <TexFormula>{texFormula(entailment.negation)}</TexFormula>.
          </p>
        )}
      </div>
      <RankTableSection entailment={entailment} />
      <EntailmentCheck entailment={entailment} />
    </div>
  );
}

export { LexEntailment };
