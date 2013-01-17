package com.heuristix.guns;

import net.minecraft.entity.EntityLiving;
import net.minecraft.world.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 7/27/12
 * Time: 2:09 AM
 */
public class EntityIncendiaryBulletBase extends EntityIncendiaryBullet {

    public EntityIncendiaryBulletBase(World world) {
        super(world);
    }

    public EntityIncendiaryBulletBase(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    public EntityIncendiaryBulletBase(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public float getSpread() {
        return 0;    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int getDamage() {
        return 1;
    }

    @Override
    public float getEffectiveRange() {
        return 0;
    }
}
