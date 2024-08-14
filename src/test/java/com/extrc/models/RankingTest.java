package com.extrc.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

public class RankingTest {

  private Ranking ranking;

  @BeforeEach
  public void setUp() {
    ranking = new Ranking();
  }

  @Test
  public void testDefaultConstructor() {
    Ranking ranking = new Ranking();

    assertTrue(ranking.isEmpty());
  }

  @Test
  public void testConstructorWithRanks() {
    PlFormula p = new Proposition("p");
    PlFormula q = new Proposition("q");
    Rank rank1 = new Rank(0, Arrays.asList(p));
    Rank rank2 = new Rank(1, Arrays.asList(q));
    List<Rank> ranks = Arrays.asList(rank1, rank2);

    Ranking ranking = new Ranking(ranks);

    assertEquals(2, ranking.size());
    assertEquals(rank1, ranking.get(0));
    assertEquals(rank2, ranking.get(1));
  }

  @Test
  public void testAddRank() {
    int rankNumber = 1;
    PlFormula p = new Proposition("p");
    KnowledgeBase knowledgeBase = new KnowledgeBase(Arrays.asList(p));

    ranking.addRank(rankNumber, knowledgeBase);

    assertEquals(1, ranking.size());
    Rank rank = ranking.get(0);
    assertEquals(rankNumber, rank.getRankNumber());
    assertTrue(rank.getFormulas().contains(p));
  }

  @Test
  public void testGetRankByNumber() {
    PlFormula p = new Proposition("p");
    PlFormula q = new Proposition("q");
    ranking.addRank(0, new KnowledgeBase(Arrays.asList(p)));
    ranking.addRank(1, new KnowledgeBase(Arrays.asList(q)));

    Rank rank0 = ranking.getRank(0);
    Rank rank1 = ranking.getRank(1);

    assertEquals(0, rank0.getRankNumber());
    assertTrue(rank0.getFormulas().contains(p));
    assertEquals(1, rank1.getRankNumber());
    assertTrue(rank1.getFormulas().contains(q));
  }

  @Test
  public void testGetRankWithMaxInt() {
    PlFormula p = new Proposition("p");
    PlFormula q = new Proposition("q");
    ranking.addRank(0, new KnowledgeBase(Arrays.asList(p)));
    ranking.addRank(Integer.MAX_VALUE, new KnowledgeBase(Arrays.asList(q)));

    Rank rankMax = ranking.getRank(Integer.MAX_VALUE);

    assertEquals(Integer.MAX_VALUE, rankMax.getRankNumber());
    assertTrue(rankMax.getFormulas().contains(q));
  }
}
