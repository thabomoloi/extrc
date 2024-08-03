package com.extrc.view.web.helpers;

import com.extrc.common.structures.EntailmentResult;

public class EntailmentJson {
  public final String queryFormula;
  public final String knowledgeBase;
  public final boolean entailed;
  public final String[] sequence;
  public final String[] baseRanking;
  public final String[] removedRanking;
  public final String[] subsets;
  public final Object[] times;

  public EntailmentJson(EntailmentResult entailmentResult) {
    queryFormula = entailmentResult.getFormula().toString();
    knowledgeBase = entailmentResult.getKnowledgeBase().toString();
    entailed = entailmentResult.getEntailed();

    int n = entailmentResult.getBaseRank().getSequence().size();
    sequence = new String[n];
    for (int i = 0; i < n; i++) {
      sequence[i] = entailmentResult.getBaseRank().getSequence().get(i).toString();
    }

    n = entailmentResult.getBaseRank().getRanking().size();
    baseRanking = new String[n];
    for (int i = 0; i < n; i++) {
      baseRanking[i] = entailmentResult.getBaseRank().getRanking().get(i).toString();
    }

    n = entailmentResult.getRemovedRanking().size();
    removedRanking = new String[n];
    for (int i = 0; i < n; i++) {
      removedRanking[i] = entailmentResult.getRemovedRanking().get(i).toString();
    }

    n = entailmentResult.getSubsets().size();
    subsets = new String[n];
    for (int i = 0; i < n; i++) {
      subsets[i] = entailmentResult.getSubsets().get(i).toString();
    }

    n = entailmentResult.getTimer().size();
    times = new Object[n];
    for (int i = 0; i < n; i++) {
      final int j = i;
      times[i] = new Object() {
        @SuppressWarnings("unused")
        public final String title = entailmentResult.getTimer().get(j).getTitle();
        @SuppressWarnings("unused")
        public final double timeTaken = entailmentResult.getTimer().get(j).getTimeTaken();
      };
    }

  }
}
