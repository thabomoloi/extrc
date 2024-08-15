package com.extrc.services;

import com.extrc.models.BaseRank;
import com.extrc.models.KnowledgeBase;

public interface BaseRankService {
  public BaseRank constructBaseRank(KnowledgeBase knowledgeBase);
}
