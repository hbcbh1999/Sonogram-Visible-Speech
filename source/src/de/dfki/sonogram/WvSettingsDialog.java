package de.dfki.sonogram;

import de.dfki.maths.*;
import java.awt.*;
import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

//  (c)      Christoph Lauer Engineering
//
//  @file    smoothed_speudo_wigner_ville_distribution.h
//  @class   SmoothedPseudoWignerVilleDistribution
//  @version 1.0
//  @date    2008
//  @author  Christoph Lauer
//  @brief   This class implements a smoothed-pseudo-wigner-ville-distribution (SPWVD)
//  @see     http://en.wikipedia.org/wiki/Wigner_quasi-probability_distribution
//  @see     http://en.wikipedia.org/wiki/Cohen's_class_distribution_function
//  @todo    finished and tested so far.

// ----------------------------------------------------------------------------------------------------------------

class WvSettingsDialog extends JDialog {
  public static int resolution;
  public static int length;
  public double[][] spwv;
  public JSlider sliderx;
  public JSlider slidery;
  private JSlider logSlider;
  private JSlider resSlider;
  public JComboBox colCombo;
  public JFrame frame;
  public static boolean wvmessage;
  private static Sonogram sono;
  private BufferedImage image;
  private static Image scaledImage;
  private JTextField selectedSamplesText;
  private ImagePanel ip;
  private double scaleX;
  private double scaleY;
  private static JSlider zoomx;
  private static JSlider zoomy;
  private JScrollPane jScrollPane;
  private int heightOffset;
  private int widthOffset;
  private JCheckBox cb1, cb2, cb3, cb4;
  private JRadioButton rb1, rb2;
  private JButton perButton, okButton;
  private static Color linealBgColor;
  private static Color linealFgColor;

  public enum RenderType {
    RENDER_2D,
    RENDER_3D,
    RENDER_IMAGE
  }

  public RenderType renderType;
  private static int startp;
  final int defaultTimeout = ToolTipManager.sharedInstance().getInitialDelay();
  private boolean fullscreen = false;
  JButton zoomone;

  // ----------------------------------------------------------------------------------------------------------------

  public void setSelectedSamples(int samples) {
    selectedSamplesText.setText(" Selected Area: " + samples + " Samples");
  }

  // ----------------------------------------------------------------------------------------------------------------

  public WvSettingsDialog(Frame owner) {
    super(owner, "SPWVD - Generation Settings");
    sono = (Sonogram) owner;
    Toolkit tk = Toolkit.getDefaultToolkit();
    setIconImage(tk.getImage(Sonogram.class.getResource("Sonogram.gif")));
    linealBgColor = new Color(180, 230, 30);
    linealFgColor = new Color(150, 50, 50);
    String OS = System.getProperties().getProperty("os.name");
    if (OS.contains("Mac") == true) {
      heightOffset = 105;
      widthOffset = 63;
    } else {
      heightOffset = 107;
      widthOffset = 53;
    }
    wvmessage = sono.iwvconfirmed;

    getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

    // GENERIC PARAMETER
    JPanel p1 = new JPanel();
    p1.setLayout(new GridLayout(2, 0));
    p1.setBorder(new TitledBorder(new EtchedBorder(), "SPWV Generation Parameters"));
    JPanel p11 = new JPanel();
    p11.setLayout(new GridLayout(0, 2));
    selectedSamplesText = new JTextField(" Selected Area: 0 Samples");
    p11.add(selectedSamplesText);
    colCombo =
        new JComboBox(
            new Object[] {
              "Classical Fire Colors",
              "Fire Colors",
              "Extended Fire Colors",
              "Rainbow Colors",
              "Purpur Purple",
              "Classical Black/White"
            });
    Map<Object, Icon> icons = new HashMap<Object, Icon>();
    icons.put("Classical Fire Colors", new ImageIcon(Sonogram.class.getResource("firec.png")));
    icons.put("Fire Colors", new ImageIcon(Sonogram.class.getResource("fire.gif")));
    icons.put("Extended Fire Colors", new ImageIcon(Sonogram.class.getResource("col.gif")));
    icons.put("Rainbow Colors", new ImageIcon(Sonogram.class.getResource("rai.gif")));
    icons.put("Purpur Purple", new ImageIcon(Sonogram.class.getResource("gre.gif")));
    icons.put("Classical Black/White", new ImageIcon(Sonogram.class.getResource("bw.gif")));
    colCombo.setRenderer(new IconListRenderer(icons));
    addComponentListener(
        new ComponentListener() {
          public void componentHidden(ComponentEvent e) {}

          public void componentMoved(ComponentEvent e) {}

          public void componentResized(ComponentEvent e) {}

          public void componentShown(ComponentEvent e) {
            if (frame != null) {
              frame.dispose();
              releaseMemory();
            }
            if (wvmessage == false) {
              JCheckBox askAgain =
                  new JCheckBox(
                      "<html><i><font size = -2>Do not show this information message again");
              Object[] message = new Object[2];
              message[0] =
                  "<html>The <i>Smoothed-Pseudo-Wigner-Ville-Distribution</i> (<u>SPWVD</u>) allows"
                      + " very deep zooms into <br>the Time and Frequency domain with a <b>so far"
                      + " unknown accuracy</b>. Unlike the classical FFT<br>we can overcome the"
                      + " uncertainty principle. You can select the Dimension of the"
                      + " rendered<br>SPWVD-Spectogram in this Settings-Dialog. Starting point for"
                      + " the transformation is the<br><u>start point of the selection</u> in the"
                      + " Sonogram. The length of the Spectogram can be selected<br>with the"
                      + " <i>\"(X-Size) Windowlengt in Samples\"</i> slider, and the Frequency"
                      + " image resolution can<br>be selected with <i>\"(Y-Size) Frequency"
                      + " Resolution\"</i> slider. One Pixel lengt in X-Direction of"
                      + " the<br>rendered Image equals exactely one raw Sample in the Time-Domain,"
                      + " so the length of the <br>generated SPWVD corresponds directely to the"
                      + " number of raw samples. Note that <u>very large<br>Spectograms are memory"
                      + " intensive</u>, so the render algorithm can run into memory"
                      + " problems<br>for big Spectograms. The SuperZOOM Feature is only available"
                      + " for relatively small spektrum<br>sizes and the SuperZOOM Button will be"
                      + " automatically enabled/disabled. Every time a new <br>SPWVD-Spectrum is"
                      + " rendered the old one will be deleted. Despite the massive"
                      + " parallelization<br>of the core Algorithm, <u>the render process is very"
                      + " computationally intensive and could take</u>.<br>time. Use the"
                      + " <i>Right-Mouse-Button</i> for direct rendering without showing the"
                      + " Settings-Dialog.";
              ImageIcon icon = new ImageIcon(Sonogram.class.getResource("wv.png"));
              message[1] = askAgain;
              JOptionPane.showConfirmDialog(
                  frame,
                  message,
                  "Welcome to the Smoothed-Pseudo-Wigner-Ville-Distribution (SPWVD)",
                  JOptionPane.DEFAULT_OPTION,
                  JOptionPane.INFORMATION_MESSAGE,
                  icon);
              wvmessage = askAgain.isSelected();
            }

            int col = 0;
            if (sono.gad.r0.isSelected() == true) col = 0;
            if (sono.gad.r1.isSelected() == true) col = 1;
            if (sono.gad.r2.isSelected() == true) col = 2;
            if (sono.gad.r3.isSelected() == true) col = 3;
            if (sono.gad.r4.isSelected() == true) col = 4;
            if (sono.gad.r5.isSelected() == true) col = 5;
            colCombo.setSelectedIndex(col);
          }
        });
    p11.add(colCombo);
    p1.add(p11);
    JPanel p12 = new JPanel();
    p12.setLayout(new GridLayout(1, 3));
    cb3 = new JCheckBox("Zoom 1:1");
    cb3.setToolTipText("Zoom the Image to fit the window size");
    cb3.setSelected(true);
    cb2 = new JCheckBox("Fullscreen");
    cb2.setToolTipText("Maximize the Window");
    cb1 = new JCheckBox("Color Scale");
    cb1.setToolTipText("Paint the Aplitude Color-Scale from 0...1");
    p12.add(cb3);
    p12.add(cb2);
    p12.add(cb1);
    p1.add(p12);
    getContentPane().add(p1);

    // LOGARITHMIZATION PANEL
    JPanel p2 = new JPanel();
    p2.setBorder(new TitledBorder(new EtchedBorder(), "Amplitude Logarithmization"));
    p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));
    JPanel p21 = new JPanel();
    p21.setLayout(new GridLayout(1, 3));
    rb1 = new JRadioButton("Logarithm");
    rb1.setSelected(true);
    rb2 = new JRadioButton("Linear");
    rb1.addChangeListener(
        new ChangeListener() {
          public void stateChanged(ChangeEvent e) {
            logSlider.setEnabled(rb1.isSelected());
          }
        });
    rb2 = new JRadioButton("Linear");
    rb2.addChangeListener(
        new ChangeListener() {
          public void stateChanged(ChangeEvent e) {
            logSlider.setEnabled(rb1.isSelected());
          }
        });
    ButtonGroup group = new ButtonGroup();
    group.add(rb1);
    group.add(rb2);
    p21.add(rb1);
    p21.add(rb2);
    cb4 = new JCheckBox("Subtract 5%");
    cb4.setToolTipText(
        "<html>Set the <b>first 5% of the Amplitude to zero</b><br>so the image gets artificially"
            + " more contrast.");
    p21.add(cb4);
    p2.add(p21);
    logSlider = new JSlider(SwingConstants.HORIZONTAL, 10, 200, 80);
    Hashtable h1 = new Hashtable();
    h1.put(Integer.valueOf(10), new JLabel("   Max    "));
    h1.put(Integer.valueOf(92), new JLabel("<html>|<font size=-4 color=666666>&lt;-standard"));
    h1.put(Integer.valueOf(200), new JLabel("     Min  "));
    logSlider.setLabelTable(h1);
    logSlider.setPaintLabels(true);
    logSlider.setSnapToTicks(true);
    logSlider.setMinorTickSpacing(3);
    logSlider.setPaintTicks(true);
    p2.add(logSlider);
    getContentPane().add(p2);

    // RESOLUTION PANEL
    JPanel p5 = new JPanel();
    p5.setBorder(new TitledBorder(new EtchedBorder(), "Algorithm Time/Frequency Resolution"));
    resSlider = new JSlider(SwingConstants.HORIZONTAL, 80, 130, 100);
    Hashtable h5 = new Hashtable();
    h5.put(Integer.valueOf(80), new JLabel("   Min"));
    h5.put(Integer.valueOf(104), new JLabel("<html>| <font size=-4 color=666666> &lt;- standard"));
    h5.put(Integer.valueOf(130), new JLabel("Max   "));
    resSlider.setLabelTable(h5);
    resSlider.setPaintLabels(true);
    resSlider.setSnapToTicks(true);
    resSlider.setMinorTickSpacing(1);
    resSlider.setPaintTicks(true);
    resSlider.setPreferredSize(new Dimension(400, 42));
    p5.add(resSlider);
    getContentPane().add(p5);

    // IMAGE SIZE PANEL
    JPanel p3 = new JPanel();
    p3.setLayout(new GridLayout(2, 1));
    p3.setBorder(new TitledBorder(new EtchedBorder(), "SPWV Size Settings"));
    sliderx = new JSlider(JSlider.HORIZONTAL, 8, 15, 10);
    Hashtable h2 = new Hashtable();
    h2.put(Integer.valueOf(8), new JLabel("256"));
    h2.put(Integer.valueOf(9), new JLabel("512"));
    h2.put(Integer.valueOf(10), new JLabel("1024"));
    h2.put(Integer.valueOf(11), new JLabel("2048"));
    h2.put(Integer.valueOf(12), new JLabel("4096"));
    h2.put(Integer.valueOf(13), new JLabel("8192"));
    h2.put(Integer.valueOf(14), new JLabel("16k"));
    h2.put(Integer.valueOf(15), new JLabel("32k"));
    sliderx.setLabelTable(h2);
    sliderx.setPaintLabels(true);
    sliderx.setSnapToTicks(true);
    sliderx.setMinorTickSpacing(1);
    sliderx.setPaintTicks(true);
    sliderx.setPreferredSize(new Dimension(400, 70));
    sliderx.setBorder(new TitledBorder(new EtchedBorder(), "(X-Size) - Windowlength in Samples"));
    sliderx.addChangeListener(
        new ChangeListener() {
          public void stateChanged(ChangeEvent e) {
            // System.out.println(sliderx.getValue()*slidery.getValue());
            if (sliderx.getValue() * slidery.getValue() <= 150) {
              perButton.setEnabled(true);
              // perButton.setBorder(BorderFactory.createLineBorder(new Color(90,90,160)));
            } else perButton.setEnabled(false);
            // perButton.setBorder(BorderFactory.createCompoundBorder());
          }
        });
    slidery = new JSlider(JSlider.HORIZONTAL, 8, 13, 10);
    Hashtable h3 = new Hashtable();
    h3.put(Integer.valueOf(8), new JLabel("256"));
    h3.put(Integer.valueOf(9), new JLabel("512"));
    h3.put(Integer.valueOf(10), new JLabel("1024"));
    h3.put(Integer.valueOf(11), new JLabel("2048"));
    h3.put(Integer.valueOf(12), new JLabel("4096"));
    h3.put(Integer.valueOf(13), new JLabel("8192"));
    slidery.setLabelTable(h3);
    slidery.setPaintLabels(true);
    slidery.setSnapToTicks(true);
    slidery.setMinorTickSpacing(1);
    slidery.setPaintTicks(true);
    slidery.setPreferredSize(new Dimension(400, 70));
    slidery.setBorder(new TitledBorder(new EtchedBorder(), "(Y-Size) - Frequency Resolution"));
    slidery.addChangeListener(
        new ChangeListener() {
          public void stateChanged(ChangeEvent e) {
            // System.out.println(sliderx.getValue()*slidery.getValue());
            if (sliderx.getValue() * slidery.getValue() <= 150) perButton.setEnabled(true);
            else perButton.setEnabled(false);
          }
        });
    p3.add(sliderx);
    p3.add(slidery);
    getContentPane().add(p3);

    // BUTTON PANEL
    JPanel p4 = new JPanel();
    p4.setLayout(new GridLayout(1, 3));
    okButton = new JButton("Render");
    okButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            renderType = RenderType.RENDER_2D;
            generateHandler();
          }
        });
    perButton = new JButton("3D");
    perButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            renderType = RenderType.RENDER_3D;
            generateHandler();
          }
        });
    JButton saveButton = new JButton("Save to File");
    saveButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            renderType = RenderType.RENDER_IMAGE;
            generateHandler();
          }
        });
    JButton cancelButton = new JButton("Abort");
    cancelButton.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            setVisible(false);
          }
        });
    p4.add(okButton);
    p4.add(saveButton);
    p4.add(perButton);
    p4.add(cancelButton);
    getContentPane().add(p4);

    pack();
    setLocationRelativeTo(null);
  }

  // ----------------------------------------------------------------------------------------------------------------

  public class IconListRenderer extends DefaultListCellRenderer {
    private Map<Object, Icon> icons = null;

    // ------------------------------------------------------------------------------------------------------------

    public IconListRenderer(Map<Object, Icon> icons) {
      this.icons = icons;
    }

    // ------------------------------------------------------------------------------------------------------------

    public Component getListCellRendererComponent(
        JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
      JLabel label =
          (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      Icon icon = icons.get(value);
      label.setIcon(icon);
      return label;
    }
  }

  // ----------------------------------------------------------------------------------------------------------------

  public void generateHandler() {

    Thread thread =
        new Thread(
            new Runnable() {
              public void run() {
                ProgressMonitor pm =
                    new ProgressMonitor(sono, "", "parallelize Algorithm..", 0, 100);
                pm.setMillisToDecideToPopup(0);
                pm.setMillisToPopup(0);

                try {
                  pm.setProgress(1);
                  setVisible(false);
                  // determine window size
                  length = (int) Math.pow(2.0, (double) sliderx.getValue());
                  if (sono.samplesall < 256) {
                    JOptionPane.showMessageDialog(
                        sono,
                        "<html><font size=5 Color=#AA0000>The Signal length is to Short !",
                        "Signal length lesser than 256 Smaples",
                        JOptionPane.ERROR_MESSAGE);
                    return;
                  }

                  // access the start point of the selection
                  double[] buffer = new double[length];
                  double start = sono.selectedstart;
                  startp = (int) (start * (double) sono.samplesall);
                  if (startp > (sono.samplesall - length)) {
                    startp = sono.samplesall - length;
                    if (startp >= 0) {
                      JOptionPane.showMessageDialog(
                          sono,
                          "<html><i>The selected <u>time span is to short</u> and touches<br>"
                              + "the end of the time Signal. The starting point of<br>"
                              + "the selection will be moved Forward, so the SPWVD-<br>"
                              + "Spectogram can be generated with the selected<br>"
                              + "Windowlengt",
                          "Selection starting Point moved Forward",
                          JOptionPane.INFORMATION_MESSAGE);
                    } else {
                      JOptionPane.showMessageDialog(
                          sono,
                          "<html><i>The overall <u>Signal length is to short</u> for the selected"
                              + " Windowlength !<br>We try to <u>decrease the Windowlengt</u> to"
                              + " the half and render again...",
                          "Signal to Short for Windowlengt",
                          JOptionPane.WARNING_MESSAGE);
                      sliderx.setValue(sliderx.getValue() - 1);
                      generateHandler();
                      return;
                    }
                  }
                  Byte tempbyte;
                  for (int i = 0; i < length; i++) {
                    tempbyte = (Byte) sono.reader.audioStream.get(startp + i);
                    buffer[i] = tempbyte.doubleValue();
                  }

                  // extract the frequency resolution
                  resolution = (int) Math.pow(2.0, (double) slidery.getValue());
                  // call the core algorithm
                  SmoothedPseudoWignerVilleDistribution SPWV =
                      new SmoothedPseudoWignerVilleDistribution();
                  SPWV.pm = pm;
                  spwv =
                      SPWV.calculateSmoothedPseudoWignerVilleDistribution(
                          buffer,
                          length,
                          resolution,
                          (int) Math.pow(2.0, (double) resSlider.getValue() / 10));
                  // normalize the result
                  double max = Double.MIN_VALUE;
                  double min = Double.MAX_VALUE;
                  pm.setNote("searching Peaks...");
                  for (int i = 0; i < length; i++) {
                    pm.setProgress((int) ((double) i / (double) length * 5) + 40);
                    if (pm.isCanceled() == true) {
                      releaseMemory();
                      return;
                    }
                    for (int j = 0; j < resolution; j++) {
                      if (rb1.isSelected() == true)
                        spwv[i][j] =
                            Math.log1p(
                                Math.pow(spwv[i][j], ((double) logSlider.getValue() / 100.0)));
                      // Math.pow(spwv[i][j], 0.28);
                      if (max < spwv[i][j]) max = spwv[i][j];
                      if (min > spwv[i][j]) min = spwv[i][j];
                    }
                  }
                  pm.setNote("normalizing...");
                  for (int i = 0; i < length; i++) {
                    pm.setProgress((int) ((double) i / (double) length * 5) + 45);
                    for (int j = 0; j < resolution; j++) {
                      spwv[i][j] -= min;
                      spwv[i][j] /= max - min;
                      if (cb4.isSelected()) if (spwv[i][j] < 0.05) spwv[i][j] = 0.0;
                    }
                  }
                  // select the colors
                  Color[] colors = new Color[256];
                  switch (colCombo.getSelectedIndex()) {
                    case 0:
                      colors = sono.pp.cocFI;
                      break;
                    case 1:
                      colors = sono.pp.coFI;
                      break;
                    case 2:
                      colors = sono.pp.coCO;
                      break;
                    case 3:
                      colors = sono.pp.coRA;
                      break;
                    case 4:
                      colors = sono.pp.coCG;
                      break;
                    case 5:
                      colors = sono.pp.coSW;
                      break;
                  }
                  //////////////////////////////////////////
                  // RENDER 2D
                  pm.setNote("rendering the Image...");
                  if (renderType == RenderType.RENDER_2D) {
                    // paint the image
                    image = new BufferedImage(length, resolution, BufferedImage.TYPE_INT_RGB);
                    Graphics2D big = image.createGraphics();
                    big.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    for (int i = 0; i < length; i++) {
                      pm.setProgress((int) ((double) i / (double) length * 50) + 50);
                      if (pm.isCanceled() == true) {
                        releaseMemory();
                        return;
                      }
                      for (int j = 0; j < resolution; j++) {
                        int color = (int) ((spwv[i][resolution - j - 1]) * (double) 255);
                        big.setColor(colors[color]);
                        // big.drawLine(i,j,i,j); // this line was buggy !!!
                        big.fillRect(i, j, 1, 1);
                      }
                    }
                    // the color scale
                    // open the presenter
                    ip = new ImagePanel();
                    ip.setPreferredSize(new Dimension(length, resolution));
                    jScrollPane = new JScrollPane(ip);
                    jScrollPane.setHorizontalScrollBarPolicy(
                        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
                    PixelRule horizontalPixelRule = new PixelRule(PixelRule.HORIZONTAL);
                    PixelRule verticalPixelRule = new PixelRule(PixelRule.VERTICAL);
                    jScrollPane.setRowHeaderView(verticalPixelRule);
                    jScrollPane.setColumnHeaderView(horizontalPixelRule);
                    jScrollPane.setWheelScrollingEnabled(true);
                    JLabel[] corners = new JLabel[3];
                    for (int i = 0; i < 3; i++) {
                      corners[i] = new JLabel();
                      corners[i].setBackground(linealBgColor);
                      corners[i].setOpaque(true);
                    }
                    // corners[2].setIcon(new ImageIcon(Sonogram.class.getResource("corner.png")));
                    jScrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, corners[2]);
                    jScrollPane.setCorner(JScrollPane.UPPER_RIGHT_CORNER, corners[1]);
                    jScrollPane.setCorner(JScrollPane.LOWER_LEFT_CORNER, corners[0]);

                    frame =
                        new JFrame(
                            "UltraZOOM - Interpolated Smoothed Pseudo Wigner-Ville Distribution"
                                + " (SPWVD)");
                    frame.setMinimumSize(new Dimension(100, 100));
                    WindowListener listener =
                        new WindowAdapter() {
                          public void windowClosing(WindowEvent w) {
                            System.out.println("--> SPWVD Window closed: Release Memory");
                            frame.dispose();
                            releaseMemory();
                          }
                        };
                    frame.addWindowListener(listener);

                    WindowStateListener listenerState =
                        new WindowStateListener() {
                          public void windowStateChanged(WindowEvent e) {
                            if (e.getOldState() == Frame.NORMAL
                                && e.getNewState() == Frame.MAXIMIZED_BOTH) {
                              System.out.println("--> SPWVD Window state changed: FULLSCREEN");
                              fullscreen = true;
                            }
                            if (e.getOldState() == Frame.MAXIMIZED_BOTH
                                && e.getNewState() == Frame.NORMAL) {
                              System.out.println("--> SPWVD Window state changed: NORMAL");
                              fullscreen = false;
                            }
                          }
                        };
                    frame.addWindowStateListener(listenerState);
                    Toolkit tk = Toolkit.getDefaultToolkit();
                    frame.setIconImage(tk.getImage(Sonogram.class.getResource("Sonogram.gif")));
                    frame.addComponentListener(
                        new ComponentListener() {
                          public void componentHidden(ComponentEvent e) {}

                          public void componentMoved(ComponentEvent e) {}

                          public void componentResized(ComponentEvent e) {
                            int w = e.getComponent().getWidth();
                            int h = e.getComponent().getHeight();
                            if (h > resolution + heightOffset) h = resolution + heightOffset;
                            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                            if (h > (int) dim.getHeight()) h = (int) dim.getHeight();
                            e.getComponent().setSize(w, h);
                          }

                          public void componentShown(ComponentEvent e) {}
                        });
                    frame.add(jScrollPane, BorderLayout.CENTER);

                    JToolBar toolbar = new JToolBar();
                    toolbar.setFloatable(false);
                    toolbar.setBorderPainted(false);
                    zoomone = new JButton("1:1");
                    zoomone.setMargin(new Insets(0, 0, 0, 0));
                    zoomone.addActionListener(
                        new ActionListener() {
                          public void actionPerformed(ActionEvent e) {
                            int scrollOffset = 0;
                            if (jScrollPane.getVerticalScrollBar().isVisible() == true)
                              scrollOffset = 16;
                            int x =
                                (int)
                                    (10000.0
                                        * (double)
                                            (jScrollPane.getViewport().getWidth() + scrollOffset)
                                        / (double) image.getWidth());
                            zoomx.setValue(x);
                            int y =
                                (int)
                                    (10000.0
                                        * (double) jScrollPane.getViewport().getHeight()
                                        / (double) image.getHeight());
                            zoomy.setValue(y);
                          }
                        });
                    toolbar.add(zoomone);
                    toolbar.setMaximumSize(new Dimension(10000, 16));
                    toolbar.setMinimumSize(new Dimension(0, 16));
                    zoomx = new JSlider(SwingConstants.HORIZONTAL, 1, 10000, 10000);
                    toolbar.add(zoomx);
                    zoomx.addChangeListener(
                        new ChangeListener() {
                          public void stateChanged(ChangeEvent e) {
                            scaleImage(false);
                          }
                        });
                    zoomx.addMouseListener(
                        new MouseListener() {
                          public void mouseReleased(MouseEvent e) {
                            scaleImage(true);
                          }

                          public void mouseClicked(MouseEvent e) {}

                          public void mouseEntered(MouseEvent e) {}

                          public void mouseExited(MouseEvent e) {}

                          public void mousePressed(MouseEvent e) {}
                        });
                    /*JButton save = new JButton("save");
                    save.setMargin(new Insets(0,0,0,0));
                    save.addActionListener
                    (
                      new ActionListener()
                      {
                        public void actionPerformed(ActionEvent e)
                        {
                        }
                      }
                    );
                    toolbar.add(save);*/
                    frame.add(toolbar, BorderLayout.NORTH);
                    if (pm.isCanceled() == true) {
                      releaseMemory();
                      return;
                    }
                    JToolBar toolbar2 = new JToolBar();
                    toolbar2.setFloatable(false);
                    toolbar2.setBorderPainted(false);
                    zoomy = new JSlider(SwingConstants.VERTICAL, 1, 10000, 10000);
                    zoomy.setInverted(true);
                    toolbar2.add(zoomy);
                    zoomy.addChangeListener(
                        new ChangeListener() {
                          public void stateChanged(ChangeEvent e) {
                            scaleImage(false);
                            ip.updateColorScaleImage();
                          }
                        });
                    frame.add(toolbar2, BorderLayout.WEST);
                    scaleImage(true);
                    ip.updateColorScaleImage();

                    jScrollPane.getViewport().setBackground(new Color(110, 110, 110));
                    jScrollPane.setBackground(new Color(110, 110, 110));
                    frame.setBackground(new Color(110, 110, 110));
                    ip.setBackground(new Color(110, 110, 110));

                    // Window size and location (fullscreen or windowed)
                    frame.setMinimumSize(new Dimension(100, 100));
                    if (cb2.isSelected() == true) {
                      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                      int width = screenSize.width;
                      int height = screenSize.height;
                      frame.setSize(width, height);
                      GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                      frame.setMaximizedBounds(env.getMaximumWindowBounds());
                      frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);
                      fullscreen = true;
                    } else {
                      frame.setSize(
                          (int) Math.min(1024 + widthOffset, (double) length) + widthOffset,
                          512 + heightOffset);
                      frame.setLocationRelativeTo(null);
                      fullscreen = false;
                    }

                    if (pm.isCanceled() == true) {
                      releaseMemory();
                      return;
                    }

                    frame.setVisible(true);
                    if (cb3.isSelected()) zoomone.doClick();
                    setVisible(false);
                  }
                  /////////////////////////////////
                  // RENDER 3D
                  if (renderType == RenderType.RENDER_3D) {
                    if (pm.isCanceled() == true) {
                      releaseMemory();
                      return;
                    }
                    pm.setNote("generate Perspectogram...");
                    pm.setProgress(50);
                    Surface perspectogram = new Surface(sono, true);
                    setVisible(false);
                    spwv = null;
                  }
                  /////////////////////////////////
                  // RENDER IMAGE
                  if (renderType == RenderType.RENDER_IMAGE) {
                    pm.setNote("rendering the Image...");
                    // paint the image
                    image = new BufferedImage(length, resolution, BufferedImage.TYPE_INT_RGB);
                    Graphics2D big = image.createGraphics();
                    big.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    for (int i = 0; i < length; i++) {
                      pm.setProgress((int) ((double) i / (double) length * 49) + 50);
                      for (int j = 0; j < resolution; j++) {
                        int color = (int) ((spwv[i][resolution - j - 1]) * (double) 255);
                        big.setColor(colors[color]);
                        big.drawLine(i, j, i, j);
                      }
                    }
                    spwv = null;
                    // the color scale image
                    if (cb1.isSelected() == true) {
                      // paint the scale
                      int h = image.getHeight() - 80;
                      for (int y = 0; y < h; y++) {
                        int color = (int) ((double) y / (double) h * 255.0);
                        big.setColor(colors[255 - color]);
                        big.drawLine(0 + 20, y + 40, 30 + 20, y + 40);
                      }
                      big.setColor(new Color(0, 0, 0));
                      big.drawLine(0 + 20, 0 + 40, 29 + 20, 0 + 40);
                      big.drawLine(29 + 20, 0 + 40, 29 + 20, h - 1 + 40);
                      big.drawLine(0 + 20, 0 + 40, 0 + 20, h - 1 + 40);
                      big.drawLine(0 + 20, h - 1 + 40, 29 + 20, h - 1 + 40);
                      big.setFont(new Font("Sans-Serif", Font.PLAIN, 9));
                      double percent = 9.0;
                      for (double y = (double) h / 10.0;
                          y <= (double) h * 0.91;
                          y += (double) h / 10) {
                        big.drawLine(1 + 20, (int) y + 40, 29 + 20, (int) y + 40);
                        if (h / 10 - 0 > 20)
                          big.drawString(Double.toString(percent / 10), 8 + 20, (int) y - 2 + 40);
                        percent -= 1.0;
                      }
                    }
                    jScrollPane = new JScrollPane(ip);
                    // save the image
                    pm.setNote("saving the Image...");
                    pm.setProgress(99);

                    String filename = "SPWV-Perspectogram.png";
                    try {
                      class MyFilter extends javax.swing.filechooser.FileFilter {
                        public boolean accept(File file) {
                          String filename = file.getName();
                          return filename.endsWith(".png");
                        }

                        public String getDescription() {
                          return "Portable Network Graphics (PNG)";
                        }
                      }
                      // the file chooser
                      JFileChooser chooser = new JFileChooser();
                      chooser.addChoosableFileFilter(new MyFilter());
                      chooser.setSelectedFile(new File("SPWV-Spectrum.png"));
                      int returnVal = chooser.showSaveDialog(sono);
                      // write the image to file
                      if (returnVal == JFileChooser.APPROVE_OPTION)
                        ImageIO.write(image, "png", chooser.getSelectedFile());
                      setVisible(false);
                    } catch (IOException e) {
                      JOptionPane.showMessageDialog(
                          sono,
                          "<html><i>An eror occoured while save the image.<br>"
                              + "Did you have write permission to the selected file ?</i><br><br>"
                              + "File: "
                              + filename,
                          "Cannot save the Image",
                          JOptionPane.ERROR_MESSAGE);
                    }
                  }
                }
                /////////////////////////////
                // CATCH EXCEPTION BLOCK
                catch (final java.lang.OutOfMemoryError exception) {
                  int memMb = (int) Math.round(Runtime.getRuntime().totalMemory() / 1024 / 1024);
                  String memMessage = "";
                  if (memMb < 1000)
                    memMessage +=
                        "<br><table border=0><tr><td><font color=#440000 size =4>Allocated Memory: "
                            + "</td><td><font color=#440000 size =4>"
                            + memMb
                            + " MB</td></tr>";
                  else
                    memMessage +=
                        "<br><table border=0><tr><td><font color=#440000 size =4>Allocated Memory: "
                            + "</td><td><font color=#440000 size =4>"
                            + Math.round((double) memMb) / 1000.0
                            + " GB</td></tr>";

                  memMb = (int) Math.round(Runtime.getRuntime().freeMemory() / 1024 / 1024);
                  if (memMb < 1000)
                    memMessage +=
                        "<tr><td><font color=#440000 size =4>Available Free Memory: </td><td><font"
                            + " color=#440000 size =4>"
                            + memMb
                            + " MB</td></tr></table>";
                  else
                    memMessage +=
                        "<tr><td><font color=#440000 size =4>Available Free Memory: </td><td><font"
                            + " color=#440000 size =4>"
                            + Math.ceil((double) memMb) / 1000.0
                            + " GB</td></tr></table>";
                  JOptionPane.showMessageDialog(
                      sono,
                      "<html><i>We running <u>into an out of memory Error.</i></u><br>"
                          + "Please reduce the Windowlength or the Resolution of the SPWVD.<br>"
                          + memMessage,
                      "Out of Memory",
                      JOptionPane.ERROR_MESSAGE);
                  releaseMemory();
                } finally {
                  pm.setProgress(100);
                }
              } // end of the run() function
            }); // end of the Thread

    thread.start();
  }

  // ----------------------------------------------------------------------------------------------------------------

  private void releaseMemory() {
    if (frame != null) frame.dispose();
    if (spwv != null) spwv = null;
    if (image != null) image = null;
    if (scaledImage != null) scaledImage = null;
    System.gc();
  }

  // ----------------------------------------------------------------------------------------------------------------

  private void scaleImage(boolean highQuality) {
    if (zoomx != null) scaleX = (double) zoomx.getValue() / 10000.0;
    if (zoomy != null) scaleY = (double) zoomy.getValue() / 10000.0;
    // create scaled BufferedImage:
    int w = (int) (length * scaleX);
    int h = (int) (resolution * scaleY);
    if (w <= 0) w = 1;
    if (h <= 0) h = 1;
    if (w >= 32767) w = 32767;
    if (h >= 32767) h = 32767;
    if (highQuality == false) scaledImage = image.getScaledInstance(w, h, Image.SCALE_FAST);
    if (highQuality == true) scaledImage = image.getScaledInstance(w, h, Image.SCALE_SMOOTH);
    if (ip != null) {
      ip.setPreferredSize(new Dimension(w, h));
      ip.repaint();
    }
    if (frame != null) {
      frame.repaint();
    }
    if (jScrollPane != null) {
      jScrollPane.repaint();
      jScrollPane.getViewport().setView(ip);
    }
  }

  // ----------------------------------------------------------------------------------------------------------------

  public void shakeButton() {
    final Point point = okButton.getLocation();
    final int delay = 75;
    Runnable r =
        new Runnable() {
          @Override
          public void run() {
            for (int i = 0; i < 10; i++) {
              try {
                moveButton(new Point(point.x + 5, point.y));
                Thread.sleep(delay);
                moveButton(point);
                Thread.sleep(delay);
                moveButton(new Point(point.x - 5, point.y));
                Thread.sleep(delay);
                moveButton(point);
                Thread.sleep(delay);
              } catch (InterruptedException ex) {
                ex.printStackTrace();
              }
            }
          }
        };
    Thread t = new Thread(r);
    t.start();
  }

  // ----------------------------------------------------------------------------------------------------------------

  private void moveButton(final Point p) {
    SwingUtilities.invokeLater(
        new Runnable() {
          @Override
          public void run() {
            okButton.setLocation(p);
          }
        });
  }

  // ----------------------------------------------------------------------------------------------------------------

  private class ImagePanel extends JPanel implements MouseMotionListener {
    BufferedImage colorScaleImage;

    // -----------------------------------------------------------------------------

    public void updateColorScaleImage() {
      if (cb1.isSelected() == false) return;
      // allocate the image
      colorScaleImage =
          new BufferedImage(30, scaledImage.getHeight(this) - 80, BufferedImage.TYPE_INT_RGB);
      Graphics2D big = colorScaleImage.createGraphics();
      big.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      // select the colors
      Color[] colors = new Color[256];
      switch (colCombo.getSelectedIndex()) {
        case 0:
          colors = sono.pp.coFI;
          break;
        case 1:
          colors = sono.pp.coCO;
          break;
        case 2:
          colors = sono.pp.coRA;
          break;
        case 3:
          colors = sono.pp.coCG;
          break;
        case 4:
          colors = sono.pp.coSW;
          break;
      }
      // paint the scale
      int h = scaledImage.getHeight(this) - 80;
      for (int y = 0; y < h; y++) {
        int color = (int) ((double) y / (double) h * 255.0);
        big.setColor(colors[255 - color]);
        big.drawLine(0, y, 30, y);
      }
      big.setColor(new Color(0, 0, 0));
      big.drawLine(0, 0, 29, 0);
      big.drawLine(29, 0, 29, h - 1);
      big.drawLine(0, 0, 0, h - 1);
      big.drawLine(0, h - 1, 29, h - 1);
      big.setFont(new Font("Sans-Serif", Font.PLAIN, 9));
      double percent = 9.0;
      for (double y = (double) h / 10.0; y <= (double) h * 0.91; y += (double) h / 10) {
        big.drawLine(1, (int) y, 29, (int) y);
        if (h / 10 - 0 > 20) big.drawString(Double.toString(percent / 10), 8, (int) y - 2);
        percent -= 1.0;
      }
    }

    // -----------------------------------------------------------------------------

    ImagePanel() {
      addMouseMotionListener(this);
      addMouseListener(
          new MouseListener() {
            public void mouseReleased(MouseEvent e) {}

            public void mousePressed(MouseEvent e) {}

            public void mouseEntered(MouseEvent e) {
              ToolTipManager.sharedInstance().setInitialDelay(0);
            }

            public void mouseExited(MouseEvent e) {
              ToolTipManager.sharedInstance().setInitialDelay(defaultTimeout);
            }

            public void mouseClicked(MouseEvent e) {
              if (e.getClickCount() == 2) {
                if (fullscreen == false) {
                  frame.setVisible(false);
                  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                  int width = screenSize.width;
                  int height = screenSize.height;
                  frame.setSize(width, height);
                  GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
                  frame.setMaximizedBounds(env.getMaximumWindowBounds());
                  frame.setExtendedState(frame.getExtendedState() | frame.MAXIMIZED_BOTH);
                  fullscreen = true;
                  frame.setVisible(true);
                } else {
                  frame.setVisible(false);
                  frame.setSize(
                      (int) Math.min(1024 + widthOffset, (double) length) + widthOffset,
                      512 + heightOffset);
                  System.out.println("--> SPWV WINDOWED");
                  frame.setLocationRelativeTo(null);
                  fullscreen = false;
                  frame.setVisible(true);
                }
                zoomone.doClick();
              }
            }
          });
    }

    // -----------------------------------------------------------------------------

    public void paint(Graphics g) {
      Graphics2D g2 = (Graphics2D) g;
      g2.drawImage(scaledImage, 0, 0, this);
      if (cb1.isSelected() == true) {
        g2.drawImage(colorScaleImage, 20, 40, this);
      }
    }

    // -----------------------------------------------------------------------------

    public void mouseMoved(MouseEvent e) {
      int x = e.getX();
      if (x > scaledImage.getWidth(this)) {
        return;
      }

      int y = e.getY();
      double zoomX = (double) zoomx.getValue() / 10000.0;
      double zoomY = (double) zoomy.getValue() / 10000.0;
      double f =
          Math.round(
              (double) (scaledImage.getHeight(this) - y)
                  / (double) scaledImage.getHeight(this)
                  * (double) sono.samplerate
                  / 2.0);
      double t =
          Math.round(((double) (startp) + (double) x / zoomX) / (double) sono.samplerate * 1000.0)
              / 1000.0;
      int indexX = (int) Math.min(length - 1, Math.max(0.0, ((double) x / zoomX)));
      int indexY = (int) Math.min(resolution - 1, Math.max(0.0, (resolution - (double) y / zoomY)));
      double a = Math.round(spwv[indexX][indexY] * 10000.0) / 100.0;
      double l = Math.round(Math.log(a / 100.0) * 100.0) / 10.0;

      String musicalNote = FrequencyToNoteConverter.query(f);
      String ttt =
          "<html><pre>"
              + "<b>Frequency: </b><tt><font color=black >"
              + f / 1000.0
              + "kHz</font></tt><br>"
              + "<b>Time:      </b><tt><font color=black >"
              + t
              + "s</font></tt><br>"
              + "<b>Amplitude: </b><tt><font color=black >"
              + a
              + "%</font></tt><br>"
              + "<b>Note:      </b><tt><font color=black >"
              + musicalNote
              + "</font></tt><br>"
              + "<b><font size=1>Level:  </font></b><tt><font color=black size=1>"
              + l
              + "dB</font></tt>";

      setToolTipText(ttt);
    }

    // -----------------------------------------------------------------------------

    public void mouseDragged(MouseEvent e) {}
  }

  // ----------------------------------------------------------------------------------------------------------------

  static class PixelRule extends JComponent {
    private int orientation;
    private static int MAX_SIZE = 32768;
    private static Font FONT = new Font("Sans-Serif", Font.PLAIN, 9);
    private static Font FONT1 = new Font("Sans-Serif", Font.BOLD, 9);
    private static Font FON2 = new Font("Sans-Serif", Font.PLAIN, 8);
    public static int HORIZONTAL = 0;
    public static int VERTICAL = 1;

    // ------------------------------------------------------------------------------------------------------------

    PixelRule(int orientation) {
      this.orientation = orientation;
      setPreferredSize(
          orientation == PixelRule.HORIZONTAL
              ? new Dimension(MAX_SIZE, 30)
              : new Dimension(30, MAX_SIZE));
    }

    // ------------------------------------------------------------------------------------------------------------

    public void paint(Graphics g1) {
      Graphics2D g = (Graphics2D) g1;
      g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      double smallTicks = 100;
      double bigTicks = 10;
      int sampleStart = startp;
      int sampleEnd = startp + length;
      int sampleRate = sono.samplerate;
      int size;

      // init the values
      if (orientation == PixelRule.HORIZONTAL) {
        size = Math.max(scaledImage.getWidth(this), 1);
      } else {
        size = Math.max(scaledImage.getHeight(this), 1);
        if (sono.samplerate <= 48000) {
          bigTicks = sono.samplerate / 4000.0;
          smallTicks = sono.samplerate / 400.0;
        }
        if (sono.samplerate <= 32000) {
          bigTicks = sono.samplerate / 2000.0;
          smallTicks = sono.samplerate / 200.0;
        }
        if (sono.samplerate <= 16000) {
          bigTicks = sono.samplerate / 1000.0;
          smallTicks = sono.samplerate / 100.0;
        }
        if (sono.samplerate <= 8000) {
          bigTicks = sono.samplerate / 500.0;
          smallTicks = sono.samplerate / 50.0;
        }
        if (sono.samplerate <= 4000) {
          bigTicks = sono.samplerate / 250.0;
          smallTicks = sono.samplerate / 25.0;
        }
        // reduce the point density for small sizes
        if (size < 300) smallTicks /= 2.0;
        if (size < 200) {
          smallTicks /= 2.0;
          bigTicks /= 2.0;
        }
        if (size < 100) {
          smallTicks /= 2.0;
          bigTicks /= 2.0;
        }
        if (size < 50) {
          smallTicks /= 2.0;
          bigTicks /= 2.0;
        }
        if (bigTicks < 1.0) bigTicks = 1.0;
        if (smallTicks < 1.0) smallTicks = 1.0;
      }

      // fill the background
      g.setColor(linealBgColor);
      g.fillRect(0, 0, getSize().width, getSize().height);

      // the horizontal frequency lineal
      g.setColor(linealFgColor);
      g.setFont(FONT1);
      if (orientation == PixelRule.HORIZONTAL) {
        double zoom = (double) zoomx.getValue() / 10000.0 - 0.0000001;
        double startSec = Math.round((double) sampleStart / (double) sampleRate * 1000.0) / 1000.0;
        g.drawLine(0, 20, 0, 28);
        String startString = "Start: " + Double.toString(startSec) + " Seconds";
        int startStringLength = g.getFontMetrics().stringWidth(startString);
        g.drawString(startString, 12, 29);
        g.drawLine(0, 0, 0, 30);
        g.drawLine(1, 24, 10, 24);
        g.drawLine(1, 24, 5, 27);
        g.drawLine(1, 24, 5, 21);
        if (size > startStringLength + 10) {
          startSec = Math.round((double) sampleEnd / (double) sampleRate * 1000.0) / 1000.0;
          g.drawString("End: " + Double.toString(startSec) + " Seconds", size + 12, 29);
          g.drawLine(size, 0, size, 30);
          g.drawLine(size + 1, 24, size + 10, 24);
          g.drawLine(size + 1, 24, size + 5, 27);
          g.drawLine(size + 1, 24, size + 5, 21);
        }

        int sec = 0;
        // SECOND TICKS + NUMBERS
        for (double i = 0; i < sampleEnd; i += (double) sampleRate) {
          if (i > sampleStart && i < sampleEnd) {
            int x = (int) ((double) (i - sampleStart) * zoom);
            g.drawLine(x, 0, x, 30);
            if (x > startStringLength + 10) {
              String secS = " Seconds";
              if (sec == 1) secS = " Second";
              g.drawString(Integer.toString(sec) + secS, x + 3, 29);
            }
          }
          sec++;
        }
        sec = (int) Math.floor((double) sampleStart / (double) sampleRate);
        // 100ms TICKS + NUMBERS
        g.setFont(FONT);
        double density = (double) (sampleEnd - sampleStart) / (double) sampleRate / (double) size;
        if (density < 0.025)
          for (double i = Math.max(0, sec - 2) * sampleRate;
              i < sampleEnd;
              i += (double) sampleRate / 10.0) {
            if (i < sampleStart && i > sampleEnd) continue;
            int x = (int) ((double) (i - sampleStart) * zoom);
            if (x < 0) continue;
            g.drawLine(x, 0, x, 22);
            double secs = Math.round((double) i / (double) sampleRate * 10.0) / 10.0;
            if (Math.round(secs) - secs == 0.0) continue;
            if (x > startStringLength + 17) g.drawString(Double.toString(secs), x - 7, 29);
          }
        // 10ms TICKS + NUMBERS
        g.setFont(FON2);
        if (density < 0.0025)
          for (double i = Math.max(0, sec - 2) * sampleRate;
              i < sampleEnd;
              i += (double) sampleRate / 100.0) {
            if (i < sampleStart && i > sampleEnd) continue;
            int x = (int) ((double) (i - sampleStart) * zoom);
            if (x < 0) continue;
            g.drawLine(x, 0, x, 15);
            if (density < 0.00035) {
              double secs = Math.round((double) i / (double) sampleRate * 100.0) / 100.0;
              if (Math.round(secs * 10) - secs * 10 == 0.0) continue;
              if (Math.round(secs) - secs == 0.0) continue;
              g.drawString(Double.toString(secs), x - 7, 21);
            }
          }
        // 1ms TICKS
        if (density < 0.00025)
          for (double i = Math.max(0, sec - 2) * sampleRate;
              i < sampleEnd;
              i += (double) sampleRate / 1000.0) {
            if (i < sampleStart && i > sampleEnd) continue;
            int x = (int) ((double) (i - sampleStart) * zoom);
            if (x < 0) continue;
            g.drawLine(x, 0, x, 10);
          }
        // 100us TICKS
        if (density < 0.00004)
          for (double i = Math.max(0, sec - 2) * sampleRate;
              i < sampleEnd;
              i += (double) sampleRate / 1000.0) {
            if (i < sampleStart && i > sampleEnd) continue;
            for (double j = 1; j < 10; j++) {
              int x = (int) ((double) ((i - sampleStart) + (j * sampleRate / 10000.0)) * zoom);
              if (x < 0) continue;
              g.drawLine(x, 0, x, 5);
            }
          }
      }

      // the vertical frequency lineal
      g.setFont(FONT);
      if (orientation == PixelRule.VERTICAL) {
        for (double i = 0; i <= smallTicks; i++) {
          int y = size - (int) ((double) size * i / smallTicks);
          g.drawLine(0, y, 7, y);
        }
        for (double i = 0; i <= bigTicks; i++) {
          int y = size - (int) ((double) size * i / bigTicks);
          if (i == 0 && zoomy.getValue() == 10000) y--;
          g.drawLine(0, y, 22, y);
          // the text
          double frequency =
              Math.round((double) sono.samplerate * (double) i / (double) bigTicks / 20.0) / 100.0;
          String s = Double.toString(frequency) + "k";
          if (frequency == 0.0 && (resolution - size) < 10) y -= 12;
          g.drawString(s, 30 - g.getFontMetrics().stringWidth(s), y + 10);
        }
        g.drawLine(0, 0, size, 0);
      }
    }
    // ------------------------------------------------------------------------------------------------------------

  } // end class PixelRule

  // ----------------------------------------------------------------------------------------------------------------

} // end class WxSettingsDialog
