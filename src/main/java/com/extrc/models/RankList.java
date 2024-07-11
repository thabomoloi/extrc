package com.extrc.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RankList {
  private final List<Rank> ranks;

  public RankList() {
    this.ranks = new ArrayList<>();
  }

  public void addRank(Rank rank) {
    this.ranks.add(rank);
  }

  public void addRank(Rank... ranks) {
    this.ranks.addAll(Arrays.asList(ranks));
  }

  public Rank getRank(int rankNumber) {
    for (Rank rank : this.ranks) {
      if (rank.getRankNumber() == rankNumber) {
        return rank;
      }
    }
    return null;
  }

  public List<Rank> listRanks() {
    return new ArrayList<>(this.ranks);
  }

  public Rank pop() {
    if (!this.ranks.isEmpty()) {
      return this.ranks.remove(0);
    }
    return null;
  }

  public int size() {
    return this.ranks.size();
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (Rank rank : ranks) {
      builder.append(rank).append("\n");
    }
    return builder.toString();
  }
}
