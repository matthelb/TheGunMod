package com.heuristix;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/2/11
 * Time: 7:59 PM
 */
public class Utilities {

    private static final Random RANDOM = new Random();

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
        if(difference <= 0) {
            difference++;
        }
        return nextInt(++difference) + realMin;
    }

    public static float randomFloat(float min, float max) {
        return Math.min(min, max) + nextFloat() * Math.abs(max - min);
    }

    public static boolean remove(InventoryPlayer inventory, int id, int amount) {
        while(amount > 0) {
            int slot = getItemSlot(inventory, id);
            if(slot == -1)
                return false;
            ItemStack stack = inventory.getStackInSlot(slot);
            if(stack != null) {
                int reduce = Math.min(stack.stackSize, amount);
                if(reduce > amount)
                    reduce = amount;
                inventory.decrStackSize(slot, amount);
                if((amount -= reduce) == 0)
                    break;
            }
        }
        return true;
    }

    public static int getItemSlot(InventoryPlayer inventory, int id) {
        for(int i = 0; i < inventory.mainInventory.length; i++) {
            if(inventory.mainInventory[i] != null && inventory.mainInventory[i].itemID == id)
                return i;
        }
        return -1;
    }

    public static int getCount(InventoryPlayer inventory, int id) {
        int count = 0;
        ItemStack[][] stacks = new ItemStack[][]{inventory.mainInventory, inventory.armorInventory};
        for(int i = 0; i < stacks.length; i++) {
            for(int j = 0; j < stacks[i].length; j++) {
                ItemStack is = stacks[i][j];
                if(is != null && is.itemID == id)
                    count += (is.stackSize == 0) ? 1 : is.stackSize;
            }
        }
        return count;
    }

    public static void renderTexture(Minecraft minecraft, String texture, float opacity) {
        ScaledResolution localqm = new ScaledResolution(minecraft.gameSettings, minecraft.displayWidth, minecraft.displayHeight);
        int width = localqm.getScaledWidth();
        int height = localqm.getScaledHeight();
        GL11.glEnable(3042);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, opacity);
        GL11.glDisable(3008);
        GL11.glBindTexture(3553, minecraft.renderEngine.getTexture(texture));
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(0.0D, height, -90.0D, 0.0D, 1.0D);
        t.addVertexWithUV(width, height, -90.0D, 1.0D, 1.0D);
        t.addVertexWithUV(width, 0.0D, -90.0D, 1.0D, 0.0D);
        t.addVertexWithUV(0.0D, 0.0D, -90.0D, 0.0D, 0.0D);
        t.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, opacity);
    }

    public static void renderRect(Color color, int x, int y, int width, int height, float opacity) {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f((float) color.getRed() / 255, (float) color.getGreen() / 255, (float) color.getBlue() / 255, opacity);
        GL11.glDisable(3008);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setNormal(0.0F, 1.0F, 0.0F);
        t.addVertexWithUV(x, y, 0, 0, 0);
        t.addVertexWithUV(x + width, y, 0, 1, 0);
        t.addVertexWithUV(x + width, y + height, 0, 1, 1);
        t.addVertexWithUV(x, y + height, 0, 0, 1);
        t.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        GL11.glEnable(3008);
        GL11.glPopMatrix();
    }

    public static float toRadians(double deg) {
        return (float) (deg / 180.0f * 3.141593f);
    }

    private static File HOME_DIRECTORY;
    public static File getHomeDirectory() {
        if(HOME_DIRECTORY == null) {
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
            if(in != null) {
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
            if(fis != null) {
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
        if(os.contains("win")) {
            String appdata = System.getenv("APPDATA");
            if(appdata != null) {
                file = new File(appdata, "." + dir + '/');
            } else {
                file = new File(home, '.' + dir + '/');
            }
        } else if(os.contains("linux") || os.contains("solaris")) {
            file = new File(home, '.' + dir + File.separator);
        } else if(os.contains("mac")) {
            file = new File(home, "Library/Application Support/" + dir);
        } else if(file != null && !file.exists() && !file.mkdirs())
            return null;
         return file;
    }

    public static File getMinecraftDir(String dir) {
        File minecraftDir = getAppDir("minecraft");
        if(minecraftDir != null)
            return new File(minecraftDir.getAbsolutePath() + File.separator + dir);
        return null;
    }

    public static File getHeuristixDir(String dir) {
        File heuristixDir = getMinecraftDir("heuristix");
        if(heuristixDir != null) {
            heuristixDir = new File(heuristixDir.getAbsoluteFile() + File.separator + dir);
            if(!heuristixDir.exists())
                heuristixDir.mkdirs();
            return heuristixDir;
        }
        return null;
    }

    private static JarFile minecraftJar;

    public static JarFile getMinecraftJar() {
        if(minecraftJar == null) {
            File file = new File(getMinecraftDir("bin") + File.separator + "minecraft.jar");
            if(file.exists()) {
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
        for(File file : dir.listFiles()) {
            if(file.getName().equals(name))
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
            if(out != null) {
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

    public static String normalize(String string) {
        String normalized = string.toLowerCase().replaceAll("_", " ");
        return normalized.substring(0, 1).toUpperCase() + normalized.substring(1, normalized.length());
    }

    private static final Map<Character, Character> numbersToStrings = new HashMap<Character, Character>();
    static {
        numbersToStrings.put('1', 'A');
        numbersToStrings.put('2', 'B');
        numbersToStrings.put('3', 'C');
        numbersToStrings.put('4', 'D');
        numbersToStrings.put('5', 'E');
        numbersToStrings.put('6', 'F');
        numbersToStrings.put('7', 'G');
        numbersToStrings.put('8', 'H');
        numbersToStrings.put('9', 'I');
        numbersToStrings.put('0', 'J');
    }
    public static String replaceNumbers(String text) {
        char[] chars = text.toCharArray();
        for(int i = 0; i < chars.length; i++) {
            if(Character.isDigit(chars[i])) {
                chars[i] = numbersToStrings.get(chars[i]);
            }
        }
        return new String(chars);
    }

    public static byte[] parseByteArray(String text) {
        if(text != null && !text.isEmpty()) {
            String[] texts = text.split(",");
            byte[] bytes = new byte[texts.length];
            for(int i = 0; i < bytes.length; i++) {
                bytes[i] = Byte.parseByte(texts[i]);
            }
            return bytes;
        }
        return null;
    }

    public static int[] toIntArray(byte[] bytes) {
        int[] array = new int[bytes.length];
        for(int i = 0; i < array.length; i++)
            array[i] = bytes[i];
        return array;
    }
}
