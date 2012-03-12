package net.minecraft.src;

public class EntityAIDefendVillage extends EntityAITarget
{
    EntityIronGolem irongolem;
    EntityLiving field_48301_b;

    public EntityAIDefendVillage(EntityIronGolem par1EntityIronGolem)
    {
        super(par1EntityIronGolem, 16F, false, true);
        irongolem = par1EntityIronGolem;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        Village village = irongolem.getVillage();

        if (village == null)
        {
            return false;
        }
        else
        {
            field_48301_b = village.findNearestVillageAggressor(irongolem);
            return func_48284_a(field_48301_b, false);
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        irongolem.setAttackTarget(field_48301_b);
        super.startExecuting();
    }
}
