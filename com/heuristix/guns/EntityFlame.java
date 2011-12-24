package com.heuristix.guns;

import com.heuristix.EntityProjectile;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/24/11
 * Time: 12:19 PM
 */
public class EntityFlame extends EntityProjectile {

    public EntityFlame(World world) {
        super(world);
    }

    public EntityFlame(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    @Override
    public int getDamage() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public float getSpeed() {
        return 64;
    }

    @Override
    public int getMaxGroundTicks() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getHitSound() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getMoveSound() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getEffectiveRange() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public float getMass() {
        return 1;
    }

    @Override
    public float getSpread() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
