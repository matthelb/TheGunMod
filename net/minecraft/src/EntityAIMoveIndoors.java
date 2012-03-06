package net.minecraft.src;

import java.util.Random;

public class EntityAIMoveIndoors extends EntityAIBase
{
    private EntityCreature field_48175_a;
    private VillageDoorInfo field_48173_b;
    private int field_48174_c;
    private int field_48172_d;

    public EntityAIMoveIndoors(EntityCreature par1EntityCreature)
    {
        field_48174_c = -1;
        field_48172_d = -1;
        field_48175_a = par1EntityCreature;
        func_46087_a(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (field_48175_a.worldObj.isDaytime() && !field_48175_a.worldObj.isRaining() || field_48175_a.worldObj.worldProvider.hasNoSky)
        {
            return false;
        }

        if (field_48175_a.getRNG().nextInt(50) != 0)
        {
            return false;
        }

        if (field_48174_c != -1 && field_48175_a.getDistanceSq(field_48174_c, field_48175_a.posY, field_48172_d) < 4D)
        {
            return false;
        }

        Village village = field_48175_a.worldObj.field_48096_A.func_48632_a(MathHelper.floor_double(field_48175_a.posX), MathHelper.floor_double(field_48175_a.posY), MathHelper.floor_double(field_48175_a.posZ), 14);

        if (village == null)
        {
            return false;
        }
        else
        {
            field_48173_b = village.func_48513_c(MathHelper.floor_double(field_48175_a.posX), MathHelper.floor_double(field_48175_a.posY), MathHelper.floor_double(field_48175_a.posZ));
            return field_48173_b != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !field_48175_a.func_48333_ak().func_46034_b();
    }

    public void func_46088_e()
    {
        field_48174_c = -1;

        if (field_48175_a.getDistanceSq(field_48173_b.func_48483_a(), field_48173_b.field_48491_b, field_48173_b.func_48484_c()) > 256D)
        {
            Vec3D vec3d = RandomPositionGenerator.func_48395_a(field_48175_a, 14, 3, Vec3D.createVector((double)field_48173_b.func_48483_a() + 0.5D, field_48173_b.func_48485_b(), (double)field_48173_b.func_48484_c() + 0.5D));

            if (vec3d != null)
            {
                field_48175_a.func_48333_ak().func_48658_a(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 0.3F);
            }
        }
        else
        {
            field_48175_a.func_48333_ak().func_48658_a((double)field_48173_b.func_48483_a() + 0.5D, field_48173_b.func_48485_b(), (double)field_48173_b.func_48484_c() + 0.5D, 0.3F);
        }
    }

    public void resetTask()
    {
        field_48174_c = field_48173_b.func_48483_a();
        field_48172_d = field_48173_b.func_48484_c();
        field_48173_b = null;
    }
}
