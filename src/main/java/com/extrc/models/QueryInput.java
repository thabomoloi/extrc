package com.extrc.models;

import org.tweetyproject.logics.pl.syntax.PlFormula;

public class QueryInput {
  private final PlFormula queryFormula; // Converted to string
  private final KnowledgeBase knowledgeBase;

  public QueryInput() {
    this(null, new KnowledgeBase());
  }

  public QueryInput(PlFormula queryFormula, KnowledgeBase knowledgeBase) {
    this.queryFormula = queryFormula;
    this.knowledgeBase = knowledgeBase;
  }

  public PlFormula getQueryFormula() {
    return queryFormula;
  }

  public KnowledgeBase getKnowledgeBase() {
    return knowledgeBase;
  }

}
