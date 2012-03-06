package net.minecraft.src;

import java.util.*;

public class EntityAIFollowGolem extends EntityAIBase
{
    private EntityVillager field_48216_a;
    private EntityIronGolem field_48214_b;
    private int field_48215_c;
    private boolean field_48213_d;

    public EntityAIFollowGolem(EntityVillager par1EntityVillager)
    {
        field_48213_d = false;
        field_48216_a = par1EntityVillager;
        func_46087_a(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (field_48216_a.func_48351_J() >= 0)
        {
            return false;
        }

        if (!field_48216_a.worldObj.isDaytime())
        {
            return false;
        }

        List list = field_48216_a.worldObj.getEntitiesWithinAABB(net.minecraft.src.EntityIronGolem.class, field_48216_a.boundingBox.expand(6D, 2D, 6D));

        if (list.size() == 0)
        {
            return false;
        }

        Iterator iterator = list.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            Entity entity = (Entity)iterator.next();
            EntityIronGolem entityirongolem = (EntityIronGolem)entity;

            if (entityirongolem.func_48382_m_() <= 0)
            {
                continue;
            }

            field_48214_b = entityirongolem;
            break;
        }
        while (true);

        return field_48214_b != null;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return field_48214_b.func_48382_m_() > 0;
    }

    public void func_46088_e()
    {
        field_48215_c = field_48216_a.getRNG().nextInt(320);
        field_48213_d = false;
        field_48214_b.func_48333_ak().func_48662_f();
    }

    public void resetTask()
    {
        field_48214_b = null;
        field_48216_a.func_48333_ak().func_48662_f();
    }

    public void updateTask()
    {
        field_48216_a.getLookHelper().setLookPositionWithEntity(field_48214_b, 30F, 30F);

        if (field_48214_b.func_48382_m_() == field_48215_c)
        {
            field_48216_a.func_48333_ak().func_48652_a(field_48214_b, 0.15F);
            field_48213_d = true;
        }

        if (field_48213_d && field_48216_a.getDistanceSqToEntity(field_48214_b) < 4D)
        {
            field_48214_b.func_48383_a(false);
            field_48216_a.func_48333_ak().func_48662_f();
        }
    }
}
