package com.extrc.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

public class EntailmentTest {

  @Test
  public void testDefaultConstructor() {

    Entailment entailment = new Entailment();

    assertTrue(entailment.getKnowledgeBase().isEmpty());
    assertNull(entailment.getQueryFormula());
    assertNull(entailment.getNegation());
    assertTrue(entailment.getBaseRanking().isEmpty());
    assertTrue(entailment.getRemovedRanking().isEmpty());
    assertTrue(entailment.getSubsets().isEmpty());
    assertFalse(entailment.getEntailed());
    assertEquals(0.0, entailment.getTimeTaken());
  }

  @Test
  public void testConstructorWithParameters() {

    KnowledgeBase knowledgeBase = new KnowledgeBase();
    PlFormula antecedent = new Proposition("p");
    PlFormula consequent = new Proposition("q");
    Implication queryFormula = new Implication(antecedent, consequent);
    Ranking baseRanking = new Ranking();
    Ranking removedRanking = new Ranking();
    Ranking subsets = new Ranking();
    boolean entailed = true;
    double timeTaken = 2.5;

    Entailment entailment = new Entailment(knowledgeBase, queryFormula, baseRanking, removedRanking, subsets, entailed,
        timeTaken);

    assertEquals(knowledgeBase, entailment.getKnowledgeBase());
    assertEquals(queryFormula, entailment.getQueryFormula());
    assertEquals(new Negation(antecedent), entailment.getNegation());
    assertEquals(baseRanking, entailment.getBaseRanking());
    assertEquals(removedRanking, entailment.getRemovedRanking());
    assertEquals(subsets, entailment.getSubsets());
    assertTrue(entailment.getEntailed());
    assertEquals(2.5, entailment.getTimeTaken());
  }

  @Test
  public void testConstructorWithNullQueryFormula() {

    KnowledgeBase knowledgeBase = new KnowledgeBase();
    Ranking baseRanking = new Ranking();
    Ranking removedRanking = new Ranking();
    Ranking subsets = new Ranking();
    boolean entailed = false;
    double timeTaken = 1.0;

    Entailment entailment = new Entailment(knowledgeBase, null, baseRanking, removedRanking, subsets, entailed,
        timeTaken);

    assertEquals(knowledgeBase, entailment.getKnowledgeBase());
    assertNull(entailment.getQueryFormula());
    assertNull(entailment.getNegation());
    assertEquals(baseRanking, entailment.getBaseRanking());
    assertEquals(removedRanking, entailment.getRemovedRanking());
    assertEquals(subsets, entailment.getSubsets());
    assertFalse(entailment.getEntailed());
    assertEquals(1.0, entailment.getTimeTaken());
  }

  @Test
  public void testGetKnowledgeBase() {

    KnowledgeBase knowledgeBase = new KnowledgeBase();
    Entailment entailment = new Entailment(knowledgeBase, null, new Ranking(), new Ranking(), new Ranking(), false,
        0.0);

    KnowledgeBase result = entailment.getKnowledgeBase();

    assertEquals(knowledgeBase, result);
  }

  @Test
  public void testGetQueryFormula() {

    PlFormula antecedent = new Proposition("p");
    PlFormula consequent = new Proposition("q");
    Implication queryFormula = new Implication(antecedent, consequent);
    Entailment entailment = new Entailment(new KnowledgeBase(), queryFormula, new Ranking(), new Ranking(),
        new Ranking(), true, 3.0);

    PlFormula result = entailment.getQueryFormula();

    assertEquals(queryFormula, result);
  }

  @Test
  public void testGetNegation() {

    PlFormula antecedent = new Proposition("p");
    Implication queryFormula = new Implication(antecedent, new Proposition("q"));
    Entailment entailment = new Entailment(new KnowledgeBase(), queryFormula, new Ranking(), new Ranking(),
        new Ranking(), true, 3.0);

    PlFormula result = entailment.getNegation();

    assertEquals(new Negation(antecedent), result);
  }

  @Test
  public void testGetBaseRanking() {

    Ranking baseRanking = new Ranking();
    baseRanking.addRank(1, new KnowledgeBase());
    Entailment entailment = new Entailment(new KnowledgeBase(), null, baseRanking, new Ranking(), new Ranking(), false,
        0.0);

    Ranking result = entailment.getBaseRanking();

    assertEquals(baseRanking, result);
  }

  @Test
  public void testGetRemovedRanking() {

    Ranking removedRanking = new Ranking();
    removedRanking.addRank(2, new KnowledgeBase());
    Entailment entailment = new Entailment(new KnowledgeBase(), null, new Ranking(), removedRanking, new Ranking(),
        false, 0.0);

    Ranking result = entailment.getRemovedRanking();

    assertEquals(removedRanking, result);
  }

  @Test
  public void testGetSubsets() {

    Ranking subsets = new Ranking();
    subsets.addRank(3, new KnowledgeBase());
    Entailment entailment = new Entailment(new KnowledgeBase(), null, new Ranking(), new Ranking(), subsets, false,
        0.0);

    Ranking result = entailment.getSubsets();

    assertEquals(subsets, result);
  }

  @Test
  public void testGetEntailed() {

    boolean entailed = true;
    Entailment entailment = new Entailment(new KnowledgeBase(), null, new Ranking(), new Ranking(), new Ranking(),
        entailed, 0.0);

    boolean result = entailment.getEntailed();

    assertTrue(result);
  }

  @Test
  public void testGetTimeTaken() {

    double timeTaken = 4.5;
    Entailment entailment = new Entailment(new KnowledgeBase(), null, new Ranking(), new Ranking(), new Ranking(),
        false, timeTaken);

    double result = entailment.getTimeTaken();

    assertEquals(timeTaken, result);
  }
}
