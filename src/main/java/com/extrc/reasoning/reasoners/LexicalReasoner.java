package com.extrc.reasoning.reasoners;

import org.tweetyproject.logics.pl.reasoner.SatReasoner;
import org.tweetyproject.logics.pl.sat.Sat4jSolver;
import org.tweetyproject.logics.pl.sat.SatSolver;
import org.tweetyproject.logics.pl.syntax.Conjunction;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.services.DefeasibleReasoner;
import com.extrc.common.services.RankConstuctor;
import com.extrc.common.structures.Entailment;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.common.structures.Rank;
import com.extrc.common.structures.Ranking;
import com.extrc.common.structures.ReasonerTimer;
import com.extrc.reasoning.ranking.BaseRank;

public class LexicalReasoner implements DefeasibleReasoner {
  private final RankConstuctor rankConstructor;
  private final KnowledgeBase knowledgeBase;

  public LexicalReasoner(KnowledgeBase knowledgeBase) {
    this.knowledgeBase = knowledgeBase;
    this.rankConstructor = new BaseRank(knowledgeBase);
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
    while (!formulas.isEmpty() && reasoner.query(formulas, negation)) {
      Rank rank = baseRanking.get(i);
      formulas.removeAll(rank);

      Rank removedRank = new Rank();
      removedRank.setRankNumber(rank.getRankNumber());

      int subsetSize = rank.size() - 1;

      KnowledgeBase rankFormulas;

      do {
        rankFormulas = weakenRank(rank, subsetSize);
        for (PlFormula conjunction : rankFormulas) {
          formulas.add(conjunction);
          if (reasoner.query(formulas, negation)) {
            formulas.remove(conjunction);
            Conjunction alpha = (Conjunction) conjunction;
            for (PlFormula f : alpha) {
              KnowledgeBase temp = new KnowledgeBase();
              temp.add(f);
              if (reasoner.query(formulas.union(temp), negation)) {
                removedRank.add(f);
              }
            }
          }
        }
        subsetSize--;

      } while (reasoner.query(formulas.union(rankFormulas), negation) && subsetSize > 0);

      removedRanking.add(removedRank);
      i++;
    }

    boolean entailed = !formulas.isEmpty() && reasoner.query(formulas, formula);
    timer.end();

    return new Entailment(knowledgeBase, baseRanking, removedRanking, queryFormula, entailed, timer);
  }

  private KnowledgeBase weakenRank(Rank rank, int size) {
    int n = rank.size();
    Object[] rankArray = rank.toArray();

    KnowledgeBase possibleFormulas = new KnowledgeBase();
    for (int bitmask = 0; bitmask < (1 << n); bitmask++) {
      if (Integer.bitCount(bitmask) == size) {
        PlBeliefSet conjunction = new PlBeliefSet();
        for (int i = 0; i < n; i++) {
          if ((bitmask & (1 << i)) != 0) {
            conjunction.add((PlFormula) rankArray[i]);
          }
        }
        possibleFormulas.add(new Conjunction(conjunction));
      }
    }
    return possibleFormulas;
  }

}
