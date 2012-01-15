package net.minecraft.src;

import java.util.Random;

public class BlockLadder extends Block
{
    protected BlockLadder(int i, int j)
    {
        super(i, j, Material.circuits);
    }

    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int i, int j, int k)
    {
        return null;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int getRenderType()
    {
        return 8;
    }

    public boolean canPlaceBlockAt(World world, int i, int j, int k)
    {
        if (world.isBlockNormalCube(i - 1, j, k))
        {
            return true;
        }
        if (world.isBlockNormalCube(i + 1, j, k))
        {
            return true;
        }
        if (world.isBlockNormalCube(i, j, k - 1))
        {
            return true;
        }
        return world.isBlockNormalCube(i, j, k + 1);
    }

    public void onBlockPlaced(World world, int i, int j, int k, int l)
    {
        int i1 = world.getBlockMetadata(i, j, k);
        if ((i1 == 0 || l == 2) && world.isBlockNormalCube(i, j, k + 1))
        {
            i1 = 2;
        }
        if ((i1 == 0 || l == 3) && world.isBlockNormalCube(i, j, k - 1))
        {
            i1 = 3;
        }
        if ((i1 == 0 || l == 4) && world.isBlockNormalCube(i + 1, j, k))
        {
            i1 = 4;
        }
        if ((i1 == 0 || l == 5) && world.isBlockNormalCube(i - 1, j, k))
        {
            i1 = 5;
        }
        world.setBlockMetadataWithNotify(i, j, k, i1);
    }

    public void onNeighborBlockChange(World world, int i, int j, int k, int l)
    {
        int i1 = world.getBlockMetadata(i, j, k);
        boolean flag = false;
        if (i1 == 2 && world.isBlockNormalCube(i, j, k + 1))
        {
            flag = true;
        }
        if (i1 == 3 && world.isBlockNormalCube(i, j, k - 1))
        {
            flag = true;
        }
        if (i1 == 4 && world.isBlockNormalCube(i + 1, j, k))
        {
            flag = true;
        }
        if (i1 == 5 && world.isBlockNormalCube(i - 1, j, k))
        {
            flag = true;
        }
        if (!flag)
        {
            dropBlockAsItem(world, i, j, k, i1, 0);
            world.setBlockWithNotify(i, j, k, 0);
        }
        super.onNeighborBlockChange(world, i, j, k, l);
    }

    public int quantityDropped(Random random)
    {
        return 1;
    }
}
