package com.extrc.models;

import java.util.LinkedList;
import java.util.Queue;

public class RankList {
  private final Queue<Rank> ranks;

  public RankList() {
    this.ranks = new LinkedList<>();
  }

  public RankList(RankList rankList) {
    this.ranks = new LinkedList<Rank>();
    for (Rank rank : rankList.ranks) {
      this.ranks.add(new Rank(rank));
    }
  }

  public void add(Rank rank) {
    this.ranks.add(rank);
  }

  public Rank get(int rankNumber) {
    for (Rank rank : this.ranks) {
      if (rank.getRankNumber() == rankNumber) {
        return rank;
      }
    }
    return null;
  }

  public Rank discard() {
    return this.ranks.poll();
  }

  public int size() {
    return this.ranks.size();
  }

  public KnowledgeBase getKnowlegeBase() {
    KnowledgeBase knowledgeBase = new KnowledgeBase();
    for (Rank rank : this.ranks) {
      knowledgeBase.addAll(rank.getKnowledgeBase().getFormulas());
    }
    return knowledgeBase;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for (Rank rank : ranks) {
      builder.append(rank).append("\n");
    }
    return builder.toString();
  }
}
