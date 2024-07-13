package com.extrc.reasoning.ranking;

import org.tweetyproject.logics.pl.reasoner.SatReasoner;
import org.tweetyproject.logics.pl.sat.Sat4jSolver;
import org.tweetyproject.logics.pl.sat.SatSolver;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.services.RankConstuctor;
import com.extrc.common.structures.DefeasibleImplication;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.common.structures.Rank;
import com.extrc.common.structures.Ranking;

public class BaseRank implements RankConstuctor {
  private final KnowledgeBase defeasibleKb;
  private final KnowledgeBase classicalKb;
  private Ranking ranking;

  public BaseRank(KnowledgeBase knowledgeBase) {
    this.ranking = new Ranking();
    this.defeasibleKb = new KnowledgeBase();
    this.classicalKb = new KnowledgeBase();
    for (PlFormula formula : knowledgeBase) {
      if (formula instanceof DefeasibleImplication) {
        this.defeasibleKb.add(formula);
      } else {
        this.classicalKb.add(formula);
      }
    }
  }

  public Ranking construct() {
    this.ranking = new Ranking();

    // SAT reasoner
    SatSolver.setDefaultSolver(new Sat4jSolver());
    SatReasoner reasoner = new SatReasoner();

    // Exceptionality sequences
    KnowledgeBase current = this.defeasibleKb.materialise();
    KnowledgeBase previous = new KnowledgeBase();

    while (!current.equals(previous)) {
      previous = current;
      current = new KnowledgeBase();
      KnowledgeBase union = previous.union(this.classicalKb);
      KnowledgeBase exceptionals = new KnowledgeBase();
      for (PlFormula antecedant : previous.antecedants()) {
        if (reasoner.query(union, new Negation(antecedant))) {
          exceptionals.add(antecedant);
        }
      }
      Rank rank = new Rank();
      for (PlFormula formula : previous) {
        PlFormula antecedant = ((Implication) formula).getFormulas().getFirst();
        if (exceptionals.contains(antecedant)) {
          current.add(formula);
        } else {
          rank.add(formula);
        }
      }

      if (!rank.isEmpty()) {
        rank.setRankNumber(this.ranking.size());
        this.ranking.add(rank);
      }

    }
    ranking.add(new Rank(Integer.MAX_VALUE, this.classicalKb));
    return ranking;
  }

}
