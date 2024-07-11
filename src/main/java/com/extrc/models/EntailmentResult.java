package com.extrc.models;

import org.tweetyproject.logics.pl.syntax.PlFormula;

public class EntailmentResult {
  private KnowledgeBase knowledgeBase;
  private Ranking baseRanking;
  private Ranking removedRanking;
  private PlFormula queryFormula;
  private boolean entailed;

  public EntailmentResult(KnowledgeBase knowledgeBase, Ranking baseRanking, Ranking removedRanking, boolean entailed,
      PlFormula queryFormula) {
    this.knowledgeBase = knowledgeBase;
    this.baseRanking = baseRanking;
    this.removedRanking = removedRanking;
    this.queryFormula = queryFormula;
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

  public PlFormula getQueryFormula() {
    return queryFormula;
  }

  public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
    this.knowledgeBase = knowledgeBase;
  }

  public void setBaseRanking(Ranking baseRanking) {
    this.baseRanking = baseRanking;
  }

  public void setRemovedRanking(Ranking removedRanking) {
    this.removedRanking = removedRanking;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("K = ").append(knowledgeBase).append("\n");
    sb.append("α = ").append(queryFormula).append("\n\n");
    sb.append("Base Ranks:\n=====================\n").append(baseRanking).append("\n\n");
    sb.append("Removed Ranks:\n=====================\n").append(removedRanking).append("\n\n");
    sb.append("Does K entail α: ").append(entailed).append("\n");
    return sb.toString();
  }

  public void setQueryFormula(PlFormula queryFormula) {
    this.queryFormula = queryFormula;
  }

  public boolean isEntailed() {
    return entailed;
  }

  public void setEntailed(boolean entailed) {
    this.entailed = entailed;
  }
}
