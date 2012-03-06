package net.minecraft.src;

public class EntityAIOwnerHurtTarget extends EntityAITarget
{
    EntityTameable field_48304_a;
    EntityLiving field_48303_b;

    public EntityAIOwnerHurtTarget(EntityTameable par1EntityTameable)
    {
        super(par1EntityTameable, 32F, false);
        field_48304_a = par1EntityTameable;
        func_46087_a(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!field_48304_a.func_48373_u_())
        {
            return false;
        }

        EntityLiving entityliving = field_48304_a.func_48368_w_();

        if (entityliving == null)
        {
            return false;
        }
        else
        {
            field_48303_b = entityliving.func_48324_ao();
            return func_48284_a(field_48303_b, false);
        }
    }

    public void func_46088_e()
    {
        field_48291_c.func_48327_b(field_48303_b);
        super.func_46088_e();
    }
}
