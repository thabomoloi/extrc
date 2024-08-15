package com.extrc.services;

import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.BaseRank;
import com.extrc.models.Entailment;

public interface ReasonerService {
  public Entailment getEntailment(BaseRank baseRank, PlFormula queryFormula);
}
