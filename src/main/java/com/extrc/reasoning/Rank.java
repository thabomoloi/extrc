package com.extrc.reasoning;

import java.util.Collection;

import org.tweetyproject.logics.pl.syntax.PlFormula;

public class Rank extends KnowledgeBase {
  private int rankNumber;

  public Rank() {
    super();
    this.rankNumber = 0;
  }

  public Rank(int rankNumber, Collection<? extends PlFormula> formulas) {
    super(formulas);
    this.rankNumber = rankNumber;
  }

  public Rank(Rank rank) {
    super(rank);
    this.rankNumber = rank.rankNumber;
  }

  public int getRankNumber() {
    return this.rankNumber;
  }

  public void setRankNumber(int rankNumber) {
    this.rankNumber = rankNumber;
  }

  @Override
  public String toString() {
    if (this.rankNumber == Integer.MAX_VALUE) {
      return "Rank ∞: " + super.toString();
    }
    return "Rank " + rankNumber + ": " + super.toString();
  }
}
