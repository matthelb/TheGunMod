package com.heuristix.guns.swing;

import com.heuristix.guns.Util;
import com.heuristix.guns.util.Log;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DisplayableImageButton extends DisplayableFileButton {

    private final List<TextureChangedListener> listeners;
    private BufferedImage icon;

    public DisplayableImageButton() {
        this(null);
    }

    public DisplayableImageButton(File file) {
        super(file);
        this.listeners = new LinkedList<TextureChangedListener>();
    }

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
        this.icon = image;
        setIcon(new ImageIcon(Util.resizeImage(getImage(), getWidth(), getHeight())));
        notifyListeners(image);
    }

    public BufferedImage getImage() {
        return this.icon;
    }
    public void addTextureChangedListener(TextureChangedListener... listeners) {
        Collections.addAll(this.listeners, listeners);
    }

    public boolean removeTextureChangedListener(TextureChangedListener... listeners) {
        return this.listeners.removeAll(Arrays.asList(listeners));
    }

    private void notifyListeners(BufferedImage image) {
        for (TextureChangedListener l : listeners) {
            l.textureChanged(this, image);
        }
    }
}