package com.extrc.common.structures;

/**
 * This class represents element of a sequence.
 * 
 * @author Thabo Vincent Moloi
 */
public class SequenceElement extends KnowledgeBase {
  /** Represents element number. */
  private int elementNumber;
  /** Represents exceptionals that lead to creation of this element. */
  private KnowledgeBase exceptionals;

  /**
   * Constructs new (empty) element.
   */
  public SequenceElement() {
    super();
    this.elementNumber = 0;
    this.exceptionals = new KnowledgeBase();
  }

  /**
   * Constructs a new element with given number and formulas.
   * 
   * @param elementNumber Element number.
   * @param formulas      Knowledge base of formulas.
   */
  public SequenceElement(int elementNumber, KnowledgeBase formulas) {
    super(formulas);
    this.elementNumber = elementNumber;
    this.exceptionals = new KnowledgeBase();
  }

  /**
   * Constructs a new element with given number, formulas and exceptionals.
   * 
   * @param elementNumber Element number.
   * @param formulas      Knowledge base of formulas.
   * @param exceptionals  Knowledge base of exceptional antecedants.
   */
  public SequenceElement(int elementNumber, KnowledgeBase formulas, KnowledgeBase exceptionals) {
    super(formulas);
    this.elementNumber = elementNumber;
    this.exceptionals = exceptionals;
  }

  /**
   * Sets the element number.
   * 
   * @param elementNumber Element number.
   */
  public void setElementNumber(int elementNumber) {
    this.elementNumber = elementNumber;
  }

  /**
   * Gets the element number.
   * 
   * @return Element number.
   */
  public int getElementNumber() {
    return this.elementNumber;
  }

  /**
   * Set the exceptionals.
   * 
   * @param exceptionals Knowledge base of exceptional antecedents.
   */
  public void setExceptionals(KnowledgeBase exceptionals) {
    this.exceptionals = exceptionals;
  }

  /**
   * Get the exceptionals.
   * 
   * @return Knowledge base of exceptional antecedants.
   */
  public KnowledgeBase getExceptionals() {
    return this.exceptionals;
  }
}
