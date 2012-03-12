package net.minecraft.src;

public class EntityAIRestrictSun extends EntityAIBase
{
    private EntityCreature field_48239_a;

    public EntityAIRestrictSun(EntityCreature par1EntityCreature)
    {
        field_48239_a = par1EntityCreature;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return field_48239_a.worldObj.isDaytime();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        field_48239_a.getNavigator().func_48669_d(true);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        field_48239_a.getNavigator().func_48669_d(false);
    }
}
