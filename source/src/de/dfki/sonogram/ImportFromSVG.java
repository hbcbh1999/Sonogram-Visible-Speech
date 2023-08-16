/* Decompiler 192ms, total 769ms, lines 477 */
package de.dfki.sonogram;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.Icon;
import javax.swing.JOptionPane;

class ImportFromSVG {
  Sonogram reftomain;
  File file;

  public ImportFromSVG(Sonogram var1) {
    boolean var2 = false;
    boolean var3 = false;
    boolean var4 = false;
    boolean var7 = false;
    boolean var8 = false;
    int var9 = 0;
    int var10 = 0;
    boolean var11 = false;
    boolean var12 = false;
    boolean var13 = false;
    int var14 = 0;
    this.reftomain = var1;
    System.out.println("--> Import Spectrum from SVG");
    EFileChooser var15 = new EFileChooser();
    var15.setApproveButtonText("Okay");
    var15.setDialogTitle("Select SVG file to Import");
    MediaFileFilter var16 = new MediaFileFilter("svg", "Scalable Vector Graphics (*.svg)");
    var15.addChoosableFileFilter(var16);
    boolean var17 = false;

    while (true) {
      try {
        int var33;
        do {
          var33 = var15.showOpenDialog(this.reftomain);
          if (var33 == 1) {
            return;
          }
        } while (var33 != 0);

        this.file = var15.getSelectedFile();
        System.out.println("--> Chosed File:" + this.file.getAbsolutePath());
        BufferedReader var18 = new BufferedReader(new FileReader(this.file));

        String var5;
        while ((var5 = var18.readLine()) != null) {
          if (var5.indexOf("SVG file written by Sonogram") > 0) {
            System.out.println("--> File written by Sonogram.");
            var2 = true;
          }

          if (var5.indexOf("SVG file written as Pure svg file") > 0) {
            System.out.println("--> No reopen File.");
            var3 = true;
          }
        }

        if (!var2) {
          this.reftomain.messageBox(
              "No open possible", "SVG file not written by Sonogram.\nNo opening possible", 2);
          return;
        }

        if (var3) {
          this.reftomain.messageBox(
              "No open possible",
              "SVG file written as PURE SVG\nwith no Sonogram specific Tags.\nNo opening possible.",
              2);
          return;
        }

        if (!var2) {
          continue;
        }

        this.reftomain.spektrumExist = false;
        this.reftomain.openingflag = true;
        var18 = new BufferedReader(new FileReader(this.file));

        String var6;
        int var28;
        int var29;
        while ((var5 = var18.readLine()) != null) {
          if (var5.indexOf("FilePath") > 0) {
            var28 = var5.indexOf("FilePath");
            var28 += 10;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            this.reftomain.filepath = var6;
          }

          if (var5.indexOf("TimewindowLength") > 0) {
            var28 = var5.indexOf("TimewindowLength");
            var28 += 18;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            this.reftomain.timewindowlength = (short) Integer.parseInt(var6);
            var10 = this.reftomain.timewindowlength / 2;
          }

          if (var5.indexOf("WindowNumbers") > 0) {
            var28 = var5.indexOf("WindowNumbers");
            var28 += 15;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            var9 = Integer.parseInt(var6);
          }

          if (var5.indexOf("WindowNumberAuto") > 0) {
            var28 = var5.indexOf("WindowNumberAuto");
            var28 += 18;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            if (var6.equals("true")) {
              this.reftomain.gad.cauto.setSelected(true);
            }

            if (var6.equals("false")) {
              this.reftomain.gad.cauto.setSelected(false);
            }
          }

          if (var5.indexOf("WindowFunktion") > 0) {
            var28 = var5.indexOf("WindowFunktion");
            var28 += 16;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            if (var6.equals("Hamming")) {
              this.reftomain.hamItem.setSelected(true);
            }

            if (var6.equals("Hanning")) {
              this.reftomain.hanItem.setSelected(true);
            }

            if (var6.equals("Blackman")) {
              this.reftomain.blaItem.setSelected(true);
            }

            if (var6.equals("Rectagle")) {
              this.reftomain.rectItem.setSelected(true);
            }

            if (var6.equals("Triangle")) {
              this.reftomain.triItem.setSelected(true);
            }

            if (var6.equals("Gauss")) {
              this.reftomain.gauItem.setSelected(true);
            }

            if (var6.equals("Welch")) {
              this.reftomain.welItem.setSelected(true);
            }
          }

          if (var5.indexOf("SmoothFrequenz") > 0) {
            var28 = var5.indexOf("SmoothFrequenz");
            var28 += 16;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            if (var6.equals("true")) {
              this.reftomain.gad.csmooth.setSelected(true);
              this.reftomain.gad.marksmothy = true;
            }

            if (var6.equals("false")) {
              this.reftomain.gad.csmooth.setSelected(false);
              this.reftomain.gad.marksmothy = false;
            }
          }

          if (var5.indexOf("SmoothTime") > 0) {
            var28 = var5.indexOf("SmoothTime");
            var28 += 12;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            if (var6.equals("true")) {
              this.reftomain.gad.csmoothx.setSelected(true);
              this.reftomain.gad.marksmothx = true;
            }

            if (var6.equals("false")) {
              this.reftomain.gad.csmoothx.setSelected(false);
              this.reftomain.gad.marksmothx = true;
            }
          }

          if (var5.indexOf("OverLapping") > 0) {
            var28 = var5.indexOf("OverLapping");
            var28 += 13;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            if (!var6.equals("to=")) {
              this.reftomain.gad.sliderwinspeed.setValue(Integer.parseInt(var6));
            }
          }

          if (var5.indexOf("OverLappingAuto") > 0) {
            var28 = var5.indexOf("OverLappingAuto");
            var28 += 17;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            if (var6.equals("true")) {
              this.reftomain.gad.coverlapping.setSelected(true);
              this.reftomain.gad.markoverl = true;
            }

            if (var6.equals("false")) {
              this.reftomain.gad.coverlapping.setSelected(false);
              this.reftomain.gad.markoverl = false;
            }
          }

          if (var5.indexOf("LogarithmEnabled") > 0) {
            var28 = var5.indexOf("LogarithmEnabled");
            var28 += 18;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            if (var6.equals("true")) {
              this.reftomain.gad.clog.setSelected(true);
              this.reftomain.gad.marklog = true;
            }

            if (var6.equals("false")) {
              this.reftomain.gad.clog.setSelected(false);
              this.reftomain.gad.marklog = false;
            }
          }

          if (var5.indexOf("LogarithmScale") > 0) {
            var28 = var5.indexOf("LogarithmScale");
            var28 += 16;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            this.reftomain.gad.sliderlog.setValue(Integer.parseInt(var6));
          }

          if (var5.indexOf("Samplerate8Khz") > 0) {
            var28 = var5.indexOf("Samplerate8Khz");
            var28 += 16;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            if (var6.equals("true")) {
              this.reftomain.gad.csampl.setSelected(true);
              this.reftomain.gad.markcsamp = true;
            }

            if (var6.equals("false")) {
              this.reftomain.gad.csampl.setSelected(false);
              this.reftomain.gad.markcsamp = false;
            }
          }

          if (var5.indexOf("Transformation") > 0) {
            var28 = var5.indexOf("Transformation");
            var28 += 16;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            if (var6.equals("FFT")) {
              this.reftomain.gad.rfft.setSelected(true);
              this.reftomain.gad.marktrans = true;
            }

            if (var6.equals("LPC")) {
              this.reftomain.gad.rlpc.setSelected(true);
              this.reftomain.gad.marktrans = true;
            }
          }

          if (var5.indexOf("LPCCoeficients") > 0) {
            var28 = var5.indexOf("LPCCoeficients");
            var28 += 16;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            this.reftomain.gad.sliderlpccoef.setValue(Integer.parseInt(var6));
          }

          if (var5.indexOf("LPCFFTPoints") > 0) {
            var28 = var5.indexOf("LPCFFTPoints");
            var28 += 14;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            this.reftomain.gad.sliderlpcfftnum.setValue(Integer.parseInt(var6));
          }

          if (var5.indexOf("PaintScaleFact") > 0) {
            var28 = var5.indexOf("PaintScaleFact");
            var28 += 16;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            var14 = Integer.parseInt(var6);
          }

          if (var5.indexOf("SampleRate") > 0) {
            var28 = var5.indexOf("SampleRate");
            var28 += 12;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            this.reftomain.samplerate = Integer.parseInt(var6);
          }

          if (var5.indexOf("SamplesTotal") > 0) {
            var28 = var5.indexOf("SamplesTotal");
            var28 += 14;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            this.reftomain.samplestotal = Integer.parseInt(var6);
          }
        }

        this.reftomain.gad.applyChanges();
        System.out.println("--> Configuration tag read: w=" + var9 + " heigth=" + var10);
        this.reftomain.spektrum.removeAllElements();

        for (int var19 = 0; var19 < var9; ++var19) {
          this.reftomain.spektrum.addElement(new float[var10]);
        }

        float[][] var34 = new float[var9][var10];
        var18 = new BufferedReader(new FileReader(this.file));

        while ((var5 = var18.readLine()) != null) {
          if (var5.indexOf("rect") > 0) {
            var28 = var5.indexOf("amplitude=");
            var28 += 11;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            int var32 = Integer.parseInt(var6);
            var28 = var5.indexOf("x=");
            var28 += 3;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            int var30 = Integer.parseInt(var6);
            var28 = var5.indexOf("y=");
            var28 += 3;
            var29 = var5.indexOf("\"", var28);
            var6 = var5.substring(var28, var29);
            int var31 = Integer.parseInt(var6);
            var34[var30 / var14][var31 / var14] = (float) var32;
          }
        }

        for (int var21 = 0; var21 < var9; ++var21) {
          float[] var20 = (float[]) this.reftomain.spektrum.get(var21);

          for (int var22 = 0; var22 < var10; ++var22) {
            var20[var10 - var22 - 1] = var34[var21][var22];
          }
        }

        try {
          File var35 = new File(this.reftomain.filepath.substring(5));
          var4 = var35.canRead();
          System.out.println("--> File is redable:" + var4);
        } catch (Throwable var26) {
          this.reftomain.messageBox("Error", "Error while check for file if exist", 2);
        }

        float[] var36 = new float[2000];
        float var37 = 0.0F;
        float var23 = 0.0F;

        int var24;
        for (var24 = 0; var24 < 2000; ++var24) {
          var37 = 0.0F;

          for (int var25 = 0; var25 < var10; ++var25) {
            var37 += var34[(int) ((float) var24 / 2000.0F * (float) var9)][var25];
          }

          var36[var24] = var37;
          if (var23 < var37) {
            var23 = var37;
          }
        }

        for (var24 = 0; var24 < 2000; ++var24) {
          var36[var24] /= var23;
          var36[var24] *= 127.0F;
        }

        for (var24 = 0; var24 < 2000; ++var24) {
          this.reftomain.timeline[var24] = (byte) ((int) var36[var24]);
        }

        this.reftomain.gad.cenergy.setSelected(false);
        this.reftomain.spektrumExist = true;
        this.reftomain.updateimageflag = true;
        this.reftomain.openingflag = false;
        this.reftomain.repaint();
        if (var4) {
          this.reftomain.player = new PlaySound(this.reftomain.filepath, this.reftomain);
          this.reftomain.pp.plstart = 0.0D;
          this.reftomain.pp.plstop = 1.0D;
          this.reftomain.pp.plbutton = 0.0D;
          System.out.println("--> Opening from SVG File Finished");
          var24 =
              JOptionPane.showOptionDialog(
                  this.reftomain,
                  "If a plot is imported via SVG,\n"
                      + "not all features of Sonogram are\n"
                      + "aviable (zoom,logarithm amplitude...)\n"
                      + "Would you reopen ths File from orginal source ?",
                  "Reopen ?",
                  0,
                  3,
                  (Icon) null,
                  (Object[]) null,
                  (Object) null);
          if (var24 == 0) {
            this.reftomain.openFile(this.reftomain.filepath);
            return;
          }
        }

        this.reftomain.spektrumExist = false;
        this.reftomain.enableItems(true);
        this.reftomain.stopItem.setEnabled(false);
        this.reftomain.stopbutton.setEnabled(false);
        this.reftomain.infoItem.setEnabled(false);
        this.reftomain.infobutton.setEnabled(false);
        this.reftomain.zinbutton.setEnabled(false);
        this.reftomain.zinItem.setEnabled(false);
        this.reftomain.zbabutton.setEnabled(false);
        this.reftomain.zbaItem.setEnabled(false);
        this.reftomain.zprebutton.setEnabled(false);
        this.reftomain.zpreItem.setEnabled(false);
        this.reftomain.cepbutton.setEnabled(false);
        this.reftomain.pitchbutton.setEnabled(false);
        this.reftomain.autocorrelationbutton.setEnabled(false);
        this.reftomain.wavbutton.setEnabled(false);
        this.reftomain.wavItem.setEnabled(false);
        this.reftomain.cepItem.setEnabled(false);
        this.reftomain.forbutton.setEnabled(false);
        this.reftomain.forItem.setEnabled(false);
        this.reftomain.lpcbutton.setEnabled(false);
        this.reftomain.lpcItem.setEnabled(false);
        this.reftomain.hamItem.setEnabled(false);
        this.reftomain.hanItem.setEnabled(false);
        this.reftomain.gauItem.setEnabled(false);
        this.reftomain.welItem.setEnabled(false);
        this.reftomain.blaItem.setEnabled(false);
        this.reftomain.triItem.setEnabled(false);
        this.reftomain.svgItem.setEnabled(false);
        this.reftomain.svgbutton.setEnabled(false);
        this.reftomain.walItem.setEnabled(false);
        this.reftomain.walbutton.setEnabled(false);
        this.reftomain.rectItem.setEnabled(false);
        this.reftomain.d3Item.setEnabled(false);
        this.reftomain.gad.sliderwinfunktion.setEnabled(false);
        this.reftomain.logItem.setEnabled(false);
        this.reftomain.gad.clog.setEnabled(false);
        this.reftomain.gad.sliderlog.setEnabled(false);
        this.reftomain.gad.csmoothx.setEnabled(false);
        this.reftomain.gad.csmooth.setEnabled(false);
        this.reftomain.gad.cenergy.setEnabled(false);
        this.reftomain.gad.coverlapping.setEnabled(false);
        this.reftomain.gad.sliderwinspeed.setEnabled(false);
        this.reftomain.gad.sliderwinsize.setEnabled(false);
        this.reftomain.gad.cauto.setEnabled(false);
        this.reftomain.spektrumExist = true;
        if (!var4) {
          this.reftomain.messageBox(
              "Source File not found",
              "The original Source File of this plot was not found at:\n"
                  + this.reftomain.filepath
                  + "\n"
                  + "Not all the Features of Sonogram are aviable without\n"
                  + "the original Source Media File.",
              2);
          this.reftomain.gad.csampl.setEnabled(false);
          this.reftomain.playbutton.setEnabled(false);
          this.reftomain.playItem.setEnabled(false);
          this.reftomain.stopbutton.setEnabled(false);
          this.reftomain.stopItem.setEnabled(false);
          this.reftomain.revbutton.setEnabled(false);
          this.reftomain.revItem.setEnabled(false);
          this.reftomain.gad.btro.setEnabled(false);
          this.reftomain.wavbutton.setEnabled(false);
          this.reftomain.wavItem.setEnabled(false);
          this.reftomain.walItem.setEnabled(false);
        }

        File var38 = new File(this.reftomain.filepath.substring(5));
        this.reftomain.setTitle("Sonogram 1.2  " + var38.getName());
        this.reftomain.openenedbysvg = true;
      } catch (Throwable var27) {
        this.reftomain.messageBox("Error", "Error while reading File\nSee console for details.", 1);
        System.out.println(var27);
      }

      return;
    }
  }
}
