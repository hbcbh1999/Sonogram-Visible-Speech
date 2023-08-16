package de.dfki.sonogram;

import de.dfki.maths.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>The LPC window shows the Linear Predictive Coding curve in a window.
 *
 * @author Christoph Lauer
 * @version 1.0, Begin 21/05/2001, Current 23/10/2002
 */
public class LinearPredictionView extends JFrame {
  Sonogram refToMain;
  public int len;

  // -------------------------------------------------------------------------------------------------------------------------
  /** Constructor */
  public LinearPredictionView(Sonogram sono) {
    refToMain = sono;
    setTitle("Linear Prediction View");
    setSize(1000, 400);
    setLocation(10, 10);
    Toolkit tk = Toolkit.getDefaultToolkit();
    setIconImage(tk.getImage(Sonogram.class.getResource("Sonogram.gif")));
    cvPanel cv = new cvPanel(); // Panel for Winfunktion
    getContentPane().add(cv);
    len = 500;
  }
  // -------------------------------------------------------------------------------------------------------------------------
  public void update() {
    if (isVisible() == true && refToMain.openingflag == false && refToMain.spektrumExist == true) {
      repaint();
    }
  }
  // -------------------------------------------------------------------------------------------------------------------------
  // =========================================================================================================================
  /** Inner Panel class to paint Windowfunktion */
  class cvPanel extends JPanel {
    // -------------------------------------------------------------------------------------------------------------------------
    public void paintComponent(Graphics gr) {
      Graphics2D g = (Graphics2D) gr;
      if (refToMain.gad.cantialise.isSelected() == true)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      // Get Sliderpositions
      int slfftnum = refToMain.gad.sliderlpcfftnum.getValue();
      int slcoef = refToMain.gad.sliderlpccoef.getValue();
      int slsamfut = refToMain.gad.sliderlpcsamfutur.getValue();
      len = slsamfut;
      // Init
      Dimension size = getSize(); // Windowsize
      short xm = (short) size.getWidth();
      short ym = (short) size.getHeight();
      short yt = (short) 20;
      float[] buffer = new float[len];
      int startp = 0;
      int speclen = 0;
      int slfre = refToMain.gad.sliderlpcfre.getValue();
      int z = 0;
      int x = 0;
      int y = 0;
      int xa = 0;
      int ya = 0;
      int nmax = 2;
      int maxFrequPos = 0;
      double temp = 0.0;
      double round = 0.0;
      double fakt = 0.0;
      double diff = 0.0;
      float peak = 0.0f;
      Color coldv = new Color(30, 60, 30);
      Color colred = new Color(255, 0, 0);
      Color collv = new Color(200, 255, 10);
      Color colras = new Color(100, 100, 150);
      Byte tempbyte;
      String str;
      Color[] col = new Color[10];
      col[0] = new Color(255, 0, 0);
      col[1] = new Color(0, 255, 0);
      col[2] = new Color(0, 0, 255);
      col[3] = new Color(255, 128, 0);
      col[4] = new Color(255, 255, 255);
      col[5] = new Color(192, 255, 192);
      col[6] = new Color(255, 255, 0);
      col[7] = new Color(0, 128, 128);
      col[8] = new Color(192, 0, 192);
      col[9] = new Color(128, 128, 128);
      boolean mouseisintimespan = false;

      // Preparation
      slfftnum = (int) Math.pow(2.0, (double) slfftnum);
      if (slfre == 4) slfre = 1;
      else if (slfre == 3) slfre = 2;
      else if (slfre == 2) slfre = 4;
      else if (slfre == 1) slfre = 8;
      ym -= yt;
      // Coords and Grid
      g.setColor(new Color(40, 40, 60));
      g.fillRect(0, 0, xm, ym + yt);
      g.setColor(colred);
      g.drawLine(5, ym - 20, xm - 5, ym - 20);
      g.drawLine(20, 5, 20, ym - 5);
      for (double yl = 5; yl <= (ym - 20); yl += (double) (((double) ym - 20.0) / 10.0)) {
        g.setColor(colred);
        if (z == 0 || z == 5) g.drawLine(5, (int) yl, 20, (int) yl);
        g.drawLine(12, (int) yl, 20, (int) yl);
        g.setColor(colras);
        g.drawLine(21, (int) yl, xm - 5, (int) yl);
        z++;
      }
      g.setColor(colred);
      g.setFont(new Font("Courier", 0, 10));
      g.drawString("Frequency(kHz)", xm - 100, ym - 2);
      for (int i = 0; i <= 10; i++) {
        temp = (double) i * ((double) refToMain.samplerate / 20000.0) / (double) slfre;
        round = (double) ((int) (temp * 100)) / 100.0;
        str = String.valueOf(round);
        x = (int) ((double) i / 10.0 * (double) (xm - 25.0));
        g.setColor(colred);
        g.drawString(str, x, ym - 11);
        g.setColor(colras);
        x = (int) ((double) i / 10.0 * (double) (xm - 25.0)) + 21;
        g.drawLine(x, ym - 21, x, 5);
      }
      // Copy Timelinearray to buffer
      double start =
          refToMain.selectedstartold + (double) refToMain.pp.wnf * refToMain.selecedwidthold;
      startp = (int) (start * (double) refToMain.samplesall);
      if (startp > (refToMain.samplesall - len)) {
        startp = refToMain.samplesall - len;
        mouseisintimespan = true;
      }
      for (int i = 0; i < len; i++) {
        tempbyte = (Byte) refToMain.reader.audioStream.get(startp + i);
        buffer[i] = tempbyte.floatValue();
      }
      // Transformate
      LinearPredictionTransform lpct = new LinearPredictionTransform();
      float[] lpcspec = lpct.doLinearPredictionTransform(buffer, slfftnum, slcoef, null);
      speclen = lpcspec.length;
      // Search for Peak and Logatithmize
      for (int i = 0; i < speclen; i++) {
        if (refToMain.gad.cllog.isSelected() == true)
          lpcspec[i] = (float) Math.log(lpcspec[i] * 100 + 1.0);
        if (peak < lpcspec[i]) {
          peak = lpcspec[i];
          maxFrequPos = i;
        }
      }
      // Do FrequencyFaction
      buffer = new float[speclen];
      for (int i = 0; i < speclen; i++) buffer[i] = lpcspec[i];
      for (int i = 0; i < speclen; i++) lpcspec[i] = buffer[i / slfre];
      // Paint on Window
      fakt = (double) (xm - 25) / (double) (speclen);
      diff = (double) (speclen) / (double) (xm - 25);
      g.setStroke(new BasicStroke(3));
      g.setColor(new Color(80, 80, 90));
      for (double f = diff; f < (float) (speclen); f += diff) {
        y = (int) (lpcspec[(int) f] / peak * (ym - 25));
        ya = (int) (lpcspec[(int) (f - diff)] / peak * (ym - 25));
        x = (int) (f * fakt);
        xa = (int) ((f - diff) * fakt);
        g.drawLine(xa + 21, ym - 21 - ya, x + 21, ym - 21 - y);
      }
      g.setStroke(new BasicStroke(1));
      g.setColor(new Color(180, 180, 220));
      for (double f = diff; f < (float) (speclen); f += diff) {
        y = (int) (lpcspec[(int) f] / peak * (ym - 25));
        ya = (int) (lpcspec[(int) (f - diff)] / peak * (ym - 25));
        x = (int) (f * fakt);
        xa = (int) ((f - diff) * fakt);
        g.drawLine(xa + 21, ym - 21 - ya, x + 21, ym - 21 - y);
      }
      // Formantpoints
      if (refToMain.gad.clfor.isSelected()) {
        int[] lm = new int[speclen]; // Store for local maxima , 0=not tested,1=Beside, <1 local Max
        for (int i = slfre; i < (speclen - slfre); i++) // Loop over complette Spectrum
        if ((lpcspec[i] > lpcspec[i - slfre]) && (lpcspec[i] > lpcspec[i + slfre])) {
            lm[i] = nmax++; // then is local Maxima
          }
        // Draw the Formats
        for (int f = 0; f < (speclen); f++) { // draw Formants
          if ((lm[f] > 1) && (lm[f] < 12)) { // onely first ten Formants are drawed
            x = (int) (f * fakt) + 21;
            y = ym - 21 - (int) (lpcspec[(int) f] / peak * (ym - 25));
            g.setColor(col[lm[(int) f] - 2]);
            g.drawLine(x, y - 10, x, y + 10);
            g.drawLine(x - 10, y, x + 10, y);
            temp = (double) f / (double) (speclen) * ((double) refToMain.samplerate / 2.0) / slfre;
            str = String.valueOf((int) temp);
            x = (int) ((double) (lm[(int) f] - 2) / 11 * (double) (xm - 25)) + 30;
            g.drawString(str, x, ym - 5 + yt);
          }
        }
      }
      // Mouse in time Span
      if (mouseisintimespan == true) {
        g.setFont(new Font("Dialog", 0, 9));
        g.setColor(colred);
        g.drawString("Mouse in time span", xm - 110, 15);
      }
    }
    // -------------------------------------------------------------------------------------------------------------------------
  }
  // =========================================================================================================================
}
