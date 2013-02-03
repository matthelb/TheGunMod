package com.heuristix.guns;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundPool;
import net.minecraft.client.renderer.texturefx.TextureFX;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.src.BaseMod;
import net.minecraft.src.ModLoader;

import com.heuristix.guns.helper.IOHelper;
import com.heuristix.guns.util.Log;
import com.heuristix.guns.util.ReflectionFacade;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/1/11
 * Time: 10:12 AM
 */
public abstract class HMod extends BaseMod implements IMod {

    //TODO change for new release
    public static final String CURRENT_VERSION = "1.4.5";

    public static final float PIXELS_PER_ICON = 16;

    private boolean texturesRegistered;
    private final Set<TextureFX> textures;

    private boolean soundsRegistered;
    private final Map<String, HashMap<String, byte[]>> sounds;

    private boolean hookedIngame;

    private static final String[] SOUND_KEYS = {"sounds", "music", "streaming"};
    private static Class<? extends TextureMultipleFX> currentHDTextureClass;

    public HMod() {
        this.textures = new HashSet<TextureFX>();
        this.sounds = new HashMap<String, HashMap<String, byte[]>>();
    }

    @Override
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
            config.store(new FileOutputStream(configFile), getModName() + " v" + getVersion() + " Configuration");
            loadConfig(config);
        } catch (IOException e) {
            Log.fine("Failed to read config file for " + getClass().getName(), getClass());
        }
        init();
    }

    protected void init() { }

    @Override
    public String getVersion() {
        return getModVersion() + " for " + CURRENT_VERSION;
    }

    protected void registerItem(ItemCustom item) {
        if (item.getItemName() == null) {
            item.setItemName(item.getName());
        }
        ModLoader.addName(item, item.getName());
        if (item.hasWorkbenchRecipe() && item.getCraftingRecipe() != null && item.getCraftingRecipe().length > 0) {
            ItemStack stack = new ItemStack(item, item.getCraftingAmount());
            if(item.isShapelessRecipe()) {
                ModLoader.addShapelessRecipe(stack, item.getCraftingRecipe());
            } else {
                ModLoader.addRecipe(stack, item.getCraftingRecipe());
            }
        }
    }

    protected <B extends Block & CustomEntity> void registerBlock(B block) {
        registerBlock(block, -1);
    }

    protected <B extends Block & CustomEntity> void registerBlock(B block, int textureIndex) {
        if(block.getBlockName() == null) {
            block.setBlockName(block.getName());
        }
        block.blockIndexInTexture = textureIndex;
        ModLoader.addName(block, block.getName());
        if(block.getCraftingRecipe() != null && block.getCraftingRecipe().length > 0) {
            ItemStack stack = new ItemStack(block, block.getCraftingAmount());
            if(block.isShapelessRecipe()) {
                ModLoader.addShapelessRecipe(stack, block.getCraftingRecipe());
            } else {
                ModLoader.addRecipe(stack, block.getCraftingRecipe());
            }
        }
        Item.itemsList[block.blockID] = new ItemBlock(block.blockID - 256);
    }

    public final boolean onTickInGame(float tick, Minecraft minecraft) {
        if (!texturesRegistered && minecraft.renderEngine != null) {
            for (TextureFX texture : textures) {
                if(texture instanceof TextureDefaultMultipleFX) {
                    ((TextureDefaultMultipleFX) texture).setRenderEngine(minecraft.renderEngine);
                }
                 minecraft.renderEngine.registerTextureFX(texture);
            }
            texturesRegistered = true;
        }
        if (!soundsRegistered) {
            registerAllSounds(minecraft);
            if (!hookedIngame) {
                return false;
            }
        }
        return onTick(tick, minecraft);
    }

    public boolean onTick(float tick, Minecraft minecraft) {
        return true;
    }

    public boolean isHookedIngame() {
        return hookedIngame;
    }

    public void setHookedIngame(boolean hookedIngame) {
        this.hookedIngame = hookedIngame;
        ModLoader.setInGameHook(this, hookedIngame, false);
    }

    public Integer[] registerTextures(boolean items, BufferedImage base16IconSheet, int textureSize) {
        List<Integer> textureIndices = new ArrayList<Integer>();
        for(int y = 0; y < base16IconSheet.getHeight(); y += textureSize) {
            for(int x = 0; x < base16IconSheet.getWidth(); x += textureSize) {
                BufferedImage texture = base16IconSheet.getSubimage(x, y, textureSize, textureSize);
                if(texture != null) {
                    textureIndices.add(registerTexture(items, texture));
                }
            }
        }
        return textureIndices.toArray(new Integer[textureIndices.size()]);
    }

    public int registerTexture(boolean item, BufferedImage... textures) {
        Class<?> textureFXClass = null;
        TextureMultipleFX textureFX = (TextureMultipleFX) ReflectionFacade.getInstance().invokeConstructor(textureFXClass, new Class[]{int.class, boolean.class, BufferedImage[].class}, 0, item, textures);
        return registerTexture(textureFX);
    }

    public int registerTexture(TextureFX texture) {
        int iconIndex;
        try {
            iconIndex = ModLoader.getUniqueSpriteIndex((texture.tileImage == 1) ? "/gui/items.png" : "/terrain.png");
        } catch (Exception e) {
            Log.getLogger().fine("No more sprite indices left!");
            return -1;
        }
        texture.iconIndex = iconIndex;
        textures.add(texture);
        ModLoader.setInGameHook(this, true, false);
        return iconIndex;
    }

    public void registerSound(String name, byte[] bytes) {
        HashMap<String, byte[]> sounds = this.sounds.get(SOUND_KEYS[0]);
        if (sounds == null) {
            sounds = new HashMap<String, byte[]>();
        }
        sounds.put(name, bytes);
        this.sounds.put(SOUND_KEYS[0], sounds);
        ModLoader.setInGameHook(this, true, false);
    }

    public void registerMusic(String name, byte[] bytes) {
        HashMap<String, byte[]> music = this.sounds.get(SOUND_KEYS[1]);
        if (music == null) {
            music = new HashMap<String, byte[]>();
        }
        music.put(name, bytes);
        this.sounds.put(SOUND_KEYS[1], music);
        ModLoader.setInGameHook(this, true, false);
    }

    public void registerStreaming(String name, byte[] bytes) {
        HashMap<String, byte[]> streaming = this.sounds.get(SOUND_KEYS[2]);
        if (streaming == null) {
            streaming = new HashMap<String, byte[]>();
        }
        streaming.put(name, bytes);
        this.sounds.put(SOUND_KEYS[2], streaming);
        ModLoader.setInGameHook(this, true, false);
    }

    public void registerAllSounds(Minecraft minecraft) {
        if (minecraft.sndManager != null) {
            SoundPool[] soundPools = Util.getSoundPools(minecraft.sndManager);
            for (int i = 0; i < soundPools.length; i++) {
                if(soundPools[i] != null) {
                    soundPools[i].isGetRandomSound = false;
                }
            }
            for (int i = 0; i < SOUND_KEYS.length; i++) {
                HashMap<String, byte[]> sounds = this.sounds.get(SOUND_KEYS[i]);
                if (sounds != null) {
                    for (Map.Entry<String, byte[]> entry : sounds.entrySet()) {
                        String name = entry.getKey();
                        File soundFile = IOHelper.getTempFile(name.replaceAll("/", "."), null, entry.getValue());
                        if (soundFile != null) {
                            switch (i) {
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
            for (int i = 0; i < soundPools.length; i++) {
                if(soundPools[i] != null) {
                    soundPools[i].isGetRandomSound = true;
                }
            }
            soundsRegistered = true;
        }
    }

    public BufferedImage getImageResource(String path) {
        try {
            InputStream stream = getClass().getResourceAsStream(path);
            if(stream != null) {
                return ImageIO.read(stream);
            }
        } catch (IOException e) {
            Log.throwing(getClass(), "getImageResource(String path)", e, getClass());
        }
        return null;
    }


}
