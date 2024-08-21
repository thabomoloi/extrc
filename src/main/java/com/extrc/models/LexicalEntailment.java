package com.extrc.models;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

// LexicalEntailment class
public class LexicalEntailment extends Entailment {
  private final Ranking weakenedRanking;

  private LexicalEntailment(LexicalEntailmentBuilder builder) {
    super(builder);
    this.weakenedRanking = builder.weakenedRanking;
  }

  @Override
  public PlFormula getNegation() {
    return queryFormula == null ? null : new Negation(((Implication) queryFormula).getFirstFormula());
  }

  public Ranking getWeakenedRanking() {
    return weakenedRanking;
  }

  public static class LexicalEntailmentBuilder extends EntailmentBuilder<LexicalEntailmentBuilder> {
    private Ranking weakenedRanking;

    public LexicalEntailmentBuilder withWeakenedRanking(Ranking weakenedRanking) {
      this.weakenedRanking = weakenedRanking;
      return this;
    }

    @Override
    protected LexicalEntailmentBuilder self() {
      return this;
    }

    @Override
    public LexicalEntailment build() {
      return new LexicalEntailment(this);
    }
  }
}
