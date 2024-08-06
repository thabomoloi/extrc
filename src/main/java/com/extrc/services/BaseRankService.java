package com.extrc.services;

import com.extrc.models.BaseRank;
import com.extrc.models.QueryInput;

public interface BaseRankService {
  public BaseRank constructBaseRank(QueryInput queryInput);
}
