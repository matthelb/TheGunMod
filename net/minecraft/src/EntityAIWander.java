package net.minecraft.src;

import java.util.Random;

public class EntityAIWander extends EntityAIBase
{
    private EntityCreature field_46104_a;
    private double field_46102_b;
    private double field_46103_c;
    private double field_46101_d;

    public EntityAIWander(EntityCreature entitycreature)
    {
        field_46104_a = entitycreature;
        func_46087_a(3);
    }

    public boolean func_46090_a()
    {
        if (field_46104_a.func_46018_ak() >= 100)
        {
            return false;
        }
        if (field_46104_a.func_46019_ai().nextInt(120) != 0)
        {
            return false;
        }
        Vec3D vec3d = func_46100_h();
        if (vec3d == null)
        {
            return false;
        }
        else
        {
            field_46102_b = vec3d.xCoord;
            field_46103_c = vec3d.yCoord;
            field_46101_d = vec3d.zCoord;
            return true;
        }
    }

    public boolean func_46092_g()
    {
        return !field_46104_a.func_46023_ah().func_46034_b();
    }

    public void func_46088_e()
    {
        field_46104_a.func_46023_ah().func_46033_a(field_46102_b, field_46103_c, field_46101_d, field_46104_a.func_46016_ar());
    }

    private Vec3D func_46100_h()
    {
        Random random = field_46104_a.func_46019_ai();
        boolean flag = false;
        int i = -1;
        int j = -1;
        int k = -1;
        float f = -99999F;
        for (int l = 0; l < 10; l++)
        {
            int i1 = MathHelper.floor_double((field_46104_a.posX + (double)random.nextInt(13)) - 6D);
            int j1 = MathHelper.floor_double((field_46104_a.posY + (double)random.nextInt(7)) - 3D);
            int k1 = MathHelper.floor_double((field_46104_a.posZ + (double)random.nextInt(13)) - 6D);
            float f1 = field_46104_a.getBlockPathWeight(i1, j1, k1);
            if (f1 > f)
            {
                f = f1;
                i = i1;
                j = j1;
                k = k1;
                flag = true;
            }
        }

        if (flag)
        {
            return Vec3D.createVector(i, j, k);
        }
        else
        {
            return null;
        }
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
}
