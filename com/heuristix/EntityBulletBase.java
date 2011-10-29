package com.heuristix;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/29/11
 * Time: 7:40 PM
 */
public class EntityBulletBase extends EntityBullet {

    public EntityBulletBase(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public float getEffectiveRange() {
        return 0;
    }

    @Override
    public float getSpread() {
        return 0;
    }

    public float getSpeed() {
        return super.getSpeed();
    }

    public float getMass() {
        return super.getMass();
    }

    public boolean onHit(Entity hit) {
        return super.onHit(hit);
    }
}
