package com.extrc.common.structures;

import java.util.Collection;
import java.util.LinkedList;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

public class Ranking extends LinkedList<Rank> {
  public Ranking() {
    super();
  }

  public Ranking(Collection<? extends Rank> ranks) {
    super(ranks);
  }

  @Override
  public String toString() {
    AsciiTable ranks = new AsciiTable();
    ranks.addRule();
    int[] maxLengths = new int[] { -1, -1 };
    for (Rank rank : this) {
      String formulas = rank.formulasToString();
      String rankNumber = rank.getRankNumber() == Integer.MAX_VALUE ? "∞" : Integer.toString(rank.getRankNumber());
      maxLengths[1] = maxLengths[1] >= formulas.length() ? maxLengths[1] : formulas.length() + 2;
      maxLengths[0] = maxLengths[0] >= rankNumber.length() ? maxLengths[0] : rankNumber.length() + 2;
      ranks.addRow(rankNumber, formulas);
      ranks.addRule();
    }
    if (this.isEmpty()) {
      maxLengths[0] = 3;
      maxLengths[1] = 10;
      ranks.addRow("", "");
      ranks.addRule();
    }
    ranks.getRenderer().setCWC(new CWC_LongestWordMin(maxLengths));
    ranks.setTextAlignment(TextAlignment.CENTER);

    return ranks.render();
  }
}
