package com.extrc.models;

import java.util.Collection;

import org.tweetyproject.logics.pl.syntax.PlFormula;

/**
 * This class represents a ranked knowledge base.
 * 
 * @author Thabo Vincent Moloi
 */
public class Rank extends KnowledgeBase {
  /** Represents the rank number. */
  private int rankNumber;

  /**
   * Creates a new (empty) rank 0.
   */
  public Rank() {
    super();
    this.rankNumber = 0;
  }

  /**
   * Creates a new rank given a rank number and a set of formulas.
   * 
   * @param rankNumber Rank number.
   * @param formulas   A set of formulas.
   */
  public Rank(int rankNumber, Collection<? extends PlFormula> formulas) {
    super(formulas);
    this.rankNumber = rankNumber;
  }

  /**
   * Create a rank (copy) from a given rank.
   * 
   * @param rank Ranked knowledge base.
   */
  public Rank(Rank rank) {
    super(rank);
    this.rankNumber = rank.rankNumber;
  }

  /**
   * Get the rank number.
   * 
   * @return Rank number.
   */
  public int getRankNumber() {
    return this.rankNumber;
  }

  /**
   * Set the rank number.
   * 
   * @param rankNumber Rank number.
   */
  public void setRankNumber(int rankNumber) {
    this.rankNumber = rankNumber;
  }

  /**
   * Get formulas from this rank as a string.
   */
  public String formulasToString() {
    return super.toString().replaceAll("[{}]", "");
  }
}
