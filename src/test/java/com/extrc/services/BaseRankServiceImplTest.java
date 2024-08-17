package com.extrc.services;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.extrc.models.KnowledgeBase;
import com.extrc.utils.DefeasibleParser;

public class BaseRankServiceImplTest {
  private BaseRankService baseRankService;
  private DefeasibleParser parser;

  @BeforeEach
  public void setUp() {
    baseRankService = new BaseRankServiceImpl();
    parser = new DefeasibleParser();
  }

  @Test
  public void testWithEmptyKnowledgeBase() {
    KnowledgeBase kb = new KnowledgeBase();
    var baseRank = baseRankService.constructBaseRank(kb);

    assertNotNull(baseRank);
    assertEquals(0, baseRank.getSequence().size());
    assertTrue(baseRank.getRanking().getLast().getFormulas().isEmpty());
  }

  @Test
  public void testWithPenguinsKnowledgeBase() throws IOException {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kb1.txt");
    KnowledgeBase kb = parser.parseInputStream(inputStream);
    var baseRank = baseRankService.constructBaseRank(kb);

    assertNotNull(baseRank);
    assertEquals(3, baseRank.getSequence().size());
    assertEquals(3, baseRank.getSequence().get(0).getFormulas().size());
    assertEquals(1, baseRank.getSequence().get(1).getFormulas().size());
    assertEquals(0, baseRank.getSequence().get(2).getFormulas().size());

    assertEquals(3, baseRank.getRanking().size());
    assertEquals(2, baseRank.getRanking().get(0).getFormulas().size());
    assertEquals(1, baseRank.getRanking().get(1).getFormulas().size());
    assertEquals(1, baseRank.getRanking().get(2).getFormulas().size());
  }

  @Test
  public void testRanks10() throws IOException {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("ranks10.txt");
    KnowledgeBase kb = parser.parseInputStream(inputStream);
    var baseRank = baseRankService.constructBaseRank(kb);

    assertNotNull(baseRank);
    // 10 finite ranks plus 1 infinite rank
    assertEquals(11, baseRank.getSequence().size());
    assertEquals(11, baseRank.getRanking().size());
  }

}
