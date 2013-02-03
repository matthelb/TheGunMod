package com.heuristix.guns.client.render;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class TextureList extends ArrayList<BufferedImage> {

	private static final long serialVersionUID = 480341644348642125L;
	
	public int addTexture(BufferedImage image) {
		if (add(image)) {
			return size() - 1;
		}
		return -1;
	}
	
}