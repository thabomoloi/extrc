package com.extrc.services;

import java.util.Arrays;

import org.tweetyproject.logics.pl.reasoner.SatReasoner;
import org.tweetyproject.logics.pl.sat.Sat4jSolver;
import org.tweetyproject.logics.pl.sat.SatSolver;
import org.tweetyproject.logics.pl.syntax.Conjunction;
import org.tweetyproject.logics.pl.syntax.Disjunction;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.BaseRank;
import com.extrc.models.Entailment;
import com.extrc.models.KnowledgeBase;
import com.extrc.models.LexicalEntailment;
import com.extrc.models.Rank;
import com.extrc.models.Ranking;

public class LexicalReasonerImpl implements ReasonerService {
  private final SatReasoner reasoner;

  public LexicalReasonerImpl() {
    SatSolver.setDefaultSolver(new Sat4jSolver());
    reasoner = new SatReasoner();
  }

  @Override
  public Entailment getEntailment(BaseRank baseRank, PlFormula queryFormula) {
    long startTime = System.nanoTime();

    // Get inputs
    PlFormula negation = new Negation(((Implication) queryFormula).getFirstFormula());
    KnowledgeBase knowledgeBase = baseRank.getKnowledgeBase();
    Ranking baseRanking = baseRank.getRanking();
    Ranking weakenedRanking = new Ranking();

    KnowledgeBase union = new KnowledgeBase();
    baseRanking.forEach(rank -> {
      union.addAll(rank.getFormulas());
    });

    int i = 0;
    while (!union.isEmpty() && reasoner.query(union, negation) && i < baseRanking.size() - 1) {
      union.removeAll(baseRanking.get(i).getFormulas());

      int m = baseRanking.get(i).getFormulas().size() - 1;

      if (m != 0) {
        do {
          KnowledgeBase weakenedRank = new KnowledgeBase(Arrays.asList(weakenRank(baseRanking.get(i), m)));
          weakenedRanking.add(new Rank(i, weakenedRank));

          if (!reasoner.query(union.union(weakenedRank), negation)) {
            union.addAll(weakenedRank);
          }
          m--;
        } while (reasoner.query(union, negation) && m > 0);
      }
      i++;
    }

    // Add remaining formulas that are not weakened
    for (int k = i, n = baseRanking.size(); k < n; k++) {
      weakenedRanking.add(baseRanking.get(i));
    }

    boolean entailed = !union.isEmpty() && reasoner.query(union, queryFormula);
    long endTime = System.nanoTime();

    return new LexicalEntailment.LexicalEntailmentBuilder()
        .withKnowledgeBase(knowledgeBase)
        .withQueryFormula(queryFormula)
        .withBaseRanking(baseRanking)
        .withWeakenedRanking(weakenedRanking)
        .withEntailed(entailed)
        .withTimeTaken((endTime - startTime) / 1_000_000_000.0)
        .build();
  }

  private Disjunction weakenRank(Rank rank, int size) {
    int n = rank.getFormulas().size();
    Object[] rankArray = rank.getFormulas().toArray();
    Disjunction weakenedRank = new Disjunction();
    for (int bitmask = 0; bitmask < (1 << n); bitmask++) {
      if (Integer.bitCount(bitmask) == size) {
        Conjunction conjunction = new Conjunction();
        for (int i = 0; i < n; i++) {
          if ((bitmask & (1 << i)) != 0) {
            conjunction.add((PlFormula) rankArray[i]);
          }
        }
        weakenedRank.add(conjunction);
      }
    }
    return weakenedRank;
  }

}
