package com.extrc.models;

import org.tweetyproject.commons.util.Pair;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.utils.Symbols;

/**
 * This class models deasible implication of propositonal logic.
 * 
 * @author Thabo Vincent Moloi
 */
public class DefeasibleImplication extends Implication {

  /**
   * Creates a new implication a~>b with the two given formulas
   * 
   * @param a A propositional formula.
   * @param b A propositional formula.
   */
  public DefeasibleImplication(PlFormula a, PlFormula b) {
    super(a, b);
  }

  /**
   * Creates new defeasible implication with given pair of formulas
   * 
   * @param formulas A pair of formulas.
   */
  public DefeasibleImplication(Pair<PlFormula, PlFormula> formulas) {
    super(formulas);
  }

  @Override
  public String toString() {
    return super.toString().replaceAll(Symbols.IMPLICATION(), Symbols.DEFEASIBLE_IMPLICATION());
  }

}
