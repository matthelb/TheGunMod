package com.heuristix.guns;

import com.heuristix.guns.util.Log;
import com.heuristix.guns.util.ReverseBuffer;
import net.minecraft.src.mod_Guns;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
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

    private int damage, shotsPerMinute, reloadTime, clipSize, recoilX, recoilY, projectileId, gunId, roundsPerMinute, roundsPerShot, reloadParts;
    private float range, zoom, projectileSpread;

    private Map<String, byte[]> resources;


    public DefaultGunBridge() {
        this.resources = new HashMap<String, byte[]>();
    }

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
        resources.put(resource, value);
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

    public int getReloadParts() {
        return reloadParts;
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

    public void setReloadParts(int reloadParts) {
        this.reloadParts = reloadParts;
    }
}
