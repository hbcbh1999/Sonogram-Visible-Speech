package de.dfki.sonogram;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>This Class is the exporting dialog which converts the spektrum to the SVG-XML format. Here are
 * all SVG write commands implemented.
 *
 * @author Christoph Lauer
 * @version 1.0, Begin Current 26/09/2002
 */
class ExportSpectrumSVG extends JFrame {
  Sonogram reftomain;
  int specwidth;
  int specheight;
  File file;
  JTextField fp;
  JRadioButton r1, r2, r3, r4, r5;
  JCheckBox cinv, cgrid, cpure;
  Color colors[] = new Color[256];
  JSlider slidersize;

  public ExportSpectrumSVG(Sonogram s) {
    setTitle("Export Spectrum to SVG");
    setSize(332, 290);
    reftomain = s;

    Toolkit tk = Toolkit.getDefaultToolkit();
    setIconImage(tk.getImage(Sonogram.class.getResource("Sonogram.gif")));

    JPanel p = new JPanel();
    p.setBorder(new TitledBorder(new EtchedBorder(), "Save as Scalable Vevtor Grapgic"));
    p.setLayout(null);

    JPanel ps = new JPanel();
    ps.setLayout(new GridLayout(2, 1));
    ps.setBorder(new TitledBorder(new EtchedBorder(), "Select File"));
    ps.setSize(310, 75);
    ps.setLocation(7, 15);

    fp = new JTextField("Select SVG file.", 30);
    fp.setEditable(false);
    ps.add(fp);
    JButton btsf = new JButton("Select SVG File");
    ActionListener slst =
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            EFileChooser chooser = new EFileChooser();
            chooser.setApproveButtonText("Save");
            chooser.setDialogTitle("Select SVG file for writting.");
            chooser.setSelectedFile(new File("Sonogram.svg"));
            MediaFileFilter ffsvg = new MediaFileFilter("svg", "Scalable Vector Graphics (*.svg)");
            chooser.addChoosableFileFilter(ffsvg);
            int returnVal = 0;
            do {
              returnVal = chooser.showOpenDialog(reftomain);
              if (returnVal == JFileChooser.CANCEL_OPTION) return;
            } while (returnVal != JFileChooser.APPROVE_OPTION);
            file = chooser.getSelectedFile();
            System.out.println("--> selected SVG file: " + file);
            fp.setText(file.getAbsolutePath());
          }
        };
    btsf.addActionListener(slst);
    btsf.setToolTipText("Specify location of SVG file.");
    ps.add(btsf);
    p.add(ps);

    JPanel pc = new JPanel();
    pc.setSize(310, 75);
    pc.setLocation(7, 85);
    pc.setBorder(new TitledBorder(new EtchedBorder(), "Options for Export"));
    pc.setLayout(new GridLayout(3, 3));
    ButtonGroup group = new ButtonGroup();
    r1 = new JRadioButton("Fire", true);
    r1.setToolTipText("Export SVG image with fire Colors");
    group.add(r1);
    pc.add(r1);
    r2 = new JRadioButton("Rainbow");
    r2.setToolTipText("Export SVG image with rainbow Colors");
    group.add(r2);
    pc.add(r2);
    r3 = new JRadioButton("B/W");
    r1.setToolTipText("Export SVG image with black/white Colors");
    group.add(r3);
    pc.add(r3);
    r4 = new JRadioButton("Nice");
    r4.setToolTipText("Export SVG image with nice Colors");
    group.add(r4);
    pc.add(r4);
    r5 = new JRadioButton("Green");
    r5.setToolTipText("Export SVG image with classical green colors");
    group.add(r5);
    pc.add(r5);
    cinv = new JCheckBox("Inverse");
    cinv.setToolTipText("Inverse colors shown in sonogram");
    pc.add(cinv);
    cgrid = new JCheckBox("Grid");
    cgrid.setToolTipText("Plot graphics with grid ?");
    pc.add(cgrid);
    p.add(pc);
    cpure = new JCheckBox("Pure SVG");
    cpure.setToolTipText(
        "<html><b>Export Pure SVG</b><br>The image will be saved in th RAW-SVG<br>format so it can"
            + " be opened with other<br>programs. If the imags is saved as RAW-SVG<br>it can noten"
            + " opened again with Sonogram !");
    pc.add(cpure);
    p.add(pc);

    slidersize = new JSlider(JSlider.HORIZONTAL, 0, 30, 10);
    slidersize.setBorder(new TitledBorder(new EtchedBorder(), "Graphic Size"));
    slidersize.setToolTipText(
        "<html><b>Graphic Size</b><br>This setting influences the scale factor<br>of the quads for"
            + " the expoted SVG image.");
    slidersize.setPaintLabels(true);
    slidersize.setSnapToTicks(true);
    slidersize.setPaintTicks(true);
    slidersize.setMajorTickSpacing(5);
    slidersize.setSize(310, 70);
    slidersize.setLocation(7, 155);
    p.add(slidersize);

    JButton btok = new JButton("Export");
    JButton btcl = new JButton("Cancel");

    ActionListener olst =
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            export();
          }
        };
    ActionListener clst =
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            setVisible(false);
          }
        };
    btok.addActionListener(olst);
    btok.setToolTipText("Begin exporting of SVG file.");
    btok.setSize(100, 23);
    btok.setLocation(173, 227);

    btcl.addActionListener(clst);
    btcl.setToolTipText("Cancel exporting.");
    btcl.setSize(100, 23);
    btcl.setLocation(48, 227);
    p.add(btok);
    p.add(btcl);

    getContentPane().add(p);
  }
  // ----------------------------------------------------------------------------
  public boolean export() {
    float[] tempSpektrum;
    int amplitude;
    Color color;
    int r, g, b;
    int width = reftomain.spektrum.size();
    int heigth = reftomain.timewindowlength / 2;
    int scale = slidersize.getValue();
    if (scale == 0) scale = 1;

    if (file == null) {
      reftomain.messageBox("No file", "Please select file first", 1);
      return (false);
    }
    if (width * heigth > 30000) {
      double sel = reftomain.spektrum.size() * reftomain.timewindowlength / 2;
      double mes = Math.round(Math.round((double) sel * 83.175 / 1024.0) / 1024.0 * 100.0) / 100.0;

      int confirm =
          JOptionPane.showOptionDialog(
              this,
              "<html>This plot is very large and your <u>SVG viewer can have problems</u><br>"
                  + "reding this SVG File. The Spectrum has <u>"
                  + width * heigth
                  + "</u> Elements. You<br>"
                  + "can reduce the Overlapping, zoom more in or restrict the<br>"
                  + "Samplerate to 8KHz. The estimated Filesize is: <u><font color=#AA0000>"
                  + mes
                  + "MB</u></font><br>"
                  + "<br><font size = 4>Continue the generation of this SVG File ?",
              "Very large plot",
              JOptionPane.YES_NO_OPTION,
              JOptionPane.QUESTION_MESSAGE,
              null,
              null,
              null);
      if (confirm != 0) {
        return (false);
      }
    }

    generateColors();
    try {
      PrintWriter out = new PrintWriter(new FileWriter(file));
      out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      out.println("<!-- SVG file written by Sonogram-->");
      if (cpure.isSelected() == true) {
        out.println("<!-- SVG file written as Pure svg file-->");
        out.println("<!-- With pure SVG no reopen is possible-->");
      }
      out.println("<!-- Sonogram is a spectralanalysator relized with Java.-->");
      out.println("<!-- See www.dfki.de/~clauer/sonogram for bmore details.-->");
      out.println(
          "<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 20010719//EN\""
              + " \"http://www.w3.org/TR/2001/PR-SVG-20010719/DTD/svg10.dtd\">");
      out.println("\t<svg width=\"" + width * scale + "\" height=\"" + heigth * scale + "\">");
      // Draw spektrum
      for (int x = 0; x < width; x++) {
        tempSpektrum = (float[]) reftomain.spektrum.get(x);
        for (int y = 0; y < heigth; y++) {
          amplitude = (int) tempSpektrum[(reftomain.timewindowlength / 2 - 1) - y];
          color = colors[amplitude];
          r = color.getRed();
          g = color.getGreen();
          b = color.getBlue();
          if (cpure.isSelected() == true)
            out.println(
                "\t\t<rect x=\""
                    + x * scale
                    + "\" y=\""
                    + y * scale
                    + "\" width=\""
                    + scale
                    + "\" height=\""
                    + scale
                    + "\" fill=\"rgb("
                    + r
                    + ","
                    + g
                    + ","
                    + b
                    + ")\"/>");
          else
            out.println(
                "\t\t<rect x=\""
                    + x * scale
                    + "\" y=\""
                    + y * scale
                    + "\" width=\""
                    + scale
                    + "\" height=\""
                    + scale
                    + "\" fill=\"rgb("
                    + r
                    + ","
                    + g
                    + ","
                    + b
                    + ")\" amplitude=\""
                    + amplitude
                    + "\"/>");
        }
      }
      if (cgrid.isSelected()) {
        // grid over time
        for (double x = 0;
            x < width * scale;
            x +=
                ((double) width * scale)
                    / ((double) reftomain.samplestotal / (double) reftomain.samplerate))
          out.println(
              "\t\t<line x1=\""
                  + (int) x
                  + "\" y1=\""
                  + 0
                  + "\" x2=\""
                  + (int) x
                  + "\" y2=\""
                  + heigth * scale
                  + "\" stroke=\"white\"/>");
        // grid over frequencies
        for (double y = 0; y < heigth * scale; y += ((double) heigth * scale) / 10.0)
          out.println(
              "\t\t<line x1=\"0\" y1=\""
                  + y
                  + "\" x2=\""
                  + width * scale
                  + "\" y2=\""
                  + y
                  + "\" stroke=\"white\"/>");
      }
      out.println("\t</svg>");
      // Some Stuff onely when pure is Selected
      if (cpure.isSelected() == false) {
        out.println("\t<SpectrumSettingsForGeneration/>");
        out.println("\t\t<FilePath=\"" + reftomain.filepath + "\"/>");
        out.println("\t\t<TimewindowLength=\"" + reftomain.timewindowlength + "\"/>");
        out.println("\t\t<WindowNumbers=\"" + reftomain.spektrum.size() + "\"/>");
        out.println("\t\t<WindowNumberAuto=\"" + reftomain.gad.cauto.isSelected() + "\"/>");
        String wf = "Error";
        if (reftomain.hamItem.isSelected() == true) wf = "Hamming";
        if (reftomain.hanItem.isSelected() == true) wf = "Hanning";
        if (reftomain.blaItem.isSelected() == true) wf = "Blackman";
        if (reftomain.rectItem.isSelected() == true) wf = "Rectagle";
        if (reftomain.triItem.isSelected() == true) wf = "Triangle";
        if (reftomain.gauItem.isSelected() == true) wf = "Gauss";
        if (reftomain.welItem.isSelected() == true) wf = "Welch";
        out.println("\t\t<WindowFunktion=\"" + wf + "\"/>");
        out.println("\t\t<SmoothFrequenz=\"" + reftomain.gad.csmooth.isSelected() + "\"/>");
        out.println("\t\t<SmoothTime=\"" + reftomain.gad.csmoothx.isSelected() + "\"/>");
        out.println("\t\t<OverLapping=\"" + reftomain.gad.sliderwinspeed.getValue() + "\"/>");
        out.println("\t\t<OverLappingAuto=\"" + reftomain.gad.coverlapping.isSelected() + "\"/>");
        out.println("\t\t<LogarithmEnabled=\"" + reftomain.gad.clog.isSelected() + "\"/>");
        out.println("\t\t<LogarithmScale=\"" + reftomain.gad.sliderlog.getValue() + "\"/>");
        out.println("\t\t<Samplerate8Khz=\"" + reftomain.gad.csampl.isSelected() + "\"/>");
        String tr = "FFT";
        if (reftomain.gad.rfft.isSelected() == false) tr = "LPC";
        out.println("\t\t<Transformation=\"" + tr + "\"/>");
        out.println("\t\t<LPCCoeficients=\"" + reftomain.gad.sliderlpccoef.getValue() + "\"/>");
        out.println("\t\t<LPCFFTPoints=\"" + reftomain.gad.sliderlpcfftnum.getValue() + "\"/>");
        out.println("\t\t<PaintScaleFact=\"" + scale + "\"/>");
        out.println("\t\t<SampleRate=\"" + reftomain.samplerate + "\"/>");
        out.println("\t\t<SamplesTotal=\"" + reftomain.samplestotal + "\"/>");
        out.println("\t<SpectrumSettingsForGeneration/>");
      }

      out.close();
      reftomain.messageBox("Export sucessful", "SVG file written;\n" + file.getAbsolutePath(), 1);
      setVisible(false);
    } catch (Throwable thro) {
      reftomain.messageBox(
          "Error while writing SVG file",
          "Can not write:" + file.getAbsolutePath() + "\nPerhaps not enough free Disk space ?",
          0);
      System.out.println("--> Error while writing:" + thro);
      return (false);
    }
    return (true);
  }

  // ----------------------------------------------------------------------------
  private void generateColors() {
    for (int i = 0; i < 256; i++) {
      if (r1.isSelected() == true && cinv.isSelected() == false) colors[i] = reftomain.pp.coFI[i];
      if (r1.isSelected() == true && cinv.isSelected() == true) colors[i] = reftomain.pp.coFIi[i];
      if (r2.isSelected() == true && cinv.isSelected() == false) colors[i] = reftomain.pp.coRA[i];
      if (r2.isSelected() == true && cinv.isSelected() == true) colors[i] = reftomain.pp.coRAi[i];
      if (r3.isSelected() == true && cinv.isSelected() == false) colors[i] = reftomain.pp.coSW[i];
      if (r3.isSelected() == true && cinv.isSelected() == true) colors[i] = reftomain.pp.coSWi[i];
      if (r4.isSelected() == true && cinv.isSelected() == false) colors[i] = reftomain.pp.coCO[i];
      if (r4.isSelected() == true && cinv.isSelected() == true) colors[i] = reftomain.pp.coCOi[i];
      if (r5.isSelected() == true && cinv.isSelected() == false) colors[i] = reftomain.pp.coCG[i];
      if (r5.isSelected() == true && cinv.isSelected() == true) colors[i] = reftomain.pp.coCGi[i];
    }
  }
  // ----------------------------------------------------------------------------
}
