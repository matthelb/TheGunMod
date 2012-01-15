package net.minecraft.src;

public class ItemMetadata extends ItemBlock
{
    private Block field_35420_a;

    public ItemMetadata(int i, Block block)
    {
        super(i);
        field_35420_a = block;
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public int getMetadata(int i)
    {
        return i;
    }
}
