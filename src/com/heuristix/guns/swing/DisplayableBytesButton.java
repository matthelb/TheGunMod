package com.heuristix.guns.swing;

import java.io.File;

import com.heuristix.guns.helper.IOHelper;

public class DisplayableBytesButton extends DisplayableFileButton {

	private static final long serialVersionUID = 3174299737376770400L;

	private byte[] bytes;

    public DisplayableBytesButton() {
        this(null);
    }

    public DisplayableBytesButton(File file) {
        super(file, "All files", "*");
    }

    public boolean updateFile(File file) {
        if (super.updateFile(file)) {
            byte[] bytes = IOHelper.read(file);
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