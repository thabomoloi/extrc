package com.extrc.controllers;

import java.util.HashMap;
import java.util.Map;

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
    response.put("queryFormula", formulaService.getQueryFormula());
    ctx.status(200);
    ctx.json(response);
  }

  public static void createQueryFormula(Context ctx) {
    String formula = ctx.pathParam("queryFormula");
    DefeasibleParser parser = new DefeasibleParser();

    try {
      PlFormula queryFormula = parser.parseFormula(formula);

      if (queryFormula instanceof DefeasibleImplication) {
        Map<String, PlFormula> response = new HashMap<>();
        response.put("queryFormula", queryFormula);
        ctx.status(200);
        ctx.json(response);

      } else {
        ctx.status(400);
        ctx.json(new ErrorResponse(400, "Bad Request", "Formula is not defeasible implication."));
      }
    } catch (Exception e) {
      ctx.status(400);
      ctx.json(new ErrorResponse(400, "Bad Request", "Invalid formula: " + formula + "."));
    }
  }
}
