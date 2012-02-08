package net.minecraft.src;

public class ItemRecord extends Item
{
    public final String recordName;

    protected ItemRecord(int i, String s)
    {
        super(i);
        recordName = s;
        maxStackSize = 1;
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        if (world.getBlockId(i, j, k) == Block.jukebox.blockID && world.getBlockMetadata(i, j, k) == 0)
        {
            if (world.isRemote)
            {
                return true;
            }
            else
            {
                ((BlockJukeBox)Block.jukebox).insertRecord(world, i, j, k, shiftedIndex);
                world.playAuxSFXAtEntity(null, 1005, i, j, k, shiftedIndex);
                itemstack.stackSize--;
                return true;
            }
        }
        else
        {
            return false;
        }
    }
}
