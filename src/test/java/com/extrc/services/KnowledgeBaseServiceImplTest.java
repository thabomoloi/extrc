package com.extrc.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Proposition;

import com.extrc.models.KnowledgeBase;

import io.javalin.http.Context;

public class KnowledgeBaseServiceImplTest {

  private KnowledgeBaseServiceImpl service;
  private Context ctx;

  @BeforeEach
  public void setUp() {
    service = new KnowledgeBaseServiceImpl();
    ctx = mock(Context.class);
  }

  @Test
  public void testGetKnowledgeBaseWhenSessionAttributeIsNull() {
    when(ctx.sessionAttribute("knowledgeBase")).thenReturn(null);

    KnowledgeBase kb = service.getKnowledgeBase(ctx);
    verify(ctx).sessionAttribute("knowledgeBase", kb); // Check that saveKnowledgeBase was called
    assertNotNull(kb);
    assertEquals(4, kb.size()); // Check that default KB has 4 elements
  }

  @Test
  public void testGetKnowledgeBaseWhenSessionAttributeIsNotNull() {
    KnowledgeBase existingKb = new KnowledgeBase();
    existingKb.add(new Implication(new Proposition("x"), new Proposition("y")));
    when(ctx.sessionAttribute("knowledgeBase")).thenReturn(existingKb);

    KnowledgeBase kb = service.getKnowledgeBase(ctx);
    // verify(ctx, never()).sessionAttribute("knowledgeBase", existingKb); // Ensure
    // saveKnowledgeBase is not called
    assertEquals(existingKb, kb);
  }

  @Test
  public void testSaveKnowledgeBase() {
    KnowledgeBase kb = new KnowledgeBase();

    service.saveKnowledgeBase(ctx, kb);
    verify(ctx).sessionAttribute("knowledgeBase", kb);
  }
}
