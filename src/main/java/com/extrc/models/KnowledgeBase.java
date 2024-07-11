package com.extrc.models;

import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

public class KnowledgeBase {
  private PlBeliefSet formulas;
  private PlBeliefSet classicalFormulas;
  private PlBeliefSet defeasibleFormulas;

  public KnowledgeBase() {
    this.formulas = new PlBeliefSet();
    this.classicalFormulas = new PlBeliefSet();
    this.defeasibleFormulas = new PlBeliefSet();
  }

  public KnowledgeBase(PlBeliefSet formulas) {
    this.formulas = formulas;
    this.setSeparateFormulas();
  }

  public KnowledgeBase(KnowledgeBase knowledgeBase) {
    this.formulas = new PlBeliefSet(knowledgeBase.formulas);
    this.classicalFormulas = new PlBeliefSet(knowledgeBase.classicalFormulas);
    this.defeasibleFormulas = new PlBeliefSet(knowledgeBase.defeasibleFormulas);
  }

  public PlBeliefSet getFormulas() {
    return formulas;
  }

  public void setFormulas(PlBeliefSet formulas) {
    this.formulas = formulas;
    this.setSeparateFormulas();
  }

  public void add(PlFormula formula) {
    this.formulas.add(formula);
    if (formula instanceof DefeasibleImplication) {
      this.defeasibleFormulas.add(formula);
    } else {
      this.classicalFormulas.add(formula);
    }
  }

  public void addAll(PlBeliefSet formulas) {
    for (PlFormula formula : formulas) {
      this.add(formula);
    }
  }

  public PlBeliefSet getClassicalFormulas() {
    return this.classicalFormulas;
  }

  public PlBeliefSet getDefeasibleFormulas() {
    return this.defeasibleFormulas;
  }

  private void setSeparateFormulas() {
    this.classicalFormulas = new PlBeliefSet();
    this.defeasibleFormulas = new PlBeliefSet();

    for (PlFormula formula : this.formulas) {
      if (formula instanceof DefeasibleImplication) {
        defeasibleFormulas.add(formula);
      } else {
        classicalFormulas.add(formula);
      }
    }
  }

  @Override
  public String toString() {
    return "K = " + this.formulas.toString();
  }

}