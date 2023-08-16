/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>This is my own splash progress bar user interface implementation.
 *
 * @author Christoph Lauer
 * @version 1.0, Current 26/09/2002
 */
package de.dfki.sonogram;

import java.awt.*;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class ProgressBarUI extends BasicProgressBarUI {
  LinearGradientPaint gp1;
  LinearGradientPaint gp2;

  /**
   * Construktor for my own implementation of the ProgressBarUI smallerborders - true for smaller
   * borders called for the SonoProgressBar, Splash Screen uses the larger version.
   */
  public ProgressBarUI(int height) {
    super();

    Color[] colors1 = {new Color(195, 195, 208), new Color(131, 131, 148)};
    float[] dist = {0.0f, 1.0f};
    gp1 = new LinearGradientPaint(0f, 0f, 0f, (float) height, dist, colors1);
    Color[] colors2 = {new Color(95, 95, 108), new Color(31, 31, 48)};
    gp2 = new LinearGradientPaint(0f, 0f, 0f, (float) height, dist, colors2);
  }
  /**
   * Updates the prgressbar
   *
   * @param g - The Graphics for painting.
   * @param c - JComponent, the component to paint (no use in this function).
   */
  public void paint(Graphics g, JComponent c) {
    // INIT
    Graphics2D g2 = (Graphics2D) g;
    Insets insets = super.progressBar.getInsets();
    int i = insets.left;
    int j = insets.top;
    int k = super.progressBar.getWidth() - (insets.right + i);
    int l = super.progressBar.getHeight() - (insets.bottom + j);
    int i1 = getAmountFull(insets, k, l);
    JProgressBar prog = (JProgressBar) c;

    // BACKGROUND
    g2.setPaint(gp1);
    g2.fillRect(i1, 0, prog.getWidth(), prog.getHeight());

    // PROGRESS
    g2.setPaint(gp2);
    g2.fillRect(0, 0, i1, prog.getHeight());

    // TEXT
    paintString(g2, i, j - 2, k, l + 6, i1, insets);
  }
}
