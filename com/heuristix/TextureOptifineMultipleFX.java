package com.heuristix;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/31/12
 * Time: 8:32 PM
 */
public class TextureOptifineMultipleFX extends TextureMultipleFX {

    public TextureOptifineMultipleFX(int iconIndex, boolean item, boolean setupOnTick, BufferedImage... textures) {
        super(iconIndex, item, setupOnTick, textures);
    }

    public TextureOptifineMultipleFX(int iconIndex, int tileSize, boolean item, boolean setupOnTick, BufferedImage... textures) {
        super(iconIndex, tileSize, item, setupOnTick, textures);
    }

    @Override
    public Dimension getMaxTextureDimension() {
        return new Dimension(1024, 1024);
    }
}
