package com.heuristix;

import net.minecraft.src.RenderEngine;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/31/12
 * Time: 8:21 PM
 */
public class TextureDefaultMultipleFX extends TextureMultipleFX {

    private final RenderEngine engine;

    public TextureDefaultMultipleFX(int iconIndex, boolean item, boolean setupOnTick, RenderEngine engine, BufferedImage... textures) {
        this(iconIndex, 1, item, setupOnTick, engine, textures);
    }

    public TextureDefaultMultipleFX(int iconIndex, int tileSize, boolean item, boolean setupOnTick, RenderEngine engine, BufferedImage... textures) {
        super(iconIndex, tileSize, item, setupOnTick, textures);
        this.engine = engine;
    }

    @Override
    public Dimension getMaxTextureDimension() {
        return Util.getTextureDimensions(tileImage == 1, engine);
    }
}
