package com.extrc.services;

import org.tweetyproject.logics.pl.syntax.PlBeliefSet;

public interface KnowledgeBaseService {
  PlBeliefSet getFormulas();

  void setFormulas(PlBeliefSet formulas);

  PlBeliefSet getClassicalFormulas();

  PlBeliefSet getDefeasibleFormulas();
}
