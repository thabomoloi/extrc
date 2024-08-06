package com.extrc.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.tweetyproject.commons.ParserException;
import org.tweetyproject.logics.pl.parser.PlParser;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.KnowledgeBase;

public class DefeasibleParser {
  private final PlParser parser;

  public DefeasibleParser() {
    this.parser = new PlParser();
  }

  public PlFormula parseFormula(String formula) throws IOException, ParserException {
    PlFormula parsedFormula;
    if (formula.contains(Symbols.DEFEASIBLE_IMPLICATION())) {
      formula = reformatDefeasibleImplication(formula);
      parsedFormula = parser.parseFormula(formula);
      return KnowledgeBase.dematerialise(parsedFormula);
    } else {
      parsedFormula = parser.parseFormula(formula);
      return parsedFormula;
    }
  }

  public KnowledgeBase parseFormulas(String formulas) throws IOException, ParserException {
    String[] formulaStrings = formulas.split(",");
    KnowledgeBase kb = new KnowledgeBase();
    for (String formula : formulaStrings) {
      if (!formula.trim().isEmpty()) {
        PlFormula parsedFormula = this.parseFormula(formula.trim());
        kb.add(parsedFormula);
      }
    }
    return kb;
  }

  public KnowledgeBase parseFormulasFromFile(String filePath) throws IOException {
    KnowledgeBase kb = new KnowledgeBase();
    try (var reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        PlFormula parsedFormula = this.parseFormula(line.trim());
        kb.add(parsedFormula);
      }
    } catch (IOException e) {
      throw e;
    }
    return kb;
  }

  public KnowledgeBase parseInputStream(InputStream inputStream) throws IOException {
    KnowledgeBase kb = new KnowledgeBase();
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) {
        PlFormula parsedFormula = this.parseFormula(line.trim());
        kb.add(parsedFormula);
      }
    } catch (IOException e) {
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
