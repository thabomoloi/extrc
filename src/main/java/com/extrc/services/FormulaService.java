package com.extrc.services;

import org.tweetyproject.logics.pl.syntax.PlFormula;

import io.javalin.http.Context;

public interface FormulaService {
  public PlFormula getQueryFormula(Context ctx);

  public void saveQueryFormula(Context ctx, PlFormula queryFormula);
}
