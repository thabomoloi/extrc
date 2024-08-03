package com.extrc.view.web.helpers;

import com.extrc.common.structures.EntailmentResult;

import java.util.List;
import java.util.stream.Collectors;

import com.extrc.common.structures.Rank;

public class EntailmentJson {
  public final String queryFormula;
  public final String knowledgeBase;
  public final boolean entailed;
  public final List<String> sequence;
  public final List<RankJson> baseRanking;
  public final List<RankJson> removedRanking;
  public final List<RankJson> subsets;
  public final List<TimeJson> times;

  public EntailmentJson(EntailmentResult entailmentResult) {
    this.queryFormula = entailmentResult.getFormula().toString();
    this.knowledgeBase = entailmentResult.getKnowledgeBase().toString().replaceAll("[{}]", "");
    this.entailed = entailmentResult.getEntailed();
    this.sequence = entailmentResult.getBaseRank().getSequence().stream()
        .map(seq -> seq.toString().replaceAll("[{}]", ""))
        .collect(Collectors.toList());
    this.baseRanking = createRankList(entailmentResult.getBaseRank().getRanking());
    this.removedRanking = createRankList(entailmentResult.getRemovedRanking());
    this.subsets = createRankList(entailmentResult.getSubsets());
    this.times = entailmentResult.getTimer().stream()
        .map(timer -> new TimeJson(timer.getTitle(), timer.getTimeTaken()))
        .collect(Collectors.toList());
  }

  private List<RankJson> createRankList(List<Rank> rankList) {
    return rankList.stream()
        .map(rank -> new RankJson(
            rank.getRankNumber() == Integer.MAX_VALUE ? "∞" : Integer.toString(rank.getRankNumber()),
            rank.formulasToString()))
        .collect(Collectors.toList());
  }

  public static class RankJson {
    public final String rankNumber;
    public final String formulas;

    public RankJson(String rankNumber, String formulas) {
      this.rankNumber = rankNumber;
      this.formulas = formulas;
    }
  }

  public static class TimeJson {
    public final String title;
    public final double timeTaken;

    public TimeJson(String title, double timeTaken) {
      this.title = title;
      this.timeTaken = timeTaken;
    }
  }
}
