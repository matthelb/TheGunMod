package net.minecraft.src;

public abstract class EntityAIBase
{
    private int field_46093_a;

    public EntityAIBase()
    {
        field_46093_a = 0;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public abstract boolean shouldExecute();

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return shouldExecute();
    }

    public boolean isContinous()
    {
        return true;
    }

    public void func_46088_e()
    {
    }

    public void resetTask()
    {
    }

    public void updateTask()
    {
    }

    public void func_46087_a(int par1)
    {
        field_46093_a = par1;
    }

    public int func_46091_c()
    {
        return field_46093_a;
    }
}
