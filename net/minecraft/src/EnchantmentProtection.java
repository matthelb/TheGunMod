package net.minecraft.src;

public class EnchantmentProtection extends Enchantment
{
    private static final String protectionName[] =
    {
        "all", "fire", "fall", "explosion", "projectile"
    };
    private static final int baseEnchantability[] =
    {
        1, 10, 5, 5, 3
    };
    private static final int levelEnchantability[] =
    {
        16, 8, 6, 8, 6
    };
    private static final int thresholdEnchantability[] =
    {
        20, 12, 10, 12, 15
    };
    public final int protectionType;

    public EnchantmentProtection(int par1, int par2, int par3)
    {
        super(par1, par2, EnumEnchantmentType.armor);
        protectionType = par3;

        if (par3 == 2)
        {
            type = EnumEnchantmentType.armor_feet;
        }
    }

    public int getMinEnchantability(int par1)
    {
        return baseEnchantability[protectionType] + (par1 - 1) * levelEnchantability[protectionType];
    }

    public int getMaxEnchantability(int par1)
    {
        return getMinEnchantability(par1) + thresholdEnchantability[protectionType];
    }

    public int getMaxLevel()
    {
        return 4;
    }

    public int calcModifierDamage(int par1, DamageSource par2DamageSource)
    {
        if (par2DamageSource.canHarmInCreative())
        {
            return 0;
        }

        int i = (6 + par1 * par1) / 2;

        if (protectionType == 0)
        {
            return i;
        }

        if (protectionType == 1 && par2DamageSource.fireDamage())
        {
            return i;
        }

        if (protectionType == 2 && par2DamageSource == DamageSource.fall)
        {
            return i * 2;
        }

        if (protectionType == 3 && par2DamageSource == DamageSource.explosion)
        {
            return i;
        }

        if (protectionType == 4 && par2DamageSource.isProjectile())
        {
            return i;
        }
        else
        {
            return 0;
        }
    }

    public boolean canApplyTogether(Enchantment par1Enchantment)
    {
        if (par1Enchantment instanceof EnchantmentProtection)
        {
            EnchantmentProtection enchantmentprotection = (EnchantmentProtection)par1Enchantment;

            if (enchantmentprotection.protectionType == protectionType)
            {
                return false;
            }

            return protectionType == 2 || enchantmentprotection.protectionType == 2;
        }
        else
        {
            return super.canApplyTogether(par1Enchantment);
        }
    }
}
