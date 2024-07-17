package com.extrc.view.console;

import org.jline.terminal.Terminal;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.services.DefeasibleReasoner;
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
      this.kb = (KnowledgeBase) validate.parsedObject;
      this.terminal.writer().println(this.kb.toString());
    } else {
      this.terminal.writer().println(validate.errorMessage);
    }
    this.terminal.writer().flush();
  }

  public void loadKbFromFile(String formulas) {
    Validator.Node validate = this.validator.validateFormulasFromFile(formulas);
    if (validate.isValid) {
      this.kb = (KnowledgeBase) validate.parsedObject;
      this.terminal.writer().println(this.kb.toString());
    } else {
      this.terminal.writer().println(validate.errorMessage);
    }
    this.terminal.writer().flush();
  }

  public void queryRationalReasoner(String formula) {
    new AttributedStringBuilder()
        .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN).bold())
        .append("extrc").style(AttributedStyle.DEFAULT)
        .append("$ ").toAnsi();
  }
}
