package com.extrc.view.console.components;

import com.extrc.common.structures.ReasonerTimer;
import com.extrc.common.structures.ReasonerTimer.TimerNode;

import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

public class TimingView {
  private final ReasonerTimer timer;

  public TimingView(ReasonerTimer timer) {
    this.timer = timer;
  }

  @Override
  public String toString() {
    AsciiTable times = new AsciiTable();
    times.addRule();
    int[] maxLengths = new int[] { -1, -1 };

    for (TimerNode node : timer) {
      String timeTaken = String.format("%.6f seconds", node.getTimeTaken());
      maxLengths[1] = maxLengths[1] >= timeTaken.length() ? maxLengths[1] : timeTaken.length() + 2;
      maxLengths[0] = maxLengths[0] >= node.getTitle().length() ? maxLengths[0] : node.getTitle().length() + 2;
      times.addRow(node.getTitle(), timeTaken);
      times.addRule();
    }
    if (timer.isEmpty()) {
      maxLengths[0] = 3;
      maxLengths[1] = 10;
      times.addRow("", "");
      times.addRule();
    }
    times.setPaddingLeft(1);
    times.setPaddingRight(1);
    times.getRenderer().setCWC(new CWC_LongestWordMin(maxLengths));
    times.setTextAlignment(TextAlignment.LEFT);
    return times.render();
  }
}
