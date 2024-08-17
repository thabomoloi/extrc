package com.extrc.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.BaseRank;
import com.extrc.models.KnowledgeBase;
import com.extrc.utils.DefeasibleParser;

public class ReasonerServiceTest {
  private ReasonerService rationalReasonerService;
  private ReasonerService lexicalReasonerService;
  private BaseRankService baseRankService;
  private DefeasibleParser parser;

  @BeforeEach
  public void setUp() {
    rationalReasonerService = ReasonerFactory.createReasoner("rational");
    lexicalReasonerService = ReasonerFactory.createReasoner("lexical");
    baseRankService = new BaseRankServiceImpl();
    parser = new DefeasibleParser();
  }

  @Test
  public void testRationalPenguinsKb() throws IOException {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kb1.txt");
    KnowledgeBase kb = parser.parseInputStream(inputStream);
    BaseRank baseRank = baseRankService.constructBaseRank(kb);

    List<PlFormula> queries = Arrays.asList(parser.parseFormula("p ~> f"),
        parser.parseFormula("p ~> w"), parser.parseFormula("p ~> !f"));

    List<Boolean> expected = Arrays.asList(false, false, true);

    for (int i = 0, n = queries.size(); i < n; i++) {
      var entailment = rationalReasonerService.getEntailment(baseRank, queries.get(i));
      assertEquals(expected.get(i), entailment.getEntailed());
    }
  }

  @Test
  public void testLexicalPenguinsKb() throws IOException {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream("kb1.txt");
    KnowledgeBase kb = parser.parseInputStream(inputStream);
    BaseRank baseRank = baseRankService.constructBaseRank(kb);

    List<PlFormula> queries = Arrays.asList(parser.parseFormula("p ~> f"),
        parser.parseFormula("p ~> w"), parser.parseFormula("p ~> !f"));

    List<Boolean> expected = Arrays.asList(false, true, true);

    for (int i = 0, n = queries.size(); i < n; i++) {
      var entailment = lexicalReasonerService.getEntailment(baseRank, queries.get(i));
      assertEquals(expected.get(i), entailment.getEntailed());
    }
  }
}
