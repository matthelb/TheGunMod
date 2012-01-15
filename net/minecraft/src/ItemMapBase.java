package net.minecraft.src;

public class ItemMapBase extends Item
{
    protected ItemMapBase(int i)
    {
        super(i);
    }

    public boolean func_28019_b()
    {
        return true;
    }

    public Packet getUpdatePacket(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        return null;
    }
}
