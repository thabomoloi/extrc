package com.extrc.view.console.components;

import com.extrc.common.structures.EntailmentResult;

public class EntailmentView {
  private final EntailmentResult entailmentResult;

  public EntailmentView(EntailmentResult entailmentResult) {
    this.entailmentResult = entailmentResult;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("K = ").append(entailmentResult.getKnowledgeBase()).append("\n");
    sb.append("query = ").append(entailmentResult.getFormula()).append("\n\n");
    sb.append("Does K entail ").append(entailmentResult.getFormula()).append("? ")
        .append(entailmentResult.getEntailed() ? "Yes" : "No").append("\n\n");
    sb.append("Base ranking:\n").append(new RankingView(entailmentResult.getBaseRank().getRanking())).append("\n\n");
    sb.append("Removed ranking:\n").append(new RankingView(entailmentResult.getRemovedRanking())).append("\n\n");
    sb.append("Time taken:\n").append(new TimingView(entailmentResult.getTimer())).append("\n\n");
    return sb.toString();
  }
}
