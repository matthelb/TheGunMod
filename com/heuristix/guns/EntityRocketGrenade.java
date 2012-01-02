package com.heuristix.guns;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/21/11
 * Time: 6:31 PM
 */
public class EntityRocketGrenade extends EntityGrenade {

    public EntityRocketGrenade(World world) {
        super(world);
    }

    public EntityRocketGrenade(World world, EntityLiving living) {
        super(world, living);
    }

    @Override
    public float getMass() {
        return 0;
    }

}
