package com.extrc.reasoning.reasoners;

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
 * This class represents a defeasible reasoner using rational closure.
 * 
 * @author Thabo Vincent Moloi
 */
public class RationalReasoner implements DefeasibleReasoner {
  /** Knowledge base used to reason. */
  private KnowledgeBase knowledgeBase;

  /**
   * Constructs a new lexicographic closure reasoner.
   * 
   * @param knowledgeBase
   */
  public RationalReasoner(KnowledgeBase knowledgeBase) {
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

    timer.start("Rational Closure");
    Ranking removedRanking = entailment.getRemovedRanking();

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
    while (!formulas.isEmpty() && reasoner.query(formulas, negation) && i < baseRanking.size() - 1) {
      Rank rank = baseRanking.get(i);
      removedRanking.add(rank);
      formulas.removeAll(rank);
      i++;
    }

    boolean entailed = !formulas.isEmpty() && reasoner.query(formulas, formula);
    timer.end();
    entailment.setEntailed(entailed);
    return entailment;
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
