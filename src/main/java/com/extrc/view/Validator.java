package com.extrc.view;

import java.io.IOException;
import java.io.InputStream;

import org.tweetyproject.commons.ParserException;

import com.extrc.common.utils.DefeasibleParser;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

public class Validator {

  public final String operators;

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
    this.operators = buildOperatorTable();
  }

  private String buildOperatorTable() {
    AsciiTable operatorTable = new AsciiTable();
    operatorTable.addRule();
    operatorTable.addRow("Operator", "Name", "Example");
    operatorTable.addRule();
    operatorTable.addRow("!", "Negation", "!a");
    operatorTable.addRow("&&", "Conjunction", "a && b");
    operatorTable.addRow("||", "Disjunction", "a || b");
    operatorTable.addRow("=>", "Implication", "a => b");
    operatorTable.addRow("<=>", "Biconditional", "a <=> b");
    operatorTable.addRow("~>", "Defeasible implication", "a ~> b");
    operatorTable.addRule();
    operatorTable.getRenderer().setCWC(new CWC_LongestWordMin(new int[] { 10, 24, 10 }));
    operatorTable.setTextAlignment(TextAlignment.CENTER);
    return operatorTable.render();
  }

  public Node validateFormula(String formula) {
    return validate(() -> parser.parseFormula(formula), "formula");
  }

  public Node validateFormulas(String formulas) {
    return validate(() -> parser.parseFormulas(formulas), "formulas");
  }

  public Node validateFormulasFromFile(String filePath) {
    return validate(() -> parser.parseFormulasFromFile(filePath), "formulas");
  }

  public Node validateInputStream(InputStream inputStream) {
    return validate(() -> parser.parseInputStream(inputStream), "formulas");
  }

  private Node validate(ParserFunction parserFunction, String inputType) {
    try {
      Object parsed = parserFunction.parse();
      return new Node(true, "", parsed);
    } catch (IOException | ParserException e) {
      return createErrorNode(inputType);
    }
  }

  private Node createErrorNode(String inputType) {
    StringBuilder error = new StringBuilder();
    error.append("Error parsing ").append(inputType).append(": make sure you used the following operators.\n");
    error.append(this.operators).append("\n\n");
    return new Node(false, error.toString(), null);
  }

  @FunctionalInterface
  private interface ParserFunction {
    Object parse() throws IOException, ParserException;
  }
}
