package com.heuristix.guns;

import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/21/11
 * Time: 6:31 PM
 */
public class EntityRocketGrenade extends EntityGrenade {

    public static final float MAX_SPEED = 5.0f;
    private final boolean constructed;

    private float speed;

    public EntityRocketGrenade(World world) {
        super(world);
        this.constructed = true;
    }

    public EntityRocketGrenade(World world, EntityLiving living) {
        super(world, living);
        this.constructed = true;
    }

    public EntityRocketGrenade(World world, double x, double y, double z) {
        super(world, x, y, z);
        this.constructed = true;
    }

    @Override
    public float getMass() {
        return 0;
    }

    @Override
    public float getSpeed() {
        if(!constructed) {
            return (speed = super.getSpeed());
        }
        return speed;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if(speed < MAX_SPEED) {
            if(speed == 1.0f) {
                speed += 0.2f;
            }
            float newSpeed = (float) Math.min(Math.pow(speed, 1.1f), MAX_SPEED);
            changeVelocity(newSpeed / getSpeed());
            this.speed = newSpeed;
        }
    }

    @Override
    public boolean onEntityHit(Entity hit) {
        if(!worldObj.isRemote) {
            worldObj.createExplosion(this, posX, posY, posZ, getDamage());
        }
        return true;
    }
}
