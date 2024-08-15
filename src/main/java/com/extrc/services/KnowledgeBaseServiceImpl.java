package com.extrc.services;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.DefeasibleImplication;
import com.extrc.models.KnowledgeBase;

import io.javalin.http.Context;

public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {
  private final String SESSION_KEY = "knowledgeBase";

  private KnowledgeBase getDefault() {
    Proposition p = new Proposition("p");
    Proposition b = new Proposition("b");
    Proposition f = new Proposition("f");
    Proposition w = new Proposition("w");

    KnowledgeBase kb = new KnowledgeBase();
    kb.add(new Implication(p, b));
    kb.add(new DefeasibleImplication(b, f));
    kb.add(new DefeasibleImplication(b, w));
    kb.add(new DefeasibleImplication(p, new Negation(f)));
    return kb;
  }

  @Override
  public KnowledgeBase getKnowledgeBase(Context ctx) {
    KnowledgeBase kb = ctx.sessionAttribute(SESSION_KEY);
    if (kb == null) {
      kb = getDefault();
    }
    saveKnowledgeBase(ctx, kb);
    return kb;
  }

  @Override
  public void saveKnowledgeBase(Context ctx, KnowledgeBase kb) {
    ctx.sessionAttribute(SESSION_KEY, kb);
  }

}
