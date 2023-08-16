package de.dfki.maths;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>This Class collects statical functions that smooths an vector with the gaussial smooth
 * angorithm. It is similar to an lowpass filter in the time domain.
 *
 * @author Christoph Lauer
 * @version 1.0, Begin 16/10/2001
 */
public class VectorSmoother {
  /**
   * Smooths the given vector with the agausian algorithm of the order two.
   *
   * @param vector - the given time doamin,
   * @return float[] - the smoothed vector.
   */
  public static float[] smoothWithDegreeTwo(float[] vector) {
    int lengthofthevector = vector.length;
    vector[1] = (vector[0] + vector[1] + vector[2]) / 3.0f;
    vector[0] = (vector[0] + vector[1]) / 2.0f;
    vector[lengthofthevector - 2] =
        (vector[lengthofthevector - 1]
                + vector[lengthofthevector - 2]
                + vector[lengthofthevector - 3])
            / 3.0f;
    vector[lengthofthevector - 1] =
        (vector[lengthofthevector - 1] + vector[lengthofthevector - 2]) / 2.0f;
    for (int i = 2; i < (lengthofthevector - 2); i++)
      vector[i] =
          (vector[i] + vector[i + 1] + vector[i - 1] + vector[i + 2] + vector[i - 2]) / 5.0f;
    return vector;
  }
  /**
   * Smooths the given vector with the agausian algorithm of the order three.
   *
   * @param vector - the given time doamin,
   * @return float[] - the smoothed vector.
   */
  public static float[] smoothWithDegreeThree(float[] vector) {
    int lengthofthevector = vector.length;
    vector[2] = (vector[1] + vector[2] + vector[3] + vector[4]) / 4.0f;
    vector[1] = (vector[0] + vector[1] + vector[3]) / 3.0f;
    vector[0] = (vector[0] + vector[1]) / 3.0f;
    vector[lengthofthevector - 3] =
        (vector[lengthofthevector - 1]
                + vector[lengthofthevector - 2]
                + vector[lengthofthevector - 3]
                + vector[lengthofthevector - 4])
            / 4.0f;
    vector[lengthofthevector - 2] =
        (vector[lengthofthevector - 1]
                + vector[lengthofthevector - 2]
                + vector[lengthofthevector - 3])
            / 3.0f;
    vector[lengthofthevector - 1] =
        (vector[lengthofthevector - 1] + vector[lengthofthevector - 2]) / 2.0f;
    for (int i = 3; i < (lengthofthevector - 3); i++)
      vector[i] =
          (vector[i]
                  + vector[i + 1]
                  + vector[i - 1]
                  + vector[i + 2]
                  + vector[i - 2]
                  + vector[i + 3]
                  + vector[i - 3])
              / 7.0f;
    return vector;
  }
  /**
   * Smooths the given vector with the agausian algorithm of the order four.
   *
   * @param vector - the given time doamin,
   * @return float[] - the smoothed vector.
   */
  public static float[] smoothWithDegreeFour(float[] vector) {
    int lengthofthevector = vector.length;
    vector[3] = (vector[0] + vector[1] + vector[2] + vector[3] + vector[4]) / 5.0f;
    vector[2] = (vector[0] + vector[1] + vector[2] + vector[3]) / 4.0f;
    vector[1] = (vector[0] + vector[1] + vector[2]) / 3.0f;
    vector[0] = (vector[0] + vector[1]) / 2.0f;
    vector[lengthofthevector - 4] =
        (vector[lengthofthevector - 1]
                + vector[lengthofthevector - 2]
                + vector[lengthofthevector - 3]
                + vector[lengthofthevector - 4]
                + vector[lengthofthevector - 5])
            / 5.0f;
    vector[lengthofthevector - 3] =
        (vector[lengthofthevector - 1]
                + vector[lengthofthevector - 2]
                + vector[lengthofthevector - 3]
                + vector[lengthofthevector - 4])
            / 4.0f;
    vector[lengthofthevector - 2] =
        (vector[lengthofthevector - 1]
                + vector[lengthofthevector - 2]
                + vector[lengthofthevector - 3])
            / 3.0f;
    vector[lengthofthevector - 1] =
        (vector[lengthofthevector - 1] + vector[lengthofthevector - 2]) / 2.0f;
    for (int i = 4; i < (lengthofthevector - 4); i++)
      vector[i] =
          (vector[i]
                  + vector[i + 1]
                  + vector[i - 1]
                  + vector[i + 2]
                  + vector[i - 2]
                  + vector[i + 3]
                  + vector[i - 3]
                  + vector[i + 4]
                  + vector[i - 4])
              / 9.0f;
    return vector;
  }
  /**
   * Smooths the given vector with the agausian algorithm of the order six.
   *
   * @param vector - the given time doamin,
   * @return float[] - the smoothed vector.
   */
  public static float[] smoothWithDegreeFive(float[] vector) {
    int lengthofthevector = vector.length;
    vector[4] = (vector[0] + vector[1] + vector[2] + vector[3] + vector[4] + vector[5]) / 6.0f;
    vector[3] = (vector[0] + vector[1] + vector[2] + vector[3] + vector[4]) / 5.0f;
    vector[2] = (vector[0] + vector[1] + vector[2] + vector[3]) / 4.0f;
    vector[1] = (vector[0] + vector[1] + vector[2]) / 3.0f;
    vector[0] = (vector[0] + vector[1]) / 2.0f;
    vector[lengthofthevector - 5] =
        (vector[lengthofthevector - 1]
                + vector[lengthofthevector - 2]
                + vector[lengthofthevector - 3]
                + vector[lengthofthevector - 4]
                + vector[lengthofthevector - 5]
                + vector[lengthofthevector - 6])
            / 6.0f;
    vector[lengthofthevector - 4] =
        (vector[lengthofthevector - 1]
                + vector[lengthofthevector - 2]
                + vector[lengthofthevector - 3]
                + vector[lengthofthevector - 4]
                + vector[lengthofthevector - 5])
            / 5.0f;
    vector[lengthofthevector - 3] =
        (vector[lengthofthevector - 1]
                + vector[lengthofthevector - 2]
                + vector[lengthofthevector - 3]
                + vector[lengthofthevector - 4])
            / 4.0f;
    vector[lengthofthevector - 2] =
        (vector[lengthofthevector - 1]
                + vector[lengthofthevector - 2]
                + vector[lengthofthevector - 3])
            / 3.0f;
    vector[lengthofthevector - 1] =
        (vector[lengthofthevector - 1] + vector[lengthofthevector - 2]) / 2.0f;
    for (int i = 5; i < (lengthofthevector - 5); i++)
      vector[i] =
          (vector[i]
                  + vector[i + 1]
                  + vector[i - 1]
                  + vector[i + 2]
                  + vector[i - 2]
                  + vector[i + 3]
                  + vector[i - 3]
                  + vector[i + 4]
                  + vector[i - 4]
                  + vector[i + 4]
                  + vector[i - 4])
              / 11.0f;
    return vector;
  }
}
