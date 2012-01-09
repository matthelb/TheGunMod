package net.minecraft.src;

import com.heuristix.*;
import com.heuristix.asm.Opcodes;
import com.heuristix.guns.EntityBullet;
import com.heuristix.guns.EntityFlame;
import com.heuristix.guns.RenderBullet;
import com.heuristix.guns.RenderFlame;
import com.heuristix.swing.GunCreator;
import com.heuristix.util.ExtensibleClassAdapter;
import com.heuristix.util.InvokeMethod;
import com.heuristix.util.Method;
import com.heuristix.util.Pair;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

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
        return "0.9.2" + " for " + CURRENT_VERSION;
    }

    @Override
    public void load() throws IOException, InvocationTargetException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        loadConfig(getConfig());
        initItems();
        registerSound("guns/hit.ogg", Util.read(Util.getFile("hit.ogg", Util.getHeuristixDir("sounds"))));
        registerSound("guns/move.ogg", Util.read(Util.getFile("move.ogg", Util.getHeuristixDir("sounds"))));
        ModLoader.RegisterKey(this, reloadKeybinding, false);
        ModLoader.RegisterKey(this, zoomKeybinding, false);
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

            }
        } else {
            System.out.println("Could not find minecraft directory. Are you using an obscure operating system?");
        }
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

    private static void renderRedDot(EntityPlayer player, World world) {
        Vec3D playerPos = player.getPosition(1);
        Vec3D projectedPos = Util.getProjectedPoint(playerPos, player.getLook(1), 1000);
        MovingObjectPosition rayTrace = world.rayTraceBlocks(playerPos, projectedPos);
        Vec3D vec = null;
        if (rayTrace != null)
            vec = rayTrace.hitVec;
        if (vec == null)
            vec = projectedPos;
        GL11.glPushMatrix();
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glColor4f(1.0f, 0.0f, 0.0f, 0.4f);
        Tessellator t = Tessellator.instance;
        t.setTranslationD(vec.xCoord, vec.yCoord, vec.zCoord);
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(-1, -1, 0, 0, 0);
        t.addVertexWithUV(1, -1, 0, 1, 0);
        t.addVertexWithUV(1, 1, 0, 1, 1);
        t.addVertexWithUV(-1, 1, 0, 0, 1);
        t.draw();
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        GL11.glPopMatrix();
    }

    private void zoom(Minecraft mc, ItemGun gun, EntityPlayer player, World world, boolean in) {
        float increment = 0.1f + (((gun != null) ? gun.getZoom(): 0) / 30f);
        currentZoom = (currentZoom + ((in) ?  increment : -increment));
        currentZoom = (in) ? Math.min(gun.getZoom(), currentZoom) : Math.max(1.0f, currentZoom);
        Util.setPrivateValue(EntityRenderer.class, mc.entityRenderer, "cameraZoom", obfuscatedFields.get(EntityRenderer.class).get("cameraZoom"), (in && gun != null && gun.getZoom() > 1.0f) ? Math.min(currentZoom, gun.getZoom()) : Math.max(currentZoom, 1.0f));
        if (gun != null && gun.getScope() > 0 && (in || currentZoom != 1.0f)) {
            Util.renderTexture(mc, Scope.values()[gun.getScope()].getTexturePath(), 1.0f);
        }
    }


}