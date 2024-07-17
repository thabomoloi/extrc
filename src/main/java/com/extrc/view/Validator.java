package com.extrc.view;

import java.io.IOException;

import org.tweetyproject.commons.ParserException;

import com.extrc.common.utils.DefeasibleParser;

public class Validator {

  public static class Node {
    public final boolean isValid;
    public final String errorMessage;
    public final Object parsedObject;

    public Node(boolean isValid, String errorMessage, Object parsedObject) {
      this.isValid = isValid;
      this.errorMessage = errorMessage;
      this.parsedObject = parsedObject;
    }
  }

  private final DefeasibleParser parser;

  public Validator() {
    this.parser = new DefeasibleParser();
  }

  public Node validateFormula(String formula) {
    try {
      Object parsed = parser.parseFormula(formula);
      return new Node(true, "", parsed);
    } catch (IOException | ParserException e) {
      return new Node(false, e.getMessage(), null);
    }
  }

  public Node validateFormulas(String formulas) {
    try {
      Object parsed = parser.parseFormulas(formulas);
      return new Node(true, "", parsed);
    } catch (IOException | ParserException e) {
      return new Node(false, e.getMessage(), null);
    }
  }

  public Node validateFormulasFromFile(String filePath) {
    try {
      Object parsed = parser.parseFormulasFromFile(filePath);
      return new Node(true, "", parsed);
    } catch (IOException e) {
      return new Node(false, e.getMessage(), null);
    }
  }
}
