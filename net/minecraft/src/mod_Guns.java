package net.minecraft.src;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
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

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.Packet23VehicleSpawn;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.heuristix.EntityProjectile;
import com.heuristix.ItemGun;
import com.heuristix.ItemGunBase;
import com.heuristix.ItemProjectile;
import com.heuristix.ItemProjectileBase;
import com.heuristix.ItemProjectileShooter;
import com.heuristix.guns.AbstractGunBridge;
import com.heuristix.guns.BlockCraftGuns;
import com.heuristix.guns.ContainerCraftGuns;
import com.heuristix.guns.DefaultGunBridge;
import com.heuristix.guns.EntityBullet;
import com.heuristix.guns.EntityFlame;
import com.heuristix.guns.EntityGrenade;
import com.heuristix.guns.EntityIncendiaryBullet;
import com.heuristix.guns.EntityRocketGrenade;
import com.heuristix.guns.GuiCraftGuns;
import com.heuristix.guns.Gun;
import com.heuristix.guns.GunBridge;
import com.heuristix.guns.GunItemRenderer;
import com.heuristix.guns.HMod;
import com.heuristix.guns.Scope;
import com.heuristix.guns.Util;
import com.heuristix.guns.asm.Opcodes;
import com.heuristix.guns.render.RenderBullet;
import com.heuristix.guns.render.RenderFlame;
import com.heuristix.guns.render.RenderGrenade;
import com.heuristix.guns.render.RenderRocketGrenade;
import com.heuristix.guns.util.ClassDescriptor;
import com.heuristix.guns.util.ExtensibleClassVisitor;
import com.heuristix.guns.util.InvokeMethod;
import com.heuristix.guns.util.Log;
import com.heuristix.guns.util.Method;
import com.heuristix.guns.util.Pair;
import com.heuristix.guns.util.ReflectionFacade;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/1/11
 * Time: 9:47 AM
 */
public class mod_Guns extends HMod {

    public static final int MOUSE_LEFT = 0;
    public static final int MOUSE_RIGHT = 1;

    public static final int FIRE = 0;
    public static final int BURST = 1;
    public static final int RELOAD = 2;
    public static final int STOP_RELOADING = 3;

    private static final Properties DEFAULT_CONFIG = new Properties();

    private KeyBinding reloadKeybinding = new KeyBinding("key.reload", Keyboard.KEY_R);
    private KeyBinding zoomKeybinding = new KeyBinding("key.zoom", Keyboard.KEY_Z);
    private File gunsDir;
    private int blockArmoryId;

    //TODO change for release
    public static final boolean DEBUG = false;

    private static final int BASE_PROJECTILE_TRACKER_ID = 100;

    private int projectileTrackerId;
    private boolean justAttemptedFire;
    private boolean reflectionInit;

    private boolean isZoomed;

    private float currentZoom;

    public static int recoilY, recoilX;

    static {
        //TODO add new obfuscation names
        ReflectionFacade names = ReflectionFacade.getInstance();
        names.putField(RenderManager.class, "itemRenderer", "f");
        names.putField(EntityRenderer.class, "cameraZoom", "X");
        names.putField(EntityRenderer.class, "itemRenderer", "c");

        DEFAULT_CONFIG.setProperty("block.armory.id", String.valueOf(212));
        DEFAULT_CONFIG.setProperty("key.reload", Keyboard.getKeyName(Keyboard.KEY_R));
        DEFAULT_CONFIG.setProperty("key.zoom", Keyboard.getKeyName(Keyboard.KEY_Z));
        DEFAULT_CONFIG.setProperty("guns.dir", Util.getHeuristixDir("guns").getAbsolutePath());
        mod_Guns mod = new mod_Guns();
        DEFAULT_CONFIG.setProperty("guns.version", mod.getModVersion());
        mod = null;
    }

    private final Map<String, Class> nameToEntityProjectileClassMap;
    private final Map<Integer, Class<? extends EntityProjectile>> trackerIdToEntityProjectileClassMap;
    private final Map<String, ItemProjectile> nameToItemProjectileMap;

    public mod_Guns() {
        this.nameToEntityProjectileClassMap = new HashMap<String, Class>();
        this.nameToItemProjectileMap = new HashMap<String, ItemProjectile>();
        this.trackerIdToEntityProjectileClassMap = new HashMap<Integer, Class<? extends EntityProjectile>>();
        this.projectileTrackerId = BASE_PROJECTILE_TRACKER_ID;
    }

    public String getModName() {
        return "TheGunMod";
    }

    //TODO change for new release
    public String getModVersion() {
        return "1.2.2";
    }

    public File getConfigFile() {
        return Util.getHeuristixFile("guns", "config.ini");
    }

    public Properties getDefaultConfig() {
        return DEFAULT_CONFIG;
    }

    public String getPropertiesId() {
        return "guns";
    }

    public void loadConfig(Properties config) {
        reloadKeybinding = new KeyBinding("key.reload", Keyboard.getKeyIndex(config.getProperty("key.reload")));
        zoomKeybinding = new KeyBinding("key.zoom", Keyboard.getKeyIndex(config.getProperty("key.zoom")));
        gunsDir = new File(config.getProperty("guns.dir"));
        blockArmoryId = Integer.parseInt(config.getProperty("block.armory.id"));
    }

    public File getLogFile() {
        return Util.getHeuristixFile("guns", "log.txt");
    }

    @Override
    protected void init() {
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

    @Override
    public void addRenderer(Map map) {
        RenderBullet renderBullet = new RenderBullet();
        map.put(EntityBullet.class, renderBullet);
        map.put(EntityFlame.class, new RenderFlame());
        map.put(EntityGrenade.class, new RenderGrenade());
        map.put(EntityRocketGrenade.class, new RenderRocketGrenade());
        map.put(EntityIncendiaryBullet.class, renderBullet);
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

    private boolean initReflection(Minecraft mc) {
        try {
            ReflectionFacade names = ReflectionFacade.getInstance();
            ItemRenderer renderer = new GunItemRenderer(mc);
            names.setFieldValue(RenderManager.class, RenderManager.instance, "itemRenderer", renderer);
            names.setFieldValue(EntityRenderer.class, mc.entityRenderer, "itemRenderer", renderer);
            names.getField(EntityRenderer.class, "cameraZoom");
        } catch (NullPointerException e) {
            Log.throwing(getClass(), "initReflection(Minecraft mc)", e, getClass());
            return false;
        }
        return true;
    }

    @Override
    public boolean onTick(float tick, Minecraft minecraft) {
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
                                ModLoader.clientSendPacket(getShooterActionPacket(shooter.shiftedIndex, FIRE));
                                justAttemptedFire = true;
                            }
                        } else {
                            justAttemptedFire = false;
                        }
                        if (shooter.isBursting()) {
                            ModLoader.clientSendPacket(getShooterActionPacket(shooter.shiftedIndex, BURST));
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
    public void serverCustomPayload(NetServerHandler handler, Packet250CustomPayload packet) {
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(packet.data));
        try {
            EntityPlayer player = handler.playerEntity;
            switch (packet.channel) {
                case "TGMShootAction":
                    ItemProjectileShooter shooter = getEquippedShooter(player);
                    if (shooter != null && shooter.shiftedIndex == in.readInt()) {
                        switch (in.readInt()) {
                            case FIRE:
                                shooter.fire(player.worldObj, player);
                                break;
                            case BURST:
                                shooter.burst(player.worldObj, player);
                                break;
                            case RELOAD:
                                if (shooter instanceof ItemGun) {
                                    ((ItemGun) shooter).reload(player);
                                }
                                break;
                            case STOP_RELOADING:
                                if (shooter instanceof ItemGun) {
                                    ((ItemGun) shooter).stopReloading();
                                }
                                break;
                        }
                    }
                    break;
                case "TGMArrowClick":
                    if (player.openContainer instanceof ContainerCraftGuns) {
                        ContainerCraftGuns container = (ContainerCraftGuns) player.openContainer;
                        container.onArrowClick(in.readInt());
                    }
                    break;
            }
        } catch (IOException e) {
            Log.throwing(getClass(), "serverCustomPayload(NetServerHandler handler, Packet250CustomPayload packet)", e, getClass());
        }
    }

    @Override
    public void keyboardEvent(KeyBinding key) {
        Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.inGameHasFocus) {
            EntityPlayer player = mc.thePlayer;
            if (player != null) {
                ItemStack equippedStack = player.getCurrentEquippedItem();
                if (equippedStack != null) {
                    Item equipped = equippedStack.getItem();
                    if (equipped != null && equipped instanceof ItemGun) {
                        ItemGun equippedGun = (ItemGun) equipped;
                        if (key.equals(reloadKeybinding)) {
                            ModLoader.clientSendPacket(getShooterActionPacket(equippedGun.shiftedIndex, RELOAD));
                            isZoomed = false;
                        } else if (key.equals(zoomKeybinding)) {
                            if (equippedGun.getZoom() > 1.0f || equippedGun.getScope() > 0) {
                                isZoomed = !isZoomed;
                                if (equippedGun.isReloading()) {
                                    ModLoader.clientSendPacket(getShooterActionPacket(equippedGun.shiftedIndex, STOP_RELOADING));
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public GuiContainer getContainerGUI(EntityClientPlayerMP player, int type, int var3, int var4, int var5) {
        switch (type) {
            case ContainerCraftGuns.INVENTORY_TYPE:
                Minecraft mc = Minecraft.getMinecraft();
                return new GuiCraftGuns(new ContainerCraftGuns(mc.thePlayer, mc.playerController.isInCreativeMode()));
            default:
                break;
        }
        return null;
    }

    @Override
    public Packet23VehicleSpawn getSpawnPacket(Entity entity, int id) {
        if (id >= BASE_PROJECTILE_TRACKER_ID) {
            return new Packet23VehicleSpawn(entity, id);
        }
        return null;
    }

    @Override
    public Entity spawnEntity(int type, World world, double x, double y, double z) {
        Class<? extends EntityProjectile> entityProjectileClass = trackerIdToEntityProjectileClassMap.get(type);
        if (entityProjectileClass != null) {
            try {
                Constructor c = entityProjectileClass.getDeclaredConstructor(World.class, double.class, double.class, double.class);
                if (c != null) {
                    c.setAccessible(true);
                    return (Entity) c.newInstance(world, x, y, z);
                }
            } catch (Exception e) {
                Log.throwing(getClass(), "spawnEntity(int type, World world, double x, double y, double z)", e, getClass());
            }
        }
        return null;
    }

    private void zoom(Minecraft mc, ItemGun gun, boolean in) {
        float increment = 0.1f + (((gun != null) ? gun.getZoom() : 0) / 30f);
        currentZoom = (currentZoom + ((in) ? increment : -increment));
        currentZoom = (in) ? Math.min(gun.getZoom(), currentZoom) : Math.max(1, currentZoom);
        ReflectionFacade.getInstance().setFieldValue(EntityRenderer.class, mc.entityRenderer, "cameraZoom", (in && gun != null && gun.getZoom() > 1) ? Math.min(currentZoom, gun.getZoom()) : Math.max(currentZoom, 1));
        if (gun != null && gun.getScope() > 0 && (in || currentZoom != 1)) {
            Scope.values()[gun.getScope()].renderOverlay(gun, mc);
        }
    }

    private static void applyRecoil(EntityPlayer player, ItemGun gun) {
        double y = 0;
        double y1 = recoilY;
        if (recoilY > 0) {
            y = Math.min(Math.max(recoilY * 0.1f, 0.5f), recoilY);
            recoilY -= y;
            player.rotationPitch -= y;
        }
        if (Math.abs(recoilX) > 0) {
            double x;
            if (recoilX > 0) {
                x = Math.min(Math.max(recoilX * 0.1f / 2, 0.25f), recoilX);
            } else {
                x = Math.max(Math.min(recoilX * 0.1f / 2, -0.25f), recoilX);
            }
            if (y != 0) {
                double d3 = y / y1 * recoilX;
                if (recoilX > 0) {
                    x = Math.min(d3, x);
                } else {
                    x = Math.max(d3, x);
                }
            }
            recoilX -= x;
            player.rotationYaw += x;
        }
    }

    private static ItemProjectileShooter getEquippedShooter(EntityPlayer player) {
        ItemStack itemStack = player.getCurrentEquippedItem();
        if (itemStack != null) {
            Item item = itemStack.getItem();
            if (item != null && item instanceof ItemProjectileShooter) {
                return (ItemProjectileShooter) item;
            }
        }
        return null;
    }

    public static Packet250CustomPayload getShooterActionPacket(int shiftedIndex, int action) {
        try {
            return new Packet250CustomPayload("TGMShootAction", writeIntsToByteArray(shiftedIndex, action));
        } catch (IOException e) {
            Log.throwing(mod_Guns.class, "getShooterActionPacket(int shiftedIndex, int action)", e, mod_Guns.class);
        }
        return null;
    }

    public static Packet250CustomPayload getArrowClickPacket(int arrow) {
        try {
            return new Packet250CustomPayload("TGMArrowClick", writeIntsToByteArray(arrow));
        } catch (IOException e) {
            Log.throwing(mod_Guns.class, "getArrowClickPacket(int arrow)", e, mod_Guns.class);
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