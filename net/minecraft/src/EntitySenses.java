package net.minecraft.src;

import java.util.ArrayList;

public class EntitySenses
{
    EntityLiving field_48550_a;
    ArrayList field_48548_b;
    ArrayList field_48549_c;

    public EntitySenses(EntityLiving par1EntityLiving)
    {
        field_48548_b = new ArrayList();
        field_48549_c = new ArrayList();
        field_48550_a = par1EntityLiving;
    }

    public void func_48547_a()
    {
        field_48548_b.clear();
        field_48549_c.clear();
    }

    public boolean func_48546_a(Entity par1Entity)
    {
        if (field_48548_b.contains(par1Entity))
        {
            return true;
        }

        if (field_48549_c.contains(par1Entity))
        {
            return false;
        }

        Profiler.startSection("canSee");
        boolean flag = field_48550_a.canEntityBeSeen(par1Entity);
        Profiler.endSection();

        if (flag)
        {
            field_48548_b.add(par1Entity);
        }
        else
        {
            field_48549_c.add(par1Entity);
        }

        return flag;
    }
}
