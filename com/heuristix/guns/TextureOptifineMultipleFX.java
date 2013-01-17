package com.heuristix.guns;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import net.minecraft.src.Config;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/31/12
 * Time: 8:32 PM
 */
public class TextureOptifineMultipleFX extends TextureMultipleFX  {

    public TextureOptifineMultipleFX(int iconIndex, boolean item, BufferedImage... textures) {
        this(iconIndex, item, false, textures);
    }
    public TextureOptifineMultipleFX(int iconIndex, boolean item, boolean setupOnTick, BufferedImage... textures) {
        super(iconIndex, item, false, textures);
    }

    public TextureOptifineMultipleFX(int iconIndex, int tileSize, boolean item, boolean setupOnTick, BufferedImage... textures) {
        super(iconIndex, tileSize, item, false, textures);
    }

    @Override
    public Dimension getMaxTextureDimension() {
        int size = (tileSize == 1) ? Config.getIconWidthItems() : Config.getIconWidthTerrain();
        return new Dimension(size, size);
    }
}