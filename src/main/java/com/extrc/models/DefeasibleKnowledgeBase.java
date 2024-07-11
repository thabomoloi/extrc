package com.extrc.models;

import org.tweetyproject.logics.pl.syntax.PlBeliefSet;

public final class DefeasibleKnowledgeBase extends Kb {

  public DefeasibleKnowledgeBase() {
    super(KnowledgeBaseType.DEFEASIBLE);
  }

  public DefeasibleKnowledgeBase(PlBeliefSet formulas) {
    super(formulas, KnowledgeBaseType.DEFEASIBLE);
  }

  public DefeasibleKnowledgeBase(DefeasibleKnowledgeBase knowledgeBase) {
    super(knowledgeBase);
  }

  public DefeasibleKnowledgeBase(ClassicalKnowledgeBase knowledgeBase) {
    super(knowledgeBase.dematerialise());
  }

  public KnowledgeBase getAsDefaultKb() {
    return new KnowledgeBase(this.formulas);
  }

  @Override
  public ClassicalKnowledgeBase materialise() {
    return new ClassicalKnowledgeBase(Kb.materialise(this.formulas));
  }

  @Override
  public DefeasibleKnowledgeBase dematerialise() {
    return new DefeasibleKnowledgeBase(this);
  }

}
