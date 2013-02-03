package com.heuristix.guns.helper;

import java.awt.image.BufferedImage;

public class ImageHelper {
	
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
}