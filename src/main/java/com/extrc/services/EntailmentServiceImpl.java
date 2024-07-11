package com.extrc.services;

import com.extrc.models.Entailment;
import com.extrc.models.RankList;

public class EntailmentServiceImpl implements EntailmentService {
  private final KnowledgeBaseService knowledgeBaseService;
  private Entailment entailment;

  public EntailmentServiceImpl(KnowledgeBaseService knowledgeBaseService) {
    this.knowledgeBaseService = knowledgeBaseService;
  }

  public Entailment getEntailment() {
    return entailment;
  }

  @Override
  public KnowledgeBaseService getKnowledgeBaseService() {
    return knowledgeBaseService;
  }

  public boolean isQueryEntailed() {
    return entailment.isEntailed();
  }

  public int getNumberOfRemovedRanks() {
    return entailment.getNumberOfRemovedRanks();
  }

  public RankList getBaseRanks() {
    return entailment.getBaseRanks();
  }

  public void computeEntailment() {

  }

}
