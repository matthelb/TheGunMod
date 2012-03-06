package net.minecraft.src;

public class EntityAIOwnerHurtByTarget extends EntityAITarget
{
    EntityTameable field_48294_a;
    EntityLiving field_48293_b;

    public EntityAIOwnerHurtByTarget(EntityTameable par1EntityTameable)
    {
        super(par1EntityTameable, 32F, false);
        field_48294_a = par1EntityTameable;
        func_46087_a(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!field_48294_a.func_48373_u_())
        {
            return false;
        }

        EntityLiving entityliving = field_48294_a.func_48368_w_();

        if (entityliving == null)
        {
            return false;
        }
        else
        {
            field_48293_b = entityliving.getAITarget();
            return func_48284_a(field_48293_b, false);
        }
    }

    public void func_46088_e()
    {
        field_48291_c.func_48327_b(field_48293_b);
        super.func_46088_e();
    }
}
