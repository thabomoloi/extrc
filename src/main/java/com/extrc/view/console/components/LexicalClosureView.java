package com.extrc.view.console.components;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.structures.EntailmentResult;
import com.extrc.common.structures.Rank;
import com.extrc.common.structures.Ranking;

public class LexicalClosureView {
  public final EntailmentResult entailment;

  public LexicalClosureView(EntailmentResult entailment) {
    this.entailment = entailment;
  }

  @Override
  public String toString() {
    Ranking ranking = new Ranking(entailment.getBaseRank().getRanking());
    Ranking removedRanking = entailment.getRemovedRanking();
    PlFormula query = entailment.getFormula();
    Negation negation = new Negation(((Implication) query).getFirstFormula());
    Ranking subsets = entailment.getSubsets();

    StringBuilder sb = new StringBuilder();
    sb.append("\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n");
    if (removedRanking.isEmpty()) {
      sb.append("Ranks 0 to ∞ do not entail ").append(negation).append("\n");
      sb.append("No ranks are removed.\n");
    } else {
      for (int i = 0, n = removedRanking.size(); i < n; i++) {
        Rank rank = removedRanking.get(i);
        if (n > 4 && i >= 2 && i < n - 2) {
          if (i == 2) {
            sb.append("...\n");
          }
          continue;
        }
        sb.append("Ranks ").append(rank.getRankNumber()).append(" to ∞ entail ").append(negation).append(".");
        Ranking curr = new Ranking();
        for (Rank subset : subsets) {
          if (rank.getRankNumber() == subset.getRankNumber()) {
            curr.add(subset);
          }
        }

        if (ranking.get(0).size() == 1) {
          sb.append(" Remove rank ").append(rank.getRankNumber()).append(".\n");
          ranking.remove(0);
          sb.append("Remaining ranks:\n").append(new RankingView(ranking)).append("\n");
        } else {
          for (int j = 0, m = curr.size(); j < m; j++) {
            if (m > 2 && j >= 1 && j < m - 1) {
              if (j == 2) {
                sb.append("\n..........\n..........");
              }
              continue;
            }
            Ranking copyRanking = new Ranking(ranking);
            Rank subset = curr.get(j);
            sb.append(
                String.format("\nPossible ranking (Rank %d subset = %s):\n", subset.getRankNumber(),
                    subset.toString()));
            copyRanking.set(0, subset);
            sb.append(new RankingView(copyRanking)).append("\n\n");

            if (rank.containsAll(subset)) {
              sb.append(String.format("Ranks %d to ∞ entail ", rank.getRankNumber())).append(negation).append(".");
              if (j == m - 1 && rank.size() == ranking.get(0).size()) {
                sb.append(String.format(" Discard rank %d.\n", rank.getRankNumber()));
                ranking.remove(0);
                sb.append("Remaining ranks:\n").append(new RankingView(ranking)).append("\n");
              } else {
                sb.append(" Move to the next subset.");
              }
            } else {
              sb.append(String.format("Ranks %d to ∞ do not entail ", rank.getRankNumber())).append(negation)
                  .append(".\n");
              ranking.set(0, subset);
              break;
            }
          }
        }
        if (i < n - 1) {
          sb.append("\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\u2550\n");
        }
      }
    }
    sb.append("\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\n");

    if (entailment.getEntailed()) {
      sb.append("The remaining ranks entail the formula ").append(query).append(".\n");
      sb.append("Therefore ").append(query).append(" is entailed by the knowledge base.\n");
    } else {
      sb.append("The remaining ranks do not entail the formula ").append(query).append(".\n");
      sb.append("Therefore ").append(query).append(" is not entailed by the knowledge base.\n");
    }
    return sb.toString();
  }

}
