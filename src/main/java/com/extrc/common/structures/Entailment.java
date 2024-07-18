package com.extrc.common.structures;

import org.tweetyproject.logics.pl.syntax.PlFormula;

public class Entailment {
  private final KnowledgeBase knowledgeBase;
  private final Ranking baseRanking;
  private final Ranking removedRanking;
  private final PlFormula formula;
  private final boolean entailed;

  public Entailment(KnowledgeBase knowledgeBase, Ranking baseRanking, Ranking removedRanking, PlFormula formula,
      boolean entailed) {
    this.knowledgeBase = knowledgeBase;
    this.baseRanking = baseRanking;
    this.removedRanking = removedRanking;
    this.formula = formula;
    this.entailed = entailed;
  }

  public KnowledgeBase getKnowledgeBase() {
    return knowledgeBase;
  }

  public Ranking getBaseRanking() {
    return baseRanking;
  }

  public Ranking getRemovedRanking() {
    return removedRanking;
  }

  public PlFormula getFormula() {
    return formula;
  }

  public boolean isEntailed() {
    return entailed;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("K = ").append(knowledgeBase).append("\n");
    sb.append("query = ").append(formula).append("\n\n");
    sb.append("Base Ranks:\n").append(baseRanking).append("\n\n");
    sb.append("Removed Ranks:\n").append(removedRanking).append("\n\n");
    sb.append("Does K entail the formula ").append(formula).append("? ").append(entailed ? "Yes" : "No").append("\n");
    return sb.toString();
  }
}
