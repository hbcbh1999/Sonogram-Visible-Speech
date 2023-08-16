package de.dfki.sonogram;

import de.dfki.maths.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>The AutoCorrealtionViewer shows the Autocorrelation transformed curve in a window.
 *
 * @author Christoph Lauer
 * @version 1.0, Begin 14/10/2002
 */
public class AutoCorrelationView extends JFrame {
  private Sonogram refToMain;
  double ptime = 0.0; // Global for the info Dialog
  double pfrequency = 0.0; // Global for the info Dialog
  double windowshift = 0; // Global for the info Dialog
  double windowlength = 0; // Global for the info Dialog

  public AutoCorrelationView(Sonogram sono) {
    refToMain = sono;
    setTitle("Autocorrelation View");
    setSize(1000, 400);
    setLocation(10, 10);
    Toolkit tk = Toolkit.getDefaultToolkit();
    setIconImage(tk.getImage(Sonogram.class.getResource("Sonogram.gif")));
    AutoCorrelationPanel ap = new AutoCorrelationPanel();
    getContentPane().add(ap);
  }

  public void update() {
    if (isVisible() == true && refToMain.openingflag == false && refToMain.spektrumExist == true) {
      repaint();
    }
  }
  /** Inner Panel CLass for the AutoCorrelationView. */
  class AutoCorrelationPanel extends JPanel {
    /**
     * Overwritten function for the painting if the component.
     *
     * @param gr the pen.
     */
    public void paintComponent(Graphics gr) {
      Graphics2D g = (Graphics2D) gr;
      if (refToMain.gad.cantialise.isSelected() == true)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      Dimension size = getSize(); // Windowsize
      int xm = (short) size.getWidth();
      int ym = (short) size.getHeight();
      int y, x, startp, winlenall, xa, ya, winlensamples, winshiftsamples;
      int maxfrequenspos = 0;
      String str;
      double tmp, fakt, diff;
      boolean delinearise, mouseisintimespan = false;
      Byte tempbyte;
      // Get setted values
      int sliderwl = refToMain.gad.slideracwinlength.getValue();
      if (sliderwl == 0) windowlength = 10;
      else if (sliderwl == 1) windowlength = 25;
      else if (sliderwl == 2) windowlength = 50;
      else if (sliderwl == 3) windowlength = 100;
      else if (sliderwl == 4) windowlength = 250;
      else if (sliderwl == 5) windowlength = 500;
      else if (sliderwl == 6) windowlength = 1000;
      else if (sliderwl == 7) windowlength = 2500;
      else windowlength = 5000;
      winlensamples = (int) ((double) windowlength / 1000.0 * (double) refToMain.samplerate);
      int sliderws = refToMain.gad.slideracwinshift.getValue();
      if (sliderws == 0) windowshift = 2.5;
      else if (sliderws == 1) windowshift = 5;
      else if (sliderws == 2) windowshift = 10;
      else if (sliderws == 3) windowshift = 25;
      else if (sliderws == 4) windowshift = 50;
      else if (sliderws == 5) windowshift = 100;
      else if (sliderws == 6) windowshift = 250;
      else if (sliderws == 7) windowshift = 500;
      else windowshift = 1000;
      winshiftsamples = (int) ((double) windowshift / 1000.0 * (double) refToMain.samplerate);
      winlenall = winlensamples + winshiftsamples;
      // Coords and Grid and background
      g.setColor(new Color(15, 50, 20));
      g.fillRect(0, 0, xm + 10, ym);
      g.setColor(new Color(100, 80, 90));
      // Grid over amplitude
      for (double a = 0.0; a <= 1.0; a += 0.1) {
        y = (int) (a * ((double) ym - 30.0) + 15.0);
        g.drawLine(21, y, xm - 4, y);
      }
      g.setFont(new Font("Dialog", 0, 9));
      // Grid over time
      for (double a = 0.0; a <= 1.0; a += 0.2) {
        x = 21 + (int) (a * ((double) xm - 25.0));
        g.drawLine(x, 15, x, ym - 16);
        tmp = Math.round(a * windowlength);
        str = tmp + "ms";
        g.drawString(str, x - 15, ym - 5);
      }
      // middle line
      g.setColor(new Color(255, 0, 0));
      g.drawLine(15, ym / 2, xm - 5, ym / 2);
      // Copy Timelinearray to buffer
      float[] buffer = new float[winlenall];
      double start =
          refToMain.selectedstartold + (double) refToMain.pp.wnf * refToMain.selecedwidthold;
      startp = (int) (start * (double) refToMain.samplesall);
      if (startp > (refToMain.samplesall - (winlenall))) {
        startp = refToMain.samplesall - (winlenall);
        mouseisintimespan = true;
      }
      for (int i = 0; i < winlenall; i++) {
        tempbyte = (Byte) refToMain.reader.audioStream.get(startp + i);
        buffer[i] = tempbyte.floatValue();
      }
      // Call the Autocorrelation function
      float[] acor =
          de.dfki.maths.AutoCorrelation.autoCorrelate(buffer, winlensamples, winshiftsamples);
      // Smooth the autocorrelation
      if (refToMain.gad.cacsmooth.isSelected() == true)
        acor = de.dfki.maths.VectorSmoother.smoothWithDegreeThree(acor);
      // Find the min and max peaks
      float maxpeak = 0.0f;
      float minpeak = 0.0f;
      float peak = 0.0f;
      for (int i = 0; i < acor.length; i++) {
        if (maxpeak < acor[i]) {
          maxpeak = acor[i];
          maxfrequenspos = i;
        }
        if (minpeak > acor[i]) minpeak = acor[i];
      }
      if (-minpeak > maxpeak) peak = -minpeak;
      else peak = maxpeak;
      // Draw the correlation on the screen
      fakt = (double) (xm - 25) / (double) (winlensamples);
      diff = ((double) winlensamples / (double) (xm - 25));
      g.setStroke(new BasicStroke(3));
      g.setColor(new Color(50, 100, 40));
      for (double f = diff; f < (float) (winlensamples); f += diff) {
        y = (int) (acor[(int) f] / peak * (((float) ym - 15.0f) / 2.0));
        ya = (int) (acor[(int) (f - diff)] / peak * (((float) ym - 15.0f) / 2.0));
        x = (int) (f * fakt);
        xa = (int) ((f - diff) * fakt);
        if (refToMain.gad.cacpoints.isSelected() == true)
          g.drawLine(xa + 21, ym / 2 - ya, x + 21, ym / 2 - y);
        else g.drawLine(xa + 21, ym / 2 - y, x + 21, ym / 2 - y);
      }
      g.setStroke(new BasicStroke(1));
      g.setColor(new Color(200, 255, 10));
      for (double f = diff; f < (float) (winlensamples); f += diff) {
        y = (int) (acor[(int) f] / peak * (((float) ym - 15.0f) / 2.0));
        ya = (int) (acor[(int) (f - diff)] / peak * (((float) ym - 15.0f) / 2.0));
        x = (int) (f * fakt);
        xa = (int) ((f - diff) * fakt);
        if (refToMain.gad.cacpoints.isSelected() == true)
          g.drawLine(xa + 21, ym / 2 - ya, x + 21, ym / 2 - y);
        else g.drawLine(xa + 21, ym / 2 - y, x + 21, ym / 2 - y);
      }
      // Draw status line
      g.setColor(new Color(100, 80, 90));
      tmp =
          ((double) refToMain.selectedstartold
                  + (double) refToMain.pp.wnf * (double) refToMain.selecedwidthold)
              * (double) refToMain.samplesall
              / (double) refToMain.samplerate;
      tmp = Math.round(tmp * 1000.0) / 1000.0;
      str = "Begin time = " + tmp + " Seconds";
      g.drawString(str, 20, 12);
      str = "Window Loop Shift = " + windowshift + " Seconds";
      g.drawString(str, 180, 12);
      g.setColor(new Color(255, 0, 0));
      if (mouseisintimespan == true) g.drawString("Mouse in time span", xm - 110, 15);
      ptime = 0.0; // For the info dialog
      pfrequency = 0.0; // For the info dialog
      // Search the Periodic Peaks in the Signal and mark the highest
      if (refToMain.gad.cacpitch.isSelected() == true) {
        de.dfki.maths.PeakSearcher peaksearcher =
            new de.dfki.maths.PeakSearcher(acor, 0.2f * peak, 5, 0);
        int[] lm = peaksearcher.getLoacMaximas();
        lm[peaksearcher.getHighestPeakPosition()] =
            lm[peaksearcher.getSecondHighestPeakPosition()] = 999999; // Mark the peakpositions
        if (peaksearcher.getHighestPeakPosition() != 0
            || peaksearcher.getSecondHighestPeakPosition() != 0) {
          // Draw the Periodic Peaks on the Screen with the two top peaks
          int toppeakx1 = 0;
          int toppeaky1 = 0;
          int toppeakx2 = 0;
          int toppeaky2 = 0;
          int toppeakx1samples = 0; // For the pitch calculation
          int toppeakx2samples = 0;
          g.setColor(new Color(150, 0, 0));
          for (int i = 0; i < winlensamples; i++) {
            if (lm[i] < 2) continue;
            y = (int) (acor[i] / peak * (((float) ym - 15.0f) / 2.0));
            x = (int) (i * fakt);
            if (lm[i] == 999999) {
              g.setColor(new Color(255, 0, 0));
              g.fillOval(x + 21 - 4, ym / 2 - y - 4, 8, 8);
              g.setColor(new Color(150, 0, 0));
              if (toppeaky1 == 0) {
                toppeakx1 = x + 21;
                toppeaky1 = ym / 2 - y;
                toppeakx1samples = i;
              } else {
                toppeakx2 = x + 21;
                toppeaky2 = ym / 2 - y;
                toppeakx2samples = i;
              }
            } else g.fillOval(x + 21 - 3, ym / 2 - y - 3, 6, 6);
          }
          g.setColor(new Color(255, 0, 0));
          g.drawLine(toppeakx1, toppeaky1, toppeakx2, toppeaky2);
          // Call the tooltip
          int tooltipx = (toppeakx2 + toppeakx1) / 2;
          int tooltipy = (toppeaky2 + toppeaky1) / 2;
          ptime =
              Math.round(
                      (Math.abs(toppeakx1samples - toppeakx2samples)
                              / (double) refToMain.samplerate)
                          * 10000.0)
                  / 10.0;
          pfrequency = Math.round(1.0 / (ptime / 1000.0) * 10) / 10;
          String tooltiptext = "PITCH: T=" + ptime + "ms; f=" + pfrequency + "Hz";
          paintToopTip(g, tooltipx, tooltipy, xm, tooltiptext, false);
        }
        // If no pitch is found
        else {
          paintToopTip(g, 0, 0, 0, "", true);
        }
        refToMain.infod.update();
      }
    }
    /**
     * Plots a tool tip with the given String and the postion on the screen. The tooltip wil be
     * painted under the coordinates given by function call.
     *
     * @param g - the pen.
     * @param posX - coordinate X of the triangle of the baloon.
     * @param posY - coordinate Y of the triangle of the baloon.
     * @param maxX - The maximum width of the window is needed to set the tooltip on the regth
     *     place.
     * @param text - the text of to be written in the tooltip.
     * @param defaulttooltip - if the default flag is enabled all other values are ignored and the
     *     default tooltip "No Pitch found" will be displayed on the predefined position.
     */
    private void paintToopTip(
        Graphics2D g, int posX, int posY, int maxX, String text, boolean defaulttooltip) {
      AlphaComposite alphacomposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
      g.setComposite(alphacomposite);
      int y1 = 25; // From top to tooltip begin.
      int y2 = 15; // Tooltip height.
      int x1 = 40; // From left to begin triangle.
      int x2 = 8; // Triangle width.
      int x3 = 90; // From the triangle to the right.
      int trix1, triy1, trix2, triy2;
      int recx1, recy1, recx2, recy2, recx3, recy3, recx4, recy4;
      Color fillcolor = new Color(100, 100, 100);
      Color bordercolor = new Color(255, 255, 255);
      Color textcolor = new Color(255, 255, 255);
      // The non default tooltip
      if (defaulttooltip == false) {
        // Callculate the positions of the tooltip corresponds to the given coordinates
        triy1 = triy2 = recy1 = recy2 = posY + y1;
        recy3 = recy4 = posY + y1 + y2;
        recx1 = recx3 = posX - x2 - x1;
        trix1 = posX - x2 / 2;
        trix2 = posX + x2;
        recx2 = recx4 = posX + x3;
        // The rectangle for the text
        g.setColor(fillcolor);
        g.fillRect(recx1, recy1, recx2 - recx1, recy4 - recy2);
        // The Triangle to top
        int[] polyx = {posX, trix1, trix2};
        int[] polyy = {posY, triy1, triy2};
        g.fillPolygon(polyx, polyy, 3);
        // The Border
        g.setColor(bordercolor);
        g.drawLine(posX, posY, trix1, triy1);
        g.drawLine(trix1 - 1, triy1, recx1, recy1);
        g.drawLine(recx1, recy1 + 1, recx3, recy3);
        g.drawLine(recx3 + 1, recy3, recx4, recy4);
        g.drawLine(recx4, recy4 - 1, recx2, recy2);
        g.drawLine(recx2 - 1, recy2, trix2, triy2);
        g.drawLine(trix2, triy2 - 1, posX, posY - 1);
        // The text
        g.setColor(textcolor);
        g.drawString(text, posX - x1 - x2 + 3, posY + y1 + y2 - 3);
      }
      // The default tooltip
      else {
        int x = 30, y = 30, w = 100, h = 15;
        g.setColor(fillcolor);
        g.fillRect(x, y, w, h);
        g.setColor(bordercolor);
        g.drawLine(x, y, x + w, y);
        g.drawLine(x + w, x, x + w, y + h);
        g.drawLine(x + w, y + h, x, y + h);
        g.drawLine(x, y + h, x, y);
        g.setColor(textcolor);
        g.drawString("NO PITCH DETECTED", x + 3, y + 12);
      }
    }
  }
}
