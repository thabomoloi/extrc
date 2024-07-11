package com.extrc.utils;

import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.DefeasibleImplication;

public final class Helper {
  public static PlBeliefSet getClassicalFormulas(PlBeliefSet knowledgeBase) {
    PlBeliefSet classicalFormulas = new PlBeliefSet();
    for (PlFormula formula : knowledgeBase) {
      if (!(formula instanceof DefeasibleImplication)) {
        classicalFormulas.add(formula);
      }
    }
    return classicalFormulas;
  }

  public static PlBeliefSet getDefeasibleFormulas(PlBeliefSet knowledgeBase) {
    PlBeliefSet defeasibleFormulas = new PlBeliefSet();
    for (PlFormula formula : knowledgeBase) {
      if (formula instanceof DefeasibleImplication) {
        defeasibleFormulas.add(formula);
      }
    }
    return defeasibleFormulas;
  }
}
