package com.extrc.models;

import org.tweetyproject.logics.pl.syntax.PlBeliefSet;

public final class ClassicalKnowledgeBase extends Kb {
  public ClassicalKnowledgeBase() {
    super(KnowledgeBaseType.CLASSICAL);
  }

  public ClassicalKnowledgeBase(PlBeliefSet formulas) {
    super(formulas, KnowledgeBaseType.CLASSICAL);
  }

  public ClassicalKnowledgeBase(ClassicalKnowledgeBase knowledgeBase) {
    super(knowledgeBase);
  }

  public KnowledgeBase getAsDefaultKb() {
    return new KnowledgeBase(this.formulas);
  }

  @Override
  public ClassicalKnowledgeBase materialise() {
    return new ClassicalKnowledgeBase(this);
  }

  @Override
  public DefeasibleKnowledgeBase dematerialise() {
    return new DefeasibleKnowledgeBase(Kb.materialise(this.formulas));
  }

}
