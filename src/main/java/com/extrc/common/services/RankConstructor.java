package com.extrc.common.services;

import com.extrc.common.structures.Sequence;
import com.extrc.common.structures.Ranking;

public interface RankConstructor {
  public Sequence getSequence();

  public Ranking getRanking();

  public Ranking construct();
}