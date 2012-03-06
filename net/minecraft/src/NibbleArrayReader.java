package net.minecraft.src;

public class NibbleArrayReader
{
    public final byte field_48621_a[];
    private final int field_48619_b;
    private final int field_48620_c;

    public NibbleArrayReader(byte par1ArrayOfByte[], int par2)
    {
        field_48621_a = par1ArrayOfByte;
        field_48619_b = par2;
        field_48620_c = par2 + 4;
    }

    public int func_48618_a(int par1, int par2, int par3)
    {
        int i = par1 << field_48620_c | par3 << field_48619_b | par2;
        int j = i >> 1;
        int k = i & 1;

        if (k == 0)
        {
            return field_48621_a[j] & 0xf;
        }
        else
        {
            return field_48621_a[j] >> 4 & 0xf;
        }
    }
}
