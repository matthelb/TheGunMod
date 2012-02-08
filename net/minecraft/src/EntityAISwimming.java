package net.minecraft.src;

import java.util.Random;

public class EntityAISwimming extends EntityAIBase
{
    private EntityLiving field_46105_a;

    public EntityAISwimming(EntityLiving entityliving)
    {
        field_46105_a = entityliving;
        func_46087_a(4);
    }

    public boolean func_46090_a()
    {
        return field_46105_a.getRNG().nextFloat() < 0.8F && (field_46105_a.isInWater() || field_46105_a.handleLavaMovement());
    }

    public void func_46088_e()
    {
        field_46105_a.getJumpHelper().setJumping();
    }

    public int func_46091_c()
    {
        return super.func_46091_c();
    }

    public void func_46087_a(int i)
    {
        super.func_46087_a(i);
    }

    public void func_46089_b()
    {
        super.func_46089_b();
    }

    public void func_46085_d()
    {
        super.func_46085_d();
    }

    public boolean func_46086_f()
    {
        return super.func_46086_f();
    }

    public boolean func_46092_g()
    {
        return super.func_46092_g();
    }
}
