package com.heuristix.guns;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import net.minecraft.client.texturepacks.ITexturePack;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.sound.SoundLoadEvent;

import com.heuristix.guns.client.render.TextureManager;
import com.heuristix.guns.client.render.TextureMap;
import com.heuristix.guns.util.Log;
import com.heuristix.guns.util.ReflectionFacade;

import cpw.mods.fml.common.LoaderException;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public abstract class BaseMod implements IMod {

	protected final SoundManager soundManager;
	protected final TextureMap textureMap;
	
	private boolean texturesRegistered, soundsRegistered;

	public BaseMod() {
		this.soundManager = new SoundManager();
		this.textureMap = new TextureMap();
	}
	
	public final void load() {
        try {
            Log.addHandler(this);
        } catch (IOException e) {
            Log.getLogger().log(Level.SEVERE, "Could not initiate LogHandler for " + this.getClass());
        }
        try {
            File configFile = getConfigFile();
            Properties defaultConfig = getDefaultConfig();
            Properties config = defaultConfig;
            if (configFile.exists()) {
                config.load(new FileInputStream(configFile));
                if (!config.getProperty(getPropertiesId() + ".version").equals(defaultConfig.getProperty(getPropertiesId() + ".version"))) {
                    for (Map.Entry<Object, Object> entry : defaultConfig.entrySet()) {
                        if (!config.contains(entry.getKey())) {
                            config.put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }
            config.store(new FileOutputStream(configFile), getModName() + " v" + getModVersion() + " Configuration");
            loadConfig(config);
        } catch (IOException e) {
            Log.fine("Failed to read config file for " + getClass().getName(), getClass());
        }
    }
	
	public void registerItem(ItemCustom item) {
		item.setUnlocalizedName(item.getName().toLowerCase().replaceAll(" ", "_"));
		LanguageRegistry.addName(item, item.getName());
		try {
			GameRegistry.registerItem(item, item.getUnlocalizedName());
		} catch (LoaderException ignored) { }
	}
	
	public void registerBlock(BlockCustom block) {
		block.setUnlocalizedName(block.getName().toLowerCase().replaceAll(" ", "_"));
		LanguageRegistry.addName(block, block.getName());
		GameRegistry.registerBlock(block, ItemBlock.class, block.getUnlocalizedName());
		
	}

	public void registerAllSounds(SoundLoadEvent event) {
		soundManager.registerAllSounds(event.manager, getPropertiesId());
		soundsRegistered = true;
	}
	
	public void registerTextures() {
		textureMap.writeTemporaryTextures(getPropertiesId());		
	}
	
	public boolean isTexturesRegistered() {
		return texturesRegistered;
	}

	public boolean isSoundsRegistered() {
		return soundsRegistered;
	}

	public TextureMap getTextureMap() {
		return textureMap;
	}
	
	@Override
	public String getModName() {
		return getClass().getAnnotation(Mod.class).name();
	}

	@Override
	public String getModVersion() {
		return getClass().getAnnotation(Mod.class).version();
	}
	
	public void addURLToClassLoader(URL url, URLClassLoader loader) {
		ReflectionFacade.getInstance().invokeMethod(URLClassLoader.class, loader, "addURL", url);
	}
	
	static {
		ReflectionFacade.getInstance().putMethod(URLClassLoader.class, "addURL", "", URL.class);
	}
	
}
