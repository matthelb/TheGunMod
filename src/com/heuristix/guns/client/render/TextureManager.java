package com.heuristix.guns.client.render;

import java.awt.image.BufferedImage;
import java.io.File;

import net.minecraft.client.Minecraft;

import com.heuristix.guns.helper.IOHelper;
import com.heuristix.guns.helper.ImageHelper;
import com.heuristix.guns.helper.MathHelper;

import cpw.mods.fml.client.FMLClientHandler;

public class TextureManager {
	
	public static final String TEXTURE_FILE_NAME = "sprites";
	public static final String TEXTURE_FILE_FORMAT = "png";
	public static final String TEXTURE_FILE_NAME_FORMAT = TEXTURE_FILE_NAME + "_%d." + TEXTURE_FILE_FORMAT;
	
	private TextureList[] textures;
	
	public TextureManager() {
		this.textures = new TextureList[6];
		for (int i = 0; i < 6; i++) {
			textures[i] = new TextureList(MathHelper.pow(2, i + 4));
		}
	}
	
	public int registerTexture(BufferedImage... images) {
		int textureIndex = -1;
		for (BufferedImage image : images) {
			int index = MathHelper.log2(image.getWidth()) - 4;
			textureIndex = textures[index].addTexture(image);
		}
		for (int i = 0; i < textures.length; i++) {
			if (textures[i].size() == textureIndex) {
				textures[i].addTexture(ImageHelper.getClosestTexture(textures[i].getSize(), images));
			}
		}
		return textureIndex;
	}
	
	public File writeTemporaryTextures(String folder) {
		File f = null;
		for (TextureList list : textures) {
			f = IOHelper.getHeuristixTempFile(folder, getTextureFileName(list.getSize()));
			ImageHelper.writeImage(list.toBufferedImage(), TEXTURE_FILE_FORMAT, f);
		}
		return (f == null) ? f : f.getParentFile();
	}
	
	public static String getTextureFileName(int size) {
		return String.format(TEXTURE_FILE_NAME_FORMAT, size);
	}
	
	public static String getCurrentTextureFileName() {
		return getTextureFileName(getCurrentTextureSize());
	}
	
	public static int getCurrentTextureSize() {
		Minecraft client = FMLClientHandler.instance().getClient();
		if (client != null) {
			return client.renderEngine.texturePack.getSelectedTexturePack().getTexturePackResolution();
		}
		return 16;
	}
	
}