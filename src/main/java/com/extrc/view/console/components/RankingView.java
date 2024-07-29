package com.extrc.view.console.components;

import com.extrc.common.structures.Rank;
import com.extrc.common.structures.Ranking;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

public class RankingView {
  private final Ranking ranking;

  public RankingView(Ranking ranking) {
    this.ranking = ranking;
  }

  @Override
  public String toString() {
    AsciiTable ranks = new AsciiTable();
    ranks.addRule();
    int[] maxLengths = new int[] { -1, -1 };

    int size = ranking.size();

    for (int i = 0; i < size; i++) {
      if (size > 4 && i >= 2 && i < size - 2) {
        if (i == 2) {
          // Add a row indicating there are hidden rows
          String hiddenRowIndicator = "...";
          ranks.addRow(hiddenRowIndicator, hiddenRowIndicator);
          ranks.addRule();
        }
        continue;
      }

      Rank rank = ranking.get(i);
      String formulas = rank.formulasToString();
      String rankNumber = rank.getRankNumber() == Integer.MAX_VALUE ? "∞" : Integer.toString(rank.getRankNumber());
      maxLengths[1] = maxLengths[1] >= formulas.length() ? maxLengths[1] : formulas.length() + 2;
      maxLengths[0] = maxLengths[0] >= rankNumber.length() ? maxLengths[0] : rankNumber.length() + 2;
      ranks.addRow(rankNumber, formulas);
      ranks.addRule();
    }

    if (ranking.isEmpty()) {
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
