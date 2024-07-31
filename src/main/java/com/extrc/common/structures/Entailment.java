package com.extrc.common.structures;

public class Entailment {
  private EntailmentResult rationalEntailment;
  private EntailmentResult lexicalEntailment;

  public Entailment() {
  }

  public Entailment(EntailmentResult rationalEntailment, EntailmentResult lexicalEntailment) {
    this.rationalEntailment = rationalEntailment;
    this.lexicalEntailment = lexicalEntailment;
  }

  public void setRationalEntailment(EntailmentResult rationalEntailment) {
    this.rationalEntailment = rationalEntailment;
  }

  public void setLexicalEntailment(EntailmentResult lexicalEntailment) {
    this.lexicalEntailment = lexicalEntailment;
  }

  public EntailmentResult getRationalEntailment() {
    return this.rationalEntailment;
  }

  public EntailmentResult getLexicalEntailment() {
    return this.lexicalEntailment;
  }
}
