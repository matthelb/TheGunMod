package net.minecraft.src;

import com.heuristix.*;
import com.heuristix.asm.Opcodes;
import com.heuristix.guns.EntityBullet;
import com.heuristix.guns.EntityFlame;
import com.heuristix.guns.RenderBullet;
import com.heuristix.guns.RenderFlame;
import com.heuristix.swing.GunCreator;
import com.heuristix.util.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import javax.imageio.ImageIO;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
* Created by IntelliJ IDEA.
* User: Matt
* Date: 9/1/11
* Time: 9:47 AM
*/
public class mod_Guns extends Mod {

    public static final int MOUSE_LEFT = 0;
    public static final int MOUSE_RIGHT = 1;

    private static final Properties DEFAULT_CONFIG = new Properties();

    private KeyBinding reloadKeybinding = new KeyBinding("key.reload", Keyboard.KEY_R);
    private KeyBinding zoomKeybinding = new KeyBinding("key.zoom", Keyboard.KEY_Z);

    public static boolean DEBUG = true;

    private boolean justAttemptedFire;
    private boolean reflectionInit;

    private boolean isZoomed;
    private float currentZoom;

    public static int recoilY, recoilX;

    private static final Map<Class, Map<String, String>> obfuscatedFields = new HashMap<Class, Map<String, String>>();

    static {
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("itemRenderer", "f");
        obfuscatedFields.put(RenderManager.class, (Map<String, String>) values.clone());
        values.put("itemRenderer", "c");
        values.put("cameraZoom", "V");
        obfuscatedFields.put(EntityRenderer.class, (Map<String, String>) values.clone());
        values.clear();
        values.put("prevEquippedProgress", "d");
        values.put("equippedProgress", "c");
        values.put("mc", "a");
        values.put("itemToRender", "b");
        values.put("mapItemRenderer", "f");
        obfuscatedFields.put(ItemRenderer.class, (Map<String, String>) values.clone());

        DEFAULT_CONFIG.setProperty("key.reload", Keyboard.getKeyName(Keyboard.KEY_R));
        DEFAULT_CONFIG.setProperty("key.zoom", Keyboard.getKeyName(Keyboard.KEY_Z));
    }

    private static final Map<String, Class> classes = new HashMap<String, Class>();
    private static final Map<String, ItemProjectile> projectiles = new HashMap<String, ItemProjectile>();

    public mod_Guns() {
    }

    @Override
    public String getVersion() {
        return "0.9.3" + " for " + CURRENT_VERSION;
    }

    @Override
    public void load() {
        try {
        loadConfig(getConfig());
        } catch (IOException e) {
            System.out.println("Failed to load config file");
        }
        try {
            initItems();
        } catch (Exception e) {
            System.out.println("Failed to initialize items");
        }
        registerSound("guns/hit.ogg", Util.read(Util.getFile("hit.ogg", Util.getHeuristixDir("sounds"))));
        registerSound("guns/move.ogg", Util.read(Util.getFile("move.ogg", Util.getHeuristixDir("sounds"))));
        ModLoader.RegisterKey(this, reloadKeybinding, false);
        ModLoader.RegisterKey(this, zoomKeybinding, false);
        ModLoader.AddLocalization("key.reload", "Reload");
        ModLoader.AddLocalization("key.zoom", "Zoom");
        ModLoader.SetInGameHook(this, true, false);
    }

    private Properties getConfig() throws IOException {
        File configFile = Util.getMinecraftDir("heuristix/config.ini");
        Properties config = DEFAULT_CONFIG;
        if(configFile.exists()) {
            config.load(new FileInputStream(configFile));
        } else {
            config.store(new FileOutputStream(configFile), "TheGunMod v" + getVersion() + " Configuration");
        }
        return config;
    }

    private void loadConfig(Properties config) {
        reloadKeybinding = new KeyBinding("key.reload", Keyboard.getKeyIndex(config.getProperty("key.reload")));
        zoomKeybinding = new KeyBinding("key.zoom", Keyboard.getKeyIndex(config.getProperty("key.zoom")));
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
                    GunCreator gc = null;
                    if(gun != null) {
                        int[] versionCreated = gun.getProperties().get("versionCreated");
                        if(versionCreated == null || !Util.getStringFromBytes(versionCreated).equals(GunCreator.VERSION)) {
                            if(gc == null)
                                gc = new GunCreator();
                            gc.load(gun);
                            gc.write(new FileOutputStream(f));
                            gun = new Gun(Util.read(f));
                        }
                        registerGun(gun);
                    }
                }

            }
        } else {
            System.out.println("Could not find minecraft directory. Are you using an obscure operating system?");
        }
    }

    public void registerGun(Gun gun) throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        List<Pair<String, byte[]>> gunClasses = gun.getClasses();
        List<byte[]> resources = gun.getResources();

        Class entityBulletClass = classes.get(gunClasses.get(0).getFirst());
        if (entityBulletClass == null) {
            byte[] entityBulletClassBytes = gunClasses.get(0).getSecond();
            if (DEBUG) {
                byte[] moddedBytes = ExtensibleClassAdapter.modifyClassBytes(entityBulletClassBytes, gunClasses.get(0).getFirst() + "CheckSuper", new HashMap<String, Method>(), false);
                Class projectileType = Util.defineClass(moddedBytes, null, EntityProjectile.class.getClassLoader()).getSuperclass();
                HashMap<String, Method> methods = new HashMap<String, Method>();
                for (int i = 0; i < GunCreator.OBFUSCATED_CLASS_NAMES.size(); i++) {
                    Pair<String, String> obfuscatedNames = GunCreator.OBFUSCATED_CLASS_NAMES.get(i);
                    methods.put("<init>(L" + obfuscatedNames.getFirst() + ";L" + obfuscatedNames.getSecond() + ";)V", new Method("(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityLiving;)V",
                            new InvokeMethod(GunCreator.SUPER_WORLD_ENTITY, new int[]{Opcodes.RETURN}, projectileType.getCanonicalName().replace('.', '/'), "<init>", "(Lnet/minecraft/src/World;Lnet/minecraft/src/EntityLiving;)V", false, true, false)));
                    methods.put("<init>(L" + obfuscatedNames.getFirst() + ";)V", new Method("(Lnet/minecraft/src/World;)V",
                            new InvokeMethod(GunCreator.SUPER_WORLD, new int[]{Opcodes.RETURN}, projectileType.getCanonicalName().replace('.', '/'), "<init>", "(Lnet/minecraft/src/World;)V", false, true, false)));
                }
                entityBulletClassBytes = ExtensibleClassAdapter.modifyClassBytes(entityBulletClassBytes, gunClasses.get(0).getFirst(), methods, false);
            }
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
        if (itemBullet != null) {
            itemBullet.setIconIndex(registerTexture(Util.resize(ImageIO.read(new ByteArrayInputStream(resources.get(0))), 16, 16), true));
            registerItem(itemBullet);
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
        if (itemGun != null) {
            itemGun.setIconIndex(registerTexture(Util.resize(ImageIO.read(new ByteArrayInputStream(resources.get(1))), 16, 16), true));
            registerItem(itemGun);
            registerSound(itemGun.getShootSound().replaceFirst("\\.", "/") + ".ogg", resources.get(2));
            if (resources.size() > 3) {
                registerStreaming(itemGun.getReloadSound().replaceFirst("\\.", "/") + ".ogg", resources.get(3));
            }
        }
        itemBullet.putProjectileClass(itemGun, entityBulletClass);
        projectiles.put(gunClasses.get(1).getFirst(), itemBullet);
    }

    private boolean initReflection(Minecraft mc) {
        try {
            Util.setPrivateValue(RenderManager.class, RenderManager.instance, "itemRenderer", obfuscatedFields.get(RenderManager.class).get("itemRenderer"), new GunItemRenderer(mc, obfuscatedFields.get(ItemRenderer.class)));
            Util.setPrivateValue(EntityRenderer.class, mc.entityRenderer, "itemRenderer", obfuscatedFields.get(EntityRenderer.class).get("itemRenderer"), new GunItemRenderer(mc, obfuscatedFields.get(ItemRenderer.class)));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void KeyboardEvent(KeyBinding key) {
        Minecraft mc = ModLoader.getMinecraftInstance();
        if (mc != null) {
            if (mc.inGameHasFocus) {
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
    }

    @Override
    public boolean OnTick(float tick, Minecraft minecraft) {
        if (!reflectionInit)
            reflectionInit = initReflection(minecraft);

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
                        } else
                            justAttemptedFire = false;
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
                    zoom(minecraft, (ItemGun) item, minecraft.thePlayer, minecraft.theWorld, in);
                else {
                    currentZoom = 1.0f;
                    isZoomed = false;
                    zoom(minecraft, null, minecraft.thePlayer, minecraft.theWorld, in);
                }
            }
        }
        return true;
    }

    @Override
    public void AddRenderer(Map map) {
        map.put(EntityBullet.class, new RenderBullet());
        map.put(EntityFlame.class, new RenderFlame());
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

    private void zoom(Minecraft mc, ItemGun gun, EntityPlayer player, World world, boolean in) {
        float increment = 0.1f + (((gun != null) ? gun.getZoom(): 0) / 30f);
        currentZoom = (currentZoom + ((in) ? increment : -increment));
        currentZoom = (in) ? Math.min(gun.getZoom(), currentZoom) : Math.max(1.0f, currentZoom);
        Util.setPrivateValue(EntityRenderer.class, mc.entityRenderer, "cameraZoom", obfuscatedFields.get(EntityRenderer.class).get("cameraZoom"), (in && gun != null && gun.getZoom() > 1.0f) ? Math.min(currentZoom, gun.getZoom()) : Math.max(currentZoom, 1.0f));
        if (gun != null && gun.getScope() > 0 && (in || currentZoom != 1.0f)) {
            Util.renderTexture(mc, Scope.values()[gun.getScope()].getTexturePath(), 1.0f);
        }
    }

}