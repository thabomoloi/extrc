package com.extrc.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

public class BaseRankTest {

  @Test
  public void testDefaultConstructor() {

    BaseRank baseRank = new BaseRank();

    assertTrue(baseRank.getRanking().isEmpty());
    assertTrue(baseRank.getSequence().isEmpty());
    assertEquals(0, baseRank.getTimeTaken());
    assertTrue(baseRank.getQueryInput().getKnowledgeBase().isEmpty());
    assertEquals(null, baseRank.getQueryInput().getQueryFormula());
  }

  @Test
  public void testConstructorWithParameters() {

    PlFormula antecedent = new Proposition("p");
    PlFormula consequent = new Proposition("q");
    DefeasibleImplication queryFormula = new DefeasibleImplication(antecedent, consequent);

    KnowledgeBase knowledgeBase = new KnowledgeBase();
    knowledgeBase.add(queryFormula);
    QueryInput queryInput = new QueryInput(queryFormula, knowledgeBase);

    Ranking ranking = new Ranking();
    Ranking sequence = new Ranking();
    ranking.addRank(1, knowledgeBase);
    sequence.addRank(2, knowledgeBase);

    double timeTaken = 5.0;

    BaseRank baseRank = new BaseRank(queryInput, sequence, ranking, timeTaken);

    assertEquals(ranking, baseRank.getRanking());
    assertEquals(sequence, baseRank.getSequence());
    assertEquals(timeTaken, baseRank.getTimeTaken());
    assertEquals(queryInput.getQueryFormula(), baseRank.getQueryInput().getQueryFormula());
    assertEquals(queryInput.getKnowledgeBase(), baseRank.getQueryInput().getKnowledgeBase());
  }

  @Test
  public void testCopyConstructor() {

    PlFormula antecedent = new Proposition("p");
    PlFormula consequent = new Proposition("q");
    DefeasibleImplication queryFormula = new DefeasibleImplication(antecedent, consequent);

    KnowledgeBase knowledgeBase = new KnowledgeBase();
    knowledgeBase.add(queryFormula);
    QueryInput queryInput = new QueryInput(queryFormula, knowledgeBase);

    Ranking ranking = new Ranking();
    Ranking sequence = new Ranking();
    ranking.addRank(1, knowledgeBase);
    sequence.addRank(2, knowledgeBase);

    double timeTaken = 5.0;

    BaseRank originalBaseRank = new BaseRank(queryInput, sequence, ranking, timeTaken);

    BaseRank copiedBaseRank = new BaseRank(originalBaseRank);

    assertEquals(originalBaseRank.getRanking(), copiedBaseRank.getRanking());
    assertEquals(originalBaseRank.getSequence(), copiedBaseRank.getSequence());
    assertEquals(originalBaseRank.getTimeTaken(), copiedBaseRank.getTimeTaken());
    assertEquals(originalBaseRank.getQueryInput().getQueryFormula(), copiedBaseRank.getQueryInput().getQueryFormula());
    assertEquals(originalBaseRank.getQueryInput().getKnowledgeBase(),
        copiedBaseRank.getQueryInput().getKnowledgeBase());
  }

  @Test
  public void testGetRanking() {

    KnowledgeBase knowledgeBase = new KnowledgeBase();
    knowledgeBase.add(new Proposition("p"));
    Ranking ranking = new Ranking();
    ranking.addRank(1, knowledgeBase);

    BaseRank baseRank = new BaseRank(new QueryInput(), new Ranking(), ranking, 0);

    Ranking result = baseRank.getRanking();

    assertEquals(ranking, result);
    assertTrue(result.get(0).getFormulas().contains(new Proposition("p")));
  }

  @Test
  public void testGetSequence() {

    KnowledgeBase knowledgeBase = new KnowledgeBase();
    knowledgeBase.add(new Proposition("q"));
    Ranking sequence = new Ranking();
    sequence.addRank(2, knowledgeBase);

    BaseRank baseRank = new BaseRank(new QueryInput(), sequence, new Ranking(), 0);

    Ranking result = baseRank.getSequence();

    assertEquals(sequence, result);
    assertTrue(result.get(0).getFormulas().contains(new Proposition("q")));
  }

  @Test
  public void testGetTimeTaken() {

    double timeTaken = 5.0;
    BaseRank baseRank = new BaseRank(new QueryInput(), new Ranking(), new Ranking(), timeTaken);

    double result = baseRank.getTimeTaken();

    assertEquals(timeTaken, result);
  }

  @Test
  public void testGetQueryInput() {

    PlFormula queryFormula = new DefeasibleImplication(new Proposition("p"), new Proposition("q"));
    KnowledgeBase knowledgeBase = new KnowledgeBase();
    knowledgeBase.add(queryFormula);

    QueryInput queryInput = new QueryInput(queryFormula, knowledgeBase);
    BaseRank baseRank = new BaseRank(queryInput, new Ranking(), new Ranking(), 0);

    QueryInput result = baseRank.getQueryInput();

    assertEquals(queryInput.getQueryFormula(), result.getQueryFormula());
    assertEquals(queryInput.getKnowledgeBase(), result.getKnowledgeBase());
  }
}
