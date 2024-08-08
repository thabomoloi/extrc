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

  public BaseRank(BaseRank baseRank) {
    this(baseRank.getQueryInput(), baseRank.getSequence(), baseRank.getRanking(), baseRank.getTimeTaken());
  }

  public Ranking getRanking() {
    return new Ranking(ranking);
  }

  public Ranking getSequence() {
    return new Ranking(sequence);
  }

  public double getTimeTaken() {
    return timeTaken;
  }

  public QueryInput getQueryInput() {
    return new QueryInput(queryInput);
  }
}
