package com.extrc.view.console;

import java.io.PrintWriter;

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.services.DefeasibleReasoner;
import com.extrc.common.structures.Entailment;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.reasoning.reasoners.LexicalReasoner;
import com.extrc.reasoning.reasoners.RationalReasoner;
import com.extrc.view.Validator;
import com.extrc.view.console.components.EntailmentView;
import com.extrc.view.console.components.ExplanationView;
import com.extrc.view.console.components.RankView;

public class ConsoleAppHandler {
  private final KnowledgeBase knowledgeBase;
  private final DefeasibleReasoner rationalReasoner;
  private final DefeasibleReasoner lexicalReasoner;
  private final Validator validator;
  private final Entailment entailment;
  private final PrintWriter writer;
  private PlFormula query;

  public ConsoleAppHandler(Terminal terminal) {
    knowledgeBase = new KnowledgeBase();
    rationalReasoner = new RationalReasoner(knowledgeBase);
    lexicalReasoner = new LexicalReasoner(knowledgeBase);
    writer = terminal.writer();
    validator = new Validator();
    entailment = new Entailment();
    query = null;
  }

  public void loadKb(String formulas) {
    loadKb(validator.validateFormulas(formulas));
  }

  public void loadKbFromFile(String filepath) {
    loadKb(validator.validateFormulasFromFile(filepath));
  }

  private void loadKb(Validator.Node validation) {
    if (validation.isValid) {
      knowledgeBase.clear();
      knowledgeBase.addAll((KnowledgeBase) validation.parsedObject);
      entailment.setLexicalEntailment(null);
      entailment.setRationalEntailment(null);
      writer.println(knowledgeBase);
    } else {
      writer.println(validation.errorMessage);
    }
    writer.flush();
  }

  public void showKb() {
    writer.println(knowledgeBase);
    writer.flush();
  }

  public void queryAll(String formula) {
    if (validateQuery(formula)) {
      queryRationalReasoner(formula);
      printSeparator(50);
      queryLexicalReasoner(formula);
    }
  }

  public void queryRationalReasoner(String formula) {
    if (validateQuery(formula)) {
      entailment.setRationalEntailment(rationalReasoner.query(query));
      printTitle("RATIONAL CLOSURE");
      writer.println(new EntailmentView(entailment.getRationalEntailment()));
      writer.flush();
    }
  }

  public void queryLexicalReasoner(String formula) {
    if (validateQuery(formula)) {
      entailment.setLexicalEntailment(lexicalReasoner.query(query));
      printTitle("LEXICOGRAPHIC CLOSURE");
      writer.println(new EntailmentView(entailment.getLexicalEntailment()));
      writer.flush();
    }
  }

  public void explainAll(String formula) {
    if (validateQuery(formula)) {
      explainRationalClosure(formula);
      printSeparator(70);
      explainLexicalClosure(formula);
    }
  }

  public void explainRationalClosure(String formula) {
    if (validateQuery(formula)) {
      printTitle("RATIONAL CLOSURE EXPLANATION");
      if (entailment.getRationalEntailment() == null
          || !query.equals(entailment.getRationalEntailment().getFormula())
          || !knowledgeBase.equals(entailment.getLexicalEntailment().getKnowledgeBase())) {
        entailment.setRationalEntailment(rationalReasoner.query(query));
      }
      writer.println(new ExplanationView(entailment.getRationalEntailment()));
      writer.flush();
    }
  }

  public void explainLexicalClosure(String formula) {
    if (validateQuery(formula)) {
      printTitle("LEXICOGRAPHIC CLOSURE EXPLANATION");
      if (entailment.getLexicalEntailment() == null
          || !query.equals(entailment.getLexicalEntailment().getFormula())
          || !knowledgeBase.equals(entailment.getLexicalEntailment().getKnowledgeBase())) {
        entailment.setLexicalEntailment(lexicalReasoner.query(query));
      }
      writer.println(new ExplanationView(entailment.getLexicalEntailment()));
      writer.flush();
    }
  }

  public void viewRankAll(String number) {
    try {
      int rankNumber = Integer.parseInt(number);
      viewRankRational(Integer.toString(rankNumber));
      printSeparator(70);
      viewRankLexical(Integer.toString(rankNumber));
      writer.flush();
    } catch (NumberFormatException e) {
      writer.println("Error parsing rank number \"" + number + "\".");
      writer.flush();
    }
  }

  public void viewRankRational(String number) {
    try {
      int rankNumber = Integer.parseInt(number);
      printTitle(String.format("RANK %d (RATIONAL CLOSURE)", rankNumber));
      if (entailment.getRationalEntailment() == null) {
        writer.println("Error: rational entailment not computed yet.");
      } else if (rankNumber > entailment.getRationalEntailment().getBaseRank().getRanking().size() - 2) {
        writer.println("Error: rank number " + rankNumber + " out of range.");
      } else {
        writer.println(new RankView(rankNumber, entailment.getRationalEntailment()));
      }
      writer.flush();
    } catch (NumberFormatException e) {
      writer.println("Error parsing rank number \"" + number + "\".");
      writer.flush();
    }
  }

  public void viewRankLexical(String number) {
    try {
      int rankNumber = Integer.parseInt(number);
      printTitle(String.format("RANK %d (LEXICOGRAPHIC CLOSURE)", rankNumber));
      if (entailment.getLexicalEntailment() == null) {
        writer.println("Error: lexicographic entailment not computed yet.");
      } else if (rankNumber > entailment.getLexicalEntailment().getBaseRank().getRanking().size() - 2) {
        writer.println("Error: rank number " + rankNumber + " out of range.");
      } else {
        writer.println(new RankView(rankNumber, entailment.getLexicalEntailment()));
      }
      writer.flush();
    } catch (NumberFormatException e) {
      writer.println("Error parsing rank number \"" + number + "\".");
      writer.flush();
    }
  }

  private boolean validateQuery(String formula) {
    Validator.Node validate = validator.validateFormula(formula);
    if (!validate.isValid) {
      writer.println(validate.errorMessage);
      return false;
    }
    query = (PlFormula) validate.parsedObject;
    if (!query.toString().contains("~>")) {
      writer.println("Error: Query must be defeasible implication.");
      writer.flush();
      return false;
    }
    return true;
  }

  private void printSeparator(int length) {
    for (int i = 0; i < length; i++) {
      writer.print("\u2582");
    }
    writer.println("\n");
  }

  private void printTitle(String title) {
    String heading = new AttributedStringBuilder()
        .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE).bold())
        .append(title).style(AttributedStyle.DEFAULT).toAnsi();
    writer.println(heading);
    for (int i = 0; i < title.length(); i++) {
      writer.print("\u2500");
    }
    writer.println("\n");
  }
}
