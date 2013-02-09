package com.heuristix;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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
import java.util.Properties;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureLoadEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.event.ForgeSubscribe;

import com.heuristix.guns.AbstractGunBridge;
import com.heuristix.guns.BaseMod;
import com.heuristix.guns.BlockCraftGuns;
import com.heuristix.guns.CommonProxy;
import com.heuristix.guns.DefaultGunBridge;
import com.heuristix.guns.Gun;
import com.heuristix.guns.GunBridge;
import com.heuristix.guns.Scope;
import com.heuristix.guns.Util;
import com.heuristix.guns.asm.Opcodes;
import com.heuristix.guns.client.Resources;
import com.heuristix.guns.client.handler.GunKeyHandler;
import com.heuristix.guns.client.render.TextureManager;
import com.heuristix.guns.helper.IOHelper;
import com.heuristix.guns.util.ClassDescriptor;
import com.heuristix.guns.util.ExtensibleClassVisitor;
import com.heuristix.guns.util.InvokeMethod;
import com.heuristix.guns.util.Log;
import com.heuristix.guns.util.Method;
import com.heuristix.guns.util.Pair;
import com.heuristix.guns.util.ReflectionFacade;
import com.heuristix.guns.handler.GunGuiHandler;
import com.heuristix.guns.handler.GunPacketHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;

@Mod(modid="TheGunMod", name="TheGunMod", version="1.2.2")
@NetworkMod(clientSideRequired=true, serverSideRequired=false, channels={"TGMShootAction", "TGMArrowClick"}, packetHandler=GunPacketHandler.class)
public class TheGunMod extends BaseMod {

	private static final Properties DEFAULT_CONFIG = new Properties();

    private int reloadKey, zoomKey;
    private File gunsDir;
    private int blockArmoryId;
    private int projectileTrackerId;
    private SoundLoadEvent event;
    
    //TODO change for release
    public static final boolean DEBUG = true;

    private static final int BASE_PROJECTILE_TRACKER_ID = 100;
    
    static {
        //TODO add new obfuscation names
        ReflectionFacade names = ReflectionFacade.getInstance();
        names.putField(RenderManager.class, "itemRenderer", "f");
        names.putField(EntityRenderer.class, "cameraZoom", "X");
        names.putField(EntityRenderer.class, "itemRenderer", "c");
        names.putField(RenderEngine.class, "imageData", "g");
        
        DEFAULT_CONFIG.setProperty("block.armory.id", String.valueOf(212));
        DEFAULT_CONFIG.setProperty("key.reload", Keyboard.getKeyName(Keyboard.KEY_R));
        DEFAULT_CONFIG.setProperty("key.zoom", Keyboard.getKeyName(Keyboard.KEY_Z));
        DEFAULT_CONFIG.setProperty("guns.dir", IOHelper.getHeuristixDir("guns").getAbsolutePath());
        TheGunMod mod = new TheGunMod();
        DEFAULT_CONFIG.setProperty("guns.version", mod.getModVersion());
        mod = null;
    }
    
    private final Map<String, Class<?>> nameToEntityProjectileClassMap;
    private final Map<Integer, Class<? extends EntityProjectile>> trackerIdToEntityProjectileClassMap;
    private final Map<String, ItemProjectile> nameToItemProjectileMap;

    public TheGunMod() {
        this.nameToEntityProjectileClassMap = new HashMap<String, Class<?>>();
        this.nameToItemProjectileMap = new HashMap<String, ItemProjectile>();
        this.trackerIdToEntityProjectileClassMap = new HashMap<Integer, Class<? extends EntityProjectile>>();
        this.projectileTrackerId = BASE_PROJECTILE_TRACKER_ID;
    }
    
	@Instance("TheGunMod")
	public static TheGunMod _instance;
	
	@SidedProxy(clientSide = "com.heuristix.guns.client.ClientProxy", serverSide = "com.heuristix.guns.CommonProxy")
	public static CommonProxy proxy;
	
	@ForgeSubscribe
	public void loadSounds(SoundLoadEvent event) {
		this.event = event;
	}
	
	@ForgeSubscribe
	public void onTexturePackChange(TextureLoadEvent event) {
		MinecraftForgeClient.preloadTexture("/" + TextureManager.getTextureFileName(event.pack.getTexturePackResolution()));
	}
	
	@PreInit
    public void preInit(FMLPreInitializationEvent event) {
		load();
    }
   
	@Init
    public void load(FMLInitializationEvent event) {
		int guns = 0;
		try {
            guns = initItems();
        } catch (Exception e) {
            Log.fine("Failed to initialize items", getClass());
            Log.throwing(getClass(), "load()", e, getClass());
        }
		if (guns > 0) {
	        registerSounds();
	        proxy.registerKeyHandler(new GunKeyHandler(reloadKey, zoomKey));
	        proxy.registerTickHandler();
	        proxy.registerRenderers();
	        proxy.registerTextures(this);
	        registerBlock(new BlockCraftGuns(blockArmoryId));
	        NetworkRegistry.instance().registerGuiHandler(this, new GunGuiHandler());
		}
    }
	
	public void registerSounds() {
		File hitSound = IOHelper.getFile(Resources.HIT_SOUND, IOHelper.getHeuristixDir("sounds"));
	    if (hitSound != null) {
	        soundManager.registerSound("guns/" + Resources.HIT_SOUND, IOHelper.read(hitSound));
	    } else {
	        Log.fine("Missing sound file hit.ogg in .minecraft/heuristix/sounds/ directory");
	    }
	    File moveSound = IOHelper.getFile(Resources.MOVE_SOUND, IOHelper.getHeuristixDir("sounds"));
	    if (moveSound != null) {
	    	soundManager.registerSound("guns/" + Resources.MOVE_SOUND, IOHelper.read(moveSound));
	    } else {
	        Log.fine("Missing sound file move.ogg in .minecraft/heuristix/sounds/ directory");
	    }
	    if (event != null) {
	    	registerAllSounds(event);
	    }
	}
    
    public File getConfigFile() {
        return IOHelper.getHeuristixFile("guns", "config.ini");
    }

    public Properties getDefaultConfig() {
        return DEFAULT_CONFIG;
    }
    
    public void loadConfig(Properties config) {
        reloadKey = Keyboard.getKeyIndex(config.getProperty("key.reload"));
        zoomKey = Keyboard.getKeyIndex(config.getProperty("key.zoom"));
        gunsDir = new File(config.getProperty("guns.dir"));
        blockArmoryId = Integer.parseInt(config.getProperty("block.armory.id"));
    }

    public String getPropertiesId() {
        return "guns";
    }
    
    public File getLogFile() {
        return IOHelper.getHeuristixFile("guns", "log.txt");
    }

    private int initItems() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException, InstantiationException, ClassNotFoundException {
    	int registered = 0;
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
                        gun = new Gun(IOHelper.read(f));
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
                            gun = new Gun(IOHelper.read(f));
                        }
                        registerGun(gun, DEBUG);
                        registered++;
                    } else {
                        Log.fine("Could not load gun " + f.getName(), getClass());
                    }
                }

            }
        } else {
            Log.fine("Could not find .minecraft directory. Are you using an obscure operating system?", getClass());
        }
        return registered;
    }

    public void registerGun(Gun gun, boolean deobfuscate) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        List<Pair<String, byte[]>> gunClasses = gun.getClasses();
        Map<String, byte[]> resources = gun.getResources();

        Class<? extends EntityProjectile> entityProjectileClass = (Class<? extends EntityProjectile>) nameToEntityProjectileClassMap.get(gunClasses.get(0).getFirst());
        if (entityProjectileClass == null) {
            String projectileType = ClassDescriptor.getClassDescription(Opcodes.ASM4, gunClasses.get(0).getSecond()).getSuperName();
            HashMap<String, Method> methods = new HashMap<String, Method>();
            String worldClass = (deobfuscate) ? "net/minecraft/world/World" : AbstractGunBridge.OBFUSCATED_CLASS_NAMES.get(0).getFirst(), entityLivingClass = (deobfuscate) ? "net/minecraft/entity/EntityLiving" : AbstractGunBridge.OBFUSCATED_CLASS_NAMES.get(0).getSecond();
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
            entityProjectileClass = (Class<? extends EntityProjectile>) Util.defineClass(entityProjectileClassBytes, null/*gunClasses.get(0).getFirst()*/, EntityProjectile.class.getClassLoader());
            nameToEntityProjectileClassMap.put(entityProjectileClass.getName(), entityProjectileClass);
            trackerIdToEntityProjectileClassMap.put(projectileTrackerId, entityProjectileClass);
            EntityRegistry.registerModEntity(entityProjectileClass, entityProjectileClass.getName(), projectileTrackerId++, this, 100, 1, true);
        }
        ItemProjectile itemProjectile = nameToItemProjectileMap.get(gunClasses.get(1).getFirst());
        if (itemProjectile == null) {
            Class<?> itemProjectileClass = nameToEntityProjectileClassMap.get(gunClasses.get(1).getFirst());
            if (itemProjectileClass == null) {
                itemProjectileClass = Util.defineClass(gunClasses.get(1).getSecond(), null/*gunClasses.get(1).getFirst()*/, ItemProjectileBase.class.getClassLoader());
                nameToEntityProjectileClassMap.put(itemProjectileClass.getName(), itemProjectileClass);
            }
            Constructor<?> itemProjectileConstructor = itemProjectileClass.getConstructors()[0];
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
            itemProjectile.setIconIndex(textureManager.registerTexture(projectileTextures.toArray(new BufferedImage[projectileTextures.size()])));
            itemProjectile.setTextureFile("/" + TextureManager.TEXTURE_FILE_NAME_FORMAT);
            registerItem(itemProjectile);
        }

        Class<? > itemGunClass = nameToEntityProjectileClassMap.get(gunClasses.get(2).getFirst());
        ItemGun itemGun = null;
        if (itemGunClass == null) {
            itemGunClass = Util.defineClass(gunClasses.get(2).getSecond(), null/*gunClasses.get(2).getFirst()*/, ItemGunBase.class.getClassLoader());
            nameToEntityProjectileClassMap.put(itemGunClass.getName(), itemGunClass);
            Constructor<?> itemGunConstructor = itemGunClass.getDeclaredConstructor(int.class, ItemProjectile.class);
            itemGunConstructor.setAccessible(true);
            itemGun = (ItemGun) itemGunConstructor.newInstance(gun.getItemGunId(), itemProjectile);
        }
        if (itemGun != null) {
            itemGun.setIconIndex(textureManager.registerTexture(gunTextures.toArray(new BufferedImage[projectileTextures.size()])));
            itemGun.setTextureFile("/" + TextureManager.TEXTURE_FILE_NAME_FORMAT);
            registerItem(itemGun);
            soundManager.registerSound(itemGun.getShootSound().replaceFirst("\\.", "/") + ".ogg", resources.get("shootSound"));
            if (resources.get("reloadSound") != null) {
            	soundManager.registerStreaming(itemGun.getReloadSound().replaceFirst("\\.", "/") + ".ogg", resources.get("reloadSound"));
            }
            if (itemGun.getScope() == Scope.CUSTOM.ordinal()) {
                itemGun.setCustomScope(ImageIO.read(new ByteArrayInputStream(resources.get("customScope"))));
            }
        }
        itemProjectile.putProjectileClass(itemGun, entityProjectileClass);
        nameToItemProjectileMap.put(gunClasses.get(1).getFirst(), itemProjectile);
        Log.fine("Gun loaded " + itemGun.getName(), getClass());
    }

    public static ItemProjectileShooter getEquippedShooter(EntityPlayer player) {
        ItemStack itemStack = player.getCurrentEquippedItem();
        if (itemStack != null) {
            Item item = itemStack.getItem();
            if (item != null && item instanceof ItemProjectileShooter) {
                return (ItemProjectileShooter) item;
            }
        }
        return null;
    }

    public static Packet250CustomPayload getArrowClickPacket(int arrow) {
        try {
            return new Packet250CustomPayload("TGMArrowClick", writeIntsToByteArray(arrow));
        } catch (IOException e) {
            Log.throwing(TheGunMod.class, "getArrowClickPacket(int arrow)", e, TheGunMod.class);
        }
        return null;
    }

    public static byte[] writeIntsToByteArray(int... data) throws IOException {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bOut);
        for (int i : data) {
            out.writeInt(i);
        }
        return bOut.toByteArray();
    }
    
}
