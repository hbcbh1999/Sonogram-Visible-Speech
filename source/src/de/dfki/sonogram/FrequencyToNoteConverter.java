package de.dfki.sonogram;

/**
 * Copyright (c) 2011 Christoph Lauer @ CLEngineering, All Rights Reserved. christophlauer@me.com -
 * www.christoph-lauer.de
 *
 * <p>This class presents a frequency to note converter and contains a simple static function for
 * the conversion.
 *
 * @author Christoph Lauer
 * @version 4.0, Current 01/10/2011
 */
public class FrequencyToNoteConverter {
  static final double A4 = 440.0D;
  static final int A4_INDEX = 57;
  static final String[] notes = {
    "C0", "C#0", "D0", "D#0", "E0", "F0", "F#0", "G0", "G#0", "A0", "A#0", "B0",
    "C1", "C#1", "D1", "D#1", "E1", "F1", "F#1", "G1", "G#1", "A1", "A#1", "B1",
    "C2", "C#2", "D2", "D#2", "E2", "F2", "F#2", "G2", "G#2", "A2", "A#2", "B2",
    "C3", "C#3", "D3", "D#3", "E3", "F3", "F#3", "G3", "G#3", "A3", "A#3", "B3",
    "C4", "C#4", "D4", "D#4", "E4", "F4", "F#4", "G4", "G#4", "A4", "A#4", "B4",
    "C5", "C#5", "D5", "D#5", "E5", "F5", "F#5", "G5", "G#5", "A5", "A#5", "B5",
    "C6", "C#6", "D6", "D#6", "E6", "F6", "F#6", "G6", "G#6", "A6", "A#6", "B6",
    "C7", "C#7", "D7", "D#7", "E7", "F7", "F#7", "G7", "G#7", "A7", "A#7", "B7",
    "C8", "C#8", "D8", "D#8", "E8", "F8", "F#8", "G8", "G#8", "A8", "A#8", "B8",
    "C9", "C#9", "D9", "D#9", "E9", "F9", "F#9", "G9", "G#9", "A9", "A#9", "B9"
  };
  static final int MINUS = 0;
  static final int PLUS = 1;

  public static String query(double paramDouble) {
    double d2 = Math.pow(2.0D, 0.08333333333333333D);
    double d3 = Math.pow(2.0D, 0.0008333333333333334D);
    int i = 0;
    int j = 0;
    String str;
    if ((paramDouble < 26.73D) || (paramDouble > 14496.0D)) {
      str = "";
      return str;
    }

    double d1 = 440.0D;
    int k;
    if (paramDouble >= d1) {
      while (paramDouble >= d2 * d1) {
        d1 = d2 * d1;
        i++;
      }
      while (paramDouble > d3 * d1) {
        d1 = d3 * d1;
        j++;
      }
      if (d3 * d1 - paramDouble < paramDouble - d1) j++;
      if (j > 50) {
        i++;
        j = 100 - j;
        if (j != 0) k = 0;
        else k = 1;
      } else {
        k = 1;
      }
    } else {
      do {
        d1 /= d2;
        i--;
      } while (paramDouble <= d1 / d2);

      while (paramDouble < d1 / d3) {
        d1 /= d3;
        j++;
      }
      if (paramDouble - d1 / d3 < d1 - paramDouble) j++;
      if (j >= 50) {
        i--;
        j = 100 - j;
        k = 1;
      } else if (j != 0) {
        k = 0;
      } else {
        k = 1;
      }
    }

    if (k == 1) str = "<b>" + notes[(57 + i)] + "</b><font size = 2> +" + j + "%</font>";
    else {
      str = "<b>" + notes[(57 + i)] + "</b><font size = 2> -" + j + "%</font>";
    }
    return str;
  }
}
