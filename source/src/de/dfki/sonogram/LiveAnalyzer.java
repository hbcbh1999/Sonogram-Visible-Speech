/** author: Christoph Lauer */
package de.dfki.sonogram;

import de.dfki.maths.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.*;

// ----------------------------------------------------------------------------------------------

public class LiveAnalyzer extends JFrame implements MouseListener {
  boolean stopCapture = false;
  ByteArrayOutputStream byteArrayOutputStream;
  AudioFormat audioFormat;
  TargetDataLine targetDataLine;
  AudioInputStream audioInputStream;
  SourceDataLine sourceDataLine;
  Sonogram sono;
  EFileChooser chooser = new EFileChooser();
  Spectrum spec = new Spectrum();
  int lineCounter = 0;
  int samplesAccumulator = 0;
  int seconds = 0;
  BufferedImage image = new BufferedImage(1070, 256, BufferedImage.TYPE_INT_RGB);
  float[] fd = null;
  int sr = 48000;
  TimerPanel tp = new TimerPanel();
  SpectrumPanel sp = new SpectrumPanel();
  private Color initialColor = null;
  private Color initialColor2 = null;
  final JButton captBtn = new JButton("Record");
  final JButton stopBtn = new JButton("Stop");
  final JButton playBtn = new JButton("Play");
  final JButton saveBtn = new JButton("Save");
  final JButton closBtn = new JButton("Close");

  // ----------------------------------------------------------------------------------------------

  public void mouseClicked(MouseEvent inE) {}

  public void mousePressed(MouseEvent inE) {
    UIManager.put("Button.select", new Color(30, 30, 30));
    UIManager.put("Button.focus", new Color(0, 0, 0));
    captBtn.updateUI();
    stopBtn.updateUI();
    playBtn.updateUI();
    saveBtn.updateUI();
    closBtn.updateUI();
  }

  public void mouseReleased(MouseEvent inE) {
    UIManager.put("Button.select", initialColor);
    UIManager.put("Button.focus", initialColor2);
    captBtn.updateUI();
    stopBtn.updateUI();
    playBtn.updateUI();
    saveBtn.updateUI();
    closBtn.updateUI();
  }

  public void mouseEntered(MouseEvent inE) {}

  public void mouseExited(MouseEvent inE) {}

  // ----------------------------------------------------------------------------------------------

  public LiveAnalyzer(Sonogram ref) {
    // initial values
    sono = ref;
    spec.setPreferredSize(new Dimension(1102, 302));

    // the button colors

    // init the file chooser
    chooser.setApproveButtonText("Save");
    chooser.setDialogTitle("Select Wave File");
    chooser.setSelectedFile(new File("Recording.wav"));
    MediaFileFilter ffwav = new MediaFileFilter("wav", "Wave Files (*.wav)");
    chooser.addChoosableFileFilter(ffwav);

    initialColor = UIManager.getColor("Button.select");
    initialColor2 = UIManager.getColor("Button.focus");
    Font buttonFont = new Font("Arial", 1, 22);
    UIManager.put("Button.disabledText", Color.DARK_GRAY);

    captBtn.setForeground(Color.WHITE);
    captBtn.setBackground(Color.BLACK);
    captBtn.setBorder(new LineBorder(new Color(30, 30, 30)));
    captBtn.setFont(buttonFont);
    captBtn.addMouseListener(this);
    captBtn.setFocusable(false);

    stopBtn.setForeground(Color.WHITE);
    stopBtn.setBackground(Color.BLACK);
    stopBtn.setBorder(new LineBorder(new Color(30, 30, 30)));
    stopBtn.setFont(buttonFont);
    stopBtn.addMouseListener(this);
    stopBtn.setFocusable(false);

    playBtn.setForeground(Color.WHITE);
    playBtn.setBackground(Color.BLACK);
    playBtn.setBorder(new LineBorder(new Color(30, 30, 30)));
    playBtn.setFont(buttonFont);
    playBtn.addMouseListener(this);
    playBtn.setFocusable(false);

    saveBtn.setForeground(Color.WHITE);
    saveBtn.setBackground(Color.BLACK);
    saveBtn.setBorder(new LineBorder(new Color(30, 30, 30)));
    saveBtn.setFont(buttonFont);
    saveBtn.addMouseListener(this);
    saveBtn.setFocusable(false);

    closBtn.setForeground(Color.WHITE);
    closBtn.setBackground(Color.BLACK);
    closBtn.setBorder(new LineBorder(new Color(30, 30, 30)));
    closBtn.setFont(buttonFont);
    closBtn.addMouseListener(this);
    closBtn.setFocusable(false);

    playBtn.setEnabled(false);
    stopBtn.setEnabled(false);
    saveBtn.setEnabled(false);
    captBtn.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            captBtn.setEnabled(false);
            stopBtn.setEnabled(true);
            playBtn.setEnabled(false);
            saveBtn.setEnabled(false);
            tp.startTimer();
            captureAudio();
          }
        });
    stopBtn.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            captBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            playBtn.setEnabled(true);
            saveBtn.setEnabled(true);
            tp.stopTimer();
            sp.spectrum = null;
            stopCapture = true;
          }
        });
    playBtn.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            playAudio();
          }
        });
    saveBtn.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            saveWave();
          }
        });
    closBtn.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            captBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            playBtn.setEnabled(true);
            saveBtn.setEnabled(true);
            tp.stopTimer();
            stopCapture = true;
            sp.spectrum = null;
            setVisible(false);
          }
        });

    java.awt.event.WindowListener listener =
        new java.awt.event.WindowAdapter() {
          public void windowClosing(WindowEvent w) {
            captBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            playBtn.setEnabled(true);
            saveBtn.setEnabled(true);
            tp.stopTimer();
            stopCapture = true;
            sp.spectrum = null;
            setVisible(false);
          }
        };
    addWindowListener(listener);

    JPanel bp = new JPanel();
    bp.setBackground(new Color(30, 30, 30));
    bp.setLayout(new GridLayout(1, 5, 1, 1));
    bp.add(captBtn);
    bp.add(stopBtn);
    bp.add(playBtn);
    bp.add(saveBtn);
    bp.add(closBtn);

    bp.setBorder(null);
    captBtn.setMargin(new Insets(0, 0, 0, 0));
    stopBtn.setMargin(new Insets(0, 0, 0, 0));
    playBtn.setMargin(new Insets(0, 0, 0, 0));
    saveBtn.setMargin(new Insets(0, 0, 0, 0));
    closBtn.setMargin(new Insets(0, 0, 0, 0));
    JPanel cp = new JPanel();
    cp.setLayout(new GridLayout(1, 2));
    cp.add(tp);
    cp.add(sp);

    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(spec, "North");
    getContentPane().add(bp, "South");
    getContentPane().add(cp, "Center");
    setTitle("Audio Recorder");
    setUndecorated(true);
    getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
    pack();
    setResizable(false);
    setLocationRelativeTo(null);
  }

  // ----------------------------------------------------------------------------------------------

  private AudioFormat getAudioFormat() {
    float sampleRate = (float) 48000;
    int sampleSizeInBits = 8;
    int channels = 1;
    boolean signed = true;
    boolean bigEndian = true;
    return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
  }

  // ----------------------------------------------------------------------------------------------

  private void saveWave() {
    try {
      // open the file cooser
      int returnVal = 0;
      do {
        returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.CANCEL_OPTION) return;
      } while (returnVal != JFileChooser.APPROVE_OPTION);

      File f = chooser.getSelectedFile();
      byte audioData[] = byteArrayOutputStream.toByteArray();
      InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
      AudioFormat audioFormat = getAudioFormat();
      audioInputStream =
          new AudioInputStream(
              byteArrayInputStream, audioFormat, audioData.length / audioFormat.getFrameSize());
      AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, f);
      sono.openFile(chooser.getSelectedFile().getAbsolutePath());
      setVisible(false);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(
          null, "Error while save the Wave File !", "ERROR", JOptionPane.ERROR_MESSAGE);
    }
  }

  // ----------------------------------------------------------------------------------------------

  private void captureAudio() {
    try {
      audioFormat = getAudioFormat();
      DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
      if (targetDataLine == null) {
        targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
        targetDataLine.open(audioFormat);
      }
      targetDataLine.start();
      Thread captureThread = new Thread(new CaptureThread());
      captureThread.start();
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(
          null,
          "<html><font size=4>Error while Recording !<br/><div color=880000>Is the sound input"
              + " <u>device connected</u> and have<br>you selected the correct device in the"
              + " <u>mixer</u>?",
          "ERROR",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  // ----------------------------------------------------------------------------------------------

  private void playAudio() {
    try {
      byte audioData[] = byteArrayOutputStream.toByteArray();
      InputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
      AudioFormat audioFormat = getAudioFormat();
      audioInputStream =
          new AudioInputStream(
              byteArrayInputStream, audioFormat, audioData.length / audioFormat.getFrameSize());
      DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);
      sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
      sourceDataLine.open(audioFormat);
      sourceDataLine.start();
      Thread playThread = new Thread(new PlayThread());
      playThread.start();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(
          null, "Error while Playback !", "ERROR", JOptionPane.ERROR_MESSAGE);
    }
  }

  // ----------------------------------------------------------------------------------------------

  class CaptureThread extends Thread {
    byte tempBuffer[] = new byte[512];

    public void run() {
      byteArrayOutputStream = new ByteArrayOutputStream();
      stopCapture = false;

      // delete the image
      Graphics gi = (Graphics) image.getGraphics();
      sono.pp.selectColor(gi, 0);
      gi.fillRect(0, 0, 1070, 256);

      // get the window function
      float window[] = WindowFunktion.harrisWindow(512);

      try {
        while (!stopCapture) {
          // record one buffer
          int cnt = targetDataLine.read(tempBuffer, 0, tempBuffer.length);
          if (cnt > 0) byteArrayOutputStream.write(tempBuffer, 0, cnt);

          // apply the window function
          float[] td = new float[512];
          for (int i = 0; i < 512; i++) td[i] = ((Byte) tempBuffer[i]).floatValue();
          for (int i = 0; i < 512; i++) td[i] = td[i] * window[i];

          // calculate the Spectrum
          FastFourierTransform ft = new FastFourierTransform();
          fd = ft.doFFT(td);

          // remove the influence of the window function
          fd[0] = 0.0f;
          fd[1] = 0.0f;
          fd[2] = 0.0f;
          fd[3] = 0.0f;

          // smooth the spectrum
          for (int i = 2; i < 254; i++)
            fd[i] = (fd[i - 2] + fd[i - 1] + fd[i] + fd[i + 1] + fd[i + 2]) / 5.0f;
          // normalize the spectrum
          float min = Float.MAX_VALUE;
          float max = -Float.MAX_VALUE;
          int maxI = 0;
          for (int i = 0; i < 256; i++) {
            fd[i] = (float) Math.log1p(fd[i]);
            if (max < fd[i]) {
              max = fd[i];
              maxI = i;
            }
            if (min > fd[i]) min = fd[i];
          }
          if (max < 0.1f) max = 0.1f;
          for (int i = 0; i < 256; i++) fd[i] = (fd[i] - min) / (max - min) * 255;
          sp.spectrum = fd;
          sp.maxI = maxI;

          // set the VU meter level
          // System.out.println("MAX: " + max + "MIN: " + min);
          tp.level = (int) (max * 5) - 1;

          // paint the spectrum
          Graphics2D g2 = (Graphics2D) gi;
          if (sono.gad.cantialise.isSelected() == true)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          g2.setColor(new Color(255, 255, 255));
          for (int i = 0; i < 256; i++) {
            sono.pp.selectColor(gi, (int) fd[i]);
            // paint one of the the spec points
            gi.drawLine(lineCounter, 255 - i, lineCounter, 255 - i);
            if (i % 64 == 0 && i != 0) {
              g2.setColor(new Color(255, 255, 255));
              g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
              g2.drawLine(lineCounter, 255 - i, lineCounter, 255 - i);
              g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            }
          }
          // delete the old line before
          sono.pp.selectColor(gi, 0);
          gi.drawLine(lineCounter + 1, 0, lineCounter + 1, 255);
          samplesAccumulator += 512;
          if (lineCounter == 55) {
            g2.setColor(new Color(255, 255, 255));
            g2.setFont(new Font("Dialog", 0, 8));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g2.drawString("24.0 kHz", 1, 7);
            g2.drawString("16.0 kHz", 1, 71);
            g2.drawString("12.0 kHz", 1, 135);
            g2.drawString(" 8.0 kHz", 1, 199);
          }

          // the second line
          if (samplesAccumulator > sr) {
            g2.setColor(new Color(255, 255, 255));
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
            g2.drawLine(lineCounter, 0, lineCounter, 255);
            g2.setFont(new Font("Dialog", 0, 9));
            FontMetrics metrics = g2.getFontMetrics();
            String secStr = Integer.toString(seconds);
            int textWidth = metrics.stringWidth(secStr);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f));
            g2.drawString(secStr, lineCounter - textWidth, 8);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
            samplesAccumulator = 0;
            seconds++;
          }

          lineCounter++;
          if (lineCounter == 1070) lineCounter = 0;
          spec.repaint();
        }

        // recording finished
        lineCounter = 0;
        samplesAccumulator = 0;
        seconds = 0;
        sp.spectrum = null;

        byteArrayOutputStream.close();
        targetDataLine.stop();
      } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(
            null, "Error while Recording !", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  // ----------------------------------------------------------------------------------------------

  class PlayThread extends Thread {
    byte tempBuffer[] = new byte[10000];

    public void run() {
      try {
        int cnt;
        while ((cnt = audioInputStream.read(tempBuffer, 0, tempBuffer.length)) != -1) {
          if (cnt > 0) sourceDataLine.write(tempBuffer, 0, cnt);
        }
        sourceDataLine.drain();
        sourceDataLine.close();
      } catch (Exception e) {
        JOptionPane.showMessageDialog(
            null, "Error while Playback !", "ERROR", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  // ----------------------------------------------------------------------------------------------

  class Spectrum extends JPanel {

    private final java.awt.image.BufferedImage BACKGROUND_IMAGE = createBackground(1104, 302);

    // ---------------------------------------------------------------------------------------------

    public void Spectrum() {}

    // ---------------------------------------------------------------------------------------------

    public void paintComponent(Graphics g) {

      g.drawImage(BACKGROUND_IMAGE, 0, 0, this);
      sono.pp.selectColor(g, 0);
      g.fillRect(17, 43, 1070, 256);
      if (fd != null) g.drawImage(image, 17, 43, this);
    }

    // ----------------------------------------------------------------------------------------------

    public java.awt.image.BufferedImage createBackground(int width, int height) {
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

      g2.setColor(new java.awt.Color(10, 0, 20 /*0x253048*/));
      g2.fillRect(0, 0, width, height);

      final float[] FRACTIONS = {0.0f, 1.0f};
      final java.awt.Color[] COLORS_HIGHLIGHT = {
        new java.awt.Color(0x000000), new java.awt.Color(0x000000), new java.awt.Color(0x6C8095)
      };
      final java.awt.geom.Point2D START_HIGHLIGHT = new java.awt.geom.Point2D.Float(0, 250);
      final java.awt.geom.Point2D STOP_HIGHLIGHT = new java.awt.geom.Point2D.Float(0, 297);
      final float[] FRACTIONS_HIGHLIGHT = {0.0f, 0.88f, 1.0f};
      final java.awt.LinearGradientPaint GRADIENT_HIGHLIGHT =
          new java.awt.LinearGradientPaint(
              START_HIGHLIGHT, STOP_HIGHLIGHT, FRACTIONS_HIGHLIGHT, COLORS_HIGHLIGHT);
      g2.setPaint(GRADIENT_HIGHLIGHT);
      g2.fillRect(16, 72, 1072, 228);

      // the screws
      try {
        g2.drawImage(
            javax.imageio.ImageIO.read(Sonogram.class.getResource("Schraube1.png")), 2, 2, null);
      } catch (Throwable t) {
      }

      // the screws
      try {
        g2.drawImage(
            javax.imageio.ImageIO.read(Sonogram.class.getResource("Schraube1.png")), 1090, 2, null);
      } catch (Throwable t) {
      }

      final java.awt.BasicStroke STROKE =
          new java.awt.BasicStroke(
              4.0f, java.awt.BasicStroke.CAP_SQUARE, java.awt.BasicStroke.JOIN_BEVEL);
      g2.setStroke(STROKE);
      g2.setColor(new java.awt.Color(0xDDF3F4));
      g2.drawLine(19, 23, 19, 29);
      g2.drawLine(19, 23, 478, 23);
      g2.drawLine(623, 23, 1085, 23);
      g2.drawLine(1085, 23, 1085, 29);
      g2.setFont(new java.awt.Font("Arial", 1, 22));
      g2.drawString("SONOGRAM", 485, 30);

      return IMAGE;
    }
  }

  // ----------------------------------------------------------------------------------------------

}
