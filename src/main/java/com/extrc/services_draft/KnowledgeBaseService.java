package com.extrc.services_draft;

import org.tweetyproject.logics.pl.syntax.PlBeliefSet;

public interface KnowledgeBaseService {
  PlBeliefSet getFormulas();

  void setFormulas(PlBeliefSet formulas);

  PlBeliefSet getClassicalFormulas();

  PlBeliefSet getDefeasibleFormulas();
}
