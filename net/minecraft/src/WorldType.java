package net.minecraft.src;

public class WorldType
{
    public static final WorldType field_48459_a[] = new WorldType[16];
    public static final WorldType field_48457_b = (new WorldType(0, "default", 1)).func_48448_d();
    public static final WorldType field_48458_c = new WorldType(1, "flat");
    public static final WorldType field_48456_d = (new WorldType(8, "default_1_1", 0)).func_48450_a(false);
    private final String worldType;
    private final int field_48454_f;
    private boolean field_48455_g;
    private boolean field_48460_h;

    private WorldType(int par1, String par2Str)
    {
        this(par1, par2Str, 0);
    }

    private WorldType(int par1, String par2Str, int par3)
    {
        worldType = par2Str;
        field_48454_f = par3;
        field_48455_g = true;
        field_48459_a[par1] = this;
    }

    public String func_48449_a()
    {
        return worldType;
    }

    public int func_48452_b()
    {
        return field_48454_f;
    }

    public WorldType func_48451_a(int par1)
    {
        if (this == field_48457_b && par1 == 0)
        {
            return field_48456_d;
        }
        else
        {
            return this;
        }
    }

    private WorldType func_48450_a(boolean par1)
    {
        field_48455_g = par1;
        return this;
    }

    private WorldType func_48448_d()
    {
        field_48460_h = true;
        return this;
    }

    public boolean func_48453_c()
    {
        return field_48460_h;
    }

    public static WorldType parseWorldType(String par0Str)
    {
        for (int i = 0; i < field_48459_a.length; i++)
        {
            if (field_48459_a[i] != null && field_48459_a[i].worldType.equalsIgnoreCase(par0Str))
            {
                return field_48459_a[i];
            }
        }

        return null;
    }
}
