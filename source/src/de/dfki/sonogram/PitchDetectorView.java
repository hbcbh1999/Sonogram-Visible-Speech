package de.dfki.sonogram;

import de.dfki.maths.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>The pitch detector class is placed under the main window and shows the pitches of the sonogram
 * main window. The time boundaries are sililar to the time stamps in the main window, means it
 * shows the self time span like in the main sonogram window.
 *
 * @author Christoph Lauer
 * @version 1.0, Begin 17/10/2002
 */
public class PitchDetectorView extends JFrame {
  private Sonogram reftosonogram;
  private float[] pitches; // holds the pitch frequencies
  private float[]
      energies; // the median energie of the window (don't paint pitches for silent areas)
  private double oldtimespanduration; // Time span duration
  private double oldtimespanoffset; // Time span offset
  private double windowlength; // Window length for autocorrelation in ms
  private double windowshift; // Window shift for autocorrelation in ms
  private int windowlengthsamples; // Window length for autocorrelation in samples
  private int windowshiftsamples; // Window shift for autocorrelation in samples
  private int windowsamplesall; // Summ of winlensamples and winshiftsamples
  private int beginsamples; // Begin of the view point in samples
  private int endsamples; // End of the view point in samples
  private int spansamples; // Duration of the view in samples
  private boolean forcerecalculating = false;
  private float RUNAWAY_CONST = 0.3f;
  public int mouse_x, mouse_y;
  private int pitchcounter = 0;

  public PitchDetectorView(Sonogram ref) {
    reftosonogram = ref;
    setTitle("Autocorrealtion based Pitch Tracking");
    Toolkit tk = Toolkit.getDefaultToolkit();
    setIconImage(tk.getImage(Sonogram.class.getResource("Sonogram.gif")));
    PitchPanel pp = new PitchPanel();
    getContentPane().add(pp);
  }

  public void placePitchwindowUnderTheMainWindow() {
    Dimension sonosize = reftosonogram.getSize();
    Point sonoposition = reftosonogram.getLocation();
    int pitchwinwidth = (int) sonosize.getWidth();
    int pitchwinheigth = 220;
    int pitchwinposx = (int) sonoposition.getX();
    int pitchwinposy = (int) sonoposition.getY() + (int) sonosize.getHeight();
    setLocation(pitchwinposx, pitchwinposy);
    setSize(pitchwinwidth, pitchwinheigth);
    setResizable(false);
  }

  /**
   * This class generated the pitch array for if an update is needed. The pitches are stored in the
   * float pitch array.
   */
  public void calculatePitches() {
    pitchcounter = 0;
    // The Duration of the displayed span in seconds
    double timespanduration =
        (double) reftosonogram.samplestotal / (double) reftosonogram.samplerate;
    // The offset for the span in seconds
    double timespanoffset =
        (double) reftosonogram.selectedstartold
            * (double) reftosonogram.samplesall
            / (double) reftosonogram.samplerate;
    // Check if recalculating is needed
    if (timespanoffset != oldtimespanoffset
        || timespanduration != oldtimespanduration
        || forcerecalculating == true) {
      System.out.println("--> Pitch Tracker callculating Updating");
      System.out.println(
          "--> Pitchcalc Offset: " + timespanoffset + " Duration: " + timespanduration);
      forcerecalculating = false;
      oldtimespanduration = timespanduration;
      oldtimespanoffset = timespanoffset;
      // First read out the numbers from the sliders in the gad
      int sliderwl = reftosonogram.gad.sliderpwinlength.getValue();
      if (sliderwl == 0) windowlength = 5;
      else if (sliderwl == 1) windowlength = 10;
      else if (sliderwl == 2) windowlength = 25;
      else if (sliderwl == 3) windowlength = 50;
      else if (sliderwl == 4) windowlength = 100;
      else if (sliderwl == 5) windowlength = 250;
      windowlengthsamples =
          (int) ((double) windowlength / 1000.0 * (double) reftosonogram.samplerate);
      int sliderws = reftosonogram.gad.sliderpwinshift.getValue();
      if (sliderws == 0) windowshift = 1.25;
      else if (sliderws == 1) windowshift = 2.5;
      else if (sliderws == 2) windowshift = 5;
      else if (sliderws == 3) windowshift = 10;
      else if (sliderws == 4) windowshift = 25;
      else if (sliderws == 5) windowshift = 50;
      else if (sliderws == 6) windowshift = 100;
      windowshiftsamples =
          (int) ((double) windowshift / 1000.0 * (double) reftosonogram.samplerate);
      windowsamplesall = windowshiftsamples + windowlengthsamples;
      // The main callcualtion loop
      beginsamples = (int) ((double) oldtimespanoffset * (double) reftosonogram.samplerate);
      spansamples = (int) ((double) oldtimespanduration * (double) reftosonogram.samplerate);
      endsamples = beginsamples + spansamples;
      // Allocate the tmp buffer and some other needed fields
      float[] buffer = new float[windowsamplesall];
      Byte tempbyte;
      float frequency;
      float[] autocorrelation;
      // Allocate the pitch Buffer
      pitches = new float[spansamples / windowlengthsamples];
      energies = new float[spansamples / windowlengthsamples];
      // Main loop over the visual boundared samples in step of the windowlength
      for (int step = beginsamples;
          step < (endsamples - windowsamplesall);
          step += windowlengthsamples) {
        float energy = 0.0f;
        // Copy the stepped window in the buffer with the length of the windowlength
        for (int i = step; i < (step + windowsamplesall); i++) {
          tempbyte = (Byte) reftosonogram.reader.audioStream.get(i);
          buffer[i - step] = tempbyte.floatValue();
          if (reftosonogram.gad.cplimitfr.isSelected() == true)
            energy += tempbyte.floatValue() * tempbyte.floatValue();
        }
        // store the window energy
        if (reftosonogram.gad.cplimitfr.isSelected() == true) energies[pitchcounter] = energy;
        // call the autocorrelation
        autocorrelation =
            AutoCorrelation.autoCorrelate(buffer, windowlengthsamples, windowshiftsamples);
        //		// calc abs
        // 		for (int i=0;i<buffer.length;i++) {
        // 		    buffer[i] = (buffer[i]*buffer[i]);
        // 		}
        // Search the min and max peaks
        float maxpeak = 0.0f;
        float minpeak = 0.0f;
        float peak = 0.0f;
        for (int i = 0; i < autocorrelation.length; i++) {
          if (maxpeak < autocorrelation[i]) maxpeak = autocorrelation[i];
          if (minpeak > autocorrelation[i]) minpeak = autocorrelation[i];
        }
        if (-minpeak > maxpeak) peak = -minpeak;
        else peak = maxpeak;
        // Smooth before searching for peaks
        if (reftosonogram.gad.cpsmooth.isSelected() == true)
          autocorrelation = VectorSmoother.smoothWithDegreeFour(autocorrelation);
        // Search the Local Maximas
        PeakSearcher peaksearcher = new PeakSearcher(autocorrelation, (float) (peak * 0.2), 5, 0);
        frequency =
            (float)
                    (Math.abs(
                        peaksearcher.getHighestPeakPosition()
                            - peaksearcher.getSecondHighestPeakPosition()))
                / (float) reftosonogram.samplerate;
        frequency = 1 / frequency;
        pitches[pitchcounter] = frequency;
        pitchcounter++;
      }
      // normalize median window signal energies
      float maxenergy = 0.0f;
      for (int i = 0; i < pitches.length; i++) { // look for the peak
        if (energies[i] > maxenergy) maxenergy = energies[i];
      }
      for (int i = 0; i < pitches.length; i++) { // and normalize energies [0,1]
        energies[i] /= maxenergy;
      }
      // remove pitches in silent
      if (reftosonogram.gad.cplimitfr.isSelected() == true) {
        float[] pitches_copy = new float[spansamples / windowlengthsamples];
        System.arraycopy(pitches, 0, pitches_copy, 0, pitches.length);
        for (int i = 0; i < pitches.length; i++) {
          if (energies[i] < 0.01f) pitches_copy[i] = 0.0f;
        }
        pitches = pitches_copy;
      }

      // remove runaways
      if (reftosonogram.gad.cpraway.isSelected() == true) {
        float[] pitches_copy = new float[spansamples / windowlengthsamples];
        System.arraycopy(pitches, 0, pitches_copy, 0, pitches.length);
        for (int i = 0; i < pitches.length; i++) {
          int pb = i - 1;
          if (pb < 0) pb = 0;
          int pa = i + 1;
          if (pa >= pitches.length) pa = pitches.length - 1;
          float fb = pitches[pb];
          float f = pitches[i];
          float fa = pitches[pa];
          if (Math.abs(f - fa) > f * RUNAWAY_CONST
              && Math.abs(f - fb) > f * RUNAWAY_CONST) // Runaways
          pitches_copy[i] = 0.0f;
        }
        pitches = pitches_copy;
      }
    }
  }
  /** Like upper callculating function but without the update check. */
  public void calculatePitchesWithoutCheck() {
    forcerecalculating = true;
    update();
  }

  public void update() {
    if (isVisible() == true
        && reftosonogram.openingflag == false
        && reftosonogram.spektrumExist == true) {
      repaint();
    }
  }
  /** Inner panel class for the paint routines. */
  class PitchPanel extends JPanel {
    public PitchPanel() {
      addMouseMotionListener(
          new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
              mouse_x = e.getX();
              mouse_y = e.getY();
              if (reftosonogram.gad.cptrack.isSelected() == true) repaint();
            }
          });
    }
    /**
     * Overwritten function for the painting if the component. This function paints all the
     * backgrounds, grids and curves on the panel resp. on the window where the panel is placed.
     * This function calls the callculate pitches function first.
     *
     * @param gr Graphics the pen.
     */
    public void paintComponent(Graphics gr) {
      calculatePitches();
      Graphics2D g = (Graphics2D) gr;
      if (reftosonogram.gad.cantialise.isSelected() == true)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      Dimension size = getSize();
      int xm = (short) size.getWidth();
      int ym = (short) size.getHeight();
      int x, y;
      Color colordarkviolett = new Color(80, 80, 120);
      Color colorgrid = new Color(100, 80, 90);
      Color colorbackground = new Color(150, 150, 200);
      Color colorsignalbackground = new Color(15, 50, 20);
      double maxfrequency;
      maxfrequency = reftosonogram.gad.sliderpitchmax.getValue();
      // Backgrounds
      g.setColor(colorbackground); // main background
      g.fillRect(0, 0, xm, ym);
      g.setColor(colorsignalbackground); // main signal background
      g.fillRect(40, 10, xm - 100, ym - 20); // Delete old one
      // Grid over the amplitudes
      g.setFont(new Font("Courier", 0, 9));
      for (double a = 0.0; a <= 1.0; a += 0.1) {
        y = (int) (a * ((double) ym - 20.0) + 10.0);
        g.setColor(colorgrid);
        g.drawLine(40, y, xm - 61, y);
        g.setColor(colordarkviolett);
        g.drawLine(0, y, 40, y);
        g.drawString((int) ((1 - a) * maxfrequency) + "Hz", 2, y + 9);
      }
      // Grid over the time
      g.setColor(colorgrid);
      double secs =
          reftosonogram.selectedstartold
              * (double) reftosonogram.samplesall
              / (double) reftosonogram.samplerate;
      int isecs = (int) secs;
      int offset =
          (int)
              (1.0
                  - (secs - (double) isecs)
                      * ((double) xm - 100.0)
                      / ((double) reftosonogram.samplestotal / (double) reftosonogram.samplerate));
      secs = isecs;
      if (reftosonogram.selectedstartold == 0.0) secs = 0.0;
      for (x = 40 + offset;
          x < xm - 60;
          x +=
              (int)
                  (((double) xm - 100.0)
                      / ((double) reftosonogram.samplestotal
                          / (double) reftosonogram.samplerate))) {
        g.drawLine(x - 1, 10, x - 1, ym);
        g.drawString(secs + "s", x + 1, ym - 1);
        secs++;
      }
      // Draw the pitches on the screen
      double stepwidth =
          ((double) xm - 100.0)
              / pitches.length; // Distance between the pitch points in pixel dimensions
      double amplitudefactor =
          (double) (ym - 20) / maxfrequency; // Factor for the amplitude calculating
      Color colorpoint = new Color(255, 50, 0);
      Color colorline = new Color(255, 255, 0);
      int xa = 0, ya = 0, ia = 0;
      boolean paintlines = reftosonogram.gad.cppoints.isSelected();
      boolean paintthisline = false;
      boolean paintinfo = false;
      int pitches_painted = 0;
      float pitch_a = pitches[0];
      float pitch_f = 0.0f, pitch_t = 0.0f;
      boolean pitch_is_tracked = false; // ensure only one pitchis painted
      float highlight_area = 60.0f;
      int pitch_x = 0;
      int pitch_y = 0;
      for (int i = 0; i < pitches.length; i++) {
        // Break the paint routine it the pitch is out of border;
        if (pitches[i] > maxfrequency || pitches[i] == 0.0f) continue;
        x = (int) ((double) i * stepwidth) + 40;
        y = ym - (int) (pitches[i] * amplitudefactor) - 10;
        // look for pitch points under the mouse pointer
        if (Math.abs(mouse_x + 3 - x) < highlight_area
            && reftosonogram.gad.cptrack.isSelected() == true) {
          if (reftosonogram.gad.cpfog.isSelected() == true) {
            g.setComposite(
                AlphaComposite.getInstance(
                    AlphaComposite.SRC_OVER,
                    0.4f
                        - 0.4f
                            * (float)
                                (Math.abs((float) mouse_x + 3.0f - (float) x) / highlight_area)));
            g.setColor(new Color(255, 255, 255));
            g.fillOval(x - 7, y - 7, 14, 14);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
          }
          if (Math.abs(mouse_x + 3 - x) < 6.0 && pitch_is_tracked == false) {
            paintinfo = true;
            pitch_f = pitches[i];
            pitch_t =
                (float) (beginsamples + windowlengthsamples * i) / (float) reftosonogram.samplerate;
            pitch_x = x;
            pitch_y = y;
            pitch_is_tracked = true;
          }
        }
        g.setColor(colorpoint);
        pitches_painted++;
        g.fillOval(x - 2, y - 2, 4, 4);
        if (paintlines == true) // If line connecting is selected
        {
          if (ya == 0) {
            xa = x;
            ya = y;
          }
          if (Math.abs(pitches[i] - pitch_a) < pitches[i] * RUNAWAY_CONST) {
            // g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            if (i - ia < 5) {
              g.setStroke(new BasicStroke(1));
              g.setColor(colorline);
              g.drawLine(x, y, xa, ya);
            }
            ia = i;
          }
          xa = x;
          ya = y;
        }
        pitch_a = pitches[i];
      }
      // g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
      if (pitch_is_tracked == true) {
        g.setColor(Color.RED);
        g.drawOval(pitch_x - 6, pitch_y - 6, 12, 12);
        g.drawOval(pitch_x - 7, pitch_y - 7, 14, 14);
      }
      // Text on the side
      Color colortextfill = new Color(125, 125, 185);
      Color colortextborder = new Color(95, 95, 180);
      Color colortext = new Color(20, 20, 130);
      g.setFont(new Font("Dialog", 0, 9));
      g.setColor(colortextborder); // Borders
      g.drawRect(xm - 57, 9, 52, 27);
      g.drawRect(xm - 57, 39, 52, 27);
      g.drawRect(xm - 57, 69, 52, 27);
      g.drawRect(xm - 57, 99, 52, 27);
      g.drawRect(xm - 57, 129, 52, 27);
      g.setColor(colortextfill); // Inner Fill Rectangle
      g.fillRect(xm - 56, 10, 51, 26);
      g.fillRect(xm - 56, 40, 51, 26);
      g.fillRect(xm - 56, 70, 51, 26);
      g.fillRect(xm - 56, 100, 51, 26);
      g.fillRect(xm - 56, 130, 51, 26);
      g.setColor(colortext); // The text
      g.drawString("Callculated", xm - 56, 18);
      g.drawString("Windows:", xm - 56, 27);
      g.drawString("" + pitchcounter, xm - 56, 36);
      g.drawString("Located", xm - 56, 48);
      g.drawString("Pitches:", xm - 56, 57);
      g.drawString("" + pitches_painted, xm - 56, 66);
      g.drawString("PITCH", xm - 56, 78);
      g.drawString("Frequency:", xm - 56, 87);
      if (paintinfo == true) {
        g.setColor(Color.RED);
        g.drawString(Math.round(pitch_f) + " Hz", xm - 56, 96);
        g.setColor(colortext);
      } // The text
      g.drawString("PITCH", xm - 56, 108);
      g.drawString("Time:", xm - 56, 117);
      if (paintinfo == true) {
        g.setColor(Color.RED);
        g.drawString(Math.round(pitch_t * 1000.0f) / 1000.0f + "sec.", xm - 56, 126);
        g.setColor(colortext);
      } // The text
      g.drawString("WL:", xm - 56, 138);
      g.drawString("" + windowlength + "ms", xm - 38, 138);
      g.drawString("WS:", xm - 56, 147);
      g.drawString("" + windowshift + "ms", xm - 38, 147);
    }
  }
}
