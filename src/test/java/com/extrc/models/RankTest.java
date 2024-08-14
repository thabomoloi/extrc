package com.extrc.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

public class RankTest {

  @Test
  public void testDefaultConstructor() {
    Rank rank = new Rank();

    assertEquals(0, rank.getRankNumber());
    assertTrue(rank.getFormulas().isEmpty());
  }

  @Test
  public void testConstructorWithRankNumberAndFormulas() {
    int rankNumber = 1;
    PlFormula p = new Proposition("p");
    PlFormula q = new Proposition("q");

    Rank rank = new Rank(rankNumber, Arrays.asList(p, q));

    assertEquals(rankNumber, rank.getRankNumber());
    assertEquals(2, rank.getFormulas().size());
    assertTrue(rank.getFormulas().contains(p));
    assertTrue(rank.getFormulas().contains(q));
  }

  @Test
  public void testCopyConstructor() {
    PlFormula p = new Proposition("p");
    Rank originalRank = new Rank(2, Arrays.asList(p));

    Rank copiedRank = new Rank(originalRank);

    assertEquals(originalRank.getRankNumber(), copiedRank.getRankNumber());
    assertEquals(originalRank.getFormulas(), copiedRank.getFormulas());
  }

  @Test
  public void testGetRankNumber() {
    int rankNumber = 3;
    Rank rank = new Rank(rankNumber, Arrays.asList(new Proposition("p")));

    int result = rank.getRankNumber();

    assertEquals(rankNumber, result);
  }

  @Test
  public void testSetRankNumber() {
    Rank rank = new Rank();
    int newRankNumber = 4;

    rank.setRankNumber(newRankNumber);

    assertEquals(newRankNumber, rank.getRankNumber());
  }

  @Test
  public void testGetFormulas() {
    PlFormula p = new Proposition("p");
    PlFormula q = new Proposition("q");
    Rank rank = new Rank(1, Arrays.asList(p, q));

    KnowledgeBase formulas = rank.getFormulas();

    assertEquals(2, formulas.size());
    assertTrue(formulas.contains(p));
    assertTrue(formulas.contains(q));
  }
}
