package net.minecraft.src;

import com.heuristix.*;
import com.heuristix.asm.Opcodes;
import com.heuristix.guns.BlockCraftGuns;
import com.heuristix.guns.ContainerCraftGuns;
import com.heuristix.net.PacketCraftGunsArrowClick;
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
import java.util.Properties;
import java.util.logging.Level;


/**
* Created by IntelliJ IDEA.
* User: Matt
* Date: 9/1/11
* Time: 9:47 AM
*/
public class mod_Guns extends ModMP {

    public static boolean DEBUG = false;

    private static final Map<String, Class> classes = new HashMap<String, Class>();

    private static int uniqueId;

    private static final Map<String, ItemProjectile> projectiles = new HashMap<String, ItemProjectile>();

    static {
        ReflectionFacade names = ReflectionFacade.getInstance();
        names.putName(World.class, "World", "fq");
        names.putName(EntityLiving.class, "EntityLiving", "lx");
    }

    public mod_Guns() {
        load();
    }


    public String getModVersion() {
        return "0.9.7";
    }

    public Properties getConfig() throws IOException {
        return null;
    }

    public void loadConfig(Properties config) {
    }

    public void load() {
        try {
            Log.addHandler(this);
        } catch (IOException e) {
            Log.getLogger().log(Level.SEVERE, "Could not initiate LogHandler for " + this.getClass());
        }
        try {
            loadConfig(getConfig());
        } catch (IOException e) {
            Log.fine("Failed to read config file", getClass());
        }
        try {
            initItems();
        } catch (Exception e) {
            Log.fine("Failed to initialize items", getClass());
        }
        Util.setPacketId(PacketFireProjectile.class, PacketFireProjectile.PACKET_ID, true, true);
        Util.setPacketId(PacketDamageItem.class, PacketDamageItem.PACKET_ID, true, true);
        Util.setPacketId(PacketOpenCraftGuns.class, PacketOpenCraftGuns.PACKET_ID, true, true);
        Util.setPacketId(PacketCraftGunsArrowClick.class, PacketCraftGunsArrowClick.PACKET_ID, true, true);
        registerBlock(new BlockCraftGuns(212));
        ModLoader.SetInGameHook(this, true, false);
    }

    private void initItems() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException, InstantiationException {
        File gunsDir = Util.getHeuristixDir("guns");
        if (gunsDir != null) {
            if (!gunsDir.exists()) {
                gunsDir.mkdirs();
            } else {
                GunBridge gb = null;
                for (File f : gunsDir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".gun2");
                    }
                })) {
                    Gun gun = new Gun(Util.read(f));
                    if(gun != null) {
                        int[] versionCreated = gun.getProperties().get("versionCreated");
                        if(versionCreated == null || !Util.getStringFromBytes(versionCreated).equals(AbstractGunBridge.VERSION)) {
                            if(gb == null)
                                gb = new DefaultGunBridge();
                            gb.read(gun, DEBUG);
                            gb.write(new FileOutputStream(f));
                            gun = new Gun(Util.read(f));
                        }
                        registerGun(gun, DEBUG);
                    } else {
                        Log.fine("Could not load gun " + f.getName(), getClass());
                    }
                }

            }
        } else {
            Log.fine("Could not find minecraft directory. Are you using an obscure operating system?", getClass());
        }
    }

    public void registerGun(Gun gun, boolean deobfuscate) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        List<com.heuristix.util.Pair<String, byte[]>> gunClasses = gun.getClasses();
        Map<String, byte[]> resources = gun.getResources();

        Class entityBulletClass = classes.get(gunClasses.get(0).getFirst());
        if (entityBulletClass == null) {
            ReflectionFacade names = ReflectionFacade.getInstance();
            byte[] entityBulletClassBytes = gunClasses.get(0).getSecond();
             String projectileType = ClassDescriptor.getClassDescription(entityBulletClassBytes).getSuperName();
            HashMap<String, Method> methods = new HashMap<String, Method>();
            String worldClass = (deobfuscate) ? "net/minecraft/src/World" : names.getName(World.class);
            String entityLivingClass = (deobfuscate) ? "net/minecraft/src/EntityLiving" : names.getName(EntityLiving.class);
            for (int i = 0; i < GunCreator.OBFUSCATED_CLASS_NAMES.size(); i++) {
                com.heuristix.util.Pair<String, String> obfuscatedNames = AbstractGunBridge.OBFUSCATED_CLASS_NAMES.get(i);
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
        ItemProjectile itemProjectile = projectiles.get(gunClasses.get(1).getFirst());
        if (itemProjectile == null) {
            Class itemProjectileClass = classes.get(gunClasses.get(1).getFirst());
            if (itemProjectileClass == null) {
                itemProjectileClass = Util.defineClass(gunClasses.get(1).getSecond(), null/*gunClasses.get(1).getFirst()*/, ItemProjectileBase.class.getClassLoader());
                classes.put(itemProjectileClass.getName(), itemProjectileClass);
            }
            Constructor itemProjectileConstructor = itemProjectileClass.getDeclaredConstructor(int.class);
            itemProjectileConstructor.setAccessible(true);
            itemProjectile = (ItemProjectile) itemProjectileConstructor.newInstance(gun.getItemProjectileId());
            projectiles.put(itemProjectileClass.getName(), itemProjectile);
        }

        Class itemGunClass = classes.get(gunClasses.get(2).getFirst());
        ItemGun itemGun = null;
        if (itemGunClass == null) {
            itemGunClass = Util.defineClass(gunClasses.get(2).getSecond(), null/*gunClasses.get(2).getFirst()*/, ItemGunBase.class.getClassLoader());
            classes.put(itemGunClass.getName(), itemGunClass);
            Constructor itemGunConstructor = itemGunClass.getDeclaredConstructor(int.class, ItemProjectile.class);
            itemGunConstructor.setAccessible(true);
            itemGun = (ItemGun) itemGunConstructor.newInstance(gun.getItemGunId(), itemProjectile);
        }
        itemProjectile.putProjectileClass(itemGun, entityBulletClass);
        projectiles.put(gunClasses.get(1).getFirst(), itemProjectile);
        ModLoaderMp.RegisterEntityTrackerEntry(entityBulletClass, true , getUniqueEntityProjectileId());
        ModLoaderMp.RegisterEntityTracker(entityBulletClass, 256, 1);
        Log.fine("Gun loaded " + itemGun.getName());
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
            case 2:
                if(player.craftingInventory instanceof ContainerCraftGuns) {
                    ContainerCraftGuns container = (ContainerCraftGuns) player.craftingInventory;
                    container.onArrowClick(packet.dataInt[0]);
                }
                break;
            default:
                Log.fine("Unknown packet type.", getClass());
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

    public File getLogFile() {
        return Util.getHeuristixFile("", "server-log.txt");
    }

}