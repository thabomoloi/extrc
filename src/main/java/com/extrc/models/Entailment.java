package com.extrc.models;

import java.util.List;

public class Entailment {
  private final boolean entailed;
  private final int numberOfRemovedRanks;
  private final RankList rankList;
  private final RankList remainingRanks;

  public Entailment(boolean entailed, int numberOfRemovedRanks, RankList rankList, RankList remainingRanks) {
    this.entailed = entailed;
    this.numberOfRemovedRanks = numberOfRemovedRanks;
    this.rankList = rankList;
    this.remainingRanks = remainingRanks;
  }

  public boolean isEntailed() {
    return entailed;
  }

  public int getNumberOfRemovedRanks() {
    return numberOfRemovedRanks;
  }

  public RankList getBaseRanks() {
    return rankList;
  }

  public RankList getRemovedRanks() {
    RankList removedRanks = new RankList();
    List<Rank> allRanks = rankList.listRanks();
    for (int i = 0; i < numberOfRemovedRanks; i++) {
      removedRanks.addRank(allRanks.get(i));
    }
    return removedRanks;
  }

  public RankList getRemainingRanks() {
    return remainingRanks;
  }

  @Override
  public String toString() {
    return "Entailment{" +
        "entailed=" + entailed +
        ", numberOfRemovedRanks=" + numberOfRemovedRanks +
        ", rankList=" + rankList +
        ", remainingRanks=" + remainingRanks +
        '}';
  }
}
