package de.dfki.sonogram;

import java.awt.*;
import javax.media.j3d.*;
import javax.vecmath.*;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>Shape3D object for Surface class. Shape for coordsystem-lines.
 *
 * @author Christoph Lauer
 * @version 1.0, Current 26/09/2002
 */
public class CoordSysLines extends Shape3D {
  /** The Construktor initializes the Geometry and the Appearence of this Shape */
  private Sonogram reftomain;

  public CoordSysLines(Sonogram ref) {
    reftomain = ref;
    // the axis lines quad geometry
    this.setGeometry(axisGeometry());
    // the general appearance
    this.setAppearance(lineAppearance());
  }

  private Geometry axisGeometry() {

    LineArray axis = new LineArray(18 + 44, LineArray.COORDINATES | LineArray.COLOR_3);

    // Frequency Axis & Dart
    axis.setCoordinate(0, new Point3f(-0.6f, -0.6f, -0.4f)); // Axis
    axis.setCoordinate(1, new Point3f(-0.6f, 0.6f, -0.4f));
    axis.setColor(0, new Color3f(1f, 0f, 0f));
    axis.setColor(1, new Color3f(0.8f, 1f, 0f));
    axis.setCoordinate(6, new Point3f(-0.6f, 0.6f, -0.4f)); // Dart
    axis.setCoordinate(7, new Point3f(-0.58f, 0.55f, -0.4f));
    axis.setColor(6, new Color3f(0.8f, 1f, 0f));
    axis.setColor(7, new Color3f(0.8f, 1f, 0.6f));
    axis.setCoordinate(8, new Point3f(-0.6f, 0.6f, -0.4f));
    axis.setCoordinate(9, new Point3f(-0.62f, 0.55f, -0.4f));
    axis.setColor(8, new Color3f(0.8f, 1f, 0f));
    axis.setColor(9, new Color3f(0.8f, 1f, 0.6f));

    // Time Axis & Dart
    axis.setCoordinate(2, new Point3f(-0.6f, -0.6f, -0.4f)); // Axis
    axis.setCoordinate(3, new Point3f(0.6f, -0.6f, -0.4f));
    axis.setColor(2, new Color3f(1f, 0f, 0f));
    axis.setColor(3, new Color3f(0.5f, 0.7f, 1f));
    axis.setCoordinate(10, new Point3f(0.6f, -0.6f, -0.4f)); // Dart
    axis.setCoordinate(11, new Point3f(0.55f, -0.62f, -0.4f));
    axis.setColor(10, new Color3f(0.5f, 0.7f, 1f));
    axis.setColor(11, new Color3f(0.7f, 0.9f, 1f));
    axis.setCoordinate(12, new Point3f(0.6f, -0.6f, -0.4f));
    axis.setCoordinate(13, new Point3f(0.55f, -0.58f, -0.4f));
    axis.setColor(12, new Color3f(0.5f, 0.7f, 1f));
    axis.setColor(13, new Color3f(0.7f, 0.9f, 1f));

    // Amplitude Axis & Dart
    axis.setCoordinate(4, new Point3f(-0.6f, -0.6f, -0.4f)); // Axis
    axis.setCoordinate(5, new Point3f(-0.6f, -0.6f, 0.6f));
    axis.setColor(4, new Color3f(1f, 0f, 0f));
    axis.setColor(5, new Color3f(0.2f, 0.5f, 1f));
    axis.setCoordinate(14, new Point3f(-0.6f, -0.6f, 0.6f)); // Dart
    axis.setCoordinate(15, new Point3f(-0.61f, -0.58f, 0.55f));
    axis.setColor(14, new Color3f(0.2f, 0.5f, 1f));
    axis.setColor(15, new Color3f(0.7f, 0.9f, 1f));
    axis.setCoordinate(16, new Point3f(-0.6f, -0.6f, 0.6f));
    axis.setCoordinate(17, new Point3f(-0.58f, -0.61f, 0.55f));
    axis.setColor(16, new Color3f(0.2f, 0.5f, 1f));
    axis.setColor(17, new Color3f(0.7f, 0.9f, 1f));

    int count = 18;
    Color3f col = new Color3f(0.4f, 0.4f, 0.4f);
    for (float x = -0.5f; x <= 0.5f; x += 0.1f) {
      axis.setCoordinate(count, new Point3f(x, -0.6f, -0.4f));
      axis.setColor(count, col);
      count++;
      axis.setCoordinate(count, new Point3f(x, 0.6f, -0.4f));
      axis.setColor(count, col);
      count++;
    }
    for (float y = -0.5f; y <= 0.5f; y += 0.1f) {
      axis.setCoordinate(count, new Point3f(-0.6f, y, -0.4f));
      axis.setColor(count, col);
      count++;
      axis.setCoordinate(count, new Point3f(0.6f, y, -0.4f));
      axis.setColor(count, col);
      count++;
    }
    return (axis);
  }

  private Appearance lineAppearance() {

    Appearance app = new Appearance();
    PolygonAttributes polyAttrib = new PolygonAttributes();
    polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
    polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
    app.setPolygonAttributes(polyAttrib);

    // 	PointAttributes pointAttrib = new PointAttributes(2,true);
    //         app.setPointAttributes(pointAttrib);

    if (reftomain.iperantialias == true) {
      LineAttributes lineAttrib = new LineAttributes(2f, LineAttributes.PATTERN_SOLID, true);
      app.setLineAttributes(lineAttrib);
    }
    return app;
  }
} // end of class CoordSysLines
