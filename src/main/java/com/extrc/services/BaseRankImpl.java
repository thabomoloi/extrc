package com.extrc.services;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.ClassicalKnowledgeBase;
import com.extrc.models.DefeasibleKnowledgeBase;
import com.extrc.models.Explanation;
import com.extrc.models.Kb;
import com.extrc.models.KnowledgeBase;
import com.extrc.models.Rank;
import com.extrc.models.Ranking;

public final class BaseRankImpl implements IBaseRank {
  private KnowledgeBase knowledgeBase;
  private DefeasibleKnowledgeBase defeasibleKb;
  private ClassicalKnowledgeBase classicalKb;
  private Ranking ranking;
  private ExplanationsImpl explanationImpl;
  private final Explanation.Title explanationTitle = Explanation.Title.BaseRank;

  public BaseRankImpl(KnowledgeBase knowledgeBase, ExplanationsImpl explanationImpl) {
    this.setKnowledgeBase(knowledgeBase);
    this.ranking = new Ranking();
    this.explanationImpl = explanationImpl;
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
  public void setExplanations(ExplanationsImpl explanationImpl) {
    this.explanationImpl = explanationImpl;
  }

  @Override
  public ExplanationsImpl getExplanations() {
    return explanationImpl;
  }

  @Override
  public Ranking computeBaseRank() {
    this.ranking = new Ranking();
    explanationImpl.addExplanationStep(explanationTitle, "K is first separated classical formulas "
        + this.classicalKb.toString() + ", and defeasible formulas " + this.defeasibleKb.toString() + "\n");
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

  public static Ranking rank(KnowledgeBase knowledgeBase, ExplanationsImpl explanation) {
    return (new BaseRankImpl(knowledgeBase, explanation)).computeBaseRank();
  }

}
