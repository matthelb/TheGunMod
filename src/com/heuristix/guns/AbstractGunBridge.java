package com.heuristix.guns;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.world.World;

import com.heuristix.EntityProjectile;
import com.heuristix.ItemGun;
import com.heuristix.ItemGunBase;
import com.heuristix.ItemProjectile;
import com.heuristix.ItemProjectileBase;
import com.heuristix.guns.asm.ByteVector;
import com.heuristix.guns.asm.Opcodes;
import com.heuristix.guns.util.BytecodeValue;
import com.heuristix.guns.util.ClassDescriptor;
import com.heuristix.guns.util.ExtensibleClassVisitor;
import com.heuristix.guns.util.InvokeMethod;
import com.heuristix.guns.util.Log;
import com.heuristix.guns.util.Method;
import com.heuristix.guns.util.Pair;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 2/1/12
 * Time: 6:07 PM
 */
public abstract class AbstractGunBridge implements GunBridge {

    public static final String VERSION = "1.5.3";

    public static final List<Pair<String, String>> OBFUSCATED_CLASS_NAMES = new LinkedList<Pair<String, String>>();

    public static final int[] SUPER_WORLD = {Opcodes.ALOAD_0, Opcodes.ALOAD_1};
    public static final int[] SUPER_WORLD_ENTITY = {Opcodes.ALOAD_0, Opcodes.ALOAD_1, Opcodes.ALOAD_2};
    public static final int[] SUPER_WORLD_COORDS = {Opcodes.ALOAD_0, Opcodes.ALOAD_1, Opcodes.DLOAD_2, Opcodes.DLOAD, 0x4, Opcodes.DLOAD, 0x6};

    private static final String NON_ALPHA_NUMERICAL_REGEX = "[^a-z^A-Z^0-9]";

    static {
        //TODO add new obfuscation names (update version)
    	OBFUSCATED_CLASS_NAMES.add(new Pair<String, String>("yc", "md"));
        OBFUSCATED_CLASS_NAMES.add(new Pair<String, String>("xv", "md"));
        OBFUSCATED_CLASS_NAMES.add(new Pair<String, String>("xe", "ln"));
        OBFUSCATED_CLASS_NAMES.add(new Pair<String, String>("up", "jw"));
        OBFUSCATED_CLASS_NAMES.add(new Pair<String, String>("uo", "jv"));
        OBFUSCATED_CLASS_NAMES.add(new Pair<String, String>("xd","acq"));
        OBFUSCATED_CLASS_NAMES.add(new Pair<String, String>("wz", "acl"));
        OBFUSCATED_CLASS_NAMES.add(new Pair<String, String>("vq", "aar"));
        OBFUSCATED_CLASS_NAMES.add(new Pair<String, String>("ry", "nq"));
        OBFUSCATED_CLASS_NAMES.add(new Pair<String, String>("rv", "wd"));

    }

    public void read(Gun gun, boolean deobfuscate) {
        try {
            if (gun != null) {
                List<Pair<String, byte[]>> gunClasses = gun.getClasses();

                String projectileType = ClassDescriptor.getClassDescription(Opcodes.ASM4, gunClasses.get(0).getSecond()).getSuperName();
                HashMap<String, Method> methods = new HashMap<String, Method>();
                String worldClass = (deobfuscate) ? "net/minecraft/world/World" : OBFUSCATED_CLASS_NAMES.get(0).getFirst(), entityLivingClass = (deobfuscate) ? "net/minecraft/entity/EntityLiving" : OBFUSCATED_CLASS_NAMES.get(0).getSecond();
                for (int i = 0; i < OBFUSCATED_CLASS_NAMES.size(); i++) {
                    Pair<String, String> obfuscatedNames = OBFUSCATED_CLASS_NAMES.get(i);
                    methods.put("<init>(L" + obfuscatedNames.getFirst() + ";L" + obfuscatedNames.getSecond() + ";)V",
                            new Method("(L" + worldClass + ";L" + entityLivingClass +";)V",
                                    new InvokeMethod(SUPER_WORLD_ENTITY, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(L" + worldClass + ";L" + entityLivingClass + ";)V", false, true, false)));
                    methods.put("<init>(L" + obfuscatedNames.getFirst() + ";)V",
                            new Method("(L" + worldClass + ";)V",
                                    new InvokeMethod(SUPER_WORLD, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(L" + worldClass + ";)V", false, true, false)));
                    methods.put("<init>(L" + obfuscatedNames.getFirst() + ";DDD)V",
                            new Method("(L" + worldClass + ";DDD)V",
                                    new InvokeMethod(SUPER_WORLD_COORDS, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(L" + worldClass + ";DDD)V", false, true, false)));
                }
                byte[] entityProjectileClassBytes = ExtensibleClassVisitor.modifyClassBytes(Opcodes.ASM4, gunClasses.get(0).getSecond(), gunClasses.get(0).getFirst(), (Map<String, Method>) methods.clone(), false);
                methods.clear();
                Class<?> entityProjectileClass = null;
                for (int i = 0; entityProjectileClass == null; i++) {
                    entityProjectileClass = Util.defineClass(ExtensibleClassVisitor.modifyClassBytes(Opcodes.ASM4, entityProjectileClassBytes, gunClasses.get(0).getFirst() + i, (Map<String, Method>) methods.clone(), false), gunClasses.get(0).getFirst() + i, ProjectileType.class.getClassLoader());
                }
                methods.clear();
                methods.put("<init>(I)V", new Method(new InvokeMethod(new int[]{Opcodes.ALOAD_0, Opcodes.ILOAD_1}, new int[]{Opcodes.RETURN}, ItemProjectileBase.class.getCanonicalName().replace('.', '/'), "<init>", "(I)V", false, true, false)));
                Class<?> itemProjectileClass = null;
                for (int i = 0; itemProjectileClass == null; i++) {
                    itemProjectileClass = Util.defineClass(ExtensibleClassVisitor.modifyClassBytes(Opcodes.ASM4, gunClasses.get(1).getSecond(), gunClasses.get(1).getFirst() + i, (Map<String, Method>) methods.clone(), false), gunClasses.get(1).getFirst() + i, ItemProjectileBase.class.getClassLoader());
                }
                methods.clear();
                methods.put("<init>(ILcom/heuristix/ItemProjectile;)V", new Method(new InvokeMethod(new int[]{Opcodes.ALOAD_0, Opcodes.ILOAD_1, Opcodes.ALOAD_2}, new int[]{Opcodes.RETURN}, ItemGunBase.class.getCanonicalName().replace('.', '/'), "<init>", "(ILcom/heuristix/ItemProjectile;)V", false, true, false)));
                Constructor<?> itemProjectileConstructor = itemProjectileClass.getDeclaredConstructor(int.class);
                itemProjectileConstructor.setAccessible(true);
                ItemProjectile itemBullet = (ItemProjectile) itemProjectileConstructor.newInstance(gun.getItemProjectileId());
                Constructor<?> entityBulletConstructor = entityProjectileClass.getDeclaredConstructor(World.class);
                entityBulletConstructor.setAccessible(true);
                EntityProjectile entityProjectile = (EntityProjectile) entityBulletConstructor.newInstance(new Object[]{null});
                Class<?> itemGunClass = null;
                for (int i = 0; itemGunClass == null; i++) {
                    itemGunClass = Util.defineClass(ExtensibleClassVisitor.modifyClassBytes(Opcodes.ASM4, gunClasses.get(2).getSecond(), gunClasses.get(2).getFirst() + i, methods, false), gunClasses.get(2).getFirst() + i, ItemGunBase.class.getClassLoader());
                }
                Constructor<?> itemGunConstructor = itemGunClass.getDeclaredConstructor(int.class, ItemProjectile.class);
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
                setReloadParts(itemGun.getReloadParts());
                setReloadSound(itemGun.getReloadSound().substring(itemGun.getReloadSound().lastIndexOf('.') + 1) + ".ogg");
                setShootSound(itemGun.getShootSound().substring(itemGun.getShootSound().lastIndexOf('.') + 1) + ".ogg");

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

        Class<?> clazz = getProjectileType().getProjectileType();
        methods.put("<init>(Lnet/minecraft/world/World;Lnet/minecraft/entity/EntityLiving;)V",
                new Method("(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";L" + OBFUSCATED_CLASS_NAMES.get(0).getSecond() + ";)V",
                        new InvokeMethod(SUPER_WORLD_ENTITY, new int[]{Opcodes.RETURN}, clazz.getCanonicalName().replace('.', '/'), "<init>",
                                "(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";L" + OBFUSCATED_CLASS_NAMES.get(0).getSecond() + ";)V", false, true, false)));
        methods.put("<init>(Lnet/minecraft/world/World;)V",
                new Method("(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";)V",
                    new InvokeMethod(SUPER_WORLD, new int[]{Opcodes.RETURN}, clazz.getCanonicalName().replace('.', '/'), "<init>", "(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";)V", false, true, false)));

        methods.put("<init>(Lnet/minecraft/world/World;DDD)V",
                new Method("(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";DDD)V",
                new InvokeMethod(SUPER_WORLD_COORDS, new int[]{Opcodes.RETURN}, clazz.getCanonicalName().replace('.', '/'), "<init>", "(L" + OBFUSCATED_CLASS_NAMES.get(0).getFirst() + ";DDD)V", false, true, false)));
        String name = "Entity" + getProjectileName().replaceAll(NON_ALPHA_NUMERICAL_REGEX, "") + getGunName().replaceAll(NON_ALPHA_NUMERICAL_REGEX, "");
        byte[] bytes = ExtensibleClassVisitor.modifyClassBytes(Opcodes.ASM4, clazz, name, (HashMap<String, Method>) methods.clone(), true);
        byte[] stringBytes = Util.getStringBytes(name);
        outBytes.putByteArray(stringBytes, 0, stringBytes.length);
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        methods.clear();
        methods.put("getName()Ljava/lang/String;", new Method(new BytecodeValue(getProjectileName())));
        methods.put("getCraftAmount()I", new Method(new BytecodeValue(16)));
        methods.put("<init>(I)V", new Method(new InvokeMethod(new int[]{Opcodes.ALOAD_0, Opcodes.ILOAD_1}, new int[]{Opcodes.RETURN}, ItemProjectileBase.class.getCanonicalName().replace('.', '/'), "<init>", "(I)V", false, true, false)));
        name = "Item" + getProjectileName().replaceAll(NON_ALPHA_NUMERICAL_REGEX, "");
        bytes = ExtensibleClassVisitor.modifyClassBytes(Opcodes.ASM4, ItemProjectileBase.class, name, (HashMap<String, Method>) methods.clone(), true);
        stringBytes = Util.getStringBytes(name);
        outBytes.putByteArray(stringBytes, 0, stringBytes.length);
        outBytes.putInt(bytes.length);
        outBytes.putByteArray(bytes, 0, bytes.length);

        methods.clear();
        methods.put("getName()Ljava/lang/String;", new Method(new BytecodeValue(getGunName())));
        String shootSound = getShootSound();
        if (shootSound == null || shootSound.isEmpty()) {
        	shootSound = ".";
        }
        methods.put("getShootSound()Ljava/lang/String;", new Method(new BytecodeValue("guns." + shootSound.substring(0, shootSound.indexOf(".")))));
        String reloadSound = getReloadSound();
        if (reloadSound == null || reloadSound.isEmpty()) {
        	reloadSound = ".";
        }
        methods.put("getReloadSound()Ljava/lang/String;", new Method(new BytecodeValue("guns." + reloadSound.substring(0, reloadSound.indexOf(".")))));
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
        methods.put("getReloadParts()I", new Method(new BytecodeValue(getReloadParts())));
        methods.put("<init>(ILcom/heuristix/ItemProjectile;)V", new Method(new InvokeMethod(new int[]{Opcodes.ALOAD_0, Opcodes.ILOAD_1, Opcodes.ALOAD_2}, new int[]{Opcodes.RETURN}, ItemGunBase.class.getCanonicalName().replace('.', '/'), "<init>", "(ILcom/heuristix/ItemProjectile;)V", false, true, false)));
        name = "Item" + getGunName().replaceAll(NON_ALPHA_NUMERICAL_REGEX, "");
        bytes = ExtensibleClassVisitor.modifyClassBytes(Opcodes.ASM4, ItemGunBase.class, name, (HashMap<String, Method>) methods.clone(), true);
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
            if (bytes == null) {
            	bytes = new byte[0];
            }
            outBytes.putInt(bytes.length);
            outBytes.putByteArray(bytes, 0, bytes.length);
        }

        Map<String, int[]> properties = getProperties();
        outBytes.putInt(properties.size());
        for (Map.Entry<String, int[]> entry : properties.entrySet()) {
            stringBytes = Util.getStringBytes(entry.getKey());
            outBytes.putByteArray(stringBytes, 0, stringBytes.length);
            bytes = Util.getByteArray(entry.getValue());
            if (bytes == null) {
            	bytes = new byte[0];
            }
            outBytes.putInt(bytes.length);
            outBytes.putByteArray(bytes, 0, bytes.length);
        }

        out.write(outBytes.data);
    }

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

    protected abstract int getReloadParts();

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

    protected abstract void setReloadParts(int parts);

    protected abstract void addProperty(String property, int[] value);

    protected abstract void addResource(String resource, byte[] value);

}
