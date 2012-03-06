package net.minecraft.src;

import java.util.Random;

public class EntityAIWatchClosest extends EntityAIBase
{
    private EntityLiving field_46110_a;
    private Entity field_48242_b;
    private float field_46106_d;
    private int field_46107_e;
    private float field_48241_e;
    private Class field_48240_f;

    public EntityAIWatchClosest(EntityLiving par1EntityLiving, Class par2Class, float par3)
    {
        field_46110_a = par1EntityLiving;
        field_48240_f = par2Class;
        field_46106_d = par3;
        field_48241_e = 0.02F;
        func_46087_a(2);
    }

    public EntityAIWatchClosest(EntityLiving par1EntityLiving, Class par2Class, float par3, float par4)
    {
        field_46110_a = par1EntityLiving;
        field_48240_f = par2Class;
        field_46106_d = par3;
        field_48241_e = par4;
        func_46087_a(2);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (field_46110_a.getRNG().nextFloat() >= field_48241_e)
        {
            return false;
        }

        if (field_48240_f == (net.minecraft.src.EntityPlayer.class))
        {
            field_48242_b = field_46110_a.worldObj.getClosestPlayerToEntity(field_46110_a, field_46106_d);
        }
        else
        {
            field_48242_b = field_46110_a.worldObj.func_48085_a(field_48240_f, field_46110_a.boundingBox.expand(field_46106_d, 3D, field_46106_d), field_46110_a);
        }

        return field_48242_b != null;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (!field_48242_b.isEntityAlive())
        {
            return false;
        }

        if (field_46110_a.getDistanceSqToEntity(field_48242_b) > (double)(field_46106_d * field_46106_d))
        {
            return false;
        }
        else
        {
            return field_46107_e > 0;
        }
    }

    public void func_46088_e()
    {
        field_46107_e = 40 + field_46110_a.getRNG().nextInt(40);
    }

    public void resetTask()
    {
        field_48242_b = null;
    }

    public void updateTask()
    {
        field_46110_a.getLookHelper().setLookPosition(field_48242_b.posX, field_48242_b.posY + (double)field_48242_b.getEyeHeight(), field_48242_b.posZ, 10F, field_46110_a.getVerticalFaceSpeed());
        field_46107_e--;
    }
}
