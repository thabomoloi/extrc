package com.extrc.models_draft;

import java.util.ArrayList;

import org.tweetyproject.logics.pl.parser.PlParser;
import org.tweetyproject.logics.pl.syntax.PlBeliefSet;
import org.tweetyproject.logics.pl.syntax.PlFormula;

public class RationalClosure {
  PlBeliefSet knowledgeBase;
  PlParser parser;
  PlFormula queryFormula;

  public RationalClosure(PlBeliefSet kb, PlFormula formula, PlBeliefSet classicalStatements) {
    this.knowledgeBase = kb;
    this.parser = new PlParser();
    this.queryFormula = formula;
    ArrayList<PlBeliefSet> rankedKnowledgeBase = (new BaseRankModel(this.knowledgeBase, classicalStatements,
        new Explanation("base rank"))).rank();
    System.out.println(EntailmentChecker.checkEntailment(rankedKnowledgeBase, formula));
  }
}
