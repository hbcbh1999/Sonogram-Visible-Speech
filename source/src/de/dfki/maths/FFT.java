package de.dfki.maths;


/**
 * (c) DFKI GmbH 26.9.2002
 *
 * <p>Orginal C sources: Author Mike Jackson - University of Edinburgh - 1999-2001
 *
 * <p>The <code>FFT</code> class provides static methods supporting the performance of in-place
 * mixed-radix Fast Fourier Transforms. This is an implementation and extension of a radix-2 FFT
 * algorithm in "Numerical Recipes in Pascal", W.H.Press, B.P. Flannery, S.A. Taukolsky, and W.T.
 * Vetterling, Cambridge University Press, 1989.
 *
 * @author Christoph Lauer
 * @version 1.0
 */
public class FFT {
  /** Common constant */
  private static final float TWOPI = (float) (Math.PI * 2);

  /**
   * Sort a set of <code>Complex</code> numbers into a bit-reversed order - only sort the first
   * <code>n</code> elements. This method performs the sort in-place
   */
  public static void bitReverse(Complex[] complex, int n) {
    int halfN = n / 2;
    int i, j, m;
    Complex temp;
    for (i = j = 0; i < n; ++i) {
      if (j >= i) {
        temp = complex[j];
        complex[j] = complex[i];
        complex[i] = temp;
      }
      m = halfN;
      while (m >= 1 && j >= m) {
        j -= m;
        m /= 2;
      }
      j += m;
    }
    temp = null;
  }

  /**
   * Perform an in-place mixed-radix Fast Fourier Transform on the first <code>n</code> elements of
   * the given set of <code>Complex</code> numbers. If <code>n</code> is not a power of two then the
   * FFT is performed on the first N numbers where N is largest power of two less than <code>n
   * </code>
   */
  public static void fft(Complex[] complex, int n) {
    fft(1, complex, n);
  }

  /**
   * Perform an in-place mixed-radix inverse Fast Fourier Transform on the first <code>n</code>
   * elements of the given set of <code>Complex</code> numbers. If <code>n</code> is not a power of
   * two then the inverse FFT is performed on the first N numbers where N is largest power of two
   * less than <code>n</code>
   */
  public static void inverseFFT(Complex[] complex, int n) {
    fft(-1, complex, n);
  }

  /**
   * erform an in-place mixed-radix FFT (if sign is 1) or inverse FFT (if sign is -1) on the first n
   * elements of the given set of Complex numbers. Round n to the nearest power of two. This method
   * performs the FFT in-place on the given set.
   */
  private static void fft(int sign, Complex[] complex, int n) {
    // n is number of data elements upon which FFT will be
    // performed. Round number of data elements to nearest power
    // of 2
    n = (int) MathsPower2.roundPow2(n);
    // Sort the first n elements into bit-reversed order
    bitReverse(complex, n);
    if (n == 2) {
      // Perform a radix-2 FFT
      radix2FFT(sign, complex, n, 0);
    } else if (((float) Math.log(n) % (float) Math.log(4)) == 0) {
      // Perform a radix-4 FFT
      radix4FFT(sign, complex, n, 0);
    } else {
      // n is a multiple or two or four [8, 32, 128, ...]
      // Perform a mixed-radix FFT
      int halfN = n / 2;
      // Do a radix-4 transform on elements 0..halfN - 1 which
      // contains even-indexed elements from the original
      // unsorted set of numbers by definition of the bit
      // reversal operation
      radix4FFT(sign, complex, halfN, 0);
      // Do a radix-4 transform on elements halfN - 1 .. n - 1
      // which contains odd-indexed elements from the original
      // unsorted set of numbers by definition of the bit
      // reversal operation
      radix4FFT(sign, complex, halfN, halfN);
      // Pair off even and odd elements and do final radix-2
      // transforms, multiplying by twiddle factors as required
      // Loop variables used to point to pairs of even and odd
      // elements
      int g, h;
      // Array of two complex numbers for performing radix-2
      // FFTs on pairs of elements
      Complex[] radix2x2 = new Complex[2];
      // Twiddle factor
      Complex twiddle = new Complex();
      // Values defining twiddle factor
      float delta = -sign * TWOPI / n;
      float w = 0;
      for (g = 0, h = halfN; g < halfN; g++, h++) {
        // Twiddle factors...
        twiddle.setRealImag((float) Math.cos(w), (float) Math.sin(w));
        complex[h].multiply(twiddle);
        radix2x2[0] = complex[g];
        radix2x2[1] = complex[h];
        // Perform the radix-2 FFT
        radix2FFT(sign, radix2x2, 2, 0);
        complex[g] = radix2x2[0];
        complex[h] = radix2x2[1];
        w += delta;
      }
      radix2x2 = null;
      twiddle = null;
    }
    if (sign == -1) {
      // Divide all values by n
      for (int g = 0; g < n; g++) {
        complex[g].divide(n);
      }
    }
  }
  /**
   * Perform an in-place radix-4 FFT (if sign is 1) or inverse FFT (if sign is -1). FFT is performed
   * in the n elements starting at index lower Assumes that n is a power of 2 and that lower + n is
   * less than or equal to the number of complex numbers given This method performs the FFT in-place
   * on the given set.
   */
  private static void radix4FFT(int sign, Complex[] complex, int n, int lower) {
    // Index of last element in array which will take part in the
    // FFT
    int upper = n + lower;
    // Variables used to hold the indicies of the elements forming
    // the four inputs to a butterfly
    int i, j, k, l;
    // Variables holding the results of the four main operations
    // performed when processing a butterfly
    Complex ijAdd = new Complex();
    Complex klAdd = new Complex();
    Complex ijSub = new Complex();
    Complex klSub = new Complex();
    // Twiddle factor
    Complex twiddle = new Complex();
    // Values defining twiddle factor
    float delta, w, w2, w3;
    float deltaLower = -(float) sign * TWOPI;
    // intraGap is number of array elements between the
    // two inputs to a butterfly (equivalent to the number of
    // butterflies in a cluster)
    int intraGap;
    // interGap is the number of array elements between the first
    // input of the ith butterfly in two adjacent clusters
    int interGap;
    for (intraGap = 1, interGap = 4 * intraGap;
        intraGap < n;
        intraGap = interGap, interGap = 4 * intraGap) {
      delta = deltaLower / (float) interGap;
      // For each butterfly in a cluster
      w = w2 = w3 = 0;
      for (int but = 0; but < intraGap; ++but) {
        // Process the intraGap-th butterfly in each cluster
        // i is the top input to a butterfly and j the second,
        // k third and l fourth
        for (i = (but + lower), j = i + intraGap, k = j + intraGap, l = k + intraGap;
            i < upper;
            i += interGap, j += interGap, k += interGap, l += interGap) {
          // Calculate and apply twiddle factors
          // cos(0) = 1 and sin(0) = 0
          twiddle.setRealImag(1, 0);
          complex[i].multiply(twiddle);
          twiddle.setRealImag((float) Math.cos(w2), (float) Math.sin(w2));
          complex[j].multiply(twiddle);
          twiddle.setRealImag((float) Math.cos(w), (float) Math.sin(w));
          complex[k].multiply(twiddle);
          twiddle.setRealImag((float) Math.cos(w3), (float) Math.sin(w3));
          complex[l].multiply(twiddle);
          // Compute the butterfly
          Complex.add(complex[i], complex[j], ijAdd);
          Complex.subtract(complex[i], complex[j], ijSub);
          Complex.add(complex[k], complex[l], klAdd);
          Complex.subtract(complex[k], complex[l], klSub);
          // Assign values
          Complex.add(ijAdd, klAdd, complex[i]);
          klSub.multiply(sign);
          complex[j].setRealImag(
              ijSub.getReal() + klSub.getImag(), ijSub.getImag() - klSub.getReal());
          Complex.subtract(ijAdd, klAdd, complex[k]);
          complex[l].setRealImag(
              ijSub.getReal() - klSub.getImag(), ijSub.getImag() + klSub.getReal());
        }
        w += delta;
        w2 = w + w;
        w3 = w2 + w;
      }
      intraGap = interGap;
    }
    ijAdd = klAdd = ijSub = klSub = twiddle = null;
  }

  /**
   * Perform an in-place radix-2 FFT (if sign is 1) or inverse FFT (if sign is -1). FFT is performed
   * in the n elements starting at index lower Assumes that n is a power of 2 and that lower + n is
   * less than or equal to the number of complex numbers given... This method performs the FFT
   * in-place on the given set.
   */
  private static void radix2FFT(int sign, Complex[] complex, int n, int lower) {
    // Index of last element in array which will take part in the
    // FFT
    int upper = n + lower;
    // Variables used to hold the indicies of the elements forming
    // the two inputs to a butterfly
    int i, j;
    // intraGap is number of array elements between the
    // two inputs to a butterfly (equivalent to the number of
    // butterflies in a cluster)
    int intraGap;
    // interGap is the number of array elements between the first
    // input of the ith butterfly in two adjacent clusters
    int interGap;
    // The twiddle factor
    Complex twiddle = new Complex();
    // Values defining twiddle factor
    float deltaLower = -(float) (sign * Math.PI);
    float w, delta;
    // Variable used to hold result of multiplying butterfly input
    // by a twiddle factor
    Complex twiddledInput = new Complex();
    for (intraGap = 1, interGap = intraGap + intraGap;
        intraGap < n;
        intraGap = interGap, interGap = intraGap + intraGap) {
      delta = deltaLower / (float) intraGap;
      // For each butterfly in a cluster
      w = 0;
      for (int butterfly = 0; butterfly < intraGap; ++butterfly) {
        // Calculate the twiddle factor
        twiddle.setRealImag((float) Math.cos(w), (float) Math.sin(w));
        // i is the top input to a butterfly and j the
        // bottom
        for (i = (butterfly + lower), j = i + intraGap; i < upper; i += interGap, j += interGap) {
          // Calculate the butterfly-th butterfly in
          // each cluster
          // Apply the twiddle factor
          Complex.multiply(complex[j], twiddle, twiddledInput);
          // Subtraction part of butterfly
          Complex.subtract(complex[i], twiddledInput, complex[j]);
          // Addition part of butterfly
          complex[i].add(twiddledInput);
        }
        w += delta;
      }
      intraGap = interGap;
    }
    twiddle = twiddledInput = null;
  }
}
