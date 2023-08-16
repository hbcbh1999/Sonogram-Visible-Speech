package de.dfki.sonogram;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>Shows the waveform in a window. It connect simple the samples with lines.
 *
 * @author Christoph Lauer
 * @version 1.0, Current 26/09/2002
 */
public class WaveformView extends JFrame {
  /** Waveform View for Sonogram Thsi Class */
  Sonogram refToMain;

  int len = 0;
  double sec = 0.0;
  // -------------------------------------------------------------------------------------------------------------------------
  public WaveformView(Sonogram sono) {
    /**
     * Construktor for Waveform View. This Class extendet from JFrame shows amplitude in time domain
     */
    refToMain = sono;
    setTitle("Waveform View");
    setSize(1000, 400);
    setLocation(10, 10);
    Toolkit tk = Toolkit.getDefaultToolkit();
    setIconImage(tk.getImage(Sonogram.class.getResource("Sonogram.gif")));
    cvPanel cv = new cvPanel(); // Panel for Winfunktion
    getContentPane().add(cv);
  }
  // -------------------------------------------------------------------------------------------------------------------------
  public void update() {
    if (isVisible() == true && refToMain.openingflag == false && refToMain.spektrumExist == true) {
      repaint();
    }
  }
  // -------------------------------------------------------------------------------------------------------------------------
  public void getLen() {
    /** Callcualte samples length with settings in General adjustment Dialog */
    if (refToMain.gad.sliderwave.getValue() == 1) sec = 0.001;
    if (refToMain.gad.sliderwave.getValue() == 2) sec = 0.0025;
    if (refToMain.gad.sliderwave.getValue() == 3) sec = 0.01;
    if (refToMain.gad.sliderwave.getValue() == 4) sec = 0.025;
    if (refToMain.gad.sliderwave.getValue() == 5) sec = 0.100;
    if (refToMain.gad.sliderwave.getValue() == 6) sec = 0.250;
    if (refToMain.gad.sliderwave.getValue() == 7) sec = 1.000;
    len = (int) (sec * (double) refToMain.samplerate);
  }
  // =========================================================================================================================
  /** Inner Panel class to paint Windowfunktion */
  class cvPanel extends JPanel {
    // -------------------------------------------------------------------------------------------------------------------------
    public void paintComponent(Graphics gr) {
      Graphics2D g = (Graphics2D) gr;
      if (refToMain.gad.cantialise.isSelected() == true)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      float[] buffer;
      Dimension size = getSize(); // Windowsize
      int xm = (short) size.getWidth() - 10;
      int ym = (short) size.getHeight();
      int startp = 0;
      Byte tempbyte;
      double diff = 0.0;
      double fakt = 0.0;
      double tmp;
      int x, xa;
      int y, ya;
      String str;
      boolean mouseisintimespan = false;
      // Coords and Grid
      getLen();
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
        tmp = Math.round(a * sec * 1000);
        str = tmp + "ms";
        g.drawString(str, x - 15, ym - 5);
      }
      // middle line
      g.setColor(new Color(255, 0, 0));
      g.drawLine(15, ym / 2, xm - 5, ym / 2);
      // Copy Timelinearray to buffer
      buffer = new float[len];
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
      // Normalize
      float peak = 0.0f;
      if (refToMain.gad.cwfnorm.isSelected() == true) {
        for (int i = 0; i < len; i++) {
          if (peak < Math.abs(buffer[i])) peak = Math.abs(buffer[i]);
        }
        for (int i = 0; i < len; i++) {
          buffer[i] *= (128.0 / peak);
        }
      }
      // Paint Amplitude on Screen
      fakt = (double) (xm - 25) / (double) (len);
      diff = ((double) len / (double) (xm - 25));
      g.setStroke(new BasicStroke(3));
      g.setColor(new Color(50, 100, 40));
      for (double f = diff; f < (float) (len); f += diff) {
        y = (int) (buffer[(int) f] / 128.0f * (((float) ym - 15.0f) / 2.0));
        ya = (int) (buffer[(int) (f - diff)] / 128.0f * (((float) ym - 15.0f) / 2.0));
        x = (int) (f * fakt);
        xa = (int) ((f - diff) * fakt);
        if (refToMain.gad.cwavelines.isSelected() == true)
          g.drawLine(xa + 21, ym / 2 - ya, x + 21, ym / 2 - y);
        else g.drawLine(xa + 21, ym / 2 - y, x + 21, ym / 2 - y);
      }
      g.setStroke(new BasicStroke(1));
      g.setColor(new Color(200, 255, 10));
      for (double f = diff; f < (float) (len); f += diff) {
        y = (int) (buffer[(int) f] / 128.0f * (((float) ym - 15.0f) / 2.0));
        ya = (int) (buffer[(int) (f - diff)] / 128.0f * (((float) ym - 15.0f) / 2.0));
        x = (int) (f * fakt);
        xa = (int) ((f - diff) * fakt);
        if (refToMain.gad.cwavelines.isSelected() == true)
          g.drawLine(xa + 21, ym / 2 - ya, x + 21, ym / 2 - y);
        else g.drawLine(xa + 21, ym / 2 - y, x + 21, ym / 2 - y);
      }
      // draw over status line
      g.setColor(new Color(100, 80, 90));
      tmp =
          ((double) refToMain.selectedstartold
                  + (double) refToMain.pp.wnf * (double) refToMain.selecedwidthold)
              * (double) refToMain.samplesall
              / (double) refToMain.samplerate;
      tmp = Math.round(tmp * 1000.0) / 1000.0;
      str = "Begin time = " + tmp + " Seconds";
      g.drawString(str, 20, 12);
      str = "Time span = " + sec + " Seconds";
      g.drawString(str, 180, 12);
      g.setColor(new Color(220, 60, 0));
      if (mouseisintimespan == true) g.drawString("Mouse in time span", xm - 110, 15);
      if (refToMain.gad.cwfnorm.isSelected() == true) {
        g.setFont(new Font("Dialog", Font.BOLD, 10));
        g.drawString(
            "NORMALIZED: peak=" + Double.toString(Math.round(peak / 128 * 10000) / 100) + "%",
            25,
            28);
      }
    }
    // -------------------------------------------------------------------------------------------------------------------------
  }
  // =========================================================================================================================
}
