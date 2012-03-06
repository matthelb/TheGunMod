package net.minecraft.src;

final class EnchantmentModifierLiving implements IEnchantmentModifier
{
    public int livingModifier;
    public EntityLiving entityLiving;

    private EnchantmentModifierLiving()
    {
    }

    public void calculateModifier(Enchantment par1Enchantment, int par2)
    {
        livingModifier += par1Enchantment.calcModifierLiving(par2, entityLiving);
    }

    EnchantmentModifierLiving(Empty3 par1Empty3)
    {
        this();
    }
}
