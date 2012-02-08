package com.heuristix;

import com.heuristix.util.Log;
import com.heuristix.util.ReflectionFacade;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
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
    private final Set<TextureFX> textures;

    private boolean soundsRegistered;
    private final Map<String, HashMap<String, byte[]>> sounds;
    private static final String[] SOUND_KEYS = {"sounds", "music", "streaming"};
    private static Class<? extends TextureMultipleFX> currentHDTextureClass;

    public ModMP() {
        this.textures = new HashSet<TextureFX>();
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
            ItemStack stack = new ItemStack(item, item.getCraftingAmount());
            if(item.isShapelessRecipe()) {
                ModLoader.AddShapelessRecipe(stack, item.getCraftingRecipe());
            } else {
                ModLoader.AddRecipe(stack, item.getCraftingRecipe());
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
        if(block.getIconPath() != null) {
             textureIndex = ModLoader.addOverride("/terrain.png", block.getIconPath());
        }
        block.blockIndexInTexture = textureIndex;
        ModLoader.AddName(block, block.getName());
        if(block.getCraftingRecipe() != null && block.getCraftingRecipe().length > 0) {
            ItemStack stack = new ItemStack(block, block.getCraftingAmount());
            if(block.isShapelessRecipe()) {
                ModLoader.AddShapelessRecipe(stack, block.getCraftingRecipe());
            } else {
                ModLoader.AddRecipe(stack, block.getCraftingRecipe());
            }
        }
        Item.itemsList[block.blockID] = new ItemBlock(block.blockID - 256);
    }

    public final boolean OnTickInGame(float tick, Minecraft minecraft) {
        if (!texturesRegistered && minecraft.renderEngine != null) {
            for (TextureFX texture : textures) {
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
        Class[] paramTypes = new Class[]{int.class, boolean.class, boolean.class, BufferedImage[].class};
        Object[] params = new Object[]{0, item, true, textures};
        Class textureFXClass = getCurrentHDTextureClass();
        if(TextureDefaultMultipleFX.class.isAssignableFrom(textureFXClass)) {
            paramTypes = new Class[]{int.class, boolean.class, boolean.class, RenderEngine.class, BufferedImage[].class};
            params = new Object[]{0, item, true, ModLoader.getMinecraftInstance().renderEngine, textures};
        }
        TextureMultipleFX textureFX = (TextureMultipleFX) ReflectionFacade.getInstance().invokeConstructor(textureFXClass, paramTypes, params);
        return registerTexture(textureFX);
    }

    public int registerTexture(TextureFX texture) {
        int iconIndex = ModLoader.getUniqueSpriteIndex((texture.tileImage == 1) ? "/gui/items.png" : "/terrain.png");
        texture.iconIndex = iconIndex;
        textures.add(texture);
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
            InputStream stream = getClass().getResourceAsStream(path);
            if(stream != null) {
                return ImageIO.read(stream);
            }
        } catch (IOException e) {
            Log.throwing(getClass(), "getImageResource(String path)", e, getClass());
        }
        return null;
    }

    public static Class<? extends TextureMultipleFX> getCurrentHDTextureClass() {
        if(currentHDTextureClass == null) {
            currentHDTextureClass = getHDTextureCompatibleClass();
            Class[] params = new Class[]{int.class, boolean.class, boolean.class, BufferedImage[].class};
            if(TextureDefaultMultipleFX.class.isAssignableFrom(currentHDTextureClass)) {
                params = new Class[]{int.class, boolean.class, boolean.class, RenderEngine.class, BufferedImage[].class};
            }
            ReflectionFacade.getInstance().putConstructor(currentHDTextureClass, params);
        }
        return currentHDTextureClass;
    }

    public static Class<? extends TextureMultipleFX> getHDTextureCompatibleClass() {
        try {
            if(Class.forName("TextureHDFX") != null) {
                return TextureOptifineMultipleFX.class;
            }
        } catch(ClassNotFoundException e) {
            Log.getLogger().throwing(ModMP.class.getName(), "getHDTextureCompatibleClass()", e);
        }
        try {
            if(Class.forName("com.pclewis.mcpatcher.mod.TileSize") != null) {
                return TexturePatchedMultipleFX.class;
            }
        } catch(ClassNotFoundException e) {
            Log.getLogger().throwing(ModMP.class.getName(), "getHDTextureCompatibleClass()", e);
        }
        return TextureDefaultMultipleFX.class;
    }


}
