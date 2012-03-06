package net.minecraft.src;

import java.util.Random;

public class EntityAILookAtVillager extends EntityAIBase
{
    private EntityIronGolem field_48226_a;
    private EntityVillager field_48224_b;
    private int field_48225_c;

    public EntityAILookAtVillager(EntityIronGolem par1EntityIronGolem)
    {
        field_48226_a = par1EntityIronGolem;
        func_46087_a(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!field_48226_a.worldObj.isDaytime())
        {
            return false;
        }

        if (field_48226_a.getRNG().nextInt(8000) != 0)
        {
            return false;
        }
        else
        {
            field_48224_b = (EntityVillager)field_48226_a.worldObj.func_48085_a(net.minecraft.src.EntityVillager.class, field_48226_a.boundingBox.expand(6D, 2D, 6D), field_48226_a);
            return field_48224_b != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return field_48225_c > 0;
    }

    public void func_46088_e()
    {
        field_48225_c = 400;
        field_48226_a.func_48383_a(true);
    }

    public void resetTask()
    {
        field_48226_a.func_48383_a(false);
        field_48224_b = null;
    }

    public void updateTask()
    {
        field_48226_a.getLookHelper().setLookPositionWithEntity(field_48224_b, 30F, 30F);
        field_48225_c--;
    }
}
