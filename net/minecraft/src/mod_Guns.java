package net.minecraft.src;

import com.heuristix.ItemGun;
import com.heuristix.asm.Opcodes;
import com.heuristix.guns.*;
import com.heuristix.net.PacketOpenCraftGuns;
import com.heuristix.*;
import com.heuristix.util.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;

/**
* Created by IntelliJ IDEA.
* User: Matt
* Date: 9/1/11
* Time: 9:47 AM
*/
public class mod_Guns extends ModMP {

    public static final int MOUSE_LEFT = 0;
    public static final int MOUSE_RIGHT = 1;

    private static final Properties DEFAULT_CONFIG = new Properties();

    private KeyBinding reloadKeybinding = new KeyBinding("key.reload", Keyboard.KEY_R);
    private KeyBinding zoomKeybinding = new KeyBinding("key.zoom", Keyboard.KEY_Z);
    private File gunsDir;

    public static boolean DEBUG = false;

    private boolean justAttemptedFire;
    private boolean reflectionInit;

    private boolean isZoomed;
    private float currentZoom;

    public static int recoilY, recoilX;

    private static int uniqueId = 0;//Gun.MAGIC;

    static {
        ReflectionFacade names = ReflectionFacade.getInstance();
        names.putField(RenderManager.class, "itemRenderer", "f");
        names.putField(EntityRenderer.class, "cameraZoom", "V");
        names.putField(EntityRenderer.class, "itemRenderer", "c");
        names.putField(ItemRenderer.class, "equippedProgress", "c");
        names.putField(ItemRenderer.class, "itemToRender", "b");
        names.putField(ItemRenderer.class, "mapItemRenderer", "f");
        names.putField(ItemRenderer.class, "mc", "a");
        names.putField(ItemRenderer.class, "prevEquippedProgress", "d");

        DEFAULT_CONFIG.setProperty("key.reload", Keyboard.getKeyName(Keyboard.KEY_R));
        DEFAULT_CONFIG.setProperty("key.zoom", Keyboard.getKeyName(Keyboard.KEY_Z));
        DEFAULT_CONFIG.setProperty("guns.dir", Util.getHeuristixDir("guns").getAbsolutePath());
    }

    private static final Map<String, Class> classes = new HashMap<String, Class>();
    private static final Map<String, ItemProjectile> projectiles = new HashMap<String, ItemProjectile>();

    public mod_Guns() {
    }

    public String getModVersion() {
        return "0.9.82";
    }

    @Override
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
            Log.throwing(getClass(), "load()", e, getClass());
        }
        File hitSound = Util.getFile("hit.ogg", Util.getHeuristixDir("sounds"));
        if(hitSound != null) {
            registerSound("guns/hit.ogg", Util.read(hitSound));
        } else {
            Log.fine("Missing sound file hit.ogg in .minecraft/heuristix/sounds/ directory");
        }
        File moveSound = Util.getFile("move.ogg", Util.getHeuristixDir("sounds"));
        if(moveSound != null) {
            registerSound("guns/move.ogg", Util.read(moveSound));
        } else {
            Log.fine("Missing sound file move.ogg in .minecraft/heuristix/sounds/ directory");
        }
        BufferedImage blockTextures = getImageResource("/heuristix/gun-blocks.png");
        if(blockTextures != null) {
            Integer[] textureIndices = registerTextures(false, blockTextures, 16);
            BlockCraftGuns block = new BlockCraftGuns(212);
            registerBlock(block, textureIndices[0]);
            block.setSideTextureIndex(textureIndices[1]);
            block.setBottomTextureIndex(textureIndices[2]);
        } else {
            Log.fine("Missing block textures file blocks.png in minecraft.jar/heuristix/", getClass());
        }
        ModLoader.RegisterKey(this, reloadKeybinding, false);
        ModLoader.RegisterKey(this, zoomKeybinding, false);
        ModLoader.AddLocalization("key.reload", "Reload");
        ModLoader.AddLocalization("key.zoom", "Zoom");
        ModLoader.SetInGameHook(this, true, false);
        Util.setPacketId(PacketOpenCraftGuns.class, PacketOpenCraftGuns.PACKET_ID, true, true);
        ModLoaderMp.RegisterGUI(this, PacketOpenCraftGuns.INVENTORY_TYPE);

    }

    public Properties getConfig() throws IOException {
        File configFile = Util.getHeuristixFile("guns", "config.ini");
        Properties config = DEFAULT_CONFIG;
        if(configFile.exists()) {
            config.load(new FileInputStream(configFile));
            if(config.getProperty("guns.version").equals(DEFAULT_CONFIG.getProperty("guns.version"))) {
                return config;
            } else {
                for(Map.Entry<Object, Object> entry : DEFAULT_CONFIG.entrySet()) {
                    if(!config.contains(entry.getKey())) {
                        config.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
        config.store(new FileOutputStream(configFile), "TheGunMod v" + getVersion() + " Configuration");
        return config;
    }

    public void loadConfig(Properties config) {
        reloadKeybinding = new KeyBinding("key.reload", Keyboard.getKeyIndex(config.getProperty("key.reload")));
        zoomKeybinding = new KeyBinding("key.zoom", Keyboard.getKeyIndex(config.getProperty("key.zoom")));
        gunsDir = new File(config.getProperty("guns.dir"));
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
                    Gun gun = new Gun(Util.read(f));
                    if(gun != null) {
                        int[] versionCreated = gun.getProperties().get("versionCreated");
                        String version = "";
                        if(versionCreated == null || !(version = Util.getStringFromBytes(versionCreated)).equals(AbstractGunBridge.VERSION)) {
                            if(gb == null) {
                                gb = new DefaultGunBridge();
                            }
                            gb.read(gun, DEBUG);
                            gb.write(new FileOutputStream(f));
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
            Log.fine("Could not find minecraft directory. Are you using an obscure operating system?", getClass());
        }
    }

    public void registerGun(Gun gun, boolean deobfuscate) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        List<Pair<String, byte[]>> gunClasses = gun.getClasses();
        Map<String, byte[]> resources = gun.getResources();

        Class entityBulletClass = classes.get(gunClasses.get(0).getFirst());
        if (entityBulletClass == null) {
            byte[] entityBulletClassBytes = gunClasses.get(0).getSecond();
            if (deobfuscate) {
                String projectileType = ClassDescriptor.getClassDescription(entityBulletClassBytes).getSuperName();
                HashMap<String, Method> methods = new HashMap<String, Method>();
                for (int i = 0; i < AbstractGunBridge.OBFUSCATED_CLASS_NAMES.size(); i++) {
                    Pair<String, String> ReflectionFacade = AbstractGunBridge.OBFUSCATED_CLASS_NAMES.get(i);
                    methods.put("<init>(L" + ReflectionFacade.getFirst() + ";L" + ReflectionFacade.getSecond() + ";)V", new Method("(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityLiving;)V",
                            new InvokeMethod(AbstractGunBridge.SUPER_WORLD_ENTITY, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityLiving;)V", false, true, false)));
                    methods.put("<init>(L" + ReflectionFacade.getFirst() + ";)V", new Method("(Lnet/minecraft/src/World;)V",
                            new InvokeMethod(AbstractGunBridge.SUPER_WORLD, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(Lnet/minecraft/src/World;)V", false, true, false)));
                    methods.put("<init>(L" + ReflectionFacade.getFirst() + ";DDD)V", new Method("(Lnet/minecraft/src/World;DDD)V",
                            new InvokeMethod(AbstractGunBridge.SUPER_WORLD_COORDS, new int[]{Opcodes.RETURN}, projectileType, "<init>", "(Lnet/minecraft/src/World;DDD)V", false, true, false)));
                }
                entityBulletClassBytes = ExtensibleClassAdapter.modifyClassBytes(entityBulletClassBytes, gunClasses.get(0).getFirst(), methods, false);
            }
            entityBulletClass = Util.defineClass(entityBulletClassBytes, null/*gunClasses.get(0).getFirst()*/, EntityProjectile.class.getClassLoader());
            classes.put(entityBulletClass.getName(), entityBulletClass);
            ModLoaderMp.RegisterNetClientHandlerEntity(entityBulletClass, true, getUniqueEntityProjectileId());
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
        List<BufferedImage> projectileTextures = new LinkedList<BufferedImage>();
        List<BufferedImage> gunTextures = new LinkedList<BufferedImage>();
        for(Map.Entry<String, byte[]> entry : resources.entrySet()) {
            if(entry.getKey().contains("gun") || entry.getKey().contains("projectile")) {
                List<BufferedImage> textures = (entry.getKey().contains("gun")) ? gunTextures : projectileTextures;
                textures.add(ImageIO.read(new ByteArrayInputStream(entry.getValue())));
            }
        }
        if (itemProjectile != null) {
            itemProjectile.setIconIndex(registerTexture(true, projectileTextures.toArray(new BufferedImage[projectileTextures.size()])));
            registerItem(itemProjectile);
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
        if (itemGun != null) {
            itemGun.setIconIndex(registerTexture(true, gunTextures.toArray(new BufferedImage[projectileTextures.size()])));
            registerItem(itemGun);
            registerSound(itemGun.getShootSound().replaceFirst("\\.", "/") + ".ogg", resources.get("shootSound"));
            if (resources.get("reloadSound") != null) {
                registerStreaming(itemGun.getReloadSound().replaceFirst("\\.", "/") + ".ogg", resources.get("reloadSound"));
            }
        }
        itemProjectile.putProjectileClass(itemGun, entityBulletClass);
        projectiles.put(gunClasses.get(1).getFirst(), itemProjectile);
        Log.fine("Gun loaded " + itemGun.getName(), getClass());
    }

    private boolean initReflection(Minecraft mc) {
        try {
            ReflectionFacade names = ReflectionFacade.getInstance();
            ItemRenderer renderer = new GunItemRenderer(mc);
            names.setFieldValue(RenderManager.class, RenderManager.instance, "itemRenderer", renderer);
            names.setFieldValue(EntityRenderer.class, mc.entityRenderer, "itemRenderer", renderer);
            names.getField(EntityRenderer.class, "cameraZoom");
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void KeyboardEvent(KeyBinding key) {
        Minecraft mc = ModLoader.getMinecraftInstance();
        if (mc != null && mc.inGameHasFocus) {
            EntityPlayer player = mc.thePlayer;
            if (player != null) {
                ItemStack equippedStack = player.getCurrentEquippedItem();
                if (equippedStack != null) {
                    Item equipped = equippedStack.getItem();
                    if (equipped != null && equipped instanceof ItemGun) {
                        ItemGun equippedGun = (ItemGun) equipped;
                        if (key.equals(reloadKeybinding)) {
                            equippedGun.reload(player, mc);
                            isZoomed = false;
                        } else if (key.equals(zoomKeybinding)) {
                            if (equippedGun.getZoom() > 1.0f || equippedGun.getScope() > 0) {
                                isZoomed = !isZoomed;
                                if (equippedGun.isReloading())
                                    equippedGun.stopReloading(mc);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean OnTick(float tick, Minecraft minecraft) {
        if (!reflectionInit) {
            reflectionInit = initReflection(minecraft);
        }

        if (minecraft.inGameHasFocus) {
            ItemStack equippedStack = minecraft.thePlayer.getCurrentEquippedItem();
            if (equippedStack != null) {
                Item equipped = equippedStack.getItem();
                if (equipped != null) {
                    if (equipped instanceof ItemProjectileShooter) {
                        ItemProjectileShooter shooter = (ItemProjectileShooter) equipped;
                        if (Mouse.isButtonDown(MOUSE_RIGHT)) {
                            if ((shooter.getFireMode() == ItemProjectileShooter.FIRE_MODE_AUTO || !justAttemptedFire) && !shooter.isBursting()) {
                                shooter.fire(minecraft.theWorld, minecraft.thePlayer, minecraft);
                                justAttemptedFire = true;
                            }
                        } else {
                            justAttemptedFire = false;
                        }
                        if (shooter.isBursting()) {
                            shooter.burst(minecraft.theWorld, minecraft.thePlayer, minecraft);
                        }
                        if (equipped instanceof ItemGun) {
                            applyRecoil(minecraft.thePlayer, (ItemGun) equipped);
                        }
                    }
                }
            }
            boolean in = (minecraft.gameSettings.thirdPersonView == 0) && (equippedStack != null) && (equippedStack.getItem() != null) && (equippedStack.getItem() instanceof ItemGun) && isZoomed;
            if (equippedStack != null) {
                Item item = equippedStack.getItem();
                if (item instanceof ItemGun)
                    zoom(minecraft, (ItemGun) item, in);
                else {
                    currentZoom = 1.0f;
                    isZoomed = false;
                    zoom(minecraft, null, in);
                }
            }
        }
        return true;
    }

    @Override
    public void AddRenderer(Map map) {
        map.put(EntityBullet.class, new RenderBullet());
        map.put(EntityFlame.class, new RenderFlame());
        map.put(EntityGrenade.class, new RenderGrenade());
        map.put(EntityRocketGrenade.class, new RenderRocketGrenade());
    }

    @Override
    public GuiScreen HandleGUI(int type) {
        switch(type) {
            case PacketOpenCraftGuns.INVENTORY_TYPE:
                Minecraft mc = ModLoader.getMinecraftInstance();
                return new GuiCraftGuns(new ContainerCraftGuns(mc.thePlayer, Util.isCreative(mc.thePlayer)));
            default:
                break;
        }
        return null;
    }

    private static void applyRecoil(EntityPlayer player, ItemGun gun) {
        double y = 0.0D;
        double y1 = recoilY;
        if (recoilY > 0.0D) {
            y = Math.min(Math.max(recoilY * 0.1000000014901161D, 0.5D), recoilY);
            recoilY -= y;
            player.rotationPitch -= y;
        }
        if (Math.abs(recoilX) > 0.0D) {
            double x;
            if (recoilX > 0.0D) {
                x = Math.min(Math.max(recoilX * 0.1000000014901161D / 2.0D, 0.25D), recoilX);
            } else {
                x = Math.max(Math.min(recoilX * 0.1000000014901161D / 2.0D, -0.25D), recoilX);
            }
            if (y != 0.0D) {
                double d3 = y / y1 * recoilX;
                if (recoilX > 0.0D) {
                    x = Math.min(d3, x);
                } else {
                    x = Math.max(d3, x);
                }
            }
            recoilX -= x;
            player.rotationYaw += x;
        }
    }

    private void zoom(Minecraft mc, ItemGun gun, boolean in) {
        float increment = 0.1f + (((gun != null) ? gun.getZoom(): 0) / 30f);
        currentZoom = (currentZoom + ((in) ? increment : -increment));
        currentZoom = (in) ? Math.min(gun.getZoom(), currentZoom) : Math.max(1.0f, currentZoom);
        ReflectionFacade.getInstance().setFieldValue(EntityRenderer.class, mc.entityRenderer, "cameraZoom", (in && gun != null && gun.getZoom() > 1.0f) ? Math.min(currentZoom, gun.getZoom()) : Math.max(currentZoom, 1.0f));
        if (gun != null && gun.getScope() > 0 && (in || currentZoom != 1.0f)) {
            Util.renderTexture(Scope.values()[gun.getScope()].getTexturePath(), 1.0f, mc);
        }
    }

    private static int getUniqueEntityProjectileId() {
        return uniqueId++;
    }

    public File getLogFile() {
        return Util.getHeuristixFile("guns", "log.txt");
    }

}