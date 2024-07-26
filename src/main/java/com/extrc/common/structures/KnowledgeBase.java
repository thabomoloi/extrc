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

  public KnowledgeBase union(KnowledgeBase knowledgeBase) {
    KnowledgeBase result = new KnowledgeBase(this);
    result.addAll(knowledgeBase);
    return result;
  }

  public KnowledgeBase union(Collection<KnowledgeBase> knowledgeBase) {
    KnowledgeBase result = new KnowledgeBase();
    for (KnowledgeBase kb : knowledgeBase) {
      result.addAll(kb);
    }
    return result;
  }

  public KnowledgeBase intersection(KnowledgeBase knowledgeBase) {
    KnowledgeBase result = new KnowledgeBase();
    for (PlFormula formula : this) {
      if (knowledgeBase.contains(formula)) {
        result.add(formula);
      }
    }
    return result;
  }

  public KnowledgeBase difference(KnowledgeBase knowledgeBase) {
    KnowledgeBase result = new KnowledgeBase(this);
    result.removeAll(knowledgeBase);
    return result;
  }

  public KnowledgeBase antecedents() {
    KnowledgeBase antecedents = new KnowledgeBase();
    for (PlFormula formula : this) {
      if (formula instanceof Implication implication) {
        antecedents.add(implication.getFirstFormula());
      }
    }
    return antecedents;
  }

  public KnowledgeBase materialise() {
    KnowledgeBase result = new KnowledgeBase();
    for (PlFormula formula : this) {
      if (formula instanceof DefeasibleImplication defeasibleImplication) {
        result.add(new Implication(defeasibleImplication.getFormulas()));
      }
    }
    return result;
  }

  public KnowledgeBase dematerialise() {
    KnowledgeBase result = new KnowledgeBase();
    for (PlFormula formula : this) {
      if ((formula instanceof Implication implication) && !(formula instanceof DefeasibleImplication)) {
        result.add(new DefeasibleImplication(implication.getFormulas()));
      }
    }
    return result;
  }

  public static PlFormula materialise(PlFormula formula) {
    if (formula instanceof DefeasibleImplication defeasibleImplication) {
      return new Implication(defeasibleImplication.getFormulas());
    }
    return formula;
  }

  public static PlFormula dematerialise(PlFormula formula) {
    if (formula instanceof Implication implication) {
      return new DefeasibleImplication(implication.getFormulas());
    }
    return formula;
  }
}
