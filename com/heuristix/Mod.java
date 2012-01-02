package com.heuristix;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/1/11
 * Time: 10:12 AM
 */
public abstract class Mod extends BaseMod {

    public static final String CURRENT_VERSION = "1.0.0";

    private boolean texturesRegistered;
    private final HashSet<ModTextureStatic> textures;

    private boolean soundsRegistered;
    private final HashMap sounds;
    private static final String[] SOUND_KEYS = {"sounds", "music", "streaming"};

    public Mod() {
        this.textures = new HashSet<ModTextureStatic>();
        this.sounds = new HashMap();
    }

    protected void registerItem(ItemCustom item) {
        if(item.getIconPath() != null)
            item.setIconIndex(ModLoader.addOverride("/gui/items.png", item.getIconPath()));
        if(item.getItemName() == null)
            item.setItemName(item.getName());
        ModLoader.AddName(item, item.getName());
        if(item.getCraftingRecipe() != null && item.getCraftingRecipe().length > 0)
            ModLoader.AddRecipe(new ItemStack(item, item.getCraftingAmount()), item.getCraftingRecipe());
    }

    public String Version() {
        return CURRENT_VERSION;
    }

    public abstract String getVersion();

    public final boolean OnTickInGame(float tick, Minecraft minecraft) {
        if(!texturesRegistered) {
            if(minecraft.renderEngine != null) {
                for(ModTextureStatic texture : textures)
                    minecraft.renderEngine.registerTextureFX(texture);
                texturesRegistered = true;
            }
        }
        if(!soundsRegistered) {
            registerAllSounds(minecraft );
        }
        return OnTick(tick, minecraft);
    }

    public boolean OnTick(float tick, Minecraft minecraft) {
        return true;
    }

    public int registerTexture(BufferedImage texture, boolean item) {
        int iconIndex = ModLoader.getUniqueSpriteIndex((item) ? "/gui/items.png":"/terrain.png");
        textures.add(new ModTextureStatic(iconIndex, (item) ? 1:0, texture));
        return iconIndex;
    }

    public void registerSound(String name, byte[] bytes) {
        HashMap sounds = (HashMap) this.sounds.get(SOUND_KEYS[0]);
        if(sounds == null)
            sounds = new HashMap();
        sounds.put(name, bytes);
        this.sounds.put(SOUND_KEYS[0], sounds);
    }

    public void registerMusic(String name, byte[] bytes) {
        HashMap music = (HashMap) this.sounds.get(SOUND_KEYS[1]);
        if(music == null)
            music = new HashMap();
        music.put(name, bytes);
        this.sounds.put(SOUND_KEYS[1], music);
    }

    public void registerStreaming(String name, byte[] bytes) {
        HashMap streaming = (HashMap) this.sounds.get(SOUND_KEYS[2]);
        if(streaming == null)
            streaming = new HashMap();
        streaming.put(name, bytes);
        this.sounds.put(SOUND_KEYS[2], streaming);
    }

    public void registerAllSounds(Minecraft minecraft) {
        if(minecraft.sndManager != null) {
            SoundPool[] soundPools = Util.getSoundPools(minecraft.sndManager);
            try {
                for(int i = 0; i < soundPools.length; i++) {
                    soundPools[i].isGetRandomSound = false;
                }
            } catch (NullPointerException e) { }
            for(int i = 0; i < SOUND_KEYS.length; i++) {
                HashMap<String, byte[]> sounds = (HashMap<String, byte[]>) this.sounds.get(SOUND_KEYS[i]);
                if(sounds != null) {
                    for(Map.Entry<String, byte[]> entry: sounds.entrySet()) {
                        String name = entry.getKey();
                        File soundFile = Util.getTempFile(name.replaceAll("/", "."), null, entry.getValue());
                        if(soundFile != null) {
                            switch(i) {
                                case 0:
                                    minecraft.sndManager.addSound(name, soundFile);
                                    break;
                                case 1:
                                    minecraft.sndManager.addMusic(name, soundFile);
                                    break;
                                case 2:
                                    minecraft.sndManager.addStreaming(name, soundFile);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                }
            }
            try {
                for(int i = 0; i < soundPools.length; i++) {
                    soundPools[i].isGetRandomSound = true;
                }
            } catch (NullPointerException e) { }
            soundsRegistered = true;
        }
    }


}
