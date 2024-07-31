package com.extrc.view.console.components;

import com.extrc.common.structures.EntailmentResult;

public class ExplanationView {
  private final EntailmentResult entailmentResult;

  public ExplanationView(EntailmentResult entailmentResult) {
    this.entailmentResult = entailmentResult;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("K\t= ").append(entailmentResult.getKnowledgeBase()).append("\n");
    sb.append("query\t= ").append(entailmentResult.getFormula()).append("\n\n");
    sb.append(new BaseRankView(entailmentResult.getBaseRank())).append("\n");
    if (entailmentResult.getSubsets().isEmpty()) {
      sb.append(new RationalClosureView(entailmentResult));
    } else {
      sb.append(new LexicalClosureView(entailmentResult));
    }
    return sb.toString();
  }
}
