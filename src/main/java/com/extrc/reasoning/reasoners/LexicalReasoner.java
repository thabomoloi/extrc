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
import com.extrc.common.structures.EntailmentResult;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.common.structures.Rank;
import com.extrc.common.structures.Ranking;
import com.extrc.common.structures.ReasonerTimer;
import com.extrc.reasoning.ranking.BaseRank;

/**
 * This class represents a defeasible reasoner using lexicographic closure.
 * 
 * @author Thabo Vincent Moloi
 */
public class LexicalReasoner implements DefeasibleReasoner {
  /** Knowledge base used to reason. */
  private KnowledgeBase knowledgeBase;

  /**
   * Constructs a new lexicographic closure reasoner.
   * 
   * @param knowledgeBase
   */
  public LexicalReasoner(KnowledgeBase knowledgeBase) {
    this.knowledgeBase = knowledgeBase;
  }

  @Override
  public EntailmentResult query(PlFormula queryFormula) {
    EntailmentResult entailment = new EntailmentResult(queryFormula, this.knowledgeBase,
        new BaseRank(this.knowledgeBase));
    ReasonerTimer timer = entailment.getTimer();

    // Base ranking
    timer.start("Base Rank");
    Ranking baseRanking = entailment.getBaseRank().construct();
    timer.end();

    timer.start("Lexicographic Closure");
    Ranking removedRanking = entailment.getRemoveRanking();

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
    Ranking allSubsets = entailment.getSubsets();

    while (!formulas.isEmpty() && reasoner.query(formulas, negation) && i < baseRanking.size() - 1) {
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
            }
          }
          subsetSize--;

        } while (reasoner.query(formulas, negation) && subsetSize > 0);
        if (!subsets.isEmpty()) {
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

    entailment.setEntailed(entailed);

    return entailment;
  }

  /**
   * Finds subsets of a specific size in which rank formulas are removed.
   * 
   * @param rank Rank to refine.
   * @param size Size of the subset.
   * @return The list of subsets representing possible ranks.
   */
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
  public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
    this.knowledgeBase = knowledgeBase;
  }

  @Override
  public KnowledgeBase getKnowledgeBase() {
    return this.knowledgeBase;
  }
}
