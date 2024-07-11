package com.extrc.services_draft;

import org.tweetyproject.logics.pl.syntax.PlBeliefSet;

public class RankDraft {
  private int rankNumber;
  private PlBeliefSet formulas;

  public RankDraft(int rankNumber, PlBeliefSet formulas) {
    this.rankNumber = rankNumber;
    this.formulas = formulas;
  }

  public int getRankNumber() {
    return this.rankNumber;
  }

  public void setRankNumber(int rankNumber) {
    this.rankNumber = rankNumber;
  }

  public PlBeliefSet getFormulas() {
    return this.formulas;
  }

  public void setFormulas(PlBeliefSet formulas) {
    this.formulas = formulas;
  }

  @Override
  public String toString() {
    if (this.rankNumber == Integer.MAX_VALUE) {
      return "Rank ∞: " + this.formulas.toString();
    }
    return "Rank " + this.rankNumber + ": " + this.formulas.toString();
  }
}
