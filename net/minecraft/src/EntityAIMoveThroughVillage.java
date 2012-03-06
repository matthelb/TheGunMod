package net.minecraft.src;

import java.util.*;

public class EntityAIMoveThroughVillage extends EntityAIBase
{
    private EntityCreature field_48283_a;
    private float field_48281_b;
    private PathEntity field_48282_c;
    private VillageDoorInfo field_48279_d;
    private boolean field_48280_e;
    private List field_48278_f;

    public EntityAIMoveThroughVillage(EntityCreature par1EntityCreature, float par2, boolean par3)
    {
        field_48278_f = new ArrayList();
        field_48283_a = par1EntityCreature;
        field_48281_b = par2;
        field_48280_e = par3;
        func_46087_a(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        func_48277_f();

        if (field_48280_e && field_48283_a.worldObj.isDaytime())
        {
            return false;
        }

        Village village = field_48283_a.worldObj.field_48096_A.func_48632_a(MathHelper.floor_double(field_48283_a.posX), MathHelper.floor_double(field_48283_a.posY), MathHelper.floor_double(field_48283_a.posZ), 0);

        if (village == null)
        {
            return false;
        }

        field_48279_d = func_48276_a(village);

        if (field_48279_d == null)
        {
            return false;
        }

        boolean flag = field_48283_a.func_48333_ak().func_48657_b();
        field_48283_a.func_48333_ak().func_48663_b(false);
        field_48282_c = field_48283_a.func_48333_ak().func_48650_a(field_48279_d.field_48493_a, field_48279_d.field_48491_b, field_48279_d.field_48492_c);
        field_48283_a.func_48333_ak().func_48663_b(flag);

        if (field_48282_c != null)
        {
            return true;
        }

        Vec3D vec3d = RandomPositionGenerator.func_48395_a(field_48283_a, 10, 7, Vec3D.createVector(field_48279_d.field_48493_a, field_48279_d.field_48491_b, field_48279_d.field_48492_c));

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            field_48283_a.func_48333_ak().func_48663_b(false);
            field_48282_c = field_48283_a.func_48333_ak().func_48650_a(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
            field_48283_a.func_48333_ak().func_48663_b(flag);
            return field_48282_c != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (field_48283_a.func_48333_ak().func_46034_b())
        {
            return false;
        }
        else
        {
            float f = field_48283_a.width + 4F;
            return field_48283_a.getDistanceSq(field_48279_d.field_48493_a, field_48279_d.field_48491_b, field_48279_d.field_48492_c) > (double)(f * f);
        }
    }

    public void func_46088_e()
    {
        field_48283_a.func_48333_ak().func_48647_a(field_48282_c, field_48281_b);
    }

    public void resetTask()
    {
        if (field_48283_a.func_48333_ak().func_46034_b() || field_48283_a.getDistanceSq(field_48279_d.field_48493_a, field_48279_d.field_48491_b, field_48279_d.field_48492_c) < 16D)
        {
            field_48278_f.add(field_48279_d);
        }
    }

    private VillageDoorInfo func_48276_a(Village par1Village)
    {
        VillageDoorInfo villagedoorinfo = null;
        int i = 0x7fffffff;
        List list = par1Village.func_48517_f();
        Iterator iterator = list.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            VillageDoorInfo villagedoorinfo1 = (VillageDoorInfo)iterator.next();
            int j = villagedoorinfo1.func_48481_a(MathHelper.floor_double(field_48283_a.posX), MathHelper.floor_double(field_48283_a.posY), MathHelper.floor_double(field_48283_a.posZ));

            if (j < i && !func_48275_a(villagedoorinfo1))
            {
                villagedoorinfo = villagedoorinfo1;
                i = j;
            }
        }
        while (true);

        return villagedoorinfo;
    }

    private boolean func_48275_a(VillageDoorInfo par1VillageDoorInfo)
    {
        for (Iterator iterator = field_48278_f.iterator(); iterator.hasNext();)
        {
            VillageDoorInfo villagedoorinfo = (VillageDoorInfo)iterator.next();

            if (par1VillageDoorInfo.field_48493_a == villagedoorinfo.field_48493_a && par1VillageDoorInfo.field_48491_b == villagedoorinfo.field_48491_b && par1VillageDoorInfo.field_48492_c == villagedoorinfo.field_48492_c)
            {
                return true;
            }
        }

        return false;
    }

    private void func_48277_f()
    {
        if (field_48278_f.size() > 15)
        {
            field_48278_f.remove(0);
        }
    }
}
