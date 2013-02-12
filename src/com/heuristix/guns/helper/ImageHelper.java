package com.heuristix.guns.helper;

import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.heuristix.guns.util.Log;

public class ImageHelper {
	
	public static final int BITS_PER_PIXEL = 4;

	private ImageHelper() { }

	public static BufferedImage resizeImage(BufferedImage image, int width, int height) {
	    if(width != 0 && height != 0) {
	        int type = image.getType();
	        if(type == BufferedImage.TYPE_CUSTOM) {
	            type = BufferedImage.TYPE_INT_ARGB;
	        }
	        BufferedImage scaled = new BufferedImage(width, height, type);
	        scaled.createGraphics().drawImage(image, 0, 0, width, height, null);
	        return scaled;
	    }
	    return image;
	}

	public static byte[] writeImage(BufferedImage image, String format) {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    if(ImageHelper.writeImage(image, format, out)) {
	        return out.toByteArray();
	    }
	    return null;
	}

	public static boolean writeImage(BufferedImage image, String format, File file) {
		try {
			return writeImage(image, format, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			Log.getLogger().throwing(ImageHelper.class.getName(), "writeImage(BufferedImage image, String format, File file)", e);  
		}
		return false;
	}
	
	public static boolean writeImage(BufferedImage image, String format, OutputStream out) {
	    try {
	        return ImageIO.write(image, format, out);
	    } catch (IOException e) {
	        Log.getLogger().throwing(ImageHelper.class.getName(), "writeImage(BufferedImage image, String format, OutputStream out)", e);
	    }
	    return false;
	}

	public static BufferedImage getScreenImage(int width, int height) {
	    return ImageHelper.getScreenImage(width, height, ImageHelper.getScreenBytes(width, height));
	}

	public static BufferedImage getScreenImage(int width, int height, ByteBuffer buffer) {
	    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    for(int x = 0; x < width; x++) {
	        for(int y = 0; y < height; y++) {
	            int i = (x + (width * y)) * ImageHelper.BITS_PER_PIXEL;
	            int color = ((buffer.get(i + 3) & 0xFF) << 24) | ((buffer.get(i) & 0xFF) << 16) | ((buffer.get(i + 1) & 0xFF) << 8) | (buffer.get(i + 2) & 0xFF);
	            image.setRGB(x, height - (y + 1), color);
	        }
	    }
	    return image;
	}

	public static ByteBuffer getScreenBytes(int width, int height) {
	    GL11.glReadBuffer(GL11.GL_FRONT);
	    ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * ImageHelper.BITS_PER_PIXEL);
	    GL11.glReadPixels(0, 0, width, height,  GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
	    return buffer;
	}

	public static BufferedImage readImage(byte[] bytes) {
	    return ImageHelper.readImage(new ByteArrayInputStream(bytes));
	}

	public static BufferedImage readImage(InputStream in) {
	    try {
	        return ImageIO.read(in);
	    } catch (IOException e) {
	        Log.getLogger().throwing(ImageHelper.class.getName(), "writeImage(BufferedImage image, String format, OutputStream out)", e);
	    }
	    return null;
	}
	
	public static BufferedImage getClosestTexture(int size, BufferedImage... textures) {
        BufferedImage closest = null;
        int closestWidth = -1, closestHeight = -1;
        for(BufferedImage image : textures) {
            if(image.getWidth() == size && image.getHeight() == size) {
                return image;
            } else {
                int imageWidth = image.getWidth(), imageHeight = image.getHeight();
                if((imageWidth < size && imageHeight < size) ||
                        ((imageWidth < closestWidth && imageHeight < closestHeight) && (closestWidth > size && closestHeight > size))
                            || (closestWidth == -1 && closestHeight == -1)) {
                    closestWidth = imageWidth;
                    closestHeight = imageHeight;
                    closest = image;
                }
            }
        }
        if(closest != null && closest.getWidth() != size) {
            closest = ImageHelper.resizeImage(closest, size, size);
        }
        return closest;
    }
	
	public static BufferedImage getCompatibleImage(int width, int height, int transparency) {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(width, height, transparency);
	}
}