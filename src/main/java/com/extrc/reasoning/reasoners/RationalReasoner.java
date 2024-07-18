package com.extrc.reasoning.reasoners;

import org.tweetyproject.logics.pl.reasoner.SatReasoner;
import org.tweetyproject.logics.pl.sat.Sat4jSolver;
import org.tweetyproject.logics.pl.sat.SatSolver;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.services.DefeasibleReasoner;
import com.extrc.common.services.RankConstuctor;
import com.extrc.common.structures.Entailment;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.common.structures.Rank;
import com.extrc.common.structures.Ranking;
import com.extrc.common.structures.ReasonerTimer;
import com.extrc.reasoning.ranking.BaseRank;

public class RationalReasoner implements DefeasibleReasoner {
  private final RankConstuctor rankConstructor;
  private final KnowledgeBase knowledgeBase;

  public RationalReasoner(KnowledgeBase knowledgeBase) {
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
      removedRanking.add(rank);
      formulas.removeAll(rank);
      i++;
    }

    boolean entailed = !formulas.isEmpty() && reasoner.query(formulas, formula);
    timer.end();
    return new Entailment(knowledgeBase, baseRanking, removedRanking, queryFormula, entailed, timer);
  }

}
