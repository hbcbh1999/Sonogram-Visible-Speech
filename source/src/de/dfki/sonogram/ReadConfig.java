package de.dfki.sonogram;

import java.awt.Color;
import java.io.*;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>This class reads <i>Sonogramm</i> settings from XML file named SonogramConfig.xml
 *
 * @author Christoph Lauer
 * @version 1.0, Begin 31/07/2002, Current Time-stamp: <03/04/17 15:59:19 clauer>
 */
public class ReadConfig {

  // this flag changes if the configuration can not be found
  public boolean successfully = true;

  public ReadConfig(Sonogram sono) {
    try {
      String line, path;
      String tmp;
      int t1, t2;
      int sx = 0;
      int sy = 0;
      int sw = 0;
      int sh = 0;
      int gx = 0;
      int gy = 0;
      int gw = 0;
      int gh = 0;
      int cx = 0;
      int cy = 0;
      int cw = 0;
      int ch = 0;
      int lx = 0;
      int ly = 0;
      int lw = 0;
      int lh = 0;
      int fx = 0;
      int fy = 0;
      int fw = 0;
      int fh = 0;
      int wx = 0;
      int wy = 0;
      int ww = 0;
      int wh = 0;
      int ax = 0;
      int ay = 0;
      int aw = 0;
      int ah = 0;
      int kx = 0;
      int ky = 0;
      int kw = 0;
      int kh = 0;
      System.out.println("--> Starting reading SonogramConfig.xml");
      // read hotlist
      BufferedReader rd = new BufferedReader(new FileReader("SonogramConfig.xml"));
      while ((line = rd.readLine()) != null) {
        if (line.indexOf("hotlist-item") > 0) {
          t1 = line.indexOf("path");
          t1 += 6;
          t2 = line.lastIndexOf("\"");
          path = line.substring(t1, t2);
          sono.filehistory.add(path);
        }
      }
      rd.close();
      // read screen locations and dimensions
      rd = new BufferedReader(new FileReader("SonogramConfig.xml"));
      while ((line = rd.readLine()) != null) {
        if (line.indexOf("ScreenPositionSonogram") > 0) {
          t1 = line.indexOf("x=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sx = Integer.parseInt(tmp);
          t1 = line.indexOf("y=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sy = Integer.parseInt(tmp);
          t1 = line.indexOf("width=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sw = Integer.parseInt(tmp);
          t1 = line.indexOf("heigth=");
          t1 += 8;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sh = Integer.parseInt(tmp);
        }
        if (line.indexOf("ScreenPositionGAD") > 0) {
          t1 = line.indexOf("x=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          gx = Integer.parseInt(tmp);
          t1 = line.indexOf("y=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          gy = Integer.parseInt(tmp);
          t1 = line.indexOf("width=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          gw = Integer.parseInt(tmp);
          t1 = line.indexOf("heigth=");
          t1 += 8;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          gh = Integer.parseInt(tmp);
        }
        if (line.indexOf("ScreenPositionCepstrum") > 0) {
          t1 = line.indexOf("x=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          cx = Integer.parseInt(tmp);
          t1 = line.indexOf("y=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          cy = Integer.parseInt(tmp);
          t1 = line.indexOf("width=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          cw = Integer.parseInt(tmp);
          t1 = line.indexOf("heigth=");
          t1 += 8;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          ch = Integer.parseInt(tmp);
        }
        if (line.indexOf("ScreenPositionFourier") > 0) {
          t1 = line.indexOf("x=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          fx = Integer.parseInt(tmp);
          t1 = line.indexOf("y=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          fy = Integer.parseInt(tmp);
          t1 = line.indexOf("width=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          fw = Integer.parseInt(tmp);
          t1 = line.indexOf("heigth=");
          t1 += 8;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          fh = Integer.parseInt(tmp);
        }
        if (line.indexOf("ScreenPositionLPC") > 0) {
          t1 = line.indexOf("x=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          lx = Integer.parseInt(tmp);
          t1 = line.indexOf("y=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          ly = Integer.parseInt(tmp);
          t1 = line.indexOf("width=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          lw = Integer.parseInt(tmp);
          t1 = line.indexOf("heigth=");
          t1 += 8;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          lh = Integer.parseInt(tmp);
        }
        if (line.indexOf("ScreenPositionTimeWave") > 0) {
          t1 = line.indexOf("x=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          wx = Integer.parseInt(tmp);
          t1 = line.indexOf("y=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          wy = Integer.parseInt(tmp);
          t1 = line.indexOf("width=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          ww = Integer.parseInt(tmp);
          t1 = line.indexOf("heigth=");
          t1 += 8;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          wh = Integer.parseInt(tmp);
        }
        if (line.indexOf("ScreenPositionWavelet") > 0) {
          t1 = line.indexOf("x=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          ax = Integer.parseInt(tmp);
          t1 = line.indexOf("y=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          ay = Integer.parseInt(tmp);
          t1 = line.indexOf("width=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          aw = Integer.parseInt(tmp);
          t1 = line.indexOf("heigth=");
          t1 += 8;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          ah = Integer.parseInt(tmp);
        }
        if (line.indexOf("ScreenPositionAutoCorr") > 0) {
          t1 = line.indexOf("x=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          kx = Integer.parseInt(tmp);
          t1 = line.indexOf("y=");
          t1 += 3;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          ky = Integer.parseInt(tmp);
          t1 = line.indexOf("width=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          kw = Integer.parseInt(tmp);
          t1 = line.indexOf("heigth=");
          t1 += 8;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          kh = Integer.parseInt(tmp);
        }
        // CHECKBOXES (BOOLEANS)
        if (line.indexOf("InverseColors") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iinv = true;
          }
          if (tmp.equals("false")) {
            sono.iinv = false;
          }
        }
        if (line.indexOf("LogarithmAmplitude") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iloga = true;
          }
          if (tmp.equals("false")) {
            sono.iloga = false;
          }
        }
        if (line.indexOf("GridInSonogram") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.igrid = true;
          }
          if (tmp.equals("false")) {
            sono.igrid = false;
          }
        }
        if (line.indexOf("ToolTipInSonogram") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.itooltip = true;
          }
          if (tmp.equals("false")) {
            sono.itooltip = false;
          }
        }
        if (line.indexOf("AutomaticalSearchWinLeng") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iautowinl = true;
          }
          if (tmp.equals("false")) {
            sono.iautowinl = false;
          }
        }
        if (line.indexOf("NormalizeSingleCurve") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.inorsi = true;
          }
          if (tmp.equals("false")) {
            sono.inorsi = false;
          }
        }
        if (line.indexOf("SmoothOverFrequency") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ismof = true;
          }
          if (tmp.equals("false")) {
            sono.ismof = false;
          }
        }
        if (line.indexOf("SmoothSingleCurve") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ismosi = true;
          }
          if (tmp.equals("false")) {
            sono.ismosi = false;
          }
        }
        if (line.indexOf("ShowEnergyTimeline") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ienergy = true;
          }
          if (tmp.equals("false")) {
            sono.ienergy = false;
          }
        }
        if (line.indexOf("LogarithmInLPC") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iloglpc = true;
          }
          if (tmp.equals("false")) {
            sono.iloglpc = false;
          }
        }
        if (line.indexOf("LogarithmInFourier") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ilogfour = true;
          }
          if (tmp.equals("false")) {
            sono.ilogfour = false;
          }
        }
        if (line.indexOf("OpenWith8Khz") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iopen8 = true;
          }
          if (tmp.equals("false")) {
            sono.iopen8 = false;
          }
        }
        if (line.indexOf("SmoothOverTime") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ismot = true;
          }
          if (tmp.equals("false")) {
            sono.ismot = false;
          }
        }
        if (line.indexOf("EnablePitchDetection") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ipide = true;
          }
          if (tmp.equals("false")) {
            sono.ipide = false;
          }
        }
        if (line.indexOf("PitchFrequencyLimitation") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ipifrlim = true;
          }
          if (tmp.equals("false")) {
            sono.ipifrlim = false;
          }
        }

        if (line.indexOf("PitchRemoveRunaways") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ipraway = true;
          }
          if (tmp.equals("false")) {
            sono.ipraway = false;
          }
        }

        if (line.indexOf("EnableOverlapping") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ienov = true;
          }
          if (tmp.equals("false")) {
            sono.ienov = false;
          }
        }
        if (line.indexOf("SaveHistoryList") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.isavehi = true;
          }
          if (tmp.equals("false")) {
            sono.isavehi = false;
          }
        }
        if (line.indexOf("FogOverSonoWhilePitch") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ipitchfog = true;
          }
          if (tmp.equals("false")) {
            sono.ipitchfog = false;
          }
        }
        if (line.indexOf("BlackPitchLine") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ipitchblack = true;
          }
          if (tmp.equals("false")) {
            sono.ipitchblack = false;
          }
        }
        if (line.indexOf("GeneratePitchWithSmooth") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ipitchsm = true;
          }
          if (tmp.equals("false")) {
            sono.ipitchsm = false;
          }
        }
        if (line.indexOf("ShowSpectrumWhilePlaying") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ispecpl = true;
          }
          if (tmp.equals("false")) {
            sono.ispecpl = false;
          }
        }
        if (line.indexOf("LoopPlaying") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ilooppl = true;
          }
          if (tmp.equals("false")) {
            sono.ilooppl = false;
          }
        }
        if (line.indexOf("MonochromSingleCurve") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.imonoso = true;
          }
          if (tmp.equals("false")) {
            sono.imonoso = false;
          }
        }
        if (line.indexOf("LogarithmFrequency") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ilogf = true;
          }
          if (tmp.equals("false")) {
            sono.ilogf = false;
          }
        }
        if (line.indexOf("SaveScreenPositions") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.isascpo = true;
          }
          if (tmp.equals("false")) {
            sono.isascpo = false;
          }
        }
        if (line.indexOf("SaveConfigurationAtEnd") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.isaco = true;
          }
          if (tmp.equals("false")) {
            sono.isaco = false;
          }
        }
        if (line.indexOf("UseFftForTransformation") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iffttrans = true;
          }
          if (tmp.equals("false")) {
            sono.iffttrans = false;
          }
        }
        if (line.indexOf("OpenLastFile") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iopenlast = true;
          }
          if (tmp.equals("false")) {
            sono.iopenlast = false;
          }
        }
        if (line.indexOf("LastFileWithZoom") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ilastwithzoom = true;
          }
          if (tmp.equals("false")) {
            sono.ilastwithzoom = false;
          }
        }
        if (line.indexOf("ArrangeAfterAutoStart") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.isarr = true;
          }
          if (tmp.equals("false")) {
            sono.isarr = false;
          }
        }
        if (line.indexOf("CepstrumLogarithm") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iceplog = true;
          }
          if (tmp.equals("false")) {
            sono.iceplog = false;
          }
        }
        if (line.indexOf("WaveLines") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iwavelines = true;
          }
          if (tmp.equals("false")) {
            sono.iwavelines = false;
          }
        }
        if (line.indexOf("MuteSound") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.imute = true;
          }
          if (tmp.equals("false")) {
            sono.imute = false;
          }
        }
        if (line.indexOf("SearchForLocalPeak") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ilocalpeak = true;
          }
          if (tmp.equals("false")) {
            sono.ilocalpeak = false;
          }
        }
        if (line.indexOf("ThreeDimUniverse") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iuniverse = true;
          }
          if (tmp.equals("false")) {
            sono.iuniverse = false;
          }
        }
        if (line.indexOf("WaveletLogarithm") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iwallog = true;
          }
          if (tmp.equals("false")) {
            sono.iwallog = false;
          }
        }
        if (line.indexOf("ConnectAutocorrPoints") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iacdl = true;
          }
          if (tmp.equals("false")) {
            sono.iacdl = false;
          }
        }
        if (line.indexOf("AutocorrSmooth") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iacsmooth = true;
          }
          if (tmp.equals("false")) {
            sono.iacsmooth = false;
          }
        }
        if (line.indexOf("AutocorrPitch") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iacpitch = true;
          }
          if (tmp.equals("false")) {
            sono.iacpitch = false;
          }
        }
        if (line.indexOf("AntialiseAnalyseWins") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iantialise = true;
          }
          if (tmp.equals("false")) {
            sono.iantialise = false;
          }
        }
        if (line.indexOf("AntialiseConfirmed") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iantialisconfirmed = true;
          }
          if (tmp.equals("false")) {
            sono.iantialisconfirmed = false;
          }
        }
        if (line.indexOf("WvConfirmed") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iwvconfirmed = true;
          }
          if (tmp.equals("false")) {
            sono.iwvconfirmed = false;
          }
        }
        if (line.indexOf("NormalizeWaveForm") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iwfnorm = true;
          }
          if (tmp.equals("false")) {
            sono.iwfnorm = false;
          }
        }
        if (line.indexOf("CepstrumSmooth") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.icepsmooth = true;
          }
          if (tmp.equals("false")) {
            sono.icepsmooth = false;
          }
        }
        if (line.indexOf("Mp3Confirm") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.mp3confirm = true;
          }
          if (tmp.equals("false")) {
            sono.mp3confirm = false;
          }
        }
        if (line.indexOf("PerspectogramRotation") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.irotate = true;
          }
          if (tmp.equals("false")) {
            sono.irotate = false;
          }
        }
        if (line.indexOf("PerspectogramAntialias") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iperantialias = true;
          }
          if (tmp.equals("false")) {
            sono.iperantialias = false;
          }
        }
        if (line.indexOf("PerspectogramCoordSystem") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ipercoord = true;
          }
          if (tmp.equals("false")) {
            sono.ipercoord = false;
          }
        }
        if (line.indexOf("ACPitchConLines") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ipclin = true;
          }
          if (tmp.equals("false")) {
            sono.ipclin = false;
          }
        }
        if (line.indexOf("ACPitchSmooth") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ipsmo = true;
          }
          if (tmp.equals("false")) {
            sono.ipsmo = false;
          }
        }
        if (line.indexOf("ACPitchRemSilent") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iprsil = true;
          }
          if (tmp.equals("false")) {
            sono.iprsil = false;
          }
        }
        if (line.indexOf("ACPitchRemoveRunaways") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ipraway = true;
          }
          if (tmp.equals("false")) {
            sono.ipraway = false;
          }
        }
        if (line.indexOf("ACPitchTrackMove") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.iptrack = true;
          }
          if (tmp.equals("false")) {
            sono.iptrack = false;
          }
        }
        if (line.indexOf("ACPitchTrackFog") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.ipfog = true;
          }
          if (tmp.equals("false")) {
            sono.ipfog = false;
          }
        }
        if (line.indexOf("PolygonWarning") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.i3dpolygons = true;
          }
          if (tmp.equals("false")) {
            sono.i3dpolygons = false;
          }
        }
        if (line.indexOf("PerKeysConfirm") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if (tmp.equals("true")) {
            sono.perkeysconfirm = true;
          }
          if (tmp.equals("false")) {
            sono.perkeysconfirm = false;
          }
        }

        // SLIDERS (INTEGERS)
        if (line.indexOf("SelectedWavelet") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.iswalsel = Integer.parseInt(tmp);
        }
        if (line.indexOf("WaveletOctave") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.iswaloct = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderLoudness") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.isldb = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderWaveTime") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.islwavetime = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderWindowSize") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.islws = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderCepstrumBuffer") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.islcep = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderWindowFunktion") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.islwf = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderOverlapping") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.islov = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderSurfaceDataReduct") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.islsdr = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderFourierFraction") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.islff = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderFourierLen") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.islfl = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderLpcCoefficients") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.isllc = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderLpcFftNums") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.islf = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderLpcSamplesFutur") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.islls = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderLpcFrequencyFrac") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.isllff = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderSurfaceY") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.islsy = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderSurfaceX") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.islsx = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderPitchFrequency") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.islpf = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderLogarithmAmp") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.islla = Integer.parseInt(tmp);
        }
        if (line.indexOf("SelectedColor") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.isc = Integer.parseInt(tmp);
        }
        if (line.indexOf("SurfaceRendering") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.isr = Integer.parseInt(tmp);
        }
        if (line.indexOf("LookAndFeel") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.ilaf = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderAutoCorrWinLen") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.isacwl = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderAutoCorrWinShift") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.isacws = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderACPitchWinShift") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.isacpws = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderACPitchWinLength") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.isacpwl = Integer.parseInt(tmp);
        }
        if (line.indexOf("SliderACPitchMaxFrequ") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.isacpmax = Integer.parseInt(tmp);
        }
        if (line.indexOf("RecorderLedColors") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          sono.la.tp.colorSwitch = Integer.parseInt(tmp);
          sono.la.tp.initDigitArray();
        }

        // OTHERS
        if (line.indexOf("LastOpenedFile") > 0) {
          t1 = line.indexOf("value=");
          t1 += 7;
          t2 = line.indexOf("\"", t1);
          tmp = line.substring(t1, t2);
          if ((tmp.equals("null") == false)) if (sono.filepath == null) sono.filepath = tmp;
        }
        if (line.indexOf("BgColorForSurface") > 0) {
          int r = 0, g = 0, b = 0;
          t1 = line.indexOf("r=");
          t1 += 2;
          t2 = line.indexOf(",", t1);
          tmp = line.substring(t1, t2);
          r = Integer.parseInt(tmp);
          t1 = line.indexOf("g=");
          t1 += 2;
          t2 = line.indexOf(",", t1);
          tmp = line.substring(t1, t2);
          g = Integer.parseInt(tmp);
          t1 = line.indexOf("b=");
          t1 += 2;
          t2 = line.indexOf("]", t1);
          tmp = line.substring(t1, t2);
          b = Integer.parseInt(tmp);
          sono.ibgcol = new Color(r, g, b);
        }
        // Read Zoominformation only when openLastFile is stred as selected
        if (sono.iopenlast == true && sono.ilastwithzoom == true) {
          sono.autoopened = true;
          if (line.indexOf("SelectedStartCurrent") > 0) {
            t1 = line.indexOf("value=");
            t1 += 7;
            t2 = line.indexOf("\"", t1);
            tmp = line.substring(t1, t2);
            sono.selectedstart = Double.parseDouble(tmp);
          }
          if (line.indexOf("SelectedWidthCurrent") > 0) {
            t1 = line.indexOf("value=");
            t1 += 7;
            t2 = line.indexOf("\"", t1);
            tmp = line.substring(t1, t2);
            sono.selecedwidth = Double.parseDouble(tmp);
          }
          if (line.indexOf("ZoomPreviousIndex") > 0) {
            t1 = line.indexOf("value=");
            t1 += 7;
            t2 = line.indexOf("\"", t1);
            tmp = line.substring(t1, t2);
            sono.zoompreviousindex = Integer.parseInt(tmp);
          }
          if (line.indexOf("ZoomPreviousIndex") > 0) {
            t1 = line.indexOf("value=");
            t1 += 7;
            t2 = line.indexOf("\"", t1);
            tmp = line.substring(t1, t2);
            sono.zoompreviousindex = Integer.parseInt(tmp);
          }
          if (line.indexOf("SelectedStartPrevious") > 0) {
            t1 = line.indexOf("number=");
            t1 += 8;
            t2 = line.indexOf("\"", t1);
            tmp = line.substring(t1, t2);
            int number = Integer.parseInt(tmp);
            t1 = line.indexOf("value=");
            t1 += 7;
            t2 = line.indexOf("\"", t1);
            tmp = line.substring(t1, t2);
            sono.selectedstartpre.add(number, Double.valueOf(tmp));
          }
          if (line.indexOf("SelectedWidthPrevious") > 0) {
            t1 = line.indexOf("number=");
            t1 += 8;
            t2 = line.indexOf("\"", t1);
            tmp = line.substring(t1, t2);
            int number = Integer.parseInt(tmp);
            t1 = line.indexOf("value=");
            t1 += 7;
            t2 = line.indexOf("\"", t1);
            tmp = line.substring(t1, t2);
            sono.selectedwidthpre.add(number, Double.valueOf(tmp));
          }
        }
      }
      rd.close();
      // Set Chages
      if (sono.iopenlast == true) // For Datasourcereader to leave width and start
      sono.autoopened = true;

      // postprocess the file history vector to restrict the length of maximal 9 elements
      while (sono.filehistory.size() > 10) sono.filehistory.removeElementAt(0);

      // Not all Unix/Linux Windownmanager can give right position while
      // store old one Size and Position in Configfile, because mainwindow
      // is sized at normal position
      // sh = 1000;
      // sw = 540;
      if (sono.isascpo == true) {
        sono.setLocation(sx, sy);
        sono.setSize(sh, sw);
        sono.cv.setLocation(cx, cy);
        sono.cv.setSize(ch, cw);
        sono.lv.setLocation(lx, ly);
        sono.lv.setSize(lh, lw);
        sono.fv.setLocation(fx, fy);
        sono.fv.setSize(fh, fw);
        sono.wv.setLocation(wx, wy);
        sono.wv.setSize(wh, ww);
        sono.av.setLocation(ax, ay);
        sono.av.setSize(ah, aw);
        sono.kv.setLocation(kx, ky);
        sono.kv.setSize(kh, kw);
        // GAD was constructed later !!!
        sono.gadx = gx;
        sono.gady = gy;
        sono.gadw = gw;
        sono.gadh = gh;
      } else {
        int scw = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
        int sch = (int) java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
      }
      System.out.println("--> Configuration readed from SonogramConfig.xml");
      System.out.println("--> " + sono.filehistory.size() + " hotlistentrys");
    } catch (Throwable t) {
      System.out.println(t);
      System.out.println("--> No Configfile Found. Will create one.");
      // sono.messageBox("Create a new SonogramConfig.xml Configuration File","The Sonogram
      // configuration file SonogramConfig.xml is not available\nwhy Sonogram will now create this
      // File. This is normal if Sonogram starts\nfirst time. All the program options, settings and
      // values are stored there.\nIf Sonogram has any problems while startup simple remove the
      // old\nSonogram.xml file and Sonogram will create a new one with the default\ninitial
      // values.",1);
      successfully = false;
      SaveConfig sc = new SaveConfig(sono);
    }
  }
}
