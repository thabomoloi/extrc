package com.extrc.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.extrc.models.Rank;

public class RankServiceImpl implements RankService {
  private final List<Rank> ranks;

  public RankServiceImpl() {
    this.ranks = new ArrayList<>();
  }

  @Override
  public void addRank(Rank rank) {
    this.ranks.add(rank);
  }

  @Override
  public void addRank(Rank... ranks) {
    this.ranks.addAll(Arrays.asList(ranks));
  }

  @Override
  public Rank getRank(int rankNumber) {
    for (Rank rank : this.ranks) {
      if (rank.getRankNumber() == rankNumber) {
        return rank;
      }
    }
    return null;
  }

  @Override
  public List<Rank> listRanks() {
    return new ArrayList<>(this.ranks);
  }

  @Override
  public Rank popFirstRank() {
    if (!this.ranks.isEmpty()) {
      return this.ranks.remove(0);
    }
    return null;
  }
}
