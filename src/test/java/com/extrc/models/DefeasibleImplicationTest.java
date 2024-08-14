package com.extrc.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.tweetyproject.commons.util.Pair;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.utils.Symbols;

public class DefeasibleImplicationTest {

  @Test
  public void testDefeasibleImplicationWithFormulas() {
    PlFormula a = new Proposition("p");
    PlFormula b = new Proposition("q");
    DefeasibleImplication defeasibleImplication = new DefeasibleImplication(a, b);

    String result = defeasibleImplication.toString();

    String expected = "(p" + Symbols.DEFEASIBLE_IMPLICATION() + "q)";
    assertEquals(expected, result);
  }

  @Test
  public void testDefeasibleImplicationWithPair() {
    PlFormula a = new Proposition("p");
    PlFormula b = new Proposition("q");
    Pair<PlFormula, PlFormula> pair = new Pair<>(a, b);
    DefeasibleImplication defeasibleImplication = new DefeasibleImplication(pair);

    String result = defeasibleImplication.toString();

    String expected = "(p" + Symbols.DEFEASIBLE_IMPLICATION() + "q)";
    assertEquals(expected, result);
  }
}
