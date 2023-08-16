package de.dfki.maths;
/**
 * (c) Christoph Lauer Engineering
 *
 * @file smoothed_speudo_wigner_ville_distribution.h
 * @class SmoothedPseudoWignerVilleDistribution
 * @version 1.0
 * @date 2008
 * @author Christoph Lauer
 * @brief This class implements a smoothed-pseudo-wigner-ville-distribution (SPWVD)
 * @see http://en.wikipedia.org/wiki/Wigner_quasi-probability_distribution
 * @see http://en.wikipedia.org/wiki/Cohen's_class_distribution_function
 * @todo finished and tested so far.
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.ProgressMonitor;

public class SmoothedPseudoWignerVilleDistribution {
  /**
   * To prevent aliasing the real input signal must be band limited with the nyquist frequency SR/2.
   * The discrtete wigner ville distribution has a band limitation of SR/4, so normally for complex
   * signals the input time domain vector must be up samples to the double length. In our case we
   * have a real input signal, so we use the complex transformation with the analytic signal as
   * input which prevents aliasing at the frequency SR/4 and the mirroring of the image. For this
   * case we can switch between the transformation with the analytic signal ans the real input
   * signal. The upsampling method only required for complex input signals which is not part of out
   * implementation.
   */
  public enum SPWVD_CALCULATION_METHOD {
    // The standard real transformation has a bandlimit of SR/4. The image will be mirrored in the
    // middle.
    // with the aliasing frequency SR/4.
    SPWVD_CALCULATION_METHOD_REAL,
    // With help of the hilbert tranformation the complex analytic signal can be transformated and
    // the
    // resulting image no mirroring in the middle. This method will further depress the
    // interferences
    // from the local autocorrelation functions into the spwvd calculation function.The max.
    // frequency
    // is the nyquist frequency SR/2.
    SPWVD_CALCULATION_METHOD_COMPLEX_ANALYTIC_FUNCTION,
  }

  // ------------------------------------------------------------------------------------------------

  public ProgressMonitor pm = null;
  static int currentColumn = 0;

  // ------------------------------------------------------------------------------------------------

  // This group of structures hold the input and out structures for the transformation
  // This struct represents the time signal including the metadata for used internally for the
  // transformation.
  private class signalRepresentation {
    public int length; // Length of the signal in points
    public boolean is_complex; // true for complex signals
    public double[] real_part; // real part of the signal
    public double[] imag_part; // imaginary part of the signal
  }

  // This stuct hold the resulting time frequency representation and all the metadata. The resulting
  // value of the wigner ville transformation is stored into the real part row by row. The struct is
  // only internally used.
  private class timeFrequencyRepresentation {
    public int N_freq; // number of freq bins in the TFR matrix
    public int N_time; // number of time_bins in the TFR matrix
    public double[] time_instants; // instant for each column of the TFR
    public double[][] spwvd; // the resulting TFR
  }
  // the static instance of the result is used fro both functions
  public timeFrequencyRepresentation tfr;

  // ------------------------------------------------------------------------------------------------

  /**
   * This is the public visible wrapper function for the "Discrete Smoothed Pseudo Discrete
   * Wigner-Ville -Distribution" which the user of the function can call. The function takes the
   * samples, the length of the samples and the frequency resolution which should be from a power of
   * two. The frequency resolution can be a value which is not a power of two, but then the DFT is
   * used instead of the much more faster FFT which has a dramatically influence on the calculation
   * time. Two main methods are implemented which have different frequency resolution
   * characteristics in relation to the nyquist frequency. The standard normal
   * wigner-ville-distribution has a frequency resolution of SR/4 which means for the calculated
   * image a aliasing effect in the middle of the image because the image is calculated over the
   * frequency range from 0…SR/2. As alternative to the standard real SPWVD the complex analytic
   * function with can be transformated. The complex analytic function can be calculated via the
   * hilbert transformation the the resulting spwvd will not have the restriction to SR/4 and the
   * aliasing mirror effect in the middle of the screent. The standard real transformation should
   * only be used if it is known that the sample data input vector has a band limitation of SR/4.
   * Otherwise the extended calculation method via the complex hilbert analyti function should be
   * used to get the best frequency resolution and no antialiasing and mirroring effects in the
   * middle of the image.
   *
   * <p>/ / -j2pi f tau SPWV(t,f) = | h(tau) | g(tau-t) x(mu+tau/2)x*(mu-tau/2)e dmu dtau / /
   *
   * @param signal The pointer to the signal time domain input vector.
   * @param length The length of the signal time domain input vector.
   * @param resolution For performance reasons the required resolution shoule be, but must not have
   *     a power of two. A resolution from a power of two dramatically influences the calculation
   *     speed.
   * @param method The standard complex transformation transformates with the help of an hilbert
   *     transformator the real input signal into its analytic representation and bypasses so the
   *     WVD-aliasing frequency from SR/4 which efects the aliasing mirroring on the image in the
   *     middle of the image. With the help of the complex implemented SPWVD, which will be called
   *     normally with the analystic representation the distribution is generated with the aliasing
   *     frequency SR/2. The real method can be taken if it's known that the signal is bandlimited
   *     with SR/4. middle of the image with SR/4. The complex transformation with the analytic
   *     function prevents aliasing and has no mirror effect in the middle of the image.
   * @return The resulting time-frequency representation is a two dimensional array from the size
   *     length*resolution and can be accessed via spevd[time][frequency]. Note that the array will
   *     be allocated into this function so no pre allocation is necessary. Note that the
   *     deallocation of this structure every single time bin and the time bin pointer vector itselv
   *     must be deallocated.
   */
  public double[][] calculateSmoothedPseudoWignerVilleDistribution(
      double[] signal, int length, int resolution, int accuracy) {
    SPWVD_CALCULATION_METHOD method =
        SPWVD_CALCULATION_METHOD.SPWVD_CALCULATION_METHOD_COMPLEX_ANALYTIC_FUNCTION;
    // first initialize the time domain input structure
    signalRepresentation input = new signalRepresentation();
    input.real_part = signal;
    input.length = length;
    input.is_complex = false;

    // in case the analytic response is reqired
    if (method == SPWVD_CALCULATION_METHOD.SPWVD_CALCULATION_METHOD_COMPLEX_ANALYTIC_FUNCTION) {
      input.imag_part = fastHilbertTransform(signal, length);
      input.is_complex = true;
    }

    // next the output time-frequency-representation structure
    tfr = new timeFrequencyRepresentation();
    tfr.N_time = length;
    tfr.N_freq = resolution;
    tfr.time_instants = new double[length];
    tfr.spwvd = new double[length][resolution];

    // normally we have a linear aligned time domain input with equally spaced time points
    for (int i = 0; i < length; i++) tfr.time_instants[i] = (double) (i);

    double correctFact = (double) resolution / accuracy;
    // System.out.println(accuracy);

    // prepare the time window function --> default : gaussian window of length : the next odd
    // number after Signal_Length/10
    int WindowT_Length = (int) (resolution / 20.0 / correctFact);
    if (WindowT_Length % 2 == 0) WindowT_Length++;
    double[] WindowT = new double[WindowT_Length];
    for (int i = 0; i < WindowT_Length; i++) WindowT[i] = 1.0;
    WindowT = applyHammingWindow(WindowT, WindowT_Length);
    WindowT = normalizeAvg(WindowT, WindowT_Length);

    // prepare the frequency window function --> default : gaussian window of length : the next odd
    // number after Signal_Length/4
    int WindowF_Length = (int) ((double) resolution / 4 / correctFact);
    if (WindowF_Length % 2 == 0) WindowF_Length++;
    double[] WindowF = new double[WindowF_Length];
    for (int i = 0; i < WindowF_Length; i++) WindowF[i] = 1.0;
    WindowF = applyHammingWindow(WindowF, WindowF_Length);
    WindowF = normalizeAvg(WindowF, WindowF_Length);

    // call the core algorithm
    algorithmSmoothedPseudoWignerVilleDistribution(
        input, WindowT, WindowT_Length, WindowF, WindowF_Length);
    // call the grabage collector
    System.gc();
    // finally give the result back
    return tfr.spwvd;
  }

  // ------------------------------------------------------------------------------------------------

  /**
   * This function implements the algorithmus for the Smoothed Pseudo-Wigner-Ville Distribution
   * (SPWVD).
   *
   * <p>/ / -j2pi f tau SPWVD(t,f) = | h(tau) | g(tau-t) x(mu+tau/2)x*(mu-tau/2)e dmu dtau / /
   *
   * <p>This function is real valued. Its computation requires a real or complex signal, a vector
   * containing time instants, the number of frequency bins, a time smoothing window and a frequency
   * smoothing window.
   *
   * @param a_Signal This struct holds the signal input data. This is the signal to analyse.
   * @param a_WindowT Vector containing the points of the time moothing window.
   * @param a_windowT_Length Number of points of the time window (ODD number !).
   * @param a_WindowF Vector containing the points of the frequency window.
   * @param a_WindowF_length Number of points of the window (ODD number !). param tfr Matrix
   *     containing the resulting TFR (real). param tfr.time_instants positions of the smoothing
   *     window. param tfr.N_time length of '.time_instants' = number of cols. in the tfr matrix.
   *     param tfr.N_freq Number of frequency bins = number of rows in the tfr matrix. return
   *     tfr.real_part The output tfr matrix (real_part).
   *     <p>The following variables are used intenally into the function. remarks column, row
   *     variables of displacement in the matrices. remarks time local time-instant variable to
   *     compute the tfr. remarks half_WindowT_Length half-length of the time smoothing window.
   *     remarks normT normalization factor for the time smoothing window. remarks
   *     half_WindowF_Length half-length of the frequency smoothing window. remarks normF
   *     normalization factor for the frequency window. remarks tau time-lag variable. remarks
   *     taumax accound the beginning and the end of the signal, where the window is cut. remarks mu
   *     time-smoothing variable. remarks mumin local time-smoothing variable bounds. Used to take.
   *     remarks mumax into accound the beginning and the end of time smoothing procedure. remarks
   *     lacf_real Real and imaginary parts of the local autocorrelation. remarks lacf_imag Function
   *     of the signal. remarks R1_real R1_imag Used to compute real and imaginary parts of the time
   *     remarks R1_real R1_imag Smoothed-windowed local autocorrelation function. remarks R2_real
   *     R2_imag
   */
  private void algorithmSmoothedPseudoWignerVilleDistribution(
      signalRepresentation a_Signal,
      double[] a_WindowT,
      int a_WindowT_Length,
      double[] a_WindowF,
      int a_WindowF_Length) {

    WindowT = a_WindowT;
    WindowT_Length = a_WindowT_Length;
    WindowF = a_WindowF;
    WindowF_Length = a_WindowF_Length;
    Signal = a_Signal;

    // init the local values
    int column;
    int row;
    double normF;

    // determines some internal constants
    half_WindowT_Length = (WindowT_Length - 1) / 2;
    half_WindowF_Length = (WindowF_Length - 1) / 2;
    normF = WindowF[half_WindowF_Length];
    for (row = 0; row < WindowF_Length; row++) WindowF[row] = WindowF[row] / normF;
    // memory allocation and init. of the local autocorrelation fuction

    /////////////////////////////////////
    // START MULTITHREADING HERE
    /////////////////////////////////////
    Runtime runtime = Runtime.getRuntime();
    int NTHREADS = runtime.availableProcessors();
    System.out.println("--> Max Number of parallel SPWVD Threads: " + NTHREADS);
    ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
    // start the threads
    for (column = 0;
        column < tfr.N_time;
        column++) // the main loop over the number of column lines
    {
      // the core calc algo
      CalcThread thread = new CalcThread();
      thread.column = column;
      executor.execute(thread);
      if (pm.isCanceled() == true) return;
    }
    // wait until all threads have finished
    executor.shutdown();
    pm.setNote("calculate SPWVD (" + NTHREADS + " cores)...");
    while (!executor.isTerminated()) // update the progress
    {
      // set the progress
      if (pm != null) {
        pm.setProgress((int) (((double) currentColumn / (double) tfr.N_time) * 40));
        if (pm.isCanceled() == true) return;
      }
    }
    /////////////////////////////////////
    // END MULTITHREADING HERE
    /////////////////////////////////////

  } // end of function algorithmSmoothedPseudoWignerVilleDistribution(...)

  // shared variables for the threads
  private int half_WindowT_Length;
  private int half_WindowF_Length;
  private double[] WindowT;
  private int WindowT_Length;
  private double[] WindowF;
  private int WindowF_Length;
  private signalRepresentation Signal;

  // ------------------------------------------------------------------------------------------------

  /**
   * This class represents the outcapseled calculation routines for the parallelization with the
   * java executor service.
   */
  class CalcThread extends Thread {
    public int column;

    public CalcThread() {
      setPriority(Thread.MIN_PRIORITY);
    }

    public void run() {
      currentColumn = column;
      // local variables
      int row;
      double R0_real;
      double R0_imag;
      double R1_real;
      double R1_imag;
      double R2_real;
      double R2_imag;
      int time;
      int taumax;
      int tau;
      int mumin;
      int mumax;
      int mu;
      double[] lacf_real; // the local autocorrelation function
      double[] lacf_imag;
      lacf_real = new double[tfr.N_freq];
      lacf_imag = new double[tfr.N_freq];
      double normT;

      // time instants of interest to compute the tfr
      time = ((int) tfr.time_instants[column]);
      taumax =
          Math.min((time + half_WindowT_Length), (Signal.length - time - 1 + half_WindowT_Length));
      taumax = Math.min(taumax, (tfr.N_freq / 2 - 1));
      taumax = Math.min(taumax, half_WindowF_Length);
      // determination of the begin and end of mu
      mumin = Math.min(half_WindowT_Length, (Signal.length - time - 1));
      mumax = Math.min(half_WindowT_Length, time);

      // normalization of the time-smoothing window

      // the window norm
      normT = 0;
      for (row = -mumin; row <= mumax; row++) normT = normT + WindowT[half_WindowT_Length + row];

      R0_real = 0.0;
      R0_imag = 0.0;
      for (mu = -mumin; mu <= mumax; mu++) {
        if (Signal.is_complex == true)
          R0_real =
              R0_real
                  + (Signal.real_part[time - mu] * Signal.real_part[time - mu]
                          + Signal.imag_part[time - mu] * Signal.imag_part[time - mu])
                      * WindowT[half_WindowT_Length + mu]
                      / normT;
        else
          R0_real =
              R0_real
                  + Signal.real_part[time - mu]
                      * Signal.real_part[time - mu]
                      * WindowT[half_WindowT_Length + mu]
                      / normT;
      }
      lacf_real[0] = R0_real;
      lacf_imag[0] = R0_imag;

      // The signal is windowed around the current time
      for (tau = 1; tau <= taumax; tau++) {
        if (pm.isCanceled() == true) return;
        R1_real = 0;
        R2_real = 0;
        R1_imag = 0;
        R2_imag = 0;
        mumin = Math.min(half_WindowT_Length, (Signal.length - time - 1 - tau));
        mumax = Math.min(half_WindowT_Length, time - tau);
        // window norm
        normT = 0;
        for (row = -mumin; row <= mumax; row++) normT = normT + WindowT[half_WindowT_Length + row];
        for (mu = -mumin; mu <= mumax; mu++) {
          if (Signal.is_complex == true) {
            R1_real =
                R1_real
                    + (Signal.real_part[time + tau - mu] * Signal.real_part[time - tau - mu]
                            + Signal.imag_part[time + tau - mu] * Signal.imag_part[time - tau - mu])
                        * WindowT[half_WindowT_Length + mu]
                        / normT;
            R1_imag =
                R1_imag
                    + (Signal.imag_part[time + tau - mu] * Signal.real_part[time - tau - mu]
                            - Signal.real_part[time + tau - mu] * Signal.imag_part[time - tau - mu])
                        * WindowT[half_WindowT_Length + mu]
                        / normT;
            R2_real =
                R2_real
                    + (Signal.real_part[time - tau - mu] * Signal.real_part[time + tau - mu]
                            + Signal.imag_part[time - tau - mu] * Signal.imag_part[time + tau - mu])
                        * WindowT[half_WindowT_Length + mu]
                        / normT;
            R2_imag =
                R2_imag
                    + (Signal.imag_part[time - tau - mu] * Signal.real_part[time + tau - mu]
                            - Signal.real_part[time - tau - mu] * Signal.imag_part[time + tau - mu])
                        * WindowT[half_WindowT_Length + mu]
                        / normT;
          } else {
            R1_real =
                R1_real
                    + (Signal.real_part[time + tau - mu] * Signal.real_part[time - tau - mu])
                        * WindowT[half_WindowT_Length + mu]
                        / normT;
            R1_imag = 0.0;
            R2_real =
                R2_real
                    + (Signal.real_part[time - tau - mu] * Signal.real_part[time + tau - mu])
                        * WindowT[half_WindowT_Length + mu]
                        / normT;
            R2_imag = 0.0;
          }
        }
        lacf_real[tau] = R1_real * WindowF[half_WindowF_Length + tau];
        lacf_imag[tau] = R1_imag * WindowF[half_WindowF_Length + tau];
        lacf_real[tfr.N_freq - tau] = R2_real * WindowF[half_WindowF_Length - tau];
        lacf_imag[tfr.N_freq - tau] = R2_imag * WindowF[half_WindowF_Length - tau];
      }
      tau = (int) (Math.floor((double) (tfr.N_freq) / 2.0));
      if ((time <= Signal.length - tau - 1) & (time >= tau) & (tau <= half_WindowF_Length)) {
        mumin = Math.min(half_WindowT_Length, (Signal.length - time - 1 - tau));
        mumax = Math.min(half_WindowT_Length, time - tau);

        normT = 0;
        for (row = -mumin; row <= mumax; row++) normT = normT + WindowT[half_WindowT_Length + row];
        R1_real = 0;
        R2_real = 0;
        R1_imag = 0;
        R2_imag = 0;
        for (mu = -mumin; mu <= mumax; mu++) {
          if (pm.isCanceled() == true) return;
          if (Signal.is_complex == true) {
            R1_real =
                R1_real
                    + (Signal.real_part[time + tau - mu] * Signal.real_part[time - tau - mu]
                            + Signal.imag_part[time + tau - mu] * Signal.imag_part[time - tau - mu])
                        * WindowT[half_WindowT_Length + mu]
                        / normT;
            R1_imag =
                R1_imag
                    + (Signal.imag_part[time + tau - mu] * Signal.real_part[time - tau - mu]
                            - Signal.real_part[time + tau - mu] * Signal.imag_part[time - tau - mu])
                        * WindowT[half_WindowT_Length + mu]
                        / normT;
            R2_real =
                R2_real
                    + (Signal.real_part[time - tau - mu] * Signal.real_part[time + tau - mu]
                            + Signal.imag_part[time - tau - mu] * Signal.imag_part[time + tau - mu])
                        * WindowT[half_WindowT_Length + mu]
                        / normT;
            R2_imag =
                R2_imag
                    + (Signal.imag_part[time - tau - mu] * Signal.real_part[time + tau - mu]
                            - Signal.real_part[time - tau - mu] * Signal.imag_part[time + tau - mu])
                        * WindowT[half_WindowT_Length + mu]
                        / normT;
          } else {
            R1_real =
                R1_real
                    + (Signal.real_part[time + tau - mu] * Signal.real_part[time - tau - mu])
                        * WindowT[half_WindowT_Length + mu]
                        / normT;
            R1_imag = 0.0;
            R2_real =
                R2_real
                    + (Signal.real_part[time - tau - mu] * Signal.real_part[time + tau - mu])
                        * WindowT[half_WindowT_Length + mu]
                        / normT;
            R2_imag = 0.0;
          }
        }
        lacf_real[tau] =
            0.5
                * (R1_real * WindowF[half_WindowF_Length + tau]
                    + R2_real * WindowF[half_WindowF_Length - tau]);
        lacf_imag[tau] =
            0.5
                * (R1_imag * WindowF[half_WindowF_Length + tau]
                    + R2_imag * WindowF[half_WindowF_Length - tau]);
      }
      // call the Fourier Transformation
      FFT fft = new FFT(ld(tfr.N_freq));
      fft.doFFT(lacf_real, lacf_imag, false);
      // put the resulting fft magspec into the tfr matrix
      for (row = 0; row < tfr.N_freq; row++) {
        tfr.spwvd[column][row] =
            Math.sqrt(lacf_real[row] * lacf_real[row] + lacf_imag[row] * lacf_imag[row]);
        if (pm.isCanceled() == true) return;
      }
    } // end of function run
  } // end class CalcThread

  // ------------------------------------------------------------------------------------------------

  private int ld(int n) {
    return (int) (Math.log(n) / Math.log(2.0));
  }

  // ------------------------------------------------------------------------------------------------

  private int getNextPowerOfTwo(int value) {
    // calc the next power of two
    int n = 1;
    while (n < value) {
      n = n << 1; // the short form for the power of two
    }
    return n;
  }

  // ------------------------------------------------------------------------------------------------

  private double[] autoZeroPadding(double[] in, int len) {
    // first find the next bigger power of two value
    int newLen = getNextPowerOfTwo(len);
    // next allocate the new array
    double[] out = new double[newLen];
    System.arraycopy(in, 0, out, 0, len);
    return out;
  }

  // ------------------------------------------------------------------------------------------------

  private double[] fastHilbertTransform(double[] timeDomain, int length) {
    // forst prepare some values for the transformation
    double[] reTd = new double[length];
    double[] imTd = new double[length];
    double[] reFd;
    double[] imFd;
    // copy the time domain array
    System.arraycopy(timeDomain, 0, reTd, 0, length);
    // FASTEN YOUR SEAT BELLS, we now transform the signal into the frequency domain
    FFT fft = new FFT(ld(length));
    fft.doFFT(reTd, imTd, false);
    reFd = reTd;
    imFd = imTd;
    // now apply the hilbert transformation into the frequency domain cooresponding the
    // formular "h(x(f)) = x(f) * (-j) * sign(f)" in the complex frequency domain.
    // note that in our special case, we zero out the DC and Nyquist components.
    for (int i = 0; i < length; i++) {
      // first calculate the signum of the frequency for the hilbert transformation
      double sign = 0.0;
      if (i < length / 2)
        sign = 1.0; // for positive frequencies multiply the positive harmonics by j<
      else sign = -1.0; // for negative freuqencyies multiply the negative harmonics by -j
      if (i == 0) sign = 0.0; // zero out the zeroth component
      if (i == length / 2) sign = 0.0; // zero out the Nyquist component

      // now apply the "sign(f) * (-j)" formular for the hibert transformation in the complex
      // freuqncy domain
      double newRe = sign * imFd[i];
      double newIm = -sign * reFd[i];

      // copy the resulting values
      reFd[i] = newRe;
      imFd[i] = newIm;
    }
    // BACK TO EARTH, we transform the value back to the time domain
    fft.doFFT(reFd, imFd, true);
    // give the result back
    return reFd;
  }

  // ------------------------------------------------------------------------------------------------

  private double[] applyHammingWindow(double[] samples, int N) {
    double[] out = new double[N];
    for (int n = 0; n < N; n++)
      out[n] =
          samples[n]
              * (25.0 / 46.0
                  - (21.0 / 46.0 * Math.cos(2.0 * Math.PI * (double) (n + 0.5) / (double) N)));
    return out;
  }

  // ------------------------------------------------------------------------------------------------

  private double[] normalizeAvg(double[] samples, int length) {
    // first calcluate the average value
    double sum = 0.0;
    for (int i = 0; i < length; i++) {
      sum += samples[i];
    }
    double avg = sum / (double) (length);
    double[] out = new double[length];
    for (int i = 0; i < length; i++) out[i] = samples[i] / avg;
    return out;
  }

  // ------------------------------------------------------------------------------------------------

  private class FFT {
    /**
     * This is a Java implementation of the fast Fourier transform written by Jef Poskanzer. The
     * copyright appears above.
     */
    private static final double TWOPI = 2.0 * Math.PI;
    // Limits on the number of bits this algorithm can utilize
    private static final int LOG2_MAXFFTSIZE = 15;
    private static final int MAXFFTSIZE = 1 << LOG2_MAXFFTSIZE;
    // Private class data
    private int bits;
    private int[] bitreverse = new int[MAXFFTSIZE];

    /**
     * FFT class constructor Initializes code for doing a fast Fourier transform
     *
     * @param bits is a power of two such that 2^b is the number of samples.
     */
    public FFT(int bits) {
      this.bits = bits;
      if (bits > LOG2_MAXFFTSIZE) {
        System.out.println("" + bits + " is too big");
        System.exit(1);
      }
      for (int i = (1 << bits) - 1; i >= 0; --i) {
        int k = 0;
        for (int j = 0; j < bits; ++j) {
          k *= 2;
          if ((i & (1 << j)) != 0) k++;
        }
        this.bitreverse[i] = k;
      }
    }

    /**
     * A fast Fourier transform routine
     *
     * @param xr is the real part of the data to be transformed
     * @param xi is the imaginary part of the data to be transformed (normally zero unless inverse
     *     transofrm is effect).
     * @param invFlag which is true if inverse transform is being applied. false for a forward
     *     transform.
     */
    public void doFFT(double[] xr, double[] xi, boolean invFlag) {
      int n, n2, i, k, kn2, l, p;
      double ang, s, c, tr, ti;

      n2 = (n = (1 << this.bits)) / 2;

      for (l = 0; l < this.bits; ++l) {
        for (k = 0; k < n; k += n2) {
          for (i = 0; i < n2; ++i, ++k) {
            p = this.bitreverse[k / n2];
            ang = TWOPI * p / n;
            c = Math.cos(ang);
            s = Math.sin(ang);
            kn2 = k + n2;

            if (invFlag) s = -s;

            tr = xr[kn2] * c + xi[kn2] * s;
            ti = xi[kn2] * c - xr[kn2] * s;

            xr[kn2] = xr[k] - tr;
            xi[kn2] = xi[k] - ti;
            xr[k] += tr;
            xi[k] += ti;
          }
        }
        n2 /= 2;
      }

      for (k = 0; k < n; k++) {
        if ((i = this.bitreverse[k]) <= k) continue;

        tr = xr[k];
        ti = xi[k];
        xr[k] = xr[i];
        xi[k] = xi[i];
        xr[i] = tr;
        xi[i] = ti;
      }

      // Finally, multiply each value by 1/n, if this is the forward
      // transform.
      if (!invFlag) {
        double f = 1.0 / n;

        for (i = 0; i < n; i++) {
          xr[i] *= f;
          xi[i] *= f;
        }
      }
    }
  }

  // ------------------------------------------------------------------------------------------------

} // class SmoothedPseudoWignerVilleDistribution
