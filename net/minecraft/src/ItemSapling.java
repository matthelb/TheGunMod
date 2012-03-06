package net.minecraft.src;

public class ItemSapling extends ItemBlock
{
    public ItemSapling(int par1)
    {
        super(par1);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    /**
     * returns the argument if the item has metadata, 0 otherwise
     */
    public int getMetadata(int par1)
    {
        return par1;
    }
}
