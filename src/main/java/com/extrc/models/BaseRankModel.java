package com.extrc.models;

import java.util.ArrayList;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

public class BaseRankModel {
  private final Explanation explanation;
  private final PlBeliefSet defeasibleKb;
  private final PlBeliefSet classicalKb;
  private final ArrayList<PlBeliefSet> rankedKb;

  public BaseRankModel(PlBeliefSet defeasibleKb, PlBeliefSet classicalKb, Explanation explanation) {
    this.defeasibleKb = defeasibleKb;
    this.classicalKb = classicalKb;
    this.explanation = explanation;
    rankedKb = new ArrayList<PlBeliefSet>();
  }

  public ArrayList<PlBeliefSet> rank() {
    return rank(defeasibleKb);
  }

  public ArrayList<PlBeliefSet> rank(PlBeliefSet current) {
    PlBeliefSet previous = current;
    current = new PlBeliefSet();

    PlBeliefSet rank = new PlBeliefSet();
    PlBeliefSet temp = new PlBeliefSet(previous);
    temp.addAll(classicalKb);
    PlBeliefSet antecedants = extractAntecedants(previous);

    PlBeliefSet exceptionals = ExceptionalsService.getExceptionals(antecedants, temp);

    for (PlFormula formula : previous) {
      PlFormula antecedant = ((Implication) formula).getFormulas().getFirst();
      if (exceptionals.contains(antecedant)) {
        current.add(formula);
      }
      if (!classicalKb.contains(formula) && !current.contains(formula)) {
        rank.add(formula);
      }
    }

    if (!rank.isEmpty()) {
      rankedKb.add(rank);
      int rSize = rankedKb.size() - 1;
      explanation.show("Rank " + rSize + ": " + rank.toString());
    }

    if (!current.equals(previous)) {
      return rank(current);
    }

    rankedKb.add(classicalKb);
    explanation.show("Rank ∞: " + classicalKb);
    return rankedKb;
  }

  private PlBeliefSet extractAntecedants(PlBeliefSet beliefSet) {
    PlBeliefSet antecedants = new PlBeliefSet();
    for (PlFormula formula : beliefSet) {
      antecedants.add(((Implication) formula).getFormulas().getFirst());
    }
    return antecedants;
  }
}
