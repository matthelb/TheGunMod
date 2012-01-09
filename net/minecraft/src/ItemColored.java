// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;


// Referenced classes of package net.minecraft.src:
//            ItemBlock, Block, ItemStack

public class ItemColored extends ItemBlock
{

    private final Block blockRef;
    private String blockNames[];

    public ItemColored(int i, boolean flag)
    {
        super(i);
        blockRef = Block.blocksList[getBlockID()];
        if(flag)
        {
            setMaxDamage(0);
            setHasSubtypes(true);
        }
    }

    public int getColorFromDamage(int i)
    {
        return blockRef.getRenderColor(i);
    }

    public int getIconFromDamage(int i)
    {
        return blockRef.getBlockTextureFromSideAndMetadata(0, i);
    }

    public int getPlacedBlockMetadata(int i)
    {
        return i;
    }

    public ItemColored setBlockNames(String as[])
    {
        blockNames = as;
        return this;
    }

    public String getItemNameIS(ItemStack itemstack)
    {
        if(blockNames == null)
        {
            return super.getItemNameIS(itemstack);
        }
        int i = itemstack.getItemDamage();
        if(i >= 0 && i < blockNames.length)
        {
            return (new StringBuilder()).append(super.getItemNameIS(itemstack)).append(".").append(blockNames[i]).toString();
        } else
        {
            return super.getItemNameIS(itemstack);
        }
    }
}
