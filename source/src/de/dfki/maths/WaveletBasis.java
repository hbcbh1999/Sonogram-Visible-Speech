package de.dfki.maths;

/**
 * (c) DFKI GmbH 26.9.2002
 *
 * <p>Orginal C sources: Author Mike Jackson - University of Edinburgh - 1999-2001
 *
 * <p>The <code>WaveletBasis</code> class creates objects that represent wavelet basis sets (sets of
 * floating-point coefficients)
 *
 * @author Christoph Lauer
 * @version 1.0
 */
public class WaveletBasis {
  // Wavelet basis set name
  private String _name;
  // Number of coefficients in wavelet basis set
  private int _numCoefficients;
  // Wavelet basis set coefficients
  private float[] _coefficients;

  /**
   * Create a new <code>WaveletBasis</code> object representing a wavelet basis set with the given
   * name and given coefficient values
   */
  public WaveletBasis(String name, float[] coefficients) {
    _name = name;
    _numCoefficients = coefficients.length;
    _coefficients = coefficients;
  }

  /** Get the <code>WaveletBasis</code> set name */
  public String getName() {
    return _name;
  }

  /** Get the number of coefficients in the <code>WaveletBasis</code> set */
  public int getNumCoefficients() {
    return _numCoefficients;
  }

  /** Get the <code>WaveletBasis<code> coefficients */
  public float[] getCoefficients() {
    return _coefficients;
  }
}
