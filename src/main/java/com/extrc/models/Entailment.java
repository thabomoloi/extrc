package com.extrc.models;

import org.tweetyproject.logics.pl.syntax.PlFormula;

public class Entailment {
  private final KnowledgeBase knowledgeBase;
  private final PlFormula queryFormula;
  private final Ranking baseRanking;
  private final Ranking removedRanking;
  private final Ranking subsets;
  private final boolean entailed;
  private final double timeTaken;

  public Entailment() {
    this(new KnowledgeBase(), null, new Ranking(), new Ranking(), new Ranking(), false, 0.0);

  }

  public Entailment(KnowledgeBase knowledgeBase, PlFormula queryFormula, Ranking baseRanking,
      Ranking removedRanking, Ranking subsets, boolean entailed, double timeTaken) {
    this.knowledgeBase = knowledgeBase;
    this.queryFormula = queryFormula;
    this.baseRanking = baseRanking;
    this.removedRanking = removedRanking;
    this.subsets = subsets;
    this.entailed = entailed;
    this.timeTaken = timeTaken;
  }

  public KnowledgeBase getKnowledgeBase() {
    return knowledgeBase;
  }

  public PlFormula getQueryFormula() {
    return queryFormula;
  }

  public Ranking getBaseRanking() {
    return baseRanking;
  }

  public Ranking getRemovedRanking() {
    return removedRanking;
  }

  public Ranking getSubsets() {
    return subsets;
  }

  public boolean getEntailed() {
    return entailed;
  }

  public double getTimeTaken() {
    return timeTaken;
  }

}
