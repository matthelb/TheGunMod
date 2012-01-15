package net.minecraft.src;

import java.util.Random;

public class BlockLockedChest extends Block
{
    protected BlockLockedChest(int i)
    {
        super(i, Material.wood);
        blockIndexInTexture = 26;
    }

    public int getBlockTextureFromSide(int i)
    {
        if (i == 1)
        {
            return blockIndexInTexture - 1;
        }
        if (i == 0)
        {
            return blockIndexInTexture - 1;
        }
        if (i == 3)
        {
            return blockIndexInTexture + 1;
        }
        else
        {
            return blockIndexInTexture;
        }
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        return true;
    }

    public void updateTick(World world, int i, int j, int k, Random random)
    {
        world.setBlockWithNotify(i, j, k, 0);
    }
}
