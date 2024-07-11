package com.extrc.services;

import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.RankList;

public class RationalClosureServiceImpl {
  private final PlFormula queryFormula;
  private final KnowledgeBaseService knowledgeBaseService;
  private final EntailmentService entailmentService;
  private final RankList rankedKb;

  public RationalClosureServiceImpl(PlBeliefSet knowledgeBase, PlFormula queryFormula) {
    this.queryFormula = queryFormula;
    this.knowledgeBaseService = new KnowledgeBaseServiceImpl(knowledgeBase);
    this.entailmentService = new EntailmentServiceImpl(knowledgeBaseService);
    this.rankedKb = (new BaseRankServiceImpl(this.knowledgeBaseService)).computeBaseRank();
    this.entailmentService.computeEntailment(this.rankedKb, this.queryFormula);
    System.out.println(entailmentService.getEntailment());

  }
}
