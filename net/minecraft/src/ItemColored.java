package net.minecraft.src;

public class ItemColored extends ItemBlock
{
    private final Block field_35421_a;
    private String field_41041_b[];

    public ItemColored(int i, boolean flag)
    {
        super(i);
        field_35421_a = Block.blocksList[getBlockID()];
        if (flag)
        {
            setMaxDamage(0);
            setHasSubtypes(true);
        }
    }

    public int getMetadata(int i)
    {
        return i;
    }

    public ItemColored setBlockNames(String as[])
    {
        field_41041_b = as;
        return this;
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        if (field_41041_b == null)
        {
            return super.getItemNameIS(itemstack);
        }
        int i = itemstack.getItemDamage();
        if (i >= 0 && i < field_41041_b.length)
        {
            return (new StringBuilder()).append(super.getItemNameIS(itemstack)).append(".").append(field_41041_b[i]).toString();
        }
        else
        {
            return super.getItemNameIS(itemstack);
        }
    }
}
