package com.extrc.common.services;

import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.structures.ESequence;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.common.structures.Ranking;

public interface Explanation {

  public void setKnowledgeBase(KnowledgeBase kb);

  public void setQueryFormula(PlFormula formula);

  public void setBaseRanking(Ranking baseRanking);

  public void setSequence(ESequence sequence);
}
