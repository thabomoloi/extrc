package com.extrc.services;

import com.extrc.models.KnowledgeBase;
import com.extrc.models.Ranking;

public interface IBaseRank {
  public KnowledgeBase getKnowledgeBase();

  public void setKnowledgeBase(KnowledgeBase knowledgeBase);

  public ExplanationsImpl getExplanations();

  public void setExplanations(ExplanationsImpl explanationImpl);

  public Ranking computeBaseRank();
}
