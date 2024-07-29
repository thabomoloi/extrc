package com.extrc.common.structures;

import java.util.ArrayList;

/**
 * Represents the time taken for different algorithms.
 * This class extends ArrayList to store multiple TimerNode instances.
 * Each TimerNode represents the time taken by a specific algorithm.
 * 
 * <p>
 * Usage example:
 * </p>
 * 
 * <pre>{@code
 * ReasonerTimer timer = new ReasonerTimer();
 * 
 * timer.start("Algorithm A");
 * // run algorithm A
 * timer.end();
 * 
 * timer.start("Algorithm B");
 * // run algorithm B
 * timer.end();
 * 
 * for (ReasonerTimer.TimerNode node : timer) {
 *   System.out.println(node.getTitle() + ": " + node.getTimeTaken() + " seconds");
 * }
 * }</pre>
 * 
 * The above usage example will output the time taken by each algorithm.
 * 
 * @author Thabo Vincent Moloi
 */
public class ReasonerTimer extends ArrayList<ReasonerTimer.TimerNode> {

  /** Current title of the running timer. */
  private String currentTitle = "";
  /** Start time in nanoseconds. */
  private long startTime = 0;

  /**
   * Starts the timer for a specific algorithm.
   * 
   * @param title Name of the algorithm.
   */
  public void start(String title) {
    this.currentTitle = title;
    this.startTime = System.nanoTime();
  }

  /**
   * Ends the timer for the current algorithm and records the time taken.
   */
  public void end() {
    long endTime = System.nanoTime();
    double timeTakenInSeconds = (endTime - startTime) / 1_000_000_000.0;
    this.add(new TimerNode(currentTitle, timeTakenInSeconds));
  }

  /**
   * Represents the time taken for a specific algorithm.
   */
  public static class TimerNode {
    /** Name of the algorithm. */
    private final String title;
    /** Time taken in seconds. */
    private final double timeTaken;

    /**
     * Constructs a new TimerNode.
     * 
     * @param title     Name of the algorithm.
     * @param timeTaken Time taken in seconds.
     */
    public TimerNode(String title, double timeTaken) {
      this.title = title;
      this.timeTaken = timeTaken;
    }

    /**
     * Retrieves the title of the algorithm.
     * 
     * @return Title of the algorithm.
     */
    public String getTitle() {
      return title;
    }

    /**
     * Retrieves the time taken for the algorithm.
     * 
     * @return Time taken in seconds.
     */
    public double getTimeTaken() {
      return timeTaken;
    }
  }
}
