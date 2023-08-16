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
 * <p>Formant View is an Window that shows the Fourier Transformed view.
 *
 * @author Christoph Lauer
 * @version 1.0, Begin 07/04/2001 12:07:40, Current 26/09/2002
 */
public class FormatView extends JFrame {
  Sonogram refToMain;
  public int len = 1024; // Calback from GAD

  // -------------------------------------------------------------------------------------------------------------------------
  public FormatView(Sonogram sono) {
    refToMain = sono;
    setTitle("FFT Analyzer View");
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
  // =========================================================================================================================
  /** Inner Panel class to paint Windowfunktion */
  class cvPanel extends JPanel {
    // -------------------------------------------------------------------------------------------------------------------------
    public void paintComponent(Graphics gr) {
      Graphics2D g = (Graphics2D) gr;
      if (refToMain.gad.cantialise.isSelected() == true)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      Color coldv = new Color(15, 50, 20);
      Color colred = new Color(255, 0, 0);
      Color collv = new Color(200, 255, 10);
      Color colras = new Color(100, 80, 90);
      Dimension size = getSize(); // Windowsize
      short yt = (short) 25; // Heigth for under Text
      short xm = (short) size.getWidth();
      short ym = (short) size.getHeight();
      float[] buffer;
      float[] spectrum;
      float peak = 0.0f;
      double diff = 0.0;
      double fakt = 0.0;
      double round = 0.0;
      double temp = 0.0;
      String str;
      int x, xa;
      int y, ya;
      int maxFrequPos = 0;
      int msum = 0;
      int nmax = 3;
      int z = 0;
      int startp = 0;
      int slfre = refToMain.gad.sliderformant.getValue();
      Byte tempbyte;
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

      // Prepare SliderFrequencyFraction
      if (slfre == 4) slfre = 1;
      else if (slfre == 3) slfre = 2;
      else if (slfre == 2) slfre = 4;
      else if (slfre == 1) slfre = 8;

      ym -= yt;
      // Coords and Grid
      g.setColor(coldv);
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
        round = (double) ((int) (temp * 10)) / 10.0;
        str = String.valueOf(round);
        x = (int) ((double) i / 10.0 * (double) (xm - 25.0));
        g.setColor(colred);
        g.drawString(str, x, ym - 11);
        g.setColor(colras);
        x = (int) ((double) i / 10.0 * (double) (xm - 25.0)) + 21;
        g.drawLine(x, ym - 21, x, 5);
      }

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
      FastFourierTransform ft = new FastFourierTransform();
      spectrum = ft.doFFT(buffer);

      // smooth the result vector
      float[] out = new float[len / 2];
      int range = refToMain.gad.slidersmoothfft.getValue();
      // then smooth over the elements
      for (int i = 0; i < len / 2; i++) // this is the main loop over the whole vector
      {
        out[i] = 0;
        int sumCount = 0;
        for (int j = -range; j < range + 1; j++) // this is the averaging loop over the range
        {
          int vectorPosition = i + j;
          if (vectorPosition < 0) // catch the border runaways lower than zero
          continue;
          if (vectorPosition >= len / 2) // catch the border runaways greater than length
          continue;
          out[i] += spectrum[vectorPosition];
          sumCount++;
        }
        out[i] /= sumCount;
      }

      // finally give the result back
      spectrum = out;

      // Do FrequencyFaction
      for (int i = 0; i < len / 2; i++) buffer[i] = spectrum[i];
      for (int i = 0; i < len / 2; i++) spectrum[i] = buffer[i / slfre];
      // Search for Peak

      for (int i = 0; i < len / 2; i++) {
        spectrum[i] = (float) Math.log(spectrum[i] + 1.0f);
        if (peak < spectrum[i]) {
          peak = spectrum[i];
          maxFrequPos = i;
        }
      }

      // Paint Spectrum
      fakt = (double) (xm - 25) / (double) (len / 2);
      diff = ((double) len / 2.0 / (double) (xm - 25) / 2.0);
      g.setStroke(new BasicStroke(3));
      g.setColor(new Color(50, 100, 40));
      for (double f = diff; f < (float) (len / 2); f += diff) {
        y = (int) (spectrum[(int) f] / peak * (ym - 25));
        ya = (int) (spectrum[(int) (f - diff)] / peak * (ym - 25));
        x = (int) (f * fakt);
        xa = (int) ((f - diff) * fakt);
        g.drawLine(xa + 21, ym - 21 - ya, x + 21, ym - 21 - y);
      }
      g.setStroke(new BasicStroke(1));
      g.setColor(new Color(220, 255, 40));
      for (double f = diff; f < (float) (len / 2); f += diff) {
        y = (int) (spectrum[(int) f] / peak * (ym - 25));
        ya = (int) (spectrum[(int) (f - diff)] / peak * (ym - 25));
        x = (int) (f * fakt);
        xa = (int) ((f - diff) * fakt);
        g.drawLine(xa + 21, ym - 21 - ya, x + 21, ym - 21 - y);
      }
      // Search for Formants
      int[] lm = new int[len / 2]; // Store for locam maxima

      for (int i = 0; i < len / 2; i++) {
        spectrum[i] = spectrum[i] / peak; // Normalize to 0..1
        lm[i] = 0;
      }
      for (int i = 2; i < (len / 2 - 2); i++) { // Smooth out
        spectrum[i] =
            (spectrum[i] + spectrum[i + 1] + spectrum[i - 1] + spectrum[i + 2] + spectrum[i - 2])
                / 5.0f;
      }

      lm[maxFrequPos] = 2; // Set Local Maxima-Store on peak to 1
      for (double a = 1.0;
          (nmax < 12 && a > 0.05);
          a -= 0.01) { // Going from up to down in Amplitude
        for (int i = 2; i < (len / 2 - 2); i++) { // Loop over complette Spectrum
          if ((spectrum[i] > a) && lm[i] == 0 && lm[i - 1] == 0 && lm[i + 1] == 0) {
            lm[i] = nmax; // then is local Maxima
            nmax++;
          }
          if ((spectrum[i] > a) && lm[i] == 0 && (lm[i - 1] != 0 || lm[i + 1] != 0)) {
            lm[i] = 1; // then is beside Maximum i
          }
        }
      }

      for (int f = 0; f < (len / 2); f++) { // draw Formants
        if ((lm[f] > 1) && (lm[f] < 12)) { // onely first ten Formants are drawed
          g.setColor(col[lm[f] - 2]);
          g.setColor(col[lm[f] - 2]);
          g.setColor(col[lm[f] - 2]);
          x = (int) ((double) f * fakt);
          g.drawLine(x + 21, ym - 20 + yt, x + 21, ym - 32 + yt);

          temp =
              (double) f
                  / (double) (len / 2)
                  * ((double) refToMain.samplerate / 2.0)
                  / (double) slfre;
          str = String.valueOf((int) temp);
          x = (int) ((double) (lm[(int) f] - 2) / 11 * (double) (xm - 25)) + 30;
          x = (int) ((double) (lm[(int) f] - 2) / 11 * (double) (xm - 25)) + 30;
          g.drawString(str, x, ym - 5 + yt);
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
