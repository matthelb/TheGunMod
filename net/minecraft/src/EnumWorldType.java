package net.minecraft.src;

public enum EnumWorldType
{
    DEFAULT("DEFAULT", 0, "default"),
    FLAT("FLAT", 1, "flat");

    private String field_46052_c;
    private static final EnumWorldType field_46050_d[] = (new EnumWorldType[] {
        DEFAULT, FLAT
    });

    private EnumWorldType(String s, int i, String s1)
    {
        field_46052_c = s1;
    }

    public static EnumWorldType func_46049_a(String s)
    {
        EnumWorldType aenumworldtype[] = values();
        int i = aenumworldtype.length;
        for (int j = 0; j < i; j++)
        {
            EnumWorldType enumworldtype = aenumworldtype[j];
            if (enumworldtype.name().equals(s))
            {
                return enumworldtype;
            }
        }

        return null;
    }
}
