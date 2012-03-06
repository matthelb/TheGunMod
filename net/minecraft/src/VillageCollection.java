package net.minecraft.src;

import java.util.*;

public class VillageCollection
{
    private World field_48644_a;
    private final List field_48642_b = new ArrayList();
    private final List field_48643_c = new ArrayList();
    private final List field_48640_d = new ArrayList();
    private int field_48641_e;

    public VillageCollection(World par1World)
    {
        field_48641_e = 0;
        field_48644_a = par1World;
    }

    public void func_48639_a(int par1, int par2, int par3)
    {
        if (field_48642_b.size() > 64)
        {
            return;
        }

        if (!func_48636_d(par1, par2, par3))
        {
            field_48642_b.add(new ChunkCoordinates(par1, par2, par3));
        }
    }

    public void func_48633_a()
    {
        field_48641_e++;
        Village village;

        for (Iterator iterator = field_48640_d.iterator(); iterator.hasNext(); village.func_48519_a(field_48641_e))
        {
            village = (Village)iterator.next();
        }

        func_48637_c();
        func_48631_d();
        func_48634_e();
    }

    private void func_48637_c()
    {
        Iterator iterator = field_48640_d.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            Village village = (Village)iterator.next();

            if (village.func_48524_g())
            {
                iterator.remove();
            }
        }
        while (true);
    }

    public List func_48628_b()
    {
        return field_48640_d;
    }

    public Village func_48632_a(int par1, int par2, int par3, int par4)
    {
        Village village = null;
        float f = 3.402823E+038F;
        Iterator iterator = field_48640_d.iterator();

        do
        {
            if (!iterator.hasNext())
            {
                break;
            }

            Village village1 = (Village)iterator.next();
            float f1 = village1.func_48526_a().func_48473_c(par1, par2, par3);

            if (f1 < f)
            {
                int i = par4 + village1.func_48527_b();

                if (f1 <= (float)(i * i))
                {
                    village = village1;
                    f = f1;
                }
            }
        }
        while (true);

        return village;
    }

    private void func_48631_d()
    {
        if (field_48642_b.isEmpty())
        {
            return;
        }
        else
        {
            func_48635_a((ChunkCoordinates)field_48642_b.remove(0));
            return;
        }
    }

    private void func_48634_e()
    {
        for (int i = 0; i < field_48643_c.size(); i++)
        {
            VillageDoorInfo villagedoorinfo = (VillageDoorInfo)field_48643_c.get(i);
            boolean flag = false;
            Iterator iterator = field_48640_d.iterator();

            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }

                Village village1 = (Village)iterator.next();
                int j = (int)village1.func_48526_a().getSqDistanceTo(villagedoorinfo.field_48493_a, villagedoorinfo.field_48491_b, villagedoorinfo.field_48492_c);

                if (j > 32 + village1.func_48527_b())
                {
                    continue;
                }

                village1.func_48532_a(villagedoorinfo);
                flag = true;
                break;
            }
            while (true);

            if (!flag)
            {
                Village village = new Village(field_48644_a);
                village.func_48532_a(villagedoorinfo);
                field_48640_d.add(village);
            }
        }

        field_48643_c.clear();
    }

    private void func_48635_a(ChunkCoordinates par1ChunkCoordinates)
    {
        byte byte0 = 16;
        byte byte1 = 4;
        byte byte2 = 16;

        for (int i = par1ChunkCoordinates.posX - byte0; i < par1ChunkCoordinates.posX + byte0; i++)
        {
            for (int j = par1ChunkCoordinates.posY - byte1; j < par1ChunkCoordinates.posY + byte1; j++)
            {
                for (int k = par1ChunkCoordinates.posZ - byte2; k < par1ChunkCoordinates.posZ + byte2; k++)
                {
                    if (!func_48629_e(i, j, k))
                    {
                        continue;
                    }

                    VillageDoorInfo villagedoorinfo = func_48638_b(i, j, k);

                    if (villagedoorinfo == null)
                    {
                        func_48630_c(i, j, k);
                    }
                    else
                    {
                        villagedoorinfo.field_48487_f = field_48641_e;
                    }
                }
            }
        }
    }

    private VillageDoorInfo func_48638_b(int par1, int par2, int par3)
    {
        for (Iterator iterator = field_48643_c.iterator(); iterator.hasNext();)
        {
            VillageDoorInfo villagedoorinfo = (VillageDoorInfo)iterator.next();

            if (villagedoorinfo.field_48493_a == par1 && villagedoorinfo.field_48492_c == par3 && Math.abs(villagedoorinfo.field_48491_b - par2) <= 1)
            {
                return villagedoorinfo;
            }
        }

        for (Iterator iterator1 = field_48640_d.iterator(); iterator1.hasNext();)
        {
            Village village = (Village)iterator1.next();
            VillageDoorInfo villagedoorinfo1 = village.func_48518_d(par1, par2, par3);

            if (villagedoorinfo1 != null)
            {
                return villagedoorinfo1;
            }
        }

        return null;
    }

    private void func_48630_c(int par1, int par2, int par3)
    {
        int i = ((BlockDoor)Block.doorWood).func_48136_c(field_48644_a, par1, par2, par3);

        if (i == 0 || i == 2)
        {
            int j = 0;

            for (int l = -5; l < 0; l++)
            {
                if (field_48644_a.canBlockSeeTheSky(par1 + l, par2, par3))
                {
                    j--;
                }
            }

            for (int i1 = 1; i1 <= 5; i1++)
            {
                if (field_48644_a.canBlockSeeTheSky(par1 + i1, par2, par3))
                {
                    j++;
                }
            }

            if (j != 0)
            {
                field_48643_c.add(new VillageDoorInfo(par1, par2, par3, j <= 0 ? 2 : -2, 0, field_48641_e));
            }
        }
        else
        {
            int k = 0;

            for (int j1 = -5; j1 < 0; j1++)
            {
                if (field_48644_a.canBlockSeeTheSky(par1, par2, par3 + j1))
                {
                    k--;
                }
            }

            for (int k1 = 1; k1 <= 5; k1++)
            {
                if (field_48644_a.canBlockSeeTheSky(par1, par2, par3 + k1))
                {
                    k++;
                }
            }

            if (k != 0)
            {
                field_48643_c.add(new VillageDoorInfo(par1, par2, par3, 0, k <= 0 ? 2 : -2, field_48641_e));
            }
        }
    }

    private boolean func_48636_d(int par1, int par2, int par3)
    {
        for (Iterator iterator = field_48642_b.iterator(); iterator.hasNext();)
        {
            ChunkCoordinates chunkcoordinates = (ChunkCoordinates)iterator.next();

            if (chunkcoordinates.posX == par1 && chunkcoordinates.posY == par2 && chunkcoordinates.posZ == par3)
            {
                return true;
            }
        }

        return false;
    }

    private boolean func_48629_e(int par1, int par2, int par3)
    {
        int i = field_48644_a.getBlockId(par1, par2, par3);
        return i == Block.doorWood.blockID;
    }
}
