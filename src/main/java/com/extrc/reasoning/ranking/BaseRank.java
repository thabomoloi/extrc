package com.extrc.reasoning.ranking;

import org.tweetyproject.logics.pl.reasoner.SatReasoner;
import org.tweetyproject.logics.pl.sat.Sat4jSolver;
import org.tweetyproject.logics.pl.sat.SatSolver;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.services.RankConstructor;
import com.extrc.common.structures.DefeasibleImplication;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.common.structures.Rank;
import com.extrc.common.structures.Ranking;
import com.extrc.common.structures.Sequence;

public class BaseRank implements RankConstructor {
  private final KnowledgeBase defeasibleKb;
  private final KnowledgeBase classicalKb;
  private Sequence sequence;
  private Ranking ranking;

  public BaseRank(KnowledgeBase knowledgeBase) {
    this.ranking = new Ranking();
    this.sequence = new Sequence();
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

  @Override
  public Ranking construct() {
    this.ranking = new Ranking();
    this.sequence = new Sequence();

    // SAT reasoner
    SatSolver.setDefaultSolver(new Sat4jSolver());
    SatReasoner reasoner = new SatReasoner();

    // Exceptionality sequences
    KnowledgeBase current = this.defeasibleKb;
    KnowledgeBase previous;

    this.sequence.addElement(current);
    do {
      previous = current;
      current = new KnowledgeBase();
      KnowledgeBase union = previous.union(this.classicalKb);
      KnowledgeBase exceptionals = new KnowledgeBase();

      for (PlFormula antecedent : previous.antecedents()) {
        if (reasoner.query(union, new Negation(antecedent))) {
          exceptionals.add(antecedent);
        }
      }

      Rank rank = new Rank();
      for (PlFormula formula : previous) {
        PlFormula antecedent = ((Implication) formula).getFormulas().getFirst();
        if (exceptionals.contains(antecedent)) {
          current.add(formula);
        } else {
          rank.add(formula);
        }
      }

      if (!rank.isEmpty()) {
        rank.setRankNumber(this.ranking.size());
        this.ranking.add(rank);
      }
      if (!current.equals(previous)) {
        this.sequence.addElement(current, exceptionals);
      }
    } while (!current.equals(previous));

    ranking.add(new Rank(Integer.MAX_VALUE, this.classicalKb.union(current)));
    return ranking;
  }

  @Override
  public Sequence getSequence() {
    return this.sequence;
  }

  @Override
  public Ranking getRanking() {
    return this.ranking;
  }

}
