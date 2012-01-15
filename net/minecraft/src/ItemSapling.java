package net.minecraft.src;

public class ItemSapling extends ItemBlock
{
    public ItemSapling(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public int getMetadata(int i)
    {
        return i;
    }
}
