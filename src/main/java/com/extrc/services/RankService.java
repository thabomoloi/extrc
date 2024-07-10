package com.extrc.services;

import java.util.List;

import com.extrc.models.Rank;

public interface RankService {
  void addRank(Rank rank);

  void addRank(Rank... ranks);

  Rank getRank(int rankNumber);

  List<Rank> listRanks();

  Rank popFirstRank();
}
