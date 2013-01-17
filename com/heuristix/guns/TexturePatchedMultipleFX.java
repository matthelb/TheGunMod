package com.heuristix.guns;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.pclewis.mcpatcher.mod.TileSize;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/31/12
 * Time: 8:47 PM
 */
public class TexturePatchedMultipleFX extends TextureMultipleFX {

    public TexturePatchedMultipleFX(int iconIndex, boolean item, BufferedImage... textures) {
        this(iconIndex, item, false, textures);
    }

    public TexturePatchedMultipleFX(int iconIndex, boolean item, boolean setupOnTick, BufferedImage... textures) {
        this(iconIndex, 1, item, setupOnTick, textures);
    }

    public TexturePatchedMultipleFX(int iconIndex, int tileSize, boolean item, boolean setupOnTick, BufferedImage... textures) {
        super(iconIndex, tileSize, item, setupOnTick, textures);
    }

    @Override
    public Dimension getMaxTextureDimension() {
        return new Dimension(TileSize.int_size, TileSize.int_size);
    }
}
