package net.minecraft.src;

public class SpawnListEntry extends WeightedRandomChoice
{
    public Class entityClass;
    public int field_35484_b;
    public int field_35485_c;

    public SpawnListEntry(Class class1, int i, int j, int k)
    {
        super(i);
        entityClass = class1;
        field_35484_b = j;
        field_35485_c = k;
    }
}
