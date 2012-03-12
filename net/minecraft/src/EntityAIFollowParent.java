package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class EntityAIFollowParent extends EntityAIBase
{
    EntityAnimal childAnimal;
    EntityAnimal field_48139_b;
    float field_48140_c;
    private int field_48138_d;

    public EntityAIFollowParent(EntityAnimal par1EntityAnimal, float par2)
    {
        childAnimal = par1EntityAnimal;
        field_48140_c = par2;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (childAnimal.getGrowingAge() >= 0)
        {
            return false;
        }

        List list = childAnimal.worldObj.getEntitiesWithinAABB(childAnimal.getClass(), childAnimal.boundingBox.expand(8D, 4D, 8D));
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

            if (entityanimal1.getGrowingAge() >= 0)
            {
                double d1 = childAnimal.getDistanceSqToEntity(entityanimal1);

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

        double d = childAnimal.getDistanceSqToEntity(field_48139_b);
        return d >= 9D && d <= 256D;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        field_48138_d = 0;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        field_48139_b = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (--field_48138_d > 0)
        {
            return;
        }
        else
        {
            field_48138_d = 10;
            childAnimal.getNavigator().func_48652_a(field_48139_b, field_48140_c);
            return;
        }
    }
}
