package com.extrc.view.console;

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.services.DefeasibleReasoner;
import com.extrc.common.structures.EntailmentResult;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.reasoning.reasoners.LexicalReasoner;
import com.extrc.reasoning.reasoners.RationalReasoner;
import com.extrc.view.Validator;

public class ConsoleAppHandler {
  private final KnowledgeBase kb;
  private DefeasibleReasoner rationalReasoner;
  private DefeasibleReasoner lexicalReasoner;
  private final Validator validator;
  private final Terminal terminal;

  public ConsoleAppHandler(Terminal terminal) {
    this.kb = new KnowledgeBase();
    this.rationalReasoner = new RationalReasoner(kb);
    this.lexicalReasoner = new LexicalReasoner(kb);
    this.terminal = terminal;
    this.validator = new Validator();
  }

  public void loadKb(String formulas) {
    loadKnowledgeBase(formulas, false);
  }

  public void loadKbFromFile(String formulas) {
    loadKnowledgeBase(formulas, true);
  }

  private void loadKnowledgeBase(String formulas, boolean fromFile) {
    Validator.Node validate = fromFile ? this.validator.validateFormulasFromFile(formulas)
        : this.validator.validateFormulas(formulas);
    if (validate.isValid) {
      this.kb.clear();
      this.kb.addAll((KnowledgeBase) validate.parsedObject);
      this.terminal.writer().println(this.kb.toString());
    } else {
      this.terminal.writer().println(validate.errorMessage);
    }
    this.terminal.writer().flush();
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
      this.rationalReasoner = new RationalReasoner(kb);
      queryReasoner(formula, this.rationalReasoner, "RATIONAL CLOSURE");
    }
  }

  public void queryLexicalReasoner(String formula) {
    if (validateQuery(formula)) {
      this.lexicalReasoner = new LexicalReasoner(kb);
      queryReasoner(formula, this.lexicalReasoner, "LEXICOGRAPHIC CLOSURE");
    }
  }

  // public void explainAll(String formula) {
  //   if (validateQuery(formula)) {
  //     explainRationalClosure(formula);
  //     printSeparator(70);
  //     explainLexicalClosure(formula);
  //   }
  // }

  // public void explainRationalClosure(String formula) {
  //   if (validateQuery(formula)) {
  //     this.rationalReasoner = new RationalReasoner(kb);
  //     explainReasoner(formula, this.rationalReasoner, "RATIONAL CLOSURE EXPLANATION");
  //   }
  // }

  // public void explainLexicalClosure(String formula) {
  //   if (validateQuery(formula)) {
  //     this.lexicalReasoner = new LexicalReasoner(kb);
  //     explainReasoner(formula, this.lexicalReasoner, "LEXICOGRAPHIC CLOSURE EXPLANATION");
  //   }
  // }

  private boolean validateQuery(String formula) {
    Validator.Node validate = this.validator.validateFormula(formula);
    if (!validate.isValid) {
      this.terminal.writer().println(validate.errorMessage);
      return false;
    }
    PlFormula query = (PlFormula) validate.parsedObject;
    if (!query.toString().contains("~>")) {
      this.terminal.writer().println("Error: Query must be defeasible implication.");
      this.terminal.writer().flush();
      return false;
    }
    return true;
  }

  private void queryReasoner(String formula, DefeasibleReasoner reasoner, String title) {
    this.terminal.writer().println();
    Validator.Node validate = this.validator.validateFormula(formula);
    if (validate.isValid) {
      PlFormula query = (PlFormula) validate.parsedObject;
      EntailmentResult entailment = reasoner.query(query);
      printTitle(title);
      this.terminal.writer().println(entailment.toString());
    } else {
      this.terminal.writer().println(validate.errorMessage);
    }
    this.terminal.writer().flush();
  }

  private void printSeparator(int length) {
    for (int i = 0; i < length; i++) {
      this.terminal.writer().print("\u2582");
    }
    this.terminal.writer().println("\n");
  }

  private void printTitle(String title) {
    String heading = new AttributedStringBuilder()
        .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE).bold())
        .append(title).style(AttributedStyle.DEFAULT).toAnsi();
    this.terminal.writer().println(heading);
    for (int i = 0; i < title.length(); i++) {
      this.terminal.writer().print("\u2500");
    }
    this.terminal.writer().println("\n");
  }
}
