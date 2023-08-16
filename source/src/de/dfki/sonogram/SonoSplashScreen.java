/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>Displays splash screen in the center of the screen. Just create a SplashScreen object with
 * "new SplashScreen(..)" and it'll do what you want.
 *
 * @author Christoph Lauer
 * @version 1.0, Current 26/09/2002
 */
package de.dfki.sonogram;

import java.awt.*;
import java.awt.color.*;
import java.awt.event.*;
// import com.sun.awt.AWTUtilities;
import java.awt.image.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.*;

public class SonoSplashScreen {
  static boolean javaSplash;
  static int nativeTransparency;
  Graphics2D g;
  JWindow ownSplashWindow;
  OwnPanel ownSplashPanel;
  static ImageIcon ownSplashPic;
  static java.awt.SplashScreen splash;
  static int width;
  static String OS;
  static boolean licensePainted = false;

  // ------------------------------------------------------------------------------------------

  public SonoSplashScreen(boolean demoMode) {
    splash = java.awt.SplashScreen.getSplashScreen();

    if (splash != null) {
      g = (Graphics2D) splash.createGraphics();
      javaSplash = true;
      width = (int) splash.getBounds().getWidth();
      nativeTransparency = 1;
      System.out.println("--> Startup Splash found");
    } else {
      System.out.println(
          "--> ERROR SplashScreen.getSplashScreen() returned null, create own splash screen");
      javaSplash = false;

      // instanciate the needed classes
      ownSplashWindow = new JWindow();
      ownSplashWindow.setBackground(
          new Color(0f, 0f, 0f, 0f)); // removes the background (and the shaddow on macOS)
      ownSplashPic = new ImageIcon(Sonogram.class.getResource("Splash.png"));

      // place the window
      width = ownSplashPic.getIconWidth();
      int height = ownSplashPic.getIconHeight();
      Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
      int x = (screen.width - width) / 2;
      int y;
      OS = System.getProperties().getProperty("os.name");
      if (OS.contains("Mac") == true) y = (int) ((double) (screen.height - height) / 3);
      else y = (int) ((double) (screen.height - height) / 2);
      ownSplashWindow.setBounds(x, y, width, height);
      ownSplashPanel = new OwnPanel(ownSplashWindow);
      ownSplashPanel.setBackground(new Color(0f, 0f, 0f, 0f));

      // grab the bg and place the windows
      ownSplashWindow.getContentPane().add(ownSplashPanel);
      ownSplashWindow.setVisible(true);
      ownSplashWindow.toFront();
      // close the splash screen in demo mode when mouse is pressed
      if (demoMode == true) {
        // add the mouse handler
        MouseAdapter m =
            new MouseAdapter() {
              public void mouseClicked(MouseEvent e) {
                ownSplashWindow.dispose();
              }
            };
        ownSplashWindow.addMouseListener(m);
        // run the progress bar and message thread
        DemoMessageThread demo = new DemoMessageThread();
        demo.start();
      }
    }
  }

  // ------------------------------------------------------------------------------------------

  class DemoMessageThread extends Thread {
    public void run() {
      String text = "Sonogram  \u00A9 by Christoph Lauer                    ";
      int stringLen = text.length();
      int progress = 0;

      for (int i = 0; i < stringLen; i++) {
        // build the text block
        int subStringLen = i % stringLen;
        String subText = text.substring(0, subStringLen);
        if (progress < 10) subText += " (click to close)   " + progress + "%";
        else subText += " (click to close) " + progress + "%";
        setProgress(progress++, subText);
        // show the text on the progress monitor
        try {
          sleep(80);
        } catch (Exception e) {
        }
        ;
      }
      for (int i = stringLen - 2; i >= 0; i--) {
        // build the text block
        int subStringLen = i % stringLen;
        String subText = text.substring(0, subStringLen);
        subText += " (click to close) " + progress + "%";
        setProgress(progress++, subText);
        // show the text on the progress monitor
        try {
          sleep(80);
        } catch (Exception e) {
        }
        ;
      }
      for (int i = 200; i > 0; i--) {
        if (i < 20) setProgress(99, "closing in " + (float) i / 10 + " second (click to close)");
        else setProgress(99, "closing in " + (float) i / 10 + " seconds (click to close)");
        try {
          sleep(100);
        } catch (Exception e) {
        }
        ;
      }
      setProgress(100, "");
    }
  }

  // ------------------------------------------------------------------------------------------

  public void setProgress(int percent, String progressmessage) {
    // try{Thread.sleep(200);} catch(Exception e){}
    // delay the splash screen at the last update
    boolean fini = false;
    if (percent == 100) {
      percent = 99;
      fini = true;
    }
    // in case of the java splash screen
    if (javaSplash == true) {
      // normal update
      renderProgressBar(g, percent, progressmessage);
      if (splash != null) if (splash.isVisible() == true) splash.update();
      // sleep if we reach the end
      if (fini == true)
        try {
          Thread.sleep(250);
        } catch (Exception e) {
        }
    }
    // in case of my own splash screen
    else {
      // if we reach the end
      if (fini == true && ownSplashWindow != null) {
        ownSplashPanel.updateStatus(percent, progressmessage);
        ownSplashPanel.repaint();
        try {
          Thread.sleep(250);
        } catch (Exception e) {
        }
        ownSplashWindow.setVisible(false);
        ownSplashWindow = null;
        ownSplashPanel = null;
        ownSplashPic = null;
        return;
      }
      // for normal updates
      if (ownSplashPanel != null) {
        ownSplashPanel.updateStatus(percent, progressmessage);
        ownSplashPanel.repaint();
      }
    }
  }

  // ------------------------------------------------------------------------------------------

  /** This function renders the colored progress bar with an gradien rainbow color overlay */
  public static void renderProgressBar(Graphics2D g2, int percent, String progressmessage) {
    // get the graphics component
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    if (progressmessage == null) progressmessage = "Sonogram";
    if (g2 == null) return;

    // Init the values
    int offsetX = 22;
    int offsetY = 72;
    int size = 450;
    Color hell = Color.white;
    Color dunkel = Color.black;
    // percent = (int)(Math.pow((double)percent/100.0,0.33)*100.0);

    // Progress Bar
    Color[] colors = {
      new Color(160, 0, 255),
      new Color(30, 30, 255),
      new Color(30, 70, 200),
      Color.GREEN,
      new Color(220, 255, 0),
      new Color(220, 200, 0),
      Color.RED
    };
    float[] dist = {0.0f, 0.25f, 0.35f, 0.52f, 0.68f, 0.78f, 0.92f};
    LinearGradientPaint gp = new LinearGradientPaint(0f, 0f, (float) size, 0f, dist, colors);
    g2.setPaint(gp);
    g2.fillRect(
        1 + offsetX, 197 + offsetY, 1 + (int) ((double) size / 100.0 * (double) percent), 4);

    // Text
    g2.setColor(new Color(10, 5, 20));
    // overpaint the old one with black
    g2.fillRect(116 + offsetX, 203 + offsetY, 330, 12);
    // paint the new text
    g2.setFont(new Font("Tahoma", Font.BOLD, 10));
    FontMetrics metrics = g2.getFontMetrics();
    int textWidth = metrics.stringWidth(progressmessage);
    g2.setPaint(gp);
    g2.drawString(progressmessage, width - textWidth - 36, 212 + offsetY);

    if (licensePainted == false) {
      // License and version/build text
      g2.setColor(new Color(100, 100, 100));
      g2.setFont(new Font("Tahoma", Font.BOLD, 9));
      metrics = g2.getFontMetrics();
      // build the strings
      String versionString = "Version: " + Sonogram.version;
      String buildString = "Build: " + Sonogram.build;
      String licenString = "Licensed to:";
      if (Licenses.zoda1.equals("HIRN1HIRN1HIRN1HIRN1HIRN1HIRN1HI") == true)
        licenString = /*Integer.toString(Sonogram.isetu) + " Minute*/ "Trial Version";
      String nameString = Licenses.zoda1.replaceAll("\\s+$", "");
      // draw the text
      textWidth = metrics.stringWidth(versionString);
      g2.drawString(versionString, width - textWidth - 61, offsetY - 30);
      textWidth = metrics.stringWidth(buildString);
      g2.drawString(buildString, width - textWidth - 61, offsetY - 22);
      textWidth = metrics.stringWidth(licenString);
      g2.drawString(licenString, width - textWidth - 61, offsetY - 14);
      if (Licenses.zoda1.equals("HIRN1HIRN1HIRN1HIRN1HIRN1HIRN1HI") == false) {
        g2.setFont(new Font("Tahoma", Font.BOLD, 12));
        metrics = g2.getFontMetrics();
        textWidth = metrics.stringWidth(nameString);
        g2.drawString(nameString, width - textWidth - 63, offsetY - 4);
      }
      // prevent repainting
      licensePainted = true;
    }
  }

  // ------------------------------------------------------------------------------------------

  /**
   * this class implements the splash screen window if the native splash screen from java is not
   * availabe. If the native splash screen is available or not depends on the operating system and
   * if the splash was generated from the webstart or the native version.
   */
  private class OwnPanel extends JPanel {
    int percent;
    String progressmessage;
    BufferedImage bgImage = null;
    JWindow window;

    OwnPanel(JWindow win) {
      window = win;
      if (OS.contains("Mac") == false) {
        Rectangle rect = window.getBounds();
        try {
          bgImage = new Robot().createScreenCapture(rect);
          nativeTransparency = 2;
          System.out.println("--> Splash with faked Background-Screencapture Transparency");
        } catch (AWTException e) {
          throw new RuntimeException(e.getMessage());
        }
      } else {
        nativeTransparency = 3;
        System.out.println("--> Splash with fallback own Window with PNG");
      }
    }

    public void updateStatus(int p, String m) {
      percent = p;
      progressmessage = m;
    }

    public void paintComponent(Graphics g) {
      if (OS.contains("Mac") == false) g.drawImage(bgImage, 0, 0, ownSplashWindow);
      // paint the image
      ownSplashPic.paintIcon(ownSplashWindow, g, 0, 0);
      // the progress bar and text
      SonoSplashScreen.renderProgressBar((Graphics2D) g, percent, progressmessage);
    }
  }

  // ------------------------------------------------------------------------------------------

}
