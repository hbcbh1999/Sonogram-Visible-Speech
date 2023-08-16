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
 * <p>The cepstrum view shows the cepstum transformed vector in a curve form. For the theoretical
 * background of the Cepstrum transformation see the Sonogram paper at my homepage.
 *
 * @author Christoph Lauer
 * @version 1.0, Current 26/09/2002
 */
public class CepstrumView extends JFrame {
  Sonogram refToMain;
  public int len = 0;

  /**
   * Construktor for CepstrumView. This Class extendet from JFrame shows cepstral analyse curve in
   * his window.
   */
  public CepstrumView(Sonogram sono) {
    refToMain = sono;
    setTitle("Cepstrum View");
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
      Color coldv = new Color(40, 40, 60);
      Color colred = new Color(255, 0, 0);
      Color collv = new Color(180, 180, 220);
      Color colras = new Color(80, 80, 130);
      Dimension size = getSize(); // Windowsize
      short xm = (short) size.getWidth();
      short ym = (short) size.getHeight();
      int z = 0;
      int startp = 0;
      float[] buffer;
      float[] cepstrum;
      float peak = 0.0f;
      double diff = 0.0;
      double fakt = 0.0;
      double qufr = 0.0;
      String str;
      int x, xa;
      int y, ya;
      Byte tempbyte;
      boolean mouseisintimespan = false;
      // Coords and Grid
      g.setColor(coldv);
      g.fillRect(0, 0, xm, ym);
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
      g.drawString("Quefrenz(ms)", xm - 85, ym - 2);
      for (int i = 0; i <= 10; i++) {
        qufr = (double) i * 100.0 / (double) refToMain.samplerate * (double) len / 2.0;
        qufr = (double) ((int) qufr * 10) / 10.0;
        str = String.valueOf(qufr);
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
      CepstrumTransform ct = new CepstrumTransform();
      cepstrum = ct.doCepstrumTransform(buffer, refToMain.gad.ccep.isSelected());
      // Smooth out the Cepstrum
      if (refToMain.gad.ccepsmooth.isSelected() == true) {
        cepstrum[0] = 0.0f;
        cepstrum[2] = (cepstrum[1] + cepstrum[2] + cepstrum[3] + cepstrum[4]) / 4.0f;
        cepstrum[1] = (cepstrum[0] + cepstrum[1] + cepstrum[3]) / 3.0f;
        cepstrum[0] = (cepstrum[0] + cepstrum[1]) / 3.0f;
        cepstrum[len - 3] = (cepstrum[len - 2] + cepstrum[len - 3] + cepstrum[len - 4]) / 3.0f;
        cepstrum[len - 2] = (cepstrum[len - 1] + cepstrum[len - 2] + cepstrum[len - 3]) / 3.0f;
        cepstrum[len - 1] = (cepstrum[len - 1] + cepstrum[len - 2]) / 2.0f;
        for (int i = 3; i < (len - 3); i++)
          cepstrum[i] =
              (cepstrum[i]
                      + cepstrum[i + 1]
                      + cepstrum[i - 1]
                      + cepstrum[i + 2]
                      + cepstrum[i - 2]
                      + cepstrum[i + 3]
                      + cepstrum[i - 3])
                  / 7.0f;
      }
      // Search for Peak
      for (int i = 0; i < len / 2; i++) {
        if (peak < cepstrum[i]) peak = cepstrum[i];
      }
      // Paint Spectrum
      g.setColor(collv);
      fakt = (double) (xm - 25) / (double) (len / 2);
      diff = ((double) len / 2.0 / (double) (xm - 25));

      g.setStroke(new BasicStroke(3));
      g.setColor(new Color(80, 80, 90));
      for (double f = diff; f < (float) (len / 2); f += diff) {
        y = (int) (cepstrum[(int) f] / peak * (ym - 25));
        ya = (int) (cepstrum[(int) (f - diff)] / peak * (ym - 25));
        x = (int) (f * fakt);
        xa = (int) ((f - diff) * fakt);

        g.drawLine(xa + 21, ym - 21 - ya, x + 21, ym - 21 - y);
      }
      g.setStroke(new BasicStroke(1));
      g.setColor(new Color(180, 180, 220));
      for (double f = diff; f < (float) (len / 2); f += diff) {
        y = (int) (cepstrum[(int) f] / peak * (ym - 25));
        ya = (int) (cepstrum[(int) (f - diff)] / peak * (ym - 25));
        x = (int) (f * fakt);
        xa = (int) ((f - diff) * fakt);

        g.drawLine(xa + 21, ym - 21 - ya, x + 21, ym - 21 - y);
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
