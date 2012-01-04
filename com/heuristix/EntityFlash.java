package com.heuristix;

import net.minecraft.src.*;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/16/11
 * Time: 12:11 PM
 */
public class EntityFlash extends Entity {

    private float dieRate;
    private float brightness;
    private int radius;

    private int[][][] savedValues;

    public EntityFlash(World world, Vec3D position, int brightness, int radius) {
        this(world, position.xCoord, position.yCoord, position.zCoord, brightness, radius);
    }

    public EntityFlash(World world, double x, double y, double z, int brightness, int radius) {
        this(world, x, y, z, brightness, radius, 1);
    }

    public EntityFlash(World world, Vec3D position, int brightness, int radius, int dieRate) {
        this(world, position.xCoord, position.yCoord, position.zCoord, brightness, radius, dieRate);
    }

    public EntityFlash(World world, double x, double y, double z, float brightness, int radius, float dieRate) {
        super(world);
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.brightness = brightness;
        this.radius = radius;
        this.dieRate = dieRate;
        updateLighting();
    }

    @Override
    protected void entityInit() {
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        brightness -= dieRate;
        if (brightness <= 0) {
            brightness = 0;
            setEntityDead();
        }
        updateLighting();
    }

    private void updateLighting() {
        boolean first = (savedValues == null);
        if (first) {
            savedValues = new int[2 * radius + 1][2 * radius + 1][2 * radius + 1];
        }
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -radius; y <= radius; y++) {
                    int posXInt = (int) Math.floor(posX), posYInt = (int) Math.floor(posY), posZInt = (int) Math.floor(posZ);
                    int brightness = (int) (this.brightness / Math.max(Math.floor(Util.distance(posX, posX + x, posY, posY + y, posZ, posZ + z)), 1));
                    if (first && ((savedValues[x + radius][y + radius][z + radius] = worldObj.getBlockLightValue(posXInt + x, posYInt + y, posZInt + z)) >= brightness)) {
                        continue;
                    }
                    if (brightness == 0) {
                        brightness = savedValues[x + radius][y + radius][z + radius];
                    }
                    worldObj.setLightValue(EnumSkyBlock.Sky, posXInt + x, posYInt + y, posZInt + z, brightness);
                }
            }
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound nbttagcompound) {
        dieRate = nbttagcompound.getFloat("dieRate");
        brightness = nbttagcompound.getFloat("brightness");
        radius = nbttagcompound.getInteger("radius");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setFloat("dieRate", dieRate);
        nbttagcompound.setFloat("brightness", brightness);
        nbttagcompound.setInteger("radius", radius);
    }
}
