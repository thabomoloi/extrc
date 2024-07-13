package com.extrc.draft.services;

import com.extrc.draft.models.KnowledgeBase;
import com.extrc.draft.models.Ranking;

public interface IBaseRank {
  public KnowledgeBase getKnowledgeBase();

  public void setKnowledgeBase(KnowledgeBase knowledgeBase);

  public ExplanationsImpl getExplanations();

  public void setExplanations(ExplanationsImpl explanationImpl);

  public Ranking computeBaseRank();
}
