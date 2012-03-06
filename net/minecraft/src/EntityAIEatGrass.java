package net.minecraft.src;

import java.util.Random;

public class EntityAIEatGrass extends EntityAIBase
{
    private EntityLiving field_48228_b;
    private World field_48229_c;
    int field_48230_a;

    public EntityAIEatGrass(EntityLiving par1EntityLiving)
    {
        field_48230_a = 0;
        field_48228_b = par1EntityLiving;
        field_48229_c = par1EntityLiving.worldObj;
        func_46087_a(7);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (field_48228_b.getRNG().nextInt(field_48228_b.isChild() ? 50 : 1000) != 0)
        {
            return false;
        }

        int i = MathHelper.floor_double(field_48228_b.posX);
        int j = MathHelper.floor_double(field_48228_b.posY);
        int k = MathHelper.floor_double(field_48228_b.posZ);

        if (field_48229_c.getBlockId(i, j, k) == Block.tallGrass.blockID && field_48229_c.getBlockMetadata(i, j, k) == 1)
        {
            return true;
        }

        return field_48229_c.getBlockId(i, j - 1, k) == Block.grass.blockID;
    }

    public void func_46088_e()
    {
        field_48230_a = 40;
        field_48229_c.setEntityState(field_48228_b, (byte)10);
        field_48228_b.func_48333_ak().func_48662_f();
    }

    public void resetTask()
    {
        field_48230_a = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return field_48230_a > 0;
    }

    public int func_48227_f()
    {
        return field_48230_a;
    }

    public void updateTask()
    {
        field_48230_a = Math.max(0, field_48230_a - 1);

        if (field_48230_a != 4)
        {
            return;
        }

        int i = MathHelper.floor_double(field_48228_b.posX);
        int j = MathHelper.floor_double(field_48228_b.posY);
        int k = MathHelper.floor_double(field_48228_b.posZ);

        if (field_48229_c.getBlockId(i, j, k) == Block.tallGrass.blockID)
        {
            field_48229_c.playAuxSFX(2001, i, j, k, Block.tallGrass.blockID + 4096);
            field_48229_c.setBlockWithNotify(i, j, k, 0);
            field_48228_b.func_48319_z();
        }
        else if (field_48229_c.getBlockId(i, j - 1, k) == Block.grass.blockID)
        {
            field_48229_c.playAuxSFX(2001, i, j - 1, k, Block.grass.blockID);
            field_48229_c.setBlockWithNotify(i, j - 1, k, Block.dirt.blockID);
            field_48228_b.func_48319_z();
        }
    }
}
