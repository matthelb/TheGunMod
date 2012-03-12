package net.minecraft.src;

public class EntityAISit extends EntityAIBase
{
    private EntityTameable field_48212_a;
    private boolean field_48211_b;

    public EntityAISit(EntityTameable par1EntityTameable)
    {
        field_48211_b = false;
        field_48212_a = par1EntityTameable;
        setMutexBits(5);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!field_48212_a.isTamed())
        {
            return false;
        }

        if (field_48212_a.isInWater())
        {
            return false;
        }

        if (!field_48212_a.onGround)
        {
            return false;
        }

        EntityLiving entityliving = field_48212_a.getOwner();

        if (entityliving == null)
        {
            return true;
        }

        if (field_48212_a.getDistanceSqToEntity(entityliving) < 144D && entityliving.getAITarget() != null)
        {
            return false;
        }
        else
        {
            return field_48211_b;
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        field_48212_a.getNavigator().func_48662_f();
        field_48212_a.func_48369_c(true);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        field_48212_a.func_48369_c(false);
    }

    public void func_48210_a(boolean par1)
    {
        field_48211_b = par1;
    }
}
