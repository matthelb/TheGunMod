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

    public void func_46088_e()
    {
        field_48239_a.func_48333_ak().func_48669_d(true);
    }

    public void resetTask()
    {
        field_48239_a.func_48333_ak().func_48669_d(false);
    }
}
