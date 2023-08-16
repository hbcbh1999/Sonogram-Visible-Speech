package de.dfki.sonogram;

import de.dfki.maths.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>Wavelet window which uses the wavelet transformation.
 *
 * @author Christoph Lauer
 * @version 1.0, Current 26/09/2002
 */
public class WaveletView extends JFrame {
  /** Wavelet View View for Sonogram */
  Sonogram refToMain;

  public int len = 0;
  int lenold;
  Vector waveletspectrumold;
  double startold;

  // -------------------------------------------------------------------------------------------------------------------------
  public WaveletView(Sonogram sono) {
    /**
     * Construktor for WaveletView. This Class extendet from JFrame shows cepstral analyse curve in
     * his window.
     */
    refToMain = sono;
    setTitle("WaveletView View");
    setSize(1000, 400);
    setLocation(10, 10);
    Toolkit tk = Toolkit.getDefaultToolkit();
    setIconImage(tk.getImage(Sonogram.class.getResource("Sonogram.gif")));
    wvPanel wv = new wvPanel(); // Panel for Winfunktion
    getContentPane().add(wv);
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
  class wvPanel extends JPanel {
    // -------------------------------------------------------------------------------------------------------------------------
    public void paintComponent(Graphics gra) {
      Graphics2D g = (Graphics2D) gra;
      if (refToMain.gad.cantialise.isSelected() == true)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      boolean mouseisintimespan = false;
      Byte tempbyte;
      Dimension size = getSize(); // Windowsize
      int xm = (short) size.getWidth() - 30;
      int ym = (short) size.getHeight() - 30;
      int startp = 0;
      float[] buffer;
      float[] dwtvector;
      Color colred = new Color(255, 0, 0);
      Vector waveletspectrum;
      float[] vector;
      double start =
          refToMain.selectedstartold + (double) refToMain.pp.wnf * refToMain.selecedwidthold;
      // Copy Timelinearray to buffer
      len = refToMain.gad.walwindowlength;
      // Calculate only when values have changed
      if (len != lenold || startold != start) {
        buffer = new float[len];
        startp = (int) (start * (double) refToMain.samplesall);
        if (startp > (refToMain.samplesall - len)) {
          startp = refToMain.samplesall - len;
          mouseisintimespan = true;
        }
        for (int i = 0; i < len; i++) {
          tempbyte = (Byte) refToMain.reader.audioStream.get(startp + i);
          buffer[i] = tempbyte.floatValue();
        }
        dwtvector = new float[len / 2];
        DWT dwt =
            new DWT(len - 1, refToMain.gad.wavelets.getSet(refToMain.gad.wcb.getSelectedIndex()));
        dwt.discreteWT(buffer, dwtvector);
        // Swarch for Peak and normalize the vector
        float peak = 0.0f;
        for (int count = 0; count < len / 2; count++) {
          if (Math.abs(dwtvector[count]) > peak) peak = Math.abs(dwtvector[count]);
        }
        for (int count = 0; count < len / 2; count++) {
          if (refToMain.gad.clogwal.isSelected() == true)
            dwtvector[count] =
                (float) Math.log(1.0 + Math.abs(dwtvector[count] * 100))
                    / (float) Math.log(1.0 + peak * 100)
                    * 255.0f;
          else dwtvector[count] = Math.abs(dwtvector[count]) / peak * 255.0f;
        }
        // Split into octave vectors
        int counter = 0;
        int length;
        waveletspectrum = new Vector();
        for (int octave = 0; octave < refToMain.gad.sliderwaltl.getValue() - 1; octave++) {
          length = (int) Math.pow(2.0, (double) octave);
          vector = new float[length];
          for (int time = 1; time <= length; time++) {
            vector[time - 1] = dwtvector[counter];
            counter++;
          }
          waveletspectrum.add(vector);
        }
        lenold = len;
        waveletspectrumold = waveletspectrum;
        startold = start;
      } else {
        // Take the old values
        waveletspectrum = waveletspectrumold;
        len = lenold;
      }
      // Draw spectrum on Screen
      g.setColor(new Color(15, 50, 20));
      g.fillRect(0, 0, xm + 30, ym + 30);
      float diffy = (float) ym / (float) waveletspectrum.size();
      float diffx;
      int x1, x2, y1, y2;
      int r, gr, b;
      for (int octave = 0; octave < waveletspectrum.size(); octave++) {
        vector = (float[]) waveletspectrum.get(waveletspectrum.size() - octave - 1);
        diffx = (float) xm / (float) vector.length;
        for (int time = 0; time < vector.length; time++) {
          x1 = (int) ((float) (time + 0) * diffx) + 20;
          x2 = (int) ((float) (time + 1) * diffx) + 20;
          y1 = (int) ((float) (octave + 0) * diffy) + 10;
          y2 = (int) ((float) (octave + 1) * diffy) + 10;
          refToMain.pp.selectColor(g, (int) vector[time]);
          g.fillRect(x1, y1, x2, y2);
        }
      }
      g.setColor(new Color(15, 50, 20));
      g.fillRect(0, 0, xm + 30, 10);
      g.fillRect(xm + 20, 0, xm + 30, ym + 30);
      g.fillRect(0, ym + 10, xm + 30, ym + 10);
      g.setColor(new Color(100, 80, 90));
      g.drawLine(0, 10, 19, 10);
      g.drawLine(xm + 20, 10, xm + 30, 10);
      g.setFont(new Font("Dialog", 0, 9));
      for (int count = 0; count < waveletspectrum.size(); count++) {
        g.drawString(Integer.valueOf(count + 1).toString(), 0, (int) (ym - (count * diffy)));
        g.drawLine(0, 9 + (int) (ym - (count * diffy)), 19, 9 + (int) (ym - (count * diffy)));
        g.drawLine(
            xm + 20, 9 + (int) (ym - (count * diffy)), xm + 30, 9 + (int) (ym - (count * diffy)));
      }
      double timespan = (double) len / (double) refToMain.samplerate;
      double temp;
      for (int count = 0; count <= 4; count++) {
        x1 = 20 + (int) ((float) xm / 4.0f * count);
        g.drawLine(x1, 0, x1, 9);
        g.drawLine(x1, ym + 10, x1, ym + 30);
        temp = Math.round(timespan / 4.0 * (double) count * 10000.0);
        g.drawString(temp / 10 + "ms", x1 - 18, ym + 20);
      }
      // Mouse in time Span
      if (mouseisintimespan == true) {
        g.setFont(new Font("Dialog", 0, 9));
        g.setColor(colred);
        g.drawString("Mouse in time span", xm - 70, 20);
      }
    }
    // -------------------------------------------------------------------------------------------------------------------------
  }
  // =========================================================================================================================
}
