package com.heuristix.guns;

import com.heuristix.EntityProjectile;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/24/11
 * Time: 12:19 PM
 */
public class EntityFlame extends EntityProjectile {

    public EntityFlame(World world) {
        super(world);
    }

    public EntityFlame(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    @Override
    public boolean onHit(Entity hit) {
        if(worldObj.getBlockId((int) posX, (int) posY, (int) posZ) == 0);
            worldObj.setBlockWithNotify((int) posX, (int) posY, (int) posZ, Block.fire.blockID);
        if(hit != null)
            return super.onHit(hit);
        return true;
    }

    @Override
    public int getDamage() {
        return 4;
    }

    public float getSpeed() {
        return 64;
    }

    @Override
    public int getMaxGroundTicks() {
        return 0;
    }

    @Override
    public String getHitSound() {
        return "guns.hit";
    }

    @Override
    public String getMoveSound() {
        return "guns.move";
    }

    @Override
    public float getEffectiveRange() {
        return 100;
    }

    public float getMass() {
        return 1;
    }

    @Override
    public float getSpread() {
        return 15;
    }
}
