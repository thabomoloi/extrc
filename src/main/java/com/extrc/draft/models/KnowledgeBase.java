package com.extrc.draft.models;

import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

public final class KnowledgeBase extends Kb {

  public KnowledgeBase() {
    super(KnowledgeBaseType.DEFAULT);
  }

  public KnowledgeBase(PlBeliefSet formulas) {
    super(formulas, KnowledgeBaseType.DEFAULT);
  }

  public KnowledgeBase(KnowledgeBase knowledgeBase) {
    super(knowledgeBase);
  }

  public ClassicalKnowledgeBase getClassicalKb() {
    PlBeliefSet classicalFormulas = new PlBeliefSet();
    for (PlFormula formula : this.formulas) {
      if (!(formula instanceof DefeasibleImplication)) {
        classicalFormulas.add(formula);
      }
    }
    return new ClassicalKnowledgeBase(classicalFormulas);
  }

  public DefeasibleKnowledgeBase getDefeasibleKb() {
    PlBeliefSet defeasibleFormulas = new PlBeliefSet();
    for (PlFormula formula : this.formulas) {
      if (formula instanceof DefeasibleImplication) {
        defeasibleFormulas.add(formula);
      }
    }
    return new DefeasibleKnowledgeBase(defeasibleFormulas);
  }

  @Override
  public Kb materialise() {
    throw new UnsupportedOperationException("'materialise' is applicable for DEFAULT KnowledgeBase");
  }

  @Override
  public Kb dematerialise() {
    throw new UnsupportedOperationException("'dematerialise' is not applicable for DEFAULT KnowledgeBase");
  }

}
