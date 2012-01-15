package net.minecraft.src;

public class ItemLeaves extends ItemBlock
{
    public ItemLeaves(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public int getMetadata(int i)
    {
        return i | 4;
    }
}
