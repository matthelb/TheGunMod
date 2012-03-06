package net.minecraft.src;

import java.util.*;

public class Village
{
    private final World field_48542_a;
    private final List field_48540_b = new ArrayList();
    private final ChunkCoordinates field_48541_c = new ChunkCoordinates(0, 0, 0);
    private final ChunkCoordinates field_48538_d = new ChunkCoordinates(0, 0, 0);
    private int field_48539_e;
    private int field_48536_f;
    private int field_48537_g;
    private int field_48544_h;
    private List field_48545_i;
    private int field_48543_j;

    public Village(World par1World)
    {
        field_48539_e = 0;
        field_48536_f = 0;
        field_48537_g = 0;
        field_48544_h = 0;
        field_48545_i = new ArrayList();
        field_48543_j = 0;
        field_48542_a = par1World;
    }

    public void func_48519_a(int par1)
    {
        field_48537_g = par1;
        func_48515_k();
        func_48523_j();

        if (par1 % 20 == 0)
        {
            func_48529_i();
        }

        if (par1 % 30 == 0)
        {
            func_48535_h();
        }

        int i = field_48544_h / 16;

        if (field_48543_j < i && field_48540_b.size() > 20 && field_48542_a.rand.nextInt(7000) == 0)
        {
            Vec3D vec3d = func_48516_a(MathHelper.floor_float(field_48538_d.posX), MathHelper.floor_float(field_48538_d.posY), MathHelper.floor_float(field_48538_d.posZ), 2, 4, 2);

            if (vec3d != null)
            {
                EntityIronGolem entityirongolem = new EntityIronGolem(field_48542_a);
                entityirongolem.setPosition(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);
                field_48542_a.spawnEntityInWorld(entityirongolem);
                field_48543_j++;
            }
        }
    }

    private Vec3D func_48516_a(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        for (int i = 0; i < 10; i++)
        {
            int j = (par1 + field_48542_a.rand.nextInt(16)) - 8;
            int k = (par2 + field_48542_a.rand.nextInt(6)) - 3;
            int l = (par3 + field_48542_a.rand.nextInt(16)) - 8;

            if (func_48528_a(j, k, l) && func_48522_b(j, k, l, par4, par5, par6))
            {
                return Vec3D.createVector(j, k, l);
            }
        }

        return null;
    }

    private boolean func_48522_b(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        if (!field_48542_a.isBlockNormalCube(par1, par2 - 1, par3))
        {
            return false;
        }

        int i = par1 - par4 / 2;
        int j = par3 - par6 / 2;

        for (int k = i; k < i + par4; k++)
        {
            for (int l = par2; l < par2 + par5; l++)
            {
                for (int i1 = j; i1 < j + par6; i1++)
                {
                    if (field_48542_a.isBlockNormalCube(k, l, i1))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void func_48535_h()
    {
        List list = field_48542_a.getEntitiesWithinAABB(net.minecraft.src.EntityIronGolem.class, AxisAlignedBB.getBoundingBoxFromPool(field_48538_d.posX - field_48539_e, field_48538_d.posY - 4, field_48538_d.posZ - field_48539_e, field_48538_d.posX + field_48539_e, field_48538_d.posY + 4, field_48538_d.posZ + field_48539_e));
        field_48543_j = list.size();
    }

    private void func_48529_i()
    {
        List list = field_48542_a.getEntitiesWithinAABB(net.minecraft.src.EntityVillager.class, AxisAlignedBB.getBoundingBoxFromPool(field_48538_d.posX - field_48539_e, field_48538_d.posY - 4, field_48538_d.posZ - field_48539_e, field_48538_d.posX + field_48539_e, field_48538_d.posY + 4, field_48538_d.posZ + field_48539_e));
        field_48544_h = list.size();
    }

    public ChunkCoordinates func_48526_a()
    {
        return field_48538_d;
    }

    public int func_48527_b()
    {
        return field_48539_e;
    }

    public int func_48525_c()
    {
        return field_48540_b.size();
    }

    public int func_48520_d()
    {
        return field_48537_g - field_48536_f;
    }

    public int func_48521_e()
    {
        return field_48544_h;
    }

    public boolean func_48528_a(int par1, int par2, int par3)
    {
        return field_48538_d.func_48473_c(par1, par2, par3) < (float)(field_48539_e * field_48539_e);
    }

    public List func_48517_f()
    {
        return field_48540_b;
    }

    public VillageDoorInfo func_48533_b(int par1, int par2, int par3)
    {
        VillageDoorInfo villagedoorinfo = null;
        int i = 0x7fffffff;
        Iterator iterator = field_48540_b.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            VillageDoorInfo villagedoorinfo1 = (VillageDoorInfo)iterator.next();
            int j = villagedoorinfo1.func_48481_a(par1, par2, par3);

            if (j < i)
            {
                villagedoorinfo = villagedoorinfo1;
                i = j;
            }
        }
        while (true);

        return villagedoorinfo;
    }

    public VillageDoorInfo func_48513_c(int par1, int par2, int par3)
    {
        VillageDoorInfo villagedoorinfo = null;
        int i = 0x7fffffff;
        Iterator iterator = field_48540_b.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            VillageDoorInfo villagedoorinfo1 = (VillageDoorInfo)iterator.next();
            int j = villagedoorinfo1.func_48481_a(par1, par2, par3);

            if (j > 256)
            {
                j *= 1000;
            }
            else
            {
                j = villagedoorinfo1.func_48480_f();
            }

            if (j < i)
            {
                villagedoorinfo = villagedoorinfo1;
                i = j;
            }
        }
        while (true);

        return villagedoorinfo;
    }

    public VillageDoorInfo func_48518_d(int par1, int par2, int par3)
    {
        if (field_48538_d.func_48473_c(par1, par2, par3) > (float)(field_48539_e * field_48539_e))
        {
            return null;
        }

        for (Iterator iterator = field_48540_b.iterator(); iterator.hasNext();)
        {
            VillageDoorInfo villagedoorinfo = (VillageDoorInfo)iterator.next();

            if (villagedoorinfo.field_48493_a == par1 && villagedoorinfo.field_48492_c == par3 && Math.abs(villagedoorinfo.field_48491_b - par2) <= 1)
            {
                return villagedoorinfo;
            }
        }

        return null;
    }

    public void func_48532_a(VillageDoorInfo par1VillageDoorInfo)
    {
        field_48540_b.add(par1VillageDoorInfo);
        field_48541_c.posX += par1VillageDoorInfo.field_48493_a;
        field_48541_c.posY += par1VillageDoorInfo.field_48491_b;
        field_48541_c.posZ += par1VillageDoorInfo.field_48492_c;
        func_48531_l();
        field_48536_f = par1VillageDoorInfo.field_48487_f;
    }

    public boolean func_48524_g()
    {
        return field_48540_b.isEmpty();
    }

    public void func_48530_a(EntityLiving par1EntityLiving)
    {
        for (Iterator iterator = field_48545_i.iterator(); iterator.hasNext();)
        {
            VillageAgressor villageagressor = (VillageAgressor)iterator.next();

            if (villageagressor.field_48627_a == par1EntityLiving)
            {
                villageagressor.field_48625_b = field_48537_g;
                return;
            }
        }

        field_48545_i.add(new VillageAgressor(this, par1EntityLiving, field_48537_g));
    }

    public EntityLiving func_48534_b(EntityLiving par1EntityLiving)
    {
        double d = Double.MAX_VALUE;
        VillageAgressor villageagressor = null;

        for (int i = 0; i < field_48545_i.size(); i++)
        {
            VillageAgressor villageagressor1 = (VillageAgressor)field_48545_i.get(i);
            double d1 = villageagressor1.field_48627_a.getDistanceSqToEntity(par1EntityLiving);

            if (d1 <= d)
            {
                villageagressor = villageagressor1;
                d = d1;
            }
        }

        return villageagressor == null ? null : villageagressor.field_48627_a;
    }

    private void func_48523_j()
    {
        Iterator iterator = field_48545_i.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            VillageAgressor villageagressor = (VillageAgressor)iterator.next();

            if (!villageagressor.field_48627_a.isEntityAlive() || Math.abs(field_48537_g - villageagressor.field_48625_b) > 300)
            {
                iterator.remove();
            }
        }
        while (true);
    }

    private void func_48515_k()
    {
        boolean flag = false;
        boolean flag1 = field_48542_a.rand.nextInt(50) == 0;
        Iterator iterator = field_48540_b.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            VillageDoorInfo villagedoorinfo = (VillageDoorInfo)iterator.next();

            if (flag1)
            {
                villagedoorinfo.func_48478_d();
            }

            if (!func_48514_e(villagedoorinfo.field_48493_a, villagedoorinfo.field_48491_b, villagedoorinfo.field_48492_c) || Math.abs(field_48537_g - villagedoorinfo.field_48487_f) > 1200)
            {
                field_48541_c.posX -= villagedoorinfo.field_48493_a;
                field_48541_c.posY -= villagedoorinfo.field_48491_b;
                field_48541_c.posZ -= villagedoorinfo.field_48492_c;
                flag = true;
                villagedoorinfo.field_48488_g = true;
                iterator.remove();
            }
        }
        while (true);

        if (flag)
        {
            func_48531_l();
        }
    }

    private boolean func_48514_e(int par1, int par2, int par3)
    {
        int i = field_48542_a.getBlockId(par1, par2, par3);

        if (i <= 0)
        {
            return false;
        }
        else
        {
            return i == Block.doorWood.blockID;
        }
    }

    private void func_48531_l()
    {
        int i = field_48540_b.size();

        if (i == 0)
        {
            field_48538_d.func_48474_a(0, 0, 0);
            field_48539_e = 0;
            return;
        }

        field_48538_d.func_48474_a(field_48541_c.posX / i, field_48541_c.posY / i, field_48541_c.posZ / i);
        int j = 0;

        for (Iterator iterator = field_48540_b.iterator(); iterator.hasNext();)
        {
            VillageDoorInfo villagedoorinfo = (VillageDoorInfo)iterator.next();
            j = Math.max(villagedoorinfo.func_48481_a(field_48538_d.posX, field_48538_d.posY, field_48538_d.posZ), j);
        }

        field_48539_e = Math.max(32, (int)Math.sqrt(j));
    }
}
