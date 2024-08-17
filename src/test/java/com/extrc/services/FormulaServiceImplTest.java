package com.extrc.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.DefeasibleImplication;

import io.javalin.http.Context;

public class FormulaServiceImplTest {

  private FormulaServiceImpl formulaService;
  private Context ctx;

  @BeforeEach
  public void setUp() {
    formulaService = new FormulaServiceImpl();
    ctx = mock(Context.class);
  }

  @Test
  public void testGetQueryFormulaWhenNoneSet() {
    // Mock the behavior of sessionAttribute to return null
    when(ctx.sessionAttribute("queryFormula")).thenReturn(null);

    PlFormula result = formulaService.getQueryFormula(ctx);
    Proposition p = new Proposition("p");
    Proposition w = new Proposition("w");
    PlFormula expectedDefault = new DefeasibleImplication(p, w);

    assertEquals(expectedDefault, result);
  }

  @Test
  public void testGetQueryFormulaWhenSet() {
    // Prepare a mock formula
    Proposition p = new Proposition("p");
    Proposition w = new Proposition("w");
    PlFormula mockFormula = new DefeasibleImplication(p, w);

    // Mock the behavior of sessionAttribute to return the mock formula
    when(ctx.sessionAttribute("queryFormula")).thenReturn(mockFormula);

    PlFormula result = formulaService.getQueryFormula(ctx);

    assertEquals(mockFormula, result);
  }

  @Test
  public void testSaveQueryFormula() {
    // Prepare a mock formula
    Proposition p = new Proposition("p");
    Proposition w = new Proposition("w");
    PlFormula mockFormula = new DefeasibleImplication(p, w);

    // Call the method to save the query formula
    formulaService.saveQueryFormula(ctx, mockFormula);

    // Verify that sessionAttribute was called with the correct parameters
    verify(ctx).sessionAttribute("queryFormula", mockFormula);
  }
}
