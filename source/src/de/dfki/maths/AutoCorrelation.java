package de.dfki.maths;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>This class presents static autocorrelation funktions.
 *
 * @author Christoph Lauer
 * @version 1.2
 */
public class AutoCorrelation {

  /**
   * This static class does the autocorrelation for the given float vector. It uses the fuction:
   * autocorrelation[lag] = SUM(n=0,N) (signal[n]*signal[n+lag]) with <em>n</em> ist the input
   * sample index, and 0<lag<N. The degree to which the values of <em>signal</em> at different times
   * <em>n</em> are the same values of the same <em>signal</em> delayes by <em>lag</em> samples
   * determines the magnitude of <em>autocorrelation[lag]</em>. The output of an autocorrelation
   * shows the magnitude for different lag times. Note that the array length of the signal vector
   * must have a length from at least window length + window shift (signal.length >= N+lag-max). If
   * the length is smaler a null float array will be given back.
   *
   * @param float[] signal - the signal vector.
   * @param int windowlength - the length of correlation build with the summaration loop.
   *     Corresponds to lag-max
   * @param int windowshift - the length of the summaration lopp. Corresponds to N
   * @return float[] - the autocorrelated signal vector.
   */
  public static float[] autoCorrelate(float[] signal, int windowlength, int windowshift) {
    int signallenth = signal.length;
    if (signallenth < windowlength + windowshift) return null;
    float[] autocorrelation = new float[windowlength];
    // loop over the magnitude of the autocorrelation
    for (int lag = 0; lag < windowlength; lag++) {
      // loop over the sum
      for (int n = 0; n < windowshift; n++) {
        autocorrelation[lag] += signal[n] * signal[n + lag];
      }
    }
    return autocorrelation;
  }
}
