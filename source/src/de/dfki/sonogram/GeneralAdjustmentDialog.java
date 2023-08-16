package de.dfki.sonogram;

import de.dfki.maths.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>The Oprions Dialog for Sonogram
 *
 * @author Christoph Lauer
 * @version 1.0, Current 26/09/2002
 */
class GeneralAdjustmentDialog extends JFrame {
  Sonogram sono;
  GeneralAdjustmentDialog gethis = this;
  JButton btcl, btap, btrd, btro;
  JSlider sliderwinsize;
  JSlider sliderwinfunktion;
  JSlider sliderwinspeed;
  JSlider slidersurface;
  JSlider sliderformant;
  JSlider sliderformantlen;
  JSlider sliderlpccoef;
  JSlider sliderlpcfftnum;
  JSlider sliderlpcsamfutur;
  JSlider sliderlpcfre;
  JSlider slidersurfacey;
  JSlider slidersurfacex;
  JSlider sliderpitch;
  JSlider sliderlog;
  JSlider slidercep;
  JSlider sliderwave;
  JSlider sliderdb;
  JSlider sliderwaltl;
  JSlider slideracwinlength;
  JSlider slideracwinshift;
  JSlider sliderpwinlength;
  JSlider sliderpwinshift;
  JSlider sliderpitchmax;
  JSlider slidersmoothfft;
  JTextField tf1, tf2, tf3;
  JRadioButton r0, r1, r2, r3, r4, r5, rfft, rlpc;
  JRadioButton s1, s2, s3;
  JCheckBox cinv,
      clog,
      cgrid,
      cauto, // the checkbox for the automatic determination of the fft window length
      cback,
      csmooth,
      cacpoints,
      csmoothsi,
      cenergy,
      cllog,
      clfor,
      csampl,
      csmoothx,
      cspitch,
      cspitchlimitation,
      coverlapping,
      csavehist,
      cspitchonely,
      cspitchblack,
      cspitchsmooth,
      csspecwhileplaying,
      csloop,
      cscolsi,
      cslogfr,
      cgrid2,
      csavescreepos,
      cssaveconf,
      csopenlast,
      copenlastwithzoom,
      ccep,
      cceplog,
      cwavelines,
      cmute,
      clocalpeak,
      cuniverse,
      clogwal,
      csarrange,
      cacsmooth,
      cacpitch,
      cantialise,
      cwfnorm,
      ccepsmooth,
      cppoints,
      cplimitfr,
      cpsmooth,
      cscrennsaver,
      cperantialias,
      cpercoord,
      cpraway,
      cptrack,
      cpfog,
      ctooltip;
  JTabbedPane p1;
  JButton cb;
  JComboBox wcb;
  JLabel imgLin, imgLog;
  Color bgcol;
  int highlightedbutton = 0;
  int walwindowlength = 0;
  byte color;
  byte markcolor;
  byte winfunktion;
  boolean inv;
  boolean grid;
  boolean log;
  boolean markcsamp;
  boolean markste;
  boolean marksmothy;
  boolean marksmothx;
  boolean marklog;
  boolean markgrid;
  boolean marktrans; // true=fft , false=lpc
  boolean markoverl;
  boolean markpitch;
  boolean markpitchlim;
  boolean markpitchone;
  boolean markpitchbla;
  boolean markpitchsmo;
  boolean marklogfr;
  boolean marklocalpeak;
  boolean cperantialiaslast;
  boolean cperantialiasconfirm;

  static WaveletBasisSets wavelets = new WaveletBasisSets();
  // -------------------------------------------------------------------------------------------------------------------------
  /** Construktor for Dialogfield Defines Buttonfunktions and set Outfit. */
  public GeneralAdjustmentDialog(Frame owner) {

    sono = (Sonogram) owner;
    // Initialize Status-change-flags
    color = (byte) sono.isc;
    markcolor = (byte) sono.isc;
    ;
    winfunktion = (byte) sono.islwf;
    inv = sono.iinv;
    grid = sono.igrid;
    log = sono.iloga;
    markcsamp = sono.iopen8;
    markste = sono.ienergy;
    marksmothy = sono.ismof;
    marksmothx = sono.ismot;
    marklog = sono.iloga;
    markgrid = sono.igrid;
    marktrans = sono.iffttrans; // true=fft , false=lpc
    markoverl = sono.ienov;
    markpitch = sono.ipide;
    markpitchlim = sono.ipifrlim;
    markpitchone = sono.ipitchfog;
    markpitchbla = sono.ipitchblack;
    markpitchsmo = sono.ipitchsm;
    marklogfr = sono.ilogf;
    marklocalpeak = sono.ilocalpeak;
    cperantialiasconfirm = sono.iantialisconfirmed;

    setTitle("Sonogram Settings");
    setLocation(sono.gadx, sono.gady);
    Toolkit tk = Toolkit.getDefaultToolkit();
    setIconImage(tk.getImage(Sonogram.class.getResource("Sonogram.gif")));
    setResizable(true);

    getOptionsFormMainWin();
    bgcol = sono.ibgcol;

    JPanel p2 = new JPanel();
    p2.setLayout(new BorderLayout());

    p1 = new JTabbedPane();

    p2.add("Center", p1);
    JPanel p3 = new JPanel();

    p3.setLayout(new GridLayout(1, 5));
    p2.add("South", p3);

    btro = new JButton("ReOpen");
    ActionListener rlst =
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            applyChanges();
            if (sono.spektrumExist == true) sono.openFile(sono.filepath);
          }
        };
    btro.addActionListener(rlst);
    btro.setToolTipText("<html><b>Reopen</b> this File");
    p3.add(btro);

    btap = new JButton("ReCalc");
    ActionListener alst =
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            applyChanges();
            if (sono.spektrumExist == true) sono.readerIsBack();
          }
        };
    btap.addActionListener(alst);
    btap.setToolTipText("<html><b>Apply</b> all Changes");
    p3.add(btap);

    btrd = new JButton("ReDraw");
    ActionListener dlst =
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (sono.spektrumExist == true) {
              sono.updateimageflag = true;
              sono.repaint();
            }
          }
        };
    btrd.addActionListener(dlst);
    btrd.setToolTipText("<html><b>Redraw</b> the Sonogram Area");
    p3.add(btrd);

    btcl = new JButton("Close");
    ActionListener clst =
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            setVisible(false);
          }
        };
    btcl.addActionListener(clst);
    btcl.setToolTipText("<html>Close this Window<br>Not all Changes will be saved");
    p3.add(btcl);

    // WINDOWLENGTH
    JPanel p = new JPanel();
    p.setLayout(new GridLayout(2, 1));
    p.setBorder(new TitledBorder(new EtchedBorder(), "Time Window Length"));
    sliderwinsize = new JSlider(JSlider.HORIZONTAL, 6, 13, sono.islws);
    sliderwinsize.setToolTipText(
        "<html>Window Length for one Step<br>Larger Windows have worse<br>Frequency Resolution,"
            + " vice versa");
    Hashtable h1 = new Hashtable();
    h1.put(Integer.valueOf(6), new JLabel("64"));
    h1.put(Integer.valueOf(7), new JLabel("128"));
    h1.put(Integer.valueOf(8), new JLabel("256"));
    h1.put(Integer.valueOf(9), new JLabel("512"));
    h1.put(Integer.valueOf(10), new JLabel("1024"));
    h1.put(Integer.valueOf(11), new JLabel("2048"));
    h1.put(Integer.valueOf(12), new JLabel("4096"));
    h1.put(Integer.valueOf(13), new JLabel("8192"));
    sliderwinsize.setLabelTable(h1);
    sliderwinsize.setPaintLabels(true);
    sliderwinsize.setSnapToTicks(true);
    sliderwinsize.setMinorTickSpacing(1);
    sliderwinsize.setPaintTicks(true);
    sliderwinsize.setBorder(new TitledBorder(new EtchedBorder(), "Time Window Length in Samples"));
    p.add(sliderwinsize);
    JPanel pq = new JPanel();
    cauto = new JCheckBox("Determine the optimal Window Length automatically", sono.iautowinl);
    cauto.setToolTipText("<html>Sonogram calculates the best<br>window length automaicaly");
    pq.setBorder(new TitledBorder(new EtchedBorder(), "Options for Window Length"));
    pq.add(cauto);
    p.add(pq);
    p1.addTab("Window Length", new ImageIcon(Sonogram.class.getResource("wil.gif")), p);

    // WINDOWFUNKTIONS
    p = new JPanel();
    p.setLayout(new GridBagLayout());
    sliderwinfunktion = new JSlider(JSlider.VERTICAL, 1, 11, sono.islwf);
    sliderwinfunktion.setBorder(new TitledBorder(new EtchedBorder(), "Window Function"));
    sliderwinfunktion.setToolTipText(
        "<html>Select the Window Function multiplied<br>with the sample window");
    Hashtable h2 = new Hashtable();
    h2.put(Integer.valueOf(1), new JLabel("Rectangle"));
    h2.put(Integer.valueOf(2), new JLabel("Blackman"));
    h2.put(Integer.valueOf(3), new JLabel("Triangle"));
    h2.put(Integer.valueOf(4), new JLabel("Hamming"));
    h2.put(Integer.valueOf(5), new JLabel("Welch"));
    h2.put(Integer.valueOf(6), new JLabel("Gaussian"));
    h2.put(Integer.valueOf(7), new JLabel("Hanning"));
    h2.put(Integer.valueOf(8), new JLabel("Flat-Top"));
    h2.put(Integer.valueOf(9), new JLabel("Harris"));
    h2.put(Integer.valueOf(10), new JLabel("Cosine"));
    h2.put(Integer.valueOf(11), new JLabel("Asymetric"));
    sliderwinfunktion.setLabelTable(h2);
    sliderwinfunktion.setPaintLabels(true);
    sliderwinfunktion.setInverted(true);
    sliderwinfunktion.setSnapToTicks(true);
    sliderwinfunktion.setPaintTicks(true);
    sliderwinfunktion.setPaintTrack(true);
    sliderwinfunktion.setMinimumSize(new Dimension(120, 260));
    sliderwinfunktion.setPreferredSize(new Dimension(120, 260));
    GridBagConstraints gc = new GridBagConstraints();
    gc.gridx = 1;
    gc.gridy = 1;
    gc.gridwidth = 1;
    p.add(sliderwinfunktion, gc);
    wfPanel wf = new wfPanel();
    wf.setBorder(new TitledBorder(new EtchedBorder(), "Preview"));
    wf.setMinimumSize(new Dimension(295, 260));
    wf.setPreferredSize(new Dimension(295, 260));
    wf.setToolTipText("Window Function Preview");
    gc.gridx = 2;
    gc.gridwidth = 2;
    p.add(wf, gc);
    p1.addTab("Window Function", new ImageIcon(Sonogram.class.getResource("wif.gif")), p);
    winfunktion = (byte) sliderwinfunktion.getValue();

    // COLORS
    p = new JPanel();
    JPanel pu = new JPanel();
    pu.setLayout(new GridLayout(1, 2));
    pu.setBorder(new TitledBorder(new EtchedBorder(), "Color Array for the main Display"));
    p.setLayout(new GridLayout(6, 1));
    p.setBorder(new TitledBorder(new EtchedBorder(), "Color Selection"));
    ButtonGroup group = new ButtonGroup();
    r0 = new JRadioButton("Classical Fire Colors");
    group.add(r0);
    p.add(r0);
    r1 = new JRadioButton("Modern Fire Colors");
    group.add(r1);
    p.add(r1);
    r2 = new JRadioButton("Extended Fire Colors");
    group.add(r2);
    p.add(r2);
    r3 = new JRadioButton("Rainbow Spectrum Colors");
    group.add(r3);
    p.add(r3);
    r4 = new JRadioButton("Purpur Purple Colors");
    group.add(r4);
    p.add(r4);
    r5 = new JRadioButton("Black/White Gradient Colors");
    group.add(r5);
    p.add(r5);
    pu.add(p);
    JPanel po = new JPanel();
    po.setLayout(new GridLayout(3, 1));
    cinv = new JCheckBox("Inverse Colors", sono.iinv);
    cinv.setToolTipText("Inverse RGB Colors");
    po.setBorder(new TitledBorder(new EtchedBorder(), "Color Array Options"));
    po.add(cinv);
    crPanel cp = new crPanel(sono);
    cp.setToolTipText("Amplitude Color Preview");
    po.add(cp);
    pu.add(po);
    p1.addTab("Colors", new ImageIcon(Sonogram.class.getResource("csp.gif")), pu);
    if (sono.isc == 0) r0.setSelected(true);
    if (sono.isc == 1) r1.setSelected(true);
    if (sono.isc == 2) r2.setSelected(true);
    if (sono.isc == 3) r3.setSelected(true);
    if (sono.isc == 4) r4.setSelected(true);
    if (sono.isc == 5) r5.setSelected(true);
    color = (byte) sono.isc;

    // GENERAL OPTION
    p = new JPanel();
    p.setBorder(new TitledBorder(new EtchedBorder(), "General Options"));
    p.setLayout(new GridLayout(9, 2));
    csspecwhileplaying = new JCheckBox("Spectrum while playing", sono.ispecpl);
    csspecwhileplaying.setForeground(new Color(0, 51, 180));
    csspecwhileplaying.setToolTipText(
        "<html>Show the Spectrum in the single<br>Frequency Curve while playing<br>(Shown in the"
            + " right Area)");
    p.add(csspecwhileplaying);
    cscolsi = new JCheckBox("Momochrom Single Curve", sono.imonoso);
    cscolsi.setForeground(new Color(0, 51, 180));
    cscolsi.setToolTipText(
        "<html>Do not show amplitude<br>based colors in single Frequency view<br>(Shown in the"
            + " right Area)");
    p.add(cscolsi);
    cback = new JCheckBox("Normalize Single Curve", sono.inorsi);
    cback.setForeground(new Color(0, 51, 180));
    cback.setToolTipText(
        "<html>Normalize single frequency view to<br>max amplitude (Shown in the right Area)");
    p.add(cback);
    csmoothsi = new JCheckBox("Smooth out Single Curve", sono.ismosi);
    csmoothsi.setForeground(new Color(0, 51, 180));
    csmoothsi.setToolTipText("Smooth out single frequency view (Shown in the rigth Area)");
    p.add(csmoothsi);
    csmooth = new JCheckBox("Smooth out over Frequency", sono.ismof);
    csmooth.setForeground(new Color(180, 100, 0));
    csmooth.setToolTipText("Smooth out the Sonogram over the Frequency");
    p.add(csmooth);
    csmoothx = new JCheckBox("Smooth out over Time", sono.ismot);
    csmoothx.setForeground(new Color(180, 100, 0));
    csmoothx.setToolTipText("Smooth out the Sonogram over the Time");
    p.add(csmoothx);
    csavehist = new JCheckBox("Remember the File Hotlist", sono.isavehi);
    csavehist.setForeground(new Color(108, 29, 108));
    csavehist.setToolTipText(
        "<html>Save the File Hotlist for the<br>last opened Filenames every<br>time Sonogram"
            + " terminated");
    p.add(csavehist);
    csavescreepos = new JCheckBox("Remember Screen Positions", sono.isascpo);
    csavescreepos.setForeground(new Color(10, 115, 10));
    csavescreepos.setForeground(new Color(108, 29, 108));
    csavescreepos.setToolTipText(
        "<html><b>Remember Screen Positions</b><br>Save the screen positions and dimension<br>for"
            + " all windows, every time Sonogram terminated.<br>Next time you start Sonogram it"
            + " will place<br>the windows at hte same locations.");
    p.add(csavescreepos);
    cssaveconf = new JCheckBox("Save Configuration", sono.isaco);
    cssaveconf.setForeground(new Color(10, 115, 10));
    cssaveconf.setForeground(new Color(108, 29, 108));
    cssaveconf.setToolTipText(
        "<html><b>Save Configuration</b><br>Save the Configuration to a XML file<br>each time"
            + " Sonogram terminates. Disable<br>this if you which that Sonogram should<br>open"
            + " always with the initial settings");
    p.add(cssaveconf);
    csloop = new JCheckBox("Loop Playing", sono.ilooppl);
    csloop.setForeground(new Color(108, 29, 108));
    csloop.setToolTipText("Loop Sound when playing");
    p.add(csloop);
    csopenlast = new JCheckBox("Auto Open last File", sono.iopenlast);
    csopenlast.setForeground(new Color(122, 0, 0));
    csopenlast.setToolTipText(
        "<html><b>Auto Open last File</b><br>Sonogram then opens automatically the last<br>open"
            + " File, next time it will starting up.");
    p.add(csopenlast);
    copenlastwithzoom = new JCheckBox("Open last File with Zoom", sono.ilastwithzoom);
    copenlastwithzoom.setForeground(new Color(122, 0, 0));
    copenlastwithzoom.setToolTipText(
        "<html><b>Open Last File with Zoom</b><br>If this flag is selected, the automatically"
            + " opened File<br>will be zoomed with the last zoom factor.");
    p.add(copenlastwithzoom);
    copenlastwithzoom.setEnabled(csopenlast.isSelected());
    csarrange = new JCheckBox("Arrange All Windows while startup", sono.isarr);
    csarrange.setForeground(new Color(122, 0, 0));
    csarrange.setToolTipText(
        "<html><b>All analyze windows</b> are opened and arranged<br>if the last opened file was"
            + " automatically opened.");
    csarrange.setEnabled(csopenlast.isSelected());
    p.add(csarrange);
    csampl = new JCheckBox("Open with 8Khz Samplingrate", sono.iopen8);
    csampl.setForeground(new Color(10, 115, 10));
    csampl.setToolTipText(
        "<html><b>Open with 8Khz Samplerate</b><br>The open File will be reopened with<br>a"
            + " downsampled sampling frequency of 8kHz");
    p.add(csampl);
    cgrid = new JCheckBox("Show the Grid in the Sonogram", sono.igrid);
    sono.gridItem.setState(sono.igrid);
    cgrid.setToolTipText(
        "<html>Enables/Disabled the <b>time and frequency grid</b><br>in the Sonogram main window");
    p.add(cgrid);
    cenergy = new JCheckBox("Show Energy Intensivity", sono.ienergy);
    cenergy.setToolTipText(
        "<html>Shows the Shorttime-Energy instead of the<br>Aplitude the the Timeline");
    p.add(cenergy);
    clocalpeak = new JCheckBox("Search local Peak Point", sono.ilocalpeak);
    clocalpeak.setToolTipText("<html>Search for the local peak in<br>current displayed Sonogram");
    p.add(clocalpeak);
    cantialise = new JCheckBox("Antialiased Rendering", sono.iantialise);
    cantialise.setToolTipText("Antiaiasing for all the Analyse Windows.");
    p.add(cantialise);
    p1.addTab("General", new ImageIcon(Sonogram.class.getResource("small_adj.gif")), p);

    // WINDOW OVERLAPPING
    p = new JPanel();
    p.setLayout(new GridLayout(2, 1));
    p.setBorder(new TitledBorder(new EtchedBorder(), "Window Overlapping"));
    sliderwinspeed = new JSlider(JSlider.HORIZONTAL, 0, 60, sono.islov);
    sliderwinspeed.setToolTipText(
        "<html>Influences how mutch transformations generated<br>per window in the Sonogram");
    sliderwinspeed.setMajorTickSpacing(5);
    sliderwinspeed.setMinorTickSpacing(1);
    sliderwinspeed.setPaintTicks(true);
    sliderwinspeed.setPaintLabels(true);
    sliderwinspeed.setSnapToTicks(true);
    sliderwinspeed.setBorder(new TitledBorder(new EtchedBorder(), "Overlapping (Steps per Frame)"));
    p.add(sliderwinspeed);
    JPanel pov = new JPanel();
    coverlapping = new JCheckBox("Enable the Overlapping", sono.ienov);
    coverlapping.setToolTipText("Turn Overlapping On/Off");
    pov.setBorder(new TitledBorder(new EtchedBorder(), "Overlapping Settings"));
    pov.add(coverlapping);
    p.add(pov);
    p1.addTab("Overlapping", new ImageIcon(Sonogram.class.getResource("ove.gif")), p);

    // SURFACE
    p = new JPanel();
    JPanel ps = new JPanel();
    p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
    ButtonGroup group1 = new ButtonGroup();
    ps.setLayout(new GridLayout(2, 4));
    ps.setBorder(new TitledBorder(new EtchedBorder(), "Rendering Style Settings"));
    s1 = new JRadioButton("Point Cloud");
    s1.setToolTipText("Generate Perspectogram as Point Cloud");
    group1.add(s1);
    ps.add(s1);
    s2 = new JRadioButton("Line Grid");
    s2.setToolTipText("Generate Perspectogram points as Line Grid");
    group1.add(s2);
    ps.add(s2);
    s3 = new JRadioButton("Solid Surface");
    s3.setToolTipText("Generate Perspectogram with Solid Surface");
    group1.add(s3);
    ps.add(s3);
    cuniverse = new JCheckBox("Universe", sono.iuniverse);
    cuniverse.setToolTipText(
        "<html>The Background Spehere of the Plot is textured<br>with an starfied picture.<br><i>A"
            + " kind of EASTER EGG</i>");
    ps.add(cuniverse);
    cscrennsaver = new JCheckBox("Rotation", sono.irotate);
    cscrennsaver.setToolTipText(
        "<html>Rotates the Perspectogram if<br>ten seconds nothing happens<b>");
    ps.add(cscrennsaver);
    cperantialias = new JCheckBox("Antialiasing", sono.iperantialias);
    cperantialias.setToolTipText(
        "<html>Antialiased Perspectogram Model<br><b>Decreases Plot Performance !!!<b>");
    ps.add(cperantialias);
    cperantialiaslast = sono.iperantialias;

    cpercoord = new JCheckBox("Coordinate System", sono.ipercoord);
    cpercoord.setToolTipText("<html>Enables the 3D Coordinate System.");
    ps.add(cpercoord);

    cb = new JButton("Background");
    cb.setToolTipText("The Background Color of the");
    cb.setBackground(bgcol);
    cb.setForeground(
        new Color(255 - bgcol.getRed(), 255 - bgcol.getGreen(), 255 - bgcol.getBlue()));
    ActionListener colst =
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            System.out.println("--> Colorchooser button prssed");
            bgcol = JColorChooser.showDialog(gethis, "Backgroundcolor", bgcol);
            if (bgcol != null) {
              cb.setBackground(bgcol);
              cb.setForeground(
                  new Color(255 - bgcol.getRed(), 255 - bgcol.getGreen(), 255 - bgcol.getBlue()));
            }
          }
        };
    cb.addActionListener(colst);
    cb.setEnabled(!cuniverse.isSelected());
    ps.add(cb);
    slidersurface = new JSlider(JSlider.HORIZONTAL, 0, 100, sono.islsdr);
    slidersurface.setToolTipText(
        "<html>Point Reduction for mass data in<br>the Perspectogram. Very usefully<br>for large"
            + " Point Clouds<br><b>ALPHA VERSION<b>");
    slidersurface.setMajorTickSpacing(5);
    slidersurface.setMinorTickSpacing(1);
    slidersurface.setPaintTicks(true);
    slidersurface.setPaintLabels(true);
    slidersurface.setSnapToTicks(true);
    slidersurface.setBorder(new TitledBorder(new EtchedBorder(), "Point Density Reduction"));

    JPanel pl = new JPanel();
    pl.setLayout(new GridLayout(1, 2));

    Hashtable h7 = new Hashtable();
    h7.put(Integer.valueOf(1), new JLabel("Small"));
    h7.put(Integer.valueOf(10), new JLabel("Normal"));
    h7.put(Integer.valueOf(30), new JLabel("Large"));

    slidersurfacex = new JSlider(JSlider.HORIZONTAL, 0, 30, sono.islsx);
    slidersurfacex.setBorder(new TitledBorder(new EtchedBorder(), "Frequency Stretching"));
    slidersurfacex.setPaintTicks(true);
    slidersurfacex.setMajorTickSpacing(5);
    slidersurfacex.setMinorTickSpacing(1);
    slidersurfacex.setSnapToTicks(true);
    slidersurfacex.setLabelTable(h7);
    slidersurfacex.setPaintLabels(true);
    slidersurfacex.setToolTipText("<html>Stretches the Y-Axis for the given Factor)");

    slidersurfacey = new JSlider(JSlider.HORIZONTAL, 0, 30, sono.islsy);
    slidersurfacey.setPaintTicks(true);
    slidersurfacey.setMajorTickSpacing(5);
    slidersurfacey.setMinorTickSpacing(1);
    slidersurfacey.setSnapToTicks(true);
    slidersurfacey.setBorder(new TitledBorder(new EtchedBorder(), "Time Stretching"));
    slidersurfacey.setLabelTable(h7);
    slidersurfacey.setPaintLabels(true);
    slidersurfacey.setToolTipText("<html>Stretches the X-Axis for the given Factor");

    pl.add(slidersurfacey);
    pl.add(slidersurfacex);
    p.add(ps);
    p.add(pl);
    p.add(slidersurface);
    p1.addTab("Perspectogram", new ImageIcon(Sonogram.class.getResource("small_3d.gif")), p);
    if (sono.isr == 1) s1.setSelected(true);
    if (sono.isr == 2) s2.setSelected(true);
    if (sono.isr == 3) s3.setSelected(true);

    // Fast Fourier Transformation
    p = new JPanel();
    p.setLayout(new GridLayout(3, 1));
    p.setBorder(new TitledBorder(new EtchedBorder(), "FFT Analyzer Settings"));
    sliderformant = new JSlider(JSlider.HORIZONTAL, 1, 4, sono.islff);
    sliderformant.setToolTipText(
        "<html>Frequency Division reduces the maximal<br>displayed frequency in the FFT analyzer"
            + " window");
    sliderformant.setMajorTickSpacing(1);
    sliderformant.setMinorTickSpacing(1);
    sliderformant.setPaintTicks(true);
    sliderformant.setPaintLabels(true);
    sliderformant.setSnapToTicks(true);
    Hashtable h3 = new Hashtable();
    h3.put(Integer.valueOf(1), new JLabel("1/8"));
    h3.put(Integer.valueOf(2), new JLabel("1/4"));
    h3.put(Integer.valueOf(3), new JLabel("1/2"));
    h3.put(Integer.valueOf(4), new JLabel("1/1"));
    sliderformant.setLabelTable(h3);
    sliderformant.setBorder(new TitledBorder(new EtchedBorder(), "Frequency Display Fraction"));
    p.add(sliderformant);
    sliderformantlen = new JSlider(JSlider.HORIZONTAL, 1, 5, sono.islfl);
    sliderformantlen.setToolTipText("<html>Sample points used for the FFT Transformation");
    sliderformantlen.setMajorTickSpacing(1);
    sliderformantlen.setMinorTickSpacing(1);
    sliderformantlen.setPaintTicks(true);
    sliderformantlen.setPaintLabels(true);
    sliderformantlen.setSnapToTicks(true);
    Hashtable h4 = new Hashtable();
    h4.put(Integer.valueOf(1), new JLabel("512"));
    h4.put(Integer.valueOf(2), new JLabel("1024"));
    h4.put(Integer.valueOf(3), new JLabel("2048"));
    h4.put(Integer.valueOf(4), new JLabel("4096"));
    h4.put(Integer.valueOf(5), new JLabel("8192"));
    sliderformantlen.setLabelTable(h4);
    sliderformantlen.setBorder(new TitledBorder(new EtchedBorder(), "FFT Buffer Length"));
    p.add(sliderformantlen);
    slidersmoothfft = new JSlider(JSlider.HORIZONTAL, 0, 50, sono.islsdr);
    slidersmoothfft.setToolTipText("<html>Smooths out the result spectrum curve.");
    slidersmoothfft.setMajorTickSpacing(5);
    slidersmoothfft.setMinorTickSpacing(1);
    slidersmoothfft.setPaintTicks(true);
    slidersmoothfft.setPaintLabels(true);
    slidersmoothfft.setSnapToTicks(true);
    slidersmoothfft.setBorder(new TitledBorder(new EtchedBorder(), "Spectrum Smoothing"));
    p.add(slidersmoothfft);
    p1.addTab("FFT", new ImageIcon(Sonogram.class.getResource("small_for.gif")), p);

    // LPC Settings
    p = new JPanel();
    p.setLayout(new GridLayout(3, 2));
    sliderlpcsamfutur = new JSlider(JSlider.HORIZONTAL, 0, 5000, sono.islls);
    sliderlpcsamfutur.setToolTipText("<html>Number of Samples used in the<br>Buffer for the LPC");
    sliderlpcsamfutur.setPaintLabels(true);
    sliderlpcsamfutur.setMajorTickSpacing(1000);
    sliderlpcsamfutur.setMinorTickSpacing(500);
    sliderlpcsamfutur.setPaintTicks(true);
    sliderlpcsamfutur.setBorder(new TitledBorder(new EtchedBorder(), "Previous Samples"));
    p.add(sliderlpcsamfutur);
    sliderlpccoef = new JSlider(JSlider.HORIZONTAL, 0, 60, sono.isllc);
    sliderlpccoef.setToolTipText("Number of LPC-coefficents used for prediction");
    sliderlpccoef.setPaintLabels(true);
    sliderlpccoef.setMajorTickSpacing(10);
    sliderlpccoef.setMinorTickSpacing(5);
    sliderlpccoef.setPaintTicks(true);
    sliderlpccoef.setBorder(new TitledBorder(new EtchedBorder(), "LPC Coefficents"));
    p.add(sliderlpccoef);
    sliderlpcfftnum = new JSlider(JSlider.HORIZONTAL, 7, 12, sono.islf);
    sliderlpcfftnum.setToolTipText(
        "<html>Number of Transformation Points used<br>for the FFT in the LPC");
    Hashtable h5 = new Hashtable();
    h5.put(Integer.valueOf(7), new JLabel("128"));
    h5.put(Integer.valueOf(8), new JLabel("256"));
    h5.put(Integer.valueOf(9), new JLabel("512"));
    h5.put(Integer.valueOf(10), new JLabel("1024"));
    h5.put(Integer.valueOf(11), new JLabel("2048"));
    h5.put(Integer.valueOf(12), new JLabel("4096"));
    sliderlpcfftnum.setLabelTable(h5);
    sliderlpcfftnum.setPaintLabels(true);
    sliderlpcfftnum.setSnapToTicks(true);
    sliderlpcfftnum.setMinorTickSpacing(1);
    sliderlpcfftnum.setPaintTicks(true);
    sliderlpcfftnum.setBorder(new TitledBorder(new EtchedBorder(), "FFT length for LPC"));
    p.add(sliderlpcfftnum);
    sliderlpcfre = new JSlider(JSlider.HORIZONTAL, 1, 4, sono.isllff);
    sliderlpcfre.setToolTipText("Reduces the Maximal Displayed Frequency");
    sliderlpcfre.setMajorTickSpacing(1);
    sliderlpcfre.setMinorTickSpacing(1);
    sliderlpcfre.setPaintTicks(true);
    sliderlpcfre.setPaintLabels(true);
    sliderlpcfre.setSnapToTicks(true);
    Hashtable h6 = new Hashtable();
    h6.put(Integer.valueOf(1), new JLabel("1/8"));
    h6.put(Integer.valueOf(2), new JLabel("1/4"));
    h6.put(Integer.valueOf(3), new JLabel("1/2"));
    h6.put(Integer.valueOf(4), new JLabel("1/1"));
    sliderlpcfre.setLabelTable(h6);
    sliderlpcfre.setBorder(new TitledBorder(new EtchedBorder(), "Frequency Display Fraction"));
    p.add(sliderlpcfre);
    JPanel pl1 = new JPanel();
    pl1.setLayout(new GridLayout(2, 1));
    pl1.setBorder(new TitledBorder(new EtchedBorder(), "General LPC Settings"));
    cllog = new JCheckBox("Logarithm Amplitude", sono.iloglpc);
    cllog.setToolTipText("Logarithm Amplitude for LPC Function Curve");
    pl1.add(cllog);
    clfor = new JCheckBox("Search for Formats", sono.ilogfour);
    clfor.setToolTipText(
        "Search for peak points int the LPC spectrum which<br>are called formants in the speech");
    pl1.add(clfor);
    JPanel pl2 = new JPanel();
    pl2.setLayout(new GridLayout(2, 2));
    pl2.setBorder(new TitledBorder(new EtchedBorder(), "Selected Values"));
    tf1 = new JTextField("LPC Coefficients: 30", 1);
    tf1.setToolTipText("Number of LPC Coefficients selected with slider.");
    tf2 = new JTextField("Previous Samples: 500", 1);
    tf2.setToolTipText("Number of previous samples used to generate prediction Ccoefficients.");
    pl2.add(tf2);
    pl2.add(tf1);
    p.add(pl1);
    p.add(pl2);
    p1.addTab("LPC", new ImageIcon(Sonogram.class.getResource("small_lpc.gif")), p);
    if (sono.iffttrans == false) {
      Border redlineborder = BorderFactory.createLineBorder(Color.red);
      sliderlpccoef.setBorder(BorderFactory.createTitledBorder(redlineborder, "LPC Coefficients"));
      sliderlpcsamfutur.setBorder(
          BorderFactory.createTitledBorder(redlineborder, "Prediction Buffer"));
    }

    // Cepstrum
    p = new JPanel();
    p.setLayout(new GridLayout(2, 1));
    p.setBorder(new TitledBorder(new EtchedBorder(), "Cepstrum Justification"));
    JPanel pce = new JPanel();
    pce.setLayout(new GridLayout(3, 2));
    pce.setBorder(new TitledBorder(new EtchedBorder(), "General Cepstrum settings"));
    ccep = new JCheckBox("Enable Logarithm Quefrenz", sono.iceplog);
    ccep.setToolTipText("<html>Display the Quefrenz Amplitude on the<br>logarithm Scale");
    pce.add(ccep);
    ccepsmooth = new JCheckBox("Smooth out Cepstrum Curve", sono.icepsmooth);
    ccepsmooth.setToolTipText("Smooth out the Cepstrum Function Curve");
    pce.add(ccepsmooth);
    pce.add(ccepsmooth);
    p.add(pce);
    slidercep = new JSlider(JSlider.HORIZONTAL, 8, 13, sono.islcep);
    slidercep.setToolTipText("Buffersize for Cepstralanalyse");
    slidercep.setMajorTickSpacing(1);
    slidercep.setMinorTickSpacing(1);
    slidercep.setPaintTicks(true);
    slidercep.setPaintLabels(true);
    slidercep.setSnapToTicks(true);
    Hashtable h9 = new Hashtable();
    h9.put(Integer.valueOf(8), new JLabel("256"));
    h9.put(Integer.valueOf(9), new JLabel("512"));
    h9.put(Integer.valueOf(10), new JLabel("1024"));
    h9.put(Integer.valueOf(11), new JLabel("2048"));
    h9.put(Integer.valueOf(12), new JLabel("4k"));
    h9.put(Integer.valueOf(13), new JLabel("8k"));
    slidercep.setLabelTable(h9);
    slidercep.setBorder(
        new TitledBorder(new EtchedBorder(), "Samples used for Cepstrum Generation"));
    slidercep.setToolTipText(
        "<html>The buffer size for the cepstal analyse<br>See the Information Window for the"
            + " resulting time");
    p.add(slidercep);
    sono.cv.len = (int) Math.pow(2.0, slidercep.getValue());
    p1.addTab("Cepstrum", new ImageIcon(Sonogram.class.getResource("small_cep.gif")), p);

    // Autocorrelation based Pitch Detection
    p = new JPanel();
    p.setLayout(new GridLayout(3, 1));
    p.setBorder(new TitledBorder(new EtchedBorder(), "Pitch Justification"));
    JPanel ppg = new JPanel();
    ppg.setLayout(new GridLayout(3, 2));
    ppg.setBorder(new TitledBorder(new EtchedBorder(), "General Pitch settings"));
    cppoints = new JCheckBox("Connect Pitches with Lines", sono.ipclin);
    cppoints.setToolTipText("Connect all the pitch points with lines");
    ppg.add(cppoints);
    p.add(ppg);
    cpsmooth = new JCheckBox("Smooth before Peak search", sono.ipsmo);
    cpsmooth.setToolTipText("<html>Smooth out the Autocorrelation before<br>searching the Peaks");
    ppg.add(cpsmooth);
    cplimitfr = new JCheckBox("Remove Pitches in Silent", sono.iprsil);
    cplimitfr.setToolTipText(
        "<html>Remove all the Pitches which<br>appear to lay in in <b>Silent</b> areas");
    ppg.add(cplimitfr);
    cpraway = new JCheckBox("Remove Runaways", sono.ipraway);
    cpraway.setToolTipText(
        "<html>Remove Pitch Points don't<br><b>Match the Curve</b> or lay in <br><b>Silent</b>"
            + " areas");
    ppg.add(cpraway);
    cptrack = new JCheckBox("Track Pitch", sono.iptrack);
    cptrack.setToolTipText(
        "<html>Track Pitch while <b>move the Mouse</b><br>over the pitch or the main window");
    ppg.add(cptrack);
    cpfog = new JCheckBox("Fog around Pitch", sono.ipfog);
    cpfog.setToolTipText("<html>Lays <b>Fog</b> arround Pitch");
    cpfog.setEnabled(cptrack.isSelected());
    ppg.add(cpfog);
    p.add(ppg);
    ps = new JPanel();
    ps.setLayout(new GridLayout(1, 2));

    sliderpwinlength = new JSlider(JSlider.HORIZONTAL, 0, 5, sono.isacpwl);
    sliderpwinlength.setBorder(new TitledBorder(new EtchedBorder(), "AC Window Length (ms)"));
    sliderpwinlength.setToolTipText("<html>Length of Window who is <b>Autocorrelated");
    sliderpwinlength.setMajorTickSpacing(1);
    sliderpwinlength.setMinorTickSpacing(1);
    sliderpwinlength.setPaintTicks(true);
    sliderpwinlength.setPaintLabels(true);
    sliderpwinlength.setSnapToTicks(true);
    Hashtable hpwl = new Hashtable();
    hpwl.put(Integer.valueOf(0), new JLabel("5"));
    hpwl.put(Integer.valueOf(1), new JLabel("10"));
    hpwl.put(Integer.valueOf(2), new JLabel("25"));
    hpwl.put(Integer.valueOf(3), new JLabel("50"));
    hpwl.put(Integer.valueOf(4), new JLabel("100"));
    hpwl.put(Integer.valueOf(5), new JLabel("250"));
    sliderpwinlength.setLabelTable(hpwl);
    ps.add(sliderpwinlength);

    sliderpwinshift = new JSlider(JSlider.HORIZONTAL, 0, 6, sono.isacpws);
    sliderpwinshift.setBorder(new TitledBorder(new EtchedBorder(), "AC Loop Length (ms)"));
    sliderpwinshift.setToolTipText(
        "<html>The length of the <b>Inner Summarization Loop</b><br>of the Autocorrelation"
            + " Algorithm for the Window");
    sliderpwinshift.setMajorTickSpacing(1);
    sliderpwinshift.setMinorTickSpacing(1);
    sliderpwinshift.setPaintTicks(true);
    sliderpwinshift.setPaintLabels(true);
    sliderpwinshift.setSnapToTicks(true);
    Hashtable hpws = new Hashtable();
    hpws.put(Integer.valueOf(0), new JLabel("1.25"));
    hpws.put(Integer.valueOf(1), new JLabel("2.5"));
    hpws.put(Integer.valueOf(2), new JLabel("5"));
    hpws.put(Integer.valueOf(3), new JLabel("10"));
    hpws.put(Integer.valueOf(4), new JLabel("25"));
    hpws.put(Integer.valueOf(5), new JLabel("50"));
    hpws.put(Integer.valueOf(6), new JLabel("100"));
    sliderpwinshift.setLabelTable(hpws);
    ps.add(sliderpwinshift);
    p.add(ps);

    if (sono.isacpmax == 0) sono.isacpmax = 400;
    sliderpitchmax = new JSlider(JSlider.HORIZONTAL, 0, 10000, sono.isacpmax);
    sliderpitchmax.setBorder(new TitledBorder(new EtchedBorder(), "Search Frequency Limiter"));
    sliderpitchmax.setToolTipText(
        "<html><b>Search Frequency Limiter</b><br>Restrict the search range for the pitch"
            + " detection<br>to the selected max value. This is useful for<br>speech format"
            + " detection.");
    sliderpitchmax.setMajorTickSpacing(2000);
    sliderpitchmax.setMinorTickSpacing(200);
    sliderpitchmax.setPaintTicks(true);
    sliderpitchmax.setPaintLabels(true);
    sliderpitchmax.setSnapToTicks(true);
    p.add(sliderpitchmax);
    p1.addTab(
        "AC Based Pitch Estimation",
        new ImageIcon(Sonogram.class.getResource("small_pitch.gif")),
        p);

    // Autocorrelation View
    p = new JPanel();
    p.setLayout(new GridLayout(3, 1));
    p.setBorder(new TitledBorder(new EtchedBorder(), "Autocorrelation Justification"));
    JPanel pacg = new JPanel();
    pacg.setLayout(new GridLayout(3, 2));
    pacg.setBorder(new TitledBorder(new EtchedBorder(), "General Autocorrelation settings"));
    cacpoints = new JCheckBox("Connect Points with Lines", sono.iacdl);
    cacpoints.setToolTipText("<html><b>Connect</b> Autocorrelation Sample<br>Points with <b>Lines");
    pacg.add(cacpoints);
    p.add(pacg);
    cacsmooth = new JCheckBox("Smooth Autocorrelation", sono.iacsmooth);
    cacsmooth.setToolTipText("<html><b>Smooth Out</b>Autocorrelation Curve");
    pacg.add(cacsmooth);
    p.add(pacg);
    cacpitch = new JCheckBox("Enable Pitch search", sono.iacpitch);
    cacpitch.setToolTipText("<html>Search <b>Pitches</b> in<br>the Autocorrelation Window");
    pacg.add(cacpitch);
    p.add(pacg);
    slideracwinlength = new JSlider(JSlider.HORIZONTAL, 0, 8, sono.isacwl);
    slideracwinlength.setBorder(
        new TitledBorder(new EtchedBorder(), "Autocorrelation Window Length"));
    slideracwinlength.setToolTipText("<html>Length of Window who is <b>Autocorrelated");
    slideracwinlength.setMajorTickSpacing(1);
    slideracwinlength.setMinorTickSpacing(1);
    slideracwinlength.setPaintTicks(true);
    slideracwinlength.setPaintLabels(true);
    slideracwinlength.setSnapToTicks(true);
    Hashtable hacwl = new Hashtable();
    hacwl.put(Integer.valueOf(0), new JLabel("10"));
    hacwl.put(Integer.valueOf(1), new JLabel("25"));
    hacwl.put(Integer.valueOf(2), new JLabel("50"));
    hacwl.put(Integer.valueOf(3), new JLabel("100"));
    hacwl.put(Integer.valueOf(4), new JLabel("250"));
    hacwl.put(Integer.valueOf(5), new JLabel("500"));
    hacwl.put(Integer.valueOf(6), new JLabel("1000"));
    hacwl.put(Integer.valueOf(7), new JLabel("2500"));
    hacwl.put(Integer.valueOf(8), new JLabel("5000"));
    slideracwinlength.setLabelTable(hacwl);
    p.add(slideracwinlength);
    slideracwinshift = new JSlider(JSlider.HORIZONTAL, 0, 8, sono.isacws);
    slideracwinshift.setBorder(
        new TitledBorder(new EtchedBorder(), "Autocorrelation Loop Length (ms)"));
    slideracwinshift.setToolTipText(
        "<html>The length of the <b>Inner Summarization Loop</b><br>of the Autocorrelation"
            + " Algorithm for the Window");
    slideracwinshift.setMajorTickSpacing(1);
    slideracwinshift.setMinorTickSpacing(1);
    slideracwinshift.setPaintTicks(true);
    slideracwinshift.setPaintLabels(true);
    slideracwinshift.setSnapToTicks(true);
    Hashtable hacws = new Hashtable();
    hacws.put(Integer.valueOf(0), new JLabel("2.5"));
    hacws.put(Integer.valueOf(1), new JLabel("5"));
    hacws.put(Integer.valueOf(2), new JLabel("10"));
    hacws.put(Integer.valueOf(3), new JLabel("25"));
    hacws.put(Integer.valueOf(4), new JLabel("50"));
    hacws.put(Integer.valueOf(5), new JLabel("100"));
    hacws.put(Integer.valueOf(6), new JLabel("250"));
    hacws.put(Integer.valueOf(7), new JLabel("500"));
    hacws.put(Integer.valueOf(8), new JLabel("1000"));
    slideracwinshift.setLabelTable(hacws);
    p.add(slideracwinshift);
    p1.addTab("Autocorrelation", new ImageIcon(Sonogram.class.getResource("small_auc.gif")), p);

    // Waveform
    p = new JPanel();
    p.setLayout(new GridLayout(2, 1));
    p.setBorder(new TitledBorder(new EtchedBorder(), "Waveform Justification"));
    JPanel pwa = new JPanel();
    pwa.setLayout(new GridLayout(3, 2));
    pwa.setBorder(
        new TitledBorder(new EtchedBorder(), "General Waveform settings for the Waveform Display"));
    cwavelines = new JCheckBox("Connect the Sample Points with Lines", sono.iwavelines);
    cwavelines.setToolTipText("If ths checkbox is selected all points are connected with lines");
    pwa.add(cwavelines);
    cwfnorm = new JCheckBox("Normalize the Waveform Alplitude to Maximum", sono.iwfnorm);
    cwfnorm.setToolTipText(
        "<htmnl><b>Normalizes the Waveform Amplitude</b><br>to the maximum hight of the Window");
    pwa.add(cwfnorm);
    p.add(pwa);
    sliderwave = new JSlider(JSlider.HORIZONTAL, 1, 7, sono.islwavetime);
    sliderwave.setToolTipText("Time division for waveform view");
    sliderwave.setMajorTickSpacing(1);
    sliderwave.setMinorTickSpacing(1);
    sliderwave.setPaintTicks(true);
    sliderwave.setPaintLabels(true);
    sliderwave.setSnapToTicks(true);
    Hashtable h10 = new Hashtable();
    h10.put(Integer.valueOf(1), new JLabel("1 ms"));
    h10.put(Integer.valueOf(2), new JLabel("2,5 ms"));
    h10.put(Integer.valueOf(3), new JLabel("10 ms"));
    h10.put(Integer.valueOf(4), new JLabel("25 ms"));
    h10.put(Integer.valueOf(5), new JLabel("100 ms"));
    h10.put(Integer.valueOf(6), new JLabel("250 ms"));
    h10.put(Integer.valueOf(7), new JLabel("1 s"));
    sliderwave.setLabelTable(h10);
    sliderwave.setBorder(
        new TitledBorder(new EtchedBorder(), "Time Span displayed in the Waveform"));
    sliderwave.setToolTipText("Time division of the Wavform Display");
    p.add(sliderwave);
    p1.addTab("Waveform", new ImageIcon(Sonogram.class.getResource("small_wav.gif")), p);

    // Transformation select for Sonogam
    p = new JPanel();
    p.setBorder(new TitledBorder(new EtchedBorder(), "Transformation used to generate Sonogram"));
    p.setLayout(new GridLayout(2, 1));
    ButtonGroup group2 = new ButtonGroup();
    rfft =
        new JRadioButton(
            "<html><b><font size=6>FFT</font></b> - Use Fast Fourier Transform Algorithm for the"
                + " generation of the main Sonogram",
            true);
    rfft.setToolTipText("Use FFT for the main display");
    group2.add(rfft);
    p.add(rfft);
    rlpc =
        new JRadioButton(
            "<html><b><font size=6>LPC</font></b> - Use Linear Prediction Coefficient Algorithm for"
                + " the generation of the main Sonogram<br><font size=-2 color=#000066><i>Use the"
                + " LPC settings pannel for the algorithm prarametrization");
    rlpc.setToolTipText(
        "<html>Use LPC for the main display <b>BETA !!!</b><br>Use the LPC settings pannel to trim"
            + " the<br>algorithm parameters.");
    group2.add(rlpc);
    p.add(rlpc);
    p1.addTab("Transformation", new ImageIcon(Sonogram.class.getResource("tra.gif")), p);
    if (sono.iffttrans == true) rfft.setSelected(true);
    else rlpc.setSelected(true);

    // FFT Pitchdetection
    p = new JPanel();
    p.setLayout(new GridLayout(2, 1));
    p.setBorder(new TitledBorder(new EtchedBorder(), "Amplitude Peak Detection Options"));
    sliderpitch = new JSlider(JSlider.HORIZONTAL, 0, 4000, sono.islpf);
    sliderpitch.setToolTipText("Serarchfrequency");
    sliderpitch.setMajorTickSpacing(500);
    sliderpitch.setMinorTickSpacing(100);
    sliderpitch.setPaintTicks(true);
    sliderpitch.setPaintLabels(true);
    sliderpitch.setSnapToTicks(true);
    sliderpitch.setBorder(
        new TitledBorder(new EtchedBorder(), "Frequency Limit for Pitch Detection"));
    p.add(sliderpitch);
    JPanel pcs = new JPanel();
    pcs.setLayout(new GridLayout(3, 2));
    cspitch = new JCheckBox("Enable Amplitude Peak Detection", sono.ipide);
    pcs.add(cspitch);
    cspitchlimitation = new JCheckBox("Frequency limitation", sono.ipifrlim);
    cspitchlimitation.setToolTipText("enables/disables frequency limitation in Peak detection");
    pcs.add(cspitchlimitation);
    cspitchonely = new JCheckBox("Fog over Sonogram", sono.ipitchfog);
    cspitchonely.setToolTipText("Lays a nebular over the Sonogram");
    pcs.add(cspitchonely);
    cspitchblack = new JCheckBox("Paint Peak white", sono.ipitchblack);
    cspitchblack.setToolTipText("Paint Peak white instead of red");
    pcs.add(cspitchblack);
    pcs.setBorder(new TitledBorder(new EtchedBorder(), "Options for Pitchdetection"));
    p.add(pcs);
    cspitchsmooth = new JCheckBox("Smooth Peak Detection", sono.ipitchsm);
    cspitchsmooth.setToolTipText("Generate Peak with smoothed Spectrum");
    pcs.add(cspitchsmooth);
    p1.addTab("Amplitude Peak", new ImageIcon(Sonogram.class.getResource("pit.gif")), p);
    if (cspitch.isSelected() == false) {
      cspitchlimitation.setEnabled(false);
      sliderpitch.setEnabled(false);
      cspitchsmooth.setEnabled(false);
      cspitchblack.setEnabled(false);
      cspitchonely.setEnabled(false);
    }

    // Logarithm Amplitude
    p = new JPanel();
    p.setLayout(new GridLayout(2, 1));
    p.setBorder(
        new TitledBorder(new EtchedBorder(), "Display Amplitude Energy in Logarithm Scale"));
    JPanel plo1 = new JPanel();
    plo1.setLayout(new GridLayout(3, 2));
    plo1.setBorder(
        new TitledBorder(new EtchedBorder(), "General Logarithm Amplitude Scale Settings"));
    clog = new JCheckBox("Enalble the Logarithm Amplitude Scaling", sono.iloga);
    clog.setToolTipText("Logarithmize amplitude in sonogramview");
    plo1.add(clog);
    sliderlog = new JSlider(JSlider.HORIZONTAL, 1, 5, sono.islla);
    sliderlog.setBorder(
        new TitledBorder(new EtchedBorder(), "Scale Trimming for Logarithm Amplitude"));
    sliderlog.setMajorTickSpacing(1);
    sliderlog.setMinorTickSpacing(1);
    sliderlog.setPaintTicks(true);
    sliderlog.setPaintLabels(true);
    sliderlog.setSnapToTicks(true);
    Hashtable h8 = new Hashtable();
    h8.put(Integer.valueOf(1), new JLabel("Least"));
    h8.put(Integer.valueOf(2), new JLabel("Less"));
    h8.put(Integer.valueOf(3), new JLabel("Middle"));
    h8.put(Integer.valueOf(4), new JLabel("More"));
    h8.put(Integer.valueOf(5), new JLabel("Most"));
    sliderlog.setLabelTable(h8);
    p.add(plo1);
    p.add(sliderlog);
    p1.addTab("Logarithm Amplitude", new ImageIcon(Sonogram.class.getResource("small_log.gif")), p);
    if (clog.isSelected() == false) sliderlog.setEnabled(false);

    // Logarithm Frequency
    p = new JPanel();
    p.setLayout(new GridBagLayout());
    p.setBorder(
        new TitledBorder(new EtchedBorder(), "Display the Frequency Axis in Logarithm Scale"));
    JPanel plof1 = new JPanel();
    plof1.setLayout(new GridLayout(3, 2));
    plof1.setBorder(new TitledBorder(new EtchedBorder(), "General Logarithm Scale Settings"));
    cslogfr = new JCheckBox("Enalble the Logarithmic Frequency Scale", sono.ilogf);
    cslogfr.setToolTipText(
        "<html>A <b>logarithmic scale</b> is a scale of measurement<br>that uses the logarithm of a"
            + " physical quantity<br>instead of the quantity itself. The frequency<br>scale will be"
            + " divided into decades. <b>CTRL-L</b>");
    GridBagConstraints c = new GridBagConstraints();
    c.ipadx = 120;
    c.gridy = 0;
    p.add(plof1, c);
    plof1.add(cslogfr);
    cgrid2 = new JCheckBox("Show the Grid in Sonogram", sono.igrid);
    cgrid2.setToolTipText("<html>Enables the Grid in the Sonogram main Window <b>CTRL-G</b>");
    c = new GridBagConstraints();
    c.ipadx = 120;
    c.gridy = 1;
    p.add(cgrid2, c);
    plof1.add(cgrid2);
    ctooltip = new JCheckBox("Live Value Window", sono.itooltip);
    ctooltip.setToolTipText(
        "<html><b>The Live-Value-Window</b><br>The live values information window shows<br>detailed"
            + " information about the spectrum values<br>under the mouse arrow in the main"
            + " window,<br>and is updated if the mouse is moved.");
    c = new GridBagConstraints();
    c.ipadx = 120;
    c.gridy = 2;
    p.add(ctooltip, c);
    plof1.add(ctooltip);
    JPanel plof2 = new JPanel();
    plof2.setLayout(new GridLayout(1, 2));
    plof2.setBorder(new TitledBorder(new EtchedBorder(), "Linear and Logarithm Scale"));
    imgLin = new JLabel(new ImageIcon(Sonogram.class.getResource("imgLin.png")));
    imgLog = new JLabel(new ImageIcon(Sonogram.class.getResource("imgLog.png")));
    if (marklogfr == true) {
      imgLog.setEnabled(true);
      imgLin.setEnabled(false);
    } else {
      imgLog.setEnabled(false);
      imgLin.setEnabled(true);
    }
    c = new GridBagConstraints();
    c.ipadx = 88;
    ;
    c.ipady = 5;
    c.gridy = 3;
    p.add(plof2, c);
    plof2.add(imgLin);
    plof2.add(imgLog);
    p1.addTab(
        "Logarithm Frequency", new ImageIcon(Sonogram.class.getResource("small_logfr.gif")), p);
    // if (cslogfr.isSelected()==false)
    // {
    //    cgrid2.setEnabled(false);
    // }

    // Gain
    p = new JPanel();
    p.setLayout(new GridLayout(2, 1));
    p.setBorder(new TitledBorder(new EtchedBorder(), "Gain Justification"));
    JPanel pga = new JPanel();
    pga.setLayout(new GridLayout(3, 2));
    pga.setBorder(new TitledBorder(new EtchedBorder(), "Gain settings"));
    cmute = new JCheckBox("Mute Sound", sono.imute);
    cmute.setToolTipText("Mute Sound while playing");
    pga.add(cmute);
    p.add(pga);
    sliderdb = new JSlider(JSlider.HORIZONTAL, -40, 20, sono.isldb);
    sliderdb.setToolTipText("Gain in dB");
    sliderdb.setMajorTickSpacing(10);
    sliderdb.setMinorTickSpacing(1);
    sliderdb.setPaintTicks(true);
    sliderdb.setPaintLabels(true);
    sliderdb.setSnapToTicks(true);
    sliderdb.setBorder(new TitledBorder(new EtchedBorder(), "Gain in dB"));
    sliderdb.setToolTipText("Select here Gain in dB (dezi Bell)");
    p.add(sliderdb);
    p1.addTab("Gain", new ImageIcon(Sonogram.class.getResource("gai.gif")), p);

    // WAVELET
    p = new JPanel();
    p.setLayout(new GridLayout(1, 2));
    pwa = new JPanel();
    JPanel pwa1 = new JPanel();
    JPanel pwa2 = new JPanel();
    JPanel pwa3 = new JPanel();
    pwa1.setBorder(new TitledBorder(new EtchedBorder(), "Wavelet Selection"));
    pwa1.setToolTipText("Select sort of wavelet");
    pwa2.setBorder(new TitledBorder(new EtchedBorder(), "Wavelet Octaves"));
    pwa2.setToolTipText("Select number of octaves which correlate with the window length");
    pwa3.setBorder(new TitledBorder(new EtchedBorder(), "General Settings"));
    pwa3.setToolTipText("Some options for wavelet window");
    pwa.setLayout(new GridLayout(3, 1));
    Object[] items = {
      wavelets.getSet(0).getName(),
      wavelets.getSet(1).getName(),
      wavelets.getSet(2).getName(),
      wavelets.getSet(3).getName(),
      wavelets.getSet(4).getName(),
      wavelets.getSet(5).getName(),
      wavelets.getSet(6).getName(),
      wavelets.getSet(7).getName(),
      wavelets.getSet(8).getName(),
      wavelets.getSet(9).getName(),
      wavelets.getSet(10).getName(),
      wavelets.getSet(11).getName(),
      wavelets.getSet(12).getName(),
      wavelets.getSet(13).getName(),
      wavelets.getSet(14).getName(),
      wavelets.getSet(15).getName(),
      wavelets.getSet(16).getName(),
    };
    wcb = new JComboBox(items);
    wcb.setSelectedIndex(sono.iswalsel);
    sliderwaltl = new JSlider(JSlider.HORIZONTAL, 6, 13, sono.iswaloct);
    sliderwaltl.setPaintLabels(true);
    sliderwaltl.setSnapToTicks(true);
    sliderwaltl.setMajorTickSpacing(1);
    sliderwaltl.setPaintTicks(true);
    waPanel wa = new waPanel();
    wa.setBorder(new TitledBorder(new EtchedBorder(), "Wavelet"));
    wa.setToolTipText("Wavelet Preview");
    tf3 = new JTextField("Window Length: 512", 12);
    tf3.setToolTipText("The Window length  correleates with the number of the octaves.");
    clogwal = new JCheckBox("Logarithm Amplitude", sono.iwallog);
    pwa1.add(wcb);
    pwa2.add(sliderwaltl);
    pwa3.add(clogwal);
    pwa3.add(tf3);
    pwa.add(pwa1);
    pwa.add(pwa2);
    pwa.add(pwa3);
    p.add(pwa);
    p.add(wa);
    p1.addTab("Wavelet", new ImageIcon(Sonogram.class.getResource("small_wal.gif")), p);
    walwindowlength = (int) Math.pow(2.0, sono.iswaloct);

    // Eventhandling
    DialogListener lst = new DialogListener();
    csmoothsi.addChangeListener(lst);
    cscolsi.addChangeListener(lst);
    cback.addChangeListener(lst);
    sliderlog.addChangeListener(lst);
    sliderwinsize.addChangeListener(lst);
    cauto.addChangeListener(lst);
    sliderwinfunktion.addChangeListener(lst);
    sliderwinspeed.addChangeListener(lst);
    slidersurface.addChangeListener(lst);
    sliderformant.addChangeListener(lst);
    sliderformantlen.addChangeListener(lst);
    slidersmoothfft.addChangeListener(lst);
    sliderlpcsamfutur.addChangeListener(lst);
    sliderlpccoef.addChangeListener(lst);
    sliderlpcfftnum.addChangeListener(lst);
    sliderlpcfre.addChangeListener(lst);
    cllog.addChangeListener(lst);
    clfor.addChangeListener(lst);
    csampl.addChangeListener(lst);
    cinv.addChangeListener(lst);
    cenergy.addChangeListener(lst);
    csmooth.addChangeListener(lst);
    csmoothx.addChangeListener(lst);
    clog.addChangeListener(lst);
    cgrid.addChangeListener(lst);
    cspitch.addChangeListener(lst);
    rfft.addChangeListener(lst);
    r0.addChangeListener(lst);
    r1.addChangeListener(lst);
    r2.addChangeListener(lst);
    r3.addChangeListener(lst);
    r4.addChangeListener(lst);
    r5.addChangeListener(lst);
    cspitchlimitation.addChangeListener(lst);
    cspitch.addChangeListener(lst);
    coverlapping.addChangeListener(lst);
    cspitchonely.addChangeListener(lst);
    cspitchblack.addChangeListener(lst);
    cspitchsmooth.addChangeListener(lst);
    cb.addChangeListener(lst);
    cslogfr.addChangeListener(lst);
    slidersurfacex.addChangeListener(lst);
    slidersurfacey.addChangeListener(lst);
    sliderpitch.addChangeListener(lst);
    s1.addChangeListener(lst);
    s2.addChangeListener(lst);
    s3.addChangeListener(lst);
    cgrid2.addChangeListener(lst);
    csopenlast.addChangeListener(lst);
    ccep.addChangeListener(lst);
    ccepsmooth.addChangeListener(lst);
    slidercep.addChangeListener(lst);
    sliderwave.addChangeListener(lst);
    cwavelines.addChangeListener(lst);
    cmute.addChangeListener(lst);
    sliderdb.addChangeListener(lst);
    clocalpeak.addChangeListener(lst);
    cuniverse.addChangeListener(lst);
    wcb.addActionListener(lst);
    sliderwaltl.addChangeListener(lst);
    clogwal.addChangeListener(lst);
    slideracwinshift.addChangeListener(lst);
    slideracwinlength.addChangeListener(lst);
    cacpoints.addChangeListener(lst);
    cacpitch.addChangeListener(lst);
    cacsmooth.addChangeListener(lst);
    cantialise.addChangeListener(lst);
    cwfnorm.addChangeListener(lst);
    cplimitfr.addChangeListener(lst);
    cppoints.addChangeListener(lst);
    sliderpwinshift.addChangeListener(lst);
    sliderpitchmax.addChangeListener(lst);
    sliderpwinlength.addChangeListener(lst);
    cpsmooth.addChangeListener(lst);
    cpraway.addChangeListener(lst);
    cscrennsaver.addChangeListener(lst);
    cperantialias.addChangeListener(lst);
    cptrack.addChangeListener(lst);
    cpfog.addChangeListener(lst);

    // Set Text-Colors for the Tabs Titles
    p1.setForegroundAt(0, sono.pp.coldv); // Windowlength
    p1.setForegroundAt(1, sono.pp.coldv); // Windowfunctions
    p1.setForegroundAt(2, new Color(120, 30, 30)); // Colors
    p1.setForegroundAt(3, new Color(40, 40, 90)); // General
    p1.setForegroundAt(4, sono.pp.coldv); // Overlapping
    p1.setForegroundAt(5, new Color(30, 30, 50)); // Perspectogram
    p1.setForegroundAt(6, new Color(90, 10, 80)); // FFT
    p1.setForegroundAt(7, new Color(90, 10, 80)); // LPC
    p1.setForegroundAt(8, new Color(100, 100, 100)); // Cepstrum
    p1.setForegroundAt(9, sono.pp.coldv); // AC based Pitch
    p1.setForegroundAt(10, new Color(90, 10, 80)); // Autocorrelation
    p1.setForegroundAt(11, new Color(90, 10, 80)); // Waveform
    p1.setForegroundAt(12, new Color(40, 40, 90)); // Transformation
    p1.setForegroundAt(13, new Color(75, 97, 37)); // Sono Peak
    p1.setForegroundAt(14, new Color(75, 97, 37)); // Log Amp
    p1.setForegroundAt(15, new Color(75, 97, 37)); // Log Frequ
    p1.setForegroundAt(16, sono.pp.coldv); // Gain
    p1.setForegroundAt(17, new Color(90, 10, 80)); // Wavelet

    applyChanges();
    getContentPane().add(p2, BorderLayout.CENTER);
    p1.setSelectedIndex(3);
    pack();
    setSize(sono.gadh, sono.gadw);
    setResizable(false);
  }
  // -------------------------------------------------------------------------------------------------------------------------
  /** This Funktion sets selected Options to Sonogram */
  public void aktualize() {
    getOptionsFormMainWin();
    if (color == 0) r0.setSelected(true);
    if (color == 1) r1.setSelected(true);
    if (color == 2) r2.setSelected(true);
    if (color == 3) r3.setSelected(true);
    if (color == 4) r4.setSelected(true);
    if (color == 5) r5.setSelected(true);
    sliderwinfunktion.setValue(winfunktion);
    if (inv == true) cinv.setSelected(true);
    else cinv.setSelected(false);
    if (grid == true) {
      cgrid.setSelected(true);
      cgrid2.setSelected(true);
    } else {
      cgrid.setSelected(false);
      cgrid2.setSelected(false);
    }
    if (log == true) clog.setSelected(true);
    else clog.setSelected(false);
    if (cauto.isSelected() == true) sliderwinsize.setEnabled(false);
    else sliderwinsize.setEnabled(true);
  }
  // -------------------------------------------------------------------------------------------------------------------------
  /** This Funktion gets Options from Sonogram. */
  private void getOptionsFormMainWin() {
    if (sono.rectItem.isSelected()) winfunktion = 1;
    if (sono.blaItem.isSelected()) winfunktion = 2;
    if (sono.triItem.isSelected()) winfunktion = 3;
    if (sono.hamItem.isSelected()) winfunktion = 4;
    if (sono.welItem.isSelected()) winfunktion = 5;
    if (sono.gauItem.isSelected()) winfunktion = 6;
    if (sono.hanItem.isSelected()) winfunktion = 7;
    if (sono.flaItem.isSelected()) winfunktion = 8;
    if (sono.harItem.isSelected()) winfunktion = 9;
    if (sono.harItem.isSelected()) winfunktion = 10;
    if (sono.fireItem.isSelected()) color = 1;
    if (sono.colItem.isSelected()) color = 2;
    if (sono.rainItem.isSelected()) color = 3;
    if (sono.greenItem.isSelected()) color = 4;
    if (sono.bwItem.isSelected()) color = 5;
    if (sono.gridItem.isSelected()) grid = true;
    else grid = false;
    if (sono.negItem.isSelected()) inv = true;
    else inv = false;
    if (sono.logItem.isSelected()) log = true;
    else log = false;
  }
  // -------------------------------------------------------------------------------------------------------------------------
  public void applyChanges() {

    if (winfunktion == 1) sono.rectItem.setSelected(true);
    if (winfunktion == 2) sono.blaItem.setSelected(true);
    if (winfunktion == 3) sono.triItem.setSelected(true);
    if (winfunktion == 4) sono.hamItem.setSelected(true);
    if (winfunktion == 5) sono.welItem.setSelected(true);
    if (winfunktion == 6) sono.gauItem.setSelected(true);
    if (winfunktion == 7) sono.hanItem.setSelected(true);
    if (winfunktion == 8) sono.flaItem.setSelected(true);
    if (winfunktion == 9) sono.harItem.setSelected(true);
    if (winfunktion == 10) sono.cosItem.setSelected(true);
    if (winfunktion == 11) sono.asyItem.setSelected(true);

    if (clog.isSelected() == true) sono.logItem.setSelected(true);
    else sono.logItem.setSelected(false);
    if (cinv.isSelected() == true) sono.negItem.setSelected(true);
    else sono.negItem.setSelected(false);
    if (cgrid.isSelected()) sono.gridItem.setSelected(true);
    else sono.gridItem.setSelected(false);
    if (cslogfr.isSelected()) sono.logfrItem.setSelected(true);
    else sono.logfrItem.setSelected(false);

    if (r0.isSelected() == true) {
      sono.firecItem.setSelected(true);
      color = 0;
      sono.colorSlider.setValue(0);
    }
    if (r1.isSelected() == true) {
      sono.fireItem.setSelected(true);
      color = 1;
      sono.colorSlider.setValue(1);
    }
    if (r2.isSelected() == true) {
      sono.colItem.setSelected(true);
      color = 2;
      sono.colorSlider.setValue(2);
    }
    if (r3.isSelected() == true) {
      sono.rainItem.setSelected(true);
      color = 3;
      sono.colorSlider.setValue(3);
    }
    if (r4.isSelected() == true) {
      sono.greenItem.setSelected(true);
      color = 4;
      sono.colorSlider.setValue(4);
    }
    if (r5.isSelected() == true) {
      sono.bwItem.setSelected(true);
      color = 5;
      sono.colorSlider.setValue(5);
    }
  }
  // -------------------------------------------------------------------------------------------------------------------------
  // =======================================================================================
  /** Inner class for Eventhandling */
  class DialogListener implements ChangeListener, ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getSource() == wcb) {
        repaint();
        sono.av.update();
        sono.infod.update();
      }
    }

    public void stateChanged(ChangeEvent e) {
      if (e.getSource() == cptrack) {
        cpfog.setEnabled(cptrack.isSelected());
      }
      if (e.getSource() == cppoints
          || e.getSource() == sliderpitchmax
          || e.getSource() == cptrack
          || e.getSource() == cpfog) {
        sono.pv.update();
        sono.infod.update();
      }
      if (e.getSource() == sliderpwinlength
          || e.getSource() == sliderpwinshift
          || e.getSource() == cpsmooth
          || e.getSource() == cpraway
          || e.getSource() == cplimitfr) {
        sono.pv.calculatePitchesWithoutCheck();
        sono.infod.update();
      }
      if (e.getSource() == cacpoints
          || e.getSource() == slideracwinshift
          || e.getSource() == slideracwinlength
          || e.getSource() == cacpitch
          || e.getSource() == cacsmooth) {

        sono.kv.update();
        sono.infod.update();
      }

      if (e.getSource() == clogwal) {
        sono.av.update();
      }
      if (e.getSource() == cantialise) {
        sono.av.update();
        sono.lv.update();
        sono.fv.update();
        sono.cv.update();
        sono.kv.update();
        sono.wv.update();
        sono.pv.update();
        sono.pp.repaint();
      }
      if (e.getSource() == sliderwaltl) {
        walwindowlength = (int) Math.pow(2.0, (double) sliderwaltl.getValue());
        tf3.setText("Window Length: " + walwindowlength);
        sono.av.update();
        sono.infod.update();
      }
      if (e.getSource() == sliderwinfunktion) {
        winfunktion = (byte) sliderwinfunktion.getValue();
        highLightButton(2);
        repaint();
      }
      if (e.getSource() == sliderwinsize) highLightButton(2);
      if (e.getSource() == sliderlog) if (clog.isEnabled() == true) highLightButton(2);
      if (e.getSource() == cauto && cauto.isEnabled() == true) {
        if (cauto.isSelected() == sliderwinsize.isEnabled()) {
          highLightButton(2);
          if (cauto.isSelected() == true) {
            sliderwinsize.setEnabled(false);
          } else {
            sliderwinsize.setEnabled(true);
          }
        }
      }
      if (e.getSource() == sliderwinspeed) {
        if (coverlapping.isEnabled() == true) highLightButton(2);
        if (sliderwinspeed.getValue() == 0) sliderwinspeed.setValue(1);
      }
      if (e.getSource() == slidersurface) {
        if (slidersurface.getValue() == 0) slidersurface.setValue(1);
      }
      if (e.getSource() == sliderformant) {
        sono.fv.update();
      }
      if (e.getSource() == slidersmoothfft) {
        sono.fv.update();
      }
      if (e.getSource() == sliderformantlen) {
        int len = 0;
        if (sono.spektrumExist == true) {
          switch (sliderformantlen.getValue()) {
            case 1:
              len = 512;
              break;
            case 2:
              len = 1024;
              break;
            case 3:
              len = 2048;
              break;
            case 4:
              len = 4096;
              break;
            case 5:
              len = 8192;
              break;
          }
          if (sono.samplesall < len)
            sono.messageBox("Sonogram Settings", "Signal to short for this bufferlength", 1);
          else {
            sono.fv.update();
            sono.fv.len = len;
            sono.fv.update();
            if (sono.infovisible) sono.infod.update();
          }
        }
      }
      if (e.getSource() == sliderlpcfftnum
          || e.getSource() == sliderlpccoef
          || e.getSource() == sliderlpcsamfutur
          || e.getSource() == sliderlpcfre
          || e.getSource() == cllog
          || e.getSource() == clfor) {
        if (sliderlpccoef.getValue() < 2) sliderlpccoef.setValue(2);
        if (sliderlpcsamfutur.getValue() < 70) sliderlpcsamfutur.setValue(70);
        tf1.setText("LPC Coefficients: " + java.lang.Integer.toString(sliderlpccoef.getValue()));
        tf2.setText(
            "Previous Samples: " + java.lang.Integer.toString(sliderlpcsamfutur.getValue()));
        sono.lv.update();
        sono.infod.update();
      }
      if (e.getSource() == csampl) { // Reopen File by changing Samplerate
        if (csampl.isSelected() != markcsamp) {
          markcsamp = csampl.isSelected();
          if (sono.filepath != null && sono.spektrumExist == true) {
            System.out.println("--> Reopen from Samplerateselector");
            sono.openFile(sono.filepath);
          }
        }
      }
      if (e.getSource() == cinv) { // Inverse Colors
        if (cinv.isSelected() != inv) {
          applyChanges();
          repaint();
          inv = cinv.isSelected();
          if (sono.spektrumExist == true) {
            sono.updateimageflag = true;
            sono.repaint();
            sono.av.update();
          }
        }
      }
      if (e.getSource() == cenergy) { // Energy flag event
        if (cenergy.isSelected() != markste && sono.spektrumExist == true) {
          markste = cenergy.isSelected();
          System.out.println("--> Update from Energy Select");
          applyChanges();
          sono.readerIsBack();
        }
      }
      if (e.getSource() == csmooth) { // Smooth-Checker Y Event
        if (csmooth.isSelected() == true)
          sono.smoothfrbutton.setBorder(BorderFactory.createLoweredBevelBorder());
        else if (sono.fullbutton
            != null) // prevents error wen fullbutto is not instanciated while startup
        sono.smoothfrbutton.setBorder(sono.fullbutton.getBorder());
        if (csmooth.isSelected() != marksmothy && sono.spektrumExist == true) {
          marksmothy = csmooth.isSelected();
          applyChanges();
          sono.readerIsBack();
        }
      }
      if (e.getSource() == csmoothx) { // Smooth-Checker X Event
        if (csmoothx.isSelected() == true)
          sono.smoothtmbutton.setBorder(BorderFactory.createLoweredBevelBorder());
        else if (sono.fullbutton
            != null) // prevents error wen fullbutto is not instanciated while startup
        sono.smoothtmbutton.setBorder(sono.fullbutton.getBorder());
        if (csmoothx.isSelected() != marksmothx && sono.spektrumExist == true) {
          marksmothx = csmoothx.isSelected();
          applyChanges();
          sono.readerIsBack();
        }
      }
      if (e.getSource() == clog) sono.logItem.setSelected(clog.isSelected());
      if (e.getSource() == clog && clog.isEnabled() == true) { // Logrithm-Checker Event
        sliderlog.setEnabled(clog.isSelected());
        if (clog.isSelected() != marklog && sono.spektrumExist == true) {
          marklog = clog.isSelected();
          applyChanges();
          System.out.println("--> Update from Logarithm Select");
          sono.readerIsBack();
        }
      }
      if (e.getSource() == cgrid2) { // Grid-Checker Event
        if (cgrid2.isSelected() != markgrid) {
          markgrid = cgrid2.isSelected();
          cgrid.setSelected(markgrid);
          sono.gridItem.setSelected(markgrid);
          if (sono.spektrumExist == true) {
            applyChanges();
            sono.updateimageflag = true;
            System.out.println("--> Update from Grid Select");
            sono.repaint();
            if (sono.gridItem.isSelected() == true)
              sono.gridbutton.setBorder(BorderFactory.createLoweredBevelBorder());
            else sono.gridbutton.setBorder(sono.fullbutton.getBorder());
          }
        }
      }
      if (e.getSource() == cgrid) { // Grid-Checker Event
        if (cgrid.isSelected() != markgrid) {
          markgrid = cgrid.isSelected();
          cgrid2.setSelected(markgrid);
          sono.gridItem.setSelected(markgrid);
          if (sono.spektrumExist == true) {
            applyChanges();
            sono.updateimageflag = true;
            System.out.println("--> Update from Grid Select");
            sono.repaint();
            if (sono.gridItem.isSelected() == true)
              sono.gridbutton.setBorder(BorderFactory.createLoweredBevelBorder());
            else sono.gridbutton.setBorder(sono.fullbutton.getBorder());
          }
        }
      }
      if (e.getSource() == rfft || e.getSource() == rlpc) { // Transformation Select
        if (rlpc.isSelected() == true) {
          Border redlineborder = BorderFactory.createLineBorder(Color.red);
          sliderlpccoef.setBorder(
              BorderFactory.createTitledBorder(redlineborder, "LPC Coefficients"));
          sliderlpcsamfutur.setBorder(
              BorderFactory.createTitledBorder(redlineborder, "Prediction Buffer"));
        }
        if (rfft.isSelected() == true) {
          sliderlpcsamfutur.setBorder(new TitledBorder(new EtchedBorder(), "Prediction Buffer"));
          sliderlpccoef.setBorder(new TitledBorder(new EtchedBorder(), "LPC Coefficents"));
        }
        if (rfft.isSelected() != marktrans && sono.spektrumExist == false) {
          marktrans = rfft.isSelected();
        }
        if (rfft.isSelected() != marktrans && sono.spektrumExist == true) {
          marktrans = rfft.isSelected();
          if (rfft.isSelected() == true) System.out.println("--> FFT Transformation Selected.");
          else System.out.println("--> LPC Transformation Selected");
          sono.readerIsBack();
          if (rlpc.isSelected() == true) {
            p1.setSelectedIndex(7);
            sono.messageBox(
                "Information",
                "For better views with LPC Transformation\n"
                    + "use a greater scale for logarithm amlitude\n"
                    + "and a large number of LPC Coefficients.",
                1);
          }
        }
      }
      if (e.getSource() == r0
          || e.getSource() == r1
          || e.getSource() == r2
          || e.getSource() == r3
          || e.getSource() == r4
          || e.getSource() == r5) {
        applyChanges();
        if (markcolor != color) {
          repaint();
          markcolor = color;
          if (sono.spektrumExist == true) {
            System.out.println("--> Update from Color Selector");
            sono.updateimageflag = true;
            sono.repaint();
            sono.av.update();
          }
        }
      }
      if (e.getSource() == sliderpitch) {
        highLightButton(1);
        if (sliderpitch.getValue() < 100) sliderpitch.setValue(100);
      }
      if (e.getSource() == cspitch) {
        if (cspitch.isSelected() != markpitch) {
          markpitch = cspitch.isSelected();
          cspitchlimitation.setEnabled(cspitch.isSelected());
          sliderpitch.setEnabled(cspitch.isSelected() && cspitchlimitation.isSelected());
          cspitchonely.setEnabled(cspitch.isSelected());
          cspitchblack.setEnabled(cspitch.isSelected());
          cspitchsmooth.setEnabled(cspitch.isSelected());
          if (sono.spektrumExist == true) {
            sono.updateimageflag = true;
            sono.repaint();
          }
        }
      }
      if (e.getSource() == cspitchlimitation) {
        if (cspitchlimitation.isSelected() != markpitchlim) {
          markpitchlim = cspitchlimitation.isSelected();
          sliderpitch.setEnabled(cspitchlimitation.isSelected());
          if (sono.spektrumExist == true) {
            sono.updateimageflag = true;
            sono.repaint();
          }
        }
      }
      if (e.getSource() == cspitchblack) {
        if (cspitchblack.isSelected() != markpitchbla) {
          markpitchbla = cspitchblack.isSelected();
          if (sono.spektrumExist == true) {
            sono.updateimageflag = true;
            sono.repaint();
          }
        }
      }
      if (e.getSource() == cspitchonely) {
        if (cspitchonely.isSelected() != markpitchone) {
          markpitchone = cspitchonely.isSelected();
          if (sono.spektrumExist == true) {
            sono.updateimageflag = true;
            sono.repaint();
          }
        }
      }
      if (e.getSource() == cspitchsmooth) {
        if (cspitchsmooth.isSelected() != markpitchsmo) {
          markpitchsmo = cspitchsmooth.isSelected();
          if (sono.spektrumExist == true) {
            sono.updateimageflag = true;
            sono.repaint();
          }
        }
      }
      if (e.getSource() == coverlapping && coverlapping.isEnabled() == true) {
        if (markoverl != coverlapping.isSelected()) {
          sliderwinspeed.setEnabled(coverlapping.isSelected());
          markoverl = coverlapping.isSelected();
          if (sono.spektrumExist == true) sono.readerIsBack();
        }
      }
      if (e.getSource() == cslogfr) {
        if (marklogfr != cslogfr.isSelected()) {
          marklogfr = cslogfr.isSelected();
          if (marklogfr == true) {
            imgLog.setEnabled(true);
            imgLin.setEnabled(false);
          } else {
            imgLog.setEnabled(false);
            imgLin.setEnabled(true);
          }
          repaint();
          sono.logfrItem.setSelected(marklogfr);
          if (sono.logfrItem.isSelected() == true)
            sono.logbutton.setBorder(BorderFactory.createLoweredBevelBorder());
          else sono.logbutton.setBorder(sono.fullbutton.getBorder());
          // cgrid2.setEnabled(marklogfr);
          sono.updateimageflag = true;
          sono.repaint();
        }
      }
      if (e.getSource() == slidersurfacex
          || e.getSource() == slidersurfacey
          || e.getSource() == sliderpitch
          || e.getSource() == s1
          || e.getSource() == s2
          || e.getSource() == s3
          || e.getSource() == sliderwinfunktion) {
        if (slidersurfacex.getValue() == 0) slidersurfacex.setValue(1);
        if (slidersurfacey.getValue() == 0) slidersurfacey.setValue(1);
        sono.infod.update();
      }
      if (e.getSource() == csopenlast) {
        copenlastwithzoom.setEnabled(csopenlast.isSelected());
        csarrange.setEnabled(csopenlast.isSelected());
      }
      if (e.getSource() == ccep || e.getSource() == slidercep || e.getSource() == ccepsmooth) {
        sono.cv.len = (int) Math.pow(2.0, slidercep.getValue());
        sono.cv.update();
        sono.infod.update();
      }
      if (e.getSource() == cwavelines || e.getSource() == sliderwave || e.getSource() == cwfnorm) {
        sono.wv.update();
      }
      if (e.getSource() == sliderdb) {
        if (sono.player.gain != null) sono.player.gain.setDB((float) sliderdb.getValue());
      }
      if (e.getSource() == cmute) {
        if (sono.player != null) sono.player.gain.setMute(cmute.isSelected());
      }
      if (e.getSource() == clocalpeak) {
        if (marklocalpeak != clocalpeak.isSelected()) {
          marklocalpeak = clocalpeak.isSelected();
          if (sono.spektrumExist == true) {
            sono.updateimageflag = true;
            sono.repaint();
          }
        }
      }
      if (e.getSource() == cuniverse) {
        cb.setEnabled(!cuniverse.isSelected());
      }
      if (e.getSource() == cscolsi) {
        if (sono.spektrumExist == true) sono.pp.paintOneSpektrum(false);
      }
      if (e.getSource() == cback) {
        if (sono.spektrumExist == true) sono.pp.paintOneSpektrum(false);
      }
      if (e.getSource() == csmoothsi) {
        if (sono.spektrumExist == true) sono.pp.paintOneSpektrum(false);
      }
      if (e.getSource() == cscrennsaver) {
        sono.irotate = cscrennsaver.isSelected();
      }
      if (e.getSource() == cperantialias) {
        if (cperantialias.isSelected() == true
            && cperantialiaslast == false
            && cperantialiasconfirm == false) {
          JCheckBox askAgain =
              new JCheckBox("<html><i><font size = -2>Do not show this warning message again");
          Object[] message = new Object[2];
          message[0] =
              "<html>Antialiasing with Java3D <u><font COLOR=#660000>decreases the Graphic"
                  + " Performace<br>dramatically</u> and is not supported on all Operating"
                  + " Systems.<br><br><font size=4>Enable the Java3D Antialiasing ?";
          message[1] = askAgain;
          int confirm =
              JOptionPane.showConfirmDialog(
                  GeneralAdjustmentDialog.this,
                  message,
                  "Antialiasing decreases the 3D Performance",
                  JOptionPane.YES_NO_OPTION,
                  JOptionPane.WARNING_MESSAGE);
          if (askAgain.isSelected() == true) {
            cperantialiasconfirm = true;
            cperantialias.setSelected(true);
          }
          if (confirm == JOptionPane.YES_OPTION) {
            cperantialiaslast = true;
            cperantialias.setSelected(true);
          } else cperantialias.setSelected(false);
        }
        sono.iperantialias = cperantialias.isSelected();
        cperantialiaslast = cperantialias.isSelected();
      }
    }
  }
  // -------------------------------------------------------------------------------------------------------------------------
  public void highLightButton(int highlighted) {
    /**
     * 0 = Reset all buttons to default. 1 = Set redraw button RED 2 = Set redcalculate button RED 3
     * = Set redopen button RED ´
     */
    if (sono.spektrumExist == false) highlighted = 0;
    if (highlighted == 0) {
      btro.setBorder(btcl.getBorder());
      btap.setBorder(btcl.getBorder());
      btrd.setBorder(btcl.getBorder());
    }
    if (highlighted > highlightedbutton) {
      if (highlighted == 1) {
        btro.setBorder(btcl.getBorder());
        btap.setBorder(btcl.getBorder());
        btrd.setBorder(new MatteBorder(2, 2, 2, 2, Color.red));
      }
      if (highlighted == 2) {
        btro.setBorder(btcl.getBorder());
        btap.setBorder(new MatteBorder(2, 2, 2, 2, Color.red));
        btrd.setBorder(btcl.getBorder());
      }
      if (highlighted == 3) {
        btro.setBorder(new MatteBorder(2, 2, 2, 2, Color.red));
        btap.setBorder(btcl.getBorder());
        btrd.setBorder(btcl.getBorder());
      }
    }
    highlightedbutton = highlighted;
  }
  // =========================================================================================================================
  /** Inner Panel class to paint Windowfunktion */
  // -------------------------------------------------------------------------------------------------------------------------
  class wfPanel extends JPanel {
    // -------------------------------------------------------------------------------------------------------------------------
    public void paintComponent(Graphics g1) {
      /** this inner class paint windowfunktion on on Window-Funktion Tab */
      int height = getSize().height;
      int width = getSize().width;
      int minimunstoppbandattenuation = 0;
      int peakamplitudeofsidelobe = 0;
      String str = "";
      if (width < 50) return;
      Graphics2D g = (Graphics2D) g1;
      if (cantialise.isSelected() == true) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(
            RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      }

      // grid & rect
      g.setColor(new Color(25, 40, 15));
      g.fillRect(10, 25, width - 20, height - 35);
      g.setColor(new Color(30, 60, 30));
      for (int y = 25; y < height - 10; y += 5) g.drawLine(10, y, width - 11, y);
      for (int x = 10; x < width - 11; x += 5) g.drawLine(x, 25, x, height - 11);
      // red lines
      g.setColor(new Color(255, 80, 0));
      g.drawLine(20, height - 130, width - 21, height - 130);
      g.drawLine(25, height - 125, 25, height - 135);
      g.drawLine(width - 25, height - 125, width - 25, height - 135);
      g.drawLine(20, height - 30, width - 21, height - 30);
      g.drawLine(25, height - 25, 25, height - 35);
      g.drawLine(width - 25, height - 25, width - 25, height - 35);

      // build winfunktion
      float fktvect[] = null;
      if (winfunktion == 1) {
        fktvect = WindowFunktion.rectangleWindow(width - 50);
        minimunstoppbandattenuation = -21;
        peakamplitudeofsidelobe = -13;
        fktvect[0] = 0.0f;
        fktvect[width - 51] = 0.0f;
      }
      if (winfunktion == 2) {
        fktvect = WindowFunktion.blackmanWindow(width - 50);
        minimunstoppbandattenuation = -74;
        peakamplitudeofsidelobe = -57;
      }
      if (winfunktion == 3) {
        fktvect = WindowFunktion.triangleWindow(width - 50);
        minimunstoppbandattenuation = -25;
        peakamplitudeofsidelobe = -25;
      }
      if (winfunktion == 4) {
        fktvect = WindowFunktion.hammingWindow(width - 50);
        minimunstoppbandattenuation = -53;
        peakamplitudeofsidelobe = -42;
      }
      if (winfunktion == 5) {
        fktvect = WindowFunktion.welchWindow(width - 50);
        minimunstoppbandattenuation = -33;
        peakamplitudeofsidelobe = -22;
      }
      if (winfunktion == 6) {
        fktvect = WindowFunktion.gaussWindow(width - 50);
        minimunstoppbandattenuation = -55;
        peakamplitudeofsidelobe = -58;
      }
      if (winfunktion == 7) {
        fktvect = WindowFunktion.hanningWindow(width - 50);
        minimunstoppbandattenuation = -44;
        peakamplitudeofsidelobe = -31;
      }
      if (winfunktion == 8) {
        fktvect = WindowFunktion.flattopWindow(width - 50);
        minimunstoppbandattenuation = -54;
        peakamplitudeofsidelobe = -93;
      }
      if (winfunktion == 9) {
        fktvect = WindowFunktion.harrisWindow(width - 50);
        minimunstoppbandattenuation = -56;
        peakamplitudeofsidelobe = -90;
      }
      if (winfunktion == 10) {
        fktvect = WindowFunktion.cosineWindow(width - 50);
        minimunstoppbandattenuation = -31;
        peakamplitudeofsidelobe = -52;
      }
      if (winfunktion == 11) {
        fktvect = WindowFunktion.asymetricalWindow(width - 50);
        minimunstoppbandattenuation = -35;
        peakamplitudeofsidelobe = -24;
      }
      // paint text
      g.setColor(new Color(110, 150, 240));
      g.setFont(new Font("Courier", Font.PLAIN, 10));
      str = "Stopband Attenuation = " + minimunstoppbandattenuation + "dB";
      g.drawString(str, 16, 36);
      str = "Sidelobe Amplitude   = " + peakamplitudeofsidelobe + "dB";
      g.drawString(str, 16, 48);

      // calc the transfer function
      float[] td = new float[512];
      for (int i = 0; i < width - 50; i++) td[i / 4] = fktvect[i];
      FastFourierTransform fft = new FastFourierTransform();
      float[] magSpec = fft.doFFT(td);
      for (int i = 1; i < (width - 50); i++)
        magSpec[i] = (float) Math.sqrt(Math.sqrt(Math.log1p(Math.log1p(Math.log1p(magSpec[i])))));
      float max = 0.0f;
      for (int i = 1; i < (width - 50); i++) if (magSpec[i] > max) max = magSpec[i];
      for (int i = 1; i < (width - 50); i++) magSpec[i] /= max;

      // paint the funktion curve and the transfer function

      g.setStroke(new BasicStroke(3));
      g.setColor(new Color(50, 100, 40));
      for (int x = 1; x < (width - 50); x++)
        g.drawLine(
            x + 24,
            height - 130 - (int) (fktvect[x - 1] * (height - 190)),
            x + 25,
            height - 130 - (int) (fktvect[x] * (height - 190)));
      g.setStroke(new BasicStroke(1));
      g.setColor(new Color(220, 255, 40));
      for (int x = 1; x < (width - 50); x++)
        g.drawLine(
            x + 24,
            height - 130 - (int) (fktvect[x - 1] * (height - 190)),
            x + 25,
            height - 130 - (int) (fktvect[x] * (height - 190)));

      g.setStroke(new BasicStroke(3));
      g.setColor(new Color(50, 100, 40));
      for (int x = 2; x < (width / 2 - 24); x++) {
        g.drawLine(
            149 - x,
            height - 30 - (int) (magSpec[x - 1] * 90),
            148 - x,
            height - 30 - (int) (magSpec[x] * 90));
        g.drawLine(
            145 + x,
            height - 30 - (int) (magSpec[x - 1] * 90),
            146 + x,
            height - 30 - (int) (magSpec[x] * 90));
      }

      g.setStroke(new BasicStroke(1));
      g.setColor(new Color(220, 255, 40));
      for (int x = 2; x < (width / 2 - 24); x++) {
        g.drawLine(
            149 - x,
            height - 30 - (int) (magSpec[x - 1] * 90),
            148 - x,
            height - 30 - (int) (magSpec[x] * 90));
        g.drawLine(
            145 + x,
            height - 30 - (int) (magSpec[x - 1] * 90),
            146 + x,
            height - 30 - (int) (magSpec[x] * 90));
      }

      g.setColor(new Color(210, 245, 40, 120));
      str = "Windowfunction";
      g.drawString(str, 104, 139);
      str = "Transferfunction";
      g.drawString(str, 100, 239);
      applyChanges();
    }
  }
  // =========================================================================================================================
  /** Inner Panel class for Color-Rainbow painting */
  // -------------------------------------------------------------------------------------------------------------------------
  class crPanel extends JPanel {
    // -------------------------------------------------------------------------------------------------------------------------
    Sonogram sono;
    // -------------------------------------------------------------------------------------------------------------------------
    public crPanel(Sonogram s) {
      sono = s;
    }
    // -------------------------------------------------------------------------------------------------------------------------
    public void paintComponent(Graphics g) {
      int height = getSize().height;
      int width = getSize().width;
      Color[] colarray = new Color[1];
      g.setColor(new Color(30, 40, 60));
      g.drawRect(4, 5, width - 10, height - 8);

      if (sono.firecItem.isSelected() == true && sono.negItem.isSelected() == false)
        colarray = sono.pp.cocFI;
      if (sono.firecItem.isSelected() == true && sono.negItem.isSelected() == true)
        colarray = sono.pp.cocFIi;

      if (sono.fireItem.isSelected() == true && sono.negItem.isSelected() == false)
        colarray = sono.pp.coFI;
      if (sono.fireItem.isSelected() == true && sono.negItem.isSelected() == true)
        colarray = sono.pp.coFIi;

      if (sono.colItem.isSelected() == true && sono.negItem.isSelected() == false)
        colarray = sono.pp.coCO;
      if (sono.colItem.isSelected() == true && sono.negItem.isSelected() == true)
        colarray = sono.pp.coCOi;

      if (sono.bwItem.isSelected() == true && sono.negItem.isSelected() == false)
        colarray = sono.pp.coSW;
      if (sono.bwItem.isSelected() == true && sono.negItem.isSelected() == true)
        colarray = sono.pp.coSWi;

      if (sono.rainItem.isSelected() == true && sono.negItem.isSelected() == false)
        colarray = sono.pp.coRA;
      if (sono.rainItem.isSelected() == true && sono.negItem.isSelected() == true)
        colarray = sono.pp.coRAi;

      if (sono.greenItem.isSelected() == true && sono.negItem.isSelected() == false)
        colarray = sono.pp.coCG;
      if (sono.greenItem.isSelected() == true && sono.negItem.isSelected() == true)
        colarray = sono.pp.coCGi;

      double colfact = (255.0 / (width - 11.0));
      for (int x = 0; x < width - 11; x++) {
        g.setColor(colarray[(int) ((double) x * colfact)]);
        g.drawLine(x + 5, 6, x + 5, height - 4);
      }
    }
  }
  // =========================================================================================================================
  /** Inner Panel class to paint Wavelet selection */
  // -------------------------------------------------------------------------------------------------------------------------
  class waPanel extends JPanel {
    // -------------------------------------------------------------------------------------------------------------------------
    public void paintComponent(Graphics g1) {
      /** this inner class paints the selected wavelet into the tabbed pane */
      int height = getSize().height;
      int width = getSize().width;

      // grid & rect
      Graphics2D g = (Graphics2D) g1;
      if (cantialise.isSelected() == true)
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g.setColor(new Color(25, 45, 15));
      g.fillRect(10, 25, width - 20, height - 35);
      g.setColor(new Color(50, 67, 40));
      for (int x = 15; x < width - 11; x += 4) g.drawLine(x, 25, x, height - 11);
      for (int y = 25; y < height - 10; y += 4) g.drawLine(10, y, width - 11, y);
      g.setColor(new Color(80, 100, 70));
      g.drawLine(10, height / 2 + 9, width - 11, height / 2 + 9);
      g.drawLine(15, height / 2 + 16, 15, height / 2 + 2);
      g.drawLine(width - 17, height / 2 + 16, width - 17, height / 2 + 2);

      // Build the Wavelet with inverse discrete wavelet transformation
      int size = 4000;
      float[] eingang = new float[size];
      float[] ausgang = new float[size];
      eingang[34] = 1.0f;
      DWT dwt = new DWT(size, wavelets.getSet(wcb.getSelectedIndex()));
      dwt.inverseDiscreteWT(eingang, ausgang);

      // search for peaks
      int under = 0;
      int over = size;
      float minpeak = 1000.0f;
      float maxpeak = -1000.0f;
      float peak;
      for (int count = 0; count < size; count++) {
        if (minpeak > ausgang[count]) minpeak = ausgang[count];
        if (maxpeak < ausgang[count]) maxpeak = ausgang[count];
      }
      peak = maxpeak;
      if ((minpeak + maxpeak) < 0) peak = -minpeak;
      // search Begin and End points
      float searchamplitude = 0.001f;
      for (int count = 0; count < size; count++) {
        if (Math.abs(ausgang[count]) > searchamplitude) {
          under = count;
          break;
        }
      }
      for (int count = size - 1; count > -1; count--) {
        if (Math.abs(ausgang[count]) > searchamplitude) {
          over = count;
          break;
        }
      }
      under -= 5;
      over += 5;

      // normalize and cut the wavelet into the display vector
      int middle = 9 + height / 2; // middle of the screen
      int[] thewavelet = new int[250];
      float span = over - under + 1;
      float step = (float) span / 250.0f;
      int counter = 0;
      for (float cut = under; cut < over; cut += step) {
        thewavelet[249 - counter] =
            (int) (-1.0f * ausgang[(int) cut] / peak * height / 2 * 0.7f + middle);
        counter++;
      }
      thewavelet[0] = thewavelet[249] = middle;
      if (wcb.getSelectedIndex() == 7) // specialy for haar wavelet
      for (int count = 1; count < 10; count++) thewavelet[count] = middle;

      // draw the Wavelet
      g.setColor(new Color(190, 255, 60));
      int x1, x2;
      g.setStroke(new BasicStroke(3));
      g.setColor(new Color(50, 100, 40));
      for (int count = 1; count < 250; count++) {
        x1 = (int) ((float) (count - 1) / 250.0f * ((float) (width - 30))) + 15;
        x2 = (int) ((float) (count) / 250.0f * ((float) (width - 30))) + 15;
        g.drawLine(x1, thewavelet[count - 1], x2, thewavelet[count]);
      }
      g.setStroke(new BasicStroke(1));
      g.setColor(new Color(220, 255, 40));
      for (int count = 1; count < 250; count++) {
        x1 = (int) ((float) (count - 1) / 250.0f * ((float) (width - 30))) + 15;
        x2 = (int) ((float) (count) / 250.0f * ((float) (width - 30))) + 15;
        g.drawLine(x1, thewavelet[count - 1], x2, thewavelet[count]);
      }

      // paint text
      g.setColor(new Color(150, 0, 0));
      String name = wavelets.getSet(wcb.getSelectedIndex()).getName();
      g.drawString(name, 20, 40);
    }
  }
  // =========================================================================================================================
}
// -------------------------------------------------------------------------------------------------------------------------
