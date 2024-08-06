package com.extrc.models;

public class BaseRank {
  private final Ranking ranking;
  private final Ranking sequence;
  private final double timeTaken;

  public BaseRank(Ranking sequence, Ranking ranking, double timeTaken) {
    this.sequence = sequence;
    this.ranking = ranking;
    this.timeTaken = timeTaken;
  }

  public Ranking getRanking() {
    return ranking;
  }

  public Ranking getSequence() {
    return sequence;
  }

  public double getTimeTaken() {
    return timeTaken;
  }
}
