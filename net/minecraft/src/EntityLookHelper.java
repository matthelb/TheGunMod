package net.minecraft.src;

public class EntityLookHelper
{
    private EntityLiving field_46068_a;
    private float field_46066_b;
    private float field_46067_c;
    private boolean field_46064_d;
    private double field_46065_e;
    private double field_46062_f;
    private double field_46063_g;

    public EntityLookHelper(EntityLiving entityliving)
    {
        field_46064_d = false;
        field_46068_a = entityliving;
    }

    public void func_46058_a(Entity entity, float f, float f1)
    {
        field_46065_e = entity.posX;
        if (entity instanceof EntityLiving)
        {
            field_46062_f = entity.posY + (double)((EntityLiving)entity).getEyeHeight();
        }
        else
        {
            field_46062_f = (entity.boundingBox.minY + entity.boundingBox.maxY) / 2D;
        }
        field_46063_g = entity.posZ;
        field_46066_b = f;
        field_46067_c = f1;
        field_46064_d = true;
    }

    public void func_46060_a(double d, double d1, double d2, float f,
            float f1)
    {
        field_46065_e = d;
        field_46062_f = d1;
        field_46063_g = d2;
        field_46066_b = f;
        field_46067_c = f1;
        field_46064_d = true;
    }

    public void func_46059_a()
    {
        field_46068_a.rotationPitch = 0.0F;
        if (!field_46064_d)
        {
            return;
        }
        else
        {
            field_46064_d = false;
            double d = field_46065_e - field_46068_a.posX;
            double d1 = field_46062_f - (field_46068_a.posY + (double)field_46068_a.getEyeHeight());
            double d2 = field_46063_g - field_46068_a.posZ;
            double d3 = MathHelper.sqrt_double(d * d + d2 * d2);
            float f = (float)((Math.atan2(d2, d) * 180D) / 3.1415927410125732D) - 90F;
            float f1 = (float)(-((Math.atan2(d1, d3) * 180D) / 3.1415927410125732D));
            field_46068_a.rotationPitch = func_46061_a(field_46068_a.rotationPitch, f1, field_46067_c);
            field_46068_a.rotationYaw = func_46061_a(field_46068_a.rotationYaw, f, field_46066_b);
            return;
        }
    }

    private float func_46061_a(float f, float f1, float f2)
    {
        float f3;
        for (f3 = f1 - f; f3 < -180F; f3 += 360F) { }
        for (; f3 >= 180F; f3 -= 360F) { }
        if (f3 > f2)
        {
            f3 = f2;
        }
        if (f3 < -f2)
        {
            f3 = -f2;
        }
        return f + f3;
    }
}
