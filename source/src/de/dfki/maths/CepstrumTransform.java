package de.dfki.maths;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>Cepstrum transformation class.
 *
 * @author Christoph Lauer
 * @version 1.0, Begin Current 26/09/2002
 */
public final class CepstrumTransform {
  // ----------------------------------------------------------------------
  private int n, nu;
  private float[] xre;
  private float[] xim;
  // ----------------------------------------------------------------------
  /** Here transformation is done how descibed in many theory books. */
  public float[] doCepstrumTransform(float[] vec, boolean logarithm) {
    n = vec.length;
    double ld = (Math.log(n) / Math.log(2.0));
    nu = (int) ld;
    if ((float) ((int) ld) - ld != 0) {
      System.out.println("ERROR: in CepstrumTransform");
      System.out.println("The Given Vector did not have an power of two!!!");
      System.out.println("Calculate ending.");
      return vec;
    }
    xre = new float[n];
    xim = new float[n];
    // First Transform
    for (int i = 0; i < n; i++) {
      xre[i] = vec[i];
      xim[i] = 0.0f;
    }
    FFT();
    // Logarithm and inverse Transform
    if (logarithm == true) {
      for (int i = 0; i < n; i++) {
        xre[i] = (float) Math.log(Math.abs(100000000.0 * xre[i]) + 1.0);
        xim[i] = -(float) Math.log(Math.abs(100000000.0 * xim[i]) + 1.0);
      }
    }
    if (logarithm == false) {
      for (int i = 0; i < n; i++) {
        xre[i] = (float) Math.log(Math.abs(xre[i]) + 1.0);
        xim[i] = -(float) Math.log(Math.abs(xim[i]) + 1.0);
      }
    }
    FFT();
    float[] cep = new float[n];
    for (int i = 0; i < n; i++) {
      cep[i] = 2 * (float) (Math.sqrt(xre[i] * xre[i] + xim[i] * xim[i])) / n;
    }
    cep[0] = cep[1] = 0.0f;
    return cep;
  }
  // ----------------------------------------------------------------------
  /** Implementation of FFT Algorithm */
  private void FFT() {
    double ld = (Math.log(n) / Math.log(2.0));
    int n2 = n / 2;
    int nu1 = nu - 1;
    float tr, ti, p, arg, c, s;
    int k = 0;

    for (int l = 1; l <= nu; l++) {
      while (k < n) {
        for (int i = 1; i <= n2; i++) {
          p = bitrev(k >> nu1);
          arg = 2 * (float) Math.PI * p / n;
          c = (float) Math.cos(arg);
          s = (float) Math.sin(arg);
          tr = xre[k + n2] * c + xim[k + n2] * s;
          ti = xim[k + n2] * c - xre[k + n2] * s;
          xre[k + n2] = xre[k] - tr;
          xim[k + n2] = xim[k] - ti;
          xre[k] += tr;
          xim[k] += ti;
          k++;
        }
        k += n2;
      }
      k = 0;
      nu1--;
      n2 = n2 / 2;
    }
    k = 0;
    int r;
    while (k < n) {
      r = bitrev(k);
      if (r > k) {
        tr = xre[k];
        ti = xim[k];
        xre[k] = xre[r];
        xim[k] = xim[r];
        xre[r] = tr;
        xim[r] = ti;
      }
      k++;
    }
  }
  // ----------------------------------------------------------------------
  /** Internal calcuating Class */
  private int bitrev(int j) {
    int j2;
    int j1 = j;
    int k = 0;
    for (int i = 1; i <= nu; i++) {
      j2 = j1 / 2;
      k = 2 * k + j1 - 2 * j2;
      j1 = j2;
    }
    return k;
  }
  // ----------------------------------------------------------------------
}
