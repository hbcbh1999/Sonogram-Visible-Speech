package de.dfki.maths;


/**
 * (c) DFKI GmbH 26.9.2002
 *
 * <p>Orginal C sources: Author Mike Jackson - University of Edinburgh - 1999-2001
 *
 * <p><code>DFT</code> is a sub-class of <code>FFT</code> providing static methods implementing the
 * discrete Fourier transform and the inverse discrete Fourier Transform, the former with options
 * for zero-padding and the application of windowing functions.
 *
 * <p>Note that DFTs will only be performed on input sequences (or zero-padded input sequences) that
 * are powers of 2. If an input sequence (after zero-padding where applicable) is not of length that
 * is a power of 2 then the DFT will be calculated for the first N numbers where N is largest power
 * of two less than the length of the input sequence.
 *
 * @author Christoph Lauer
 * @version 1.0
 */
public class DFT extends FFT {

  /**
   * Calculate the DFT of the first <code>dataLength</code> elements of the purely real data.
   *
   * <p>Apply the windowing function values given in the <code>window</code> array by pairwise
   * multiplication with the input data prior to calculation of the DFT.
   *
   * <p>If <code>dftLength</code> > <code>dataLength</code> then the input data will be zero-padded
   * to <code>dftLength</code> prior to taking the DFT.
   *
   * <p>Place the complex DFT - which will be of length <code>dftLength</code> - in the given <code>
   * dft</code> array.
   *
   * <p>Assumes that <code>dataLength</code> <= <code>data.length</code> <= <code>dft.length</code>
   * <= <code>dftLength</code> and that <code>windowLength</code> <= <code>window.Length</code> <=
   * <code>dataLength</code>
   */
  public static void dft(
      float[] data,
      int dataLength,
      Complex[] dft,
      int dftLength,
      float[] window,
      int windowLength) {
    float[] windowedData = new float[dataLength];
    // Apply the window function
    for (int i = 0; i < windowLength; ++i) {
      windowedData[i] = data[i] * window[i];
    }
    // Calculate the DFT
    dft(windowedData, dataLength, dft, dftLength);
    windowedData = null;
  }

  /**
   * Calculate the DFT of the first <code>dataLength</code> elements of the purely real data.
   *
   * <p>Apply the windowing function values given in the <code>window</code> array by pairwise
   * multiplication with the input data prior to calculation of the DFT.
   *
   * <p>Place the complex DFT in the given <code>dft</code> array.
   *
   * <p>Assumes that <code>dataLength</code> <= <code>data.length</code> <= <code>dft.length</code>
   * <= <code>dftLength</code> and that <code>windowLength</code> <= <code>window.Length</code> <=
   * <code>dataLength</code>
   */
  public static void dft(
      float[] data, int dataLength, Complex[] dft, float[] window, int windowLength) {
    // Calculate the DFT - no zero-padding to be applied
    dft(data, dataLength, dft, dataLength, window, windowLength);
  }

  /**
   * Calculate the DFT of the first <code>dataLength</code> elements of the purely real data.
   *
   * <p>Place the complex DFT in the given <code>dft</code> array.
   *
   * <p>Assumes that <code>dataLength</code> <= <code>data.length</code> <= <code>dft.length</code>
   * <= <code>dftLength</code>
   */
  public static void dft(float[] data, int dataLength, Complex[] dft) {
    dft(data, dataLength, dft, dataLength);
  }

  /**
   * Calculate the DFT of the first <code>dataLength</code> elements of the purely real data.
   *
   * <p>If <code>dftLength</code> > <code>dataLength</code> then the input data will be zero-padded
   * to <code>dftLength</code> prior to taking the DFT.
   *
   * <p>Place the complex DFT - which will be of length <code>dftLength</code> - in the given <code>
   * dft</code> array.
   *
   * <p>Assumes that <code>dataLength</code> <= <code>data.length</code> <= <code>dft.length</code>
   * <= <code>dftLength</code>
   */
  public static void dft(float[] data, int dataLength, Complex[] dft, int dftLength) {
    // Convert the input data to complex form
    for (int i = 0; i < dataLength; ++i) {
      dft[i] = new Complex(data[i], 0f);
    }
    // Zero-pad the input data
    for (int i = dataLength; i < dftLength; ++i) {
      dft[i] = new Complex();
    }
    // Perform an in-place FFT
    fft(dft, dftLength);
  }

  /**
   * Calculate the inverse DFT of the first <code>dataLength</code> elements of the given <code>
   * Complex</code> data.
   *
   * <p>Place the inverse DFT - which will be of length <code>dataLength</code> - in the given
   * <code>idft</code> array.
   *
   * <p>Assumes that <code>dataLength</code> <= <code>data.length</code> <= <code>idft.length</code>
   */
  public static void inverseDFT(Complex[] data, int dataLength, Complex[] idft) {
    inverseDFT(data, dataLength, idft, dataLength);
  }

  /**
   * Calculate the inverse DFT of the first <code>dataLength</code> elements of the given <code>
   * Complex</code> data.
   *
   * <p>If <code>idftLength</code> > <code>dataLength</code> then the input data will be zero-padded
   * to <code>idftLength</code> prior to taking the inverse DFT.
   *
   * <p>Place the inverse DFT - which will be of length <code>idftLength</code> - in the given
   * <code>dft</code> array.
   *
   * <p>Assumes that <code>dataLength</code> <= <code>data.length</code> <= <code>idft.length</code>
   * <= <code>idftLength</code>
   */
  public static void inverseDFT(Complex[] data, int dataLength, Complex[] idft, int idftLength) {
    // Copy the input data ready for an in-place inverse FFT
    for (int i = 0; i < dataLength; ++i) {
      idft[i] = (Complex) data[i].clone();
    }
    // Zero-pad the input data
    for (int i = dataLength; i < idftLength; ++i) {
      idft[i] = new Complex();
    }
    inverseFFT(idft, idftLength);
  }
}
