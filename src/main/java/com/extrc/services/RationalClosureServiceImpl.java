package com.extrc.services;

import com.extrc.models.RankList;

import org.tweetyproject.logics.pl.parser.PlParser;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

public class RationalClosureServiceImpl {
  PlBeliefSet knowledgeBase;
  PlParser parser;
  PlFormula queryFormula;

  public RationalClosureServiceImpl(PlBeliefSet kb, PlFormula formula) {
    this.knowledgeBase = kb;
    this.parser = new PlParser();
    this.queryFormula = formula;
    RankList rankedKb = (new BaseRankServiceImpl(this.knowledgeBase)).computeBaseRank();
    System.out.println("==== Base Ranking ====");
    System.out.println(rankedKb);
  }
}
