package com.extrc.view.console.components;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.structures.EntailmentResult;
import com.extrc.common.structures.Rank;
import com.extrc.common.structures.Ranking;

public class RationalClosureView {
  public final EntailmentResult entailment;

  public RationalClosureView(EntailmentResult entailment) {
    this.entailment = entailment;
  }

  @Override
  public String toString() {
    Ranking ranking = new Ranking(entailment.getBaseRank().getRanking());
    Ranking removedRanking = entailment.getRemovedRanking();
    PlFormula query = entailment.getFormula();
    Negation negation = new Negation(((Implication) query).getFirstFormula());

    StringBuilder sb = new StringBuilder();
    sb.append("\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n");
    if (removedRanking.isEmpty()) {
      sb.append("Ranks 0 to ∞ do not entail ").append(negation).append("\n");
      sb.append("No ranks are removed.\n");
    } else {
      for (int i = 0, n = removedRanking.size(); i < n; i++) {
        Rank rank = removedRanking.get(i);
        if (n > 4 && i >= 2 && i < n - 2) {
          if (i == 2) {
            sb.append("......");
          }
          continue;
        }
        sb.append("Ranks ").append(rank.getRankNumber()).append(" to ∞ entail ").append(negation)
            .append(". We remove rank ").append(rank.getRankNumber()).append(".\n");
        ranking.remove(0);
        sb.append("Remaining ranks:\n").append(new RankingView(ranking)).append("\n");
        if (i < n - 1) {
          sb.append("\n");
        }
      }
    }
    sb.append("\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n");

    if (entailment.getEntailed()) {
      sb.append("The remaining ranks entail the formula ").append(query).append(".\n");
      sb.append("Therefore ").append(query).append(" is entailed by the knowledge base.\n");
    } else {
      sb.append("The remaining ranks do not entail the formula ").append(query).append(".\n");
      sb.append("Therefore ").append(query).append(" is not entailed by the knowledge base.\n");
    }
    return sb.toString();
  }

}
