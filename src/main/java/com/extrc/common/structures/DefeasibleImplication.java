package com.extrc.common.structures;

import org.tweetyproject.commons.util.Pair;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.utils.Symbols;

/**
 * Implication class Class from TweetyProject.
 * Modified to define defeasible implication class with different symbol
 */
public class DefeasibleImplication extends Implication {

  public DefeasibleImplication(PlFormula a, PlFormula b) {
    super(a, b);
  }

  public DefeasibleImplication(Pair<PlFormula, PlFormula> formulas) {
    super(formulas);
  }

  @Override
  public String toString() {
    return super.toString().replaceAll(Symbols.IMPLICATION(), Symbols.DEFEASIBLE_IMPLICATION());
  }

}
