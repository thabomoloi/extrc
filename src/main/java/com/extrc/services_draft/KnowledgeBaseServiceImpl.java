package com.extrc.services_draft;

import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.DefeasibleImplication;

public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {

  private PlBeliefSet formulas;
  private PlBeliefSet classicalFormulas;
  private PlBeliefSet defeasibleFormulas;

  public KnowledgeBaseServiceImpl(PlBeliefSet formulas) {
    this.formulas = formulas;
    this.setSeparateFormulas();
  }

  public PlBeliefSet getFormulas() {
    return formulas;
  }

  public void setFormulas(PlBeliefSet formulas) {
    this.formulas = formulas;
    this.setSeparateFormulas();
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
