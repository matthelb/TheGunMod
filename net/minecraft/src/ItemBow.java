package net.minecraft.src;

import java.util.Random;

public class ItemBow extends Item
{
    public ItemBow(int i)
    {
        super(i);
        maxStackSize = 1;
        setMaxDamage(384);
    }

    public void onPlayerStoppedUsing(ItemStack itemstack, World world, EntityPlayer entityplayer, int i)
    {
        boolean flag = entityplayer.capabilities.depleteBuckets || EnchantmentHelper.getEnchantmentLevel(Enchantment.field_46054_w.effectId, itemstack) > 0;
        if (flag || entityplayer.inventory.hasItemInInventory(Item.arrow.shiftedIndex))
        {
            int j = getMaxItemUseDuration(itemstack) - i;
            float f = (float)j / 20F;
            f = (f * f + f * 2.0F) / 3F;
            if ((double)f < 0.10000000000000001D)
            {
                return;
            }
            if (f > 1.0F)
            {
                f = 1.0F;
            }
            EntityArrow entityarrow = new EntityArrow(world, entityplayer, f * 2.0F);
            if (f == 1.0F)
            {
                entityarrow.arrowCritical = true;
            }
            int k = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_46057_t.effectId, itemstack);
            if (k > 0)
            {
                entityarrow.func_46008_a(entityarrow.func_46009_j() + (double)k * 0.5D + 0.5D);
            }
            int l = EnchantmentHelper.getEnchantmentLevel(Enchantment.field_46056_u.effectId, itemstack);
            if (l > 0)
            {
                entityarrow.func_46007_b(l);
            }
            if (EnchantmentHelper.getEnchantmentLevel(Enchantment.field_46055_v.effectId, itemstack) > 0)
            {
                entityarrow.setFire(100);
            }
            itemstack.damageItem(1, entityplayer);
            world.playSoundAtEntity(entityplayer, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
            if (!flag)
            {
                entityplayer.inventory.consumeInventoryItem(Item.arrow.shiftedIndex);
            }
            else
            {
                entityarrow.doesArrowBelongToPlayer = false;
            }
            if (!world.singleplayerWorld)
            {
                world.spawnEntityInWorld(entityarrow);
            }
        }
    }

    public ItemStack onFoodEaten(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        return itemstack;
    }

    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 0x11940;
    }

    public EnumAction getAction(ItemStack itemstack)
    {
        return EnumAction.bow;
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {
        if (entityplayer.capabilities.depleteBuckets || entityplayer.inventory.hasItemInInventory(Item.arrow.shiftedIndex))
        {
            entityplayer.setItemInUse(itemstack, getMaxItemUseDuration(itemstack));
        }
        return itemstack;
    }

    public int getItemEnchantability()
    {
        return 1;
    }
}
