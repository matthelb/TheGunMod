package net.minecraft.src;

public class ItemMonsterPlacer extends Item
{
    public ItemMonsterPlacer(int i)
    {
        super(i);
        setMaxStackSize(1);
        setHasSubtypes(true);
    }

    public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int i, int j, int k, int l)
    {
        if (world.singleplayerWorld)
        {
            return true;
        }
        i += Facing.offsetsXForSide[l];
        j += Facing.offsetsYForSide[l];
        k += Facing.offsetsZForSide[l];
        Entity entity = EntityList.func_44014_a(itemstack.getItemDamage(), world);
        if (entity != null)
        {
            if (!entityplayer.capabilities.depleteBuckets)
            {
                itemstack.stackSize--;
            }
            entity.setLocationAndAngles((double)i + 0.5D, j, (double)k + 0.5D, 0.0F, 0.0F);
            world.spawnEntityInWorld(entity);
        }
        return true;
    }
}
