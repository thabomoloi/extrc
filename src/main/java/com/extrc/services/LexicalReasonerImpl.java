package com.extrc.services;

import java.util.ArrayList;
import java.util.List;

import org.tweetyproject.logics.pl.reasoner.SatReasoner;
import org.tweetyproject.logics.pl.sat.Sat4jSolver;
import org.tweetyproject.logics.pl.sat.SatSolver;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.BaseRank;
import com.extrc.models.Entailment;
import com.extrc.models.KnowledgeBase;
import com.extrc.models.Rank;
import com.extrc.models.Ranking;

public class LexicalReasonerImpl implements ReasonerService {
  private final SatReasoner reasoner;

  public LexicalReasonerImpl() {
    SatSolver.setDefaultSolver(new Sat4jSolver());
    reasoner = new SatReasoner();
  }

  @Override
  public Entailment getEntailment(BaseRank baseRank) {
    long startTime = System.nanoTime();

    // Get inputs
    PlFormula queryFormula = baseRank.getQueryInput().getQueryFormula();
    PlFormula negation = new Negation(((Implication) queryFormula).getFirstFormula());
    KnowledgeBase knowledgeBase = baseRank.getQueryInput().getKnowledgeBase();
    Ranking baseRanking = baseRank.getRanking();
    Ranking removedRanking = new Ranking();
    Ranking subsets = new Ranking();

    KnowledgeBase union = new KnowledgeBase();
    baseRanking.forEach(rank -> {
      union.addAll(rank);
    });

    int i = 0;
    while (!union.isEmpty() && reasoner.query(union, negation) && i < baseRanking.size() - 1) {
      union.removeAll(union);

      KnowledgeBase removedFormulas = new KnowledgeBase();
      int m = baseRanking.size() - 1;

      List<KnowledgeBase> rankSubsets;
      if (m != 0) {
        do {
          rankSubsets = refineRank(baseRanking.get(i), m);
          int j = i;
          rankSubsets.forEach(subset -> {
            subsets.addRank(j, subset);
            if (!reasoner.query(union.union(subset), negation)) {
              union.addAll(subset);
            }
          });
          m--;
        } while (reasoner.query(union, negation) && m > 0);

        if (!rankSubsets.isEmpty()) {
          int min = subsets.get(subsets.size() - 1).size();
          for (int k = subsets.size() - 1; k >= 0; k--) {
            if (subsets.get(k).size() == min && subsets.get(k).getRankNumber() == i) {
              removedFormulas.addAll(subsets.get(i).difference(union));
            }
          }
        }
      } else {
        removedFormulas.addAll(baseRanking.get(i));
      }
      removedRanking.addRank(i, removedFormulas);
      i++;
    }

    boolean entailed = !union.isEmpty() && reasoner.query(union, queryFormula);
    long endTime = System.nanoTime();
    return new Entailment(knowledgeBase, queryFormula, baseRanking, removedRanking, subsets, entailed,
        (endTime - startTime) / 1_000_000_000.0);
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

}
