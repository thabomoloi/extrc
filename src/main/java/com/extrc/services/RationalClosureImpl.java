package com.extrc.services;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.ClassicalKnowledgeBase;
import com.extrc.models.EntailmentResult;
import com.extrc.models.Kb;
import com.extrc.models.KnowledgeBase;
import com.extrc.models.Rank;
import com.extrc.models.Ranking;

public class RationalClosureImpl implements IDefeasibleEntailment {
  private final KnowledgeBase knowledgeBase;
  private final PlFormula queryFormula;
  private final ExplanationsImpl explanationsImpl;

  public RationalClosureImpl(KnowledgeBase knowledgeBase, PlFormula queryFormula, ExplanationsImpl explanationsImpl) {
    this.knowledgeBase = knowledgeBase;
    this.queryFormula = queryFormula;
    this.explanationsImpl = explanationsImpl;
  }

  @Override
  public EntailmentResult getEntailmentResult() {

    Ranking baseRanking = BaseRankImpl.rank(this.knowledgeBase, this.explanationsImpl);

    Ranking baseRankingCopy = new Ranking(baseRanking);
    Ranking removedRanking = new Ranking();

    PlFormula formula = Kb.materialise(queryFormula);
    PlFormula negation = new Negation(((Implication) formula).getFormulas().getFirst());
    ClassicalKnowledgeBase formulas = new ClassicalKnowledgeBase(baseRanking.getKnowlegeBase().getFormulas());

    while (!formulas.isEmpty() && Kb.isEntailed(formulas, negation)) {
      Rank rank = baseRankingCopy.discard();
      removedRanking.add(rank);
      formulas.removeAll(rank.getKnowledgeBase());
    }

    boolean entailed = !formulas.isEmpty() && Kb.isEntailed(formulas, negation);
    return new EntailmentResult(knowledgeBase, baseRanking, removedRanking, entailed, queryFormula);

  }

  public static final EntailmentResult query(KnowledgeBase knowledgeBase, PlFormula formula,
      ExplanationsImpl explanation) {
    return new RationalClosureImpl(knowledgeBase, formula, explanation).getEntailmentResult();
  }

}
