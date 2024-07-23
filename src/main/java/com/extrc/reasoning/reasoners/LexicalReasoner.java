package com.extrc.reasoning.reasoners;

import java.util.ArrayList;
import java.util.List;

import org.tweetyproject.logics.pl.reasoner.SatReasoner;
import org.tweetyproject.logics.pl.sat.Sat4jSolver;
import org.tweetyproject.logics.pl.sat.SatSolver;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.services.DefeasibleReasoner;
import com.extrc.common.services.Explanation;
import com.extrc.common.services.RankConstuctor;
import com.extrc.common.structures.Entailment;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.common.structures.Rank;
import com.extrc.common.structures.Ranking;
import com.extrc.common.structures.ReasonerTimer;
import com.extrc.reasoning.explanation.LCExplanation;
import com.extrc.reasoning.ranking.BaseRank;

public class LexicalReasoner implements DefeasibleReasoner {
  private final RankConstuctor rankConstructor;
  private final KnowledgeBase knowledgeBase;
  private final LCExplanation explanation;

  public LexicalReasoner(KnowledgeBase knowledgeBase) {
    this.knowledgeBase = knowledgeBase;
    this.explanation = new LCExplanation(this.knowledgeBase);
    this.rankConstructor = new BaseRank(knowledgeBase, this.explanation.getBaseRankExplanation());
  }

  @Override
  public Entailment query(PlFormula queryFormula) {
    ReasonerTimer timer = new ReasonerTimer();

    // Base ranking
    timer.start("Ranking formulas");
    Ranking baseRanking = rankConstructor.construct();
    timer.end();

    timer.start("Removing formulas");
    Ranking removedRanking = new Ranking();

    // SAT reasoner
    SatSolver.setDefaultSolver(new Sat4jSolver());
    SatReasoner reasoner = new SatReasoner();

    PlFormula formula = KnowledgeBase.materialise(queryFormula);
    PlFormula negation = new Negation(((Implication) formula).getFormulas().getFirst());

    KnowledgeBase formulas = new KnowledgeBase();
    for (Rank rank : baseRanking) {
      formulas.addAll(rank);
    }

    int i = 0;
    Ranking allSubsets = new Ranking();
    Ranking allDiscardedSubsets = new Ranking();

    while (!formulas.isEmpty() && reasoner.query(formulas, negation)) {
      Rank rank = baseRanking.get(i);
      formulas.removeAll(rank);

      Rank removedRank = new Rank();
      removedRank.setRankNumber(rank.getRankNumber());

      int subsetSize = rank.size() - 1;

      List<KnowledgeBase> subsets;

      if (subsetSize != 0) {
        do {
          subsets = refineRank(rank, subsetSize);

          for (KnowledgeBase subset : subsets) {
            allSubsets.add(new Rank(i, subset));
            if (!reasoner.query(formulas.union(subset), negation)) {
              formulas.addAll(subset);
            } else {
              allDiscardedSubsets.add(new Rank(i, subset));
            }
          }
          subsetSize--;

        } while (reasoner.query(formulas, negation) && subsetSize > 0);
        if (!subsets.isEmpty()) {
          // int min = subsets.get(subsets.size() - 1).size();
          // for (int j = subsets.size() - 1; j >= 0; j--) {
          // if (subsets.get(j).size() == min && rank.containsAll(subsets.get(j))) {
          // removedRank.addAll(subsets.get(j));
          // } else {
          // break;
          // }
          // }
          int min = allSubsets.get(allSubsets.size() - 1).size();
          for (int j = allSubsets.size() - 1; j >= 0; j--) {
            Rank removed = allSubsets.get(j);
            if (removed.size() == min && removed.getRankNumber() == i) {
              removedRank.addAll(removed.difference(formulas));
            }
          }

        }
      } else {
        removedRank.addAll(rank);
      }
      removedRanking.add(removedRank);
      i++;
    }

    boolean entailed = !formulas.isEmpty() && reasoner.query(formulas, formula);
    timer.end();

    this.explanation.setEntailed(entailed);
    this.explanation.setRemovedRanking(removedRanking);
    this.explanation.setDiscardedSubsets(allDiscardedSubsets);
    this.explanation.setSubsets(allSubsets);

    return new Entailment(knowledgeBase, baseRanking, removedRanking, queryFormula, entailed, timer);
  }

  private List<KnowledgeBase> refineRank(Rank rank, int size) {
    int n = rank.size();
    Object[] rankArray = rank.toArray();

    List<KnowledgeBase> subsets = new ArrayList<>();

    for (int bitmask = 0; bitmask < (1 << n); bitmask++) {
      if (Integer.bitCount(bitmask) == size) {
        KnowledgeBase subset = new KnowledgeBase();
        for (int i = 0; i < n; i++) {
          if ((bitmask & (1 << i)) != 0) {
            subset.add((PlFormula) rankArray[i]);
          }
        }
        subsets.add(subset);
      }
    }
    return subsets;
  }

  @Override
  public Explanation explain(PlFormula formula) {
    this.explanation.setFormula(formula);
    this.query(formula);
    return this.explanation;
  }

}
