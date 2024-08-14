package com.extrc.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import io.javalin.http.Context;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.DefeasibleImplication;
import com.extrc.models.KnowledgeBase;
import com.extrc.models.QueryInput;

public class QueryInputServiceImplTest {

  private QueryInputServiceImpl queryInputService;
  private Context mockContext;

  @BeforeEach
  public void setUp() {
    queryInputService = new QueryInputServiceImpl();
    mockContext = Mockito.mock(Context.class);
  }

  @Test
  public void testGetQueryInputWhenNotSet() {
    // Simulate the session not having the QueryInput set
    Mockito.when(mockContext.sessionAttribute("queryInput")).thenReturn(null);

    QueryInput queryInput = queryInputService.getQueryInput(mockContext);

    // Verify that the default QueryInput was created and returned
    assertNotNull(queryInput);
    assertNotNull(queryInput.getQueryFormula());
    assertNotNull(queryInput.getKnowledgeBase());

    Proposition p = new Proposition("p");
    Proposition w = new Proposition("w");

    // Verify that the default QueryInput formula is the correct one
    assertEquals(new DefeasibleImplication(p, w), queryInput.getQueryFormula());

    // Verify the default knowledge base contains the expected formulas
    KnowledgeBase kb = queryInput.getKnowledgeBase();
    assertNotNull(kb);
    assertEquals(4, kb.size()); // Default KB should have 4 formulas

    // Check specific formulas in the knowledge base
    assertTrue(kb.contains(new Implication(p, new Proposition("b"))));
    assertTrue(kb.contains(new DefeasibleImplication(new Proposition("b"), new Proposition("f"))));
    assertTrue(kb.contains(new DefeasibleImplication(new Proposition("b"), new Proposition("w"))));
    assertTrue(kb.contains(new DefeasibleImplication(p, new Negation(new Proposition("f")))));
  }

  @Test
  public void testGetQueryInputWhenSet() {
    Proposition p = new Proposition("p");
    Proposition w = new Proposition("w");
    KnowledgeBase kb = new KnowledgeBase();
    kb.add(new Implication(p, w));
    QueryInput queryInput = new QueryInput(new DefeasibleImplication(p, w), kb);

    // Simulate the session having the QueryInput already set
    Mockito.when(mockContext.sessionAttribute("queryInput")).thenReturn(queryInput);

    QueryInput retrievedQueryInput = queryInputService.getQueryInput(mockContext);

    // Verify that the returned QueryInput is the same as the one set
    assertNotNull(retrievedQueryInput);
    assertEquals(queryInput.getQueryFormula(), retrievedQueryInput.getQueryFormula());
    assertEquals(queryInput.getKnowledgeBase(), retrievedQueryInput.getKnowledgeBase());
  }

  @Test
  public void testSaveQueryInput() {
    Proposition p = new Proposition("p");
    Proposition w = new Proposition("w");
    KnowledgeBase kb = new KnowledgeBase();
    kb.add(new Implication(p, w));
    QueryInput queryInput = new QueryInput(new DefeasibleImplication(p, w), kb);

    // Call the saveQueryInput method
    queryInputService.saveQueryInput(mockContext, queryInput);

    // Verify that the sessionAttribute method was called with the correct arguments
    Mockito.verify(mockContext).sessionAttribute("queryInput", queryInput);
  }
}
