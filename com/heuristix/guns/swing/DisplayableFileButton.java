package com.heuristix.guns.swing;

import com.heuristix.guns.util.Log;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.util.List;
import java.util.TooManyListenersException;

public class DisplayableFileButton extends JButton implements FileChooserCallback {

    private FileChooserActionListener fileChooserListener;
    private File file;

    public DisplayableFileButton() {
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
                        List files = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
                        dtde.dropComplete(DisplayableFileButton.this.updateFile((File) files.get(0)));
                    } catch (Exception e) {
                        Log.getLogger().throwing(getClass().getName(), "updateFile(File file)", e);
                        dtde.dropComplete(false);
                    }
                }
            });
        } catch (TooManyListenersException e) {
            Log.getLogger().throwing(getClass().getName(), "updateFile(File file)", e);
        }
        setDropTarget(target);
        fileChooserListener = (file == null) ? new FileChooserActionListener(this, this, true) : new FileChooserActionListener(this, this, true, file);
        addActionListener(fileChooserListener);
        updateFile(file);
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
        return file;
    }
}