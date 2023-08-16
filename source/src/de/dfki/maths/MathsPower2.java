package de.dfki.maths;

/**
 * (c) DFKI GmbH 26.9.2002
 *
 * <p>Orginal C sources: Author Mike Jackson - University of Edinburgh - 1999-2001
 *
 * <p>The <code>MathsPower2</code> class contains some static methods supporting useful and
 * commonly-used mathematical operations relating to powers of two and logs to the base two.
 *
 * @author Christoph Lauer
 * @version 1.0
 */
public class MathsPower2 {

  /** Return 2 to the power of <code>power</code> */
  public static int pow2(int power) {
    return (1 << power);
  }

  /** Is <code>value</code> a power of 2? */
  public static boolean isPow2(int value) {
    return (value == (int) roundPow2(value));
  }

  /** Round <code>value</code> down to nearest power of 2 */
  public static float roundPow2(float value) {
    float power = (float) (Math.log(value) / Math.log(2));
    int intPower = Math.round(power);
    return (float) (pow2(intPower));
  }

  /** Return the log to base 2 of <code>value</code> rounded to the nearest integer */
  public static int integerLog2(float value) {
    int intValue;
    if (value < 2) {
      intValue = 0;
    } else if (value < 4) {
      intValue = 1;
    } else if (value < 8) {
      intValue = 2;
    } else if (value < 16) {
      intValue = 3;
    } else if (value < 32) {
      intValue = 4;
    } else if (value < 64) {
      intValue = 5;
    } else if (value < 128) {
      intValue = 6;
    } else if (value < 256) {
      intValue = 7;
    } else if (value < 512) {
      intValue = 8;
    } else if (value < 1024) {
      intValue = 9;
    } else if (value < 2048) {
      intValue = 10;
    } else if (value < 4098) {
      intValue = 11;
    } else if (value < 8192) {
      intValue = 12;
    } else {
      intValue = Math.round(roundPow2(value));
    }
    return intValue;
  }
}
