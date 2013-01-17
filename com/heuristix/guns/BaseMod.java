package com.heuristix.guns;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.heuristix.guns.util.ReflectionFacade;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPool;
import net.minecraft.client.texturepacks.ITexturePack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BaseMod {

	private final List<Map<String, byte[]>> sounds;
	private final Map<Integer, List<List<BufferedImage>>> textures;
	
	public BaseMod() {
		this.sounds = new ArrayList<Map<String, byte[]>>(3);
		this.textures = new HashMap<Integer, List<List<BufferedImage>>>();
	}
	
	public void registerItem(ItemCustom item) {
		item.setItemName(item.getName().toLowerCase().replaceAll(" ", "_"));
		LanguageRegistry.addName(item, item.getName());
		item.setTextureFile(item.getIconPath());
	}

	public void registerSound(String path, byte[] data) {
		registerSoundEffect(0, path, data);
	}
	
	public void registerMusic(String path, byte[] data) {
		registerSoundEffect(1, path, data);
	}
	
	public void registerStreaming(String path, byte[] data) {
		registerSoundEffect(2, path, data);
	}
	
	public void registerSoundEffect(int index, String path, byte[] data) {
		Map<String, byte[]> sounds = this.sounds.get(index);
		if (sounds == null) {
			sounds = new HashMap<String, byte[]>();	
		}
		sounds.put(path, data);
		this.sounds.set(index, sounds);
		
	}
	
	public String registerTexture(BufferedImage image) {
		List<List<BufferedImage>> imagesList = textures.get(image.getWidth());
		if (imagesList == null) {
			imagesList = new ArrayList<List<BufferedImage>>();
		}
		List<BufferedImage> images = null;
		int index;
		for (index = 0; index < imagesList.size() && images == null; index++) {
			if (imagesList.get(index).size() < 256) {
				images = imagesList.get(index);
			}
		}
		if (images == null) {
			images = new ArrayList<BufferedImage>();
		}
		images.add(image);
		imagesList.set(index, images);
		textures.put(image.getWidth(), imagesList);
		return getTextureFileName(image.getWidth(), index);
	}

	protected void registerAllSounds(SoundLoadEvent event) {
		for (int i = 0; i < sounds.size(); i++) {
			Map<String, byte[]> sounds = this.sounds.get(i);
			if (sounds != null) {
				for (Map.Entry<String, byte[]> entry : sounds.entrySet()) {
					String name = entry.getKey();
					File soundFile = Util.getTempFile(name.replaceAll("/", "."), null, entry.getValue());
					if (soundFile != null) {
						switch (i) {
							case 0:
								event.manager.addSound(name, soundFile);
								break;
							case 1:
								event.manager.addMusic(name, soundFile);
								break;
							case 2:
								event.manager.addStreaming(name, soundFile);
								break;
							default:
								break;
						}
					}
				}
			}
		}
	}
	
	protected void registerAllTextures() throws IOException {
		for (Map.Entry<Integer, List<List<BufferedImage>>> entry : textures.entrySet()) {
			for (int i = 0; i < entry.getValue().size(); i++) {
				List<BufferedImage> images = entry.getValue().get(i);
				BufferedImage textures = new BufferedImage(entry.getKey() * 16, entry.getKey() * 16, BufferedImage.TYPE_INT_ARGB);
				Graphics2D g = textures.createGraphics();
				for (int j = 0; j < images.size(); j++) {
					g.drawImage(images.get(j), (j % 16) * entry.getKey(), (j / 16) * entry.getKey(),
							((j % 16) + 1) * entry.getKey(), ((j / 16) + 1) * entry.getKey(), null);
				}
				ByteArrayOutputStream os = new ByteArrayOutputStream();
			    ImageIO.write(textures, "png", os);
				File resourceFile = Util.getTempFile(getTextureFileName(entry.getKey(), i), "png", os.toByteArray());
				if (resourceFile.exists()) {
					addURLToClassLoader(resourceFile.toURI().toURL(), (URLClassLoader) ITexturePack.class.getClassLoader());
				}
			}
		}
	}
	
	public String getTextureFileName(int size, int index) {
		return getClass().getName().toLowerCase() + "_" + size + "_" + index;
	}
	
	private void addURLToClassLoader(URL url, URLClassLoader cl) {
		try {
			Method methodAddURL = cl.getClass().getMethod("addURL", URL.class);
			methodAddURL.setAccessible(true);
			methodAddURL.invoke(cl, url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
