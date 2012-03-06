package net.minecraft.src;

public class EntityAIOcelotAttack extends EntityAIBase
{
    World field_48171_a;
    EntityLiving field_48169_b;
    EntityLiving field_48170_c;
    int field_48168_d;

    public EntityAIOcelotAttack(EntityLiving par1EntityLiving)
    {
        field_48168_d = 0;
        field_48169_b = par1EntityLiving;
        field_48171_a = par1EntityLiving.worldObj;
        func_46087_a(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLiving entityliving = field_48169_b.func_48331_as();

        if (entityliving == null)
        {
            return false;
        }
        else
        {
            field_48170_c = entityliving;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (!field_48170_c.isEntityAlive())
        {
            return false;
        }

        if (field_48169_b.getDistanceSqToEntity(field_48170_c) > 225D)
        {
            return false;
        }
        else
        {
            return !field_48169_b.func_48333_ak().func_46034_b() || shouldExecute();
        }
    }

    public void resetTask()
    {
        field_48170_c = null;
        field_48169_b.func_48333_ak().func_48662_f();
    }

    public void updateTask()
    {
        field_48169_b.getLookHelper().setLookPositionWithEntity(field_48170_c, 30F, 30F);
        double d = field_48169_b.width * 2.0F * (field_48169_b.width * 2.0F);
        double d1 = field_48169_b.getDistanceSq(field_48170_c.posX, field_48170_c.boundingBox.minY, field_48170_c.posZ);
        float f = 0.23F;

        if (d1 > d && d1 < 16D)
        {
            f = 0.4F;
        }
        else if (d1 < 225D)
        {
            f = 0.18F;
        }

        field_48169_b.func_48333_ak().func_48652_a(field_48170_c, f);
        field_48168_d = Math.max(field_48168_d - 1, 0);

        if (d1 > d)
        {
            return;
        }

        if (field_48168_d > 0)
        {
            return;
        }
        else
        {
            field_48168_d = 20;
            field_48169_b.attackEntityAsMob(field_48170_c);
            return;
        }
    }
}
