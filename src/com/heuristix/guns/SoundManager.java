package com.heuristix.guns;

import java.io.File;
import java.util.Map;

import com.heuristix.guns.helper.IOHelper;

public class SoundManager {

	private final SoundMap[] soundMaps;
	
	public SoundManager() {
		this.soundMaps = new SoundMap[3];
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
		SoundMap sounds = soundMaps[index];
		if (sounds == null) {
			sounds = new SoundMap();	
		}
		sounds.put(path, data);
		soundMaps[index] = sounds;
		
	}
	
	public void registerAllSounds(net.minecraft.client.audio.SoundManager manager, String folder) {
		for (int i = 0; i < soundMaps.length; i++) {
			SoundMap sounds = soundMaps[i];
			if (sounds != null) {
				for (Map.Entry<String, byte[]> entry : sounds.entrySet()) {
					String name = entry.getKey();
					File soundFile = IOHelper.getSysTempFile(name.replaceAll("/", "."), null, entry.getValue());
					if (soundFile != null) {
						switch (i) {
							case 0:
								manager.addSound(name, soundFile);
								break;
							case 1:
								manager.addMusic(name, soundFile);
								break;
							case 2:
								manager.addStreaming(name, soundFile);
								break;
							default:
								break;
						}
					}
				}
			}
		}
	}

}
