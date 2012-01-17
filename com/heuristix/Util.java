package com.heuristix;

import com.heuristix.net.PacketDamageItem;
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

    public static float toRadians(double deg) {
        return (float) (deg / 180.0f * PI);
    }

    public static float toDegrees(double radians) {
        return (float) (radians * 180 / PI);
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
        byte[] buffer = new byte[4096 /*GL_TEXTURE_WIDTH*/];
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

    public static Field getField(Class clazz, String name, String obfuscatedName) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            try {
                return clazz.getDeclaredField(obfuscatedName);
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    public static Method getMethod(Class clazz, String name, String obfuscatedName, Class<?>... parameters) {
        try {
            return clazz.getDeclaredMethod(name, parameters);
        } catch (NoSuchMethodException e) {
            try {
                return clazz.getDeclaredMethod(obfuscatedName, parameters);
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
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
            return (SoundSystem) getPrivateValue(SoundManager.class, sndManager, "sndSystem", OBFUSCATED_NAMES.get(SoundManager.class).get("sndSystem"));
        }
        return null;
    }

    private static final Map<Class, Map<String, String>> OBFUSCATED_NAMES = new HashMap<Class, Map<String, String>>();

    static {
        HashMap<String, String> names = new HashMap<String, String>();
        names.put("options", "f");
        names.put("sndSystem", "a");
        names.put("soundPoolMusic", "d");
        names.put("soundPoolSounds", "b");
        names.put("soundPoolStreaming", "c");
        OBFUSCATED_NAMES.put(SoundManager.class, (Map<String, String>) names.clone());
        names.clear();
        names.put("mc", "b");
        OBFUSCATED_NAMES.put(EntityPlayerSP.class, (Map<String, String>) names.clone());
        names.clear();
        names.put("addIdClassMapping", "a");
        OBFUSCATED_NAMES.put(Packet.class, (Map<String, String>) names.clone());
    }

    public static SoundPool[] getSoundPools(SoundManager sndManager) {
        SoundPool[] soundPools = new SoundPool[3];
        Map<String, String> fields = OBFUSCATED_NAMES.get(SoundManager.class);
        soundPools[0] = (SoundPool) getPrivateValue(SoundManager.class, sndManager, "soundPoolSounds", fields.get("soundPoolSounds"));
        soundPools[1] = (SoundPool) getPrivateValue(SoundManager.class, sndManager, "soundPoolStreaming", fields.get("soundPoolStreaming"));
        soundPools[2] = (SoundPool) getPrivateValue(SoundManager.class, sndManager, "soundPoolMusic", fields.get("soundPoolMusic"));
        return soundPools;
    }

    public static GameSettings getGameSettings(SoundManager sndManager) {
        return (GameSettings) getPrivateValue(SoundManager.class, sndManager, "options", OBFUSCATED_NAMES.get(SoundManager.class).get("options"));
    }

    public static void setPrivateValue(Class clazz, Object classInstance, String fieldName, String obfuscatedName, Object fieldValue) {
        try {
            ModLoader.setPrivateValue(clazz, classInstance, fieldName, fieldValue);
        } catch (NoSuchFieldException e) {
            try {
                if (obfuscatedName != null)
                    ModLoader.setPrivateValue(clazz, classInstance, obfuscatedName, fieldValue);
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }
    }

    public static Object getPrivateValue(Class clazz, Object classInstance, String fieldName, String obfuscatedName) {
        try {
            return ModLoader.getPrivateValue(clazz, classInstance, fieldName);
        } catch (NoSuchFieldException e) {
            try {
                if (obfuscatedName != null) {
                    return ModLoader.getPrivateValue(clazz, classInstance, obfuscatedName);
                }
            } catch (NoSuchFieldException e1) {
                e1.printStackTrace();
            }
        }
        return null;
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
        Minecraft mc = (Minecraft) getPrivateValue(EntityPlayerSP.class, player, "mc", OBFUSCATED_NAMES.get(EntityPlayerSP.class).get("mc"));
        if(mc != null) {
            return mc;
        }
        return null;
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

    private static Method addIdClassMapping;

    public static void setPacketId(Class packetClass, int id, boolean client, boolean server) throws InvocationTargetException, IllegalAccessException {
        if(addIdClassMapping == null) {
            addIdClassMapping = getMethod(Packet.class, "addIdClassMapping", OBFUSCATED_NAMES.get(Packet.class).get("addIdClassMapping"), int.class, boolean.class, boolean.class, Class.class);
            if(addIdClassMapping == null) {
                return;
            }
            addIdClassMapping.setAccessible(true);
        }
        addIdClassMapping.invoke(null, id, client, server, packetClass);
    }

    public static String getStringFromBytes(int[] bytes) {
        int i = 0;
        while (bytes[i++] != 10) ;
        return new String(bytes, 0, i - 1);
    }

    public static void damageItem(ItemStack item, EntityPlayer player, int damage, Class modClass, World world) {
        if(world.multiplayerWorld) {
            Util.sendPacket(new PacketDamageItem(item.itemID, player.inventory.currentItem, damage), modClass);
        } else {
            item.damageItem(damage, player);
        }
    }

}
