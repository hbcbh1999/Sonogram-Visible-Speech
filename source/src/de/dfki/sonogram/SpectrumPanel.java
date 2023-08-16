package de.dfki.sonogram;

import java.awt.*;
import javax.swing.*;

// ------------------------------------------------------------------------------------------

class SpectrumPanel extends JPanel implements java.awt.event.ActionListener {
  private final java.awt.image.BufferedImage BACKGROUND_IMAGE = createBackground(552, 180);
  private final javax.swing.Timer PAINT_TIMER = new javax.swing.Timer(25, this);
  public float[] spectrum = null;
  public int maxI = 0;

  // ------------------------------------------------------------------------------------------

  SpectrumPanel() {
    setPreferredSize(new java.awt.Dimension(552, 180));
    setSize(new java.awt.Dimension(552, 180));
    PAINT_TIMER.start();
  }

  // ------------------------------------------------------------------------------------------

  public void actionPerformed(java.awt.event.ActionEvent event) {
    if (event.getSource().equals(PAINT_TIMER)) {
      repaint();
    }
  }

  // ------------------------------------------------------------------------------------------

  protected void paintComponent(java.awt.Graphics g) {
    java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
    g2.setRenderingHint(
        java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(
        java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION,
        java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g2.setRenderingHint(
        java.awt.RenderingHints.KEY_COLOR_RENDERING,
        java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g2.setRenderingHint(
        java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
    g2.setRenderingHint(
        java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
    g2.setRenderingHint(
        java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);

    super.paintComponent(g);
    g2.drawImage(BACKGROUND_IMAGE, 0, 0, this);

    if (spectrum != null) {
      g2.setStroke(new BasicStroke(3));
      g2.setColor(new Color(50, 100, 40));
      for (int i = 0; i < 255; i++) {
        int y1 = 162 - (int) (spectrum[i] / 2.28f);
        int y2 = 162 - (int) (spectrum[i + 1] / 2.28f);
        int ym = (y1 + y2) / 2;
        int x1 = i * 2 + 21;
        int x2 = (i + 1) * 2 + 22;
        g2.drawLine(x1, y1, x2, y2);
        AlphaComposite alphacomposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f);
        g2.setComposite(alphacomposite);
        g2.drawLine(x1, y1, x1, 162);
        g2.drawLine(x2, y2, x2, 162);
        alphacomposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
        g2.setComposite(alphacomposite);
      }
      g2.setStroke(new BasicStroke(1));
      g2.setColor(new Color(220, 255, 40));
      for (int i = 0; i < 255; i++) {
        int y1 = 162 - (int) (spectrum[i] / 2.28f);
        int y2 = 162 - (int) (spectrum[i + 1] / 2.28f);
        int ym = (y1 + y2) / 2;
        int x1 = i * 2 + 21;
        int x2 = (i + 1) * 2 + 22;
        g2.drawLine(x1, y1, x2, y2);
        AlphaComposite alphacomposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f);
        g2.setComposite(alphacomposite);
        g2.drawLine(x1, y1, x1, 162);
        g2.drawLine(x2, y2, x2, 162);
        alphacomposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
        g2.setComposite(alphacomposite);
      }
      double fr = (Math.round(22050.0 / 256 * (double) maxI)) / 1000.0;
      if (maxI > 1)
        paintToopTip(g2, 21 + (maxI * 2), 52, 530, "Peak: " + Double.toString(fr) + "kHz");
    }
  }

  // ------------------------------------------------------------------------------------------

  public java.awt.image.BufferedImage createBackground(int width, int height) {
    java.awt.GraphicsConfiguration gfxConf =
        java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();
    java.awt.image.BufferedImage IMAGE =
        gfxConf.createCompatibleImage(width, height, java.awt.Transparency.TRANSLUCENT);

    java.awt.Graphics2D g2 = (java.awt.Graphics2D) IMAGE.getGraphics();
    // g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
    // java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(
        java.awt.RenderingHints.KEY_STROKE_CONTROL, java.awt.RenderingHints.VALUE_STROKE_PURE);
    g2.setRenderingHint(
        java.awt.RenderingHints.KEY_ALPHA_INTERPOLATION,
        java.awt.RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
    g2.setRenderingHint(
        java.awt.RenderingHints.KEY_COLOR_RENDERING,
        java.awt.RenderingHints.VALUE_COLOR_RENDER_QUALITY);
    g2.setRenderingHint(
        java.awt.RenderingHints.KEY_DITHERING, java.awt.RenderingHints.VALUE_DITHER_ENABLE);
    g2.setRenderingHint(
        java.awt.RenderingHints.KEY_RENDERING, java.awt.RenderingHints.VALUE_RENDER_QUALITY);

    final java.awt.geom.Point2D START_BACKGROUND = new java.awt.geom.Point2D.Float(0, 0);
    final java.awt.geom.Point2D STOP_BACKGROUND = new java.awt.geom.Point2D.Float(0, height);

    final java.awt.Color[] COLORS_BACKGROUND = {
      new java.awt.Color(0, 0, 0 /*0x253048*/), new java.awt.Color(30, 30, 30 /*0x182635*/)
    };

    final float[] FRACTIONS = {0.0f, 1.0f};
    final java.awt.LinearGradientPaint GRADIENT_BACKGROUND =
        new java.awt.LinearGradientPaint(
            START_BACKGROUND, STOP_BACKGROUND, FRACTIONS, COLORS_BACKGROUND);
    g2.setPaint(GRADIENT_BACKGROUND);
    g2.fillRect(0, 0, width, height);

    final java.awt.Color[] COLORS_HIGHLIGHT = {
      new java.awt.Color(0x000000), new java.awt.Color(0x000000), new java.awt.Color(0x6C8095)
    };
    final java.awt.Color[] COLORS_RIGHT = {
      new java.awt.Color(10, 25, 12), new java.awt.Color(10, 15, 10)
    };
    final java.awt.geom.Point2D START_HIGHLIGHT = new java.awt.geom.Point2D.Float(0, 79);
    final java.awt.geom.Point2D STOP_HIGHLIGHT = new java.awt.geom.Point2D.Float(0, 166);
    final float[] FRACTIONS_HIGHLIGHT = {0.0f, 0.82f, 1.0f};
    final java.awt.LinearGradientPaint GRADIENT_HIGHLIGHT =
        new java.awt.LinearGradientPaint(
            START_HIGHLIGHT, STOP_HIGHLIGHT, FRACTIONS_HIGHLIGHT, COLORS_HIGHLIGHT);
    g2.setPaint(GRADIENT_HIGHLIGHT);
    g2.fillRect(17, 52, 520, 112);

    final java.awt.geom.Point2D START = new java.awt.geom.Point2D.Float(0, 80);
    final java.awt.geom.Point2D STOP = new java.awt.geom.Point2D.Float(0, 165);
    final java.awt.LinearGradientPaint GRADIENT_RIGHT =
        new java.awt.LinearGradientPaint(START, STOP, FRACTIONS, COLORS_RIGHT);
    g2.setPaint(GRADIENT_RIGHT);
    g2.fillRect(18, 47, 518, 116);

    // the screws
    try {
      g2.drawImage(
          javax.imageio.ImageIO.read(Sonogram.class.getResource("Schraube1.png")), 538, 163, null);
    } catch (Throwable t) {
    }

    // the grid
    java.awt.BasicStroke STROKE =
        new java.awt.BasicStroke(
            1.0f, java.awt.BasicStroke.CAP_SQUARE, java.awt.BasicStroke.JOIN_BEVEL);
    g2.setStroke(STROKE);
    g2.setColor(new Color(255, 255, 255, 30));
    for (int i = 0; i < 10; i++) {
      g2.drawLine(70 + i * 49, 48, 70 + i * 49, 162);
      g2.drawLine(19, 152 - 11 * i, 535, 152 - 11 * i);
    }

    STROKE =
        new java.awt.BasicStroke(
            4.0f, java.awt.BasicStroke.CAP_SQUARE, java.awt.BasicStroke.JOIN_BEVEL);
    g2.setStroke(STROKE);
    g2.setColor(new java.awt.Color(0xDDF3F4));
    g2.drawLine(19, 30, 19, 36);
    g2.drawLine(19, 30, 207, 30);
    g2.drawLine(347, 30, 535, 30);
    g2.drawLine(535, 30, 535, 36);
    g2.setFont(new java.awt.Font("Arial", 1, 22));
    g2.drawString("SPECTRUM", 215, 37);

    return IMAGE;
  }

  // ----------------------------------------------------------------------------------------------------------------

  private void paintToopTip(Graphics2D g, int posX, int posY, int maxX, String text) {
    AlphaComposite alphacomposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f);
    g.setComposite(alphacomposite);
    int y1 = 25; // From top to tooltip begin.
    int y2 = 15; // Tooltip height.
    int x1 = 40; // From left to begin triangle.
    int x2 = 8; // Triangle width.
    int x3 = 90; // From the triangle to the right.
    int trix1, triy1, trix2, triy2;
    int recx1, recy1, recx2, recy2, recx3, recy3, recx4, recy4;
    Color fillcolor = new Color(100, 100, 100);
    Color bordercolor = new Color(255, 255, 255);
    Color textcolor = new Color(255, 255, 255);
    // Callculate the positions of the tooltip corresponds to the given coordinates
    triy1 = triy2 = recy1 = recy2 = posY + y1;
    recy3 = recy4 = posY + y1 + y2;
    recx1 = recx3 = posX - x2 - x1;
    trix1 = posX - x2 / 2;
    trix2 = posX + x2;
    recx2 = recx4 = posX + x3;
    // The rectangle for the text
    g.setColor(fillcolor);
    g.fillRect(recx1, recy1, recx2 - recx1, recy4 - recy2);
    // The rectangle for the text
    g.setColor(fillcolor);
    g.fillRect(recx1, recy1, recx2 - recx1, recy4 - recy2);
    // The Triangle to top
    int[] polyx = {posX, trix1, trix2};
    int[] polyy = {posY, triy1, triy2};
    g.fillPolygon(polyx, polyy, 3);
    // The Border
    g.setColor(bordercolor);
    g.drawLine(posX, posY, trix1, triy1);
    g.drawLine(trix1 - 1, triy1, recx1, recy1);
    g.drawLine(recx1, recy1 + 1, recx3, recy3);
    g.drawLine(recx3 + 1, recy3, recx4, recy4);
    g.drawLine(recx4, recy4 - 1, recx2, recy2);
    g.drawLine(recx2 - 1, recy2, trix2, triy2);
    g.drawLine(trix2, triy2 - 1, posX, posY - 1);
    // The text
    g.setColor(textcolor);
    g.drawString(text, posX - x1 - x2 + 3, posY + y1 + y2 - 3);
    // the red dot
    alphacomposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f);
    g.setComposite(alphacomposite);
    g.setColor(new Color(255, 100, 0));
    g.fillOval(posX - 3, posY - 3, 6, 6);
  }

  // ----------------------------------------------------------------------------------------------------------------

}
