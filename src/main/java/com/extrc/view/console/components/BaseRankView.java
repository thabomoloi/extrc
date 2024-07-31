package com.extrc.view.console.components;

import com.extrc.common.services.RankConstructor;
import com.extrc.common.structures.Rank;
import com.extrc.common.structures.Ranking;
import com.extrc.common.structures.Sequence;
import com.extrc.common.structures.SequenceElement;

public class BaseRankView {
  private final RankConstructor baseRank;

  public BaseRankView(RankConstructor baseRank) {
    this.baseRank = baseRank;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Ranking ranking = baseRank.getRanking();
    Sequence sequence = baseRank.getSequence();
    Rank infiniteRank = ranking.isEmpty() ? new Rank() : ranking.get(ranking.size() - 1);
    sb.append("Let all classical formulas be: Kc = ").append(infiniteRank).append(".\n\n");
    sb.append("Consider the exceptionality sequence E:\n");
    sb.append(" *  E(0) = { a=>b : a~>b ∈ K }\n");
    sb.append(" *  E(i+1) = { a=>b : E(i) ∪ Kc |= !a }\n");
    sb.append(" *  We stop the sequence where E(i-1) = E(i) for some i.\n");
    sb.append("\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n");
    for (int i = 0, n = sequence.size(); i < n; i++) {
      SequenceElement element = sequence.get(i);
      if (n > 4 && i >= 2 && i < n - 2) {
        if (i == 2) {
          sb.append("...\n");
        }
        continue;
      }
      sb.append(String.format("E(%d) = %s\n", element.getElementNumber(), element.toString()));
    }
    sb.append("\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n");
    sb.append("Ranking:\n");
    sb.append(" * Finite rank i is defined as R(i) = E(i) \\ E(i+1) \t [ where E(i) != E(i+1) ]\n");
    sb.append(" * Infinite rank is defned as R∞ = E(k) ∪ Kc \t\t [ where E(k) is last element ]\n");
    sb.append(new RankingView(ranking));
    return sb.toString();
  }

}
