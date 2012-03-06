package net.minecraft.src;

public class EnchantmentDamage extends Enchantment
{
    private static final String protectionName[] =
    {
        "all", "undead", "arthropods"
    };
    private static final int baseEnchantability[] =
    {
        1, 5, 5
    };
    private static final int levelEnchantability[] =
    {
        16, 8, 8
    };
    private static final int thresholdEnchantability[] =
    {
        20, 20, 20
    };
    public final int damageType;

    public EnchantmentDamage(int par1, int par2, int par3)
    {
        super(par1, par2, EnumEnchantmentType.weapon);
        damageType = par3;
    }

    public int getMinEnchantability(int par1)
    {
        return baseEnchantability[damageType] + (par1 - 1) * levelEnchantability[damageType];
    }

    public int getMaxEnchantability(int par1)
    {
        return getMinEnchantability(par1) + thresholdEnchantability[damageType];
    }

    public int getMaxLevel()
    {
        return 5;
    }

    public int calcModifierLiving(int par1, EntityLiving par2EntityLiving)
    {
        if (damageType == 0)
        {
            return par1 * 3;
        }

        if (damageType == 1 && par2EntityLiving.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)
        {
            return par1 * 4;
        }

        if (damageType == 2 && par2EntityLiving.getCreatureAttribute() == EnumCreatureAttribute.ARTHROPOD)
        {
            return par1 * 4;
        }
        else
        {
            return 0;
        }
    }

    public boolean canApplyTogether(Enchantment par1Enchantment)
    {
        return !(par1Enchantment instanceof EnchantmentDamage);
    }
}
