package de.dfki.sonogram;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Copyright (c) 2011 Christoph Lauer @ DFKI, All Rights Reserved. christoph.lauer@me.com -
 * www.christoph-lauer.de
 *
 * <p>This Class exports the spektrum to a CSV text file.
 *
 * @author Christoph Lauer
 * @version 1.0, Begin Current 23/12/2011
 */
class ExportSpectrumCSV {
  public ExportSpectrumCSV(Sonogram reftosonogram) {

    // first select the CSV file
    EFileChooser chooser = new EFileChooser();
    chooser.setApproveButtonText("Save");
    chooser.setDialogTitle("Select CSV file for writting.");
    chooser.setSelectedFile(new File("Sonogram.csv"));
    MediaFileFilter ffcsv = new MediaFileFilter("csv", "Comma Separated Values (*.csv)");
    chooser.addChoosableFileFilter(ffcsv);
    int returnVal = 0;
    do {
      returnVal = chooser.showOpenDialog(reftosonogram);
      if (returnVal == JFileChooser.CANCEL_OPTION) return;
    } while (returnVal != JFileChooser.APPROVE_OPTION);
    File file = chooser.getSelectedFile();
    System.out.println("--> selected CSV file: " + file);

    // open the File for writing
    try {
      FileWriter outFile = new FileWriter(file);
      PrintWriter out = new PrintWriter(outFile);

      // write the CSV file
      for (int x = 0; x < reftosonogram.spektrum.size(); x++) {
        float[] spec = (float[]) reftosonogram.spektrum.get(x);
        for (int y = 0; y < (reftosonogram.timewindowlength / 2); y++) {
          float ampl = spec[y];
          out.print(ampl);
          out.print(" ; ");
        }
        out.print("\n");
      }

      // close the file
      out.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
