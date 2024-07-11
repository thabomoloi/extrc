package com.extrc.services_draft;

import org.tweetyproject.logics.pl.reasoner.SatReasoner;
import org.tweetyproject.logics.pl.sat.Sat4jSolver;
import org.tweetyproject.logics.pl.sat.SatSolver;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.DefeasibleImplication;

public class ReasonerService {
  private static final SatReasoner reasoner;

  static {
    SatSolver.setDefaultSolver(new Sat4jSolver());
    reasoner = new SatReasoner();
  }

  public static boolean isExceptional(PlBeliefSet kb, PlFormula formula) {
    return reasoner.query(kb, new Negation(formula));
  }

  public static PlBeliefSet getExceptionals(PlBeliefSet antecedants, PlBeliefSet knowledgeBase) {
    PlBeliefSet exceptionals = new PlBeliefSet();

    for (PlFormula antencedant : antecedants) {
      if (ReasonerService.isExceptional(knowledgeBase, antencedant)) {
        exceptionals.add(antencedant);
      }
    }
    return exceptionals;
  }

  public static PlBeliefSet extractAntecedants(PlBeliefSet beliefSet) {
    PlBeliefSet antecedants = new PlBeliefSet();
    for (PlFormula formula : beliefSet) {
      antecedants.add(((Implication) formula).getFormulas().getFirst());
    }
    return antecedants;
  }

  public static PlFormula materialise(PlFormula formula) {
    if (formula instanceof DefeasibleImplication) {
      Implication implication = new Implication(((DefeasibleImplication) formula).getFormulas());
      return implication;
    }
    return formula;
  }

  public static PlBeliefSet materialise(PlBeliefSet formulas) {
    PlBeliefSet materialisedFormulas = new PlBeliefSet();
    for (PlFormula formula : formulas) {
      materialisedFormulas.add(materialise(formula));
    }
    return materialisedFormulas;
  }

  public static PlFormula dematerialise(PlFormula formula) {
    if (formula instanceof Implication) {
      DefeasibleImplication implication = new DefeasibleImplication(((Implication) formula).getFormulas());
      return implication;
    }
    return formula;
  }

  public static PlBeliefSet dematerialise(PlBeliefSet formulas) {
    PlBeliefSet dematerialisedFormulas = new PlBeliefSet();
    for (PlFormula formula : formulas) {
      dematerialisedFormulas.add(dematerialise(formula));
    }
    return dematerialisedFormulas;
  }

}
