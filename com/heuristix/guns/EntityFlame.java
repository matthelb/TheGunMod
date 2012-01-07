package com.heuristix.guns;

import com.heuristix.EntityProjectile;
import com.heuristix.Util;
import net.minecraft.src.*;

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

    /*@Override
    public void onUpdate() {
        super.onUpdate();
        if(isEntityAlive())
            worldObj.spawnParticle("flame", posX + rand.nextFloat(), posY + rand.nextFloat(), posZ + rand.nextFloat(), 0, 0, 0);
    }*/

    @Override
    public boolean onBlockHit(MovingObjectPosition position) {
        int x = position.blockX, y = position.blockY, z = position.blockZ;
        switch (position.sideHit) {
            case 0:
                y--;
                break;
            case 1:
                y++;
                break;
            case 2:
                z--;
                break;
            case 3:
                z++;
                break;
            case 4:
                x--;
                break;
            case 5:
                x++;
                break;
            default:
                break;
        }
        if (worldObj.getBlockId(x, y, z) == 0) {
            worldObj.playSoundEffect(posX, posY, posZ, "fire.ignite", 1.0f, Util.nextFloat() * 0.25f + 0.8f);
            worldObj.setBlockWithNotify(x, y, z, Block.fire.blockID);
            return true;
        }
        return false;
    }

    public float getSpeed() {
        return 1;
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

    public float getMass() {
        return 0.1f;
    }

    @Override
    public float getEffectiveRange() {
        return 0;
    }

    @Override
    public float getSpread() {
        return 0;
    }

    @Override
    public int getDamage() {
        return 0;
    }
}
