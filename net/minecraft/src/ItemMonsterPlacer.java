package net.minecraft.src;

import java.util.HashMap;
import java.util.Random;

public class ItemMonsterPlacer extends Item
{
    public ItemMonsterPlacer(int par1)
    {
        super(par1);
        setHasSubtypes(true);
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
     */
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7)
    {
        if (par3World.isRemote)
        {
            return true;
        }

        int i = par3World.getBlockId(par4, par5, par6);
        par4 += Facing.offsetsXForSide[par7];
        par5 += Facing.offsetsYForSide[par7];
        par6 += Facing.offsetsZForSide[par7];
        double d = 0.0D;

        if (par7 == 1 && i == Block.fence.blockID || i == Block.netherFence.blockID)
        {
            d = 0.5D;
        }

        if (func_48390_a(par3World, par1ItemStack.getItemDamage(), (double)par4 + 0.5D, (double)par5 + d, (double)par6 + 0.5D) && !par2EntityPlayer.capabilities.isCreativeMode)
        {
            par1ItemStack.stackSize--;
        }

        return true;
    }

    public static boolean func_48390_a(World par0World, int par1, double par2, double par4, double par6)
    {
        if (!EntityList.entityEggs.containsKey(Integer.valueOf(par1)))
        {
            return false;
        }

        Entity entity = EntityList.createEntityByID(par1, par0World);

        if (entity != null)
        {
            entity.setLocationAndAngles(par2, par4, par6, par0World.rand.nextFloat() * 360F, 0.0F);
            par0World.spawnEntityInWorld(entity);
            ((EntityLiving)entity).playLivingSound();
        }

        return entity != null;
    }
}
