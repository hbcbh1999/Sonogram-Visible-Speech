package de.dfki.sonogram;

import java.io.*;
import java.net.*;
import javax.media.*;
import javax.media.control.*;
import javax.media.format.*;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>This Class prents an audio player for every media file (video od audiofile) that is supportet
 * by Java Media Framework (JMF)
 *
 * @author Christoph Lauer
 * @version 1.0, Current 26/09/2002
 */
public class PlaySound implements ControllerListener, Runnable {

  protected String movieURL = null;
  protected Player player = null;
  protected GainControl gain = null;
  public Thread timeThread = null;
  private Sonogram reftomain; // Reference to MainWND
  private boolean timeSliderFlag = false;
  private double oldsliderpos = 0.0;
  static boolean oneplayerisrunning = false;
  // ----------------------------------------------------------------------------
  public PlaySound(String url, Sonogram ref) {
    reftomain = ref;
    movieURL = url;
    if (movieURL != null) loadMovie(movieURL);
    timeThread = new Thread(this);
    timeThread.setPriority(Thread.MAX_PRIORITY);
    timeThread.start();
  }
  // ----------------------------------------------------------------------------
  /** Create a player for the URL and realize it. */
  private void loadMovie(String movieURL) {
    // Prepend a "file:" if no protocol is specified
    if (movieURL.indexOf(":") < 3) movieURL = "file:" + movieURL;
    // Try to create a player
    try {
      player = Manager.createPlayer(new MediaLocator(movieURL));
      player.addControllerListener(this);
      player.realize();
    } catch (Exception e) {
      System.out.println("Error creating player");
      return;
    }
  }
  // ----------------------------------------------------------------------------
  public void controllerUpdate(ControllerEvent ce) {
    if (ce instanceof ControllerClosedEvent) {
      stop();
    } else if (ce instanceof EndOfMediaEvent) {
      stop();
    } else if (ce instanceof PrefetchCompleteEvent) {
      // Get the GainControl from the player, if any, to control sound volume
      gain = (GainControl) player.getControl("javax.media.GainControl");
      gain.setDB((float) reftomain.gad.sliderdb.getValue());
      gain.setMute(reftomain.gad.cmute.isSelected());
    } else if (ce instanceof RealizeCompleteEvent) {
    }
  }
  // ----------------------------------------------------------------------------
  public void play() {
    if (oneplayerisrunning == true) return;
    oneplayerisrunning = true;
    oldsliderpos = reftomain.pp.plstart;
    player.start();
    timeSliderFlag = true;
  }
  // ----------------------------------------------------------------------------
  public void stop() {
    timeSliderFlag = false;
    reftomain.pp.plbutton = oldsliderpos;
    reftomain.pp.paintTimeSlider(null, false);
    player.stop();
    oneplayerisrunning = false;
    // When loop is selected.
    if (reftomain.gad.csloop.isSelected() == true
        && reftomain.stopbuttonpressed == false
        && reftomain.playbuttonpressed == true
        && oneplayerisrunning == false) {
      if (reftomain.pp.plstop == reftomain.pp.plstart) reftomain.pp.plstop = 1.0;
      springTo(
          (reftomain.selectedstartold + reftomain.pp.plstart * reftomain.selecedwidthold)
              * (double) reftomain.samplesall
              / (double) reftomain.samplerate);
      play();
      return;
    }
    // When no loop is selected buttons are change back to her default status.
    if (reftomain.stopbuttonpressed == true || reftomain.gad.csloop.isSelected() == false) {
      reftomain.stopbuttonpressed = false;
      reftomain.playbuttonpressed = false;
      reftomain.stopItem.setEnabled(false);
      reftomain.stopbutton.setEnabled(false);
      reftomain.playItem.setEnabled(true);
      reftomain.playbutton.setEnabled(true);
    }
  }
  // ----------------------------------------------------------------------------
  public void close() {
    player.close();
  }
  // ----------------------------------------------------------------------------
  public void springTo(double time) {
    player.setMediaTime(new Time(time));
  }
  // ----------------------------------------------------------------------------
  /** This Thread updates the Sonogram Display while playing */
  public void run() {
    for (; ; ) { // Endless Loop
      if (timeSliderFlag == true) {
        if (player != null) {
          long nano = player.getMediaTime().getNanoseconds();
          long dura = player.getDuration().getNanoseconds();
          if (dura >= 0 && dura < (long) 3 * 3600 * 1000000000L) {
            if (nano <= dura + 1000000) { // 1E6ns=1ms
              reftomain.pp.plbutton =
                  (((double) nano / (double) dura) - reftomain.selectedstartold)
                      / reftomain.selecedwidthold;
              if (reftomain.gad.csspecwhileplaying.isSelected() == true) {
                reftomain.pp.paintOneSpektrum(true);
                reftomain.pp.paintTimeSlider(null, false);
              }
            }
          }
        }
      }
      if (reftomain.pp.plbutton >= reftomain.pp.plstop) { // Stop when out of span
        stop();
      }
      try {
        Thread.currentThread().sleep(10);
      } catch (InterruptedException ie) {
        System.out.println("--> Interupt Exception in Play-Thread");
      }
    } // end of the endless loop
  }
  // ----------------------------------------------------------------------------
}
