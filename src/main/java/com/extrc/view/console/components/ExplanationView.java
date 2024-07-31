package com.extrc.view.console.components;

import com.extrc.common.structures.EntailmentResult;

public class ExplanationView {
  private final String algorithm;
  private final EntailmentResult entailmentResult;

  public ExplanationView(String algorithm, EntailmentResult entailmentResult) {
    this.algorithm = algorithm;
    this.entailmentResult = entailmentResult;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("K\t= ").append(entailmentResult.getKnowledgeBase()).append("\n");
    sb.append("query\t= ").append(entailmentResult.getFormula()).append("\n\n");
    sb.append(new BaseRankView(entailmentResult.getBaseRank()));
    return sb.toString();
  }
}
