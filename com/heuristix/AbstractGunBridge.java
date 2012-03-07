package com.heuristix;

import com.heuristix.asm.ByteVector;
import com.heuristix.asm.Opcodes;
import com.heuristix.util.*;
import net.minecraft.src.World;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 2/1/12
 * Time: 6:07 PM
 */
public abstract class AbstractGunBridge implements GunBridge {

    public static final String VERSION = "0.9.8";

    public static final List<Pair<String, String>> OBFUSCATED_CLASS_NAMES = new LinkedList<Pair<String, String>>();

    public static final int[] SUPER_WORLD = {Opcodes.ALOAD_0, Opcodes.ALOAD_1};
    public static final int[] SUPER_WORLD_ENTITY = {Opcodes.ALOAD_0, Opcodes.ALOAD_1, Opcodes.ALOAD_2};
    public static final int[] SUPER_WORLD_COORDS = {Opcodes.ALOAD_0, Opcodes.ALOAD_1, Opcodes.DLOAD_2, Opcodes.DLOAD, 0x4, Opcodes.DLOAD, 0x6};

    private static final String NON_ALPHA_NUMERICAL_REGEX = "[^a-z^A-Z^0-9]";

    static {
        //add additional Pairs as obfuscations increase
        OBFUSCATED_CLASS_NAMES.add(new Pair("wz", "acl"));
        OBFUSCATED_CLASS_NAMES.add(new Pair("vq", "aar"));
        OBFUSCATED_CLASS_NAMES.add(new Pair("ry", "nq"));
        OBFUSCATED_CLASS_NAMES.add(new Pair("rv", "wd"));
    }

    public void read(Gun gun, boolean deobfuscate) {
        try {
            if (gun != null) {
                List<Pair<String, byte[]>> gunClasses = gun.getClasses();

                String projectileType = ClassDescriptor.getClassDescription(gunClasses.get(0).getSecond()).getSuperName();
                HashMap<String, Method> methods = new HashMap<String, Method>();
                if (deobfuscate) {
                    for (int i = 0; i < OBFUSCATED_CLASS_NAMES.size(); i++) {
                        Pair<String, String> obfuscatedNames = OBFUSCATED_CLASS_NAMES.get(i);
                        methods.put("<init>(L" + obfuscatedNames.getFirst() + ";L" + obfuscatedNames.getSecond() + ";)V",
                                new Method("(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityLiving;)V",
                                        new InvokeMethod(SUPER_WORLD_ENTITY, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityLiving;)V", false, true, false)));
                        methods.put("<init>(L" + obfuscatedNames.getFirst() + ";)V",
                                new Method("(Lnet/minecraft/src/World;)V",
                                        new InvokeMethod(SUPER_WORLD, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(Lnet/minecraft/src/World;)V", false, true, false)));
                        methods.put("<init>(L" + obfuscatedNames.getFirst() + ";DDD)V",
                                new Method("(Lnet/minecraft/src/World;DDD)V",
                                        new InvokeMethod(SUPER_WORLD_COORDS, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(Lnet/minecraft/src/World;DDD)V", false, true, false)));
                    }
                }
                byte[] entityProjectileClassBytes = ExtensibleClassAdapter.modifyClassBytes(gunClasses.get(0).getSecond(), gunClasses.get(0).getFirst(), methods, false);
                Class entityProjectileClass = null;
                for (int i = 0; entityProjectileClass == null; i++) {
                    entityProjectileClass = Util.defineClass(ExtensibleClassAdapter.modifyClassBytes(entityProjectileClassBytes, gunClasses.get(0).getFirst() + i, new HashMap<String, Method>(), false), gunClasses.get(0).getFirst() + i, ProjectileType.class.getClassLoader());
                }
                Class itemProjectileClass = null;
                for (int i = 0; itemProjectileClass == null; i++) {
                    itemProjectileClass = Util.defineClass(ExtensibleClassAdapter.modifyClassBytes(gunClasses.get(1).getSecond(), gunClasses.get(1).getFirst() + i, new HashMap<String, Method>(), false), gunClasses.get(1).getFirst() + i, ItemProjectileBase.class.getClassLoader());
                }
                Constructor itemProjectileConstructor = itemProjectileClass.getDeclaredConstructor(int.class);
                itemProjectileConstructor.setAccessible(true);
                ItemProjectile itemBullet = (ItemProjectile) itemProjectileConstructor.newInstance(gun.getItemProjectileId());
                Constructor entityBulletConstructor = entityProjectileClass.getDeclaredConstructor(World.class);
                entityBulletConstructor.setAccessible(true);
                EntityProjectile entityProjectile = (EntityProjectile) entityBulletConstructor.newInstance(new Object[]{null});
                Class itemGunClass = null;
                for (int i = 0; itemGunClass == null; i++) {
                    itemGunClass = Util.defineClass(ExtensibleClassAdapter.modifyClassBytes(gunClasses.get(2).getSecond(), gunClasses.get(2).getFirst() + i, new HashMap<String, Method>(), false), gunClasses.get(2).getFirst() + i, ItemGunBase.class.getClassLoader());
                }
                Constructor itemGunConstructor = itemGunClass.getDeclaredConstructor(int.class, ItemProjectile.class);
                itemGunConstructor.setAccessible(true);
                ItemGun itemGun = (ItemGun) itemGunConstructor.newInstance(gun.getItemGunId(), itemBullet);

                setGunName(itemGun.getName());
                setProjectileName(itemBullet.getName());
                setProjectileType(ProjectileType.forClass(entityProjectileClass.getSuperclass()));
                setFireMode(FireMode.values()[itemGun.getFireMode()]);
                setScope(Scope.values()[itemGun.getScope()]);
                setDamage(entityProjectile.getDamage());
                setShotsPerMinute(itemGun.getShotsPerMinute());
                setReloadTime(itemGun.getReloadTime());
                setClipSize(itemGun.getClipSize());
                setRecoilX(itemGun.getRecoilX());
                setRecoilY(itemGun.getRecoilY());
                setProjectileId(gun.getItemProjectileId());
                setGunId(gun.getItemGunId());
                setRoundsPerMinute(itemGun.getRoundsPerMinute());
                setRoundsPerShot(itemGun.getRoundsPerShot());
                setRange(entityProjectile.getEffectiveRange());
                setZoom(itemGun.getZoom());
                setProjectileSpread(entityProjectile.getSpread());
                setReloadSound(itemGun.getReloadSound());
                setShootSound(itemGun.getShootSound());

                for(Map.Entry<String, byte[]> entry : gun.getResources().entrySet()) {
                    addResource(entry.getKey(), entry.getValue());
                }
                for(Map.Entry<String, int[]> entry : gun.getProperties().entrySet()) {
                    addProperty(entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            Log.getLogger().throwing(getClass().getName(), "load(Gun gun, boolean deobfuscate)", e);
        }
    }

    public void write(OutputStream out) throws IOException {
        ByteVector outBytes = new ByteVector();
        outBytes.putInt(Gun.MAGIC);

        outBytes.putInt(3);
        HashMap<String, Method> methods = new HashMap<String, Method>();
        methods.put("getDamage()I", new Method(new BytecodeValue(getDamage())));
        methods.put("getEffectiveRange()F", new Method(new BytecodeValue(getRange())));
        methods.put("getSpread()F", new Method(new BytecodeValue(getSpread())));

        Class clazz = getProjectileType().getProjectileType();
        methods.put("<init>(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityLiving;)V",
                new Method("(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";L" + OBFUSCATED_CLASS_NAMES.get(0).getSecond() + ";)V",
                        new InvokeMethod(SUPER_WORLD_ENTITY, new int[]{Opcodes.RETURN}, clazz.getCanonicalName().replace('.', '/'), "<init>",
                                "(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";L" + OBFUSCATED_CLASS_NAMES.get(0).getSecond() + ";)V", false, true, false)));
        methods.put("<init>(Lnet/minecraft/src/World;)V",
                new Method("(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";)V",
                    new InvokeMethod(SUPER_WORLD, new int[]{Opcodes.RETURN}, clazz.getCanonicalName().replace('.', '/'), "<init>", "(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";)V", false, true, false)));

        methods.put("<init>(Lnet/minecraft/src/World;DDD)V",
                new Method("(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";DDD)V",
                new InvokeMethod(SUPER_WORLD_COORDS, new int[]{Opcodes.RETURN}, clazz.getCanonicalName().replace('.', '/'), "<init>", "(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";DDD)V", false, true, false)));
        String name = "Entity" + getProjectileName().replaceAll(NON_ALPHA_NUMERICAL_REGEX, "") + getGunName().replaceAll(NON_ALPHA_NUMERICAL_REGEX, "");
        byte[] bytes = ExtensibleClassAdapter.modifyClassBytes(clazz, name, (HashMap<String, Method>) methods.clone(), true);
        byte[] stringBytes = Util.getStringBytes(name);
        outBytes.putByteArray(stringBytes, 0, stringBytes.length);
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        methods.clear();
        methods.put("getName()Ljava/lang/String;", new Method(new BytecodeValue(getProjectileName())));
        methods.put("getCraftAmount()I", new Method(new BytecodeValue(16)));
        name = "Item" + getProjectileName().replaceAll(NON_ALPHA_NUMERICAL_REGEX, "");
        bytes = ExtensibleClassAdapter.modifyClassBytes(ItemProjectileBase.class, name, (HashMap<String, Method>) methods.clone(), true);
        stringBytes = Util.getStringBytes(name);
        outBytes.putByteArray(stringBytes, 0, stringBytes.length);
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        methods.clear();
        methods.put("getName()Ljava/lang/String;", new Method(new BytecodeValue(getGunName())));
        methods.put("getShootSound()Ljava/lang/String;", new Method(new BytecodeValue("guns." + getShootSound().substring(0,getShootSound().indexOf(".")))));
        methods.put("getReloadSound()Ljava/lang/String;", new Method(new BytecodeValue("guns." + getReloadSound().substring(0, getReloadSound().indexOf(".")))));
        methods.put("getShotsPerMinute()I", new Method(new BytecodeValue(getShotsPerMinute())));
        methods.put("getFireMode()I", new Method(new BytecodeValue((getFireMode()).ordinal())));
        methods.put("getReloadTime()I", new Method(new BytecodeValue(getReloadTime())));
        methods.put("getClipSize()I", new Method(new BytecodeValue(getClipSize())));
        methods.put("getRecoilX()I", new Method(new BytecodeValue(getRecoilX())));
        methods.put("getRecoilY()I", new Method(new BytecodeValue(getRecoilY())));
        methods.put("getZoom()F", new Method(new BytecodeValue(getZoom())));
        methods.put("getScope()I", new Method(new BytecodeValue(getScope().ordinal())));
        methods.put("getRoundsPerMinute()I", new Method(new BytecodeValue(getRoundsPerMinute())));
        methods.put("getRoundsPerShot()I", new Method(new BytecodeValue(getRoundsPerShot())));
        name = "Item" + getGunName().replaceAll(NON_ALPHA_NUMERICAL_REGEX, "");
        bytes = ExtensibleClassAdapter.modifyClassBytes(ItemGunBase.class, name, (HashMap<String, Method>) methods.clone(), true);
        stringBytes = Util.getStringBytes(name);
        outBytes.putByteArray(stringBytes, 0, stringBytes.length);
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        Map<String, byte[]> resources = getResources();
        outBytes.putInt(resources.size());
        for (Map.Entry<String, byte[]> entry : resources.entrySet()) {
            stringBytes = Util.getStringBytes(entry.getKey());
            outBytes.putByteArray(stringBytes, 0, stringBytes.length);
            bytes = entry.getValue();
            outBytes.putInt(bytes.length);
            outBytes.putByteArray(bytes, 0, bytes.length);
        }

        Map<String, int[]> properties = getProperties();
        outBytes.putInt(properties.size());
        for (Map.Entry<String, int[]> entry : properties.entrySet()) {
            stringBytes = Util.getStringBytes(entry.getKey());
            outBytes.putByteArray(stringBytes, 0, stringBytes.length);
            bytes = Util.getByteArray(entry.getValue());
            outBytes.putInt(bytes.length);
            outBytes.putByteArray(bytes, 0, bytes.length);
        }

        out.write(outBytes.toByteArray());
    }

    /*protected List<byte[]> getResources() throws IOException {
        List<byte[]> resources = new LinkedList<byte[]>();
        byte[] bytes;
        ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
        ImageIO.write(getProjectileImage(0), "png", imageOut);
        bytes = imageOut.toByteArray();
        resources.add(bytes);

        imageOut = new ByteArrayOutputStream();
        ImageIO.write(getGunImage(0), "png", imageOut);
        bytes = imageOut.toByteArray();
        resources.add(bytes);

        resources.add(getShootSoundBytes());
        resources.add(getReloadSoundBytes());
        for(int i = 0; i < 2; i++) {
            for(int j = 1; j < 6; j++) {
                RenderedImage image = (i == 0) ? getGunImage(j) : getProjectileImage(j);
                if(image != null) {
                    imageOut = new ByteArrayOutputStream();
                    ImageIO.write(image, "png", imageOut);
                    bytes = imageOut.toByteArray();
                    resources.add(bytes);
                } else {
                    resources.add(new byte[1]);
                }
            }
        }
        return resources;
    } */

    /*protected Pair<String, int[]>[] getProperties() {
        Pair<String, int[]>[] properties = new Pair[PROPERTIES];
        properties[0] = new Pair<String, int[]>("itemGunId", ReverseBuffer.getInt(getGunId()));
        properties[1] = new Pair<String, int[]>("itemBulletId", ReverseBuffer.getInt(getProjectileId()));
        properties[2] = new Pair<String, int[]>("versionCreated", Util.getIntArray(Util.getStringBytes(VERSION)));
        return properties;
    }*/

    protected abstract int getGunId();

    protected abstract int getProjectileId();

    protected abstract String getShootSound();

    protected abstract String getReloadSound();

    protected abstract int getRoundsPerShot();

    protected abstract int getRoundsPerMinute();

    protected abstract Scope getScope();

    protected abstract float getZoom();

    protected abstract int getRecoilY();

    protected abstract int getRecoilX();

    protected abstract int getClipSize();

    protected abstract int getReloadTime();

    protected abstract FireMode getFireMode();

    protected abstract int getShotsPerMinute();

    protected abstract String getGunName();

    protected abstract String getProjectileName();

    protected abstract ProjectileType getProjectileType();

    protected abstract float getSpread();

    protected abstract float getRange();

    protected abstract int getDamage();

    protected abstract Map<String, byte[]> getResources();

    protected abstract Map<String, int[]> getProperties();

    protected abstract void setProjectileSpread(float spread);

    protected abstract void setZoom(float zoom);

    protected abstract void setRange(float effectiveRange);

    protected abstract void setRoundsPerShot(int roundsPerShot);

    protected abstract void setRoundsPerMinute(int roundsPerMinute);

    protected abstract void setGunId(int itemGunId);

    protected abstract void setProjectileId(int itemProjectileId);

    protected abstract void setRecoilY(int recoilY);

    protected abstract void setRecoilX(int recoilX);

    protected abstract void setClipSize(int clipSize);

    protected abstract void setReloadTime(int reloadTime);

    protected abstract void setShotsPerMinute(int shotsPerMinute);

    protected abstract void setDamage(int damage);

    protected abstract void setScope(Scope scope);

    protected abstract void setFireMode(FireMode fireMode);

    protected abstract void setProjectileType(ProjectileType projectileType);

    protected abstract void setProjectileName(String name);

    protected abstract void setGunName(String name);

    protected abstract void setShootSound(String shootSound);

    protected abstract void setReloadSound(String reloadSound);

    protected abstract void addProperty(String property, int[] value);

    protected abstract void addResource(String resource, byte[] value);
}
