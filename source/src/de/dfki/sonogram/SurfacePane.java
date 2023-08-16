package de.dfki.sonogram;

import javax.media.j3d.*;
import javax.swing.*;
import javax.vecmath.*;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>Surface class Renders a single, interactively rotatable, traslatable, and zoomable
 * Sonogram-Surface object.
 *
 * @author Christoph Lauer
 * @version 1.0, Begin 22/03/2002, Current 26/09/2002
 */
public class SurfacePane extends Shape3D {
  Sonogram reftomain;
  Surface reftosurface;
  boolean renderFlat;
  boolean isWv;
  private Color3b[] colors = new Color3b[256];

  // ----------------------------------------------------------------------------------------------------------------

  // the construktors flat parameter generates a plane solid quad for the bottom
  public SurfacePane(Sonogram sono, Surface sf, boolean flat, boolean isWvArgument) {
    renderFlat = flat;
    isWv = isWvArgument;
    reftomain = sono;
    reftosurface = sf;
    generateColors();
    this.setAppearance(surfacePaneAppearance());
    this.setGeometry(surfacePaneGeometry());
  }

  // ----------------------------------------------------------------------------------------------------------------

  private void generateColors() {
    for (int i = 0; i < 256; i++) {
      if (isWv == true) {
        switch (reftomain.wvconfig.colCombo.getSelectedIndex()) {
          case 0:
            colors[i] = new Color3b(reftomain.pp.cocFI[i]);
            break;
          case 1:
            colors[i] = new Color3b(reftomain.pp.coFI[i]);
            break;
          case 2:
            colors[i] = new Color3b(reftomain.pp.coCO[i]);
            break;
          case 3:
            colors[i] = new Color3b(reftomain.pp.coRA[i]);
            break;
          case 4:
            colors[i] = new Color3b(reftomain.pp.coCG[i]);
            break;
          case 5:
            colors[i] = new Color3b(reftomain.pp.coSW[i]);
            break;
        }
      } else {
        if (reftomain.colItem.isSelected() == true && reftomain.negItem.isSelected() == false)
          colors[i] = new Color3b(reftomain.pp.coCO[i]);
        if (reftomain.colItem.isSelected() == true && reftomain.negItem.isSelected() == true)
          colors[i] = new Color3b(reftomain.pp.coCOi[i]);
        if (reftomain.bwItem.isSelected() == true && reftomain.negItem.isSelected() == false)
          colors[i] = new Color3b(reftomain.pp.coSW[i]);
        if (reftomain.bwItem.isSelected() == true && reftomain.negItem.isSelected() == true)
          colors[i] = new Color3b(reftomain.pp.coSWi[i]);
        if (reftomain.firecItem.isSelected() == true && reftomain.negItem.isSelected() == false)
          colors[i] = new Color3b(reftomain.pp.cocFI[i]);
        if (reftomain.firecItem.isSelected() == true && reftomain.negItem.isSelected() == true)
          colors[i] = new Color3b(reftomain.pp.cocFIi[i]);
        if (reftomain.fireItem.isSelected() == true && reftomain.negItem.isSelected() == false)
          colors[i] = new Color3b(reftomain.pp.coFI[i]);
        if (reftomain.fireItem.isSelected() == true && reftomain.negItem.isSelected() == true)
          colors[i] = new Color3b(reftomain.pp.coFIi[i]);
        if (reftomain.rainItem.isSelected() == true && reftomain.negItem.isSelected() == false)
          colors[i] = new Color3b(reftomain.pp.coRA[i]);
        if (reftomain.rainItem.isSelected() == true && reftomain.negItem.isSelected() == true)
          colors[i] = new Color3b(reftomain.pp.coRAi[i]);
        if (reftomain.greenItem.isSelected() == true && reftomain.negItem.isSelected() == false)
          colors[i] = new Color3b(reftomain.pp.coCG[i]);
        if (reftomain.greenItem.isSelected() == true && reftomain.negItem.isSelected() == true)
          colors[i] = new Color3b(reftomain.pp.coCGi[i]);
      }
    }
  }
  // ----------------------------------------------------------------------------------------
  /**
   * This Funktion gegnerates the Surfacepoints. First it generates all Points and after that i
   * tryes a Datareduktion for long signals.
   */
  private Geometry surfacePaneGeometry() {
    QuadArray qar;
    float[] tempSpektrum = {0};
    float[] tempSpektrumlower = {0};
    int count = 0;
    int lx;
    int ly;
    if (isWv == false) {
      lx = reftomain.spektrum.size();
      ly = reftomain.timewindowlength / 2;
    } else {
      lx = reftomain.wvconfig.length;
      ly = reftomain.wvconfig.resolution;
    }
    int slider = reftomain.gad.slidersurface.getValue();
    int quads = (lx - 1) * (ly - 1);
    int col1, col2, col3, col4;
    int countx = 0; // Counter for x
    int county = 0; // Counter for y
    int p1 = 0;
    int p2 = 0;
    int p3 = 0;
    int p4 = 0;
    Point3d[] point = new Point3d[quads * 4];
    Color3b[] colar = new Color3b[quads * 4];
    double slx = (double) reftomain.gad.slidersurfacey.getValue() / 10.0;
    double sly = (double) reftomain.gad.slidersurfacex.getValue() / 10.0;
    double diffx = slx / (double) (lx - 1);
    double diffy = sly / (double) (ly - 1);
    double x1, x2, x3, x4;
    double y1, y2, y3, y4;
    double z1, z2, z3, z4;
    // Check for large Plots and show Dialog
    int maxPoygonsWarning = 300000;
    if (quads / reftomain.gad.slidersurface.getValue() > maxPoygonsWarning
        && renderFlat == false
        && reftomain.i3dpolygons == false) {
      System.out.println("--> Large Plot");
      JCheckBox askAgain =
          new JCheckBox("<html><i><font size = -2>Do not show this warning message again");
      Object[] message = new Object[2];
      if (isWv == true)
        message[0] =
            "<html>This Perspectogram <u><font color=#660000>is a very large <i>Mech</i>, and your"
                + " Graphic Card could<br>have problems to render this large polygon model in a"
                + " proper time</u></font> !<br>The Model has about <u>"
                + quads / reftomain.gad.slidersurface.getValue()
                + " Polygons</u>. Please reduce the Resolution<br>and/or the Windowlengt in the"
                + " Settings to reduce the number of Polygons.<br>For Point-Clouds you could also"
                + " try the Point density reduction.<br><br><font size=4>Continue the"
                + " Perspectogram's calculation ?";
      else
        message[0] =
            "<html>This Perspectogram <u><font color=#660000>is a very large <i>Mech</i>, and your"
                + " Graphic Card could<br>have problems to render this large polygon model in a"
                + " proper time</u></font> !<br>The Model has about <u>"
                + quads / reftomain.gad.slidersurface.getValue()
                + " Polygons</u>. Please reduce the Overlapping<br>or Zoom-In to reduce the number"
                + " of Polygons for this Perspectogram.<br>For Point-Clouds you could also try the"
                + " Point density reduction.<br><br><font size=4>Continue the Perspectogram"
                + " calculation ?";
      message[1] = askAgain;
      int confirm =
          JOptionPane.showConfirmDialog(
              reftomain,
              message,
              "Very Large Perspectogram",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.WARNING_MESSAGE);
      if (askAgain.isSelected() == true) {
        reftomain.i3dpolygons = true;
      }
      if (confirm != JOptionPane.YES_OPTION) {
        System.out.println("--> Surface generating Canceled");
        return (null);
      }
    }

    // loop over the spectogram matrix
    System.out.println("--> Scalefactor:" + slider);
    for (int cx = 1; cx < lx; cx++) {
      countx++;
      if (isWv == false) {
        tempSpektrum = (float[]) reftomain.spektrum.get(cx);
        tempSpektrumlower = (float[]) reftomain.spektrum.get(cx - 1);
      }
      for (int cy = 0; cy < (ly - 1); cy++) {
        county++;
        // calculate virtual Universe-Points and Colors for all spektrum points
        x1 = x4 = diffx * (double) (cx) - 0.55;
        x2 = x3 = diffx * (double) (cx + 1) - 0.55;
        y1 = y2 = diffy * (double) (cy) - 0.55;
        y3 = y4 = diffy * (double) (cy + 1) - 0.55;
        if (renderFlat == true) {
          z1 = z2 = z3 = z4 = -0.4;
        } else {

          if (isWv == false) {
            z1 = (double) tempSpektrumlower[cy] / 255.0 - 0.4;
            z2 = (double) tempSpektrum[cy] / 255.0 - 0.4;
            z3 = (double) tempSpektrum[cy + 1] / 255.0 - 0.4;
            z4 = (double) tempSpektrumlower[cy + 1] / 255.0 - 0.4;
          } else {
            z1 = reftomain.wvconfig.spwv[cx - 1][cy] - 0.4;
            z2 = reftomain.wvconfig.spwv[cx][cy] - 0.4;
            z3 = reftomain.wvconfig.spwv[cx][cy + 1] - 0.4;
            z4 = reftomain.wvconfig.spwv[cx - 1][cy + 1] - 0.4;
          }
        }
        point[count + 0] = new Point3d(x1, y1, z1);
        point[count + 1] = new Point3d(x2, y2, z2);
        point[count + 2] = new Point3d(x3, y3, z3);
        point[count + 3] = new Point3d(x4, y4, z4);

        if (isWv == false) {
          col1 = (int) tempSpektrumlower[cy];
          col2 = (int) tempSpektrum[cy];
          col3 = (int) tempSpektrum[cy + 1];
          col4 = (int) tempSpektrumlower[cy + 1];
        } else {
          col1 = (int) (reftomain.wvconfig.spwv[cx - 1][cy] * 255.0);
          col2 = (int) (reftomain.wvconfig.spwv[cx][cy] * 255.0);
          col3 = (int) (reftomain.wvconfig.spwv[cx][cy + 1] * 255.0);
          col4 = (int) (reftomain.wvconfig.spwv[cx - 1][cy + 1] * 255.0);
        }
        colar[count + 0] = colors[col1];
        colar[count + 1] = colors[col2];
        colar[count + 2] = colors[col3];
        colar[count + 3] = colors[col4];
        count += 4;
      }
    }

    System.out.println("--> " + count + " Surfacepeaces calculated");
    System.out.println("--> " + quads + " Quads");

    // And now lets reduce the number of points
    // if (renderFlat == true) slider = 1;
    count = 0;
    for (int sel = 0; sel < quads * 4; sel += slider * 4) count += 4;
    int pointssel = count;
    Point3d[] pointsel = new Point3d[pointssel];
    Color3b[] colarsel = new Color3b[pointssel];
    count = 0;
    for (int sel = 0; sel < quads * 4; sel += slider * 4) {

      p1 = (sel + 0);
      p2 = (sel + 1);
      p3 = (sel + 2);
      p4 = (sel + 3);

      if (p1 > (quads * 4 - 1)) p1 = (quads * 4 - 1);
      if (p2 > (quads * 4 - 1)) p2 = (quads * 4 - 1);
      if (p3 > (quads * 4 - 1)) p3 = (quads * 4 - 1);
      if (p4 > (quads * 4 - 1)) p4 = (quads * 4 - 1);
      pointsel[count + 0] = point[p1];
      pointsel[count + 1] = point[p2];
      pointsel[count + 2] = point[p3];
      pointsel[count + 3] = point[p4];
      colarsel[count + 0] = colar[p1];
      colarsel[count + 1] = colar[p2];
      colarsel[count + 2] = colar[p3];
      colarsel[count + 3] = colar[p4];
      count += 4;
    }
    qar = new QuadArray(pointssel, GeometryArray.COORDINATES | GeometryArray.COLOR_3);
    qar.setCoordinates(0, pointsel);
    qar.setColors(0, colarsel);
    return qar;
  }

  // ----------------------------------------------------------------------------------------------------------------

  /** create the Surface appearance */
  private Appearance surfacePaneAppearance() {
    Appearance appearance = new Appearance();

    PolygonAttributes polyAttrib = new PolygonAttributes();
    polyAttrib.setCullFace(PolygonAttributes.CULL_NONE);
    if (reftomain.gad.s1.isSelected()) polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_POINT);
    if (reftomain.gad.s2.isSelected()) polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_LINE);
    if (reftomain.gad.s3.isSelected()) {
      polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_FILL);
      TransparencyAttributes transparencyAttrib =
          new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.3f);
      appearance.setTransparencyAttributes(transparencyAttrib);
    }
    appearance.setPolygonAttributes(polyAttrib);

    if (renderFlat == true) {
      polyAttrib.setPolygonMode(PolygonAttributes.POLYGON_FILL);
      appearance.setPolygonAttributes(polyAttrib);
      TransparencyAttributes transparencyAttrib =
          new TransparencyAttributes(TransparencyAttributes.FASTEST, 0.25f);
      appearance.setTransparencyAttributes(transparencyAttrib);
    }

    if (reftomain.iperantialias == true) {
      LineAttributes lineAttrib = new LineAttributes(1.0f, LineAttributes.PATTERN_SOLID, true);
      appearance.setLineAttributes(lineAttrib);
      PointAttributes pointAttrib = new PointAttributes(4, true);
      appearance.setPointAttributes(pointAttrib);
    }

    return appearance;
  }

  // ----------------------------------------------------------------------------------------------------------------
} // end clss SurfacePane
