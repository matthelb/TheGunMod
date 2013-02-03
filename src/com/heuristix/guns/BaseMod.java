package com.heuristix.guns;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.heuristix.guns.client.render.TextureManager;
import com.heuristix.guns.helper.IOHelper;

import net.minecraftforge.client.event.sound.SoundLoadEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class BaseMod implements IMod {

	private final List<Map<String, byte[]>> sounds;
	
	@SideOnly(Side.CLIENT)
	private final TextureManager textureManager;
	
	public BaseMod() {
		this.sounds = new ArrayList<Map<String, byte[]>>(3);
		this.textureManager = new TextureManager();
	}
	
	public void registerItem(ItemCustom item) {
		item.setItemName(item.getName().toLowerCase().replaceAll(" ", "_"));
		LanguageRegistry.addName(item, item.getName());
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
	
	public int registerTexture(BufferedImage... images) {
		return textureManager.registerTexture(images);
	}

	protected void registerAllSounds(SoundLoadEvent event) {
		for (int i = 0; i < sounds.size(); i++) {
			Map<String, byte[]> sounds = this.sounds.get(i);
			if (sounds != null) {
				for (Map.Entry<String, byte[]> entry : sounds.entrySet()) {
					String name = entry.getKey();
					File soundFile = IOHelper.getTempFile(name.replaceAll("/", "."), null, entry.getValue());
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
	
	@Override
	public String getModName() {
		return getClass().getAnnotation(Mod.class).name();
	}

	@Override
	public String getModVersion() {
		return getClass().getAnnotation(Mod.class).version();
	}
	
}
