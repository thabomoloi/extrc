package com.extrc.models;

import org.tweetyproject.logics.pl.syntax.PlBeliefSet;

public class Rank {
  private int rankNumber;
  private PlBeliefSet rankStatements;

  public Rank() {
    setRankNumber(0);
    setRankStatements(new PlBeliefSet());
  }

  public Rank(int rankNumber, PlBeliefSet rankStatements) {
    setRankNumber(rankNumber);
    setRankStatements(rankStatements);
  }

  public void setRankNumber(int value) {
    this.rankNumber = value;
  }

  public int getRankNumber() {
    return this.rankNumber;
  }

  public void setRankStatements(PlBeliefSet statements) {
    this.rankStatements = statements;
  }

  public PlBeliefSet getRankStatements() {
    return this.rankStatements;
  }
}
