package com.extrc.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.tweetyproject.logics.pl.parser.PlParser;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.KnowledgeBase;

public class DefeasibleParser {
  private final PlParser parser;

  public DefeasibleParser() {
    this.parser = new PlParser();
  }

  public PlFormula parseFormula(String formula) throws Exception {
    PlFormula parsedFormula;
    try {
      boolean isDI = formula.contains(Symbols.DEFEASIBLE_IMPLICATION());
      formula = isDI ? reformatDefeasibleImplication(formula) : formula;
      parsedFormula = parser.parseFormula(formula);
      return isDI ? KnowledgeBase.dematerialise(parsedFormula) : parsedFormula;
    } catch (Exception e) {
      throw new Exception("Cannot parse formula: " + formula);
    }
  }

  public KnowledgeBase parseFormulas(String formulas) throws Exception {
    String[] formulaStrings = formulas.split(",");
    KnowledgeBase kb = new KnowledgeBase();
    for (String formula : formulaStrings) {
      if (!formula.trim().isEmpty()) {
        try {
          PlFormula parsedFormula = this.parseFormula(formula.trim());
          kb.add(parsedFormula);
        } catch (Exception e) {
          throw e;
        }
      }
    }
    return kb;

  }

  public KnowledgeBase parseFormulasFromFile(String filePath) throws Exception {
    KnowledgeBase kb = new KnowledgeBase();
    try (var reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        try {
          PlFormula parsedFormula = this.parseFormula(line.trim());
          kb.add(parsedFormula);
        } catch (Exception e) {
          throw e;
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return kb;
  }

  public KnowledgeBase parseInputStream(InputStream inputStream) throws Exception {
    KnowledgeBase kb = new KnowledgeBase();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) {
        try {
          PlFormula parsedFormula = this.parseFormula(line.trim());
          kb.add(parsedFormula);
        } catch (Exception e) {
          throw e;
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return kb;
  }

  private String reformatDefeasibleImplication(String formula) {
    int index = formula.indexOf(Symbols.DEFEASIBLE_IMPLICATION());
    formula = "(" + formula.substring(0, index) + ")" + Symbols.IMPLICATION() + "("
        + formula.substring(index + 2, formula.length()) + ")";
    return formula;
  }

}
