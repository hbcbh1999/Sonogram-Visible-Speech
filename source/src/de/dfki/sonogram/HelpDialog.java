package de.dfki.sonogram;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.event.*;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>This is the help dialog for Sonogram. It shows the index.html file in the sonogram package
 * folder. The HelpDialog Class has the abillity to link into the internet via the http protokoll.
 * It is a modal dialog.
 *
 * @author Christoph Lauer
 * @version 1.0, Current 26/09/2002
 */
public class HelpDialog extends JDialog {
  JEditorPane browser;

  public HelpDialog(Frame owner) {
    super(owner, "Sonogram Online Help", false);
    Sonogram reftomain = (Sonogram) owner;
    setSize(630, 800);
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    browser = new JEditorPane();
    browser.setMargin(new Insets(0, 0, 0, 0));
    browser.setEditable(false);
    browser.setContentType("text/html");
    browser.addHyperlinkListener(
        new HyperlinkListener() {
          public void hyperlinkUpdate(HyperlinkEvent e) {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
              goToUrl(e.getURL());
            }
          }
        });
    final java.net.URL helpurl = Sonogram.class.getResource("index.html");
    try {
      goToUrl(helpurl);
    } catch (Exception e) {
      reftomain.messageBox("Help Browser Error", "Help file index.html is not found !", 2);
      System.out.println("--> Can't find Helpfile index.html");
    }
    JScrollPane scroll = new JScrollPane(browser);
    scroll.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
    cp.add(scroll, "Center");

    JButton okay, home;
    okay = new JButton("Close");
    home = new JButton("Home");
    okay.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e1) {
            setVisible(false);
          }
        });
    home.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e2) {
            try {
              goToUrl(helpurl);
            } catch (Exception e) {
              System.out.println("--> Can't find Helpfile index.html");
            }
          }
        });
    JPanel bp = new JPanel();
    bp.setLayout(new GridLayout(1, 2));
    bp.add(home);
    bp.add(okay);
    bp.setBorder(null);
    okay.setMargin(new Insets(0, 0, 0, 0));
    home.setMargin(new Insets(0, 0, 0, 0));
    cp.add(bp, "South");
    // place it in the middle of the screen
    setLocationRelativeTo(null);
  }

  private void goToUrl(URL url) {
    try {
      browser.setPage(url);
    } catch (Exception e) {
      System.out.println("--> Can't open URL");
    }
  }
}
