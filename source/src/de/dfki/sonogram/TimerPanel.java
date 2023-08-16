package de.dfki.sonogram;

import javax.swing.*;

public class TimerPanel extends javax.swing.JPanel
    implements java.awt.event.ActionListener, java.awt.event.MouseListener {

  // the segment colors
  private java.awt.Color COLOR_ON = new java.awt.Color(255, 100, 50);
  private java.awt.Color FRAME_COLOR_ON = new java.awt.Color(255, 0, 0, 200);

  final java.awt.Color COLOR_OFF = new java.awt.Color(40, 50, 60);
  final java.awt.Color FRAME_COLOR_OFF = new java.awt.Color(61, 57, 142, 128);

  private java.awt.image.BufferedImage DOTS_ON = createDots(true);
  private java.awt.image.BufferedImage DOTS_OFF = createDots(false);
  private boolean dotsOn = true;
  private final java.awt.image.BufferedImage BACKGROUND_IMAGE = createBackground(552, 180);
  private final javax.swing.Timer PAINT_TIMER = new javax.swing.Timer(25, this);
  private int sec_right = 0;
  private int sec_left = 0;
  private int min_right = 0;
  private int min_left = 0;
  private int hour_right = 0;
  private int hour_mid = 0;
  private int hour_left = 0;
  private long startTime = 0;
  private long stopTime = 0;
  private boolean timerRunning = false;
  public int level = 0;
  private float levelPeak = 0.0f;
  int colorSwitch = 0;

  // -------------------------------------------------------------------------------------------------------------------------

  public static void main(String[] args) {
    TimerPanel tp = new TimerPanel();
    JFrame fr = new JFrame();
    fr.getContentPane().add(tp);
    fr.pack();
    fr.setVisible(true);
  }

  // -------------------------------------------------------------------------------------------------------------------------

  private java.awt.image.BufferedImage[] DIGIT_ARRAY = new java.awt.image.BufferedImage[26];

  public void initDigitArray() {
    if (colorSwitch == 0) {
      COLOR_ON = new java.awt.Color(255, 120, 60);
      FRAME_COLOR_ON = new java.awt.Color(255, 0, 0, 200);
    }
    if (colorSwitch == 1) {
      COLOR_ON = new java.awt.Color(0xEEFFEE);
      FRAME_COLOR_ON = new java.awt.Color(50, 200, 100, 128);
    }
    if (colorSwitch == 2) {
      COLOR_ON = new java.awt.Color(140, 180, 255);
      FRAME_COLOR_ON = new java.awt.Color(0, 0, 255, 180);
    }

    DIGIT_ARRAY[0] = createDigit(0);
    DIGIT_ARRAY[1] = createDigit(1);
    DIGIT_ARRAY[2] = createDigit(2);
    DIGIT_ARRAY[3] = createDigit(3);
    DIGIT_ARRAY[4] = createDigit(4);
    DIGIT_ARRAY[5] = createDigit(5);
    DIGIT_ARRAY[6] = createDigit(6);
    DIGIT_ARRAY[7] = createDigit(7);
    DIGIT_ARRAY[8] = createDigit(8);
    DIGIT_ARRAY[9] = createDigit(9);
    DIGIT_ARRAY[10] = createDigit(-1);
    DIGIT_ARRAY[11] = createDigit(-2);
    DIGIT_ARRAY[12] = createDigit(-3);
    DIGIT_ARRAY[13] = createDigit(-4);
    DIGIT_ARRAY[14] = createDigit(-5);
    DIGIT_ARRAY[15] = createDigit(-6);
    DIGIT_ARRAY[16] = createDigit(-7);
    DIGIT_ARRAY[17] = createDigit(-8);
    DIGIT_ARRAY[18] = createDigit(-9);
    DIGIT_ARRAY[19] = createDigit(-10);
    DIGIT_ARRAY[20] = createDigit(-11);
    DIGIT_ARRAY[21] = createDigit(-12);
    DIGIT_ARRAY[22] = createDigit(-13);
    DIGIT_ARRAY[23] = createDigit(-14);
    DIGIT_ARRAY[24] = createDigit(-15);
    DIGIT_ARRAY[25] = createDigit(-16);
    DOTS_ON = createDots(true);
    DOTS_OFF = createDots(false);
  }
  ;

  // -------------------------------------------------------------------------------------------------------------------------

  public TimerPanel() {
    setPreferredSize(new java.awt.Dimension(550, 180));
    setSize(new java.awt.Dimension(550, 180));
    initDigitArray();
    init();
  }

  // -------------------------------------------------------------------------------------------------------------------------

  private void init() {
    addMouseListener(this);
    PAINT_TIMER.start();
  }

  // -------------------------------------------------------------------------------------------------------------------------

  public void startTimer() {
    timerRunning = true;
    startTime = System.currentTimeMillis();
  }

  // -------------------------------------------------------------------------------------------------------------------------

  public void stopTimer() {
    level = 0;
    levelPeak = 0;
    stopTime = System.currentTimeMillis();
    timerRunning = false;
    dotsOn = true;
  }

  // -------------------------------------------------------------------------------------------------------------------------

  public void resetTimer() {
    sec_right = 0;
    sec_left = 0;
    min_right = 0;
    min_left = 0;
    hour_right = 0;
    hour_mid = 0;
    hour_left = 0;
    repaint();
    dotsOn = true;
    timerRunning = true;
    startTime = 0;
  }

  // -------------------------------------------------------------------------------------------------------------------------

  @Override
  protected void paintComponent(java.awt.Graphics g) {
    super.paintComponent(g);

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

    g2.drawImage(BACKGROUND_IMAGE, 0, 0, this);

    g2.drawImage(DIGIT_ARRAY[hour_left], 38 + 238, 90, this);
    g2.drawImage(DIGIT_ARRAY[hour_mid], 84 + 238, 90, this);
    g2.drawImage(DIGIT_ARRAY[hour_right], 130 + 238, 90, this);

    if (dotsOn) {
      g2.drawImage(DOTS_ON, 172 + 87, 90, this);
      g2.drawImage(DOTS_ON, 301 - 175, 90, this);
    } else {
      g2.drawImage(DOTS_OFF, 172 + 87, 90, this);
      g2.drawImage(DOTS_OFF, 301 - 175, 90, this);
    }

    g2.drawImage(DIGIT_ARRAY[min_left], 213 - 175, 90, this);
    g2.drawImage(DIGIT_ARRAY[min_right], 259 - 175, 90, this);

    g2.drawImage(DIGIT_ARRAY[sec_left], 319 - 175, 90, this);
    g2.drawImage(DIGIT_ARRAY[sec_right], 365 - 175, 90, this);

    // the level meter
    if (timerRunning == true) {
      if (level > 15) level = 15;
      if (level < 0) level = 0;
      for (int i = 0; i <= Math.min(level, 15); i++)
        g2.drawImage(DIGIT_ARRAY[i + 10], 458, 156 - i * 7, this);
      if (level > (int) levelPeak) levelPeak = (float) level;
      levelPeak -= 0.05;
      if (levelPeak < 0.0f) levelPeak = 0.0f;
      if (levelPeak > 15.0f) levelPeak = 15.0f;
      g2.drawImage(DIGIT_ARRAY[(int) levelPeak + 10], 458, 156 - (int) levelPeak * 7, this);
    }

    g2.dispose();
  }

  // -------------------------------------------------------------------------------------------------------------------------

  private java.awt.image.BufferedImage createBackground(int width, int height) {
    java.awt.GraphicsConfiguration gfxConf =
        java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();
    java.awt.image.BufferedImage IMAGE =
        gfxConf.createCompatibleImage(width, height, java.awt.Transparency.TRANSLUCENT);

    java.awt.Graphics2D g2 = (java.awt.Graphics2D) IMAGE.getGraphics();
    g2.setRenderingHint(
        java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
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

    final float[] FRACTIONS = {0.0f, 1.0f};

    final java.awt.Color[] COLORS_BACKGROUND = {
      new java.awt.Color(0, 0, 0 /*0x253048*/), new java.awt.Color(30, 30, 30 /*0x182635*/)
    };

    final java.awt.geom.Point2D START_HIGHLIGHT = new java.awt.geom.Point2D.Float(0, 79);
    final java.awt.geom.Point2D STOP_HIGHLIGHT = new java.awt.geom.Point2D.Float(0, 166);

    final float[] FRACTIONS_HIGHLIGHT = {0.0f, 0.82f, 1.0f};
    final java.awt.Color[] COLORS_HIGHLIGHT = {
      new java.awt.Color(0x000000), new java.awt.Color(0x000000), new java.awt.Color(0x6C8095)
    };

    final java.awt.geom.Point2D START = new java.awt.geom.Point2D.Float(0, 80);
    final java.awt.geom.Point2D STOP = new java.awt.geom.Point2D.Float(0, 165);

    final java.awt.Color[] COLORS_LEFT = {
      new java.awt.Color(20, 40, 50), new java.awt.Color(10, 28, 30)
    };
    final java.awt.Color[] COLORS_RIGHT = {
      new java.awt.Color(30, 48, 59), new java.awt.Color(11, 23, 32)
    };

    // the background
    final java.awt.LinearGradientPaint GRADIENT_BACKGROUND =
        new java.awt.LinearGradientPaint(
            START_BACKGROUND, STOP_BACKGROUND, FRACTIONS, COLORS_BACKGROUND);
    g2.setPaint(GRADIENT_BACKGROUND);
    g2.fillRect(0, 0, width, height);

    // the inlet
    final java.awt.LinearGradientPaint GRADIENT_HIGHLIGHT =
        new java.awt.LinearGradientPaint(
            START_HIGHLIGHT, STOP_HIGHLIGHT, FRACTIONS_HIGHLIGHT, COLORS_HIGHLIGHT);
    g2.setPaint(GRADIENT_HIGHLIGHT);
    g2.fillRect(17, 79, 176, 87); // left
    g2.fillRect(193, 79, 239, 87); // right
    g2.fillRect(454, 46, 50, 120); // levelmeter

    // the screws
    try {
      g2.drawImage(
          javax.imageio.ImageIO.read(Sonogram.class.getResource("Schraube1.png")), 2, 163, null);
    } catch (Throwable t) {
    }

    // the left digit gradient
    final java.awt.LinearGradientPaint GRADIENT_LEFT =
        new java.awt.LinearGradientPaint(START, STOP, FRACTIONS, COLORS_LEFT);
    g2.setPaint(GRADIENT_LEFT);
    g2.fillRect(18, 80, 238, 85);
    g2.fillRect(455, 47, 48, 118);

    // the right digit gradient
    final java.awt.LinearGradientPaint GRADIENT_RIGHT =
        new java.awt.LinearGradientPaint(START, STOP, FRACTIONS, COLORS_RIGHT);
    g2.setPaint(GRADIENT_RIGHT);
    g2.fillRect(256, 80, 175, 85);
    g2.fillRect(455, 47, 48, 118);

    // the middle line
    g2.setColor(new java.awt.Color(0x6C8095));
    g2.drawLine(256, 81, 256, 164);
    g2.setColor(new java.awt.Color(0x000000));
    g2.drawLine(257, 81, 257, 164);

    // the lines arrounf the text
    final java.awt.BasicStroke STROKE =
        new java.awt.BasicStroke(
            4.0f, java.awt.BasicStroke.CAP_SQUARE, java.awt.BasicStroke.JOIN_BEVEL);
    g2.setStroke(STROKE);
    g2.setColor(new java.awt.Color(0xDDF3F4));
    g2.drawLine(19, 30, 19, 36);
    g2.drawLine(19, 30, 130, 30);
    g2.drawLine(317, 30, 429, 30);
    g2.drawLine(429, 30, 429, 36);

    g2.drawLine(455, 30, 455, 36);
    g2.drawLine(455, 30, 477, 30);
    g2.drawLine(519, 30, 541, 30);
    g2.drawLine(541, 30, 541, 36);

    g2.setFont(new java.awt.Font("Arial", 1, 22));
    g2.drawString("RECORD TIMER", 140, 37);
    g2.drawString("VU", 484, 37);

    g2.setFont(new java.awt.Font("Arial", 1, 16));
    g2.drawString("MS", 335, 65);
    g2.drawString("MIN", 75, 65);
    g2.drawString("SEC", 175, 65);

    g2.setFont(new java.awt.Font("Arial", 1, 10));
    g2.drawString("0 dB", 519, 60);
    g2.drawString("-10 dB", 511, 80);
    g2.drawString("-20 dB", 511, 100);
    g2.drawString("-30 dB", 511, 120);
    g2.drawString("-40 dB", 511, 140);
    g2.drawString("-50 dB", 511, 160);

    g2.setColor(COLOR_OFF);
    g2.fillRect(458, 50, 43, 6);

    // level meter
    for (int i = 0; i < 16; i++) g2.fillRect(458, 52 + i * 7, 43, 6);

    g2.dispose();

    return IMAGE;
  }

  // -------------------------------------------------------------------------------------------------------------------------

  private java.awt.image.BufferedImage createDigit(int digit) {
    java.awt.GraphicsConfiguration gfxConf =
        java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();
    java.awt.image.BufferedImage IMAGE;
    if (digit < 0) IMAGE = gfxConf.createCompatibleImage(43, 6, java.awt.Transparency.TRANSLUCENT);
    else IMAGE = gfxConf.createCompatibleImage(46, 65, java.awt.Transparency.TRANSLUCENT);

    final java.awt.BasicStroke FRAME_STROKE =
        new java.awt.BasicStroke(
            1.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND);

    java.awt.Graphics2D g2 = (java.awt.Graphics2D) IMAGE.getGraphics();
    g2.setRenderingHint(
        java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
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

    if (digit < 0) {
      java.awt.Color c = new java.awt.Color(0, 0, 0);
      if (digit == -1) c = new java.awt.Color(14, 167, 29);
      else if (digit == -2) c = new java.awt.Color(15, 167, 32);
      else if (digit == -3) c = new java.awt.Color(13, 170, 21);
      else if (digit == -4) c = new java.awt.Color(13, 168, 26);
      else if (digit == -5) c = new java.awt.Color(54, 203, 17);
      else if (digit == -6) c = new java.awt.Color(87, 216, 14);
      else if (digit == -7) c = new java.awt.Color(152, 234, 18);
      else if (digit == -8) c = new java.awt.Color(209, 240, 10);
      else if (digit == -9) c = new java.awt.Color(235, 240, 17);
      else if (digit == -10) c = new java.awt.Color(240, 235, 17);
      else if (digit == -11) c = new java.awt.Color(244, 223, 10);
      else if (digit == -12) c = new java.awt.Color(252, 175, 19);
      else if (digit == -13) c = new java.awt.Color(252, 147, 22);
      else if (digit == -14) c = new java.awt.Color(253, 85, 20);
      else if (digit == -15) c = new java.awt.Color(251, 57, 24);
      else if (digit == -16) c = new java.awt.Color(255, 17, 20);

      g2.setColor(c);
      g2.fillRect(0, 0, 43, 6);

      g2.dispose();
      return IMAGE;
    }

    // A
    java.awt.geom.GeneralPath segment_a = new java.awt.geom.GeneralPath();
    segment_a.moveTo(17, 0);
    segment_a.lineTo(38, 0);
    segment_a.lineTo(37, 8);
    segment_a.lineTo(16, 8);
    segment_a.closePath();

    // B
    java.awt.geom.GeneralPath segment_b = new java.awt.geom.GeneralPath();
    segment_b.moveTo(39, 0);
    segment_b.lineTo(41, 0);
    segment_b.quadTo(45, 0, 45, 5);
    segment_b.lineTo(41, 32);
    segment_b.lineTo(38, 32);
    segment_b.lineTo(35, 28);
    segment_b.closePath();

    // C
    java.awt.geom.GeneralPath segment_c = new java.awt.geom.GeneralPath();
    segment_c.moveTo(37, 33);
    segment_c.lineTo(41, 33);
    segment_c.lineTo(37, 60);
    segment_c.quadTo(36, 65, 32, 65);
    segment_c.lineTo(30, 65);
    segment_c.lineTo(34, 37);
    segment_c.closePath();

    // D
    java.awt.geom.GeneralPath segment_d = new java.awt.geom.GeneralPath();
    segment_d.moveTo(9, 57);
    segment_d.lineTo(30, 57);
    segment_d.lineTo(29, 65);
    segment_d.lineTo(8, 65);
    segment_d.closePath();

    // E
    java.awt.geom.GeneralPath segment_e = new java.awt.geom.GeneralPath();
    segment_e.moveTo(4, 33);
    segment_e.lineTo(8, 33);
    segment_e.lineTo(11, 37);
    segment_e.lineTo(7, 65);
    segment_e.lineTo(4, 65);
    segment_e.quadTo(0, 65, 0, 60);
    segment_e.closePath();

    // F
    java.awt.geom.GeneralPath segment_f = new java.awt.geom.GeneralPath();
    segment_f.moveTo(8, 5);
    segment_f.quadTo(8, 0, 13, 0);
    segment_f.lineTo(16, 0);
    segment_f.lineTo(12, 28);
    segment_f.lineTo(8, 32);
    segment_f.lineTo(4, 32);
    segment_f.closePath();

    // G
    java.awt.geom.GeneralPath segment_g = new java.awt.geom.GeneralPath();
    segment_g.moveTo(14, 29);
    segment_g.lineTo(34, 29);
    segment_g.lineTo(36, 33);
    segment_g.lineTo(32, 37);
    segment_g.lineTo(13, 37);
    segment_g.lineTo(9, 33);
    segment_g.closePath();

    g2.setStroke(FRAME_STROKE);

    switch (digit) {
      case 0:
        g2.setColor(COLOR_ON);
        g2.fill(segment_a);
        g2.fill(segment_b);
        g2.fill(segment_c);
        g2.fill(segment_d);
        g2.fill(segment_e);
        g2.fill(segment_f);
        g2.setColor(COLOR_OFF);
        g2.fill(segment_g);
        g2.setColor(FRAME_COLOR_ON);
        g2.draw(segment_a);
        g2.draw(segment_b);
        g2.draw(segment_c);
        g2.draw(segment_d);
        g2.draw(segment_e);
        g2.draw(segment_f);
        g2.setColor(FRAME_COLOR_OFF);
        g2.draw(segment_g);
        break;
      case 1:
        g2.setColor(COLOR_ON);
        g2.fill(segment_b);
        g2.fill(segment_c);
        g2.setColor(COLOR_OFF);
        g2.fill(segment_a);
        g2.fill(segment_d);
        g2.fill(segment_e);
        g2.fill(segment_f);
        g2.fill(segment_g);
        g2.setColor(FRAME_COLOR_ON);
        g2.draw(segment_b);
        g2.draw(segment_c);
        g2.setColor(FRAME_COLOR_OFF);
        g2.draw(segment_a);
        g2.draw(segment_d);
        g2.draw(segment_e);
        g2.draw(segment_f);
        g2.draw(segment_g);
        break;
      case 2:
        g2.setColor(COLOR_ON);
        g2.fill(segment_a);
        g2.fill(segment_b);
        g2.fill(segment_d);
        g2.fill(segment_e);
        g2.fill(segment_g);
        g2.setColor(COLOR_OFF);
        g2.fill(segment_c);
        g2.fill(segment_f);
        g2.setColor(FRAME_COLOR_ON);
        g2.draw(segment_a);
        g2.draw(segment_b);
        g2.draw(segment_d);
        g2.draw(segment_e);
        g2.draw(segment_g);
        g2.setColor(FRAME_COLOR_OFF);
        g2.draw(segment_c);
        g2.draw(segment_f);
        break;
      case 3:
        g2.setColor(COLOR_ON);
        g2.fill(segment_a);
        g2.fill(segment_b);
        g2.fill(segment_c);
        g2.fill(segment_d);
        g2.fill(segment_g);
        g2.setColor(COLOR_OFF);
        g2.fill(segment_e);
        g2.fill(segment_f);
        g2.setColor(FRAME_COLOR_ON);
        g2.draw(segment_a);
        g2.draw(segment_b);
        g2.draw(segment_c);
        g2.draw(segment_d);
        g2.draw(segment_g);
        g2.setColor(FRAME_COLOR_OFF);
        g2.draw(segment_e);
        g2.draw(segment_f);
        break;
      case 4:
        g2.setColor(COLOR_ON);
        g2.fill(segment_b);
        g2.fill(segment_c);
        g2.fill(segment_f);
        g2.fill(segment_g);
        g2.setColor(COLOR_OFF);
        g2.fill(segment_a);
        g2.fill(segment_d);
        g2.fill(segment_e);
        g2.setColor(FRAME_COLOR_ON);
        g2.draw(segment_b);
        g2.draw(segment_c);
        g2.draw(segment_f);
        g2.draw(segment_g);
        g2.setColor(FRAME_COLOR_OFF);
        g2.draw(segment_a);
        g2.draw(segment_d);
        g2.draw(segment_e);
        break;
      case 5:
        g2.setColor(COLOR_ON);
        g2.fill(segment_a);
        g2.fill(segment_c);
        g2.fill(segment_d);
        g2.fill(segment_f);
        g2.fill(segment_g);
        g2.setColor(COLOR_OFF);
        g2.fill(segment_b);
        g2.fill(segment_e);
        g2.setColor(FRAME_COLOR_ON);
        g2.draw(segment_a);
        g2.draw(segment_c);
        g2.draw(segment_d);
        g2.draw(segment_f);
        g2.draw(segment_g);
        g2.setColor(FRAME_COLOR_OFF);
        g2.draw(segment_b);
        g2.draw(segment_e);
        break;
      case 6:
        g2.setColor(COLOR_ON);
        g2.fill(segment_a);
        g2.fill(segment_c);
        g2.fill(segment_d);
        g2.fill(segment_e);
        g2.fill(segment_f);
        g2.fill(segment_g);
        g2.setColor(COLOR_OFF);
        g2.fill(segment_b);
        g2.setColor(FRAME_COLOR_ON);
        g2.draw(segment_a);
        g2.draw(segment_c);
        g2.draw(segment_d);
        g2.draw(segment_e);
        g2.draw(segment_f);
        g2.draw(segment_g);
        g2.setColor(FRAME_COLOR_OFF);
        g2.draw(segment_b);
        break;
      case 7:
        g2.setColor(COLOR_ON);
        g2.fill(segment_a);
        g2.fill(segment_b);
        g2.fill(segment_c);
        g2.setColor(COLOR_OFF);
        g2.fill(segment_d);
        g2.fill(segment_e);
        g2.fill(segment_f);
        g2.fill(segment_g);
        g2.setColor(FRAME_COLOR_ON);
        g2.draw(segment_a);
        g2.draw(segment_b);
        g2.draw(segment_c);
        g2.setColor(FRAME_COLOR_OFF);
        g2.draw(segment_d);
        g2.draw(segment_e);
        g2.draw(segment_f);
        g2.draw(segment_g);
        break;
      case 8:
        g2.setColor(COLOR_ON);
        g2.fill(segment_a);
        g2.fill(segment_b);
        g2.fill(segment_c);
        g2.fill(segment_d);
        g2.fill(segment_e);
        g2.fill(segment_f);
        g2.fill(segment_g);
        g2.setColor(FRAME_COLOR_ON);
        g2.draw(segment_a);
        g2.draw(segment_b);
        g2.draw(segment_c);
        g2.draw(segment_d);
        g2.draw(segment_e);
        g2.draw(segment_f);
        g2.draw(segment_g);
        break;
      case 9:
        g2.setColor(COLOR_ON);
        g2.fill(segment_a);
        g2.fill(segment_b);
        g2.fill(segment_c);
        g2.fill(segment_d);
        g2.fill(segment_f);
        g2.fill(segment_g);
        g2.setColor(COLOR_OFF);
        g2.fill(segment_e);
        g2.setColor(FRAME_COLOR_ON);
        g2.draw(segment_a);
        g2.draw(segment_b);
        g2.draw(segment_c);
        g2.draw(segment_d);
        g2.draw(segment_f);
        g2.draw(segment_g);
        g2.setColor(FRAME_COLOR_OFF);
        g2.draw(segment_e);
        break;
      default:
        g2.setColor(COLOR_OFF);
        g2.fill(segment_a);
        g2.fill(segment_b);
        g2.fill(segment_c);
        g2.fill(segment_d);
        g2.fill(segment_e);
        g2.fill(segment_f);
        g2.fill(segment_g);
        g2.setColor(FRAME_COLOR_OFF);
        g2.draw(segment_a);
        g2.draw(segment_b);
        g2.draw(segment_c);
        g2.draw(segment_d);
        g2.draw(segment_e);
        g2.draw(segment_f);
        g2.draw(segment_g);
        break;
    }

    g2.dispose();

    return IMAGE;
  }

  // -------------------------------------------------------------------------------------------------------------------------

  private java.awt.image.BufferedImage createDots(boolean on) {
    java.awt.GraphicsConfiguration gfxConf =
        java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();
    java.awt.image.BufferedImage IMAGE =
        gfxConf.createCompatibleImage(23, 65, java.awt.Transparency.TRANSLUCENT);

    final java.awt.BasicStroke FRAME_STROKE =
        new java.awt.BasicStroke(
            1.0f, java.awt.BasicStroke.CAP_ROUND, java.awt.BasicStroke.JOIN_ROUND);

    java.awt.Graphics2D g2 = (java.awt.Graphics2D) IMAGE.getGraphics();
    g2.setRenderingHint(
        java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
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

    g2.setStroke(FRAME_STROKE);

    if (on) {
      g2.setColor(COLOR_ON);
      g2.fillOval(8, 20, 7, 7);
      g2.fillOval(5, 39, 7, 7);

      g2.setColor(FRAME_COLOR_ON);
      g2.drawOval(8, 20, 7, 7);
      g2.drawOval(5, 39, 7, 7);
    } else {
      g2.setColor(COLOR_OFF);
      g2.fillOval(8, 20, 7, 7);
      g2.fillOval(5, 39, 7, 7);

      g2.setColor(FRAME_COLOR_OFF);
      g2.drawOval(8, 20, 7, 7);
      g2.drawOval(5, 39, 7, 7);
    }

    g2.dispose();

    return IMAGE;
  }

  // -------------------------------------------------------------------------------------------------------------------------

  @Override
  public void actionPerformed(java.awt.event.ActionEvent event) {
    if (event.getSource().equals(PAINT_TIMER)) {

      if (startTime == 0) {
        hour_right = 0;
        hour_mid = 0;
        hour_left = 0;
        sec_right = 0;
        sec_left = 0;
        min_right = 0;
        min_left = 0;
        repaint();
      } else {

        long delta = 0;
        if (timerRunning == true) delta = System.currentTimeMillis() - startTime;
        else delta = stopTime - startTime;
        int minutes = (int) (delta / 60000);
        int seconds = (int) (delta / 1000);

        hour_right = (int) (delta % 10);
        hour_mid = (int) ((delta % 100) / 10);
        hour_left = (int) ((delta % 1000) / 100);

        sec_right = seconds % 10;
        sec_left = (seconds % 60) / 10;

        min_right = minutes % 10;
        min_left = (minutes % 60) / 10;

        if (hour_left >= 0 && hour_left <= 4) dotsOn = true;
        else dotsOn = false;

        repaint();
      }
    }
  }

  // -------------------------------------------------------------------------------------------------------------------------

  @Override
  public java.awt.Dimension getSize() {
    return new java.awt.Dimension(450, 180);
  }

  // -------------------------------------------------------------------------------------------------------------------------

  @Override
  public void mouseClicked(java.awt.event.MouseEvent event) {
    if (event.getButton() == java.awt.event.MouseEvent.BUTTON1) {
      colorSwitch++;
      if (colorSwitch >= 3 || colorSwitch < 0) colorSwitch = 0;
      initDigitArray();
    }
  }

  @Override
  public void mousePressed(java.awt.event.MouseEvent event) {}

  @Override
  public void mouseReleased(java.awt.event.MouseEvent event) {}

  @Override
  public void mouseEntered(java.awt.event.MouseEvent event) {}

  @Override
  public void mouseExited(java.awt.event.MouseEvent event) {}
}
