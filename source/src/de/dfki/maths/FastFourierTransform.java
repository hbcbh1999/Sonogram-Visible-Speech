package de.dfki.maths;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de (c)
 * Deutsches Forschungszentrum für Künstliche Intelligenz GmbH
 *
 * <p>Diese unveränderliche Klasse repräsentiert eine eindimensionale Schneller Fourier
 * Transformation FFT.
 *
 * @author Christoph Lauer
 * @version 1.0, Begin 07/04/2001, Current Time-stamp: <03/04/22 14:56:26 clauer>
 */
public final class FastFourierTransform {

  private int n, nu;

  public final float[] doFFT(float[] x) {
    // assume n is a power of 2
    n = x.length;
    double ld = (Math.log(n) / Math.log(2.0));

    if ((float) ((int) ld) - ld != 0) {
      System.out.println("Klasse FastFourierTransformation:");
      System.out.println("Der uebergebene Vektor hat keine laenge von 2 hoch n.");
      System.out.println("Die Laenge ist:" + n + " Der Logarithmus Dualis ist:" + ld);
      return x;
    }
    nu = (int) ld;
    int n2 = n / 2;
    int nu1 = nu - 1;
    float[] xre = new float[n];
    float[] xim = new float[n];
    float[] mag = new float[n2];
    float tr, ti, p, arg, c, s;
    for (int i = 0; i < n; i++) {
      xre[i] = x[i];
      xim[i] = 0.0f;
    }
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
    // calculate Magnitude
    mag[0] = (float) (Math.sqrt(xre[0] * xre[0] + xim[0] * xim[0])) / n;
    for (int i = 1; i < n / 2; i++)
      mag[i] = 2 * (float) (Math.sqrt(xre[i] * xre[i] + xim[i] * xim[i])) / n;
    return mag;
  }

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
}
