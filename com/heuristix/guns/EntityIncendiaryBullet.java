package com.heuristix.guns;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 7/27/12
 * Time: 2:08 AM
 */
public class EntityIncendiaryBullet extends EntityBullet {

    public EntityIncendiaryBullet(World world) {
        super(world);
    }

    public EntityIncendiaryBullet(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    public EntityIncendiaryBullet(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public boolean onEntityHit(Entity hit) {
        if(super.onEntityHit(hit)) {
            hit.setFire(getDamage());
            return true;
        }
        return false;
    }
}
