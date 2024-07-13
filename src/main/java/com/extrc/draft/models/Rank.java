package com.extrc.draft.models;

public class Rank {
  private int rankNumber;
  private Kb knowledgeBase;

  public Rank(int rankNumber, Kb knowledgeBase) {
    this.rankNumber = rankNumber;
    this.knowledgeBase = knowledgeBase;
  }

  public Rank(Rank rank) {
    this.rankNumber = rank.rankNumber;
    switch (rank.knowledgeBase.getKnowledgeBaseType()) {
      case CLASSICAL:
        this.knowledgeBase = new ClassicalKnowledgeBase((ClassicalKnowledgeBase) rank.knowledgeBase);
        break;
      case DEFEASIBLE:
        this.knowledgeBase = new DefeasibleKnowledgeBase((DefeasibleKnowledgeBase) rank.knowledgeBase);
        break;
      default:
        this.knowledgeBase = new KnowledgeBase((KnowledgeBase) rank.knowledgeBase);
        break;
    }
  }

  public int getRankNumber() {
    return this.rankNumber;
  }

  public void setRankNumber(int rankNumber) {
    this.rankNumber = rankNumber;
  }

  public Kb getKnowledgeBase() {
    return this.knowledgeBase;
  }

  public void setKnowledgeBase(Kb knowledgeBase) {
    this.knowledgeBase = knowledgeBase;
  }

  public int size() {
    return this.knowledgeBase.size();
  }

  public boolean isEmpty() {
    return this.knowledgeBase.isEmpty();
  }

  @Override
  public String toString() {
    if (this.rankNumber == Integer.MAX_VALUE) {
      return "Rank ∞: " + this.knowledgeBase;
    }
    return "Rank " + this.rankNumber + ": " + this.knowledgeBase;
  }
}