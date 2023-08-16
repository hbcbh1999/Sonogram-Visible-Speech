package de.dfki.maths;

import java.util.Vector;

/**
 * (c) DFKI GmbH 26.9.2002
 *
 * <p>Orginal C sources: Author Mike Jackson - University of Edinburgh - 1999-2001
 *
 * <p><code>RotatingPhasor</code> is a sub-class of <code>ComplexPhasor</code> which creates objects
 * which record the positions in complex space occupied by a <code>ComplexPhasor</code> as it is
 * rotated.
 *
 * @author Christoph Lauer
 * @version 1.0
 */
public class RotatingPhasor extends ComplexPhasor implements Cloneable {
  // Previously occupied points
  private Vector _locations = new Vector();
  // Current location of phasor
  private Complex _location = new Complex();
  // Current location of phasor in terms of a fraction of the 2 PI
  // unit circle
  private float _angleFraction = 0;

  /** Create a new <code>RotatingPhasor</code> */
  public RotatingPhasor() {
    super();
    project();
  }

  /** Create a new <code>RotatingPhasor</code> with unit amplitude and given velocity */
  public RotatingPhasor(float velocity) {
    super(velocity);
    project();
  }

  /** Create a new <code>RotatingPhasor</code> with the given amplitude and velocity */
  public RotatingPhasor(float amplitude, float velocity) {
    super(amplitude, velocity);
    project();
  }

  /**
   * Create a new <code>RotatingPhasor</code> with the given amplitude, velocity and decay factor
   */
  public RotatingPhasor(float amplitude, float velocity, float decay) {
    super(amplitude, velocity, decay);
    project();
  }

  // Record RotatingPhasor position at current current angle of
  // rotation

  private void project() {
    _location.setRealImag(real, imag);
  }

  /** Change amplitude of <code>RotatingPhasor</code> and update record of its current position */
  public void setAmplitude(float amplitude) {
    super.setAmplitude(amplitude);
    project();
  }

  /** Change velocity of <code>RotatingPhasor</code> and update record of its current position */
  public void setVelocity(float velocity) {
    super.setVelocity(velocity);
    project();
  }

  /** Set this <code>RotatingPhasor</code> to be its own conjugate and update current position */
  public void conjugate() {
    super.conjugate();
    project();
  }

  /**
   * Rotate the <code>RotatingPhasor</code> by the given proportion of 2 PI radians and record the
   * trail of locations occupied during the rotation.
   *
   * <p>For example, if an argument of 0.5 is given then the trail of points occupied by the <code>
   * RotatingPhasor</code> through a rotation of PI radians will be recorded. If the <code>rotate()
   * </code> method is then called with an argument of 0.25 then the <code>RotatingPhasor</code>
   * will be rotated by an additional 0.5 PI radians.
   *
   * <p>If the number of locations occupied grows larger than <code>Integer.MAX_VALUE</code> then
   * the record of locations occupied will be reset
   *
   * <p>Similarly a proportion of 0 causes the record of locations occupied to be reset
   *
   * <p>The argument <code>numPoints</code> indicates how many locations during the rotation should
   * be recorded
   */
  public void rotate(float proportion, int numPoints) {
    // Reset the vapour trail of points visited
    if ((_angleFraction == 0) || (getNumLocations() > (Integer.MAX_VALUE - numPoints))) {
      _locations.removeAllElements();
    }
    if (proportion == 0) {
      // Zero rotation
      super.rotate(0, _location);
      _angleFraction = 0;
    } else {
      // Break up proportion into smaller units to allow finer
      // grained recording of positions the phasor occupies
      float rate = Math.abs(proportion - _angleFraction) / numPoints;
      float angle = _angleFraction;
      for (int i = 1; i <= numPoints; i++) {
        rotate(angle, _location);
        _locations.addElement((Complex) (_location.clone()));
        angle += rate;
      }
      // Save this proportion
      _angleFraction = proportion;
    }
  }

  /** Get the current location of the <code>RotatingPhasor</code> in complex space */
  public Complex getLocation() {
    return _location;
  }

  /** Clear the record of previously occupied locations of the <code>RotatingPhasor</code> */
  public void resetLocations() {
    rotate(0, 20);
  }

  /** Return the number of previous locations that are currently recorded */
  public int getNumLocations() {
    return _locations.size();
  }

  /** Return the <code>n</code>th recorded location of the <code>RotatingPhasor</code> */
  public Complex getNthLocation(int n) {
    return (Complex) _locations.elementAt(n);
  }

  /** Return a clone of the <code>RotatingPhasor</code> object */
  public Object clone() {
    return new RotatingPhasor(amplitude, velocity, decay);
  }
}
