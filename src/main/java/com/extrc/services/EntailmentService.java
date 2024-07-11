package com.extrc.services;

import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.Entailment;
import com.extrc.models.RankList;

public interface EntailmentService {
  KnowledgeBaseService getKnowledgeBaseService();

  Entailment getEntailment();

  boolean isQueryEntailed();

  int getNumberOfRemovedRanks();

  RankList getBaseRanks();

  void computeEntailment(RankList rankedKb, PlFormula queryFormula);

}
