package de.dfki.sonogram;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Vector;
import javax.media.*;
import javax.media.datasink.*;
import javax.media.format.*;
import javax.media.protocol.*;
import javax.swing.JOptionPane;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>This Class open a URL (i.e. file:c:\...) and gives the Sample-Information for Audio-Datas
 * back.
 *
 * @author Christoph Lauer
 * @version 1.0, Current 26/09/2002
 */
public class DataSourceReader implements ControllerListener, DataSinkListener {

  Processor p;
  Object waitSync = new Object();
  boolean stateTransitionOK = true;
  public Vector audioStream = new Vector();
  public boolean openAllRightFlag = false;
  private Sonogram reftomain;
  double duration;
  public String dstype;
  boolean cancelIsPressed = false;
  boolean error = false;
  int samplerate = 0;
  // ---------------------------------------------------------------------------------------------------
  public void setMainRef(Sonogram ref) {
    reftomain = ref;
  }
  // ---------------------------------------------------------------------------------------------------
  /**
   * Given a DataSource, create a processor and hook up the output DataSource from the processor to
   * a customed DataSink.
   */
  public boolean open(DataSource ds) {
    //  	try{
    // remove poss. old Elements
    audioStream.removeAllElements();
    dstype = ds.getContentType();
    System.out.println("--> Create JMF-PROCESSOR; Data type = " + dstype);
    // Create PROCESSOR
    try {
      p = Manager.createProcessor(ds);
    } catch (Exception e) {
      p = null;
      reftomain.progmon.close();
      reftomain.messageBox(
          "Cannot recognize this Media File",
          "<html>Sonogram cannot recognize this file<br>as <u>valid Audio or Video</u>"
              + " file.<br><br>File: <u>"
              + reftomain.filename
              + "</u><br><i>Recognised Type: <u><font color=#880000 size=4>"
              + dstype,
          JOptionPane.WARNING_MESSAGE);
      System.err.println("--> ERROR: create PROCESSOR: " + e);
      reftomain.spektrumExist = false;
      reftomain.updateimageflag = true;
      reftomain.repaint();
      reftomain.setTitle("Sonogram Visible Speech - version " + reftomain.version);
      return false;
    }
    reftomain.progmon.setProgress(6);
    p.addControllerListener(this);
    // Put the Processor into configured state.
    p.configure();
    if (!waitForState(p.Configured)) {
      reftomain.progmon.close();
      reftomain.messageBox(
          "Cannot read the samples from Media File",
          "<html>Sonogram can recognize this Media File but <u>cannot read out the"
              + " samples</u><br>from this Media File. Perhaps the Media File is"
              + " Broken.<br><br>File: <u>"
              + reftomain.filename
              + "</u><br><i>Recognised Type: <u><font color=#880000 size=4>"
              + dstype,
          JOptionPane.WARNING_MESSAGE);
      System.err.println("--> Error: configure the JMF-PROCESSORS");
      reftomain.spektrumExist = false;
      reftomain.setTitle("Sonogram Visible Speech - version " + reftomain.version);
      reftomain.updateimageflag = true;
      reftomain.repaint();
      return false;
    }
    reftomain.progmon.setProgress(7);
    // Hier wird das Ausgabeformat des PROCESSORS Spezifiziert
    javax.media.control.TrackControl traCont[] = p.getTrackControls();
    for (int i = 0; i < traCont.length; i++) {
      if (traCont[i].getFormat() instanceof AudioFormat) {
        AudioFormat af = (AudioFormat) traCont[i].getFormat();
        // Set selected Samplingrate
        System.out.println("--> Files orginal audio format:" + af);
        if (reftomain.gad.csampl.isSelected() == false) samplerate = (int) af.getSampleRate();
        else samplerate = 8000;
        traCont[i].setFormat(new AudioFormat("LINEAR", samplerate, 8, 1, 0, 1));
        AudioFormat newaf = (AudioFormat) traCont[i].getFormat();
        System.out.println("--> Format processed in:" + newaf);
        System.out.println("--> Samplerate=" + samplerate);
        reftomain.samplerate = samplerate;
      }
    }
    reftomain.progmon.setProgress(8);
    // Get the raw output from the processor.
    p.setContentDescriptor(new ContentDescriptor(ContentDescriptor.RAW));
    p.realize();
    if (!waitForState(p.Realized)) {
      reftomain.progmon.close();
      reftomain.messageBox(
          "Connot convert Samples from Media File",
          "<html>The JMF-Processor <u>cannot convert this file</u> to specific format !",
          JOptionPane.WARNING_MESSAGE);
      System.err.println("--> ERROR: the JMF-PROCESSOR cant convert this file");
      reftomain.setTitle("Sonogram Visible Speech - version " + reftomain.version);
      reftomain.spektrumExist = false;
      reftomain.updateimageflag = true;
      reftomain.repaint();
      p = null;
      return false;
    }
    reftomain.progmon.setProgress(9);
    // Get the output DataSource from the processor and
    // hook it up to the DataSourceHandler.
    DataSource ods = p.getDataOutput();
    DataSourceHandler handler = new DataSourceHandler();
    try {
      handler.setSource(ods);
    } catch (IncompatibleSourceException e) {
      reftomain.progmon.close();
      reftomain.messageBox(
          "Cannot hadle Datasource",
          "<html>The JMF-Processot <u>cannot handel the datasource</u> !",
          JOptionPane.WARNING_MESSAGE);
      reftomain.setTitle("Sonogram Visible Speech - version " + reftomain.version);
      System.err.println("--> ERROR: the JMF-PROCESSOR cant handel this datasource: " + ods);
      reftomain.spektrumExist = false;
      reftomain.updateimageflag = true;
      reftomain.repaint();
      return false;
    }
    reftomain.progmon.setProgress(10);
    handler.addDataSinkListener(this);
    handler.start();
    // Prefetch the processor.
    p.prefetch();
    if (!waitForState(p.Prefetched)) {
      reftomain.progmon.close();
      reftomain.messageBox(
          "Cannot Prefetch Audio Date",
          "<html>The JMF-Processor <u>cannot prefetch the audio data</u> !",
          JOptionPane.WARNING_MESSAGE);
      System.err.println("--> ERROR: prefetch audio data with JMF-PROCESSOR");
      reftomain.setTitle("Sonogram Visible Speech - version " + reftomain.version);
      reftomain.spektrumExist = false;
      reftomain.updateimageflag = true;
      reftomain.repaint();
      return false;
    }
    reftomain.progmon.setProgress(15);
    // Start the processor.
    System.err.println("--> Moment Please, reading Stream");
    duration = p.getDuration().getSeconds();
    System.out.println("--> Duration:" + duration + "secs.");
    p.start();
    // 	} catch (Throwable e) {
    // 	    reftomain.progmon.close();
    // 	    reftomain.messageBox("Open File","General Error while open and read file !\nSee Console
    // for details.\nPossibly opening will continue!",JOptionPane.WARNING_MESSAGE);
    // 	    System.err.println("--> Error while reading Samples out." + e);
    // 	    reftomain.setTitle("Sonogram 1.6 - visible-speech");
    // 	    reftomain.spektrumExist = false;
    // 	    reftomain.updateimageflag=true;
    // 	    reftomain.repaint();
    // 	    reftomain.progmon.close();
    // 	    return false;
    // 	}
    return true;
  }
  // ---------------------------------------------------------------------------------------------------
  public void addNotify() {}
  // ---------------------------------------------------------------------------------------------------
  /**
   * Change the plugin list to disable the default RawBufferMux thus allowing the RawSyncBufferMux
   * to be used. This is a handy trick. You wouldn't know this, would you? :)
   */
  void enableSyncMux() {
    Vector muxes = PlugInManager.getPlugInList(null, null, PlugInManager.MULTIPLEXER);
    for (int i = 0; i < muxes.size(); i++) {
      String cname = (String) muxes.elementAt(i);
      if (cname.equals("com.sun.media.multiplexer.RawBufferMux")) {
        muxes.removeElementAt(i);
        break;
      }
    }
    PlugInManager.setPlugInList(muxes, PlugInManager.MULTIPLEXER);
  }
  // ---------------------------------------------------------------------------------------------------
  /**
   * Block until the processor has transitioned to the given state. Return false if the transition
   * failed.
   */
  boolean waitForState(int state) {
    synchronized (waitSync) {
      try {
        while (p.getState() < state && stateTransitionOK) waitSync.wait();
      } catch (Exception e) {
        reftomain.progmon.close();
        reftomain.messageBox(
            "Error in the JMF Transition",
            "<html>Error <u>while JMF-Processor Transition</u> !",
            JOptionPane.WARNING_MESSAGE);
        System.out.println("--> ERROR while JMF transition");
      }
    }
    return stateTransitionOK;
  }
  // ---------------------------------------------------------------------------------------------------
  /** Controller Listener. */
  public void controllerUpdate(ControllerEvent evt) {

    if (evt instanceof ConfigureCompleteEvent
        || evt instanceof RealizeCompleteEvent
        || evt instanceof PrefetchCompleteEvent) {
      synchronized (waitSync) {
        stateTransitionOK = true;
        waitSync.notifyAll();
      }
    } else if (evt instanceof ResourceUnavailableEvent) {
      synchronized (waitSync) {
        stateTransitionOK = false;
        waitSync.notifyAll();
      }
    } else if (evt instanceof EndOfMediaEvent) {
      p.close();
    } else if (evt instanceof SizeChangeEvent) {
    }
  }
  // ---------------------------------------------------------------------------------------------------
  /** DataSink Listener */
  public void dataSinkUpdate(DataSinkEvent evt) {

    if (evt instanceof EndOfStreamEvent) {

      if (cancelIsPressed == true) {
        openAllRightFlag = false;
        return;
      }
      openAllRightFlag = true;
      System.err.println("--> Stream ausgelesen: " + audioStream.size() + " Samples");
      evt.getSourceDataSink().close();
      if (audioStream.size() == 0) {
        reftomain.progmon.close();
        reftomain.messageBox(
            "No Audio Track found",
            "<html>This Media File <u>does not contain any Audiotrack</u> !",
            JOptionPane.WARNING_MESSAGE);
        reftomain.updateimageflag = true;
        reftomain.spektrumExist = false;
        reftomain.repaint();
        return;
      }
      if (reftomain.autoopened == false) { // if file is not autoopened show in full Span
        reftomain.selectedstart = 0.0;
        reftomain.selecedwidth = 1.0;
        reftomain.zoompreviousindex = 0;
      } else reftomain.autoopened = false; // else show in stored width an reset Flag to false
      reftomain.progmon.setProgress(100);
      reftomain.progmon.close();
      reftomain.readerIsBack();
    }
  }
  // ---------------------------------------------------------------------------------------------------
  // Konvertiert eine Byte Variable bitweise in eine Integer Variable (Vorzeichen,bits....)
  private synchronized int copyByteToIntBitwise(byte b) {
    int i = 0;
    if (b > 0) i = b;
    if (b < 0) {
      i = ~b;
      i += 128;
    }
    return i;
  }
  // ====================================================================================================
  /***************************************************
   * Inner class
   ***************************************************/
  /**
   * This DataSourceHandler class reads from a DataSource and display information of each frame of
   * data received.
   */
  // ---------------------------------------------------------------------------------------------------
  class DataSourceHandler implements DataSink, BufferTransferHandler {
    DataSource source;
    PullBufferStream pullStrms[] = null;
    PushBufferStream pushStrms[] = null;
    // Data sink listeners.
    private Vector listeners = new Vector(1);
    // Stored all the streams that are not yet finished (i.e. EOM
    // has not been received.
    SourceStream unfinishedStrms[] = null;
    // Loop threads to pull data from a PullBufferDataSource.
    // There is one thread per each PullSourceStream.
    Loop loops[] = null;
    Buffer readBuffer;
    // ---------------------------------------------------------------------------------------------------
    /** Sets the media source this <code>MediaHandler</code> should use to obtain content. */
    public void setSource(DataSource source) throws IncompatibleSourceException {

      // Different types of DataSources need to handled differently.
      if (source instanceof PushBufferDataSource) {
        pushStrms = ((PushBufferDataSource) source).getStreams();
        unfinishedStrms = new SourceStream[pushStrms.length];
        // Set the transfer handler to receive pushed data from
        // the push DataSource.
        for (int i = 0; i < pushStrms.length; i++) {
          pushStrms[i].setTransferHandler(this);
          unfinishedStrms[i] = pushStrms[i];
        }
      } else if (source instanceof PullBufferDataSource) {
        pullStrms = ((PullBufferDataSource) source).getStreams();
        unfinishedStrms = new SourceStream[pullStrms.length];
        // For pull data sources, we'll start a thread per
        // stream to pull data from the source.
        loops = new Loop[pullStrms.length];
        for (int i = 0; i < pullStrms.length; i++) {
          loops[i] = new Loop(this, pullStrms[i]);
          unfinishedStrms[i] = pullStrms[i];
        }
      } else {
        // This handler only handles push or pull buffer datasource.
        throw new IncompatibleSourceException();
      }
      this.source = source;
      readBuffer = new Buffer();
    }
    // ---------------------------------------------------------------------------------------------------
    /** For completeness, DataSink's require this method. But we don't need it. */
    public void setOutputLocator(MediaLocator ml) {}
    // ---------------------------------------------------------------------------------------------------
    public MediaLocator getOutputLocator() {
      return null;
    }
    // ---------------------------------------------------------------------------------------------------
    public String getContentType() {
      return source.getContentType();
    }
    // ---------------------------------------------------------------------------------------------------
    /** Our DataSink does not need to be opened. */
    public void open() {}
    // ---------------------------------------------------------------------------------------------------
    public void start() {
      try {
        source.start();
      } catch (IOException e) {
        reftomain.progmon.close();
        System.err.println(e);
        reftomain.messageBox(
            "Error while starting the JFM-Processor",
            "<html>Error while <u>starting the source</u> in the JMF-Processor",
            JOptionPane.WARNING_MESSAGE);
        System.out.println("--> ERROR while start source in JMF-PROCESSOR");
      }
      // Start the processing loop if we are dealing with a
      // PullBufferDataSource.
      if (loops != null) {
        for (int i = 0; i < loops.length; i++) {
          loops[i].restart();
        }
      }
    }
    // ---------------------------------------------------------------------------------------------------
    public void stop() {
      try {
        source.stop();
      } catch (IOException e) {
        reftomain.progmon.close();
        reftomain.messageBox(
            "Error while stopping the JMF-Processor",
            "<html>Error while <u>stopping the source</u> in the JMF-Processor",
            JOptionPane.WARNING_MESSAGE);
        System.out.println("--> ERROR while stop source in JMF-PROCESSOR");
        System.err.println(e);
      }
      // Start the processing loop if we are dealing with a
      // PullBufferDataSource.
      if (loops != null) {
        for (int i = 0; i < loops.length; i++) loops[i].pause();
      }
    }
    // ---------------------------------------------------------------------------------------------------
    public void close() {
      stop();
      if (loops != null) {
        for (int i = 0; i < loops.length; i++) loops[i].kill();
      }
    }
    // ---------------------------------------------------------------------------------------------------
    public void addDataSinkListener(DataSinkListener dsl) {
      if (dsl != null) if (!listeners.contains(dsl)) listeners.addElement(dsl);
    }
    // ---------------------------------------------------------------------------------------------------
    public void removeDataSinkListener(DataSinkListener dsl) {
      if (dsl != null) listeners.removeElement(dsl);
    }
    // ---------------------------------------------------------------------------------------------------
    protected void sendEvent(DataSinkEvent event) {
      if (!listeners.isEmpty()) {
        synchronized (listeners) {
          Enumeration list = listeners.elements();
          while (list.hasMoreElements()) {
            DataSinkListener listener = (DataSinkListener) list.nextElement();
            listener.dataSinkUpdate(event);
          }
        }
      }
    }
    // ---------------------------------------------------------------------------------------------------
    /** This will get called when there's data pushed from the PushBufferDataSource. */
    public synchronized void transferData(PushBufferStream stream) {
      try {
        stream.read(readBuffer);
      } catch (IOException e) {
        reftomain.progmon.close();
        System.err.println(e);
        sendEvent(new DataSinkErrorEvent(this, e.getMessage()));
        reftomain.messageBox(
            "JMF read Buffer",
            "Error while read buffer by JMF-Processor\nin funktion transferData.",
            JOptionPane.WARNING_MESSAGE);
        System.out.println("--> ERROR while read buffer by JMF-PROCESSOR");
        return;
      }
      printDataInfo(readBuffer);
      // Check to see if we are done with all the streams.
      if (readBuffer.isEOM() && checkDone(stream)) {
        sendEvent(new EndOfStreamEvent(this));
      }
    }
    // ---------------------------------------------------------------------------------------------------
    /** This is called from the Loop thread to pull data from the PullBufferStream. */
    public synchronized boolean readPullData(PullBufferStream stream) {
      try {
        stream.read(readBuffer);
      } catch (IOException e) {
        reftomain.progmon.close();
        System.err.println(e);
        reftomain.messageBox(
            "Error while read the Buffer",
            "<html>Error while <u>read the Buffer</u> in the JMF-Processor",
            JOptionPane.WARNING_MESSAGE);
        System.out.println("--> ERROR while read buffer by JMF-PROCESSOR");
        return true;
      }
      printDataInfo(readBuffer);
      if (readBuffer.isEOM()) {
        // Check to see if we are done with all the streams.
        if (checkDone(stream)) {
          System.err.println("All done!");
          close();
        }
        return true;
      }
      return false;
    }
    // ---------------------------------------------------------------------------------------------------
    /** Check to see if all the streams are processed. */
    public boolean checkDone(SourceStream strm) {
      boolean done = true;

      for (int i = 0; i < unfinishedStrms.length; i++) {
        if (strm == unfinishedStrms[i]) unfinishedStrms[i] = null;
        else if (unfinishedStrms[i] != null) {
          // There's at least one stream that's not done.
          done = false;
        }
      }
      return done;
    }
    // ---------------------------------------------------------------------------------------------------
    /** Writes Data in on Vektor */
    void printDataInfo(Buffer buffer) {
      try {
        if (buffer.getFormat() instanceof AudioFormat) {
          byte[] buf = (byte[]) buffer.getData();
          if (reftomain.progmon.isCanceled() == true) {
            System.out.println("--> CANCEL Button is pressed while read Samples from File.");
            reftomain.progmon.close();
            reftomain.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            reftomain.setTitle("Sonogram Visible Speech - version " + reftomain.version);
            reftomain.spektrumExist = false;
            reftomain.updateimageflag = true;
            reftomain.repaint();
            cancelIsPressed = true;
            p.stop();
            p.close();
            return;
          }
          for (int i = 0; i < (buffer.getLength()); i++) { // Copy Data To Buffer
            try {
              audioStream.addElement(Byte.valueOf(buf[i]));
            } catch (Throwable t) {
              if (error == true) return;
              error = true;
              reftomain.messageBox(
                  "Error while reading the Samples",
                  "<html>Error while <u>reading the Samples from the File</u>! <br>Some Samples are"
                      + " failed or missed !",
                  1);
            }
          }
          int pr =
              (int) (double) (audioStream.size() / (double) samplerate / (double) duration * 85.0);
          if (pr < 100) {
            reftomain.progmon.setProgress(15 + pr);
          }
        }
      } catch (Exception e) {
        System.err.println("--> Error while reading Samples out." + e);
        e.printStackTrace();
        reftomain.messageBox(
            "Error while reading the Samples",
            "<html>A General Error occoured while <u>reading Samples in the JMF-Processor</u> !",
            JOptionPane.WARNING_MESSAGE);
        reftomain.spektrumExist = false;
        reftomain.updateimageflag = true;
        reftomain.repaint();
        reftomain.progmon.close();
        return;
      }
    }
    // ---------------------------------------------------------------------------------------------------
    public Object[] getControls() {
      return new Object[0];
    }
    // ---------------------------------------------------------------------------------------------------
    public Object getControl(String name) {
      return null;
    }
    // ---------------------------------------------------------------------------------------------------
  }
  // ====================================================================================================
  // ====================================================================================================
  /**
   * A thread class to implement a processing loop. This loop reads data from a
   * PullBufferDataSource.
   */
  // ---------------------------------------------------------------------------------------------------
  class Loop extends Thread {

    DataSourceHandler handler;
    PullBufferStream stream;
    boolean paused = true;
    boolean killed = false;
    // ---------------------------------------------------------------------------------------------------
    public Loop(DataSourceHandler handler, PullBufferStream stream) {
      this.handler = handler;
      this.stream = stream;
      start();
    }
    // ---------------------------------------------------------------------------------------------------
    public synchronized void restart() {
      paused = false;
      notify();
    }
    // ---------------------------------------------------------------------------------------------------
    /** This is the correct way to pause a thread; unlike suspend. */
    public synchronized void pause() {
      paused = true;
    }
    // ---------------------------------------------------------------------------------------------------
    /** This is the correct way to kill a thread; unlike stop. */
    public synchronized void kill() {
      killed = true;
      notify();
    }
    // ---------------------------------------------------------------------------------------------------
    /** This is the processing loop to pull data from a PullBufferDataSource. */
    public void run() {
      while (!killed) {
        try {
          while (paused && !killed) {
            wait();
          }
        } catch (InterruptedException e) {
        }

        if (!killed) {
          boolean done = handler.readPullData(stream);
          if (done) pause();
        }
      }
    }
    // ---------------------------------------------------------------------------------------------------
  }
  // ====================================================================================================

  public void generateSamplesFromURL(String url) {

    // url="http://github.com/Christoph-Lauer/Sonogram/blob/main/build/samples/11.wav";
    System.out.println("--> DataSourceReader.generateSamplesFromUrl: " + url);
    // Find out URL type
    int urltype = 0; // 0=error, 1=local file system, 2=https, 3=http, 4=ftp
    if (url.substring(0, 4).equals("file") == true) {
      urltype = 1;
      System.out.println("--> File on local file system");
    } else if (url.substring(0, 5).equals("https") == true) {
      urltype = 3;
      System.out.println("--> File on https file system");
    } else if (url.substring(0, 4).equals("http") == true) {
      urltype = 2;
      System.out.println("--> File on http file system");
    } else if (url.substring(0, 3).equals("ftp") == true) {
      urltype = 4;
      System.out.println("--> File on ftp file system");
    } else {
      reftomain.messageBox(
          "Error while open the URL", "<html>This is not a valid URL: <u>" + url, 2);
      return;
    }

    // Build specific Progressmonitor
    if (urltype == 1) {
      reftomain.progmon =
          new SonoProgressMonitor(
              reftomain, "Open File.", "Reading samples from local file system.", 0, 100);
    }
    if (urltype == 2) {
      reftomain.progmon =
          new SonoProgressMonitor(
              reftomain,
              "Open File",
              "Reading samples from remote file via HTTP protocol.",
              0,
              100);
    }
    if (urltype == 3) {
      reftomain.progmon =
          new SonoProgressMonitor(
              reftomain,
              "Open File",
              "Reading samples from remote file via HTTPS protocol.",
              0,
              100);
    }
    if (urltype == 4) {
      reftomain.progmon =
          new SonoProgressMonitor(
              reftomain,
              "Open File.",
              "Reading samples from remote file via FTP protocol.",
              0,
              100);
    }
    reftomain.progmon.setProgress(1);
    String filename = "";
    try {
      filename = (new java.io.File((new URL(url)).getFile()).getName());
    } catch (Throwable t) {
    }
    reftomain.filename = filename;
    if (reftomain.fileisfromurl == true)
      reftomain.progmon.setNote(
          "Read out the Samples from the File  «" + filename + "»  over the Network");
    else
      reftomain.progmon.setNote(
          "Read out the Samples from the File  «" + filename + "»  on the local file system");
    // Medialocator from URL
    MediaLocator ml;
    if ((ml = new MediaLocator(url)) == null) {
      reftomain.progmon.close();
      reftomain.messageBox(
          "Error from Medialocator",
          "<html><u>Cannot build the MediaLocator",
          JOptionPane.WARNING_MESSAGE);
      System.err.println("--> ERROR: Cant Build MediaLocator: " + url);
      reftomain.setTitle("Sonogram Visible Speech - version " + reftomain.version);
      reftomain.spektrumExist = false;
      reftomain.updateimageflag = true;
      reftomain.repaint();
      return;
    }

    System.out.println("--> PROTOCOLL:" + ml.getProtocol());

    reftomain.progmon.setProgress(2);
    DataSource ds = null;
    // Create a DataSource given the media locator.
    try {
      ds = Manager.createDataSource(ml);
    } catch (Exception e) {
      reftomain.progmon.close();
      if (urltype == 1)
        reftomain.messageBox(
            "Error while open the Local File",
            "<html><i>The File on the local file system <u>does not exist</u> !<br><br></i>File:"
                + " <u>"
                + url,
            JOptionPane.WARNING_MESSAGE);
      if (urltype == 2)
        reftomain.messageBox(
            "Error while open the Network File",
            "<html><i>The remote http <u>file does not exist</u> ! Perhaps the<br>internet"
                + " connection was broken, or you have <br>prescribed you with in the URL"
                + " ?<br><br><font size=-3>"
                + e.getMessage()
                + "</font><br><br></i>URL: <u>"
                + url
                + "</u>",
            JOptionPane.WARNING_MESSAGE);
      if (urltype == 3)
        reftomain.messageBox(
            "Error while open the Network File",
            "<html><i>The remote https <u>file does not exist</u> ! Perhaps the<br>internet"
                + " connection was broken, or you have <br>prescribed you with in the URL"
                + " ?<br><br><font size=-3>"
                + e.getMessage()
                + "</font><br><br></i>URL: <u>"
                + url
                + "</u>",
            JOptionPane.WARNING_MESSAGE);
      if (urltype == 4)
        reftomain.messageBox(
            "Error while open the Network File",
            "<html><i>The remote ftp <u>file or does not exist</u> ! Perhaps the<br>internet"
                + " connection was broken, or you have <br>prescribed you with the URL"
                + " ?<br><br><font size=-3>"
                + e.getMessage()
                + "</font><br><br></i>URL: <u>"
                + url
                + "</u>",
            JOptionPane.WARNING_MESSAGE);
      System.err.println("--> ERROR:" + e.getMessage());
      reftomain.setTitle("Sonogram Visible Speech - version " + reftomain.version);
      reftomain.spektrumExist = false;
      reftomain.updateimageflag = true;
      reftomain.repaint();
      return;
    }
    // Call to open in this CLass
    reftomain.progmon.setProgress(4);
    if (!open(ds)) {
      return;
    }
    reftomain.filepath = url;
    reftomain.url = url;
  }
  // ---------------------------------------------------------------------------------------------------
}
