package com.extrc.models;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

// RationalEntailment class
public class RationalEntailment extends Entailment {

  private RationalEntailment(RationalEntailmentBuilder builder) {
    super(builder);
  }

  @Override
  public PlFormula getNegation() {
    return queryFormula == null ? null : new Negation(((Implication) queryFormula).getFirstFormula());
  }

  public static class RationalEntailmentBuilder extends EntailmentBuilder<RationalEntailmentBuilder> {

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
