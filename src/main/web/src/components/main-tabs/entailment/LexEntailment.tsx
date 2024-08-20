import { TexFormula } from "@/components/latex/TexFormula";
import { texFormula, unionRanks } from "@/lib/latex";
import { arrayEquals, remainingRanks } from "@/lib/utils";
import { RankingTable } from "../tables/RankingTable";
import { entailResult } from "@/components/latex/helpers";
import { LexicalEntailmentModel, Ranking } from "@/lib/models";

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
  const baseFormulas = entailment.baseRanking[index].formulas;

  return (
    <div className="my-4">
      {index === 2 && index < array.length - 1 && (
        <div>
          <TexFormula>{"\\vdots"}</TexFormula>
        </div>
      )}
      {(index < 2 || index === array.length - 1) && (
        <div>
          {arrayEquals(value.formulas, baseFormulas) && (
            <RankRemoval
              value={value}
              index={index}
              entailment={entailment}
              remainingRanking={remainingRanking}
            />
          )}
          {!arrayEquals(value.formulas, baseFormulas) && (
            <div>
              <div>
                <TexFormula>{`R_{\\infty}\\cup \\left(\\bigcup_{j={${
                  value.rankNumber
                }}}^{j<${
                  entailment.baseRanking.length - 1
                }} \\overrightarrow{R_j} \\right)\\models ${texFormula(
                  entailment.negation
                )}`}</TexFormula>
              </div>
              <TexFormula>{`\\exists S_{R_{${
                value.rankNumber
              }}},\\; R_{\\infty} \\cup \\overrightarrow{S_{R_{${index}}}} \\cup \\left(\\bigcup_{j=${
                value.rankNumber + 1
              }}^{j<${
                entailment.baseRanking.length - 1
              }} \\overrightarrow{R_j}\\right) \\not\\models ${texFormula(
                entailment.negation
              )}`}</TexFormula>
            </div>
          )}
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
    <div className="my-8">
      <div>
        <TexFormula>{`R_{\\infty}\\cup \\left(\\bigcup_{j={${
          value.rankNumber
        }}}^{j<${
          entailment.baseRanking.length - 1
        }} \\overrightarrow{R_j} \\right)\\models ${texFormula(
          entailment.negation
        )}`}</TexFormula>
      </div>
      <div>
        <TexFormula>{`\\forall S_{R_{${
          value.rankNumber
        }}},\\; R_{\\infty} \\cup \\overrightarrow{S_{R_{${index}}}} \\cup \\left(\\bigcup_{j=${
          value.rankNumber + 1
        }}^{j<${
          entailment.baseRanking.length - 1
        }} \\overrightarrow{R_j}\\right) \\models ${texFormula(
          entailment.negation
        )}`}</TexFormula>
      </div>
      <p>
        Remove rank <TexFormula>{`R_{${value.rankNumber}}`}</TexFormula>
      </p>
      {index === entailment.removedRanking.length - 1 &&
        remainingRanking.length > 1 && (
          <div className="my-4">
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
  remainingRanking: Ranking[];
}

function RankTableSection({
  entailment,
  remainingRanking,
}: RankTableSectionProps) {
  return (
    <div>
      <p className="font-medium">Removed ranks</p>
      <RankingTable
        ranking={entailment.removedRanking}
        caption="Ranks removed by Lexicographic Closure"
      />
      <p className="font-medium">Remaining ranks</p>
      <RankingTable ranking={remainingRanking} caption="Remaining ranks" />
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
        Now we check if the remaining ranks{" "}
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

function LexEntailment({ entailment, className }: LexEntailmentProps) {
  const remainingRanking = remainingRanks(
    entailment.baseRanking,
    entailment.removedRanking
  );

  return (
    <div className={className}>
      <p>
        Check whether the ranks entail{" "}
        <TexFormula>{texFormula(entailment.negation)}</TexFormula>. If they do,
      </p>
      <ul className="ml-8 list-disc">
        <li className="-mt-2 ">
          Weaken the lowest finite rank{" "}
          <TexFormula>{"\\mathcal{P}\\left(R_i\\right)"}</TexFormula> be a power
          set of the lowest finite rank <TexFormula>{"R_i"}</TexFormula>.
          Consider all subsets of <TexFormula>{"R_i"}</TexFormula> in{" "}
          <TexFormula>
            {"{\\mathcal{P}(R_i)\\setminus\\left\\{\\emptyset, R_i\\right\\}}"}
          </TexFormula>{" "}
          starting with the largest subset.{" "}
        </li>
        <li>
          If we find subset{" "}
          <TexFormula>
            {
              "S_{R_i}\\in \\mathcal{P}(R_i)\\setminus\\left\\{\\emptyset, R_i\\right\\}"
            }
          </TexFormula>{" "}
          such that{" "}
          <TexFormula>
            {`R_{\\infty} \\cup \\overrightarrow{S_{R_i}} \\cup \\left(\\bigcup_{j=i+1}^{j<${
              entailment.baseRanking.length - 1
            }} \\overrightarrow{R_j}\\right) \\not\\models ${texFormula(
              entailment.negation
            )}`}
          </TexFormula>
          , the formulas of rank <TexFormula>{"R_i"}</TexFormula> are replaced
          with <TexFormula>{"S_{R_i}"}</TexFormula> and we stop removing ranks.
        </li>
        <li>
          If no such subset can be found, the entire rank is removed, and we
          look at the next lowest finite rank.
        </li>
      </ul>
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
        {remainingRanking.length === 1 && <p>All finite ranks are removed.</p>}
      </div>
      <RankTableSection
        entailment={entailment}
        remainingRanking={remainingRanking}
      />
      <EntailmentCheck entailment={entailment} />
    </div>
  );
}

export { LexEntailment };
