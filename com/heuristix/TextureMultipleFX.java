package com.heuristix;

import net.minecraft.src.RenderEngine;
import net.minecraft.src.TextureFX;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/30/12
 * Time: 4:17 PM
 */
public abstract class TextureMultipleFX extends TextureFX {

    private BufferedImage[] textures;

    private boolean oldAnalygraph;
    private int[] imagePixelColors;

    private boolean setup;

    public TextureMultipleFX(int iconIndex, boolean item, boolean setupOnTick, BufferedImage... textures) {
        this(iconIndex, 1, item, setupOnTick, textures);
    }

    public TextureMultipleFX(int iconIndex, int tileSize, boolean item, boolean setupOnTick, BufferedImage... textures) {
        super(iconIndex);
        this.tileSize = tileSize;
        this.tileImage = (item) ? 1 : 0;
        this.textures = textures;
        if(!setupOnTick) {
            setup();
        }
    }

    @Override
    public void onTick() {
        if(!setup) {
            setup();
        }
        if(oldAnalygraph != anaglyphEnabled) {
            updateImageData();
        }
    }

    private void updateImageData() {
        for (int i = 0; i < imagePixelColors.length; i++) {
            int a = imagePixelColors[i] >> 24 & 0xff;
            int r = imagePixelColors[i] >> 16 & 0xff;
            int g = imagePixelColors[i] >> 8 & 0xff;
            int b = imagePixelColors[i] >> 0 & 0xff;
            if (anaglyphEnabled) {
                int average = (r + g + b) / 3;
                r = g = b = average;
            }
            imageData[i * 4 + 0] = (byte)r;
            imageData[i * 4 + 1] = (byte)g;
            imageData[i * 4 + 2] = (byte)b;
            imageData[i * 4 + 3] = (byte)a;
        }
        oldAnalygraph = anaglyphEnabled;
    }

    private void setup() {
        Dimension textureDimensions = getMaxTextureDimension();
        BufferedImage image = getTexture(textureDimensions.width, textureDimensions.height);
        imagePixelColors = new int[textureDimensions.width * textureDimensions.height];
        imageData = new byte[textureDimensions.width * textureDimensions.height * 4];
        if (textureDimensions.width != image.getWidth() || textureDimensions.height != image.getHeight()) {
            BufferedImage scaledImage = new BufferedImage(textureDimensions.width, textureDimensions.height, BufferedImage.TYPE_4BYTE_ABGR);
            scaledImage.getGraphics().drawImage(image, 0, 0, textureDimensions.width, textureDimensions.height, 0, 0, image.getWidth(), image.getHeight(), null);
            image = scaledImage;
        }
        image.getRGB(0, 0, image.getWidth(), image.getHeight(), imagePixelColors, 0, image.getWidth());
        setup = true;
        updateImageData();
    }

    public abstract Dimension getMaxTextureDimension();

    private BufferedImage getTexture(int width, int height) {
        BufferedImage closest = null;
        int closestWidth = -1, closestHeight = -1;
        for(BufferedImage image : textures) {
            if(image.getWidth() == width && image.getHeight() == height) {
                return image;
            } else if((image.getWidth() > closestWidth && image.getWidth() < width && image.getHeight() > closestHeight && image.getHeight() < height)
                    || (closestWidth == -1 && closestHeight == -1)) {
                closest = image;
                closestWidth = image.getWidth();
                closestHeight = image.getHeight();
            }

        }
        return Util.resize(closest, width, height);
    }


}
