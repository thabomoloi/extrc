package com.extrc.draft.services;

import org.tweetyproject.logics.pl.syntax.Conjunction;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.draft.models.ClassicalKnowledgeBase;
import com.extrc.draft.models.EntailmentResult;
import com.extrc.draft.models.Kb;
import com.extrc.draft.models.KnowledgeBase;
import com.extrc.draft.models.Rank;
import com.extrc.draft.models.Ranking;

public class LexicographicClosureImpl implements IDefeasibleEntailment {
  private final KnowledgeBase knowledgeBase;
  private final PlFormula queryFormula;
  private final ExplanationsImpl explanationsImpl;

  public LexicographicClosureImpl(KnowledgeBase knowledgeBase, PlFormula queryFormula,
      ExplanationsImpl explanationsImpl) {
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

    int i = 0;
    while (!formulas.isEmpty() && Kb.isEntailed(formulas, negation)) {
      Rank rank = baseRanking.get(i);
      formulas.removeAll(rank.getKnowledgeBase());
      int m = rank.size() - 1;

      Rank weakRank = getWeakenedRank(rank, m);
      ClassicalKnowledgeBase union = new ClassicalKnowledgeBase(formulas);
      ClassicalKnowledgeBase weaken = new ClassicalKnowledgeBase();
      for (PlFormula conjunction : weakRank.getKnowledgeBase().getFormulas()) {
        union.add(conjunction);
        if (Kb.isEntailed(union, negation)) {
          System.out.println(conjunction + " " + union.remove(conjunction));
          weakRank.getKnowledgeBase().remove(formula);
        } else {
          weaken.add(conjunction);
        }
      }
      formulas.addAll(weaken.getFormulas());
      System.out.println(formulas);
      i++;

      break;
    }

    boolean entailed = !formulas.isEmpty() && Kb.isEntailed(formulas, formula);
    return new EntailmentResult(knowledgeBase, baseRanking, removedRanking, entailed, queryFormula);

  }

  public Rank getWeakenedRank(Rank rank, int size) {
    int n = rank.size();
    Rank weakenedRank = new Rank(rank);

    Object[] rankArray = rank.getKnowledgeBase().getFormulas().toArray();

    PlBeliefSet disjunction = new PlBeliefSet();
    for (int bitmask = 0; bitmask < (1 << n); bitmask++) {
      if (Integer.bitCount(bitmask) == size) {
        PlBeliefSet conjunction = new PlBeliefSet();
        for (int i = 0; i < n; i++) {
          if ((bitmask & (1 << i)) != 0) {
            conjunction.add((PlFormula) rankArray[i]);
          }
        }
        disjunction.add(new Conjunction(conjunction));
      }
    }
    weakenedRank.getKnowledgeBase().setFormulas(disjunction);
    return weakenedRank;
  }

  public static final EntailmentResult query(KnowledgeBase knowledgeBase, PlFormula formula,
      ExplanationsImpl explanation) {
    return new LexicographicClosureImpl(knowledgeBase, formula, explanation).getEntailmentResult();
  }

}
