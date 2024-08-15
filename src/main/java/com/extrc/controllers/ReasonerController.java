package com.extrc.controllers;

import java.io.IOException;

import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.BaseRank;
import com.extrc.models.ErrorResponse;
import com.extrc.services.ReasonerFactory;
import com.extrc.services.ReasonerService;
import com.extrc.utils.DefeasibleParser;

import io.javalin.http.Context;

public class ReasonerController {

  public static void getEntailment(Context ctx) {
    String reasonerType = ctx.pathParam("reasoner");
    String formula = ctx.pathParam("queryFormula");

    try {
      DefeasibleParser parser = new DefeasibleParser();
      PlFormula queryFormula = parser.parseFormula(formula);
      BaseRank baseRank = ctx.bodyAsClass(BaseRank.class);
      BaseRank baseRankCopy = new BaseRank(baseRank);
      ReasonerService reasoner = ReasonerFactory.createReasoner(reasonerType);
      ctx.json(reasoner.getEntailment(baseRankCopy, queryFormula));

    } catch (IllegalArgumentException e) {
      ctx.status(400).json(new ErrorResponse(400, "Bad Request", "Invalid reasoner: " + reasonerType));
    } catch (IOException | ParserException e) {
      ctx.status(400).json(new ErrorResponse(400, "Bad Request", "Invalid query formula: " + formula));
    }
  }

}
