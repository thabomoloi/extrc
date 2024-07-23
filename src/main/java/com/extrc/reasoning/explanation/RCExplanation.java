package com.extrc.reasoning.explanation;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.services.Explanation;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.common.structures.Rank;
import com.extrc.common.structures.Ranking;

public class RCExplanation implements Explanation {
  private final KnowledgeBase kb;
  private Ranking ranking;
  private PlFormula formula;
  private PlFormula negation;
  private boolean entailed;
  private final BaseRankExplanation baseRankExplanation;
  private Ranking removedRanking;

  public RCExplanation(KnowledgeBase kb) {
    this.kb = kb;
    this.entailed = false;
    this.baseRankExplanation = new BaseRankExplanation();
    this.ranking = new Ranking();
    this.removedRanking = new Ranking();
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

  @Override
  public String toString() {
    ranking = new Ranking(this.baseRankExplanation.getBaseRanking());
    StringBuilder sb = new StringBuilder();
    sb.append("K = ").append(kb).append("\n");
    sb.append("query = ").append(formula).append("\n\n");
    sb.append(this.baseRankExplanation).append("\n\n");
    if (this.removedRanking.isEmpty()) {
      sb.append("Ranks 0 to ∞ do not entail ").append(negation).append("\n");
    } else {
      for (Rank rank : this.removedRanking) {
        sb.append("Ranks ").append(rank.getRankNumber()).append(" to ∞ entail ").append(negation).append("\n");
        sb.append("Remove rank ").append(rank.getRankNumber()).append("\n\n");
        this.ranking.poll();
        sb.append("Remaining ranks:\n").append(this.ranking).append("\n");
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
