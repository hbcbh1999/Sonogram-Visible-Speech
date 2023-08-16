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
public class LicenseDialog extends JDialog {
  JEditorPane browser;

  public LicenseDialog(Frame owner) {
    super(owner, "License", true);
    Sonogram reftomain = (Sonogram) owner;
    setSize(655, 800);
    setResizable(false);
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    browser = new JEditorPane();
    browser.setEditable(false);
    browser.setMargin(new Insets(0, 0, 0, 0));
    browser.setContentType("text/html");
    final java.net.URL helpurl = Sonogram.class.getResource("license.html");
    try {
      goToUrl(helpurl);
    } catch (Exception e) {
      reftomain.messageBox("Help Browser Error", "Help file license.html is not found !", 2);
      System.out.println("--> Can't find Helpfile index.html");
    }
    JScrollPane scroll = new JScrollPane(browser);
    scroll.getVerticalScrollBar().setPreferredSize(new Dimension(12, 0));
    cp.add(scroll, "Center");
    JButton okay;
    okay = new JButton("Close");
    okay.setMargin(new Insets(0, 0, 0, 0));
    okay.addActionListener(
        new ActionListener() {
          public void actionPerformed(ActionEvent e1) {
            setVisible(false);
          }
        });
    cp.add(okay, "South");
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
