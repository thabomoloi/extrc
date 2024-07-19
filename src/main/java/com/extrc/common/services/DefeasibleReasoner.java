package com.extrc.common.services;

import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.structures.Entailment;

public interface DefeasibleReasoner {
  public Explanation explain(PlFormula formula);

  public Entailment query(PlFormula formula);
}
