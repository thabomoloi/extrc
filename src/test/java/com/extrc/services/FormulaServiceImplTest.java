package com.extrc.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.DefeasibleImplication;

public class FormulaServiceImplTest {

  private FormulaServiceImpl formulaService;

  @BeforeEach
  public void setUp() {
    formulaService = new FormulaServiceImpl();
  }

  @Test
  public void testGetQueryFormulaWhenNoneSet() {

    PlFormula result = formulaService.getQueryFormula();
    Proposition p = new Proposition("p");
    Proposition w = new Proposition("w");
    PlFormula expectedDefault = new DefeasibleImplication(p, w);

    assertEquals(expectedDefault, result);
  }
}
