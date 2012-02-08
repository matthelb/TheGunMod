package com.heuristix;

import com.heuristix.util.Log;
import com.heuristix.util.ReverseBuffer;
import net.minecraft.src.mod_Guns;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 2/1/12
 * Time: 6:24 PM
 */
public class DefaultGunBridge extends AbstractGunBridge {

    private String gunName, projectileName, shootSound, reloadSound;
    private ProjectileType projectileType;
    private FireMode fireMode;
    private Scope scope;

    private int damage, shotsPerMinute, reloadTime, clipSize, recoilX, recoilY, projectileId, gunId, roundsPerMinute, roundsPerShot;
    private float range, zoom, projectileSpread;

    private HashMap<Integer, BufferedImage> gunTextures;
    private HashMap<Integer, BufferedImage> projectileTextures;

    private byte[] shootSoundBytes, reloadSoundBytes;

    public String getGunName() {
        return gunName;
    }

    public void setGunName(String gunName) {
        this.gunName = gunName;
    }

    @Override
    protected void setShootSound(String shootSound) {
        this.shootSound = shootSound;
    }

    @Override
    protected void setReloadSound(String reloadSound) {
        this.reloadSound = reloadSound;
    }

    @Override
    protected void addProperty(String property, int[] value) {
        if(property.equals("itemGunId")) {
            setGunId(((value[0] & 0xFF) << 24) + ((value[1] & 0xFF) << 16) + ((value[2] & 0xFF) << 8) + (value[3] & 0xFF));
        } else if(property.equals("itemProjectileId") || property.equals("itemBulletId")) {
            setProjectileId(((value[0] & 0xFF) << 24) + ((value[1] & 0xFF) << 16) + ((value[2] & 0xFF) << 8) + (value[3] & 0xFF));
        }
    }

    @Override
    protected void addResource(String resource, byte[] value) {
        if(resource.contains("gun") || resource.contains("projectile")) {
            HashMap<Integer, BufferedImage> textures = (resource.contains("gun")) ? gunTextures : projectileTextures;
            try {
                BufferedImage image = ImageIO.read(new ByteArrayInputStream(value));
                if(resource.contains("16")) {
                    textures.put(16, image);
                } else if(resource.contains("32")) {
                    textures.put(32, image);
                } else if(resource.contains("64")) {
                    textures.put(64, image);
                } else if(resource.contains("128")) {
                    textures.put(128, image);
                } else if(resource.contains("256")) {
                    textures.put(256, image);
                } else if(resource.contains("512")) {
                    textures.put(512, image);
                }
            } catch (IOException e) {
                Log.throwing(getClass(), "addResource(String resource, byte[] value)", e, mod_Guns.class);
            }
        } else if(resource.contains("shootSound")) {
            shootSoundBytes = value;
        } else if(resource.contains("reloadSound")) {
            reloadSoundBytes = value;
        }
    }

    public String getProjectileName() {
        return projectileName;
    }

    public void setProjectileName(String projectileName) {
        this.projectileName = projectileName;
    }

    public ProjectileType getProjectileType() {
        return projectileType;
    }

    public void setProjectileType(ProjectileType projectileType) {
        this.projectileType = projectileType;
    }

    public FireMode getFireMode() {
        return fireMode;
    }

    public void setFireMode(FireMode fireMode) {
        this.fireMode = fireMode;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public int getDamage() {
        return damage;
    }

    @Override
    protected Map<String, byte[]> getResources() {
        HashMap<String, byte[]> resources = new HashMap<String, byte[]>();
        for(int i = 0; i < 2; i++) {
            HashMap<Integer, BufferedImage> textures = (i == 0) ? gunTextures : projectileTextures;
            for(Map.Entry<Integer, BufferedImage> entry : textures.entrySet()) {
                String key = entry.getKey() + "x" + entry.getKey() + ((i == 0) ? "gun" : "projectile");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    ImageIO.write(entry.getValue(), "PNG", out);
                    resources.put(key, out.toByteArray());
                } catch (IOException e) {
                    Log.throwing(getClass(), "getResources()", e, mod_Guns.class);
                }
            }
        }
        resources.put("shootSound", shootSoundBytes);
        resources.put("reloadSound", reloadSoundBytes);
        return resources;
    }

    @Override
    protected Map<String, int[]> getProperties() {
        HashMap<String, int[]> properties = new HashMap<String, int[]>();
        properties.put("itemGunId", ReverseBuffer.getInt(getGunId()));
        properties.put("itemProjectileId", ReverseBuffer.getInt(getProjectileId()));
        properties.put("versionCreated", ReverseBuffer.getString(VERSION));
        return properties;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getShotsPerMinute() {
        return shotsPerMinute;
    }

    public void setShotsPerMinute(int shotsPerMinute) {
        this.shotsPerMinute = shotsPerMinute;
    }

    public int getReloadTime() {
        return reloadTime;
    }

    public void setReloadTime(int reloadTime) {
        this.reloadTime = reloadTime;
    }

    public int getClipSize() {
        return clipSize;
    }

    public void setClipSize(int clipSize) {
        this.clipSize = clipSize;
    }

    public int getRecoilX() {
        return recoilX;
    }

    public void setRecoilX(int recoilX) {
        this.recoilX = recoilX;
    }

    public int getRecoilY() {
        return recoilY;
    }

    public void setRecoilY(int recoilY) {
        this.recoilY = recoilY;
    }

    public int getProjectileId() {
        return projectileId;
    }

    @Override
    protected String getShootSound() {
        return shootSound;
    }

    @Override
    protected String getReloadSound() {
        return reloadSound;
    }

    public void setProjectileId(int projectileId) {
        this.projectileId = projectileId;
    }

    public int getGunId() {
        return gunId;
    }

    public void setGunId(int gunId) {
        this.gunId = gunId;
    }

    public int getRoundsPerMinute() {
        return roundsPerMinute;
    }

    public void setRoundsPerMinute(int roundsPerMinute) {
        this.roundsPerMinute = roundsPerMinute;
    }

    public int getRoundsPerShot() {
        return roundsPerShot;
    }

    public void setRoundsPerShot(int roundsPerShot) {
        this.roundsPerShot = roundsPerShot;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public float getZoom() {
        return zoom;
    }

    public void setZoom(float zoom) {
        this.zoom = zoom;
    }

    public float getSpread() {
        return projectileSpread;
    }

    public void setProjectileSpread(float projectileSpread) {
        this.projectileSpread = projectileSpread;
    }
}
