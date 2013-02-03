package com.heuristix.guns.swing;

import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 8/30/12
 * Time: 6:44 PM
 */
public interface TextureChangedListener {

    void textureChanged(DisplayableImageButton button, BufferedImage texture);

}
