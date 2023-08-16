package de.dfki.maths;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de (c)
 * Deutsches Forschungszentrum für Künstliche Intelligenz GmbH
 *
 * <p>This Class computes the Linear Prediction Transform Vector and transformate them to Frequency
 * domain. Linear Prediction first calculate Linear Prediction Coefficients for given count of
 * Coefficients. After that a Prediction Vector is generated who windowed or not windowed is
 * transformated into Frequencyzone. All options are received from the GAD dialog.
 *
 * @author Christoph Lauer
 * @version 1.0, Begin 07/04/2001, Current Time-stamp: <03/04/22 14:53:02 clauer>
 */
public class LinearPredictionTransform {
  // ------------------------------------------------------------------------------------------------------
  /**
   * This Funktion Transform a given time domainVector with Samples with Linear Prediction Transform
   * into a Frequencyvector.
   *
   * @param samples: Time domain based Vector with Samplepoints.
   * @param fftlen: lenght of buffer for FTT at end.
   * @param lpccoef: number of Linear Prediction Coefficients user to generate Futurvector.
   * @param windowing: Windowfunktion do befor FFT at end.
   */
  public static float[] doLinearPredictionTransform(
      float[] samples, int fftlen, int lpccoef, float[] windowing) {
    // Generate LPC Coef.
    float[] lpc = memcof(samples, lpccoef);
    // Generate Future Tileline
    float[] future = predic(samples, lpc, fftlen - 1);
    // Windowing
    if (windowing != null)
      if (windowing.length == future.length) {
        for (int i = 0; i < future.length; i++) future[i] *= windowing[i];
      } else System.out.println("WRONG WINDOWLENGTH FOR WINDOWFUNKTION IN LPC");
    // Make FastFourierTransform
    FastFourierTransform fft = new FastFourierTransform();
    float[] lpcspec = fft.doFFT(future);

    return (lpcspec);
  }
  // ------------------------------------------------------------------------------------------------------
  private static float[] memcof(float[] data, int m) {

    int n = data.length - 1;
    float p = 0.0f;
    float[] wk1 = new float[n + 1];
    float[] wk2 = new float[n + 1];
    float[] wkm = new float[m + 1];
    float[] d = new float[m + 1];
    float xms;
    int k, j, i;

    for (j = 1; j <= n; j++) p += (data[j] * data[j]);
    xms = p / n;
    wk1[1] = data[1];
    wk2[n - 1] = data[n];
    for (j = 2; j <= n - 1; j++) {
      wk1[j] = data[j];
      wk2[j - 1] = data[j];
    }
    for (k = 1; k <= m; k++) {
      float num = 0.0f, denom = 0.0f;
      for (j = 1; j <= (n - k); j++) {
        num += wk1[j] * wk2[j];
        denom += (wk1[j] * wk1[j]) + (wk2[j] * wk2[j]);
      }
      d[k] = 2.0f * num / denom;
      xms = (1.0f - (float) (d[k] * d[k]));
      for (i = 1; i <= (k - 1); i++) d[i] = wkm[i] - d[k] * wkm[k - i];
      if (k == m) {
        return d;
      }
      for (i = 1; i <= k; i++) wkm[i] = d[i];
      for (j = 1; j <= (n - k - 1); j++) {
        wk1[j] -= wkm[k] * wk2[j];
        wk2[j] = wk2[j + 1] - wkm[k] * wk1[j + 1];
      }
    }
    System.out.println("Never get here in memcof !!!");
    return d; // Only for Compiler
  }
  // ------------------------------------------------------------------------------------------------------
  private static float[] predic(float[] data, float[] d, int nfut) {
    int ndata = data.length - 1;
    int m = d.length - 1;
    int k, j;
    float sum, discrp;
    float[] reg = new float[m + 1];
    float[] future = new float[nfut + 1];

    for (j = 1; j <= m; j++) reg[j] = data[ndata + 1 - j];
    for (j = 1; j <= nfut; j++) {
      discrp = 0.0f;
      sum = discrp;
      for (k = 1; k <= m; k++) sum += d[k] * reg[k];
      for (k = m; k >= 2; k--) reg[k] = reg[k - 1];
      future[j] = reg[1] = sum;
    }
    return future;
  }
  // ------------------------------------------------------------------------------------------------------
}
