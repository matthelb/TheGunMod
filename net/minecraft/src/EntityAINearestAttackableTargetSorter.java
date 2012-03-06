package net.minecraft.src;

import java.util.Comparator;

public class EntityAINearestAttackableTargetSorter implements Comparator
{
    private Entity field_48471_b;
    final EntityAINearestAttackableTarget field_48472_a;

    public EntityAINearestAttackableTargetSorter(EntityAINearestAttackableTarget par1EntityAINearestAttackableTarget, Entity par2Entity)
    {
        field_48472_a = par1EntityAINearestAttackableTarget;
        field_48471_b = par2Entity;
    }

    public int func_48470_a(Entity par1Entity, Entity par2Entity)
    {
        double d = field_48471_b.getDistanceSqToEntity(par1Entity);
        double d1 = field_48471_b.getDistanceSqToEntity(par2Entity);

        if (d < d1)
        {
            return -1;
        }

        return d <= d1 ? 0 : 1;
    }

    public int compare(Object par1Obj, Object par2Obj)
    {
        return func_48470_a((Entity)par1Obj, (Entity)par2Obj);
    }
}
