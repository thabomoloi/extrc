package com.extrc.common.structures;

import org.tweetyproject.logics.pl.syntax.PlFormula;
import com.extrc.common.services.RankConstructor;

/**
 * This class represents the result of an entailment check.
 * 
 * @author Thabo Vincent Moloi
 */
public class EntailmentResult {
  private final PlFormula formula;
  private final KnowledgeBase knowledgeBase;
  private final RankConstructor baseRank;
  private final Ranking removedRanking;
  private boolean entailed;
  private final Ranking subsets;
  private final ReasonerTimer timer;

  /**
   * Constructs an EntailmentResult with the specified formula, knowledge base,
   * and base rank.
   *
   * @param formula       the propositional logic formula involved in the
   *                      entailment check
   * @param knowledgeBase the knowledge base used in the entailment check
   * @param baseRank      the base rank constructor used in the entailment check
   */
  public EntailmentResult(PlFormula formula, KnowledgeBase knowledgeBase, RankConstructor baseRank) {
    this.formula = formula;
    this.knowledgeBase = knowledgeBase;
    this.baseRank = baseRank;
    this.removedRanking = new Ranking();
    this.entailed = false;
    this.subsets = new Ranking();
    this.timer = new ReasonerTimer();
  }

  /**
   * Sets the entailed status of the formula.
   *
   * @param entailed true if the formula is entailed, false otherwise
   */
  public void setEntailed(boolean entailed) {
    this.entailed = entailed;
  }

  /**
   * Returns the entailed status of the formula.
   *
   * @return true if the formula is entailed, false otherwise
   */
  public boolean getEntailed() {
    return this.entailed;
  }

  /**
   * Returns the formula involved in the entailment check.
   *
   * @return the formula
   */
  public PlFormula getFormula() {
    return this.formula;
  }

  /**
   * Returns the knowledge base used in the entailment check.
   *
   * @return the knowledge base
   */
  public KnowledgeBase getKnowledgeBase() {
    return this.knowledgeBase;
  }

  /**
   * Returns the base rank constructor used in the entailment check.
   *
   * @return the base rank constructor
   */
  public RankConstructor getBaseRank() {
    return this.baseRank;
  }

  /**
   * Returns the ranking of removed elements during the entailment check.
   *
   * @return the ranking of removed elements
   */
  public Ranking getRemovedRanking() {
    return this.removedRanking;
  }

  /**
   * Returns the subsets of the ranks used in the entailment check.
   *
   * @return the subsets of the ranks
   */
  public Ranking getSubsets() {
    return this.subsets;
  }

  /**
   * Returns the timer used for measuring reasoning time during the entailment
   * check.
   *
   * @return the reasoner timer
   */
  public ReasonerTimer getTimer() {
    return this.timer;
  }
}
