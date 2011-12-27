package com.heuristix.swing;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;
import java.util.TooManyListenersException;
import javax.swing.JButton;

public class DisplayableFileButton extends JButton
  implements FileChooserCallback
{
  private FileChooserActionListener fileChooserListener;
  protected File file;

  public DisplayableFileButton()
  {
    this(null);
  }

  public DisplayableFileButton(File file) {
    this.file = file;
    init();
  }

  private void init() {
    DropTarget target = new DropTarget();
    try {
      target.addDropTargetListener(new DropTargetAdapter() {
        public void drop(DropTargetDropEvent dtde) {
          dtde.acceptDrop(dtde.getDropAction());
          Transferable t = dtde.getTransferable();
          try {
            List files = (List)t.getTransferData(DataFlavor.javaFileListFlavor);
            dtde.dropComplete(DisplayableFileButton.this.updateFile((File)files.get(0)));
          } catch (Exception e) {
            e.printStackTrace();
            dtde.dropComplete(false);
          }
        } } );
    } catch (TooManyListenersException e) {
      e.printStackTrace();
    }
    setDropTarget(target);
    this.fileChooserListener = new FileChooserActionListener(this, this, true);
    addActionListener(this.fileChooserListener);
    updateFile(this.file);
  }

  public boolean updateFile(File file) {
    if (file != null) {
      this.file = file;
      this.fileChooserListener.setLastSelectedFile(file);
      return true;
    }
    return false;
  }

  public void selectedFile(File file) {
    updateFile(file);
  }

  public File getFile() {
    return this.file;
  }
}