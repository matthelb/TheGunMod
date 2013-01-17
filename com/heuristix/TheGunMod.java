package com.heuristix;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.minecraft.src.ModLoader;

import com.heuristix.guns.AbstractGunBridge;
import com.heuristix.guns.BaseMod;
import com.heuristix.guns.BlockCraftGuns;
import com.heuristix.guns.ContainerCraftGuns;
import com.heuristix.guns.DefaultGunBridge;
import com.heuristix.guns.Gun;
import com.heuristix.guns.GunBridge;
import com.heuristix.guns.Scope;
import com.heuristix.guns.Util;
import com.heuristix.guns.asm.Opcodes;
import com.heuristix.guns.util.ClassDescriptor;
import com.heuristix.guns.util.ExtensibleClassVisitor;
import com.heuristix.guns.util.InvokeMethod;
import com.heuristix.guns.util.Log;
import com.heuristix.guns.util.Method;
import com.heuristix.guns.util.Pair;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid="TheGunMod", name="TheGunMod", version="1.2.2")
@NetworkMod(clientSideRequired=true, serverSideRequired=false)
public class TheGunMod extends BaseMod {

	@Instance("TheGunMod")
	public static TheGunMod _instance;
	
	@PreInit
    public void preInit(FMLPreInitializationEvent event) {
            // Stub Method
    }
   
    @Init
    public void load(FMLInitializationEvent event) {
    	try {
            initItems();
        } catch (Exception e) {
            Log.fine("Failed to initialize items", getClass());
            Log.throwing(getClass(), "load()", e, getClass());
        }
        File hitSound = Util.getFile("hit.ogg", Util.getHeuristixDir("sounds"));
        if (hitSound != null) {
            registerSound("guns/hit.ogg", Util.read(hitSound));
        } else {
            Log.fine("Missing sound file hit.ogg in .minecraft/heuristix/sounds/ directory");
        }
        File moveSound = Util.getFile("move.ogg", Util.getHeuristixDir("sounds"));
        if (moveSound != null) {
            registerSound("guns/move.ogg", Util.read(moveSound));
        } else {
            Log.fine("Missing sound file move.ogg in .minecraft/heuristix/sounds/ directory");
        }
        BufferedImage blockTextures = getImageResource("/heuristix/gun-blocks.png");
        if (blockTextures != null) {
            Integer[] textureIndices = registerTextures(false, blockTextures, 16);
            BlockCraftGuns block = new BlockCraftGuns(blockArmoryId);
            registerBlock(block, textureIndices[0]);
            block.setSideTextureIndex(textureIndices[1]);
            block.setBottomTextureIndex(textureIndices[2]);
        } else {
            Log.fine("Missing block textures file gun-blocks.png in minecraft.jar/heuristix/", getClass());
        }
        ModLoader.registerKey(this, reloadKeybinding, false);
        ModLoader.registerKey(this, zoomKeybinding, false);
        ModLoader.addLocalization("key.reload", "Reload");
        ModLoader.addLocalization("key.zoom", "Zoom");
        ModLoader.registerContainerID(this, ContainerCraftGuns.INVENTORY_TYPE);
        ModLoader.registerPacketChannel(this, "TGMShootAction");
        ModLoader.registerPacketChannel(this, "TGMArrowClick");
        setHookedIngame(true);
    }
    
    private void initItems() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
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
                    Gun gun = null;
                    try {
                        gun = new Gun(Util.read(f));
                    } catch (Exception e) {
                        Log.throwing(getClass(), "initItems()", e, getClass());
                    }
                    if (gun != null) {
                        int[] versionCreated = gun.getProperties().get("versionCreated");
                        String version = "";
                        if (versionCreated == null || !(version = Util.getStringFromBytes(versionCreated)).equals(AbstractGunBridge.VERSION)) {
                            if (gb == null) {
                                gb = new DefaultGunBridge();
                            }
                            gb.read(gun, DEBUG);
                            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                            gb.write(byteOut);
                            new FileOutputStream(f).write(byteOut.toByteArray());
                            Log.fine("Updated gun file " + f.getName() + " from version " + version + " to " + AbstractGunBridge.VERSION, getClass());
                            gun = new Gun(Util.read(f));
                        }
                        registerGun(gun, DEBUG);
                    } else {
                        Log.fine("Could not load gun " + f.getName(), getClass());
                    }
                }

            }
        } else {
            Log.fine("Could not find .minecraft directory. Are you using an obscure operating system?", getClass());
        }
    }

    public void registerGun(Gun gun, boolean deobfuscate) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        List<Pair<String, byte[]>> gunClasses = gun.getClasses();
        Map<String, byte[]> resources = gun.getResources();

        Class entityProjectileClass = nameToEntityProjectileClassMap.get(gunClasses.get(0).getFirst());
        if (entityProjectileClass == null) {
            String projectileType = ClassDescriptor.getClassDescription(Opcodes.ASM4, gunClasses.get(0).getSecond()).getSuperName();
            HashMap<String, Method> methods = new HashMap<String, Method>();
            String worldClass = (deobfuscate) ? "net/minecraft/src/World" : AbstractGunBridge.OBFUSCATED_CLASS_NAMES.get(0).getFirst(), entityLivingClass = (deobfuscate) ? "net/minecraft/src/EntityLiving" : AbstractGunBridge.OBFUSCATED_CLASS_NAMES.get(0).getSecond();
            for (int i = 0; i < AbstractGunBridge.OBFUSCATED_CLASS_NAMES.size(); i++) {
                Pair<String, String> obfuscatedNames = AbstractGunBridge.OBFUSCATED_CLASS_NAMES.get(i);
                methods.put("<init>(L" + obfuscatedNames.getFirst() + ";L" + obfuscatedNames.getSecond() + ";)V",
                        new Method("(L" + worldClass + ";L" + entityLivingClass + ";)V",
                                new InvokeMethod(AbstractGunBridge.SUPER_WORLD_ENTITY, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(L" + worldClass + ";L" + entityLivingClass + ";)V", false, true, false)));
                methods.put("<init>(L" + obfuscatedNames.getFirst() + ";)V",
                        new Method("(L" + worldClass + ";)V",
                                new InvokeMethod(AbstractGunBridge.SUPER_WORLD, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(L" + worldClass + ";)V", false, true, false)));
                methods.put("<init>(L" + obfuscatedNames.getFirst() + ";DDD)V",
                        new Method("(L" + worldClass + ";DDD)V",
                                new InvokeMethod(AbstractGunBridge.SUPER_WORLD_COORDS, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(L" + worldClass + ";DDD)V", false, true, false)));
            }
            byte[] entityProjectileClassBytes = ExtensibleClassVisitor.modifyClassBytes(Opcodes.ASM4, gunClasses.get(0).getSecond(), gunClasses.get(0).getFirst(), (Map<String, Method>) methods.clone(), false);
            entityProjectileClass = Util.defineClass(entityProjectileClassBytes, null/*gunClasses.get(0).getFirst()*/, EntityProjectile.class.getClassLoader());
            nameToEntityProjectileClassMap.put(entityProjectileClass.getName(), entityProjectileClass);
            trackerIdToEntityProjectileClassMap.put(projectileTrackerId, entityProjectileClass);
            ModLoader.addEntityTracker(this, entityProjectileClass, projectileTrackerId++, 100, 1, true);
        }
        ItemProjectile itemProjectile = nameToItemProjectileMap.get(gunClasses.get(1).getFirst());
        if (itemProjectile == null) {
            Class itemProjectileClass = nameToEntityProjectileClassMap.get(gunClasses.get(1).getFirst());
            if (itemProjectileClass == null) {
                itemProjectileClass = Util.defineClass(gunClasses.get(1).getSecond(), null/*gunClasses.get(1).getFirst()*/, ItemProjectileBase.class.getClassLoader());
                nameToEntityProjectileClassMap.put(itemProjectileClass.getName(), itemProjectileClass);
            }
            Constructor itemProjectileConstructor = itemProjectileClass.getConstructors()[0];
            itemProjectileConstructor.setAccessible(true);
            itemProjectile = (ItemProjectile) itemProjectileConstructor.newInstance(gun.getItemProjectileId());
            nameToItemProjectileMap.put(itemProjectileClass.getName(), itemProjectile);
        }
        List<BufferedImage> projectileTextures = new LinkedList<BufferedImage>();
        List<BufferedImage> gunTextures = new LinkedList<BufferedImage>();
        for (Map.Entry<String, byte[]> entry : resources.entrySet()) {
            if (entry.getKey().contains("gun") || entry.getKey().contains("projectile")) {
                List<BufferedImage> textures = (entry.getKey().contains("gun")) ? gunTextures : projectileTextures;
                textures.add(ImageIO.read(new ByteArrayInputStream(entry.getValue())));
            }
        }
        if (itemProjectile != null) {
            itemProjectile.setIconIndex(registerTexture(true, projectileTextures.toArray(new BufferedImage[projectileTextures.size()])));
            registerItem(itemProjectile);
        }

        Class itemGunClass = nameToEntityProjectileClassMap.get(gunClasses.get(2).getFirst());
        ItemGun itemGun = null;
        if (itemGunClass == null) {
            itemGunClass = Util.defineClass(gunClasses.get(2).getSecond(), null/*gunClasses.get(2).getFirst()*/, ItemGunBase.class.getClassLoader());
            nameToEntityProjectileClassMap.put(itemGunClass.getName(), itemGunClass);
            Constructor itemGunConstructor = itemGunClass.getDeclaredConstructor(int.class, ItemProjectile.class);
            itemGunConstructor.setAccessible(true);
            itemGun = (ItemGun) itemGunConstructor.newInstance(gun.getItemGunId(), itemProjectile);
        }
        if (itemGun != null) {
            itemGun.setIconIndex(registerTexture(true, gunTextures.toArray(new BufferedImage[projectileTextures.size()])));
            registerItem(itemGun);
            registerSound(itemGun.getShootSound().replaceFirst("\\.", "/") + ".ogg", resources.get("shootSound"));
            if (resources.get("reloadSound") != null) {
                registerStreaming(itemGun.getReloadSound().replaceFirst("\\.", "/") + ".ogg", resources.get("reloadSound"));
            }
            if (itemGun.getScope() == Scope.CUSTOM.ordinal()) {
                itemGun.setCustomScope(ImageIO.read(new ByteArrayInputStream(resources.get("customScope"))));
            }
        }
        itemProjectile.putProjectileClass(itemGun, entityProjectileClass);
        nameToItemProjectileMap.put(gunClasses.get(1).getFirst(), itemProjectile);
        Log.fine("Gun loaded " + itemGun.getName(), getClass());
    }
   
    @PostInit
    public void postInit(FMLPostInitializationEvent event) {
            // Stub Method
    }
    
}
