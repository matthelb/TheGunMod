package net.minecraft.src;

import java.util.Random;

public class EntityAIWatchClosest extends EntityAIBase
{
    private EntityLiving field_46110_a;
    private EntityLiving field_46108_b;
    private World field_46109_c;
    private float field_46106_d;
    private int field_46107_e;

    public EntityAIWatchClosest(EntityLiving entityliving, World world, float f)
    {
        field_46110_a = entityliving;
        field_46109_c = world;
        field_46106_d = f;
        func_46087_a(3);
    }

    public boolean func_46090_a()
    {
        if (field_46110_a.getRNG().nextFloat() >= 0.02F)
        {
            return false;
        }
        field_46108_b = field_46109_c.getClosestPlayerToEntity(field_46110_a, field_46106_d);
        return field_46108_b != null;
    }

    public boolean func_46092_g()
    {
        if (!field_46108_b.isEntityAlive())
        {
            return false;
        }
        if (field_46110_a.getDistanceSqToEntity(field_46108_b) > (double)(field_46106_d * field_46106_d))
        {
            return false;
        }
        else
        {
            return field_46107_e > 0;
        }
    }

    public void func_46088_e()
    {
        field_46107_e = 40 + field_46110_a.getRNG().nextInt(40);
    }

    public void func_46085_d()
    {
        field_46108_b = null;
    }

    public void func_46089_b()
    {
        field_46110_a.getLookHelper().func_46060_a(field_46108_b.posX, field_46108_b.posY + (double)field_46108_b.getEyeHeight(), field_46108_b.posZ, 10F, field_46110_a.getVerticalFaceSpeed());
        field_46107_e--;
    }

    public int func_46091_c()
    {
        return super.func_46091_c();
    }

    public void func_46087_a(int i)
    {
        super.func_46087_a(i);
    }

    public boolean func_46086_f()
    {
        return super.func_46086_f();
    }
}
