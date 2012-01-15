package net.minecraft.src;

public class ItemCloth extends ItemBlock
{
    public ItemCloth(int i)
    {
        super(i);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    public int getMetadata(int i)
    {
        return i;
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        return (new StringBuilder()).append(super.getItemName()).append(".").append(ItemDye.dyeColorNames[BlockCloth.getBlockFromDye(itemstack.getItemDamage())]).toString();
    }
}
