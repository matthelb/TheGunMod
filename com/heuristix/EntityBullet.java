package com.heuristix;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/1/11
 * Time: 11:45 PM
 */
public abstract class EntityBullet extends EntityProjectile {

    public EntityBullet(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    @Override
    public float getSpeed() {
        return 1024f;
    }

    @Override
    public int getMaxGroundTicks() {
        return 0;
    }

    @Override
    public String getHitSound() {
        return "guns.hit";
    }

    @Override
    public String getMoveSound() {
        return "guns.move";
    }

    @Override
    public float getMass() {
        return 0;
    }

}
