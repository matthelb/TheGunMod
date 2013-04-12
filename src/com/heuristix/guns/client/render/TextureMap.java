package com.heuristix.guns.client.render;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.heuristix.guns.helper.IOHelper;
import com.heuristix.guns.helper.ImageHelper;

public class TextureMap extends HashMap<String, BufferedImage[]> {

	private int maxResolution;
	
	public TextureMap() {
		this.maxResolution = 16;
	}
	
	public void setMaxResolution(int resolution) {
		this.maxResolution = resolution;
	}
	
	public BufferedImage getTexture(String id) {
		BufferedImage[] textures = get(id);
		BufferedImage closest = null;
		if (textures != null) {
			int closestWidth = -1, closestHeight = -1;
	        for(BufferedImage image : textures) {
	            if(image.getWidth() == maxResolution && image.getHeight() == maxResolution) {
	                return image;
	            } else {
	                int imageWidth = image.getWidth(), imageHeight = image.getHeight();
	                if((imageWidth < maxResolution && imageHeight < maxResolution) ||
	                        ((imageWidth < closestWidth && imageHeight < closestHeight) && (closestWidth > maxResolution && closestHeight > maxResolution))
	                            || closest == null) {
	                    closestWidth = imageWidth;
	                    closestHeight = imageHeight;
	                    closest = image;
	                }
	            }
	        }
		}
        return closest;
	}
	
	public void writeTemporaryTextures(String modName) {
		File parent = new File(IOHelper.getMinecraftDir("mods").getAbsolutePath() + 
				File.separator + modName + File.separator + "textures");
		for (Map.Entry<String, BufferedImage[]> entry : entrySet()) {
			String id = entry.getKey();
			File folder = null;
			if (id.startsWith("blocks:")) {
				folder = new File(parent.getAbsolutePath() + File.separator + "blocks");
			} else if (id.startsWith("items:")) {
				folder = new File(parent.getAbsolutePath() + File.separator + "items");
			}
			if (folder != null) {
				if (!folder.exists()) {
					folder.mkdirs();
				}
				folder = new File(folder.getAbsolutePath() + File.separator + id.substring(id.indexOf("item.") + 5) + ".png");
				ImageHelper.writeImage(getTexture(id), "png", folder);
			}
		}
	}

}
