package com.heuristix.guns;

import com.heuristix.EntityProjectile;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/22/11
 * Time: 8:42 PM
 */
public class EntityBullet extends EntityProjectile {

    public EntityBullet(World world) {
        super(world);
    }

    public EntityBullet(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    @Override
    public int getDamage() {
        return 0;
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
        return null;
    }

    @Override
    public String getMoveSound() {
        return null;
    }

    @Override
    public float getEffectiveRange() {
        return 0;
    }

    @Override
    public float getMass() {
        return 0;
    }

    @Override
    public float getSpread() {
        return 0;
    }

}
