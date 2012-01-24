package com.heuristix;

import com.heuristix.net.PacketDamageItem;
import com.heuristix.util.ObfuscatedNames;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import paulscode.sound.SoundSystem;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.jar.JarFile;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/2/11
 * Time: 7:59 PM
 */
public class Util {

    public static final float PI = 3.141593f;

    private static final Random RANDOM = new Random();

    private Util() { }

    public static int nextInt() {
        return RANDOM.nextInt();
    }

    public static int nextInt(int n) {
        return RANDOM.nextInt(n);
    }

    public static double nextGaussian() {
        return RANDOM.nextGaussian();
    }

    public static float nextFloat() {
        return RANDOM.nextFloat();
    }

    public static int randomInt(int min, int max) {
        int realMin = Math.min(min, max);
        int realMax = Math.max(min, max);
        int difference = realMax - realMin;
        if (difference <= 0) {
            difference++;
        }
        return nextInt(++difference) + realMin;
    }

    public static float randomFloat(float min, float max) {
        return Math.min(min, max) + nextFloat() * Math.abs(max - min);
    }

    public static double limit(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public static float toRadians(double deg) {
        return (float) (deg / 180.0f * PI);
    }

    public static float toDegrees(double radians) {
        return (float) (radians * 180 / PI);
    }

    public static boolean remove(InventoryPlayer inventory, int id, int amount) {
        while (amount > 0) {
            int slot = getItemSlot(inventory, id);
            if (slot == -1)
                return false;
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack != null) {
                int reduce = Math.min(stack.stackSize, amount);
                if (reduce > amount)
                    reduce = amount;
                inventory.decrStackSize(slot, amount);
                if ((amount -= reduce) == 0)
                    break;
            }
        }
        return true;
    }

    public static int getItemSlot(InventoryPlayer inventory, int id) {
        for (int i = 0; i < inventory.mainInventory.length; i++) {
            if (inventory.mainInventory[i] != null && inventory.mainInventory[i].itemID == id)
                return i;
        }
        return -1;
    }

    public static int getCount(InventoryPlayer inventory, int id) {
        int count = 0;
        ItemStack[][] stacks = new ItemStack[][]{inventory.mainInventory, inventory.armorInventory};
        for (int i = 0; i < stacks.length; i++) {
            for (int j = 0; j < stacks[i].length; j++) {
                ItemStack is = stacks[i][j];
                if (is != null && is.itemID == id)
                    count += (is.stackSize == 0) ? 1 : is.stackSize;
            }
        }
        return count;
    }

    public static void damageItem(ItemStack item, EntityPlayer player, int damage, Class modClass, World world) {
        if(world.multiplayerWorld) {
            Util.sendPacket(new PacketDamageItem(item.itemID, player.inventory.currentItem, damage), modClass);
        } else {
            item.damageItem(damage, player);
        }
    }

    private static File HOME_DIRECTORY;

    public static File getHomeDirectory() {
        if (HOME_DIRECTORY == null) {
            HOME_DIRECTORY = new File(System.getProperty("user.home"));
        }
        return HOME_DIRECTORY;
    }

    public static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage scaled = new BufferedImage(width, height, image.getType());
        scaled.createGraphics().drawImage(image, 0, 0, width, height, null);
        return scaled;
    }

    public static byte[] read(URL url) {
        InputStream in = null;
        try {
            in = url.openStream();
            return read(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    public static byte[] read(File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            return read(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
    }

    public static byte[] read(InputStream is) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        try {
            while ((n = is.read(buffer)) != -1) {
                os.write(buffer, 0, n);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return os.toByteArray();
    }

    public static byte[] getStringBytes(String string) {
        byte[] stringBytes = string.getBytes();
        byte[] bytes = new byte[stringBytes.length + 1];
        System.arraycopy(stringBytes, 0, bytes, 0, stringBytes.length);
        bytes[bytes.length - 1] = 10;
        return bytes;
    }

    public static File getAppDir(String dir) {
        String home = System.getProperty("user.home", ".");
        File file = null;
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            String appdata = System.getenv("APPDATA");
            if (appdata != null) {
                file = new File(appdata, "." + dir + '/');
            } else {
                file = new File(home, '.' + dir + '/');
            }
        } else if (os.contains("linux") || os.contains("solaris")) {
            file = new File(home, '.' + dir + File.separator);
        } else if (os.contains("mac")) {
            file = new File(home, "Library/Application Support/" + dir);
        } else if (file != null && !file.exists() && !file.mkdirs())
            return null;
        return file;
    }

    public static File getMinecraftDir(String dir) {
        File minecraftDir = getAppDir("minecraft");
        if (minecraftDir != null)
            return new File(minecraftDir.getAbsolutePath() + File.separator + dir);
        return null;
    }

    public static File getHeuristixDir(String dir) {
        File heuristixDir = getMinecraftDir("heuristix");
        if (heuristixDir != null) {
            heuristixDir = new File(heuristixDir.getAbsoluteFile() + File.separator + dir);
            if (!heuristixDir.exists())
                heuristixDir.mkdirs();
            return heuristixDir;
        }
        return null;
    }

    private static JarFile minecraftJar;

    public static JarFile getMinecraftJar() {
        if (minecraftJar == null) {
            File file = new File(getMinecraftDir("bin") + File.separator + "minecraft.jar");
            if (file.exists()) {
                try {
                    minecraftJar = new JarFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return minecraftJar;
    }

    public static File getFile(String name, File dir) {
        for (File file : dir.listFiles()) {
            if (file.getName().equals(name))
                return file;
        }
        return null;
    }

    public static File getTempFile(String name, String format, byte[] value) {
        FileOutputStream out = null;
        try {
            File file = File.createTempFile(name, format);
            out = new FileOutputStream(file);
            out.write(value);
            return file;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
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

    public static Class defineClass(byte[] code, String name) {
        return defineClass(code, name, Thread.currentThread().getContextClassLoader());
    }

    private static Method methodDefineClass;

    public static Class defineClass(byte[] code, String name, ClassLoader cl) {
        try {
            if (methodDefineClass == null) {
                methodDefineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
                methodDefineClass.setAccessible(true);
            }
            return (Class) methodDefineClass.invoke(cl, name, code, 0, code.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double distance(double x1, double x2, double y1, double y2, double z1, double z2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
    }

    public static Vec3D getProjectedPoint(EntityLiving living, double distance) {
        Vec3D pos = living.getPosition(1);
        Vec3D look = living.getLook(1);
        return getProjectedPoint(pos, look, distance);
    }

    public static Vec3D getProjectedPoint(Vec3D pos, Vec3D look, double distance) {
        return pos.addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
    }

    public static void playStreamingAtEntity(Entity entity, String sound, String stream, float f, float f1, Minecraft minecraft) {
        playStreaming(sound, stream, (float) entity.posX, (float) entity.posY, (float) entity.posZ, f, f1, minecraft);
    }

    public static void playStreaming(String sound, String stream, float x, float y, float z, float v1, float v2, Minecraft minecraft) {
        SoundSystem sndSystem = getSoundSystem(minecraft);
        if(sndSystem != null) {
            if (sndSystem.playing(stream)) {
                sndSystem.stop(stream);
            }
            if (sound == null) {
                return;
            }
            SoundPoolEntry soundpoolentry = getSoundPools(minecraft.sndManager)[1].getRandomSoundFromSoundPool(sound);
            if (soundpoolentry != null && v1 > 0) {
                float f5 = 16F;
                sndSystem.newStreamingSource(true, stream, soundpoolentry.soundUrl, soundpoolentry.soundName, false, x, y, z, 2, f5 * 4F);
                GameSettings options = getGameSettings(minecraft.sndManager);
                float volume = 1.0f;
                if (options != null)
                    volume = options.soundVolume;
                sndSystem.setVolume(stream, 0.5f * volume);
                sndSystem.play(stream);
            }
        }
    }

    public static SoundSystem getSoundSystem(Minecraft minecraft) {
        if (minecraft != null) {
            return getSoundSystem(minecraft.sndManager);
        }
        return null;
    }

    public static SoundSystem getSoundSystem(SoundManager sndManager) {
        if (sndManager != null) {
            return (SoundSystem) ObfuscatedNames.getInstance().getFieldValue(SoundManager.class, sndManager, "sndSystem");
        }
        return null;
    }

    private static final Map<Class, Map<String, String>> OBFUSCATED_NAMES = new HashMap<Class, Map<String, String>>();

    static {
        ObfuscatedNames names = ObfuscatedNames.getInstance();
        names.putField(SoundManager.class, "options", "f");
        names.putField(SoundManager.class, "sndSystem", "a");
        names.putField(SoundManager.class, "soundPoolMusic", "d");
        names.putField(SoundManager.class, "soundPoolSounds", "b");
        names.putField(SoundManager.class, "soundPoolStreaming", "c");
        names.putField(EntityPlayerSP.class, "mc", "b");
        names.putField(Packet.class, "addIdClassMapping", "a");
        names.putField(RenderItem.class, "renderBlocks", "c");
        names.putField(RenderPlayer.class, "modelBiped", "c");
        names.putMethod(Packet.class, "addIdClassMapping", "a", int.class, boolean.class, boolean.class, Class.class);
    }

    public static SoundPool[] getSoundPools(SoundManager sndManager) {
        SoundPool[] soundPools = new SoundPool[3];
        ObfuscatedNames names = ObfuscatedNames.getInstance();
        soundPools[0] = (SoundPool) names.getFieldValue(SoundManager.class, sndManager, "soundPoolSounds");
        soundPools[1] = (SoundPool) names.getFieldValue(SoundManager.class, sndManager, "soundPoolStreaming");
        soundPools[2] = (SoundPool) names.getFieldValue(SoundManager.class, sndManager, "soundPoolMusic");
        return soundPools;
    }

    public static GameSettings getGameSettings(SoundManager sndManager) {
        return (GameSettings) ObfuscatedNames.getInstance().getFieldValue(SoundManager.class, sndManager, "options");
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
        Minecraft mc = (Minecraft) ObfuscatedNames.getInstance().getFieldValue(EntityPlayerSP.class, player, "mc");
        if(mc != null) {
            return mc;
        }
        return null;
    }

    public static RenderBlocks getBlockRender(RenderItem renderItem) {
        return (RenderBlocks) ObfuscatedNames.getInstance().getFieldValue(RenderItem.class, renderItem, "renderBlocks");
    }

    public static ModelBiped getModelBiped(RenderPlayer render) {
        return (ModelBiped) ObfuscatedNames.getInstance().getFieldValue(RenderPlayer.class, render, "modelBipedMain");
    }

    public static BaseMod getLoadedMod(Class clazz) {
        List<BaseMod> mods = ModLoader.getLoadedMods();
        Iterator itr = mods.iterator();
        while(itr.hasNext()) {
            BaseMod next = (BaseMod) itr.next();
            if(next.getClass().equals(clazz))
                return next;
        }
        return null;
    }

    public static int getModMPId(BaseMod mod) {
        return mod.toString().hashCode();
    }

    public static int getModMPId(Class clazz) {
        return getModMPId(getLoadedMod(clazz));
    }

    public static void sendPacket(Packet230ModLoader packet, Class<? extends ModMP> modClass) {
        packet.modId = Util.getModMPId(modClass);
        sendPacket(packet);
    }

    public static void sendPacket(Packet packet) {
        ModLoader.getMinecraftInstance().getSendQueue().addToSendQueue(packet);
    }


    public static void setPacketId(Class packetClass, int id, boolean client, boolean server) {
         ObfuscatedNames.getInstance().invokeMethod(Packet.class, null, "addIdClassMapping", id, client, server, packetClass);
    }

    public static String getStringFromBytes(int[] bytes) {
        int i = 0;
        while (bytes[i++] != 10) ;
        return new String(bytes, 0, i - 1);
    }

    public static boolean isCreative(EntityPlayerSP player) {
        return getMinecraft(player).playerController.isInCreativeMode();
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

    public static void drawItemIntoGui(RenderBlocks renderBlocks, FontRenderer fontRenderer, RenderEngine renderEngine, int itemId, int damage, int iconIndex, double x, double y, double z, boolean useColor, float scale) {
        if (itemId < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[itemId].getRenderType())) {
            renderEngine.bindTexture(renderEngine.getTexture("/terrain.png"));
            Block block = Block.blocksList[itemId];
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x - 2, (float)y + 3, -3F + (float)z);
            GL11.glScalef(10, 10, 10);
            GL11.glTranslatef(1, 0.5f, 1);
            GL11.glScalef(1, 1, -1);
            GL11.glRotatef(210, 1, 0, 0);
            GL11.glRotatef(45, 0, 1, 0);
            if (useColor) {
                int color = Item.itemsList[itemId].getColorFromDamage(damage, 0);
                float r = (color >> 16 & 0xFF) / 255f;
                float g = (color >> 8 & 0xFF) / 255f;
                float b = (color & 0xFF) / 255f;
                GL11.glColor4f(r, g, b, 1);
            }
            GL11.glRotatef(-90, 0, 1, 0);
            renderBlocks.useInventoryTint = useColor;
            renderBlocks.renderBlockAsItem(block, damage, 1);
            renderBlocks.useInventoryTint = true;
            GL11.glPopMatrix();
        } else if (Item.itemsList[itemId].func_46058_c()) {
            GL11.glDisable(GL11.GL_LIGHTING);
            renderEngine.bindTexture(renderEngine.getTexture("/gui/items.png"));
            for (int k1 = 0; k1 <= 1; k1++)
            {
                int actualIconIndex = Item.itemsList[itemId].func_46057_a(damage, k1);
                if (useColor) {
                    int color = Item.itemsList[itemId].getColorFromDamage(damage, k1);
                    float r = (float)(color >> 16 & 0xff) / 255F;
                    float g = (float)(color >> 8 & 0xff) / 255F;
                    float b = (float)(color & 0xff) / 255F;
                    GL11.glColor4f(r, g, b, 1.0F);
                }
                renderTexturedQuad(Tessellator.instance, x, y, z, (actualIconIndex % 16) * 16, (actualIconIndex / 16) * 16, (int) (16 * scale), (int) (16 * scale), 16, 16);
            }

            GL11.glEnable(GL11.GL_LIGHTING);
        } else if (iconIndex >= 0) {
            GL11.glDisable(GL11.GL_LIGHTING);
            if (itemId < 256) {
                renderEngine.bindTexture(renderEngine.getTexture("/terrain.png"));
            }
            else {
                renderEngine.bindTexture(renderEngine.getTexture("/gui/items.png"));
            }
            if (useColor) {
                int color = Item.itemsList[itemId].getColorFromDamage(damage, 0);
                float r = (float)(color >> 16 & 0xff) / 255F;
                float g = (float)(color >> 8 & 0xff) / 255F;
                float b = (float)(color & 0xff) / 255F;
                GL11.glColor4f(r, g, b, 1.0F);
            }
            renderTexturedQuad(Tessellator.instance, x, y, z, (iconIndex % 16) * 16, (iconIndex / 16) * 16, (int) (16 * scale), (int) (16 * scale), 16, 16);
            GL11.glEnable(GL11.GL_LIGHTING);
        }
        GL11.glEnable(GL11.GL_CULL_FACE);
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

    public static void renderTexture(String texture, float opacity, Minecraft minecraft) {
        ScaledResolution sr = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();
        GL11.glEnable(GL11.GL_BLEND);
        renderTexture(new Rectangle(width, height), texture, opacity, minecraft);
    }

    public static void renderTexture(Rectangle rectangle, String texture, float opacity, Minecraft minecraft) {
        renderTexture(rectangle.x, rectangle.y, rectangle.width, rectangle.height, texture, opacity, minecraft);
    }

    public static void renderTexture(int x, int y, int width, int height, String texture, float opacity, Minecraft minecraft) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1, 1, 1, opacity);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, minecraft.renderEngine.getTexture(texture));
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

}

