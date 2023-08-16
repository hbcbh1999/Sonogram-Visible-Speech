/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>Implements Static Methods for Window Funktions for the Short-Time-Fourier-Transformation
 * (STFT)
 *
 * @author Christoph lauer
 * @version 1.2, Begin 15/02/2002, Current 26/09/2002
 */
package de.dfki.maths;

public class WindowFunktion {

  // ------------------------------------------------------------------------------------------

  public static float[] hammingWindow(int len) {
    float[] hamming = new float[len];
    for (int n = 0; n < len; n++)
      hamming[n] = 0.5f - 0.5f * (float) Math.cos(2.0 * Math.PI * n / len);
    return hamming;
  }

  // ------------------------------------------------------------------------------------------

  public static float[] rectangleWindow(int len) {
    float[] rectangle = new float[len];
    for (int n = 0; n < len; n++) rectangle[n] = 1.0f;
    return rectangle;
  }

  // ------------------------------------------------------------------------------------------

  public static float[] blackmanWindow(int len) {
    float[] blackman = new float[len];
    for (int n = 0; n < len; n++)
      blackman[n] =
          0.42f
              - (float)
                  (0.5 * Math.cos(2.0 * Math.PI * n / len)
                      + 0.08 * Math.cos(2.0 * Math.PI * n / len));
    return blackman;
  }

  // ------------------------------------------------------------------------------------------

  public static float[] hanningWindow(int len) {
    float[] henning = new float[len];
    for (int n = 0; n < len; n++)
      henning[n] = 0.54f - (float) (0.46 * Math.cos(2.0 * Math.PI * n / len));
    return henning;
  }

  // ------------------------------------------------------------------------------------------

  public static float[] triangleWindow(int len) {
    float[] triangle = new float[len];
    for (int n = 0; n < len; n++) {
      if (n < len / 2) triangle[n] = (float) n / (float) len * 2.0f;
      else triangle[n] = 2.0f - (float) (n) / (float) len * 2.0f;
    }
    return triangle;
  }

  // ------------------------------------------------------------------------------------------

  public static float[] welchWindow(int len) {
    float[] welch = new float[len];
    for (int n = 0; n < len; n++)
      welch[n] =
          1.0f - (float) Math.pow(((double) n - (double) len / 2.0) / ((double) len / 2.0), 2.0);
    return welch;
  }

  // ------------------------------------------------------------------------------------------

  public static float[] gaussWindow(int len) {
    float[] gauss = new float[len];
    float rho = 0.3333f; // Parameter der Gausskurve
    for (int n = 0; n < len; n++)
      gauss[n] = (float) Math.exp(-0.5 * Math.pow((n - len / 2.0) / (rho * len / 2.0), 2));
    return gauss;
  }

  // ------------------------------------------------------------------------------------------

  public static float[] flattopWindow(int len) {
    float[] flattop = new float[len];
    float a0 = 1.000f;
    float a1 = 1.930f;
    float a2 = 1.290f;
    float a3 = 0.388f;
    float a4 = 0.032f;
    float max = -Float.MAX_VALUE;
    for (int n = 0; n < len; n++) {
      flattop[n] =
          a0
              - a1 * (float) Math.cos(2 * Math.PI * n / (len - 1))
              + a2 * (float) Math.cos(4 * Math.PI * n / (len - 1))
              - a3 * (float) Math.cos(6 * Math.PI * n / (len - 1))
              + a4 * (float) Math.cos(8 * Math.PI * n / (len - 1));
      if (max < flattop[n]) max = flattop[n];
    }
    for (int n = 0; n < len; n++) flattop[n] /= max;
    return flattop;
  }

  // ------------------------------------------------------------------------------------------

  public static float[] harrisWindow(int len) {
    float[] harris = new float[len];
    float a0 = 0.355768f;
    float a1 = 0.487396f;
    float a2 = 0.144232f;
    float a3 = 0.012604f;
    float max = -Float.MAX_VALUE;
    for (int n = 0; n < len; n++) {
      harris[n] =
          a0
              - a1 * (float) Math.cos(2 * Math.PI * n / (len - 1))
              + a2 * (float) Math.cos(4 * Math.PI * n / (len - 1))
              - a3 * (float) Math.cos(6 * Math.PI * n / (len - 1));
      if (max < harris[n]) max = harris[n];
    }
    for (int n = 0; n < len; n++) harris[n] /= max;
    return harris;
  }

  // ------------------------------------------------------------------------------------------

  public static float[] cosineWindow(int len) {
    float[] cosine = new float[len];
    for (int n = 0; n < len; n++) cosine[n] = (float) Math.sin(Math.PI * n / (len - 1));
    return cosine;
  }

  // ------------------------------------------------------------------------------------------

  public static float[] asymetricalWindow(int len) // FoF Window
      {
    float[] asym = cosineWindow(len);
    float a = 0.75f;
    float max = -Float.MAX_VALUE;
    for (int n = 0; n < len; n++) {
      asym[n] *= n * n;
      if (max < asym[n]) max = asym[n];
    }
    for (int n = 0; n < len; n++) // normalize
    asym[n] /= max;
    return asym;
  }

  // ------------------------------------------------------------------------------------------

} // WindowFunktion
