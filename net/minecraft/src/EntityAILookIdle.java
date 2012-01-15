package net.minecraft.src;

import java.util.Random;

public class EntityAILookIdle extends EntityAIBase
{
    private EntityLiving field_46114_a;
    private double field_46112_b;
    private double field_46113_c;
    private int field_46111_d;

    public EntityAILookIdle(EntityLiving entityliving)
    {
        field_46111_d = 0;
        field_46114_a = entityliving;
        func_46087_a(3);
    }

    public boolean func_46090_a()
    {
        return field_46114_a.func_46019_ai().nextFloat() < 0.02F;
    }

    public boolean func_46092_g()
    {
        return field_46111_d >= 0;
    }

    public void func_46088_e()
    {
        double d = 6.2831853071795862D * field_46114_a.func_46019_ai().nextDouble();
        field_46112_b = Math.cos(d);
        field_46113_c = Math.sin(d);
        field_46111_d = 20 + field_46114_a.func_46019_ai().nextInt(20);
    }

    public void func_46089_b()
    {
        field_46111_d--;
        field_46114_a.func_46021_ae().func_46060_a(field_46114_a.posX + field_46112_b, field_46114_a.posY + (double)field_46114_a.getEyeHeight(), field_46114_a.posZ + field_46113_c, 10F, field_46114_a.getVerticalFaceSpeed());
    }

    public int func_46091_c()
    {
        return super.func_46091_c();
    }

    public void func_46087_a(int i)
    {
        super.func_46087_a(i);
    }

    public void func_46085_d()
    {
        super.func_46085_d();
    }

    public boolean func_46086_f()
    {
        return super.func_46086_f();
    }
}
