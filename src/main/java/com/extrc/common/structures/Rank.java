package com.extrc.common.structures;

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

  public String formulasToString() {
    return super.toString().replaceAll("[{}]", "");
  }
}
