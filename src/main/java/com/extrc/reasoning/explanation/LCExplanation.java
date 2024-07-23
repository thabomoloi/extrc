package com.extrc.reasoning.explanation;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.services.Explanation;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.common.structures.Rank;
import com.extrc.common.structures.Ranking;

public class LCExplanation implements Explanation {
  private final KnowledgeBase kb;
  private Ranking ranking;
  private PlFormula formula;
  private PlFormula negation;
  private boolean entailed;
  private final BaseRankExplanation baseRankExplanation;
  private Ranking removedRanking;
  private Ranking subsets;
  private Ranking discardedSubsets;

  public LCExplanation(KnowledgeBase kb) {
    this.kb = kb;
    this.entailed = false;
    this.baseRankExplanation = new BaseRankExplanation();
    this.ranking = new Ranking();
    this.removedRanking = new Ranking();
    this.subsets = new Ranking();
    this.discardedSubsets = new Ranking();
  }

  @Override
  public void setFormula(PlFormula formula) {
    this.formula = formula;
    this.negation = new Negation(((Implication) formula).getFormulas().getFirst());
  }

  @Override
  public BaseRankExplanation getBaseRankExplanation() {
    return this.baseRankExplanation;
  }

  @Override
  public void setEntailed(boolean entailed) {
    this.entailed = entailed;
  }

  @Override
  public void setRemovedRanking(Ranking removedRanking) {
    this.removedRanking = removedRanking;
  }

  public void setDiscardedSubsets(Ranking discardedSubsets) {
    this.discardedSubsets = discardedSubsets;
  }

  public void setSubsets(Ranking subsets) {
    this.subsets = subsets;
  }

  @Override
  public String toString() {
    this.ranking = new Ranking(this.baseRankExplanation.getBaseRanking());
    StringBuilder sb = new StringBuilder();
    sb.append("K = ").append(kb).append("\n");
    sb.append("query = ").append(formula).append("\n\n");
    sb.append(this.baseRankExplanation).append("\n\n");
    if (this.removedRanking.isEmpty()) {
      sb.append("Ranks 0 to ∞ do not entail ").append(negation).append("\n");
    } else {
      for (Rank rank : this.removedRanking) {
        sb.append("Ranks ").append(rank.getRankNumber()).append(" to ∞ entail ").append(negation).append("\n\n");
        Ranking curr = new Ranking();
        for (Rank subset : this.subsets) {
          if (rank.getRankNumber() == subset.getRankNumber()) {
            curr.add(subset);
          }
        }

        if (this.ranking.peek().size() == 1) {
          sb.append(String.format("Remove rank %d\n\n", rank.getRankNumber()));
          this.ranking.poll();
          sb.append("Remaining ranks:\n").append(this.ranking).append("\n\n");
        } else {
          for (int j = 0; j < curr.size(); j++) {
            Ranking copyRanking = new Ranking(this.ranking);
            Rank subset = curr.get(j);
            sb.append(
                String.format("Possible ranking (Rank %d subset = %s)\n", subset.getRankNumber(), subset.toString()));
            copyRanking.set(0, subset);
            sb.append(copyRanking).append("\n\n");

            if (rank.containsAll(subset)) {
              sb.append(String.format("Ranks %d to ∞ entail ", rank.getRankNumber())).append(negation)
                  .append("\n");
              if (j == curr.size() - 1 && rank.size() == this.ranking.peek().size()) {
                sb.append(String.format("Discard rank %d.\n\n", rank.getRankNumber()));
                this.ranking.poll();
                sb.append("Remaining ranks:\n").append(this.ranking).append("\n\n");
              } else {
                sb.append("Move to the next subset.\n\n");
              }
            } else {
              sb.append(String.format("Ranks %d to ∞ do not entail ", rank.getRankNumber())).append(negation)
                  .append("\n");
              this.ranking.set(0, subset);
              break;
            }
          }
        }
      }
    }
    if (entailed) {
      sb.append("\nThe remaining ranks entail the formula ").append(formula).append(".\n");
      sb.append("Therefore ").append(formula).append(" is entailed by the knowledge base.\n");
    } else {
      sb.append("\nThe remaining ranks do not entail the formula ").append(formula).append(".\n");
      sb.append("Therefore ").append(formula).append(" is not entailed by the knowledge base.\n");
    }
    return sb.toString();
  }
}
