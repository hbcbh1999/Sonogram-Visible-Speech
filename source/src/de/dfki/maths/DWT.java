package de.dfki.maths;


/**
 * (c) DFKI GmbH 26.9.2002
 *
 * <p>Orginal C sources: Author Mike Jackson - University of Edinburgh - 1999-2001
 *
 * <p>The <code>DWT</code> class provides objects to manage the calculation of cyclic (periodic)
 * discrete wavelet transforms and their inverses. A <code>WaveletBasis</code> object is used to
 * hold the wavelet set used in the calculation of the transforms.
 *
 * <p>The implementation follows the matrix-based form of the transforms presented in "An
 * Introduction to Random Vibrations, Spectral and Wavelet Analysis" (Third Edition) by D.E.
 * Newland, Longman Press, Harlow, Essex. 1997, Chapter 17 Discrete Wavelet Analysis, pp326 -339.
 *
 * <p>When there are N samples with N = 2^j then the maximum scale level supported by the DWT is j
 * and the number of levels ranges from -1 to j - 1 representing scaling and wavelet functions c(0),
 * d(0), ..., d(j - 1).
 *
 * <p>Methods are also provided that allow the extraction of a wavelet at a specific scale level and
 * also the partial summation of wavelets to a specific scale level.
 *
 * <p>In addition, a set of static methods allow conversion between the sequential representation of
 * a discrete wavelet transform and the shifted/scaled (c(k), d(j, k)) representation
 *
 * @author Christoph Lauer
 * @version 1.0
 */
public class DWT {
  // A scratch array for calculating a DWT or the inverse DWT
  private float[] _scratch;
  // Transform size ( = Number of samples in data sets being
  // transformed)
  private int _transformSize;
  // The current wavelet basis set (Haar Daubechies etc)
  private WaveletBasis _waveletBasis;
  // Data in column zero of M and G matrices.
  // Example:
  // For DWT of maximum level 6 (corresponding to 64 sample DWT with
  // 6 levels: -1, 0, 1, 2, 3, 4, 5) _colZeroM has length 64 and
  // holds the first column of each of the M matrices M1, M2, ... ,
  // M5 in its 64 elements as follows:
  // _colZeroM[0]      - ignored
  // _colZeroM[1]      - ignored
  // _colZeroM[2..3]   - first element of each of the 2 rows of M1
  // _colZeroM[4..7]   - first element of each of the 4 rows of M2
  // _colZeroM[8..15]  - first element of each of the 8 rows of M3
  // _colZeroM[16..31] - first element of each of the 16 rows of M4
  // _colZeroM[32..63] - first element of each of the 32 rows of M5
  //
  // _colZeroG is structured similarly, holding information on G
  // matrices.
  private float[] _colZeroM, _colZeroG;
  // One plus the maximum scale level provided by a
  // _transformSize-sized DWT
  private int _maxLevelPlusOne;

  /**
   * Create a <code>DWT</code> object to allow calculation of discrete wavelet transforms and
   * inverse discrete wavelet transforms. Set the initial number of samples in the data to be
   * transformed. Set the initial wavelet basis set to be the given <code>WaveletBasis</code> object
   */
  public DWT(int transformSize, WaveletBasis waveletBasis) {
    setWaveletBasis(waveletBasis);
    setTransformSize(transformSize);
  }

  /**
   * Set the wavelet bais set (Haar, Daubechies etc) to be the the given <code>WaveletBasis</code>
   * object
   */
  public void setWaveletBasis(WaveletBasis waveletBasis) {
    _waveletBasis = waveletBasis;
    constructMatrices();
  }

  /**
   * Set the size of the transform i.e. the number of samples that are to be transformed in any
   * given data set. If this is not a power of 2 then the transform size is rounded down to the
   * nearest power of 2
   */
  public void setTransformSize(int transformSize) {
    // Maximum scale / level = J - 1 if there are 2 ^ J samples.
    _maxLevelPlusOne = maxLevel(transformSize) + 1;
    // Round number of samples to nearest power of 2
    _transformSize = MathsPower2.pow2(_maxLevelPlusOne);
    // Set up a new scrath array
    _scratch = new float[_transformSize];
    // Update the matrices
    constructMatrices();
  }

  // Construct DWT matrices M and G
  //
  // Only the first column is calculated and stored. The other
  // columns can be determined via offsets. Similarly, H and L
  // matrices can be calculated from M and G.

  private void constructMatrices() {
    int elements = MathsPower2.pow2(_maxLevelPlusOne + 1);
    _colZeroM = new float[elements];
    _colZeroG = new float[elements];
    for (int i = _maxLevelPlusOne; i > 0; i--) {
      constructMatricesRow(i);
    }
  }

  // Calculate the first elements of the rows of matrices
  // M_levelPlusOne and G_levelPlusOne

  private void constructMatricesRow(int levelPlusOne) {
    int numWaveletBasisValues = _waveletBasis.getNumCoefficients();
    float[] waveletBasis = _waveletBasis.getCoefficients();
    int offset = MathsPower2.pow2(levelPlusOne);
    int filled = numWaveletBasisValues / offset;
    int spilled = numWaveletBasisValues % offset;
    int row, coeffIndex, offsetRow;
    for (row = 0, offsetRow = offset; row < offset; row++, offsetRow++) {
      coeffIndex = row;
      for (int i = 0; i < filled; i++) {
        _colZeroM[offsetRow] += waveletBasis[coeffIndex];
        _colZeroG[offsetRow] += waveletBasis[numWaveletBasisValues - coeffIndex - 1];
        coeffIndex += offset;
      }
      if (row < spilled) {
        coeffIndex = filled * offset + row;
        _colZeroM[offsetRow] += waveletBasis[coeffIndex];
        _colZeroG[offsetRow] += waveletBasis[numWaveletBasisValues - coeffIndex - 1];
      }
      if ((row % 2) == 0) {
        _colZeroG[offsetRow] = -_colZeroG[offsetRow];
      }
    }
  }

  // Access individual matrix elements of M, G, H and L matrices
  // where rowsMG is the number of rows in the M / G matrices

  private float elementL(int rowsMG, int row, int column) {
    return elementM(rowsMG, column, row);
  }

  private float elementM(int rowsMG, int row, int column) {
    float element;
    if (column == 0) {
      element = _colZeroM[rowsMG + row];
    } else {
      element = _colZeroM[rowsMG + (((row - (column << 1)) + rowsMG) % rowsMG)];
    }
    return element;
  }

  private float elementH(int rowsMG, int row, int column) {
    return elementG(rowsMG, column, row);
  }

  private float elementG(int rowsMG, int row, int column) {
    float element;
    if (column == 0) {
      element = _colZeroG[rowsMG + row];
    } else {
      element = _colZeroG[rowsMG + (((row - (column << 1)) + rowsMG) % rowsMG)];
    }
    return element;
  }

  /**
   * Given a set of data samples calculate the discrete wavelet transform of these samples and
   * return in the given <code>dwt</code> array. Uses the current wavelet basis set and operates on
   * the first N elements of the array where N is the current transform size.
   */
  public void discreteWT(float[] data, float[] dwt) {
    System.arraycopy(data, 0, dwt, 0, _transformSize);
    wt(_maxLevelPlusOne, (int) MathsPower2.pow2(_maxLevelPlusOne), dwt);
  }

  /**
   * Given a set of data samples calculate the discrete wavelet transform of these samples and
   * return in the given <code>dwt</code> array. Prior to calculation, update the current transform
   * size to be as given (or the nearest power of 2 to this). Uses the current wavelet basis set and
   * operates on the first N elements of the array where N is the current transform size.
   */
  public void discreteWT(int transformSize, float[] data, float[] dwt) {
    setTransformSize(transformSize);
    discreteWT(data, dwt);
  }

  // Execute in-place calculation of the discrete wavelet transform

  private void wt(int levelPlusOne, int levelPlusOnePow2, float[] data) {
    if (levelPlusOne > 0) {
      // Multiply data by H and L matrices
      multiplyHL(data, levelPlusOne, levelPlusOnePow2);
      // Now perform recursive DWT calculation
      wt(levelPlusOne - 1, levelPlusOnePow2 >> 1, data);
    }
  }

  // Multiply data by H and L matrices

  private void multiplyHL(float[] data, int levelPlusOne, int levelPlusOnePow2) {
    int columnsMG = levelPlusOnePow2 >> 1;
    int rowsHL = columnsMG;
    int rowsMG = 2 * columnsMG;
    int columnsHL = rowsMG;
    int i, offset;
    float dataJ, scratchI, scratchOffset;
    for (i = 0, offset = rowsHL; i < rowsHL; i++, offset++) {
      scratchI = scratchOffset = 0;
      for (int j = 0; j < columnsHL; j++) {
        dataJ = data[j];
        scratchI += dataJ * elementL(rowsMG, i, j);
        scratchOffset += dataJ * elementH(rowsMG, i, j);
      }
      _scratch[i] = scratchI /= 2;
      _scratch[offset] = scratchOffset /= 2;
    }
    System.arraycopy(_scratch, 0, data, 0, rowsHL + rowsHL);
  }

  /**
   * Given a set of data samples holding a discrete wavelet transform, calculate the inverse
   * discrete wavelet transform of these samples and return in the given <code>data</code> array.
   * Uses current wavelet basis and operates on the first N elements of the array where N is the
   * current transform size.
   */
  public void inverseDiscreteWT(float[] dwt, float[] data) {
    System.arraycopy(dwt, 0, data, 0, _transformSize);
    iwt(_maxLevelPlusOne, (int) MathsPower2.pow2(_maxLevelPlusOne), _transformSize, data);
  }

  /**
   * Given a set of data samples holding a discrete wavelet transform calculate the inverse discrete
   * wavelet transform of these samples and return in the given <code>data</code> array. Prior to
   * calculation, update the current transform size to be as given (or the nearest power of 2 to
   * this). Uses the current wavelet basis set and operates on the first N elements of the array
   * where N is the current transform size.
   */
  public void inverseDiscreteWT(int transformSize, float[] dwt, float[] data) {
    setTransformSize(transformSize);
    inverseDiscreteWT(dwt, data);
  }

  // Execute in-place calculation of the inverse discrete wavelet
  // transform

  private void iwt(int levelPlusOne, int levelPlusOnePow2, int rows, float[] data) {
    if (levelPlusOne > 0) {
      int halfRows = rows / 2;
      // Recursive calculation of inverse DWT
      iwt(levelPlusOne - 1, levelPlusOnePow2 >> 1, halfRows, data);
      // Multiply data by M and G matrices
      multiplyMG(levelPlusOne, levelPlusOnePow2, halfRows, data);
    }
  }

  // Multiply data by M and G matrices

  private void multiplyMG(int rows, int rowsPow2, int halfRows, float[] data) {
    float outputM, outputG;
    int columnsMG = rowsPow2 >> 1;
    int rowsMG = 2 * columnsMG;
    for (int row = 0; row < rowsMG; row++) {
      outputM = outputG = 0;
      for (int column = 0; column < columnsMG; column++) {
        outputM += (elementM(rowsPow2, row, column) * data[column]);
        outputG += (elementG(rowsPow2, row, column) * data[halfRows + column]);
      }
      _scratch[row] = outputM + outputG;
    }
    System.arraycopy(_scratch, 0, data, 0, rowsMG);
  }

  /**
   * Given a set of data samples holding a discrete wavelet transform return the wavelet of the
   * given scale level and place in the given <code>wavelet</code> array. If <code>level</code> is
   * -1 then return the scaling function. Uses the current wavelet basis set
   */

  // Example:
  // If the DWT of the data is c(0,_) d(0,0) d(1,0) d(1,1) d(2,0)
  // d(2,1) d(2,2) d(2,3) then the contribution of the scale level 1
  // wavelet can be found by performing an inverse DWT on 0 0 d(1,0)
  // d(1,1) 0 0 0 0

  public void wavelet(int level, float[] dwt, float[] wavelet) {
    sumWavelets(level, level, dwt, wavelet);
  }

  /**
   * Given a set of data samples holding a discrete wavelet transform calculate the sum of wavelets
   * from the scaling function to the given level and place in the given <code>summation</code>
   * array. If <code>level</code> is -1 then return the scaling function. Uses the current wavelet
   * basis set
   */

  // Example:
  // If the DWT of the data is c(0,_) d(0,0) d(1,0) d(1,1) d(2,0)
  // d(2,1) d(2,2) d(2,3) then the contribution of the wavelets from
  // scale levels -1 to 1 can be found by performing an inverse DWT
  // on c(0,_) d(0,0) d(1,0) d(1,1) 0 0 0 0

  public void summation(int level, float[] dwt, float[] summation) {
    sumWavelets(-1, level, dwt, summation);
  }

  // Given a set of data samples holding a discrete wavelet
  // transform place the sum of wavelets from the given minimum
  // level to the given maximum level in the summation array. Uses
  // the current wavelet basis set

  // Example:
  // If the DWT of the data is c(0,_) d(0,0) d(1,0) d(1,1) d(2,0)
  // d(2,1) d(2,2) d(2,3) then the contribution of the wavelets from
  // scale levels 1 to 2 can be found by performing an inverse DWT
  // on 0 0 d(1,0) d(1,1) d(2,0) d(2,1) d(2,2) d(2,3)

  private void sumWavelets(int minLevel, int maxLevel, float[] data, float[] summation) {
    // Ensure minLevel and maxLevel are within number of levels
    // supported by the  current transform size
    minLevel = Math.min(jToIndex(minLevel), _transformSize);
    maxLevel = Math.min((jToIndex(maxLevel + 1)), _transformSize);
    int length = summation.length;
    for (int i = 0; i < summation.length; i++) {
      summation[i] = 0;
    }
    System.arraycopy(data, minLevel, summation, minLevel, maxLevel - minLevel);
    inverseDiscreteWT(summation, summation);
  }

  /** Is the given scale level the scale level of the scaling function (which has level = -1) */
  public static boolean isScalingFunction(int level) {
    return (level == -1);
  }

  /**
   * Given a coefficient c(0) or d(j,k) of a discrete wavelet transform return the corresponding
   * index into a sequential list representation of the discrete wavelet transform.
   *
   * <p>Example: the index of d(2,1) is 5 if the sequential list view of a discrete wavelet
   * transform is c(0) d(0,0) d(1,0) d(1,1) d(2,0) d(2,1) d(2,2) d(2,3).
   *
   * <p>If <code>j</code> is -1 - denoting the scaling function - then 0 is returned (corresponding
   * to coefficient c(0))
   */
  public static int jkToIndex(int j, int k) {
    int index = 0;
    if (j > -1) {
      index = MathsPower2.pow2(j) + k;
    }
    return index;
  }

  /**
   * Given the j component of a coefficient c(0) or d(j,k) of a discrete wavelet transform return
   * the corresponding index into a sequential list representation of the discrete wavelet
   * transform.
   *
   * <p>Example: the j component of d(2,1) is 2 and the corresponding index into the sequential list
   * is 4 if the sequential list view of the discrete wavelet transform is c(0) d(0,0) d(1,0) d(1,1)
   * d(2,0) d(2,1) d(2,2) d(2,3).
   *
   * <p>If <code>j</code> is -1 - denoting the scaling function - then 0 is returned (corresponding
   * to coefficient c(0))
   *
   * <p>This method is the inverse of <code>indexToJ()</code>
   */
  public static int jToIndex(int j) {
    return jkToIndex(j, 0);
  }

  /**
   * Given an index into a sequential list representation of a discrete wavelet transform return the
   * j component of the corresponding DWT coefficient c(0) (which has j index -1) or d(j, k)
   *
   * <p>Example: if the sequential list view of the discrete wavelet transform is c(0) d(0,0) d(1,0)
   * d(1,1) d(2,0) d(2,1) d(2,2) d(2,3) then index 4 corresponds to coefficient d(2,0) which has j
   * component 2.
   *
   * <p>If <code>index</code> is 0 - denoting the scaling function - then j = -1 is returned
   * (corresponding to coefficient c(0))
   *
   * <p>This method is the inverse of <code>jToIndex()</code>
   */
  public static int indexToJ(int index) {
    int j = -1;
    if (index > 0) {
      j = MathsPower2.integerLog2(index);
    }
    return j;
  }

  /**
   * Given an index into a sequential list representation of a discrete wavelet transform return the
   * k component of the corresponding DWT coefficient c(0) or d(j,k)
   *
   * <p>Example: if the sequential list view of the discrete wavelet transform is c(0) d(0,0) d(1,0)
   * d(1,1) d(2,0) d(2,1) d(2,2) d(2,3) then index 4 corresponds to coefficient d(2,0) which has k
   * component 0.
   *
   * <p>If <code>index</code> is 0 - denoting the scaling function - then k = 0 is returned
   * (corresponding to coefficient c(0))
   */
  public static int indexToK(int index) {
    return index - (int) MathsPower2.roundPow2(index);
  }

  /**
   * Given a scale level return the number of DWT coefficients used to represent data at that scale
   * level.
   *
   * <p>Example: if the sequential list view of the discrete wavelet transform is c(0) d(0,0) d(1,0)
   * d(1,1) d(2,0) d(2,1) d(2,2) d(2,3) then 4 coefficients are used to represent data at scale
   * level 2.
   *
   * <p>If <code>level</code> is -1 (the scaling function) then 1 is returned.
   */
  public static int numCoeffsPerLevel(int level) {
    int numCoefficients = 1;
    if (level > -1) {
      numCoefficients = MathsPower2.pow2(level);
    }
    return numCoefficients;
  }

  /**
   * Given a number of DWT coefficients under the concatenated list view then return the maximum
   * wavelet scale level supported by these coefficients.
   *
   * <p>Example: if the sequential list view of the discrete wavelet transform is c(0) d(0,0) d(1,0)
   * d(1,1) d(2,0) d(2,1) d(2,2) d(2,3) then these 8 coefficients support a maximum scale level of
   * 2.
   */
  public static int maxLevel(int numCoefficients) {
    return MathsPower2.integerLog2(numCoefficients) - 1;
  }

  /**
   * Given a number of DWT coefficients under the concatenated list view then return the number of
   * scale levels supported by these coefficients.
   *
   * <p>Example: if the sequential list view of the discrete wavelet transform is c(0) d(0,0) d(1,0)
   * d(1,1) d(2,0) d(2,1) d(2,2) d(2,3) then these 8 coefficients support 4 scale levels: c(0)
   * (scaling function), d(0,0), d(1,0..1) and d(2,0..3) (level 0, 1 and 2 wavelets).
   */
  public static int numLevels(int numCoefficients) {
    return MathsPower2.integerLog2(numCoefficients) + 1;
  }
}
