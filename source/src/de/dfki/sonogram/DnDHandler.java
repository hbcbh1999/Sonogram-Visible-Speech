package de.dfki.sonogram;

import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.File;

/**
 * Copyright (c) 2001 Christoph Lauer @ DFKI, All Rights Reserved. clauer@dfki.de - www.dfki.de
 *
 * <p>Drag & Drop Support. It implemets the complette DropTargetListener Interface Onely the
 * <i>drop</i> method is used.
 *
 * @author Christoph Lauer
 * @version 1.6, Current 10/07/2002
 */
public class DnDHandler implements DropTargetListener {
  private Sonogram reftosonogram;

  public DnDHandler(Sonogram ref) {
    super();
    reftosonogram = ref;
  }

  public void dragEnter(DropTargetDragEvent e) {}

  public void dragExit(DropTargetEvent e) {}

  public void dragOver(DropTargetDragEvent e) {}

  public void drop(DropTargetDropEvent evt) {
    Transferable t = evt.getTransferable();
    evt.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
    try {
      java.util.List files = (java.util.List) t.getTransferData(DataFlavor.javaFileListFlavor);
      File f = (File) files.get(0);
      evt.dropComplete(true);
      reftosonogram.openFile(f.getAbsolutePath());
    } catch (Throwable ex) {
      evt.dropComplete(true);
    }
    evt.dropComplete(true);
  }

  public void dropActionChanged(DropTargetDragEvent e) {}
}
