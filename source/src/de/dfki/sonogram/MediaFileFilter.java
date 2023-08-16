/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>My FileFilter implementation for Mediafile filters. It extend default FileFilter Class.
 *
 * @author Christoph Lauer
 * @version 1.0, Current 26/09/2002
 */
package de.dfki.sonogram;

import java.io.File;

class MediaFileFilter extends javax.swing.filechooser.FileFilter {
  // -------------------------------------------------------------------------
  private String m_description = null;
  private String m_extension = null;
  private boolean allMediaFilesFlag = false;
  // -------------------------------------------------------------------------
  /** The Construkltor */
  public MediaFileFilter(String extension, String description) {
    m_description = description;
    m_extension = "." + extension.toLowerCase();
  }
  // -------------------------------------------------------------------------
  public void aceptAllMediaFiles(boolean flag) {
    allMediaFilesFlag = flag;
  }
  // -------------------------------------------------------------------------
  public String getDescription() {
    return m_description;
  }
  // -------------------------------------------------------------------------
  public boolean accept(File f) {
    if (f == null) return false;
    if (f.isDirectory()) return true;
    if (allMediaFilesFlag == false) return f.getName().toLowerCase().endsWith(m_extension);
    else
      return (f.getName().toLowerCase().endsWith("wav")
          || f.getName().toLowerCase().endsWith("au")
          || f.getName().toLowerCase().endsWith("avi")
          || f.getName().toLowerCase().endsWith("aiff")
          || f.getName().toLowerCase().endsWith("swf")
          || f.getName().toLowerCase().endsWith("spl")
          || f.getName().toLowerCase().endsWith("gsm")
          || f.getName().toLowerCase().endsWith("mvr")
          || f.getName().toLowerCase().endsWith("mpg")
          || f.getName().toLowerCase().endsWith("mp2")
          || f.getName().toLowerCase().endsWith("mp3")
          || f.getName().toLowerCase().endsWith("mov"));
  }
  // -------------------------------------------------------------------------

}
