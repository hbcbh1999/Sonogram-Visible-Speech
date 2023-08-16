package de.dfki.maths;


/**
 * (c) DFKI GmbH 26.9.2002
 *
 * <p>Orginal C sources: Author Mike Jackson - University of Edinburgh - 1999-2001
 *
 * <p>The <code>WaveletBasisSets</code> class creates objects that manage a collection of <code>
 * WaveletBasis</code> objects and record the indices of a default and current set
 *
 * @author Christoph Lauer
 * @version 1.0
 */
public class WaveletBasisSets {
  // Default set index
  private int _defaultSet = 0;
  // Current set index
  private int _currentSet = 0;
  // Wavelet basis sets
  private WaveletBasis[] _sets = {
    new WaveletBasis("Beylkin 18", WavletCoefficients.beylkin18),
    new WaveletBasis("Vaidyanathan 24", WavletCoefficients.vaidyanathan24),
    new WaveletBasis("Coifman 6", WavletCoefficients.coifman6),
    new WaveletBasis("Coifman 12", WavletCoefficients.coifman12),
    new WaveletBasis("Coifman 18", WavletCoefficients.coifman18),
    new WaveletBasis("Coifman 24", WavletCoefficients.coifman24),
    new WaveletBasis("Coifman 30", WavletCoefficients.coifman30),
    new WaveletBasis("Daubechies 2(Haar)", WavletCoefficients.daubechies2),
    new WaveletBasis("Daubechies 4", WavletCoefficients.daubechies4),
    new WaveletBasis("Daubechies 6", WavletCoefficients.daubechies6),
    new WaveletBasis("Daubechies 8", WavletCoefficients.daubechies8),
    new WaveletBasis("Daubechies 10", WavletCoefficients.daubechies10),
    new WaveletBasis("Daubechies 12", WavletCoefficients.daubechies12),
    new WaveletBasis("Daubechies 14", WavletCoefficients.daubechies14),
    new WaveletBasis("Daubechies 16", WavletCoefficients.daubechies16),
    new WaveletBasis("Daubechies 18", WavletCoefficients.daubechies18),
    new WaveletBasis("Daubechies 20", WavletCoefficients.daubechies20),
  };

  /**
   * Create a new <code>WaveletBasisSets</code> object recording information about zero <code>
   * WaveletBasis</code> sets, with default and current sets of index 0
   */
  public WaveletBasisSets() {
    _defaultSet = _currentSet = 0;
  }

  /** Set the <code>WaveletBasis</code> sets held by the <code>WaveletBasisSets</code> object. */
  public void setSets(WaveletBasis[] sets) {
    _sets = sets;
  }

  /** Set the index of the default <code>WaveletBasis</code> set */
  public void setDefaultSet(int defaultSet) {
    _defaultSet = defaultSet;
  }

  /** Get the index of the default <code>WaveletBasis</code> set */
  public int getDefaultSet() {
    return _defaultSet;
  }

  /** Set the index of the current <code>WaveletBasis</code> set */
  public void setCurrentSet(int currentSet) {
    _currentSet = currentSet;
  }

  /** Get the index of the current <code>WaveletBasis</code> set */
  public int getCurrentSet() {
    return _currentSet;
  }

  /** Set the current set to be the default <code>WaveletBasis</code> set */
  public void selectDefaultSet() {
    _currentSet = _defaultSet;
  }

  /** Get the number of <code>WaveletBasis</code> sets */
  public int getNumSets() {
    int length = 0;
    if (_sets != null) {
      length = _sets.length;
    }
    return length;
  }

  /** Get the <code>i</code>th <code>WaveletBasis</code> set */
  public WaveletBasis getSet(int i) {
    return _sets[i];
  }
}
