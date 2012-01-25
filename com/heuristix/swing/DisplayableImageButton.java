package com.heuristix.swing;

import com.heuristix.Util;
import com.heuristix.util.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DisplayableImageButton extends DisplayableFileButton {
    private BufferedImage icon;

    public boolean updateFile(File file) {
        if (super.updateFile(file)) {
            try {
                updateImage(ImageIO.read(file));
                return true;
            } catch (IOException e) {
                Log.getLogger().throwing(getClass().getName(), "updateFile(File file)", e);
            }
        }
        return false;
    }

    public void updateImage(BufferedImage image) {
        this.icon = Util.resize(image, getWidth(), getHeight());
        setIcon(new ImageIcon(this.icon));
    }

    public BufferedImage getImage() {
        return this.icon;
    }
}