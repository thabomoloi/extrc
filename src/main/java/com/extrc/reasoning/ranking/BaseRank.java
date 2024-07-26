package com.extrc.reasoning.ranking;

import org.tweetyproject.logics.pl.reasoner.SatReasoner;
import org.tweetyproject.logics.pl.sat.Sat4jSolver;
import org.tweetyproject.logics.pl.sat.SatSolver;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.common.services.RankConstuctor;
import com.extrc.common.structures.DefeasibleImplication;
import com.extrc.common.structures.ExceptionalitySequence;
import com.extrc.common.structures.KnowledgeBase;
import com.extrc.common.structures.Rank;
import com.extrc.common.structures.Ranking;
import com.extrc.reasoning.explanation.BaseRankExplanation;

public class BaseRank implements RankConstuctor {
  private final KnowledgeBase defeasibleKb;
  private final KnowledgeBase classicalKb;
  private Ranking ranking;
  private final BaseRankExplanation explanation;

  public BaseRank(KnowledgeBase knowledgeBase, BaseRankExplanation explanation) {
    this.ranking = new Ranking();
    this.defeasibleKb = new KnowledgeBase();
    this.classicalKb = new KnowledgeBase();
    this.explanation = explanation;
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
    ExceptionalitySequence sequence = new ExceptionalitySequence();
    // SAT reasoner
    SatSolver.setDefaultSolver(new Sat4jSolver());
    SatReasoner reasoner = new SatReasoner();

    // Exceptionality sequences
    KnowledgeBase current = this.defeasibleKb.materialise();
    KnowledgeBase previous;

    sequence.addElement(current);
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
      sequence.addElement(current, exceptionals);
    } while (!current.equals(previous));

    ranking.add(new Rank(Integer.MAX_VALUE, this.classicalKb.union(current)));
    explanation.setBaseRanking(this.ranking);
    explanation.setSequence(sequence);
    return ranking;
  }

}
