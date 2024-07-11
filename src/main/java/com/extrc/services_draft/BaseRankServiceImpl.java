package com.extrc.services_draft;

import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

public class BaseRankServiceImpl {
  private final KnowledgeBaseService knowledgeBaseService;
  private final RankList rankedKb;
  private final PlBeliefSet classicalFormulas;
  private final PlBeliefSet defeasibleFormulas;

  public BaseRankServiceImpl(KnowledgeBaseService knowledgeBaseService) {
    this.knowledgeBaseService = knowledgeBaseService;
    this.rankedKb = new RankList();
    this.classicalFormulas = this.knowledgeBaseService.getClassicalFormulas();
    this.defeasibleFormulas = this.knowledgeBaseService.getDefeasibleFormulas();
  }

  public RankList computeBaseRank() {
    return computeBaseRank(ReasonerService.materialise(defeasibleFormulas));
  }

  private RankList computeBaseRank(PlBeliefSet current) {
    PlBeliefSet previous = current;
    current = new PlBeliefSet();

    PlBeliefSet temp = new PlBeliefSet(previous);
    temp.addAll(this.classicalFormulas);

    PlBeliefSet antecedants = ReasonerService.extractAntecedants(previous);
    PlBeliefSet exceptionals = ReasonerService.getExceptionals(antecedants, temp);

    PlBeliefSet rankFormulas = new PlBeliefSet();

    for (PlFormula formula : previous) {
      PlFormula antecedant = ((Implication) formula).getFormulas().getFirst();
      if (exceptionals.contains(antecedant)) {
        current.add(formula);
      }
      if (!classicalFormulas.contains(formula) && !current.contains(formula)) {
        rankFormulas.add(formula);
      }
    }

    if (!rankFormulas.isEmpty()) {
      int newRankNumber = rankedKb.size();
      rankedKb.addRank(new RankDraft(newRankNumber, ReasonerService.dematerialise(rankFormulas)));
      // explanation.show("Rank " + rSize + ": " + rank.toString());
    }

    if (!current.equals(previous)) {
      return computeBaseRank(current);
    }

    rankedKb.addRank(new RankDraft(Integer.MAX_VALUE, classicalFormulas));

    return rankedKb;
  }
}
