package com.heuristix.guns;

import com.heuristix.guns.EntityRocketGrenade;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/30/11
 * Time: 11:50 AM
 */
public class EntityRocketGrenadeBase extends EntityRocketGrenade {

    public EntityRocketGrenadeBase(World world) {
        super(world);
    }

    public EntityRocketGrenadeBase(World world, EntityLiving living) {
        super(world, living);
    }

    public EntityRocketGrenadeBase(World world, double x, double y, double z) {
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
