import { TexFormula } from "@/components/latex/TexFormula";
import { texFormula, unionRanks } from "@/lib/latex";
import { arrayEquals, remainingRanks } from "@/lib/utils";
import { Entailment } from "@/types";
import { RankingTable } from "../tables/RankingTable";
import { entailResult } from "@/components/latex/helpers";

interface LexEntailmentProps {
  entailment: Entailment;
  className?: string;
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
        remove the formula from the lowest rank, one at a time. To do this, we
        remove formulas from rank <TexFormula>{"R_i"}</TexFormula> until you
        find a subset{" "}
        <TexFormula>{"S_{R_i} \\in \\mathcal{P}(R_i)"}</TexFormula> (where{" "}
        <TexFormula>{"\\mathcal{P}(R_i)"}</TexFormula> is the power set of{" "}
        <TexFormula>{"R_i"}</TexFormula>), starting with the largest subset,
        such that{" "}
        <TexFormula>
          {`R_{\\infty} \\cup S_{R_i} \\cup \\left(\\bigcup_{j=i+1}^{j<n} R_j\\right) \\not\\models ${texFormula(
            entailment.negation
          )}`}
        </TexFormula>
        . If no such subset can be found, the entire rank is removed.
      </p>
      <div className="text-center">
        {entailment.removedRanking.map((value, index, array) => (
          <div key={index} className="space-y-3">
            {index == 2 && index < array.length - 1 && (
              <TexFormula>{"\\vdots"}</TexFormula>
            )}
            {(index < 2 || index == array.length - 1) && (
              <div>
                {arrayEquals(
                  value.formulas,
                  entailment.baseRanking[index].formulas
                ) && (
                  <div className="mb-8">
                    <div>
                      <TexFormula>{`R_{\\infty}\\cup \\left(\\bigcup_{j={${
                        value.rankNumber
                      }}}^{j<${
                        entailment.baseRanking.length - 1
                      }} R_j \\right)\\not\\models ${texFormula(
                        entailment.negation
                      )}`}</TexFormula>
                    </div>
                    <div>
                      <TexFormula>{`\\forall S_{R_{${
                        value.rankNumber
                      }}}\\in \\mathcal{P} \\left(R_{${index}}\\right),\\; R_{\\infty} \\cup S_{R_{${index}}} \\cup \\left(\\bigcup_{j=${
                        value.rankNumber + 1
                      }}^{j<${
                        entailment.baseRanking.length - 1
                      }} R_j\\right) \\models ${texFormula(
                        entailment.negation
                      )}`}</TexFormula>
                    </div>
                    <p>
                      Remove rank{" "}
                      <TexFormula>{`R_{${value.rankNumber}}`}</TexFormula>
                    </p>
                  </div>
                )}
                {!arrayEquals(
                  value.formulas,
                  entailment.baseRanking[index].formulas
                ) && (
                  <div>
                    <TexFormula>{`\\exists S_{R_{${
                      value.rankNumber
                    }}}\\in \\mathcal{P} \\left(R_{${index}}\\right),\\; R_{\\infty} \\cup S_{R_{${index}}} \\cup \\left(\\bigcup_{j=${
                      value.rankNumber + 1
                    }}^{j<${
                      entailment.baseRanking.length - 1
                    }} R_j\\right) \\not\\models ${texFormula(
                      entailment.negation
                    )}`}</TexFormula>
                  </div>
                )}
                {arrayEquals(
                  value.formulas,
                  entailment.baseRanking[index].formulas
                ) &&
                  index == array.length - 1 &&
                  remainingRanking.length > 1 && (
                    <div>
                      <TexFormula>{`R_{\\infty}\\cup \\left(\\bigcup_{j={${
                        remainingRanking[0].rankNumber
                      }}}^{j<${
                        entailment.baseRanking.length - 1
                      }} R_j \\right)\\not\\models ${texFormula(
                        entailment.negation
                      )}`}</TexFormula>
                    </div>
                  )}
              </div>
            )}
          </div>
        ))}
        {remainingRanking.length == 1 && <p>All finite ranks are removed.</p>}
      </div>
      <div>
        <p className="font-medium">Removed ranks</p>
        <RankingTable
          ranking={entailment.removedRanking}
          caption="Ranks removed by Lexicographic Closure"
        />
        <p className="font-medium">Remaining ranks</p>
        <RankingTable ranking={remainingRanking} caption="Remaining ranks" />
      </div>
      <div>
        <div className="space-y-4">
          <p>
            Now we check if{" "}
            <TexFormula>
              {`R_\\infty${unionRanks({
                start: entailment.removedRanking.length,
                ranks: entailment.baseRanking,
              })}`}
            </TexFormula>{" "}
            entails the formula{" "}
            <TexFormula>{texFormula(entailment.queryFormula)}</TexFormula>.
          </p>
          <p>
            If follows that{" "}
            <TexFormula>
              {`R_\\infty${unionRanks({
                start: entailment.removedRanking.length,
                ranks: entailment.baseRanking,
              })} ${
                entailment.entailed ? "\\models" : "\\not\\models"
              }${texFormula(entailment.queryFormula)}`}
            </TexFormula>
            .
          </p>
          <p>Therefore {entailResult(entailment)}.</p>
        </div>
      </div>
    </div>
  );
}

export { LexEntailment };
