package com.extrc.services_draft;

import org.tweetyproject.logics.pl.syntax.PlFormula;

public interface EntailmentService {
  KnowledgeBaseService getKnowledgeBaseService();

  Entailment getEntailment();

  boolean isQueryEntailed();

  int getNumberOfRemovedRanks();

  RankList getBaseRanks();

  void computeEntailment(RankList rankedKb, PlFormula queryFormula);

}
