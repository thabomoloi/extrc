package com.extrc.controllers;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.ErrorResponse;
import com.extrc.utils.DefeasibleParser;

import io.javalin.http.Context;

public class FormulaControllerTest {

  private Context ctx;
  private DefeasibleParser parser;

  @BeforeEach
  public void setUp() {
    ctx = mock(Context.class);
    parser = new DefeasibleParser();
  }

  @Test
  public void testGetQueryFormula() throws Exception {
    FormulaController.getQueryFormula(ctx);

    verify(ctx).status(200);
    @SuppressWarnings("unchecked")
    ArgumentCaptor<Map<String, PlFormula>> captor = ArgumentCaptor.forClass(Map.class);
    verify(ctx).json(captor.capture());

    Map<String, PlFormula> response = captor.getValue();
    assertEquals(parser.parseFormula("p ~> w"), response.get("queryFormula"));
  }

  @Test
  public void testCreateQueryFormulaSuccess() throws Exception {
    String formula = "(a~>b)";

    when(ctx.pathParam("queryFormula")).thenReturn(formula);
    FormulaController.createQueryFormula(ctx);

    verify(ctx).status(200);
    verify(ctx).json(argThat(response -> {
      @SuppressWarnings("unchecked")
      Map<String, PlFormula> map = (Map<String, PlFormula>) response;
      return map.get("queryFormula").toString().equals(formula);
    }));
  }

  @Test
  public void testCreateQueryFormulaNonDefeasibleImplication() {
    String formula = "a => b";
    when(ctx.pathParam("queryFormula")).thenReturn(formula);
    FormulaController.createQueryFormula(ctx);

    verify(ctx).status(400);
    verify(ctx).json(argThat(response -> {
      ErrorResponse errorResponse = (ErrorResponse) response;
      return errorResponse.code == 400;
      // && errorResponse.message.equals("Formula is not defeasible implication.");
    }));
  }

  @Test
  public void testCreateQueryFormulaInvalid() {
    String formula = "p > q";
    when(ctx.pathParam("queryFormula")).thenReturn(formula);
    FormulaController.createQueryFormula(ctx);

    verify(ctx).status(400);
    verify(ctx).json(argThat(response -> {
      ErrorResponse errorResponse = (ErrorResponse) response;
      return errorResponse.code == 400 && errorResponse.message.equals("Invalid formula: " + formula + ".");
    }));
  }

}
