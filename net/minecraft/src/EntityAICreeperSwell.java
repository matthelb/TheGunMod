package net.minecraft.src;

public class EntityAICreeperSwell extends EntityAIBase
{
    EntityCreeper field_48244_a;
    EntityLiving field_48243_b;

    public EntityAICreeperSwell(EntityCreeper par1EntityCreeper)
    {
        field_48244_a = par1EntityCreeper;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLiving entityliving = field_48244_a.getAttackTarget();
        return field_48244_a.getCreeperState() > 0 || entityliving != null && field_48244_a.getDistanceSqToEntity(entityliving) < 9D;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        field_48244_a.getNavigator().func_48662_f();
        field_48243_b = field_48244_a.getAttackTarget();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        field_48243_b = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (field_48243_b == null)
        {
            field_48244_a.setCreeperState(-1);
            return;
        }

        if (field_48244_a.getDistanceSqToEntity(field_48243_b) > 49D)
        {
            field_48244_a.setCreeperState(-1);
            return;
        }

        if (!field_48244_a.func_48318_al().canSee(field_48243_b))
        {
            field_48244_a.setCreeperState(-1);
            return;
        }
        else
        {
            field_48244_a.setCreeperState(1);
            return;
        }
    }
}
