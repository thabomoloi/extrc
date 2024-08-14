package com.extrc.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

public class QueryInputTest {

  @Test
  public void testDefaultConstructor() {

    QueryInput queryInput = new QueryInput();

    assertNull(queryInput.getQueryFormula());
    assertTrue(queryInput.getKnowledgeBase().isEmpty());
  }

  @Test
  public void testConstructorWithDefeasibleImplicationAndKnowledgeBase() {

    PlFormula antecedent = new Proposition("p");
    PlFormula consequent = new Proposition("q");
    DefeasibleImplication queryFormula = new DefeasibleImplication(antecedent, consequent);
    KnowledgeBase knowledgeBase = new KnowledgeBase();
    knowledgeBase.add(queryFormula);

    QueryInput queryInput = new QueryInput(queryFormula, knowledgeBase);

    assertEquals(queryFormula, queryInput.getQueryFormula());
    assertEquals(knowledgeBase, queryInput.getKnowledgeBase());
  }

  @Test
  public void testCopyConstructor() {

    PlFormula antecedent = new Proposition("p");
    PlFormula consequent = new Proposition("q");
    DefeasibleImplication queryFormula = new DefeasibleImplication(antecedent, consequent);
    KnowledgeBase knowledgeBase = new KnowledgeBase();
    knowledgeBase.add(queryFormula);
    QueryInput originalQueryInput = new QueryInput(queryFormula, knowledgeBase);

    QueryInput copiedQueryInput = new QueryInput(originalQueryInput);

    assertEquals(originalQueryInput.getQueryFormula(), copiedQueryInput.getQueryFormula());
    assertEquals(originalQueryInput.getKnowledgeBase(), copiedQueryInput.getKnowledgeBase());
  }

  @Test
  public void testGetQueryFormula() {

    PlFormula antecedent = new Proposition("p");
    PlFormula consequent = new Proposition("q");
    DefeasibleImplication queryFormula = new DefeasibleImplication(antecedent, consequent);
    QueryInput queryInput = new QueryInput(queryFormula, new KnowledgeBase());

    PlFormula result = queryInput.getQueryFormula();

    assertEquals(queryFormula, result);
  }

  @Test
  public void testGetKnowledgeBase() {
    PlFormula antecedent = new Proposition("p");
    PlFormula consequent = new Proposition("q");
    DefeasibleImplication queryFormula = new DefeasibleImplication(antecedent, consequent);
    KnowledgeBase knowledgeBase = new KnowledgeBase();
    knowledgeBase.add(queryFormula);
    QueryInput queryInput = new QueryInput(queryFormula, knowledgeBase);

    KnowledgeBase result = queryInput.getKnowledgeBase();

    assertEquals(knowledgeBase, result);
    assertTrue(result.contains(queryFormula));
  }
}
