package com.heuristix;

import com.heuristix.guns.BlockCraftGuns;
import com.heuristix.util.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/1/11
 * Time: 10:12 AM
 */
public abstract class ModMP extends BaseModMp implements Mod {

    public static final String CURRENT_VERSION = "1.1.0";

    public static final float PIXELS_PER_ICON = 16;

    private boolean texturesRegistered;
    private final Set<ModTextureStatic> textures;

    private boolean soundsRegistered;
    private final Map<String, HashMap<String, byte[]>> sounds;
    private static final String[] SOUND_KEYS = {"sounds", "music", "streaming"};

    public ModMP() {
        this.textures = new HashSet<ModTextureStatic>();
        this.sounds = new HashMap();
    }

    @Override
    public String getVersion() {
        return getModVersion() + " for " + CURRENT_VERSION;
    }

    protected void registerItem(ItemCustom item) {
        if (item.getIconPath() != null) {
            item.setIconIndex(ModLoader.addOverride("/gui/items.png", item.getIconPath()));
        }
        if (item.getItemName() == null) {
            item.setItemName(item.getName());
        }
        ModLoader.AddName(item, item.getName());
        if (item.hasWorkbenchRecipe() && item.getCraftingRecipe() != null && item.getCraftingRecipe().length > 0) {
            ModLoader.AddRecipe(new ItemStack(item, item.getCraftingAmount()), item.getCraftingRecipe());
        }
    }

    protected <B extends Block & CustomEntity> void registerBlock(B block) {
        registerBlock(block, -1);
    }
    protected <B extends Block & CustomEntity> void registerBlock(B block, int textureIndex) {
        if(block.getBlockName() == null) {
            block.setBlockName(block.getName());
        }
        if(block.getIconPath() != null) {
             textureIndex = ModLoader.addOverride("/terrain.png", block.getIconPath());
        }
        block.blockIndexInTexture = textureIndex;
        ModLoader.AddName(block, block.getName());
        if(block.getCraftingRecipe() != null && block.getCraftingRecipe().length > 0) {
            ModLoader.AddRecipe(new ItemStack(block, block.getCraftingAmount()), block.getCraftingRecipe());
        }
        Item.itemsList[block.blockID] = new ItemBlock(block.blockID - 256);
    }

    public final boolean OnTickInGame(float tick, Minecraft minecraft) {
        if (!texturesRegistered && minecraft.renderEngine != null) {
            for (ModTextureStatic texture : textures) {
                 minecraft.renderEngine.registerTextureFX(texture);
            }
            texturesRegistered = true;
        }
        if (!soundsRegistered) {
            registerAllSounds(minecraft);
        }
        return OnTick(tick, minecraft);
    }

    public boolean OnTick(float tick, Minecraft minecraft) {
        return true;
    }

    public Integer[] registerTextures(BufferedImage base16IconSheet, int textureSize, boolean items) {
        List<Integer> textureIndices = new ArrayList<Integer>();
        for(int y = 0; y < base16IconSheet.getHeight(); y += textureSize) {
            for(int x = 0; x < base16IconSheet.getWidth(); x += textureSize) {
                BufferedImage texture = base16IconSheet.getSubimage(x, y, textureSize, textureSize);
                if(texture != null) {
                    textureIndices.add(registerTexture(texture, items));
                }
            }
        }
        return textureIndices.toArray(new Integer[textureIndices.size()]);
    }

    public int registerTexture(BufferedImage texture, boolean item) {
        int iconIndex = ModLoader.getUniqueSpriteIndex((item) ? "/gui/items.png" : "/terrain.png");
        textures.add(new ModTextureStatic(iconIndex, (item) ? 1 : 0, texture));
        return iconIndex;
    }

    public void registerSound(String name, byte[] bytes) {
        HashMap sounds = this.sounds.get(SOUND_KEYS[0]);
        if (sounds == null) {
            sounds = new HashMap();
        }
        sounds.put(name, bytes);
        this.sounds.put(SOUND_KEYS[0], sounds);
    }

    public void registerMusic(String name, byte[] bytes) {
        HashMap music = this.sounds.get(SOUND_KEYS[1]);
        if (music == null) {
            music = new HashMap();
        }
        music.put(name, bytes);
        this.sounds.put(SOUND_KEYS[1], music);
    }

    public void registerStreaming(String name, byte[] bytes) {
        HashMap streaming = this.sounds.get(SOUND_KEYS[2]);
        if (streaming == null) {
            streaming = new HashMap();
        }
        streaming.put(name, bytes);
        this.sounds.put(SOUND_KEYS[2], streaming);
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
                        File soundFile = Util.getTempFile(name.replaceAll("/", "."), null, entry.getValue());
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
            return ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            Log.fine("Could not load image resource " + path, getClass());
        }
        return null;
    }


}
