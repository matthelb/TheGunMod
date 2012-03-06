package net.minecraft.src;

import java.util.Random;

public class EntityAIBreakDoor extends EntityAIDoorInteract
{
    private int field_48194_i;

    public EntityAIBreakDoor(EntityLiving par1EntityLiving)
    {
        super(par1EntityLiving);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!super.shouldExecute())
        {
            return false;
        }
        else
        {
            return !field_48189_e.func_48135_d(field_48192_a.worldObj, field_48190_b, field_48191_c, field_48188_d);
        }
    }

    public void func_46088_e()
    {
        super.func_46088_e();
        field_48194_i = 240;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        double d = field_48192_a.getDistanceSq(field_48190_b, field_48191_c, field_48188_d);
        return field_48194_i >= 0 && !field_48189_e.func_48135_d(field_48192_a.worldObj, field_48190_b, field_48191_c, field_48188_d) && d < 4D;
    }

    public void updateTask()
    {
        super.updateTask();

        if (field_48192_a.getRNG().nextInt(20) == 0)
        {
            field_48192_a.worldObj.playAuxSFX(1010, field_48190_b, field_48191_c, field_48188_d, 0);
        }

        if (--field_48194_i == 0 && field_48192_a.worldObj.difficultySetting == 3)
        {
            field_48192_a.worldObj.setBlockWithNotify(field_48190_b, field_48191_c, field_48188_d, 0);
            field_48192_a.worldObj.playAuxSFX(1012, field_48190_b, field_48191_c, field_48188_d, 0);
            field_48192_a.worldObj.playAuxSFX(2001, field_48190_b, field_48191_c, field_48188_d, field_48189_e.blockID);
        }
    }
}
