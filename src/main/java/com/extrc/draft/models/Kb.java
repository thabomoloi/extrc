package com.extrc.draft.models;

import org.tweetyproject.logics.pl.reasoner.SatReasoner;
import org.tweetyproject.logics.pl.reasoner.SimplePlReasoner;
import org.tweetyproject.logics.pl.sat.Sat4jSolver;
import org.tweetyproject.logics.pl.sat.SatSolver;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.structures.DefeasibleImplication;

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

  private static final SatReasoner reasoner;
  private static final SimplePlReasoner classicalReasoner;

  static {
    SatSolver.setDefaultSolver(new Sat4jSolver());
    reasoner = new SatReasoner();
    classicalReasoner = new SimplePlReasoner();
  }

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

  public boolean contains(PlFormula formula) {
    return this.formulas.contains(formula);
  }

  public boolean isEmpty() {
    return this.formulas.isEmpty();
  }

  public int size() {
    return this.formulas.size();
  }

  public boolean equals(Kb kb) {
    return this.knowledgeBaseType == kb.knowledgeBaseType && this.formulas.equals(kb.formulas);
  }

  public boolean removeAll(PlBeliefSet formulas) {
    return this.formulas.removeAll(formulas);
  }

  public boolean remove(PlFormula formula) {
    return this.formulas.remove(formula);
  }

  public boolean removeAll(Kb kb) {
    return this.formulas.removeAll(kb.formulas);
  }

  public abstract Kb materialise();

  public abstract Kb dematerialise();

  public static final PlFormula materialise(PlFormula formula) {
    if (formula instanceof DefeasibleImplication) {
      return new Implication(((DefeasibleImplication) formula).getFormulas());
    }
    return formula;
  }

  public static final PlBeliefSet materialise(PlBeliefSet formulas) {
    PlBeliefSet materialisedFormulas = new PlBeliefSet();
    for (PlFormula formula : formulas) {
      materialisedFormulas.add(materialise(formula));
    }
    return materialisedFormulas;
  }

  public static final PlFormula dematerialise(PlFormula formula) {
    if ((formula instanceof Implication)) {
      return new DefeasibleImplication(((Implication) formula).getFormulas());
    }
    return formula;
  }

  public static final PlBeliefSet dematerialise(PlBeliefSet formulas) {
    PlBeliefSet dematerialisedFormulas = new PlBeliefSet();
    for (PlFormula formula : formulas) {
      dematerialisedFormulas.add(dematerialise(formula));
    }
    return dematerialisedFormulas;
  }

  public static final ClassicalKnowledgeBase getAntecedants(ClassicalKnowledgeBase kb) {
    ClassicalKnowledgeBase antecedants = new ClassicalKnowledgeBase();
    for (PlFormula formula : kb.formulas) {
      antecedants.add(((Implication) formula).getFormulas().getFirst());
    }
    return antecedants;
  }

  public static final boolean isExceptional(ClassicalKnowledgeBase kb, PlFormula formula) {
    return reasoner.query(kb.formulas, new Negation(formula));
  }

  public static final ClassicalKnowledgeBase getExceptionals(ClassicalKnowledgeBase antecedants,
      ClassicalKnowledgeBase knowledgeBase) {
    ClassicalKnowledgeBase exceptionals = new ClassicalKnowledgeBase();

    for (PlFormula antencedant : antecedants.formulas) {
      if (Kb.isExceptional(knowledgeBase, antencedant)) {
        exceptionals.add(antencedant);
      }
    }
    return exceptionals;
  }

  public static final boolean isEntailed(ClassicalKnowledgeBase kb, PlFormula formula) {
    return classicalReasoner.query(kb.formulas, formula);
  }

  @Override
  public String toString() {
    return this.formulas.toString();
  }

}