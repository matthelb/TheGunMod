package net.minecraft.src;

import java.util.Random;

public class EntityAIEatGrass extends EntityAIBase
{
    private EntityLiving field_48397_b;
    private World field_48398_c;

    /** A decrementing tick used for the sheep's head offset and animation. */
    int eatGrassTick;

    public EntityAIEatGrass(EntityLiving par1EntityLiving)
    {
        eatGrassTick = 0;
        field_48397_b = par1EntityLiving;
        field_48398_c = par1EntityLiving.worldObj;
        setMutexBits(7);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (field_48397_b.getRNG().nextInt(field_48397_b.isChild() ? 50 : 1000) != 0)
        {
            return false;
        }

        int i = MathHelper.floor_double(field_48397_b.posX);
        int j = MathHelper.floor_double(field_48397_b.posY);
        int k = MathHelper.floor_double(field_48397_b.posZ);

        if (field_48398_c.getBlockId(i, j, k) == Block.tallGrass.blockID && field_48398_c.getBlockMetadata(i, j, k) == 1)
        {
            return true;
        }

        return field_48398_c.getBlockId(i, j - 1, k) == Block.grass.blockID;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        eatGrassTick = 40;
        field_48398_c.setEntityState(field_48397_b, (byte)10);
        field_48397_b.getNavigator().func_48672_f();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        eatGrassTick = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return eatGrassTick > 0;
    }

    public int func_48396_h()
    {
        return eatGrassTick;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        eatGrassTick = Math.max(0, eatGrassTick - 1);

        if (eatGrassTick != 4)
        {
            return;
        }

        int i = MathHelper.floor_double(field_48397_b.posX);
        int j = MathHelper.floor_double(field_48397_b.posY);
        int k = MathHelper.floor_double(field_48397_b.posZ);

        if (field_48398_c.getBlockId(i, j, k) == Block.tallGrass.blockID)
        {
            field_48398_c.playAuxSFX(2001, i, j, k, Block.tallGrass.blockID + 4096);
            field_48398_c.setBlockWithNotify(i, j, k, 0);
            field_48397_b.func_48095_u();
        }
        else if (field_48398_c.getBlockId(i, j - 1, k) == Block.grass.blockID)
        {
            field_48398_c.playAuxSFX(2001, i, j - 1, k, Block.grass.blockID);
            field_48398_c.setBlockWithNotify(i, j - 1, k, Block.dirt.blockID);
            field_48397_b.func_48095_u();
        }
    }
}
