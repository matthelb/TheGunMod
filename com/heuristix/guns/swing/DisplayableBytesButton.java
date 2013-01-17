package com.heuristix.guns.swing;

import com.heuristix.guns.Util;

import java.io.File;
import java.util.LinkedList;

public class DisplayableBytesButton extends DisplayableFileButton {

    private byte[] bytes;

    public DisplayableBytesButton() {
        this(null);
    }

    public DisplayableBytesButton(File file) {
        super(file);
    }

    public boolean updateFile(File file) {
        if (super.updateFile(file)) {
            byte[] bytes = Util.read(file);
            if (bytes != null) {
                updateButton(file.getName(), bytes);
                return true;
            }
        }
        return false;
    }

    public void updateButton(String name, byte[] bytes) {
        this.bytes = bytes;
        setText(name);
    }

    public byte[] getBytes() {
        return this.bytes;
    }
}