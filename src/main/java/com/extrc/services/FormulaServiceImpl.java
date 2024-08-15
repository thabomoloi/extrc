package com.extrc.services;

import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.DefeasibleImplication;

import io.javalin.http.Context;

public class FormulaServiceImpl implements FormulaService {

  private final String SESSION_KEY = "queryFormula";

  private PlFormula getDefault() {
    Proposition p = new Proposition("p");
    Proposition w = new Proposition("w");
    return new DefeasibleImplication(p, w);
  }

  @Override
  public PlFormula getQueryFormula(Context ctx) {
    PlFormula queryFormula = ctx.sessionAttribute(SESSION_KEY);
    if (queryFormula == null) {
      queryFormula = getDefault();
    }
    return queryFormula;
  }

  @Override
  public void saveQueryFormula(Context ctx, PlFormula queryFormula) {
    ctx.sessionAttribute(SESSION_KEY, queryFormula);
  }

}
