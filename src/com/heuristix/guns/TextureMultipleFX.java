package com.heuristix.guns;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.heuristix.guns.helper.ImageHelper;

import net.minecraft.client.renderer.texturefx.TextureFX;

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
    private Dimension currentTextureDimension;

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
        if(!setup || !getMaxTextureDimension().equals(currentTextureDimension)) {
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
        currentTextureDimension = getMaxTextureDimension();
        if(currentTextureDimension.width == 0) {
            currentTextureDimension.width = currentTextureDimension.height;
        }
        if(currentTextureDimension.height == 0) {
            currentTextureDimension.width = currentTextureDimension.height = 16;
        }
        BufferedImage image = getTexture(currentTextureDimension.width, currentTextureDimension.height);
        imagePixelColors = new int[currentTextureDimension.width * currentTextureDimension.height];
        imageData = new byte[currentTextureDimension.width * currentTextureDimension.height * 4];
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
            } else {
                int imageWidth = image.getWidth(), imageHeight = image.getHeight();
                if((imageWidth < width && imageHeight < height) ||
                        ((imageWidth < closestWidth && imageHeight < closestHeight) && (closestWidth > width && closestHeight > height))
                            || (closestWidth == -1 && closestHeight == -1)) {
                    closestWidth = imageWidth;
                    closestHeight = imageHeight;
                    closest = image;
                }
            }
        }
        if(closest != null && closest.getWidth() != width) {
            closest = ImageHelper.resizeImage(closest, width, height);
        }
        return closest;
    }


}
