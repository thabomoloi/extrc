package com.extrc.models;

public class BaseRank {
  private final QueryInput queryInput;
  private final Ranking ranking;
  private final Ranking sequence;
  private final double timeTaken;

  public BaseRank() {
    this(new QueryInput(), new Ranking(), new Ranking(), 0);
  }

  public BaseRank(QueryInput queryInput, Ranking sequence, Ranking ranking, double timeTaken) {
    this.sequence = sequence;
    this.ranking = ranking;
    this.timeTaken = timeTaken;
    this.queryInput = queryInput;
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

  public QueryInput getQueryInput() {
    return queryInput;
  }
}
