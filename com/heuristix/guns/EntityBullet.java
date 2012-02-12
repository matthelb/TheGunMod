package com.heuristix.guns;

import com.heuristix.EntityProjectile;
import com.heuristix.Util;
import net.minecraft.client.Minecraft;
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
        return 128f;
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
            int x = position.blockX, y = position.blockY, z = position.blockZ;
            Minecraft mc = Util.getMinecraft((EntityPlayerSP) getOwner());
            if(mc != null) {
                Block block = Block.blocksList[worldObj.getBlockId(position.blockX, position.blockY, position.blockZ)];
                if(block != null && block.blockMaterial.equals(Material.glass)) {
                    World world = worldObj;
                    world.playAuxSFX(2001, x, y, z, block.blockID + world.getBlockMetadata(x, y, z) * 256);
                    if(!world.isRemote) {
                        int i1 = world.getBlockMetadata(x, y, z);
                        boolean notified = world.setBlockWithNotify(x, y, z, 0);
                        if (block != null && notified) {
                            block.onBlockDestroyedByPlayer(world, x, y, z, i1);
                        }
                        return true;
                    }
                }

            }
        }
        return false;
    }

}
