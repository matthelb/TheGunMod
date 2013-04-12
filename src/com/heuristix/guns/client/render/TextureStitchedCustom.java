package com.heuristix.guns.client.render;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.heuristix.guns.BaseMod;

import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureStitched;
import net.minecraft.client.texturepacks.ITexturePack;

public class TextureStitchedCustom extends TextureStitched {

	private final BaseMod mod;
	
	public TextureStitchedCustom(String name, BaseMod mod) {
		super(name);
		this.mod = mod;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean loadTexture(TextureManager manager, ITexturePack texturepack, String name, String fileName, BufferedImage image, ArrayList textures) {
		image = mod.getTextureMap().getTexture("items:item." + name);
		textures.add(manager.makeTexture(name, 2, image.getWidth(), image.getHeight(), 10496, 6408, 9728, 9728, false, image));
		return true;
	}
	
	

}
