package com.heuristix;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/22/11
 * Time: 8:42 PM
 */
public class EntityBullet extends EntityProjectileBase {

    public EntityBullet(World world) {
        super(world);
    }

    public EntityBullet(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    @Override
    public int getDamage() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getSpeed() {
        return 1024f;
    }

    @Override
    public float getEffectiveRange() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getSpread() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
