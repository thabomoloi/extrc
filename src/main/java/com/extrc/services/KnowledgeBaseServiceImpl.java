package com.extrc.services;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.DefeasibleImplication;
import com.extrc.models.KnowledgeBase;

public class KnowledgeBaseServiceImpl implements KnowledgeBaseService {

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
  public KnowledgeBase getKnowledgeBase() {
    return getDefault();
  }

}
