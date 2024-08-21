package com.extrc.models;

import org.tweetyproject.logics.pl.syntax.PlFormula;

// Base class for Entailment
public abstract class Entailment {
  protected final KnowledgeBase knowledgeBase;
  protected final PlFormula queryFormula;
  protected final Ranking baseRanking;
  protected final Ranking removedRanking;
  protected final boolean entailed;
  protected final double timeTaken;

  protected Entailment(EntailmentBuilder<?> builder) {
    this.knowledgeBase = builder.knowledgeBase;
    this.queryFormula = builder.queryFormula;
    this.baseRanking = builder.baseRanking;
    this.removedRanking = builder.removedRanking;
    this.entailed = builder.entailed;
    this.timeTaken = builder.timeTaken;
  }

  public KnowledgeBase getKnowledgeBase() {
    return knowledgeBase;
  }

  public PlFormula getQueryFormula() {
    return queryFormula;
  }

  public abstract PlFormula getNegation();

  public Ranking getBaseRanking() {
    return baseRanking;
  }

  public Ranking getRemovedRanking() {
    return removedRanking;
  }

  public boolean getEntailed() {
    return entailed;
  }

  public double getTimeTaken() {
    return timeTaken;
  }

  // Builder for Entailment
  public static abstract class EntailmentBuilder<T extends EntailmentBuilder<T>> {
    private KnowledgeBase knowledgeBase;
    private PlFormula queryFormula;
    private Ranking baseRanking;
    private Ranking removedRanking;
    private boolean entailed;
    private double timeTaken;

    public T withKnowledgeBase(KnowledgeBase knowledgeBase) {
      this.knowledgeBase = knowledgeBase;
      return self();
    }

    public T withQueryFormula(PlFormula queryFormula) {
      this.queryFormula = queryFormula;
      return self();
    }

    public T withBaseRanking(Ranking baseRanking) {
      this.baseRanking = baseRanking;
      return self();
    }

    public T withRemovedRanking(Ranking removedRanking) {
      this.removedRanking = removedRanking;
      return self();
    }

    public T withEntailed(boolean entailed) {
      this.entailed = entailed;
      return self();
    }

    public T withTimeTaken(double timeTaken) {
      this.timeTaken = timeTaken;
      return self();
    }

    protected abstract T self();

    public abstract Entailment build();
  }
}
