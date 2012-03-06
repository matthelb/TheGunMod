package net.minecraft.src;

import java.util.Random;

public class EntityAIFleeSun extends EntityAIBase
{
    private EntityCreature field_48260_a;
    private double field_48258_b;
    private double field_48259_c;
    private double field_48256_d;
    private float field_48257_e;
    private World field_48255_f;

    public EntityAIFleeSun(EntityCreature par1EntityCreature, float par2)
    {
        field_48260_a = par1EntityCreature;
        field_48257_e = par2;
        field_48255_f = par1EntityCreature.worldObj;
        func_46087_a(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!field_48255_f.isDaytime())
        {
            return false;
        }

        if (!field_48260_a.isBurning())
        {
            return false;
        }

        if (!field_48255_f.canBlockSeeTheSky(MathHelper.floor_double(field_48260_a.posX), (int)field_48260_a.boundingBox.minY, MathHelper.floor_double(field_48260_a.posZ)))
        {
            return false;
        }

        Vec3D vec3d = func_48254_f();

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            field_48258_b = vec3d.xCoord;
            field_48259_c = vec3d.yCoord;
            field_48256_d = vec3d.zCoord;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !field_48260_a.func_48333_ak().func_46034_b();
    }

    public void func_46088_e()
    {
        field_48260_a.func_48333_ak().func_48658_a(field_48258_b, field_48259_c, field_48256_d, field_48257_e);
    }

    private Vec3D func_48254_f()
    {
        Random random = field_48260_a.getRNG();

        for (int i = 0; i < 10; i++)
        {
            int j = MathHelper.floor_double((field_48260_a.posX + (double)random.nextInt(20)) - 10D);
            int k = MathHelper.floor_double((field_48260_a.boundingBox.minY + (double)random.nextInt(6)) - 3D);
            int l = MathHelper.floor_double((field_48260_a.posZ + (double)random.nextInt(20)) - 10D);

            if (!field_48255_f.canBlockSeeTheSky(j, k, l) && field_48260_a.getBlockPathWeight(j, k, l) < 0.0F)
            {
                return Vec3D.createVector(j, k, l);
            }
        }

        return null;
    }
}
