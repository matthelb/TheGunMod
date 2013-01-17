package com.heuristix.guns;

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

    private RenderEngine engine;

    public TextureDefaultMultipleFX(int iconIndex, boolean item, BufferedImage... textures) {
        this(iconIndex, item, true, textures);
    }

    public TextureDefaultMultipleFX(int iconIndex, boolean item, boolean setupOnTick, BufferedImage... textures) {
        this(iconIndex, 1, item, setupOnTick, textures);
    }

    public TextureDefaultMultipleFX(int iconIndex, int tileSize, boolean item, boolean setupOnTick, BufferedImage... textures) {
        super(iconIndex, tileSize, item, setupOnTick, textures);
    }

    @Override
    public Dimension getMaxTextureDimension() {
        return Util.getTextureDimensions(tileImage == 1, engine);
    }

    public void setRenderEngine(RenderEngine engine) {
        this.engine = engine;
    }
}
