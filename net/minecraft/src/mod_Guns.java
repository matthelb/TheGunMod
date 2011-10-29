package net.minecraft.src;

import com.heuristix.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/1/11
 * Time: 9:47 AM
 */
public class mod_Guns extends Mod {

    public static final int MOUSE_LEFT = 0;
    public static final int MOUSE_RIGHT = 1;

    private static final KeyBinding RELOAD_KEYBINDING = new KeyBinding("key.reload", Keyboard.KEY_R);
    private static final KeyBinding ZOOM_KEYBINDING = new KeyBinding("key.zoom", Keyboard.KEY_Z);

    private boolean justAttemptedFire;
    private boolean reflectionInit;

    private boolean isZoomed;

    public static int recoilY, recoilX;

    private static final Map<Class, Map<String, String>> obfuscatedFields = new HashMap<Class, Map<String, String>>();
    static {
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("itemRenderer", "f");
        obfuscatedFields.put(RenderManager.class, (Map<String, String>) values.clone());
        values.put("itemRenderer", "c");
        values.put("cameraZoom", "Q");
        obfuscatedFields.put(EntityRenderer.class, (Map<String, String>) values.clone());
        values.clear();
        values.put("prevEquippedProgress", "d");
        values.put("equippedProgress", "c");
        values.put("mc", "a");
        values.put("itemToRender", "b");
        values.put("mapItemRenderer", "f");
        obfuscatedFields.put(ItemRenderer.class, (Map<String, String>) values.clone());
    }

    public mod_Guns() {
        initItems();
        registerSound("guns/hit.ogg", Utilities.read(Utilities.getFile("hit.ogg", Utilities.getHeuristixDir("sounds"))));
        registerSound("guns/move.ogg", Utilities.read(Utilities.getFile("move.ogg", Utilities.getHeuristixDir("sounds"))));
        ModLoader.RegisterKey(this, RELOAD_KEYBINDING, false);
        ModLoader.RegisterKey(this, ZOOM_KEYBINDING, false);
        ModLoader.SetInGameHook(this, true, false);
    }

    private void initItems() {
        File gunsDir = Utilities.getHeuristixDir("guns");
        if(gunsDir != null) {
            if(!gunsDir.exists()) {
                gunsDir.mkdirs();
            } else {
                for(File f : gunsDir.listFiles(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.toLowerCase().endsWith(".gun");
                    }
                } )) {
                    CustomGun gun = new CustomGun(Utilities.read(f));
                    ItemGun itemGun = gun.getItemGun();
                    BufferedImage gunTexture = Utilities.resize(gun.getGunTexture(), 16, 16);
                    itemGun.setIconIndex(registerTexture(gunTexture, true));
                    registerItem(itemGun);
                    registerSound(itemGun.getShootSound().replaceFirst("\\.", "/") + ".ogg", gun.getShootSoundBytes());
                    ItemProjectile itemBullet = gun.getItemBullet();
                    BufferedImage bulletTexture = Utilities.resize(gun.getBulletTexture(), 16, 16);
                    itemBullet.setIconIndex(registerTexture(bulletTexture, true));
                    registerItem(itemBullet);
                }
            }
        } else {
            System.out.println("Could not find minecraft directory. Are you using an obscure operating system?");
        }
    }

    private boolean initReflection(Minecraft mc) {
        try {
            setPrivateValue(RenderManager.class, RenderManager.instance, "itemRenderer", obfuscatedFields.get(RenderManager.class).get("itemRenderer"), new GunItemRenderer(mc, obfuscatedFields.get(ItemRenderer.class)));
            setPrivateValue(EntityRenderer.class, mc.entityRenderer, "itemRenderer", obfuscatedFields.get(EntityRenderer.class).get("itemRenderer"), new GunItemRenderer(mc, obfuscatedFields.get(ItemRenderer.class)));
        } catch(NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public String Version() {
        return getVersion() + " for " + CURRENT_VERSION;
    }

    @Override
    public String getVersion() {
        return "0.4.2";
    }

    public void KeyboardEvent(KeyBinding key) {
        Minecraft mc = ModLoader.getMinecraftInstance();
        if(mc != null) {
            EntityPlayer player = mc.thePlayer;
            if(player != null) {
                ItemStack equippedStack = player.getCurrentEquippedItem();
                if(equippedStack != null) {
                    Item equipped = equippedStack.getItem();
                    if(equipped != null && equipped instanceof ItemGun) {
                        ItemGun equippedGun = (ItemGun) equipped;
                        if(key.equals(RELOAD_KEYBINDING)) {
                            equippedGun.reload(player);
                            isZoomed = false;
                        } else if(key.equals(ZOOM_KEYBINDING)) {
                            if(equippedGun.getZoom() > 1.0f || equippedGun.getScope() > 0) {
                                isZoomed = !isZoomed;
                                if(equippedGun.isReloading())
                                    equippedGun.stopReloading();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean OnTick(float tick, Minecraft minecraft) {
        if(!reflectionInit)
            reflectionInit = initReflection(minecraft);

        if(minecraft.inGameHasFocus) {
            ItemStack equippedStack = minecraft.thePlayer.getCurrentEquippedItem();
            if(equippedStack != null) {
                Item equipped = equippedStack.getItem();
                if(equipped != null) {
                    if(equipped instanceof ItemProjectileShooter) {
                        ItemProjectileShooter shooter = (ItemProjectileShooter) equipped;
                        if(Mouse.isButtonDown(MOUSE_RIGHT)) {
                            if((shooter.getFireMode() == ItemProjectileShooter.FIRE_MODE_AUTO || !justAttemptedFire) && !shooter.isBursting()) {
                                shooter.fire(minecraft.theWorld, minecraft.thePlayer);
                                justAttemptedFire = true;
                            }
                        } else
                            justAttemptedFire = false;
                        if(shooter.isBursting()) {
                            shooter.burst(minecraft.theWorld, minecraft.thePlayer);
                        }
                        if(equipped instanceof ItemGun) {
                            applyRecoil(minecraft.thePlayer, (ItemGun) equipped);
                        }
                    }
                }
            }
            boolean in =  (equippedStack != null) && (equippedStack.getItem() != null) && (equippedStack.getItem() instanceof ItemGun) && isZoomed;
            zoom(minecraft, (in) ? (ItemGun) equippedStack.getItem():null, in);
        }
        return true;
    }

    @Override
    public void AddRenderer(Map map) {
        map.put(EntityBullet.class, new RenderBullet());
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
            }
            else {
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

    private static void zoom(Minecraft mc, ItemGun gun, boolean in) {
        setPrivateValue(EntityRenderer.class, mc.entityRenderer, "cameraZoom", obfuscatedFields.get(EntityRenderer.class).get("cameraZoom"), (in && gun != null && gun.getZoom() > 1.0f) ? gun.getZoom() : 1.0f);
        if(gun != null && gun.getScope() > 0 && in) {
            Utilities.renderTexture(mc, Scope.values()[gun.getScope()].getTexturePath(), 1.0f);
        }
    }


}
