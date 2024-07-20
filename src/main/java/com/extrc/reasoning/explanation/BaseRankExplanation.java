package com.extrc.reasoning.explanation;

import com.extrc.common.structures.ESequence;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.common.structures.Ranking;

public class BaseRankExplanation {

  private ESequence sequence;
  private Ranking baseRanking;

  public BaseRankExplanation() {
    this.sequence = new ESequence();
    this.baseRanking = new Ranking();
  }

  public void setSequence(ESequence sequence) {
    this.sequence = sequence;
  }

  public void setBaseRanking(Ranking baseRanking) {
    this.baseRanking = baseRanking;
  }

  public Ranking getBaseRanking() {
    return this.baseRanking;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    KnowledgeBase rank = this.baseRanking.isEmpty() ? new KnowledgeBase() : (KnowledgeBase) this.baseRanking.getLast();
    sb.append("Put all classical formulas in infinite rank: R∞ = ").append(rank).append(".\n\n");
    sb.append("Let:\n");
    sb.append(" *  E(0) = { a=>b | a~>b is in K}\n");
    sb.append(" *  E(i+1) = { a=>b | E(i) ∪ R∞ entails !a }:\n");
    sb.append(this.sequence).append("\n\n");
    sb.append("R(i) = E(i) \\ E(i+1), therefore the ranks are as follows:").append("\n");
    sb.append(this.baseRanking);
    return sb.toString();
  }
}
