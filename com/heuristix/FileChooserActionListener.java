package com.heuristix;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/23/11
 * Time: 4:29 PM
 */
public class FileChooserActionListener implements ActionListener {

    private final Component parent;
    private final boolean open;
        private final FileChooserCallback fcc;
    private JFileChooser fileChooser;
    private File lastSelectedFile;

    public FileChooserActionListener(Component parent, FileChooserCallback fcc, boolean open) {
        this(parent, fcc, open, Utilities.getHomeDirectory());
    }

    public FileChooserActionListener(Component parent, FileChooserCallback fcc, boolean open, File directory) {
        this(parent, fcc, open, new JFileChooser(directory));
    }

    public FileChooserActionListener(Component parent, FileChooserCallback fcc, boolean open, JFileChooser fileChooser) {
        this.parent = parent;
        this.fcc = fcc;
        this.open = open;
        this.fileChooser = fileChooser;
    }

    public void actionPerformed(ActionEvent e) {
        int result = (open) ? fileChooser.showOpenDialog(parent) : fileChooser.showSaveDialog(parent);
        if(result == JFileChooser.APPROVE_OPTION) {
            lastSelectedFile = fileChooser.getSelectedFile();
            fcc.selectedFile(lastSelectedFile);
        }
    }

    public File getLastSelectedFile() {
        return lastSelectedFile;
    }

    public void setLastSelectedFile(File file) {
        this.lastSelectedFile = file;
    }
}
