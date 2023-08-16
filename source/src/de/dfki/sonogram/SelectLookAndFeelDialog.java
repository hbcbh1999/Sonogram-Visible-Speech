package de.dfki.sonogram;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>Simple dialog whith slider for L&F selection.
 *
 * @author Christoph Lauer
 * @version 1.0, Current 26/09/2002
 */
class SelectLookAndFeelDialog extends JDialog {
  JSlider slider;
  Sonogram reftomain;

  public SelectLookAndFeelDialog(Sonogram ref) {
    super(ref, "Select Look'n Feel", false);
    reftomain = ref;

    JPanel p1 = new JPanel();
    p1.setBorder(new TitledBorder(new EtchedBorder(), "Look And Feel"));
    p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));

    slider = new JSlider(JSlider.VERTICAL, 0, 7, 3);
    slider.setToolTipText("<html>Change <i>\"Look and Feel\"</i> of <b>all</b> Sonogram Windows");
    Hashtable h = new Hashtable();
    h.put(Integer.valueOf(0), new JLabel("Metal")); // NOTE --> HT.PUT(K,V)
    h.put(
        Integer.valueOf(1),
        new JLabel("<html><font color = #660000>Kunststoff (bright Colors)</font>"));
    h.put(Integer.valueOf(2), new JLabel("Kunststoff (Desktop)"));
    h.put(Integer.valueOf(3), new JLabel("Kunststoff (Notebook)"));
    h.put(Integer.valueOf(4), new JLabel("Kunststoff (Presentation)"));
    h.put(Integer.valueOf(5), new JLabel("Kunststoff (Sonogram)"));
    h.put(Integer.valueOf(6), new JLabel("Nativ OS *"));
    h.put(Integer.valueOf(7), new JLabel("Ocean"));
    slider.setLabelTable(h);
    slider.setPaintLabels(true);
    slider.setSnapToTicks(true);
    slider.setMinorTickSpacing(1);
    slider.setPaintTicks(true);
    p1.add(slider);
    JLabel hint = new JLabel("<html> <font size=-3 color=#000066><i>(* = needs restart)");
    hint.setHorizontalAlignment(JLabel.LEFT);
    p1.add(hint);
    getContentPane().add(p1, BorderLayout.CENTER);

    JPanel p2 = new JPanel();
    JButton btap = new JButton("Test");
    ActionListener alst =
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            reftomain.setLookAndFeel(slider.getValue());
          }
        };
    btap.addActionListener(alst);
    btap.setBounds(6, 130, 95, 25);
    p2.add(btap);
    JButton btok = new JButton("Okay");
    ActionListener olst =
        new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            reftomain.setLookAndFeel(slider.getValue());
            setVisible(false);
          }
        };
    btok.addActionListener(olst);
    btok.setBounds(6, 100, 95, 25);
    p2.add(btok);
    getContentPane().add(p2, BorderLayout.SOUTH);

    pack();
  }
}
