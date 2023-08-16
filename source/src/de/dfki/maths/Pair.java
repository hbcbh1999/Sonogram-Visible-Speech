package de.dfki.maths;

/**
 * (c) DFKI GmbH 26.9.2002
 *
 * <p>Orginal C sources: Author Mike Jackson - University of Edinburgh - 1999-2001
 *
 * <p>The <code>Pair</code> class creates objects to hold a pair of floating point numbers
 *
 * @author Christoph Lauer
 * @version 1.0
 */
public class Pair {

  /** The first number */
  protected float fst;

  /** The second number */
  protected float snd;

  /** Create a pair (0, 0) */
  public Pair() {
    fst = snd = 0f;
  }

  /** Create a <code>Pair</code> (<code>fst</code>, <code>snd</code>) */
  public Pair(float fst, float snd) {
    this.fst = fst;
    this.snd = snd;
  }

  /** Get first element */
  public float getFst() {
    return fst;
  }

  /** Set first element */
  public void setFst(float fst) {
    this.fst = fst;
  }

  /** Get second element */
  public float getSnd() {
    return snd;
  }

  /** Set second element */
  public void setSnd(float snd) {
    this.snd = snd;
  }
}
