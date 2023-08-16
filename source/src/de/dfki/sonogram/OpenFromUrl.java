package de.dfki.sonogram;

import java.awt.*;
import java.awt.event.*;
import java.util.Vector;
import javax.swing.*;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>Simple swing edit dialog to grab a URL from KeyBoard.
 *
 * @author Christoph Lauer
 * @version 1.0, Begin 31/06/2001, Current 26/09/2002
 */

// ----------------------------------------------------------------------------------------

public class OpenFromUrl extends JFrame implements ActionListener {
  JComboBox tf;
  Sonogram reftomain;

  // ---------------------------------------------------------------------------------------

  public OpenFromUrl(Sonogram owner) {
    reftomain = owner;
    // Change Icon
    Toolkit tk = Toolkit.getDefaultToolkit();
    setIconImage(tk.getImage(Sonogram.class.getResource("Sonogram.gif")));

    // Set Title Text
    setTitle("Enter the URL of the remote media file");
    Container cp = getContentPane();
    cp.setLayout(new FlowLayout());
    // generate the demo wave file entries
    Vector<String> demos = new Vector<String>();
    for (int i = 1; i <= 25; i++) {
      String s = "http://github.com/Christoph-Lauer/Sonogram/blob/main/examples/" + i + ".wav";
      if (i == 1) s += "    (Cat Purr)";
      if (i == 2) s += "    (Donkey)";
      if (i == 3) s += "    (Hedgehog)";
      if (i == 4) s += "    (Logarithmic Sweep)";
      if (i == 5) s += "    (Japanese Speech)";
      if (i == 6) s += "    (Applause)";
      if (i == 7) s += "    (Fanfare)";
      if (i == 8) s += "    (Engine Start)";
      if (i == 9) s += "    (Diphone Speech Synthesis)";
      if (i == 10) s += "  (Car Door Close)";
      if (i == 11) s += "  (Dolphins)";
      if (i == 12) s += "  (Bird Trush)";
      if (i == 13) s += "  (Orca)";
      if (i == 14) s += "  (Modem)";
      if (i == 15) s += "  (Violine)";
      if (i == 16) s += "  (Trombone)";
      if (i == 17) s += "  (Whistling)";
      if (i == 18) s += "  (Police Siren)";
      if (i == 19) s += "  (Swans)";
      if (i == 20) s += "  (Human Speech)";
      if (i == 21) s += "  (TTS french)";
      if (i == 22) s += "  (Bearing)";
      if (i == 23) s += "  (Spectogram Art)";
      if (i == 24) s += "  ()";
      if (i == 25) s += "  ()";
      demos.add(s);
    }

    // alloc the combo box
    tf = new JComboBox(demos);
    tf.setMaximumRowCount(25);
    tf.setPreferredSize(new Dimension(900, 21));
    tf.setEditable(true);
    tf.setSelectedIndex(5);
    tf.setToolTipText(
        "<html><b>Preconfigured Demo Files</b><br>Preconfigured demo files can be found at the"
            + " adress:<br><tt>http://www.christoph-lauer.de/demo1.wav ... demo6.wav</tt></html>");
    tf.addActionListener(this);
    cp.add(tf);
    JButton button = new JButton("Open");
    button.setMargin(new Insets(0, 0, 0, 0));
    button.addActionListener(this);
    cp.add(button);
    pack();

    // place it in the middle of the screen
    setLocationRelativeTo(null);

    // and finaly make it visible
    setResizable(false);
  }

  // ----------------------------------------------------------------------------------------

  public void actionPerformed(ActionEvent event) {
    System.out.println("--> URI: " + tf.getSelectedItem().toString());
    reftomain.fileisfromurl = true;
    reftomain.reader.generateSamplesFromURL(tf.getSelectedItem().toString());
    reftomain.storedurl = tf.getSelectedItem().toString();
    setVisible(false);
  }

  // -----------------------------------------------------------------------------------------

}
