package net.minecraft.src;

import java.util.Random;

public class EntityAILeapAtTarget extends EntityAIBase
{
    EntityLiving field_48163_a;
    EntityLiving field_48161_b;
    float field_48162_c;

    public EntityAILeapAtTarget(EntityLiving par1EntityLiving, float par2)
    {
        field_48163_a = par1EntityLiving;
        field_48162_c = par2;
        func_46087_a(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        field_48161_b = field_48163_a.func_48331_as();

        if (field_48161_b == null)
        {
            return false;
        }

        double d = field_48163_a.getDistanceSqToEntity(field_48161_b);

        if (d < 4D || d > 16D)
        {
            return false;
        }

        if (!field_48163_a.onGround)
        {
            return false;
        }

        return field_48163_a.getRNG().nextInt(5) == 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !field_48163_a.onGround;
    }

    public void func_46088_e()
    {
        double d = field_48161_b.posX - field_48163_a.posX;
        double d1 = field_48161_b.posZ - field_48163_a.posZ;
        float f = MathHelper.sqrt_double(d * d + d1 * d1);
        field_48163_a.motionX += (d / (double)f) * 0.5D * 0.8D + field_48163_a.motionX * 0.2D;
        field_48163_a.motionZ += (d1 / (double)f) * 0.5D * 0.8D + field_48163_a.motionZ * 0.2D;
        field_48163_a.motionY = field_48162_c;
    }
}
