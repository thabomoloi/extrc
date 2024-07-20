package com.extrc.common.services;

import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.structures.Ranking;
import com.extrc.reasoning.explanation.BaseRankExplanation;

public interface Explanation {

  void setFormula(PlFormula formula);

  void setEntailed(boolean entailed);

  void setRemovedRanking(Ranking removedRanking);

  BaseRankExplanation getBaseRankExplanation();

  @Override
  String toString();

}
