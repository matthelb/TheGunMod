package net.minecraft.src;

public class EntityAIDefendVillage extends EntityAITarget
{
    EntityIronGolem field_48302_a;
    EntityLiving field_48301_b;

    public EntityAIDefendVillage(EntityIronGolem par1EntityIronGolem)
    {
        super(par1EntityIronGolem, 16F, false, true);
        field_48302_a = par1EntityIronGolem;
        func_46087_a(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        Village village = field_48302_a.func_48380_l_();

        if (village == null)
        {
            return false;
        }
        else
        {
            field_48301_b = village.func_48534_b(field_48302_a);
            return func_48284_a(field_48301_b, false);
        }
    }

    public void func_46088_e()
    {
        field_48302_a.func_48327_b(field_48301_b);
        super.func_46088_e();
    }
}
