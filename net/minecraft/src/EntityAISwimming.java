package net.minecraft.src;

import java.util.Random;

public class EntityAISwimming extends EntityAIBase
{
    private EntityLiving field_46105_a;

    public EntityAISwimming(EntityLiving par1EntityLiving)
    {
        field_46105_a = par1EntityLiving;
        func_46087_a(4);
        par1EntityLiving.func_48333_ak().func_48660_e(true);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return field_46105_a.isInWater() || field_46105_a.handleLavaMovement();
    }

    public void updateTask()
    {
        if (field_46105_a.getRNG().nextFloat() < 0.8F)
        {
            field_46105_a.getJumpHelper().setJumping();
        }
    }
}
