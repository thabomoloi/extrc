package com.extrc.common.services;

import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.structures.EntailmentResult;
import com.extrc.common.structures.KnowledgeBase;

public interface DefeasibleReasoner {

  /**
   * Sets the knowledge base of this defeasible reasoner.
   * 
   * @param knowledgeBase
   */
  public void setKnowledgeBase(KnowledgeBase knowledgeBase);

  /**
   * Gets the current knowledge base of the defeasible reasoner.
   * 
   * @return Knowledge base
   */
  public KnowledgeBase getKnowledgeBase();

  /**
   * Checks if a formula is entailed.
   * 
   * @param formula Formula to check entailment for.
   * @return The entailment result.
   */
  public EntailmentResult query(PlFormula formula);
}
