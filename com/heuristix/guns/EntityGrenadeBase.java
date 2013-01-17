package com.heuristix.guns;

import com.heuristix.guns.EntityGrenade;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/30/11
 * Time: 11:49 AM
 */
public class EntityGrenadeBase extends EntityGrenade {

    public EntityGrenadeBase(World world) {
        super(world);
    }

    public EntityGrenadeBase(World world, EntityLiving owner) {
        super(world, owner);
    }

    public EntityGrenadeBase(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public int getDamage() {
        return 1;
    }

    @Override
    public float getSpread() {
        return 0;
    }

    @Override
    public float getEffectiveRange() {
        return 0;
    }
}
