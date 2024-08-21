package com.extrc.services;

import org.tweetyproject.logics.pl.reasoner.SatReasoner;
import org.tweetyproject.logics.pl.sat.Sat4jSolver;
import org.tweetyproject.logics.pl.sat.SatSolver;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.BaseRank;
import com.extrc.models.Entailment;
import com.extrc.models.KnowledgeBase;
import com.extrc.models.Ranking;
import com.extrc.models.RationalEntailment;

public class RationalReasonerImpl implements ReasonerService {
  private final SatReasoner reasoner;

  public RationalReasonerImpl() {
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
    Ranking removedRanking = new Ranking();

    KnowledgeBase union = new KnowledgeBase();
    baseRanking.forEach(rank -> {
      union.addAll(rank.getFormulas());
    });

    int i = 0;
    while (!union.isEmpty() && reasoner.query(union, negation) && i < baseRanking.size() - 1) {
      removedRanking.add(baseRanking.get(i));
      union.removeAll(baseRanking.get(i).getFormulas());
      i++;
    }

    boolean entailed = !union.isEmpty() && reasoner.query(union, queryFormula);
    long endTime = System.nanoTime();

    return new RationalEntailment.RationalEntailmentBuilder()
        .withKnowledgeBase(knowledgeBase)
        .withQueryFormula(queryFormula)
        .withBaseRanking(baseRanking)
        .withRemovedRanking(removedRanking)
        .withEntailed(entailed)
        .withTimeTaken((endTime - startTime) / 1_000_000_000.0)
        .build();
  }

}
