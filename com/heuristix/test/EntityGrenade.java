package com.heuristix.test;

import com.heuristix.EntityBulletBase;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 10/23/11
 * Time: 6:10 PM
 */
public class EntityGrenade extends EntityBulletBase {


    public EntityGrenade(World world, EntityLiving owner) {
        super(world, owner);
    }

    @Override
    public boolean onHit(Entity hit) {
        if(hit != null)
            return super.onHit(hit);
        else {
            worldObj.createExplosion(this, posX, posY, posZ, getDamage());
            return true;
        }
    }

    @Override
    public float getSpeed() {
        return 1.0f;
    }

    @Override
    public float getMass() {
        return 0.1f;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getDamage() {
        return 5;
    }


}
