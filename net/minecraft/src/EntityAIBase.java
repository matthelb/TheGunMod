package net.minecraft.src;

abstract class EntityAIBase
{
    private int field_46093_a;

    EntityAIBase()
    {
        field_46093_a = 0;
    }

    public abstract boolean func_46090_a();

    public boolean func_46092_g()
    {
        return func_46090_a();
    }

    public boolean func_46086_f()
    {
        return true;
    }

    public void func_46088_e()
    {
    }

    public void func_46085_d()
    {
    }

    public void func_46089_b()
    {
    }

    public void func_46087_a(int i)
    {
        field_46093_a = i;
    }

    public int func_46091_c()
    {
        return field_46093_a;
    }
}
