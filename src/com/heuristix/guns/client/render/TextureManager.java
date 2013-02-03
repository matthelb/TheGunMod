package com.heuristix.guns.client.render;

import java.awt.image.BufferedImage;

import com.heuristix.guns.helper.MathHelper;

public class TextureManager {
	
	private TextureList[] textures;
	
	public TextureManager() {
		this.textures = new TextureList[6];
		for (int i = 0; i < 6; i++) {
			textures[i] = new TextureList();
		}
	}
	
	public int registerTexture(BufferedImage... images) {
		int textureIndex = -1;
		for (BufferedImage image : images) {
			int index = MathHelper.log2(image.getWidth());
			textureIndex = textures[index].addTexture(image);
		}
		for (int i = 0; i < textures.length; i++) {
			if (textures[i].size() == textureIndex) {
				textures[i].addTexture(null);
			}
		}
		return textureIndex;
	}
	
}