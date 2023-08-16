package de.dfki.maths;


/**
 * (c) DFKI GmbH 26.9.2002
 *
 * <p>Orginal C sources: Author Mike Jackson - University of Edinburgh - 1999-2001
 *
 * <p>A sub-class of <code>FFT</code> providing a static method implementing convolution
 *
 * @author Christoph Lauer
 * @version 1.0
 */
public class Convolution extends FFT {

  /**
   * Convolve the first <code>length</code> elements of the two purely real data sets, <code>xReals
   * </code> and <code>yReals</code>, and place the results in the array <code>convolution</code>.
   *
   * <p>Assumes <code>length</code> <= </code>xReals.length</code>, <code>length</code> <= <code>
   * yReals.length</code>, and 2 * <code>length</code> <= <code>convolution.length</code>
   */
  public static void convolve(float[] xReals, float[] yReals, Complex[] convolution, int length) {
    // Length of the convolution of the two data sets
    int convolvedLength = 2 * length;
    // Complex number forms of the two data sets
    Complex xComplex[] = new Complex[convolvedLength];
    Complex yComplex[] = new Complex[convolvedLength];
    // Loop indices
    int i, j;
    for (i = 0, j = length; i < length; ++i, ++j) {
      // Convert the data sets to complex form
      xComplex[i] = new Complex(xReals[i], 0f);
      yComplex[i] = new Complex(yReals[i], 0f);
      // Zero-pad to double length
      xComplex[j] = new Complex();
      yComplex[j] = new Complex();
    }
    // FFT the data sets
    fft(xComplex, convolvedLength);
    fft(yComplex, convolvedLength);
    // Pairwise multiply in frequency domain
    for (i = 0; i < convolvedLength; ++i) {
      xComplex[i].multiply(yComplex[i]);
    }
    // Take the inverse FFT of the product of the FFTs
    inverseFFT(xComplex, convolvedLength);
    // Copy the result into the given convolution array
    System.arraycopy(xComplex, 0, convolution, 0, convolvedLength);
    xComplex = yComplex = null;
  }
}
