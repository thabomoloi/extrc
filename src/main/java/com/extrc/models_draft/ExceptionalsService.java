package com.extrc.models_draft;

import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

public class ExceptionalsService {
  public static PlBeliefSet getExceptionals(PlBeliefSet antecedants, PlBeliefSet knowledgeBase) {
    PlBeliefSet exceptionals = new PlBeliefSet();

    for (PlFormula antencedant : antecedants) {
      if (ReasonerService.isExceptional(knowledgeBase, antencedant)) {
        exceptionals.add(antencedant);
      }
    }
    return exceptionals;
  }
}