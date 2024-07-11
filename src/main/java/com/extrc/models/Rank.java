package com.extrc.models;

public class Rank {
  private int rankNumber;
  private KnowledgeBase knowledgeBase;

  public Rank(int rankNumber, KnowledgeBase knowledgeBase) {
    this.rankNumber = rankNumber;
    this.knowledgeBase = knowledgeBase;
  }

  public Rank(Rank rank) {
    this.rankNumber = rank.rankNumber;
    this.knowledgeBase = new KnowledgeBase(rank.knowledgeBase);
  }

  public int getRankNumber() {
    return this.rankNumber;
  }

  public void setRankNumber(int rankNumber) {
    this.rankNumber = rankNumber;
  }

  public KnowledgeBase getKnowledgeBase() {
    return this.knowledgeBase;
  }

  public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
    this.knowledgeBase = knowledgeBase;
  }

  @Override
  public String toString() {
    if (this.rankNumber == Integer.MAX_VALUE) {
      return "Rank ∞: " + this.knowledgeBase;
    }
    return "Rank " + this.rankNumber + ": " + this.knowledgeBase;
  }
}