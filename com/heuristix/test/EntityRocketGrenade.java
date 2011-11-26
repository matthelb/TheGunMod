package com.heuristix.test;

import com.heuristix.EntityBulletBase;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/21/11
 * Time: 6:31 PM
 */
public class EntityRocketGrenade extends EntityGrenade {

    protected EntityRocketGrenade(World world, EntityLiving living) {
        super(world, living);
    }

    @Override
    public float getMass() {
        return 0;
    }

}
