package com.extrc.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.BaseRank;
import com.extrc.models.DefeasibleImplication;
import com.extrc.models.Entailment;
import com.extrc.models.KnowledgeBase;
import com.extrc.models.QueryInput;

public class ReasonerServiceTest {
  private ReasonerService rational;
  private ReasonerService lexical;
  private PlFormula query1;
  private PlFormula query2;
  private KnowledgeBase kb;
  BaseRankService baseRankService;

  @BeforeEach
  public void setUp() {
    baseRankService = new BaseRankServiceImpl();
    rational = ReasonerFactory.createReasoner("rational");
    lexical = ReasonerFactory.createReasoner("lexical");

    // Creating mock formulas
    PlFormula p = new Proposition("p");
    PlFormula b = new Proposition("b");
    PlFormula f = new Proposition("f");
    PlFormula w = new Proposition("w");

    PlFormula pb = new Implication(p, b);
    PlFormula bf = new DefeasibleImplication(b, f);
    PlFormula bw = new DefeasibleImplication(b, w);
    PlFormula pNotF = new DefeasibleImplication(p, new Negation(f));

    query1 = new DefeasibleImplication(p, f);
    query2 = new DefeasibleImplication(p, w);
    kb = new KnowledgeBase(Arrays.asList(pb, bf, bw, pNotF));
  }

  @Test
  public void testGetRationalEntailment() {
    BaseRank baseRank1 = baseRankService.constructBaseRank(new QueryInput(query1, kb));
    BaseRank baseRank2 = baseRankService.constructBaseRank(new QueryInput(query2, kb));

    Entailment entailment1 = rational.getEntailment(baseRank1);
    Entailment entailment2 = rational.getEntailment(baseRank2);

    assertFalse(entailment1.getEntailed());
    assertFalse(entailment2.getEntailed());
  }

  @Test
  public void testGetLexicalEntailment() {
    BaseRank baseRank1 = baseRankService.constructBaseRank(new QueryInput(query1, kb));
    BaseRank baseRank2 = baseRankService.constructBaseRank(new QueryInput(query2, kb));

    Entailment entailment1 = lexical.getEntailment(baseRank1);
    Entailment entailment2 = lexical.getEntailment(baseRank2);

    assertFalse(entailment1.getEntailed());
    assertTrue(entailment2.getEntailed());
  }
}
