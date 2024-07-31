package com.extrc.view.console.components;

import com.extrc.common.structures.EntailmentResult;
import com.extrc.common.structures.Rank;
import com.extrc.common.structures.Ranking;

public class RankView {
  public final EntailmentResult entailment;
  public final int rankNumber;

  public RankView(int rankNumber, EntailmentResult entailment) {
    this.rankNumber = rankNumber;
    this.entailment = entailment;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Ranking ranking = new Ranking();
    Ranking removedRanking = new Ranking();
    ranking.add(entailment.getBaseRank().getRanking().get(rankNumber));
    for (Rank rank : entailment.getRemovedRanking()) {
      if (rank.getRankNumber() == rankNumber) {
        removedRanking.add(rank);
      }
    }

    sb.append("K\t= ").append(entailment.getKnowledgeBase()).append("\n");
    sb.append("query\t= ").append(entailment.getFormula()).append("\n\n");
    sb.append("Does K entail ").append(entailment.getFormula()).append("? ")
        .append(entailment.getEntailed() ? "Yes" : "No").append("\n\n");
    sb.append("Formulas for rank ").append(rankNumber).append(":\n");
    sb.append(new RankingView(ranking)).append("\n\n");
    sb.append("Formulas removed from rank ").append(rankNumber).append(":\n");
    sb.append(new RankingView(removedRanking));
    return sb.toString();
  }

}
