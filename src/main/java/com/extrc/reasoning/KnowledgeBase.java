package com.extrc.reasoning;

import java.util.Collection;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.draft.models.DefeasibleImplication;

public class KnowledgeBase extends PlBeliefSet {
  public KnowledgeBase() {
    super();
  }

  public KnowledgeBase(Collection<? extends PlFormula> formulas) {
    super(formulas);
  }

  public KnowledgeBase union(KnowledgeBase kb) {
    KnowledgeBase result = new KnowledgeBase(this);
    result.addAll(kb);
    return result;
  }

  public KnowledgeBase intersection(KnowledgeBase kb) {
    KnowledgeBase result = new KnowledgeBase();
    for (PlFormula f : this) {
      if (kb.contains(f)) {
        result.add(f);
      }
    }
    return result;
  }

  public KnowledgeBase difference(KnowledgeBase kb) {
    KnowledgeBase result = new KnowledgeBase(this);
    result.removeAll(kb);
    return result;
  }

  public KnowledgeBase materialise() {
    KnowledgeBase result = new KnowledgeBase();
    for (PlFormula f : this) {
      result.add(KnowledgeBase.materialise(f));
    }
    return result;
  }

  public KnowledgeBase dematerialise() {
    KnowledgeBase result = new KnowledgeBase();
    for (PlFormula f : this) {
      result.add(KnowledgeBase.dematerialise(f));
    }
    return result;
  }

  public static final PlFormula dematerialise(PlFormula formula) {
    if ((formula instanceof Implication)) {
      return new DefeasibleImplication(((Implication) formula).getFormulas());
    }
    return formula;
  }

  public static final PlFormula materialise(PlFormula formula) {
    if (formula instanceof DefeasibleImplication) {
      return new Implication(((DefeasibleImplication) formula).getFormulas());
    }
    return formula;
  }
}
