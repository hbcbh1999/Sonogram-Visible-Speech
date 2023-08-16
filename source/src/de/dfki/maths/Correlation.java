package de.dfki.maths;


/**
 * (c) DFKI GmbH 26.9.2002
 *
 * <p>Orginal C sources: Author Mike Jackson - University of Edinburgh - 1999-2001
 *
 * <p>A sub-class of <code>FFT</code> providing a static method implementing auto- and
 * cross-correlation
 *
 * @author Christoph Lauer
 * @version 1.0
 */
public class Correlation extends FFT {

  /**
   * Correlate the first <code>length</code> elements of the two purely real data sets, <code>xReals
   * </code> and <code>yReals</code>, and place the results in the array <code>correlation</code>.
   *
   * <p>Assumes <code>length</code> <= </code>xReals.length</code>, <code>length</code> <= <code>
   * yReals.length</code>, and 2 * <code>length</code> <= <code>correlation.length</code>
   */
  public static void correlate(float[] xReals, float[] yReals, Complex[] correlation, int length) {
    // Length of the correlation of the two data sets
    int correlatedLength = 2 * length;
    // Complex number forms of the two data sets
    Complex xComplex[] = new Complex[correlatedLength];
    Complex yComplex[] = new Complex[correlatedLength];
    // Loop indices
    int i, j;
    for (i = 0, j = length; i < length; ++i, ++j) {
      // Convert the data sets to complex form
      xComplex[j] = new Complex(xReals[i], 0f);
      yComplex[i] = new Complex(yReals[i], 0f);
      // Zero-pad to double length - adding the zero-padding to
      // the start of the first data set
      xComplex[i] = new Complex();
      yComplex[j] = new Complex();
    }
    // FFT the data sets
    fft(xComplex, correlatedLength);
    fft(yComplex, correlatedLength);
    // Take the complex conjugate of each element of the FFT of
    // the first data set then pairwise multiply in frequency
    // domain
    for (i = 0; i < correlatedLength; ++i) {
      xComplex[i].conjugate();
      xComplex[i].multiply(yComplex[i]);
    }
    // Take the inverse FFT of the product of the FFTs
    inverseFFT(xComplex, correlatedLength);
    // Copy the result into the given correlation array
    System.arraycopy(xComplex, 0, correlation, 0, correlatedLength);
    xComplex = yComplex = null;
  }
}
