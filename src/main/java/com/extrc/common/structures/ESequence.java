package com.extrc.common.structures;

import java.util.LinkedList;

import org.tweetyproject.logics.pl.syntax.PlFormula;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;

public class ESequence extends LinkedList<KnowledgeBase> {
  public class EsequenceNode extends KnowledgeBase {
    private final KnowledgeBase exceptionals;
    private final int number;

    public EsequenceNode() {
      super();
      this.exceptionals = new KnowledgeBase();
      this.number = 0;
    }

    public EsequenceNode(int number, KnowledgeBase kb, KnowledgeBase exceptionals) {
      super(kb);
      this.number = number;
      this.exceptionals = exceptionals;
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("E(").append(this.number).append(") = ").append(super.toString());
      if (!this.exceptionals.isEmpty()) {
        sb.append("\t\t(");
        if (this.exceptionals.size() > 1) {
          sb.append("anticidents  ");
        } else {
          sb.append("anticident ");
        }
        for (PlFormula anticident : this.exceptionals) {
          sb.append(anticident.toString()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        if (this.exceptionals.size() > 1) {
          sb.append(" are exceptional");
        } else {
          sb.append(" is exceptional");
        }
      }
      return sb.toString();
    }
  }

  public void addNode(KnowledgeBase kb) {
    this.add(new EsequenceNode(this.size(), kb, new KnowledgeBase()));
  }

  public void addNode(KnowledgeBase kb, KnowledgeBase exceptionals) {
    this.add(new EsequenceNode(this.size(), kb, exceptionals));
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    int maxLength = 0;
    if (!this.isEmpty()) {
      AsciiTable table = new AsciiTable();
      table.addRule();
      for (KnowledgeBase esequence : this) {
        table.addRow(esequence.toString());
        maxLength = Math.max(maxLength, esequence.toString().length());
      }
      table.addRule();
      table.getRenderer().setCWC(new CWC_LongestWordMin(new int[] { maxLength + 3 }));
      sb.append(table.render());
    }

    return sb.toString();
  }

}
