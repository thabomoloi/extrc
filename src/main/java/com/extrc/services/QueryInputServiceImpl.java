package com.extrc.services;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.DefeasibleImplication;
import com.extrc.models.KnowledgeBase;
import com.extrc.models.QueryInput;

import io.javalin.http.Context;

public class QueryInputServiceImpl implements QueryInputService {
  private final String SESSION_KEY = "queryInput";

  private QueryInput getDefault() {
    Proposition p = new Proposition("p");
    Proposition b = new Proposition("b");
    Proposition f = new Proposition("f");
    Proposition w = new Proposition("w");

    KnowledgeBase kb = new KnowledgeBase();
    kb.add(new Implication(p, b));
    kb.add(new DefeasibleImplication(b, f));
    kb.add(new DefeasibleImplication(b, w));
    kb.add(new DefeasibleImplication(p, new Negation(f)));
    return new QueryInput(new DefeasibleImplication(p, f), kb);
  }

  @Override
  public QueryInput getQueryInput(Context ctx) {
    QueryInput queryInput = ctx.sessionAttribute(SESSION_KEY);
    if (queryInput == null) {
      queryInput = getDefault();
    }
    saveQueryInput(ctx, queryInput);
    return queryInput;
  }

  @Override
  public void saveQueryInput(Context ctx, QueryInput queryInput) {
    ctx.sessionAttribute(SESSION_KEY, queryInput);
  }
}
