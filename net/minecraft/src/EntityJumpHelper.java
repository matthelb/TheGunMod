package net.minecraft.src;

public class EntityJumpHelper
{
    private EntityLiving field_46118_a;
    private boolean field_46117_b;

    public EntityJumpHelper(EntityLiving entityliving)
    {
        field_46117_b = false;
        field_46118_a = entityliving;
    }

    public void func_46115_a()
    {
        field_46117_b = true;
    }

    public void func_46116_b()
    {
        if (!field_46117_b)
        {
            return;
        }
        else
        {
            field_46118_a.func_46014_e(true);
            field_46117_b = false;
            return;
        }
    }
}
