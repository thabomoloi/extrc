package com.extrc.services;

import org.tweetyproject.logics.pl.reasoner.SimplePlReasoner;
import org.tweetyproject.logics.pl.syntax.Implication;
import org.tweetyproject.logics.pl.syntax.Negation;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

import com.extrc.models.Entailment;
import com.extrc.models.Rank;
import com.extrc.models.RankList;

public class EntailmentServiceImpl implements EntailmentService {
  private final KnowledgeBaseService knowledgeBaseService;
  private Entailment entailment;

  public EntailmentServiceImpl(KnowledgeBaseService knowledgeBaseService) {
    this.knowledgeBaseService = knowledgeBaseService;
  }

  @Override
  public Entailment getEntailment() {
    return entailment;
  }

  @Override
  public KnowledgeBaseService getKnowledgeBaseService() {
    return knowledgeBaseService;
  }

  public boolean isQueryEntailed() {
    return entailment.isEntailed();
  }

  public int getNumberOfRemovedRanks() {
    return entailment.getNumberOfRemovedRanks();
  }

  public RankList getBaseRanks() {
    return entailment.getBaseRanks();
  }

  public void computeEntailment(RankList rankedKb, PlFormula queryFormula) {
    RankList baseRanks = new RankList();
    for (Rank rank : rankedKb.listRanks()) {
      baseRanks.addRank(rank);
    }

    System.out.println(baseRanks);

    queryFormula = ReasonerService.materialise(queryFormula);
    SimplePlReasoner classicalReasoner = new SimplePlReasoner();
    PlFormula negationOfAntecedent = new Negation(((Implication) queryFormula).getFormulas().getFirst());

    PlBeliefSet allRankFormulas = rankedKb.getAllFormulas();
    while (allRankFormulas.isEmpty()) {
      System.out.println(
          "We are checking whether or not " + negationOfAntecedent + " is entailed by: " + allRankFormulas.toString());
      if (classicalReasoner.query(allRankFormulas, negationOfAntecedent)) {
        Rank rank = rankedKb.pop();
        System.out.println("It is! so we remove " + rank);
        allRankFormulas.removeAll(rank.getFormulas());
      } else {
        System.out.println("It is not!");
        break;
      }
    }

    boolean entailed;
    if (allRankFormulas.isEmpty()) {
      System.out.println("We now check whether or not the formula" +
          queryFormula + " is entailed by "
          + allRankFormulas);
      entailed = (classicalReasoner.query(allRankFormulas, queryFormula));

    } else {
      System.out.println(
          "There would then be no ranks remaining, which means the knowledge base entails " +
              negationOfAntecedent.toString() + ", and thus it entails " + queryFormula.toString() +
              ", so we know the defeasible counterpart of this implication is also    entailed!");
      entailed = true;
    }

    entailment = new Entailment(entailed, baseRanks.size() - rankedKb.size(), baseRanks, rankedKb);
  }

}
