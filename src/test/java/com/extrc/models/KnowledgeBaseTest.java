package com.extrc.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.PlFormula;
import org.tweetyproject.logics.pl.syntax.Proposition;

public class KnowledgeBaseTest {

  @Test
  public void testConstructorWithFormulas() {
    PlFormula a = new Proposition("p");
    PlFormula b = new Proposition("q");
    List<PlFormula> formulas = Arrays.asList(a, b);

    KnowledgeBase knowledgeBase = new KnowledgeBase(formulas);

    assertTrue(knowledgeBase.contains(a));
    assertTrue(knowledgeBase.contains(b));
  }

  @Test
  public void testUnionWithSingleKnowledgeBase() {
    KnowledgeBase kb1 = new KnowledgeBase(Arrays.asList(new Proposition("p")));
    KnowledgeBase kb2 = new KnowledgeBase(Arrays.asList(new Proposition("q")));

    KnowledgeBase result = kb1.union(kb2);

    assertEquals(2, result.size());
    assertTrue(result.contains(new Proposition("p")));
    assertTrue(result.contains(new Proposition("q")));
  }

  @Test
  public void testUnionWithMultipleKnowledgeBases() {
    KnowledgeBase kb1 = new KnowledgeBase(Arrays.asList(new Proposition("p")));
    KnowledgeBase kb2 = new KnowledgeBase(Arrays.asList(new Proposition("q")));
    KnowledgeBase kb3 = new KnowledgeBase(Arrays.asList(new Proposition("r")));

    KnowledgeBase result = kb1.union(Arrays.asList(kb1, kb2, kb3));

    assertEquals(3, result.size());
    assertTrue(result.contains(new Proposition("p")));
    assertTrue(result.contains(new Proposition("q")));
    assertTrue(result.contains(new Proposition("r")));
  }

  @Test
  public void testIntersection() {
    KnowledgeBase kb1 = new KnowledgeBase(Arrays.asList(new Proposition("p"), new Proposition("q")));
    KnowledgeBase kb2 = new KnowledgeBase(Arrays.asList(new Proposition("q"), new Proposition("r")));

    KnowledgeBase result = kb1.intersection(kb2);

    assertEquals(1, result.size());
    assertTrue(result.contains(new Proposition("q")));
  }

  @Test
  public void testDifference() {
    KnowledgeBase kb1 = new KnowledgeBase(Arrays.asList(new Proposition("p"), new Proposition("q")));
    KnowledgeBase kb2 = new KnowledgeBase(Arrays.asList(new Proposition("q")));

    KnowledgeBase result = kb1.difference(kb2);

    assertEquals(1, result.size());
    assertTrue(result.contains(new Proposition("p")));
  }

  @Test
  public void testAntecedents() {
    PlFormula p = new Proposition("p");
    PlFormula q = new Proposition("q");
    KnowledgeBase kb = new KnowledgeBase(Arrays.asList(new Implication(p, q)));

    KnowledgeBase result = kb.antecedents();

    assertEquals(1, result.size());
    assertTrue(result.contains(p));
  }

  @Test
  public void testMaterialise() {
    PlFormula p = new Proposition("p");
    PlFormula q = new Proposition("q");
    DefeasibleImplication defeasibleImplication = new DefeasibleImplication(p, q);
    KnowledgeBase kb = new KnowledgeBase(Arrays.asList(defeasibleImplication));

    KnowledgeBase result = kb.materialise();

    assertEquals(1, result.size());
    assertTrue(result.contains(new Implication(p, q)));
  }

  @Test
  public void testDematerialise() {
    PlFormula p = new Proposition("p");
    PlFormula q = new Proposition("q");
    Implication implication = new Implication(p, q);
    KnowledgeBase kb = new KnowledgeBase(Arrays.asList(implication));

    KnowledgeBase result = kb.dematerialise();

    assertEquals(1, result.size());
    assertTrue(result.contains(new DefeasibleImplication(p, q)));
  }

  @Test
  public void testSeparate() {
    PlFormula p = new Proposition("p");
    PlFormula q = new Proposition("q");
    DefeasibleImplication defeasibleImplication = new DefeasibleImplication(p, q);
    Implication implication = new Implication(q, p);
    KnowledgeBase kb = new KnowledgeBase(Arrays.asList(defeasibleImplication, implication));

    KnowledgeBase[] result = kb.separate();

    assertEquals(1, result[0].size());
    assertTrue(result[0].contains(defeasibleImplication));

    assertEquals(1, result[1].size());
    assertTrue(result[1].contains(implication));
  }

  @Test
  public void testStaticMaterialise() {
    PlFormula p = new Proposition("p");
    PlFormula q = new Proposition("q");
    DefeasibleImplication defeasibleImplication = new DefeasibleImplication(p, q);

    PlFormula result = KnowledgeBase.materialise(defeasibleImplication);

    assertTrue(result instanceof Implication);
    assertEquals(new Implication(p, q), result);
  }

  @Test
  public void testStaticDematerialise() {
    PlFormula p = new Proposition("p");
    PlFormula q = new Proposition("q");
    Implication implication = new Implication(p, q);

    PlFormula result = KnowledgeBase.dematerialise(implication);

    assertTrue(result instanceof DefeasibleImplication);
    assertEquals(new DefeasibleImplication(p, q), result);
  }
}
