package com.extrc.services;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.extrc.models.KnowledgeBase;

public class KnowledgeBaseServiceImplTest {

  private KnowledgeBaseServiceImpl service;

  @BeforeEach
  public void setUp() {
    service = new KnowledgeBaseServiceImpl();
  }

  @Test
  public void testGetKnowledgeBaseWhenSessionAttributeIsNull() {
    KnowledgeBase kb = service.getKnowledgeBase();
    assertNotNull(kb);
    assertEquals(4, kb.size()); // Check that default KB has 4 elements
  }
}
