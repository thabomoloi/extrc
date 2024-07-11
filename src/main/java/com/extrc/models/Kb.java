package com.extrc.models;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

public abstract class Kb {
  public enum KnowledgeBaseType {
    /**
     * Represents a knowledge base that has both classical and defeasible
     * statements.
     */
    DEFAULT,

    /** Represents a knowledge base that has only classical statements. */
    CLASSICAL,

    /** Represents a knowledge base that has only defeasible statements. */
    DEFEASIBLE
  }

  protected KnowledgeBaseType knowledgeBaseType;
  protected PlBeliefSet formulas;

  public Kb() {
    this.knowledgeBaseType = KnowledgeBaseType.DEFAULT;
    this.formulas = new PlBeliefSet();
  }

  public Kb(KnowledgeBaseType knowledgeBaseType) {
    this.knowledgeBaseType = knowledgeBaseType;
    this.formulas = new PlBeliefSet();
  }

  public Kb(PlBeliefSet formulas, KnowledgeBaseType knowledgeBaseType) {
    this.knowledgeBaseType = knowledgeBaseType;
    this.formulas = formulas;
  }

  public Kb(Kb knowledgeBase) {
    this.knowledgeBaseType = knowledgeBase.knowledgeBaseType;
    this.formulas = new PlBeliefSet(knowledgeBase.formulas);
  }

  public KnowledgeBaseType getKnowledgeBaseType() {
    return knowledgeBaseType;
  }

  public PlBeliefSet getFormulas() {
    return formulas;
  }

  public void setFormulas(PlBeliefSet formulas) {
    switch (knowledgeBaseType) {
      case CLASSICAL:
        this.formulas = Kb.materialise(formulas);
        break;
      case DEFEASIBLE:
        this.formulas = Kb.dematerialise(formulas);
        break;
      default:
        this.formulas = formulas;
        break;
    }
  }

  public void add(PlFormula formula) {
    switch (knowledgeBaseType) {
      case CLASSICAL:
        this.formulas.add(Kb.materialise(formula));
        break;
      case DEFEASIBLE:
        this.formulas.add(Kb.dematerialise(formula));
        break;
      default:
        this.formulas.add(formula);
        break;
    }
  }

  public void addAll(PlBeliefSet formulas) {
    switch (knowledgeBaseType) {
      case CLASSICAL:
        this.formulas.addAll(Kb.materialise(formulas));
        break;
      case DEFEASIBLE:
        this.formulas.addAll(Kb.dematerialise(formulas));
        break;
      default:
        this.formulas.addAll(formulas);
        break;
    }
  }

  public abstract Kb materialise();

  public abstract Kb dematerialise();

  protected static final PlFormula materialise(PlFormula formula) {
    if (formula instanceof DefeasibleImplication) {
      return new Implication(((DefeasibleImplication) formula).getFormulas());
    }
    return formula;
  }

  protected static final PlBeliefSet materialise(PlBeliefSet formulas) {
    PlBeliefSet materialisedFormulas = new PlBeliefSet();
    for (PlFormula formula : formulas) {
      materialisedFormulas.add(materialise(formula));
    }
    return materialisedFormulas;
  }

  protected static final PlFormula dematerialise(PlFormula formula) {
    if ((formula instanceof Implication)) {
      return new DefeasibleImplication(((Implication) formula).getFormulas());
    }
    return formula;
  }

  protected static final PlBeliefSet dematerialise(PlBeliefSet formulas) {
    PlBeliefSet dematerialisedFormulas = new PlBeliefSet();
    for (PlFormula formula : formulas) {
      dematerialisedFormulas.add(dematerialise(formula));
    }
    return dematerialisedFormulas;
  }

  @Override
  public String toString() {
    return "K = " + this.formulas.toString();
  }

}