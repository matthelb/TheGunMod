package net.minecraft.src;

public class EnchantmentWaterWorker extends Enchantment
{
    public EnchantmentWaterWorker(int par1, int par2)
    {
        super(par1, par2, EnumEnchantmentType.armor_head);
        setName("waterWorker");
    }

    public int getMinEnchantability(int par1)
    {
        return 1;
    }

    public int getMaxEnchantability(int par1)
    {
        return getMinEnchantability(par1) + 40;
    }

    public int getMaxLevel()
    {
        return 1;
    }
}
