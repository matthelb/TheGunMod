package net.minecraft.src;

public class ExtendedBlockStorage
{
    private int field_48615_a;
    private int field_48613_b;
    private int field_48614_c;
    private byte field_48611_d[];
    private NibbleArray field_48612_e;
    private NibbleArray field_48609_f;
    private NibbleArray field_48610_g;
    private NibbleArray field_48616_h;

    public ExtendedBlockStorage(int par1)
    {
        field_48615_a = par1;
        field_48611_d = new byte[4096];
        field_48609_f = new NibbleArray(field_48611_d.length, 4);
        field_48616_h = new NibbleArray(field_48611_d.length, 4);
        field_48610_g = new NibbleArray(field_48611_d.length, 4);
    }

    public int func_48591_a(int par1, int par2, int par3)
    {
        int i = field_48611_d[par2 << 8 | par3 << 4 | par1] & 0xff;

        if (field_48612_e != null)
        {
            return field_48612_e.get(par1, par2, par3) << 8 | i;
        }
        else
        {
            return i;
        }
    }

    public void func_48588_a(int par1, int par2, int par3, int par4)
    {
        int i = field_48611_d[par2 << 8 | par3 << 4 | par1] & 0xff;

        if (field_48612_e != null)
        {
            i = field_48612_e.get(par1, par2, par3) << 8 | i;
        }

        if (i == 0 && par4 != 0)
        {
            field_48613_b++;

            if (Block.blocksList[par4] != null && Block.blocksList[par4].func_48125_m())
            {
                field_48614_c++;
            }
        }
        else if (i != 0 && par4 == 0)
        {
            field_48613_b--;

            if (Block.blocksList[i] != null && Block.blocksList[i].func_48125_m())
            {
                field_48614_c--;
            }
        }
        else if (Block.blocksList[i] != null && Block.blocksList[i].func_48125_m() && (Block.blocksList[par4] == null || !Block.blocksList[par4].func_48125_m()))
        {
            field_48614_c--;
        }
        else if ((Block.blocksList[i] == null || !Block.blocksList[i].func_48125_m()) && Block.blocksList[par4] != null && Block.blocksList[par4].func_48125_m())
        {
            field_48614_c++;
        }

        field_48611_d[par2 << 8 | par3 << 4 | par1] = (byte)(par4 & 0xff);

        if (par4 > 255)
        {
            if (field_48612_e == null)
            {
                field_48612_e = new NibbleArray(field_48611_d.length, 4);
            }

            field_48612_e.set(par1, par2, par3, (par4 & 0xf00) >> 8);
        }
        else if (field_48612_e != null)
        {
            field_48612_e.set(par1, par2, par3, 0);
        }
    }

    public int func_48598_b(int par1, int par2, int par3)
    {
        return field_48609_f.get(par1, par2, par3);
    }

    public void func_48585_b(int par1, int par2, int par3, int par4)
    {
        field_48609_f.set(par1, par2, par3, par4);
    }

    public boolean func_48595_a()
    {
        return field_48613_b == 0;
    }

    public boolean func_48607_b()
    {
        return field_48614_c > 0;
    }

    public int func_48597_c()
    {
        return field_48615_a;
    }

    public void func_48592_c(int par1, int par2, int par3, int par4)
    {
        field_48616_h.set(par1, par2, par3, par4);
    }

    public int func_48602_c(int par1, int par2, int par3)
    {
        return field_48616_h.get(par1, par2, par3);
    }

    public void func_48608_d(int par1, int par2, int par3, int par4)
    {
        field_48610_g.set(par1, par2, par3, par4);
    }

    public int func_48604_d(int par1, int par2, int par3)
    {
        return field_48610_g.get(par1, par2, par3);
    }

    public void func_48599_d()
    {
        field_48613_b = 0;
        field_48614_c = 0;

        for (int i = 0; i < 16; i++)
        {
            for (int j = 0; j < 16; j++)
            {
                for (int k = 0; k < 16; k++)
                {
                    int l = func_48591_a(i, j, k);

                    if (l <= 0)
                    {
                        continue;
                    }

                    if (Block.blocksList[l] == null)
                    {
                        field_48611_d[j << 8 | k << 4 | i] = 0;

                        if (field_48612_e != null)
                        {
                            field_48612_e.set(i, j, k, 0);
                        }

                        continue;
                    }

                    field_48613_b++;

                    if (Block.blocksList[l].func_48125_m())
                    {
                        field_48614_c++;
                    }
                }
            }
        }
    }

    public void func_48603_e()
    {
    }

    public int func_48587_f()
    {
        return field_48613_b;
    }

    public byte[] func_48590_g()
    {
        return field_48611_d;
    }

    public NibbleArray func_48601_h()
    {
        return field_48612_e;
    }

    public NibbleArray func_48594_i()
    {
        return field_48609_f;
    }

    public NibbleArray func_48600_j()
    {
        return field_48610_g;
    }

    public NibbleArray func_48605_k()
    {
        return field_48616_h;
    }

    public void func_48596_a(byte par1ArrayOfByte[])
    {
        field_48611_d = par1ArrayOfByte;
    }

    public void func_48593_a(NibbleArray par1NibbleArray)
    {
        field_48612_e = par1NibbleArray;
    }

    public void func_48586_b(NibbleArray par1NibbleArray)
    {
        field_48609_f = par1NibbleArray;
    }

    public void func_48606_c(NibbleArray par1NibbleArray)
    {
        field_48610_g = par1NibbleArray;
    }

    public void func_48589_d(NibbleArray par1NibbleArray)
    {
        field_48616_h = par1NibbleArray;
    }
}
