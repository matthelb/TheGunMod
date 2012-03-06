package net.minecraft.src;

public class EntityAICreeperSwell extends EntityAIBase
{
    EntityCreeper field_48244_a;
    EntityLiving field_48243_b;

    public EntityAICreeperSwell(EntityCreeper par1EntityCreeper)
    {
        field_48244_a = par1EntityCreeper;
        func_46087_a(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLiving entityliving = field_48244_a.func_48331_as();
        return field_48244_a.getCreeperState() > 0 || entityliving != null && field_48244_a.getDistanceSqToEntity(entityliving) < 9D;
    }

    public void func_46088_e()
    {
        field_48244_a.func_48333_ak().func_48662_f();
        field_48243_b = field_48244_a.func_48331_as();
    }

    public void resetTask()
    {
        field_48243_b = null;
    }

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

        if (!field_48244_a.func_48318_al().func_48546_a(field_48243_b))
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
