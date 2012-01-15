package net.minecraft.src;

public class EntityMoveHelper
{
    private EntityLiving field_46079_a;
    private double field_46077_b;
    private double field_46078_c;
    private double field_46075_d;
    private float field_46076_e;
    private boolean field_46074_f;

    public EntityMoveHelper(EntityLiving entityliving, float f)
    {
        field_46074_f = false;
        field_46079_a = entityliving;
        field_46077_b = entityliving.posX;
        field_46078_c = entityliving.posY;
        field_46075_d = entityliving.posZ;
        field_46076_e = f;
    }

    public void func_46073_a(double d, double d1, double d2)
    {
        field_46077_b = d;
        field_46078_c = d1;
        field_46075_d = d2;
        field_46074_f = true;
    }

    public void func_46071_a(float f)
    {
        field_46076_e = f;
    }

    public void func_46072_a()
    {
        field_46079_a.func_46017_d(0.0F);
        if (!field_46074_f)
        {
            return;
        }
        field_46074_f = false;
        int i = MathHelper.floor_double(field_46079_a.boundingBox.minY + 0.5D);
        double d = field_46077_b - field_46079_a.posX;
        double d1 = field_46075_d - field_46079_a.posZ;
        double d2 = field_46078_c - (double)i;
        float f = (float)((Math.atan2(d1, d) * 180D) / 3.1415927410125732D) - 90F;
        float f1;
        for (f1 = f - field_46079_a.rotationYaw; f1 < -180F; f1 += 360F) { }
        for (; f1 >= 180F; f1 -= 360F) { }
        if (f1 > 30F)
        {
            f1 = 30F;
        }
        if (f1 < -30F)
        {
            f1 = -30F;
        }
        field_46079_a.rotationYaw += f1;
        field_46079_a.func_46017_d(field_46076_e);
        field_46079_a.func_46014_e(d2 > 0.0D);
    }
}
