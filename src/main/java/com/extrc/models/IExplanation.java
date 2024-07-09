package com.extrc.models;

public interface IExplanation {
  public void addSubscriber(IObserver observer);

  public void removeSubscriber(IObserver observer);

  public void notifySubscribers(String explanation);
}
