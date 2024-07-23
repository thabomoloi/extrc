package com.extrc.common.structures;

import java.util.Collection;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

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

  public KnowledgeBase union(Collection<KnowledgeBase> kb) {
    KnowledgeBase result = new KnowledgeBase();
    for (KnowledgeBase k : kb) {
      result = result.union(k);
    }
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

  public KnowledgeBase antecedents() {
    KnowledgeBase antecedents = new KnowledgeBase();
    for (PlFormula f : this) {
      antecedents.add(((Implication) f).getFormulas().getFirst());
    }
    return antecedents;
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
