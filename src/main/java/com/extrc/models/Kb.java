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
    knowledgeBaseType = KnowledgeBaseType.DEFAULT;
    formulas = new PlBeliefSet();
  }

  public KnowledgeBaseType getKnowledgeBaseType() {
    return knowledgeBaseType;
  }

  public PlBeliefSet getFormulas() {
    return formulas;
  }

  public abstract PlBeliefSet setFormulas(PlBeliefSet formulas);

  public abstract void add(PlFormula formula);

  public abstract void addAll(PlBeliefSet formula);

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