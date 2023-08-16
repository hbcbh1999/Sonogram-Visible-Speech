package de.dfki.maths;

/**
 * (c) DFKI GmbH 26.9.2002
 *
 * <p>Orginal C sources: Author Mike Jackson - University of Edinburgh - 1999-2001
 *
 * <p>The <code>RandomNumber</code> class creates object that provide a simple random number
 * generator, generating random numbers between 0 and 1.
 *
 * <p>This is an implementation of algorithm ran2 in "Numerical Recipes in Pascal", W.H.Press, B.P.
 * Flannery, S.A. Taukolsky, and W.T. Vetterling, Cambridge University Press, 1989, page 220.
 *
 * @author Christoph Lauer
 * @version 1.0
 */
public class RandomNumber {
  private static final int M = 714025;
  private static final int IA = 1366;
  private static final int IC = 150889;
  private static final float RM = 1 / (float) M;
  private final int[] _ran2Ir = new int[97];
  private int _ran2Iy;
  private int _index = -2;

  /** Create a new random number generator */
  public RandomNumber() {
    ;
  }

  /** Reset the random number generator */
  public void reset() {
    _index = (IC - _index) % M;
    for (int j = 0; j < 97; j++) {
      _index = (IA * _index + IC) % M;
      _ran2Ir[j] = _index;
    }
    _index = (IA * _index + IC) % M;
    _ran2Iy = _index;
    random();
  }

  /** Return a random number between 0 and 1 */
  public float random() {
    int j = (int) ((97f * _ran2Iy) / M);
    _ran2Iy = _ran2Ir[j];
    float result = _ran2Iy * RM;
    _index = (IA * _index + IC) % M;
    _ran2Ir[j] = _index;
    return result;
  }
}
