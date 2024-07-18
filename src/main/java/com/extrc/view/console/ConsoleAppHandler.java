package com.extrc.view.console;

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

public class ConsoleAppHandler {
  private KnowledgeBase kb;
  private DefeasibleReasoner rationalReasoner;
  private DefeasibleReasoner lexicalReasoner;
  private Validator validator;
  private Terminal terminal;

  public ConsoleAppHandler(Terminal terminal) {
    this.kb = new KnowledgeBase();
    this.rationalReasoner = new RationalReasoner(kb);
    this.lexicalReasoner = new LexicalReasoner(kb);
    this.terminal = terminal;
    this.validator = new Validator();
  }

  public void loadKb(String formulas) {
    Validator.Node validate = this.validator.validateFormulas(formulas);
    if (validate.isValid) {
      this.kb.clear();
      this.kb.addAll((KnowledgeBase) validate.parsedObject);
      this.terminal.writer().println(this.kb.toString());
    } else {
      this.terminal.writer().println(validate.errorMessage);
    }
    this.terminal.writer().flush();
  }

  public void loadKbFromFile(String formulas) {
    Validator.Node validate = this.validator.validateFormulasFromFile(formulas);
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
    this.queryRationalReasoner(formula);
    for (int i = 0; i < 50; i++) {
      this.terminal.writer().print("\u2582");
    }
    this.terminal.writer().println("\n");
    this.queryLexicalReasoner(formula);
  }

  public void queryRationalReasoner(String formula) {
    this.terminal.writer().println();
    this.rationalReasoner = new RationalReasoner(kb);
    Validator.Node validate = this.validator.validateFormula(formula);
    if (validate.isValid) {
      PlFormula query = (PlFormula) validate.parsedObject;
      Entailment entailment = this.rationalReasoner.query(query);
      printTitle("RATIONAL CLOSURE");
      this.terminal.writer().println(entailment.toString());
    } else {
      this.terminal.writer().println(validate.errorMessage);
    }
    this.terminal.writer().flush();

  }

  public void queryLexicalReasoner(String formula) {
    this.terminal.writer().println();
    this.lexicalReasoner = new LexicalReasoner(kb);

    Validator.Node validate = this.validator.validateFormula(formula);
    if (validate.isValid) {
      PlFormula query = (PlFormula) validate.parsedObject;
      Entailment entailment = this.lexicalReasoner.query(query);
      printTitle("LEXICOGRAPHIC CLOSURE");
      this.terminal.writer().println(entailment.toString());
    } else {
      this.terminal.writer().println(validate.errorMessage);
    }
    this.terminal.writer().flush();
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
