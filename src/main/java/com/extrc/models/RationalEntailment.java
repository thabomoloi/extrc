package com.extrc.models;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

// RationalEntailment class
public class RationalEntailment extends Entailment {

  protected final Ranking removedRanking;

  private RationalEntailment(RationalEntailmentBuilder builder) {
    super(builder);
    this.removedRanking = builder.removedRanking;
  }

  @Override
  public PlFormula getNegation() {
    return queryFormula == null ? null : new Negation(((Implication) queryFormula).getFirstFormula());
  }

  public Ranking getRemovedRanking() {
    return removedRanking;
  }

  public static class RationalEntailmentBuilder extends EntailmentBuilder<RationalEntailmentBuilder> {
    private Ranking removedRanking;

    public RationalEntailmentBuilder withRemovedRanking(Ranking removedRanking) {
      this.removedRanking = removedRanking;
      return this;
    }

    @Override
    protected RationalEntailmentBuilder self() {
      return this;
    }

    @Override
    public RationalEntailment build() {
      return new RationalEntailment(this);
    }
  }
}
