package com.heuristix.guns;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.audio.SoundPool;
import net.minecraft.client.audio.SoundPoolEntry;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.src.BaseMod;
import net.minecraft.src.ModLoader;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;

import paulscode.sound.SoundSystem;

import com.heuristix.TheGunMod;
import com.heuristix.guns.helper.ImageHelper;
import com.heuristix.guns.util.Log;
import com.heuristix.guns.util.ReflectionFacade;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/2/11
 * Time: 7:59 PM
 */
public class Util {

    private Util() { }

    static {
        ReflectionFacade.getInstance().putMethod(ClassLoader.class, "defineClass", "", String.class, byte[].class, int.class, int.class);
    }
    
    public static byte[] getStringBytes(String string) {
        byte[] stringBytes = string.getBytes();
        byte[] bytes = new byte[stringBytes.length + 1];
        System.arraycopy(stringBytes, 0, bytes, 0, stringBytes.length);
        bytes[bytes.length - 1] = 10;
        return bytes;
    }

    public static String normalize(String string) {
        String normalized = string.toLowerCase().replaceAll("_", " ");
        return normalized.substring(0, 1).toUpperCase() + normalized.substring(1, normalized.length());
    }

    private static final Map<Character, Character> NUMBER_LETTER_MAP = new HashMap<Character, Character>();

    static {
        NUMBER_LETTER_MAP.put('1', 'A');
        NUMBER_LETTER_MAP.put('2', 'B');
        NUMBER_LETTER_MAP.put('3', 'C');
        NUMBER_LETTER_MAP.put('4', 'D');
        NUMBER_LETTER_MAP.put('5', 'E');
        NUMBER_LETTER_MAP.put('6', 'F');
        NUMBER_LETTER_MAP.put('7', 'G');
        NUMBER_LETTER_MAP.put('8', 'H');
        NUMBER_LETTER_MAP.put('9', 'I');
        NUMBER_LETTER_MAP.put('0', 'J');
    }

    public static String numbersToText(String text) {
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (Character.isDigit(chars[i])) {
                chars[i] = NUMBER_LETTER_MAP.get(chars[i]);
            }
        }
        return new String(chars);
    }

    public static byte[] parseByteArray(String text) {
        if (text != null && !text.isEmpty()) {
            String[] texts = text.split(",");
            byte[] bytes = new byte[texts.length];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = Byte.parseByte(texts[i]);
            }
            return bytes;
        }
        return null;
    }

    public static int[] toIntArray(byte[] bytes) {
        int[] array = new int[bytes.length];
        for (int i = 0; i < array.length; i++)
            array[i] = bytes[i];
        return array;
    }

    public static Class<?> defineClass(byte[] code, String name) {
        return defineClass(code, name, Thread.currentThread().getContextClassLoader());
    }

    public static Class<?> defineClass(byte[] code, String name, ClassLoader cl) {
        return (Class<?>) ReflectionFacade.getInstance().invokeMethod(ClassLoader.class, cl, "defineClass", name, code, 0, code.length);
    }

    public static Class<?> getPrimitiveClass(final Class<?> clazz) {
        try{
            final java.lang.reflect.Field field = clazz.getDeclaredField("TYPE");
            final int modifiers = field.getModifiers();
            if(Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers)
                && Modifier.isFinal(modifiers)
                && Class.class.equals(field.getType())){
                return (Class<?>) field.get(null);
            }
        } catch (Exception e) {
            Log.getLogger().fine("Supplied class " + clazz + " is not a primitive class");
        }
        return null;
    }


    public static double distance(double x1, double x2, double y1, double y2, double z1, double z2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
    }

    public static Vec3 getProjectedPoint(EntityLiving living, double distance) {
        Vec3 pos = living.getPosition(1);
        Vec3 look = living.getLook(1);
        return getProjectedPoint(pos, look, distance);
    }

    public static Vec3 getProjectedPoint(Vec3 pos, Vec3 look, double distance) {
        return pos.addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
    }

    public static void playStreamingAtEntity(Entity entity, String sound, String stream, float f, float f1) {
        playStreaming(sound, stream, (float) entity.posX, (float) entity.posY, (float) entity.posZ, f, f1);
    }

    public static void playStreaming(String sound, String stream, float x, float y, float z, float v1, float v2) {
        Minecraft minecraft = Minecraft.getMinecraft();
    	SoundSystem sndSystem = SoundManager.sndSystem;;
            if (sndSystem.playing(stream)) {
                sndSystem.stop(stream);
            }
            if (sound == null) {
                return;
            }
            SoundPoolEntry soundpoolentry = minecraft.sndManager.soundPoolStreaming.getRandomSoundFromSoundPool(sound);
            if (soundpoolentry != null && v1 > 0) {
                float f5 = 16F;
                sndSystem.newStreamingSource(true, stream, soundpoolentry.soundUrl, soundpoolentry.soundName, false, x, y, z, 2, f5 * 4F);
                GameSettings options = getGameSettings(minecraft.sndManager);
                float volume = 1.0f;
                if (options != null) {
                    volume = options.soundVolume;
                }
                sndSystem.setVolume(stream, 0.5f * volume);
                sndSystem.play(stream);
            }
    }

    public static SoundPool[] getSoundPools(SoundManager sndManager) {
        SoundPool[] soundPools = new SoundPool[3];
        ReflectionFacade names = ReflectionFacade.getInstance();
        soundPools[0] = (SoundPool) names.getFieldValue(SoundManager.class, sndManager, "soundPoolSounds");
        soundPools[1] = (SoundPool) names.getFieldValue(SoundManager.class, sndManager, "soundPoolStreaming");
        soundPools[2] = (SoundPool) names.getFieldValue(SoundManager.class, sndManager, "soundPoolMusic");
        return soundPools;
    }

    public static GameSettings getGameSettings(SoundManager sndManager) {
        return (GameSettings) ReflectionFacade.getInstance().getFieldValue(SoundManager.class, sndManager, "options");
    }

    public static int[] getIntArray(byte[] bytes) {
        int[] ints = new int[bytes.length];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = bytes[i];
        }
        return ints;
    }

    public static byte[] getByteArray(int[] ints) {
        byte[] bytes = new byte[ints.length];
        for (int i = 0; i < ints.length; i++) {
            bytes[i] = (byte) ints[i];
        }
        return bytes;
    }

    public static Minecraft getMinecraft(EntityPlayerSP player) {
        Minecraft mc = (Minecraft) ReflectionFacade.getInstance().getFieldValue(EntityPlayerSP.class, player, "mc");
        if(mc != null) {
            return mc;
        }
        return null;
    }
    
    public static String getStringFromBytes(int[] bytes) {
        int i = 0;
        while (bytes[i++] != 10) ;
        return new String(bytes, 0, i - 1);
    }

    public static boolean isCreative(EntityPlayerMP player) {
        return player.theItemInWorldManager.isCreative();
    }

    public static void renderItemOverlayIntoGUI(FontRenderer fontRenderer, RenderEngine renderEngine, ItemStack stack, int minStack, int x, int y, int color, float scale) {
        if (stack == null) {
            return;
        }
        if (stack.stackSize >= minStack) {
            String string = String.valueOf(stack.stackSize);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            drawString(string, scale, (int) ((x + (19 * scale)) - (2 * scale) - fontRenderer.getStringWidth(string)), (int) (y + (scale * 9)), color, true, fontRenderer);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
        if (stack.isItemDamaged()) {
            int k = (int)Math.round(13 - ((double)stack.getItemDamageForDisplay() * 13) / (double)stack.getMaxDamage());
            int damagePercent = (int)Math.round(255 - ((double)stack.getItemDamageForDisplay() * 255) / (double)stack.getMaxDamage());
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            Tessellator t = Tessellator.instance;
            int i1 = 255 - damagePercent << 16 | damagePercent << 8;
            int j1 = (255 - damagePercent) / 4 << 16 | 0x3f00;
            renderQuad(t, x + 2, y + 13, 13, 2, 0);
            renderQuad(t, x + 2, y + 13, 12, 1, j1);
            renderQuad(t, x + 2, y + 13, k, 1, i1);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glColor4f(1, 1, 1, 1);
        }
    }

    public static void renderQuad(Tessellator t, int x, int y, int x1, int y1, int color) {
        t.startDrawingQuads();
        t.setColorOpaque_I(color);
        t.addVertex(x + 0, y + 0, 0);
        t.addVertex(x + 0, y + y1, 0);
        t.addVertex(x + x1, y + y1, 0);
        t.addVertex(x + x1, y + 0, 0);
        t.draw();
    }
    
    public static void renderTexturedQuad(Tessellator t, double x, double y, double z, double iconX, double iconY, double width, double height, double iconWidth, int iconHeight) {
        float scaleFactor1 = 0.00390625F;
        float scaleFactor2 = 0.00390625F;
        t.startDrawingQuads();
        t.addVertexWithUV(x + 0, y + height, z, (iconX + 0) * scaleFactor1, (iconY + iconHeight) * scaleFactor2);
        t.addVertexWithUV(x + width, y + height, z, (iconX + iconWidth) * scaleFactor1, (iconY + iconHeight) * scaleFactor2);
        t.addVertexWithUV(x + width, y + 0, z, (iconX + iconWidth) * scaleFactor1, (iconY + 0) * scaleFactor2);
        t.addVertexWithUV(x + 0, y + 0, z, (iconX + 0) * scaleFactor1, (iconY + 0) * scaleFactor2);
        t.draw();
    }

    public static void drawString(String string, float scale, int x, int y, int color, boolean shadow, FontRenderer renderer) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glScalef(scale, scale, scale);
        if(shadow) {
            renderer.drawStringWithShadow(string, 0, 0, color);
        } else {
            renderer.drawString(string, 0, 0, color);
        }
        GL11.glScalef(1, 1, 1);
        GL11.glTranslatef(-x, -y, 0);
        GL11.glPopMatrix();
    }

    public static void renderTexture(int texture, float opacity, Minecraft minecraft) {
        Dimension screenDimensions = getScaledDimensions(minecraft);
        GL11.glEnable(GL11.GL_BLEND);
        renderTexture(new Rectangle(screenDimensions.width, screenDimensions.height), texture, opacity);
    }

    public static Dimension getScaledDimensions(Minecraft minecraft) {
        ScaledResolution sr = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
        return new Dimension(sr.getScaledWidth(), sr.getScaledHeight());
    }

    public static void renderTexture(Rectangle rectangle, int texture, float opacity) {
        renderTexture(rectangle.x, rectangle.y, rectangle.width, rectangle.height, texture, opacity);
    }

    public static void renderTexture(int x, int y, int width, int height, int texture, float opacity) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1, 1, 1, opacity);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(x, x + height, -90, 0, 1);
        t.addVertexWithUV(x + width, y + height, -90, 1, 1);
        t.addVertexWithUV(x + width, y, -90, 1, 0);
        t.addVertexWithUV(x, y, -90, 0, 0);
        t.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public static Dimension getTextureDimensions(boolean items, RenderEngine engine) {
        if(!isDefaultTextureBound(items, engine)) {
            engine.bindTexture(items ? "/gui/items.png" : "/terrain.png");
        }
        return new Dimension(GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH) / 16, GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT) / 16);
    }

    private static boolean isDefaultTextureBound(boolean items, RenderEngine engine) {
        return GL11.glGetInteger(GL11.GL_TEXTURE_BINDING_2D) == engine.getTexture(items ? "/gui/items.png" : "/terrain.png");
    }

    public static byte[] resizeImageBytes(byte[] bytes, int width, int height) {
        return resizeImageBytes(bytes, width, height, "PNG");
    }

    public static byte[] resizeImageBytes(byte[] bytes, int width, int height, String format) {
        BufferedImage image = ImageHelper.readImage(bytes);
        if(image != null) {
            ImageHelper.resizeImage(image, width, height);
            return ImageHelper.writeImage(image, format);
        }
        return null;
    }

    public static void displayGUI(EntityPlayerMP player, IInventory inventory, Container container) {
        ReflectionFacade names = ReflectionFacade.getInstance();
        int currentWindowId = (Integer) names.getFieldValue(EntityPlayerMP.class, player, "currentWindowId");
        currentWindowId = currentWindowId % 100 + 1;
        ModLoader.serverSendPacket(player.playerNetServerHandler, getOpenWindowPayload(currentWindowId, ContainerCraftGuns.INVENTORY_TYPE, inventory.getInvName(), inventory.getSizeInventory(), player.dimension));
        player.openContainer = container;
        player.openContainer.windowId = currentWindowId;
        names.setFieldValue(EntityPlayerMP.class, player, "currentWindowId", currentWindowId);
    }

    public static Packet250CustomPayload getOpenWindowPayload(int currentWindowId, int type, String inventoryName, int size, int dimension) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bOut);
        try {
            out.write(currentWindowId);
            out.writeInt(type);
            //unknown
            out.writeInt(0);
            out.writeInt(0);
            out.writeInt(0);
            //end unknown
            out.write(dimension);
        } catch (IOException e) {
            Log.throwing(Util.class, "getOpenWindowPayload(int currentWindowId, int type, String inventoryName, int size, byte dimension)", e, TheGunMod.class);
        }
        return new Packet250CustomPayload("ML|OpenTE", bOut.toByteArray());
    }
}

