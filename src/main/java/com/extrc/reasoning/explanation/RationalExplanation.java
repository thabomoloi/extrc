package com.extrc.reasoning.explanation;

import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.services.Explanation;
import com.extrc.common.structures.ESequence;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.common.structures.Ranking;

public class RationalExplanation implements Explanation {
  private ESequence sequence;
  private Ranking baseRanking;
  private KnowledgeBase kb;
  private PlFormula queryFormula;

  public RationalExplanation() {
    this.sequence = new ESequence();
    this.baseRanking = new Ranking();
    this.kb = new KnowledgeBase();
  }

  @Override
  public void setBaseRanking(Ranking baseRanking) {
    this.baseRanking = baseRanking;
  }

  @Override
  public void setSequence(ESequence sequence) {
    this.sequence = sequence;
  }

  @Override
  public void setKnowledgeBase(KnowledgeBase kb) {
    this.kb = kb;
  }

  @Override
  public void setQueryFormula(PlFormula formula) {
    this.queryFormula = formula;
  }

  @Override
  public String toString() {
    StringBuilder explanation = new StringBuilder();
    explanation.append("K = ").append(kb).append("\n");
    explanation.append("query = ").append(queryFormula == null ? "" : queryFormula).append("\n\n");
    KnowledgeBase rank = this.baseRanking.isEmpty() ? new KnowledgeBase() : (KnowledgeBase) this.baseRanking.getLast();
    explanation.append("Put all classical formulas in infinite rank: R∞ = ").append(rank).append(".\n\n");
    explanation.append("Let:\n");
    explanation.append(" *  E(0) = { a=>b | a~>b is in K}\n");
    explanation.append(" *  E(i+1) = { a=>b | E(i) ∪ R∞ entails !a }:\n");
    explanation.append(this.sequence).append("\n\n");
    explanation.append("R(i) = E(i) \\ E(i+1), therefore the ranks are as follows:").append("\n");
    explanation.append(this.baseRanking);
    return explanation.toString();
  }

}
