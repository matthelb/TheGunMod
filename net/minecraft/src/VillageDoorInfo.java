package net.minecraft.src;

public class VillageDoorInfo
{
    public final int field_48493_a;
    public final int field_48491_b;
    public final int field_48492_c;
    public final int field_48489_d;
    public final int field_48490_e;
    public int field_48487_f;
    public boolean field_48488_g;
    private int field_48494_h;

    public VillageDoorInfo(int par1, int par2, int par3, int par4, int par5, int par6)
    {
        field_48488_g = false;
        field_48494_h = 0;
        field_48493_a = par1;
        field_48491_b = par2;
        field_48492_c = par3;
        field_48489_d = par4;
        field_48490_e = par5;
        field_48487_f = par6;
    }

    public int func_48481_a(int par1, int par2, int par3)
    {
        int i = par1 - field_48493_a;
        int j = par2 - field_48491_b;
        int k = par3 - field_48492_c;
        return i * i + j * j + k * k;
    }

    public int func_48486_b(int par1, int par2, int par3)
    {
        int i = par1 - field_48493_a - field_48489_d;
        int j = par2 - field_48491_b;
        int k = par3 - field_48492_c - field_48490_e;
        return i * i + j * j + k * k;
    }

    public int func_48483_a()
    {
        return field_48493_a + field_48489_d;
    }

    public int func_48485_b()
    {
        return field_48491_b;
    }

    public int func_48484_c()
    {
        return field_48492_c + field_48490_e;
    }

    public boolean func_48479_a(int par1, int par2)
    {
        int i = par1 - field_48493_a;
        int j = par2 - field_48492_c;
        return i * field_48489_d + j * field_48490_e >= 0;
    }

    public void func_48478_d()
    {
        field_48494_h = 0;
    }

    public void func_48482_e()
    {
        field_48494_h++;
    }

    public int func_48480_f()
    {
        return field_48494_h;
    }
}
