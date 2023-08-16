package de.dfki.maths;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>This class search for peaks in the given vector. It searches the highest and the second
 * highest peak too. The algorithm is based on the first derivation of the given float vector.
 *
 * @author Christoph Lauer
 * @version 1.0, Begin 16/10/2001
 */
public class PeakSearcher {
  private int highestpeakpos = -1;
  private int secondhighestpeakpos = -1;
  private int localmaxcounter = 2; // 0 and 1 are reserved...
  private int[] localmaximas;

  /**
   * Starts the peak search algorithm for the given float vector.
   *
   * @param float[] vector - the float array to check for local maximas.
   * @param float underborder - the under border of the amplitude for searching. Values under this
   *     number are ignored. (typical 0.2)
   * @param int beginsearchborder - defines the border in which the peaks are searched. FOr example:
   *     If at the beginning are some peaks that should not be founded this value can be set to 10,
   *     then the first 10 samples are jumped over.
   * @param int endsearchborder - defines the upper search border. See above
   */
  public PeakSearcher(
      float[] vector, float underborder, int beginsearchborder, int endsearchborder) {
    if (beginsearchborder < 1) beginsearchborder = 1;
    localmaximas =
        new int[vector.length]; // Store for local maximas, 0=no local maxima,1=Beside, <1 local Max
    for (int i = beginsearchborder;
        i < (vector.length - endsearchborder - 1);
        i++) // Loop over complette vector
    if ((vector[i] > vector[i - 1]) && (vector[i] > vector[i + 1] && vector[i] > underborder)) {
        localmaximas[i] = localmaxcounter++; // then is local Maxima
        // Searching for the highest paek
        if (getVectorElement(vector, highestpeakpos) < vector[i]) {
          secondhighestpeakpos = highestpeakpos;
          highestpeakpos = i;
        }
        // Searching for the second highest peak
        if (getVectorElement(vector, secondhighestpeakpos) < vector[i]
            && vector[highestpeakpos] > vector[i]) {
          secondhighestpeakpos = i;
        }
      }
    if (highestpeakpos == -1) highestpeakpos = 0;
    if (secondhighestpeakpos == -1) secondhighestpeakpos = 0;
  }
  /**
   * This function gives 0 back if the selected element is smaller than null.
   *
   * @param float[] vector - the array to access.
   * @param int element - the element to access.
   * @return float - the selected element.
   */
  private float getVectorElement(float[] vector, int element) {
    if (element < 0) return 0.0f;
    else return vector[element];
  }

  /**
   * Returns an vector with the length of the float vector. Possible numbers of the vector are: 0 if
   * ot is no local maximum; 1 if it is beside a local maximum; 2 and higher is it is a local
   * maximum and higher numbers for other local maximas. For example the 5 nd highest maxima has 5
   * at the position of the array where the 5-nd highest maxima is.
   *
   * @return int[] - vector with the local peak position informations.
   */
  public int[] getLoacMaximas() {
    return localmaximas;
  }
  /**
   * Get the value of highest peak position.
   *
   * @return int - value of highestpeakpos.
   */
  public int getHighestPeakPosition() {
    return highestpeakpos;
  }
  /**
   * Get the value of second highest peak position.
   *
   * @return int - value of second highestpeakpos.
   */
  public int getSecondHighestPeakPosition() {
    return secondhighestpeakpos;
  }
}
