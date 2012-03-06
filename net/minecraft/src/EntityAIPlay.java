package net.minecraft.src;

import java.util.*;

public class EntityAIPlay extends EntityAIBase
{
    private EntityVillager field_48167_a;
    private EntityLiving field_48165_b;
    private float field_48166_c;
    private int field_48164_d;

    public EntityAIPlay(EntityVillager par1EntityVillager, float par2)
    {
        field_48167_a = par1EntityVillager;
        field_48166_c = par2;
        func_46087_a(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (field_48167_a.func_48351_J() >= 0)
        {
            return false;
        }

        if (field_48167_a.getRNG().nextInt(400) != 0)
        {
            return false;
        }

        List list = field_48167_a.worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityVillager.class, field_48167_a.boundingBox.expand(6D, 3D, 6D));
        double d = Double.MAX_VALUE;
        Iterator iterator = list.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            Entity entity = (Entity)iterator.next();

            if (entity != field_48167_a)
            {
                EntityVillager entityvillager = (EntityVillager)entity;

                if (!entityvillager.func_48353_E_() && entityvillager.func_48351_J() < 0)
                {
                    double d1 = entityvillager.getDistanceSqToEntity(field_48167_a);

                    if (d1 <= d)
                    {
                        d = d1;
                        field_48165_b = entityvillager;
                    }
                }
            }
        }
        while (true);

        if (field_48165_b == null)
        {
            Vec3D vec3d = RandomPositionGenerator.func_48396_a(field_48167_a, 16, 3);

            if (vec3d == null)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return field_48164_d > 0;
    }

    public void func_46088_e()
    {
        if (field_48165_b != null)
        {
            field_48167_a.func_48354_b(true);
        }

        field_48164_d = 1000;
    }

    public void resetTask()
    {
        field_48167_a.func_48354_b(false);
        field_48165_b = null;
    }

    public void updateTask()
    {
        field_48164_d--;

        if (field_48165_b != null)
        {
            if (field_48167_a.getDistanceSqToEntity(field_48165_b) > 4D)
            {
                field_48167_a.func_48333_ak().func_48652_a(field_48165_b, field_48166_c);
            }
        }
        else if (field_48167_a.func_48333_ak().func_46034_b())
        {
            Vec3D vec3d = RandomPositionGenerator.func_48396_a(field_48167_a, 16, 3);

            if (vec3d == null)
            {
                return;
            }

            field_48167_a.func_48333_ak().func_48658_a(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, field_48166_c);
        }
    }
}
