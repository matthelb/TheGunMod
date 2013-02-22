package com.heuristix;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureLoadEvent;
import net.minecraftforge.client.event.sound.SoundLoadEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;

import org.lwjgl.input.Keyboard;

import com.heuristix.guns.BaseMod;
import com.heuristix.guns.BlockCraftGuns;
import com.heuristix.guns.CommonProxy;
import com.heuristix.guns.GunManager;
import com.heuristix.guns.client.Resources;
import com.heuristix.guns.client.handler.GunKeyHandler;
import com.heuristix.guns.client.render.TextureManager;
import com.heuristix.guns.handler.GunGuiHandler;
import com.heuristix.guns.handler.GunPacketHandler;
import com.heuristix.guns.helper.IOHelper;
import com.heuristix.guns.util.Log;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid="TheGunMod", name="TheGunMod", version="@VERSION@")
@NetworkMod(clientSideRequired=true, serverSideRequired=false, channels={"TGMShootAction", "TGMArrowClick", "TGMInfo"}, packetHandler=GunPacketHandler.class)
public class TheGunMod extends BaseMod {

	private static final Properties DEFAULT_CONFIG = new Properties();

    private int reloadKey, zoomKey;
    private File gunsDir;
    private int blockArmoryId;
    
    private SoundLoadEvent event;
    
    private GunManager gunManager;
    
    //TODO change for release
    public static final boolean DEBUG = false;
    
    static {
        //TODO add new obfuscation names
        DEFAULT_CONFIG.setProperty("block.armory.id", String.valueOf(212));
        DEFAULT_CONFIG.setProperty("key.reload", Keyboard.getKeyName(Keyboard.KEY_R));
        DEFAULT_CONFIG.setProperty("key.zoom", Keyboard.getKeyName(Keyboard.KEY_Z));
        DEFAULT_CONFIG.setProperty("guns.dir", IOHelper.getHeuristixDir("guns").getAbsolutePath());
        TheGunMod mod = new TheGunMod();
        DEFAULT_CONFIG.setProperty("guns.version", mod.getModVersion());
        mod = null;
    }
    
    

    public TheGunMod() {
        this.gunManager = new GunManager(this);
    }
    
	@Instance("TheGunMod")
	public static TheGunMod instance;
	
	@SidedProxy(clientSide = "com.heuristix.guns.client.ClientProxy", serverSide = "com.heuristix.guns.CommonProxy")
	public static CommonProxy proxy;
	
	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void onSoundLoadEvent(SoundLoadEvent event) {
		this.event = event;
	}
	
	@SideOnly(Side.CLIENT)
	@ForgeSubscribe
	public void onTexturePackChange(TextureLoadEvent event) {
		if (isTexturesRegistered()) {
			MinecraftForgeClient.preloadTexture("/" + TextureManager.getTextureFileName(event.pack.getTexturePackResolution()));
		}
	}
	
	@PreInit
    public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		proxy.registerObfuscatedNames();
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
    	return gunManager.initGuns(gunsDir);
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
    
}
