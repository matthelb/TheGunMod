package com.heuristix.guns.util;

import com.heuristix.EntityProjectile;
import com.heuristix.ItemGunBase;
import com.heuristix.ItemProjectileBase;
import com.heuristix.guns.*;
import com.heuristix.guns.asm.Opcodes;
import net.minecraft.src.mod_Guns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/30/11
 * Time: 5:21 PM
 */
public final class GunDumper {

    private static int id;

    private GunDumper() { }

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Incorrect args length");
            System.exit(1);
        }
        Log.addHandler(new mod_Guns());
        File inDir = new File(args[0]);
        File outDir = new File(args[1]);
        outDir.mkdirs();
        for (File f : inDir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".gun2") || name.toLowerCase().endsWith(".gun");
            }
        })) {
            if(f.getName().endsWith(".gun2")) {
                try {
                    Gun gun = new Gun(Util.read(f));
                    for (Pair<String, byte[]> clazz : gun.getClasses()) {
                        File dir = new File(outDir.getAbsolutePath() + File.separator + f.getName());
                        dir.mkdirs();
                        FileOutputStream out = new FileOutputStream(new File(dir.getAbsolutePath() + File.separator + clazz.getFirst() + ".class"));
                        out.write(clazz.getSecond());
                        out.flush();
                        out.close();
                    }
                    /*for (int i = 0; i < gun.getResources().size(); i++) {
                        String name = "";
                        switch (i) {
                            case 0:
                                name = "projectile-icon.png";
                                break;
                            case 1:
                                name = "gun-icon.png";
                                break;
                            case 2:
                                name = "shoot-sound.ogg";
                                break;
                            case 3:
                                name = "reload-sound.ogg";
                                break;
                            default:
                                break;
                        }
                        File dir = new File(outDir.getAbsolutePath() + File.separator + f.getName());
                        dir.mkdirs();
                        FileOutputStream out = new FileOutputStream(new File(dir.getAbsolutePath() + File.separator + name));
                        out.write(gun.getResources().get(i));
                        out.flush();
                        out.close();
                        if(id == 0) {
                            id = gun.getItemGunId();
                        }
                        System.out.println(gun.getItemGunId());
                        System.out.println(gun.getItemProjectileId());
                        /*gun.getProperties().put("itemGunId", ReverseBuffer.getInt(id++));
                        gun.getProperties().put("itemBulletId", ReverseBuffer.getInt(id++));
                        FileOutputStream out1 = new FileOutputStream(new File(outDir.getAbsolutePath() + File.separator + f.getName() + ".gun2"));
                        gun.write(out1);
                        out.flush();
                        out.close();
                    }             */
                } catch (IllegalArgumentException e) {
                    System.out.println("Could not read gun file: " + f.toString());
                }
            } else {
                Object[] data = readOldGun(f);
                File dir = new File(outDir.getAbsolutePath() + File.separator + f.getName());
                dir.mkdirs();
                for(int i = 0; i < 6; i += 2) {
                    FileOutputStream out = new FileOutputStream(new File(dir.getAbsolutePath() + File.separator + data[i] + ".class"));
                    out.write((byte[]) data[i + 1]);
                    out.flush();
                    out.close();
                }
                for (int i = 6; i < data.length; i++) {
                        String name = "";
                        switch (i - 6) {
                            case 0:
                                name = "projectile-icon.png";
                                break;
                            case 1:
                                name = "gun-icon.png";
                                break;
                            case 2:
                                name = "shoot-sound.ogg";
                                break;
                            case 3:
                                name = "reload-sound.ogg";
                                break;
                            default:
                                break;
                        }
                        FileOutputStream out = new FileOutputStream(new File(dir.getAbsolutePath() + File.separator + name));
                        out.write((byte[]) data[i]);
                        out.flush();
                        out.close();
                    }
            }
        }
    }

    private static Object[] readOldGun(File f) {
        Buffer buffer = new Buffer(Util.read(f));
        try {
            if(buffer.readInt() == 0xABCDEFAB) {
                String name = buffer.readString();

                int bulletDamage = buffer.readInt();
                float bulletRange = Float.intBitsToFloat(buffer.readInt());
                int bulletMethods = buffer.readShort();

                int itemBulletId = buffer.readInt();
                byte[] bytes = new byte[buffer.readInt()];
                buffer.readBytes(bytes, bytes.length, 0);
                byte[] bulletTextyreByte = bytes;
                int itemBulletMethods = buffer.readShort();

                int itemGunId = buffer.readInt();
                bytes = new byte[buffer.readInt()];
                buffer.readBytes(bytes, bytes.length, 0);
                byte[] gunTextureByte = bytes;
                bytes = new byte[buffer.readInt()];
                buffer.readBytes(bytes, bytes.length, 0);
                byte[] itemGunShootSoundBytes = bytes;
                String itemGunShootSound = buffer.readString();
                int itemGunShotsPerMinute = buffer.readInt();
                int itemGunFireMode = buffer.readInt();
                int itemGunScope = buffer.readInt();
                float itemGunZoom = Float.intBitsToFloat(buffer.readInt());
                int itemGunClipSize = buffer.readInt();
                int itemGunReloadTime = buffer.readInt();
                int itemGunRecoilX = buffer.readInt();
                int itemGunRecoilY = buffer.readInt();
                int itemGunMethods = buffer.readShort();

                int itemGunRoundsPerMinute = buffer.readInt();
                float bulletSpread = Float.intBitsToFloat(buffer.readInt());
                int itemGunRoundsPerShot = buffer.readInt();
                if(itemGunRoundsPerShot == 0)
                    itemGunRoundsPerShot = 1;

                HashMap<String, Method> methods = new HashMap<String, Method>();
                methods.put("getDamage()I", new Method(new BytecodeValue(bulletDamage)));
                methods.put("getEffectiveRange()F", new Method(new BytecodeValue(bulletRange)));
                methods.put("getSpread()F", new Method(new BytecodeValue(bulletSpread)));

                byte[] bulletClassByte = null;
                try {
                    bulletClassByte = ExtensibleClassVisitor.modifyClassBytes(Opcodes.ASM4, EntityProjectile.class, "Entity" + name.replaceAll(" ", "") + "Bullet", methods, true);
                } catch (IOException e) {
                    Log.throwing(GunDumper.class, "updateFile(File file)", e, mod_Guns.class);
                    return null;
                }
                byte[] itemBulletClassByte = null;
                methods.put("getName()Ljava/lang/String;", new Method(new BytecodeValue(name + " Bullet")));
                methods.put("getCraftAmount()I", new Method(new BytecodeValue(16)));
                try {
                    itemBulletClassByte = ExtensibleClassVisitor.modifyClassBytes(Opcodes.ASM4, ItemProjectileBase.class, "Item" + name.replaceAll(" ", "") + "Bullet", methods, true);
                } catch (Exception e) {
                    Log.throwing(GunDumper.class, "updateFile(File file)", e, mod_Guns.class);
                    return null;
                }
                methods.put("getName()Ljava/lang/String;", new Method(new BytecodeValue(name)));
                methods.put("getShootSound()Ljava/lang/String;", new Method(new BytecodeValue("guns." + itemGunShootSound.substring(0, itemGunShootSound.indexOf(".")))));
                methods.put("getShotsPerMinute()I", new Method(new BytecodeValue(itemGunShotsPerMinute)));
                methods.put("getFireMode()I", new Method(new BytecodeValue(itemGunFireMode)));
                methods.put("getReloadTime()I", new Method(new BytecodeValue(itemGunReloadTime)));
                methods.put("getClipSize()I", new Method(new BytecodeValue(itemGunClipSize)));
                methods.put("getRecoilX()I", new Method(new BytecodeValue(itemGunRecoilX)));
                methods.put("getRecoilY()I", new Method(new BytecodeValue(itemGunRecoilY)));
                methods.put("getZoom()F", new Method(new BytecodeValue(itemGunZoom)));
                methods.put("getScope()I", new Method(new BytecodeValue(itemGunScope)));
                methods.put("getBulletDamage()I", new Method(new BytecodeValue(bulletDamage)));
                methods.put("getBulletEffectiveRange()F", new Method(new BytecodeValue(bulletRange)));
                methods.put("getBulletSpread()F", new Method(new BytecodeValue(bulletSpread)));
                if(itemGunRoundsPerMinute != 0)
                    methods.put("getRoundsPerMinute()I", new Method(new BytecodeValue(itemGunRoundsPerMinute)));
                methods.put("getRoundsPerShot()I", new Method(new BytecodeValue(itemGunRoundsPerShot)));
                byte[] itemGunClassByte = null;
                try {
                    itemGunClassByte = ExtensibleClassVisitor.modifyClassBytes(Opcodes.ASM4, ItemGunBase.class, "Item" + name.replaceAll(" ", ""), methods, true);
                } catch (Exception e) {
                    Log.throwing(GunDumper.class, "updateFile(File file)", e, mod_Guns.class);
                    return null;
                }
                return new Object[]{"Entity" + name.replaceAll(" ", "") + "Bullet", bulletClassByte, "Item" + name.replaceAll(" ", "") + "Bullet", itemBulletClassByte, "Item" + name.replaceAll(" ", ""), itemGunClassByte, bulletTextyreByte, gunTextureByte, itemGunShootSoundBytes};
            }
        } catch (Exception e) {
            Log.throwing(GunDumper.class, "updateFile(File file)", e, mod_Guns.class);
        }
        return null;
    }
}
