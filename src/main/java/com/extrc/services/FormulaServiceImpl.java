package com.extrc.services;

import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.DefeasibleImplication;

public class FormulaServiceImpl implements FormulaService {

  private PlFormula getDefault() {
    Proposition p = new Proposition("p");
    Proposition w = new Proposition("w");
    return new DefeasibleImplication(p, w);
  }

  @Override
  public PlFormula getQueryFormula() {
    return getDefault();
  }
}
