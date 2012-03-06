package net.minecraft.src;

import java.util.List;

public class Achievement extends StatBase
{
    public final int displayColumn;
    public final int displayRow;
    public final Achievement parentAchievement;
    private final String achievementDescription;
    public final ItemStack theItemStack;
    private boolean isSpecial;

    public Achievement(int par1, String par2Str, int par3, int par4, Item par5Item, Achievement par6Achievement)
    {
        this(par1, par2Str, par3, par4, new ItemStack(par5Item), par6Achievement);
    }

    public Achievement(int par1, String par2Str, int par3, int par4, Block par5Block, Achievement par6Achievement)
    {
        this(par1, par2Str, par3, par4, new ItemStack(par5Block), par6Achievement);
    }

    public Achievement(int par1, String par2Str, int par3, int par4, ItemStack par5ItemStack, Achievement par6Achievement)
    {
        super(0x500000 + par1, (new StringBuilder()).append("achievement.").append(par2Str).toString());
        theItemStack = par5ItemStack;
        achievementDescription = (new StringBuilder()).append("achievement.").append(par2Str).append(".desc").toString();
        displayColumn = par3;
        displayRow = par4;

        if (par3 < AchievementList.minDisplayColumn)
        {
            AchievementList.minDisplayColumn = par3;
        }

        if (par4 < AchievementList.minDisplayRow)
        {
            AchievementList.minDisplayRow = par4;
        }

        if (par3 > AchievementList.maxDisplayColumn)
        {
            AchievementList.maxDisplayColumn = par3;
        }

        if (par4 > AchievementList.maxDisplayRow)
        {
            AchievementList.maxDisplayRow = par4;
        }

        parentAchievement = par6Achievement;
    }

    public Achievement setIndependent()
    {
        isIndependent = true;
        return this;
    }

    public Achievement setSpecial()
    {
        isSpecial = true;
        return this;
    }

    public Achievement registerAchievement()
    {
        super.registerStat();
        AchievementList.achievementList.add(this);
        return this;
    }

    public StatBase registerStat()
    {
        return registerAchievement();
    }

    public StatBase initIndependentStat()
    {
        return setIndependent();
    }
}
