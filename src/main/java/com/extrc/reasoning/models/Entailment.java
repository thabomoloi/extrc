package com.extrc.reasoning.models;

import org.tweetyproject.logics.pl.syntax.PlFormula;

public class Entailment {
  private final KnowledgeBase knowledgeBase;
  private final Ranking baseRanking;
  private final Ranking removedRanking;
  private final PlFormula formula;

  public Entailment(KnowledgeBase knowledgeBase, Ranking baseRanking, Ranking removedRanking, PlFormula formula) {
    this.knowledgeBase = knowledgeBase;
    this.baseRanking = baseRanking;
    this.removedRanking = removedRanking;
    this.formula = formula;
  }

  public KnowledgeBase getKnowledgeBase() {
    return knowledgeBase;
  }

  public Ranking getBaseRanking() {
    return baseRanking;
  }

  public Ranking getRemovedRanking() {
    return removedRanking;
  }

  public PlFormula getFormula() {
    return formula;
  }
}
