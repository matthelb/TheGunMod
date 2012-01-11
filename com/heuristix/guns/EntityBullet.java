package com.heuristix.guns;

import com.heuristix.EntityProjectile;
import com.heuristix.Util;
import net.minecraft.src.*;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/22/11
 * Time: 8:42 PM
 */
public class EntityBullet extends EntityProjectile {

    public EntityBullet(World world) {
        super(world);
    }

    public EntityBullet(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    public EntityBullet(World world, double x, double y, double z) {
        super(world, x, y, z);
    }

    @Override
    public int getDamage() {
        return 0;
    }

    @Override
    public float getSpeed() {
        return 1024f;
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
        return 0;
    }

    @Override
    public float getMass() {
        return 0;
    }

    @Override
    public float getSpread() {
        return 0;
    }

    public boolean onBlockHit(MovingObjectPosition position) {
        if(getOwner() instanceof EntityPlayerSP) {
            PlayerController controller = Util.getPlayerController((EntityPlayerSP) getOwner());
            if(controller != null) {
                Block block = Block.blocksList[worldObj.getBlockId(position.blockX, position.blockY, position.blockZ)];
                if(block.blockMaterial.equals(Material.glass)) {
                    controller.onPlayerDestroyBlock(position.blockX, position.blockY, position.blockZ, block.blockID);
                }
                return true;
            }
        }
        return false;
    }

}
