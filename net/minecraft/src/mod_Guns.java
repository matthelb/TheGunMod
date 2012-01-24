package net.minecraft.src;

import com.heuristix.*;
import com.heuristix.asm.Opcodes;
import com.heuristix.guns.BlockCraftGuns;
import com.heuristix.net.PacketDamageItem;
import com.heuristix.net.PacketFireProjectile;
import com.heuristix.net.PacketOpenCraftGuns;
import com.heuristix.swing.GunCreator;
import com.heuristix.util.*;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Created by IntelliJ IDEA.
* User: Matt
* Date: 9/1/11
* Time: 9:47 AM
*/
public class mod_Guns extends ModMP {

    public static boolean DEBUG = true;

    private static final Map<String, Class> classes = new HashMap<String, Class>();

    private static int uniqueId;

    private static final Map<String, ItemProjectile> projectiles = new HashMap<String, ItemProjectile>();

    static {
        ObfuscatedNames names = ObfuscatedNames.getInstance();
        names.putName(World.class, "fq");
        names.putName(EntityLiving.class, "lx");
    }

    public mod_Guns() {
        load();
    }

    @Override
    public String getVersion() {
        return "0.9.5" + " for " + CURRENT_VERSION;
    }

    public void load() {
        try {
            initItems();
        } catch (Exception e) {
            System.out.println("Failed to initialize items");
        }
        Util.setPacketId(PacketFireProjectile.class, PacketFireProjectile.PACKET_ID, true, true);
        Util.setPacketId(PacketDamageItem.class, PacketDamageItem.PACKET_ID, true, true);
        Util.setPacketId(PacketOpenCraftGuns.class, PacketOpenCraftGuns.PACKET_ID, true, true);
        registerBlock(new BlockCraftGuns(212));
        ModLoader.SetInGameHook(this, true, false);
    }

    private void initItems() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException, InstantiationException {
        File gunsDir = Util.getHeuristixDir("guns");
        if (gunsDir != null) {
            if (!gunsDir.exists()) {
                gunsDir.mkdirs();
            } else {
                for (File f : gunsDir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".gun2");
                    }
                })) {
                    Gun gun = new Gun(Util.read(f));
                    if(gun != null) {
                        registerGun(gun);
                    }
                }

            }
        } else {
            System.out.println("Could not find minecraft directory. Are you using an obscure operating system?");
        }
    }

    public void registerGun(Gun gun) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        List<com.heuristix.util.Pair<String, byte[]>> gunClasses = gun.getClasses();

        Class entityBulletClass = classes.get(gunClasses.get(0).getFirst());
        if (entityBulletClass == null) {
            ObfuscatedNames names = ObfuscatedNames.getInstance();
            byte[] entityBulletClassBytes = gunClasses.get(0).getSecond();
            String projectileType = ClassDescriptor.getClassDescription(entityBulletClassBytes).getSuperName();
            HashMap<String, Method> methods = new HashMap<String, Method>();
            String worldClass = (DEBUG) ? "net/minecraft/src/World" : names.getName(World.class);
            String entityLivingClass = (DEBUG) ? "net/minecraft/src/EntityLiving" : names.getName(EntityLiving.class);
            for (int i = 0; i < GunCreator.OBFUSCATED_CLASS_NAMES.size(); i++) {
                com.heuristix.util.Pair<String, String> obfuscatedNames = GunCreator.OBFUSCATED_CLASS_NAMES.get(i);
                methods.put("<init>(L" + obfuscatedNames.getFirst() + ";L" + obfuscatedNames.getSecond() + ";)V", new Method("(L" + worldClass + ";L" + entityLivingClass + ";)V",
                        new InvokeMethod(GunCreator.SUPER_WORLD_ENTITY, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(L" + worldClass + ";L" + entityLivingClass + ";)V", false, true, false)));
                methods.put("<init>(L" + obfuscatedNames.getFirst() + ";)V", new Method("(L" + worldClass + ";)V",
                        new InvokeMethod(GunCreator.SUPER_WORLD, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(L" + worldClass + ";)V", false, true, false)));
                methods.put("<init>(L" + obfuscatedNames.getFirst() + ";DDD)V", new Method("(L" + worldClass + ";DDD)V",
                            new InvokeMethod(GunCreator.SUPER_WORLD_COORDS, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(L" + worldClass +";DDD)V", false, true, false)));
            }
            entityBulletClassBytes = ExtensibleClassAdapter.modifyClassBytes(entityBulletClassBytes, gunClasses.get(0).getFirst(), methods, false);
            entityBulletClass = Util.defineClass(entityBulletClassBytes, null/*gunClasses.get(0).getFirst()*/, EntityProjectile.class.getClassLoader());
            classes.put(entityBulletClass.getName(), entityBulletClass);
        }
        ItemProjectile itemBullet = projectiles.get(gunClasses.get(1).getFirst());
        if (itemBullet == null) {
            Class itemBulletClass = classes.get(gunClasses.get(1).getFirst());
            if (itemBulletClass == null) {
                itemBulletClass = Util.defineClass(gunClasses.get(1).getSecond(), null/*gunClasses.get(1).getFirst()*/, ItemProjectileBase.class.getClassLoader());
                classes.put(itemBulletClass.getName(), itemBulletClass);
            }
            Constructor itemBulletConstructor = itemBulletClass.getDeclaredConstructor(int.class);
            itemBulletConstructor.setAccessible(true);
            itemBullet = (ItemProjectile) itemBulletConstructor.newInstance(gun.getItemBulletId());
            projectiles.put(itemBulletClass.getName(), itemBullet);
        }

        Class itemGunClass = classes.get(gunClasses.get(2).getFirst());
        ItemGun itemGun = null;
        if (itemGunClass == null) {
            itemGunClass = Util.defineClass(gunClasses.get(2).getSecond(), null/*gunClasses.get(2).getFirst()*/, ItemGunBase.class.getClassLoader());
            classes.put(itemGunClass.getName(), itemGunClass);
            Constructor itemGunConstructor = itemGunClass.getDeclaredConstructor(int.class, ItemProjectile.class);
            itemGunConstructor.setAccessible(true);
            itemGun = (ItemGun) itemGunConstructor.newInstance(gun.getItemGunId(), itemBullet);
        }
        itemBullet.putProjectileClass(itemGun, entityBulletClass);
        projectiles.put(gunClasses.get(1).getFirst(), itemBullet);
        ModLoaderMp.RegisterEntityTrackerEntry(entityBulletClass, true ,getUniqueEntityProjectileId());
        ModLoaderMp.RegisterEntityTracker(entityBulletClass, 256, 1);
    }

    @Override
    public void HandlePacket(Packet230ModLoader packet, EntityPlayerMP player) {
        switch(packet.packetType) {
            case 0:
                ItemProjectileShooter shooter = getEquippedShooter(player);
                if(shooter != null && shooter.shiftedIndex == packet.dataInt[0]) {
                    shooter.spawnProjectile(player.worldObj, player);
                }
                break;
            case 1:
                ItemStack stack = player.getCurrentEquippedItem();
                if(stack != null && stack.itemID == packet.dataInt[0] && player.inventory.currentItem == packet.dataInt[1]) {
                    stack.damageItem(packet.dataInt[2], player);
                }
                break;
            default:
                System.out.println("Unknown packet type.");
                break;
        }
    }

    private static int getUniqueEntityProjectileId() {
        return uniqueId++;
    }

    private static ItemProjectileShooter getEquippedShooter(EntityPlayer player) {
        ItemStack itemStack = player.getCurrentEquippedItem();
        if(itemStack != null) {
            Item item = itemStack.getItem();
            if(item != null && item instanceof ItemProjectileShooter) {
                return (ItemProjectileShooter) item;
            }
        }
        return null;
    }

}