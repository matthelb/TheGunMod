package com.heuristix.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 2/5/12
 * Time: 1:33 PM
 */
public class TexturePanelFacade extends DisplayableImageButton {

    private int lastI, lastJ;

    private TexturePanel panel;
    private JDialog dialog;

    public TexturePanelFacade(Frame owner) {
        this.panel = new TexturePanel();
        this.dialog = new JDialog(owner, "Advanced Textures", true);
        removeActionListener(getActionListeners()[0]);
        addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dialog.setVisible(dialog.isVisible());
            }
        });
    }
    @Override
    public boolean updateFile(File file) {
        return panel.updateFile(file, lastI, lastJ);
    }

    @Override
    public void updateImage(BufferedImage image) {
        panel.updateImage(image, lastI, lastJ);
    }

    @Override
    public BufferedImage getImage() {
        return panel.getTexture(lastI, lastJ);
    }

    @Override
    public void selectedFile(File file) {
        panel.selectedFile(file, lastI, lastJ);
    }

    @Override
    public File getFile() {
        return panel.getFile(lastI, lastJ);
    }

    public JDialog getTexturePanelDialog() {
        return dialog;
    }
}
