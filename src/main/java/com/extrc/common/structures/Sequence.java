package com.extrc.common.structures;

import java.util.ArrayList;

/**
 * This class represents the exceptiionality sequence.
 * 
 * @author Thabo Vincent Moloi
 */
public class Sequence extends ArrayList<SequenceElement> {

  /**
   * Add element to this sequence from given formulas.
   * 
   * @param formulas Knowledge base of formulas.
   */
  public void addElement(KnowledgeBase formulas) {
    this.add(new SequenceElement(this.size(), formulas));
  }

  /**
   * Add element to this sequence from given formulas and exceptionals.
   * 
   * @param formulas     Knowledge base of formulas.
   * @param exceptionals Exceptional antecedents.
   */
  public void addElement(KnowledgeBase formulas, KnowledgeBase exceptionals) {
    this.add(new SequenceElement(this.size(), formulas, exceptionals));
  }
}
