package com.extrc.services;

public class ReasonerFactory {
  public static ReasonerService createReasoner(String type) {
    return switch (type) {
      case "rational" -> new RationalReasonerImpl();
      case "lexical" -> new LexicalReasonerImpl();
      default -> throw new IllegalArgumentException("Unknown reasoner: " + type);
    };
  }
}
