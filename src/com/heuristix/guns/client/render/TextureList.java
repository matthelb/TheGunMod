package com.heuristix.guns.client.render;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.heuristix.guns.helper.MathHelper;

public class TextureList extends ArrayList<BufferedImage> {

	private static final long serialVersionUID = 480341644348642125L;
	
	private final int size;
	
	public TextureList(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}
	
	public int addTexture(BufferedImage image) {
		if (image.getWidth() != size) {
			throw new IllegalArgumentException(String.format("TextureList may only contains images of the same size %d", size));
		}
		if (add(image)) {
			return size() - 1;
		}
		return -1;
	}
	
	public BufferedImage toBufferedImage() {
		int width = getSize() * 16, height = getSize() * 16;//MathHelper.roundUp(size(), 16) / 16 * getSize();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		for (int i = 0; i < size(); i++) {
			BufferedImage t = this.get(i);
			if (t != null) {
				g.drawImage(t, (i % 16) * getSize(), (i / 16) * getSize(), getSize(), getSize(), null); 
			}
		}
		return image;
	}
	
}