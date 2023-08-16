package de.dfki.sonogram;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>This class saves <i>Sonogramm</i> settings in an XML file named SonogramConfig.xml. This class
 * has only the constructor which handles the complette saving.
 *
 * @author Christoph Lauer
 * @version 1.0, Begin 31/07/2002, Current 26/09/2002
 */
public class SaveConfig {
  /** Saves all genaral adjustment settings. */
  public SaveConfig(Sonogram sono) {
    try {
      File f = new File("SonogramConfig.xml");

      if (sono.gad.cssaveconf.isSelected() == false) {
        // Make sure the file or directory exists and isn't write protected
        if (!f.exists()) System.out.println("--> Delete: no such file or directory.");
        if (!f.canWrite()) System.out.println("--> Delete: write protected.");
        // If it is a directory, make sure it is empty
        if (f.isDirectory()) {
          String[] files = f.list();
          if (files.length > 0) System.out.println("--> Delete: directory not empty.");
        }
        // Attempt to delete it
        boolean success = f.delete();
        if (!success) System.out.println("--> Delete: deletion failed.");
        System.out.println("--> Delete: deletion successfully.");
        return;
      }

      PrintWriter out = new PrintWriter(new FileWriter(f));
      out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      out.println("<!-- XML file written by Sonogram application while ending program-->");
      out.println("<!DOCTYPE sonogramconfig SYSTEM \"SonogramConfig.dtd\">");
      out.println("\t<sonogramconfig>");
      if (sono.gad.csavehist.isSelected() == true) { // save only if the save flag in gad is enables
        out.println("\t\t<hotlist>");
        for (int i = 0; i < sono.filehistory.size(); i++) {
          out.println(
              "\t\t\t<hotlist-item number=\""
                  + i
                  + "\" path=\""
                  + sono.filehistory.get(i)
                  + "\"/>");
        }
        out.println("\t\t</hotlist>");
      }

      out.println("\t\t<ScreenPositions>");
      Dimension sd = sono.getSize();
      Point sl = sono.getLocation();
      out.println(
          "\t\t\t<ScreenPositionSonogram x=\""
              + sl.x
              + "\" y=\""
              + sl.y
              + "\" width=\""
              + sd.height
              + "\" heigth=\""
              + sd.width
              + "\"/>");
      Dimension gd = sono.gad.getSize();
      Point gl = sono.gad.getLocation();
      out.println(
          "\t\t\t<ScreenPositionGAD      x=\""
              + gl.x
              + "\" y=\""
              + gl.y
              + "\" width=\""
              + gd.height
              + "\" heigth=\""
              + gd.width
              + "\"/>");
      Dimension cd = sono.cv.getSize();
      Point cl = sono.cv.getLocation();
      out.println(
          "\t\t\t<ScreenPositionCepstrum x=\""
              + cl.x
              + "\" y=\""
              + cl.y
              + "\" width=\""
              + cd.height
              + "\" heigth=\""
              + cd.width
              + "\"/>");
      Dimension ld = sono.lv.getSize();
      Point ll = sono.lv.getLocation();
      out.println(
          "\t\t\t<ScreenPositionLPC      x=\""
              + ll.x
              + "\" y=\""
              + ll.y
              + "\" width=\""
              + ld.height
              + "\" heigth=\""
              + ld.width
              + "\"/>");
      Dimension fd = sono.fv.getSize();
      Point fl = sono.fv.getLocation();
      out.println(
          "\t\t\t<ScreenPositionFourier  x=\""
              + fl.x
              + "\" y=\""
              + fl.y
              + "\" width=\""
              + fd.height
              + "\" heigth=\""
              + fd.width
              + "\"/>");
      Dimension wd = sono.wv.getSize();
      Point wl = sono.wv.getLocation();
      out.println(
          "\t\t\t<ScreenPositionTimeWave x=\""
              + wl.x
              + "\" y=\""
              + wl.y
              + "\" width=\""
              + wd.height
              + "\" heigth=\""
              + wd.width
              + "\"/>");
      Dimension ad = sono.av.getSize();
      Point al = sono.av.getLocation();
      out.println(
          "\t\t\t<ScreenPositionWavelet  x=\""
              + al.x
              + "\" y=\""
              + al.y
              + "\" width=\""
              + ad.height
              + "\" heigth=\""
              + ad.width
              + "\"/>");
      Dimension kd = sono.av.getSize();
      Point kl = sono.av.getLocation();
      out.println(
          "\t\t\t<ScreenPositionAutoCorr  x=\""
              + kl.x
              + "\" y=\""
              + kl.y
              + "\" width=\""
              + kd.height
              + "\" heigth=\""
              + kd.width
              + "\"/>");
      out.println("\t\t</ScreenPositions>");
      out.println("\t\t<GeneralAdjustment>");

      out.println("\t\t\t<InverseColors            value=\"" + sono.gad.cinv.isSelected() + "\"/>");
      out.println("\t\t\t<LogarithmAmplitude       value=\"" + sono.gad.clog.isSelected() + "\"/>");
      out.println(
          "\t\t\t<GridInSonogram           value=\"" + sono.gad.cgrid.isSelected() + "\"/>");
      out.println(
          "\t\t\t<ToolTipInSonogram        value=\"" + sono.gad.ctooltip.isSelected() + "\"/>");
      out.println(
          "\t\t\t<AutomaticalSearchWinLeng value=\"" + sono.gad.cauto.isSelected() + "\"/>");
      out.println(
          "\t\t\t<NormalizeSingleCurve     value=\"" + sono.gad.cback.isSelected() + "\"/>");
      out.println(
          "\t\t\t<SmoothOverFrequency      value=\"" + sono.gad.csmooth.isSelected() + "\"/>");
      out.println(
          "\t\t\t<SmoothSingleCurve        value=\"" + sono.gad.csmoothsi.isSelected() + "\"/>");
      out.println(
          "\t\t\t<ShowEnergyTimeline       value=\"" + sono.gad.cenergy.isSelected() + "\"/>");
      out.println(
          "\t\t\t<LogarithmInLPC           value=\"" + sono.gad.cllog.isSelected() + "\"/>");
      out.println(
          "\t\t\t<LogarithmInFourier       value=\"" + sono.gad.clfor.isSelected() + "\"/>");
      out.println(
          "\t\t\t<OpenWith8Khz             value=\"" + sono.gad.csampl.isSelected() + "\"/>");
      out.println(
          "\t\t\t<SmoothOverTime           value=\"" + sono.gad.csmoothx.isSelected() + "\"/>");
      out.println(
          "\t\t\t<EnablePitchDetection     value=\"" + sono.gad.cspitch.isSelected() + "\"/>");
      out.println(
          "\t\t\t<PitchFrequencyLimitation value=\""
              + sono.gad.cspitchlimitation.isSelected()
              + "\"/>");
      out.println(
          "\t\t\t<EnableOverlapping        value=\"" + sono.gad.coverlapping.isSelected() + "\"/>");
      out.println(
          "\t\t\t<SaveHistoryList          value=\"" + sono.gad.csavehist.isSelected() + "\"/>");
      out.println(
          "\t\t\t<FogOverSonoWhilePitch    value=\"" + sono.gad.cspitchonely.isSelected() + "\"/>");
      out.println(
          "\t\t\t<BlackPitchLine           value=\"" + sono.gad.cspitchblack.isSelected() + "\"/>");
      out.println(
          "\t\t\t<GeneratePitchWithSmooth  value=\""
              + sono.gad.cspitchsmooth.isSelected()
              + "\"/>");
      out.println(
          "\t\t\t<ShowSpectrumWhilePlaying value=\""
              + sono.gad.csspecwhileplaying.isSelected()
              + "\"/>");
      out.println(
          "\t\t\t<LoopPlaying              value=\"" + sono.gad.csloop.isSelected() + "\"/>");
      out.println(
          "\t\t\t<MonochromSingleCurve     value=\"" + sono.gad.cscolsi.isSelected() + "\"/>");
      out.println(
          "\t\t\t<LogarithmFrequency       value=\"" + sono.gad.cslogfr.isSelected() + "\"/>");
      out.println(
          "\t\t\t<SaveScreenPositions      value=\""
              + sono.gad.csavescreepos.isSelected()
              + "\"/>");
      out.println(
          "\t\t\t<SaveConfigurationAtEnd   value=\"" + sono.gad.cssaveconf.isSelected() + "\"/>");
      out.println("\t\t\t<UseFftForTransformation  value=\"" + sono.gad.rfft.isSelected() + "\"/>");
      out.println("\t\t\t<CepstrumLogarithm        value=\"" + sono.gad.ccep.isSelected() + "\"/>");
      out.println(
          "\t\t\t<WaveLines                value=\"" + sono.gad.cwavelines.isSelected() + "\"/>");
      out.println(
          "\t\t\t<MuteSound                value=\"" + sono.gad.cmute.isSelected() + "\"/>");
      out.println(
          "\t\t\t<SearchForLocalPeak       value=\"" + sono.gad.clocalpeak.isSelected() + "\"/>");
      out.println(
          "\t\t\t<ThreeDimUniverse         value=\"" + sono.gad.cuniverse.isSelected() + "\"/>");
      out.println(
          "\t\t\t<ConnectAutocorrPoints    value=\"" + sono.gad.cacpoints.isSelected() + "\"/>");
      out.println(
          "\t\t\t<AutocorrSmooth           value=\"" + sono.gad.cacsmooth.isSelected() + "\"/>");
      out.println(
          "\t\t\t<AutocorrPitch            value=\"" + sono.gad.cacpitch.isSelected() + "\"/>");
      out.println(
          "\t\t\t<AntialiseAnalyseWins     value=\"" + sono.gad.cantialise.isSelected() + "\"/>");
      out.println(
          "\t\t\t<AntialiseConfirmed       value=\"" + sono.gad.cperantialiasconfirm + "\"/>");
      out.println("\t\t\t<WvConfirmed              value=\"" + sono.wvconfig.wvmessage + "\"/>");
      out.println(
          "\t\t\t<NormalizeWaveForm        value=\"" + sono.gad.cwfnorm.isSelected() + "\"/>");
      out.println(
          "\t\t\t<CepstrumSmooth           value=\"" + sono.gad.ccepsmooth.isSelected() + "\"/>");
      out.println(
          "\t\t\t<PerspectogramRotation    value=\"" + sono.gad.cscrennsaver.isSelected() + "\"/>");
      out.println(
          "\t\t\t<PerspectogramAntialias   value=\""
              + sono.gad.cperantialias.isSelected()
              + "\"/>");
      out.println(
          "\t\t\t<PerspectogramCoordSystem value=\"" + sono.gad.cpercoord.isSelected() + "\"/>");
      out.println(
          "\t\t\t<OpenLastFile             value=\"" + sono.gad.csopenlast.isSelected() + "\"/>");
      out.println(
          "\t\t\t<LastFileWithZoom         value=\""
              + sono.gad.copenlastwithzoom.isSelected()
              + "\"/>");
      out.println(
          "\t\t\t<ArrangeAfterAutoStart    value=\"" + sono.gad.csarrange.isSelected() + "\"/>");
      out.println(
          "\t\t\t<ACPitchConLines          value=\"" + sono.gad.cppoints.isSelected() + "\"/>");
      out.println(
          "\t\t\t<ACPitchSmooth            value=\"" + sono.gad.cpsmooth.isSelected() + "\"/>");
      out.println(
          "\t\t\t<ACPitchRemSilent         value=\"" + sono.gad.cplimitfr.isSelected() + "\"/>");
      out.println(
          "\t\t\t<ACPitchRemoveRunaways    value=\"" + sono.gad.cpraway.isSelected() + "\"/>");
      out.println(
          "\t\t\t<ACPitchTrackMove         value=\"" + sono.gad.cptrack.isSelected() + "\"/>");
      out.println(
          "\t\t\t<ACPitchTrackFog          value=\"" + sono.gad.cpfog.isSelected() + "\"/>");
      out.println("\t\t\t<PolygonWarning           value=\"" + sono.i3dpolygons + "\"/>");
      out.println("\t\t\t<Mp3Confirm               value=\"" + sono.mp3confirm + "\"/>");
      out.println("\t\t\t<PerKeysConfirm           value=\"" + sono.perkeysconfirm + "\"/>");
      out.println("\t\t\t<RecorderLedColors        value=\"" + sono.la.tp.colorSwitch + "\"/>");

      if (sono.spektrumExist == true)
        out.println("\t\t\t<LastOpenedFile           value=\"" + sono.filepath + "\"/>");
      out.println("\t\t\t<LookAndFeel              value=\"" + sono.ilaf + "\"/>");
      out.println(
          "\t\t\t<SliderLoudness           value=\"" + sono.gad.sliderdb.getValue() + "\"/>");
      out.println(
          "\t\t\t<SliderWaveTime           value=\"" + sono.gad.sliderwave.getValue() + "\"/>");
      out.println(
          "\t\t\t<SliderCepstrumBuffer     value=\"" + sono.gad.slidercep.getValue() + "\"/>");
      out.println(
          "\t\t\t<SliderWindowSize         value=\"" + sono.gad.sliderwinsize.getValue() + "\"/>");
      out.println(
          "\t\t\t<SliderWindowFunktion     value=\""
              + sono.gad.sliderwinfunktion.getValue()
              + "\"/>");
      out.println(
          "\t\t\t<SliderOverlapping        value=\"" + sono.gad.sliderwinspeed.getValue() + "\"/>");
      out.println(
          "\t\t\t<SliderSurfaceDataReduct  value=\"" + sono.gad.slidersurface.getValue() + "\"/>");
      out.println(
          "\t\t\t<SliderFourierFraction    value=\"" + sono.gad.sliderformant.getValue() + "\"/>");
      out.println(
          "\t\t\t<SliderFourierLen         value=\""
              + sono.gad.sliderformantlen.getValue()
              + "\"/>");
      out.println(
          "\t\t\t<SliderLpcCoefficients    value=\"" + sono.gad.sliderlpccoef.getValue() + "\"/>");
      out.println(
          "\t\t\t<SliderLpcFftNums         value=\""
              + sono.gad.sliderlpcfftnum.getValue()
              + "\"/>");
      out.println(
          "\t\t\t<SliderLpcSamplesFutur    value=\""
              + sono.gad.sliderlpcsamfutur.getValue()
              + "\"/>");
      out.println(
          "\t\t\t<SliderLpcFrequencyFrac   value=\"" + sono.gad.sliderlpcfre.getValue() + "\"/>");
      out.println(
          "\t\t\t<SliderSurfaceY           value=\"" + sono.gad.slidersurfacey.getValue() + "\"/>");
      out.println(
          "\t\t\t<SliderSurfaceX           value=\"" + sono.gad.slidersurfacex.getValue() + "\"/>");
      out.println(
          "\t\t\t<SliderPitchFrequency     value=\"" + sono.gad.sliderpitch.getValue() + "\"/>");
      out.println(
          "\t\t\t<SliderLogarithmAmp       value=\"" + sono.gad.sliderlog.getValue() + "\"/>");
      out.println(
          "\t\t\t<SliderAutoCorrWinLen     value=\""
              + sono.gad.slideracwinlength.getValue()
              + "\"/>");
      out.println(
          "\t\t\t<SliderAutoCorrWinShift   value=\""
              + sono.gad.slideracwinshift.getValue()
              + "\"/>");
      out.println(
          "\t\t\t<SliderACPitchWinShift    value=\""
              + sono.gad.sliderpwinshift.getValue()
              + "\"/>");
      out.println(
          "\t\t\t<SliderACPitchWinLength   value=\""
              + sono.gad.sliderpwinlength.getValue()
              + "\"/>");
      out.println(
          "\t\t\t<SliderACPitchMaxFrequ    value=\"" + sono.gad.sliderpitchmax.getValue() + "\"/>");
      int color = 0;
      if (sono.gad.r1.isSelected() == true) color = 1;
      if (sono.gad.r2.isSelected() == true) color = 2;
      if (sono.gad.r3.isSelected() == true) color = 3;
      if (sono.gad.r4.isSelected() == true) color = 4;
      if (sono.gad.r5.isSelected() == true) color = 5;
      out.println("\t\t\t<SelectedColor            value=\"" + color + "\"/>");

      int ren = 3;
      if (sono.gad.s1.isSelected() == true) ren = 1;
      if (sono.gad.s2.isSelected() == true) ren = 2;
      out.println("\t\t\t<SurfaceRendering         value=\"" + ren + "\"/>");
      out.println("\t\t\t<BgColorForSurface        value=\"" + sono.gad.bgcol + "\"/>");
      out.println(
          "\t\t\t<SelectedWavelet          value=\"" + sono.gad.wcb.getSelectedIndex() + "\"/>");
      out.println(
          "\t\t\t<WaveletOctave            value=\"" + sono.gad.sliderwaltl.getValue() + "\"/>");
      out.println(
          "\t\t\t<WaveletLogarithm         value=\"" + sono.gad.clogwal.isSelected() + "\"/>");

      out.println("\t\t</GeneralAdjustment>");
      out.println("\t\t<ZoomHistory>");
      out.println("\t\t\t<SelectedStartCurrent     value=\"" + sono.selectedstartold + "\"/>");
      out.println("\t\t\t<SelectedWidthCurrent     value=\"" + sono.selecedwidthold + "\"/>");
      out.println("\t\t\t<ZoomPreviousIndex        value=\"" + sono.zoompreviousindex + "\"/>");
      for (int i = 0; i < sono.zoompreviousindex; i++) {
        out.println(
            "\t\t\t<SelectedStartPrevious    number=\""
                + i
                + "\" value=\""
                + sono.selectedstartpre.get(i)
                + "\"/>");
        out.println(
            "\t\t\t<SelectedWidthPrevious    number=\""
                + i
                + "\" value=\""
                + sono.selectedwidthpre.get(i)
                + "\"/>");
      }
      out.println("\t\t</ZoomHistory>");
      out.println("\t</sonogramconfig>");
      out.close();
      System.out.println("--> Configuration saved in SonogramConfig.xml");
      System.out.println("--> " + sono.filehistory.size() + " hotlistitems saved");
      sono.chooser.saveDirectoryEntries();
    } catch (Exception e) {
      System.out.println("--> Error occured while writing the config fIle .");
    }
  }
}
// -------------------------------------------------------------------------------------------------------------------------
