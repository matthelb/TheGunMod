package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class EntityAIFollowParent extends EntityAIBase
{
    EntityAnimal field_48141_a;
    EntityAnimal field_48139_b;
    float field_48140_c;
    private int field_48138_d;

    public EntityAIFollowParent(EntityAnimal par1EntityAnimal, float par2)
    {
        field_48141_a = par1EntityAnimal;
        field_48140_c = par2;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (field_48141_a.func_48351_J() >= 0)
        {
            return false;
        }

        List list = field_48141_a.worldObj.getEntitiesWithinAABB(field_48141_a.getClass(), field_48141_a.boundingBox.expand(8D, 4D, 8D));
        EntityAnimal entityanimal = null;
        double d = Double.MAX_VALUE;
        Iterator iterator = list.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            Entity entity = (Entity)iterator.next();
            EntityAnimal entityanimal1 = (EntityAnimal)entity;

            if (entityanimal1.func_48351_J() >= 0)
            {
                double d1 = field_48141_a.getDistanceSqToEntity(entityanimal1);

                if (d1 <= d)
                {
                    d = d1;
                    entityanimal = entityanimal1;
                }
            }
        }
        while (true);

        if (entityanimal == null)
        {
            return false;
        }

        if (d < 9D)
        {
            return false;
        }
        else
        {
            field_48139_b = entityanimal;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (!field_48139_b.isEntityAlive())
        {
            return false;
        }

        double d = field_48141_a.getDistanceSqToEntity(field_48139_b);
        return d >= 9D && d <= 256D;
    }

    public void func_46088_e()
    {
        field_48138_d = 0;
    }

    public void resetTask()
    {
        field_48139_b = null;
    }

    public void updateTask()
    {
        if (--field_48138_d > 0)
        {
            return;
        }
        else
        {
            field_48138_d = 10;
            field_48141_a.func_48333_ak().func_48652_a(field_48139_b, field_48140_c);
            return;
        }
    }
}
