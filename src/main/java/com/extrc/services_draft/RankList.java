package com.extrc.services_draft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.tweetyproject.logics.pl.syntax.PlBeliefSet;

public class RankList {
  private final List<RankDraft> ranks;

  public RankList() {
    this.ranks = new ArrayList<>();
  }

  public void addRank(RankDraft rank) {
    this.ranks.add(rank);
  }

  public void addRank(RankDraft... ranks) {
    this.ranks.addAll(Arrays.asList(ranks));
  }

  public RankDraft getRank(int rankNumber) {
    for (RankDraft rank : this.ranks) {
      if (rank.getRankNumber() == rankNumber) {
        return rank;
      }
    }
    return null;
  }

  public List<RankDraft> listRanks() {
    return new ArrayList<>(this.ranks);
  }

  public RankDraft pop() {
    if (!this.ranks.isEmpty()) {
      return this.ranks.remove(0);
    }
    return null;
  }

  public int size() {
    return this.ranks.size();
  }

  public PlBeliefSet getAllFormulas() {
    PlBeliefSet combined = new PlBeliefSet();
    for (RankDraft rank : this.ranks) {
      combined.addAll(rank.getFormulas());
    }
    return combined;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (RankDraft rank : ranks) {
      builder.append(rank).append("\n");
    }
    return builder.toString();
  }
}
