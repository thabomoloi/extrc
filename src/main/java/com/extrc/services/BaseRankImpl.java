package com.extrc.services;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.ClassicalKnowledgeBase;
import com.extrc.models.DefeasibleKnowledgeBase;
import com.extrc.models.Kb;
import com.extrc.models.KnowledgeBase;
import com.extrc.models.Rank;
import com.extrc.models.Ranking;

public final class BaseRankImpl implements IBaseRank {
  private KnowledgeBase knowledgeBase;
  private DefeasibleKnowledgeBase defeasibleKb;
  private ClassicalKnowledgeBase classicalKb;
  private Ranking ranking;

  public BaseRankImpl(KnowledgeBase knowledgeBase) {
    this.setKnowledgeBase(knowledgeBase);
    this.ranking = new Ranking();
  }

  @Override
  public void setKnowledgeBase(KnowledgeBase knowledgeBase) {
    this.knowledgeBase = knowledgeBase;
    this.defeasibleKb = knowledgeBase.getDefeasibleKb();
    this.classicalKb = knowledgeBase.getClassicalKb();
  }

  @Override
  public KnowledgeBase getKnowledgeBase() {
    return knowledgeBase;
  }

  @Override
  public Ranking computeBaseRank() {
    this.ranking = new Ranking();
    return computeBaseRank(this.defeasibleKb.materialise());
  }

  private Ranking computeBaseRank(ClassicalKnowledgeBase current) {

    ClassicalKnowledgeBase previous = current;
    current = new ClassicalKnowledgeBase();

    ClassicalKnowledgeBase temp = new ClassicalKnowledgeBase(previous);
    temp.addAll(this.classicalKb.getFormulas());

    ClassicalKnowledgeBase antecedants = Kb.getAntecedants(previous);
    ClassicalKnowledgeBase exceptionals = Kb.getExceptionals(antecedants, temp);

    ClassicalKnowledgeBase formulas = new ClassicalKnowledgeBase();

    for (PlFormula formula : previous.getFormulas()) {
      PlFormula antecedant = ((Implication) formula).getFormulas().getFirst();
      if (exceptionals.contains(antecedant)) {
        current.add(formula);
      }
      if (!classicalKb.contains(formula) && !current.contains(formula)) {
        formulas.add(formula);
      }
    }

    if (!formulas.isEmpty()) {
      int rankIdx = ranking.size();
      ranking.add(new Rank(rankIdx, formulas));
    }

    if (!current.equals(previous)) {
      return computeBaseRank(current);
    }

    ranking.add(new Rank(Integer.MAX_VALUE, this.classicalKb));

    return ranking;
  }

  public static Ranking rank(KnowledgeBase knowledgeBase) {
    return (new BaseRankImpl(knowledgeBase)).computeBaseRank();
  }

}
