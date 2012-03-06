package net.minecraft.src;

import java.util.Random;

public class EntityAILookIdle extends EntityAIBase
{
    private EntityLiving field_46114_a;
    private double field_46112_b;
    private double field_46113_c;
    private int field_46111_d;

    public EntityAILookIdle(EntityLiving par1EntityLiving)
    {
        field_46111_d = 0;
        field_46114_a = par1EntityLiving;
        func_46087_a(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return field_46114_a.getRNG().nextFloat() < 0.02F;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return field_46111_d >= 0;
    }

    public void func_46088_e()
    {
        double d = (Math.PI * 2D) * field_46114_a.getRNG().nextDouble();
        field_46112_b = Math.cos(d);
        field_46113_c = Math.sin(d);
        field_46111_d = 20 + field_46114_a.getRNG().nextInt(20);
    }

    public void updateTask()
    {
        field_46111_d--;
        field_46114_a.getLookHelper().setLookPosition(field_46114_a.posX + field_46112_b, field_46114_a.posY + (double)field_46114_a.getEyeHeight(), field_46114_a.posZ + field_46113_c, 10F, field_46114_a.getVerticalFaceSpeed());
    }
}
