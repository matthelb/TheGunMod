package com.heuristix.guns;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import com.heuristix.EntityProjectile;

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
        return 0.01f;
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
        int x = position.blockX, y = position.blockY, z = position.blockZ;
        Block block = Block.blocksList[worldObj.getBlockId(position.blockX, position.blockY, position.blockZ)];
        if (block != null && block.blockMaterial.equals(Material.glass) || block.blockMaterial.equals(Material.ice)) {
            World world = worldObj;
            world.playAuxSFX(2001, x, y, z, block.blockID + world.getBlockMetadata(x, y, z) * 256);
            int i1 = world.getBlockMetadata(x, y, z);
            boolean notified = world.setBlockWithNotify(x, y, z, 0);
            if (block != null && notified) {
                block.onBlockDestroyedByPlayer(world, x, y, z, i1);
            }
            return true;
        }
        return false;
    }

}
