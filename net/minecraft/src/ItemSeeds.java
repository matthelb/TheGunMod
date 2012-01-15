package net.minecraft.src;

public class ItemSeeds extends Item
{
    private int blockType;
    private int field_40253_bQ;

    public ItemSeeds(int i, int j, int k)
    {
        super(i);
        blockType = j;
        field_40253_bQ = k;
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        if (l != 1)
        {
            return false;
        }
        if (!entityplayer.canPlayerEdit(i, j, k) || !entityplayer.canPlayerEdit(i, j + 1, k))
        {
            return false;
        }
        int i1 = world.getBlockId(i, j, k);
        if (i1 == field_40253_bQ && world.isAirBlock(i, j + 1, k))
        {
            world.setBlockWithNotify(i, j + 1, k, blockType);
            itemstack.stackSize--;
            return true;
        }
        else
        {
            return false;
        }
    }
}
