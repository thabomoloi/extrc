package com.extrc.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.DefeasibleImplication;
import com.extrc.models.ErrorResponse;
import com.extrc.services.FormulaServiceImpl;
import com.extrc.utils.DefeasibleParser;
import com.extrc.services.FormulaService;

import io.javalin.http.Context;

public class FormulaController {
  private final static FormulaService formulaService = new FormulaServiceImpl();

  public static void getQueryFormula(Context ctx) {
    Map<String, PlFormula> response = new HashMap<>();
    response.put("queryFormula", formulaService.getQueryFormula(ctx));
    ctx.json(response);
  }

  public static void createQueryFormula(Context ctx) {
    String formula = ctx.pathParam("queryFormula");
    DefeasibleParser parser = new DefeasibleParser();

    try {
      PlFormula queryFormula = parser.parseFormula(formula);
      if (queryFormula instanceof DefeasibleImplication) {
        formulaService.saveQueryFormula(ctx, queryFormula);
        Map<String, PlFormula> response = new HashMap<>();
        response.put("queryFormula", formulaService.getQueryFormula(ctx));
        ctx.json(response);
      } else {
        ctx.json(new ErrorResponse(400, "Bad Request", "Formula is not defeasible implication."));
      }
    } catch (IOException | ParserException e) {
      ctx.status(400).json(new ErrorResponse(400, "Bad Request", "Invalid formula: " + formula + "."));
    }
  }
}
