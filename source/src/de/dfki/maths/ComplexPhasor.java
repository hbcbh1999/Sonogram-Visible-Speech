package de.dfki.maths;


/**
 * (c) DFKI GmbH 26.9.2002
 *
 * <p>Orginal C sources: Author Mike Jackson - University of Edinburgh - 1999-2001
 *
 * <p>A sub-class of <code>Complex</code> which generates objects that represent complex phasors in
 * terms of their amplitude, velocity (in radians) and decay factor. An operation to determine the
 * position of the <code>ComplexPhasor</code> after a rotation by a fraction of 2 PI is also
 * provided.
 *
 * @author Christoph Lauer
 * @version 1.0
 */
public class ComplexPhasor extends Complex implements Cloneable {

  /** Complex phasor amplitude */
  protected float amplitude;

  /** Complex phasor velocity in radians */
  protected float velocity;

  /** Complex phasor decay factor */
  protected float decay;

  /** Create a new phasor of unit amplitude with zero velocity */
  public ComplexPhasor() {
    this(1f, 0f, 0f);
  }

  /** Create a new phasor of unit amplitude with the given velocity in radians */
  public ComplexPhasor(float velocity) {
    this(1f, velocity, 0f);
  }

  /** Create a new phasor with the given amplitude and velocity in radians */
  public ComplexPhasor(float amplitude, float velocity) {
    this(amplitude, velocity, 0f);
  }

  /** Create a new phasor with the given amplitude, velocity in radians and decay factor */
  public ComplexPhasor(float amplitude, float velocity, float decay) {
    this.amplitude = amplitude;
    this.velocity = velocity;
    this.decay = decay;
    calculateTrigonometricForm();
  }

  /** Calculate the trigonometric form of the complex phasor */
  private void calculateTrigonometricForm() {
    real = (float) (amplitude * Math.cos(velocity));
    imag = (float) (amplitude * Math.sin(velocity));
  }

  /** Set the decay factor of the phasor */
  public void setDecay(float decay) {
    this.decay = decay;
  }

  /** Get the decay factor of the phasor */
  public float getDecay() {
    return decay;
  }

  /** Set the amplitude of the phasor */
  public void setAmplitude(float amplitude) {
    this.amplitude = amplitude;
    calculateTrigonometricForm();
  }

  /** Get the amplitude of the phasor */
  public float getAmplitude() {
    return amplitude;
  }

  /** Set the velocity (in radians) of the phasor */
  public void setVelocity(float velocity) {
    this.velocity = velocity;
    calculateTrigonometricForm();
  }

  /** Get the velocity (in radians) of the phasor */
  public float getVelocity() {
    return velocity;
  }

  /** Set this phasor to be its own complex conjugate */
  public void conjugate() {
    setVelocity(-velocity);
  }

  /**
   * Rotate the phasor by the given fraction of 2 PI and place the new location of the phasor in the
   * given <code>Complex</code> number object
   */
  public void rotate(float fraction, Complex complex) {
    double factor = Math.pow(Math.E, fraction * decay) * amplitude;
    float fractionVelocity = velocity * fraction;
    complex.real = (float) (factor * Math.cos(fractionVelocity));
    complex.imag = (float) (factor * Math.sin(fractionVelocity));
  }

  /** Return a clone of the <code>ComplexPhasor</code> object */
  public Object clone() {
    return new ComplexPhasor(amplitude, velocity, decay);
  }
}
