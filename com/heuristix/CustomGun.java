package com.heuristix;

import com.heuristix.asm.ByteVector;
import com.heuristix.asm.Opcodes;
import com.heuristix.util.OverrideClassAdapter;
import com.heuristix.util.BytecodeValue;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/18/11
 * Time: 4:46 PM
 */
public class CustomGun<G extends ItemGun, B extends ItemProjectile> {

    public static final int MAGIC = 0xABCDEFAB;

    private Class<? extends EntityBullet> bulletClass;
    private G itemGun;
    private B itemBullet;

    byte[] itemGunShootSoundBytes;
    String name, itemGunShootSound;
    int bulletDamage, itemBulletId, itemGunShotsPerMinute, itemGunFireMode, itemGunScope, itemGunClipSize, itemGunReloadTime, itemGunId, itemGunRecoilX, itemGunRecoilY, itemGunRoundsPerMinute, itemGunRoundsPerShot;
    float bulletRange, itemGunZoom, bulletSpread;

    final HashMap<String, byte[]> bulletOverrideMethods, itemGunOverrideMethods, itemBulletOverrideMethods;
    private BufferedImage bulletTexture, gunTexture;

    private Class itemGunBaseClass = ItemGunBase.class, entityBulletBaseClass = EntityBulletBase.class, itemProjectileBaseClass = ItemProjectileBase.class;

    public CustomGun(byte[] bytes) {
        this(new Buffer(bytes));
    }

    public CustomGun(Buffer buffer) {
        bulletOverrideMethods = new HashMap<String, byte[]>();
        itemGunOverrideMethods = new HashMap<String, byte[]>();
        itemBulletOverrideMethods = new HashMap<String, byte[]>();
        if(!read(buffer))
            throw new IllegalArgumentException("File is not a valid GUN file.");
    }

    private boolean read(Buffer buffer) {
        try {
            if(buffer.readInt() == MAGIC) {
                name = buffer.readString();

                bulletDamage = buffer.readInt();
                bulletRange = Float.intBitsToFloat(buffer.readInt());
                int bulletMethods = buffer.readShort();
                while(bulletMethods-- > 0) {
                    int length = buffer.readInt();
                    bulletOverrideMethods.put(buffer.readString(), buffer.readBytes(length));
                }

                itemBulletId = buffer.readInt();
                byte[] bytes = new byte[buffer.readInt()];
                buffer.readBytes(bytes, bytes.length, 0);
                bulletTexture = ImageIO.read(new ByteArrayInputStream(bytes));
                int itemBulletMethods = buffer.readShort();
                while(itemBulletMethods-- > 0) {
                    int length = buffer.readInt();
                    itemBulletOverrideMethods.put(buffer.readString(), buffer.readBytes(length));
                }

                itemGunId = buffer.readInt();
                bytes = new byte[buffer.readInt()];
                buffer.readBytes(bytes, bytes.length, 0);
                gunTexture = ImageIO.read(new ByteArrayInputStream(bytes));
                bytes = new byte[buffer.readInt()];
                buffer.readBytes(bytes, bytes.length, 0);
                itemGunShootSoundBytes = bytes;
                itemGunShootSound = buffer.readString();
                itemGunShotsPerMinute = buffer.readInt();
                itemGunFireMode = buffer.readInt();
                itemGunScope = buffer.readInt();
                itemGunZoom = Float.intBitsToFloat(buffer.readInt());
                itemGunClipSize = buffer.readInt();
                itemGunReloadTime = buffer.readInt();
                itemGunRecoilX = buffer.readInt();
                itemGunRecoilY = buffer.readInt();
                int itemGunMethods = buffer.readShort();
                while(itemGunMethods-- > 0) {
                    int length = buffer.readInt();
                    itemGunOverrideMethods.put(buffer.readString(), buffer.readBytes(length));
                }

                try {
                    itemGunRoundsPerMinute = buffer.readInt();
                    bulletSpread = Float.intBitsToFloat(buffer.readInt());
                    itemGunRoundsPerShot = buffer.readInt();
                } catch (ArrayIndexOutOfBoundsException ignored) {

                }
                if(itemGunRoundsPerShot == 0)
                    itemGunRoundsPerShot = 1;
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void write(ByteVector out) throws IOException {
        out.putInt(MAGIC);
        byte[] nameBytes = Utilities.getStringBytes(name);
        out.putByteArray(nameBytes, 0, nameBytes.length);
        out.putInt(bulletDamage);
        out.putInt(Float.floatToIntBits(bulletRange));
        out.putShort(bulletOverrideMethods.size());
        for(Map.Entry<String, byte[]> entry : bulletOverrideMethods.entrySet()) {
            byte[] bytes = entry.getValue();
            out.putInt(bytes.length);
            byte[] stringBytes = Utilities.getStringBytes(entry.getKey());
            out.putByteArray(stringBytes, 0, stringBytes.length);
            out.putByteArray(bytes, 0, bytes.length);
        }

        out.putInt(itemBulletId);
        ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
        ImageIO.write(bulletTexture, "png", imageOut);
        byte[] imageBytes = imageOut.toByteArray();
        out.putInt(imageBytes.length);
        out.putByteArray(imageBytes, 0, imageBytes.length);
        for(Map.Entry<String, byte[]> entry : itemBulletOverrideMethods.entrySet()) {
            byte[] bytes = entry.getValue();
            out.putInt(bytes.length);
            byte[] stringBytes = Utilities.getStringBytes(entry.getKey());
            out.putByteArray(stringBytes, 0, stringBytes.length);
            out.putByteArray(bytes, 0, bytes.length);
        }

        out.putInt(itemGunId);
        imageOut.reset();
        ImageIO.write(gunTexture, "png", imageOut);
        imageBytes = imageOut.toByteArray();
        imageOut.close();
        out.putInt(imageBytes.length);
        out.putByteArray(imageBytes, 0, imageBytes.length);
        out.putInt(itemGunShotsPerMinute);
        out.putInt(itemGunFireMode);
        out.putInt(itemGunClipSize);
        out.putInt(itemGunReloadTime);
        out.putInt(itemGunRecoilX);
        out.putInt(itemGunRecoilY);
        for(Map.Entry<String, byte[]> entry : itemGunOverrideMethods.entrySet()) {
            byte[] bytes = entry.getValue();
            out.putInt(bytes.length);
            byte[] stringBytes = Utilities.getStringBytes(entry.getKey());
            out.putByteArray(stringBytes, 0, stringBytes.length);
            out.putByteArray(bytes, 0, bytes.length);
        }
        out.putInt(itemGunRoundsPerMinute);
        out.putInt(Float.floatToIntBits(bulletSpread));
        out.putInt(itemGunRoundsPerShot);
    }



    public Class<? extends EntityBullet> getBulletClass() {
        if(bulletClass == null) {
            HashMap<String, Object> methods = new HashMap<String, Object>();
            methods.put("getDamage", new BytecodeValue(bulletDamage));
            methods.put("getEffectiveRange", new BytecodeValue(bulletRange));
            methods.put("getSpread", new BytecodeValue(bulletSpread));
            methods.putAll(transform(bulletOverrideMethods));

            try {
                bulletClass = OverrideClassAdapter.extendClass(entityBulletBaseClass, "Entity" + name.replaceAll(" ", "") + "Bullet", new LinkedList<int[]>(), methods);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return bulletClass;
    }

    public B getItemBullet() {
        if(itemBullet == null) {
            HashMap<String, Object> methods = new HashMap<String, Object>();
            methods.put("getName", new BytecodeValue(name + " Bullet"));
            methods.put("getCraftAmount", new BytecodeValue(16));
            methods.putAll(transform(itemBulletOverrideMethods));
            LinkedList<int[]> constructorCode = new LinkedList<int[]>();
            //constructorCode.add(getItemConstructorCode());

            try {
                Constructor init = OverrideClassAdapter.extendClass(itemProjectileBaseClass, "Item" + name.replaceAll(" ", "") + "Bullet", constructorCode, methods).getDeclaredConstructor(int.class, Class.class);
                init.setAccessible(true);
                itemBullet = (B) init.newInstance(itemBulletId - 256, getBulletClass());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return itemBullet;
    }

    public G getItemGun() {
        if(itemGun == null) {
            HashMap<String, Object> methods = new HashMap<String, Object>();
            methods.put("getName", new BytecodeValue(name));
            methods.put("getShootSound", new BytecodeValue("guns." + itemGunShootSound.substring(0, itemGunShootSound.indexOf("."))));
            methods.put("getShotsPerMinute", new BytecodeValue(itemGunShotsPerMinute));
            methods.put("getFireMode", new BytecodeValue(itemGunFireMode));
            methods.put("getReloadTime", new BytecodeValue(itemGunReloadTime));
            methods.put("getClipSize", new BytecodeValue(itemGunClipSize));
            methods.put("getRecoilX", new BytecodeValue(itemGunRecoilX));
            methods.put("getRecoilY", new BytecodeValue(itemGunRecoilY));
            methods.put("getZoom", new BytecodeValue(itemGunZoom));
            methods.put("getScope", new BytecodeValue(itemGunScope));
            if(itemGunRoundsPerMinute != 0)
                methods.put("getRoundsPerMinute", new BytecodeValue(itemGunRoundsPerMinute));
            methods.put("getRoundsPerShot", new BytecodeValue(itemGunRoundsPerShot));
            methods.putAll(transform(itemGunOverrideMethods));
            LinkedList<int[]> constructorCode = new LinkedList<int[]>();
            //constructorCode.add(getItemConstructorCode());

            try {
                Constructor init = OverrideClassAdapter.extendClass(itemGunBaseClass, "Item" + name.replaceAll(" ", ""), constructorCode, methods).getDeclaredConstructor(int.class, ItemProjectile.class);
                init.setAccessible(true);
                itemGun = (G) init.newInstance(itemGunId - 256, getItemBullet());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return itemGun;
    }

    private static HashMap<String, int[]> transform(HashMap<String, byte[]> map) {
        HashMap<String, int[]> newMap = new HashMap<String, int[]>();
        for(Map.Entry<String, byte[]> entry : map.entrySet()) {
            newMap.put(entry.getKey(), Utilities.toIntArray(entry.getValue()));
        }
        return newMap;
    }

    private int[] getItemConstructorCode() {
        return new int[] {
            0x2a, //ALOAD_0
            0x1b, //ILOAD_1
            0x2c, //ALOAD_2
            Opcodes.INVOKESPECIAL,
                0,
                0,
            Opcodes.RETURN
        };
    }

    public BufferedImage getBulletTexture() {
        return bulletTexture;
    }

    public BufferedImage getGunTexture() {
        return gunTexture;
    }

    public byte[] getShootSoundBytes() {
        return itemGunShootSoundBytes;
    }

}
