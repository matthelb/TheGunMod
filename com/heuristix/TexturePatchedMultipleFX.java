package com.heuristix;

import com.heuristix.util.ReflectionFacade;
import net.minecraft.src.RenderEngine;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/31/12
 * Time: 8:47 PM
 */
public class TexturePatchedMultipleFX extends TextureMultipleFX {

    public TexturePatchedMultipleFX(int iconIndex, boolean item, boolean setupOnTick, BufferedImage... textures) {
        this(iconIndex, 1, item, setupOnTick, textures);
    }

    public TexturePatchedMultipleFX(int iconIndex, int tileSize, boolean item, boolean setupOnTick, BufferedImage... textures) {
        super(iconIndex, tileSize, item, setupOnTick, textures);
        ReflectionFacade.getInstance().putField(RenderEngine.class, "int_size", "");
    }

    @Override
    public Dimension getMaxTextureDimension() {
        int size = (Integer) ReflectionFacade.getInstance().getFieldValue(RenderEngine.class, "int_size", null);
        return new Dimension(size, size);
    }
}
