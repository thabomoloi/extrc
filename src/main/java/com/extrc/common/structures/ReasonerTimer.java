package com.extrc.common.structures;

import java.util.ArrayList;

import com.extrc.common.structures.ReasonerTimer.TimerNode;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

public class ReasonerTimer extends ArrayList<TimerNode> {
  public class TimerNode {
    public final String title;
    public final double timeTaken;

    public TimerNode(String title, double timeTaken) {
      this.title = title;
      this.timeTaken = timeTaken;
    }
  }

  private String title = "";
  private long start_time = 0;
  private long end_time = 0;

  public void start(String title) {
    this.title = title;
    start_time = System.nanoTime();
  }

  public void end() {
    end_time = System.nanoTime();
    add(new TimerNode(title, (end_time - start_time) / 1_000_000_000.0));
  }

  @Override
  public String toString() {
    AsciiTable times = new AsciiTable();
    times.addRule();
    int[] maxLengths = new int[] { -1, -1 };

    for (TimerNode node : this) {
      String timeTaken = String.format("%.6f seconds", node.timeTaken);
      maxLengths[1] = maxLengths[1] >= timeTaken.length() ? maxLengths[1] : timeTaken.length() + 2;
      maxLengths[0] = maxLengths[0] >= node.title.length() ? maxLengths[0] : node.title.length() + 2;
      times.addRow(node.title, timeTaken);
      times.addRule();
    }
    if (this.isEmpty()) {
      maxLengths[0] = 3;
      maxLengths[1] = 10;
      times.addRow("", "");
      times.addRule();
    }
    times.getRenderer().setCWC(new CWC_LongestWordMin(maxLengths));
    times.setTextAlignment(TextAlignment.LEFT);
    return times.render();

  }
}
