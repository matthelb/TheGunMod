package net.minecraft.src;

import java.util.Random;

public class BlockFlowing extends BlockFluid
{
    int numAdjacentSources;
    boolean isOptimalFlowDirection[];
    int flowCost[];

    protected BlockFlowing(int par1, Material par2Material)
    {
        super(par1, par2Material);
        numAdjacentSources = 0;
        isOptimalFlowDirection = new boolean[4];
        flowCost = new int[4];
    }

    /**
     * Updates the flow for the BlockFlowing object.
     */
    private void updateFlow(World par1World, int par2, int par3, int par4)
    {
        int i = par1World.getBlockMetadata(par2, par3, par4);
        par1World.setBlockAndMetadata(par2, par3, par4, blockID + 1, i);
        par1World.markBlocksDirty(par2, par3, par4, par2, par3, par4);
        par1World.markBlockNeedsUpdate(par2, par3, par4);
    }

    public boolean func_48127_b(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        return blockMaterial != Material.lava;
    }

    /**
     * Ticks the block if it's been scheduled
     */
    public void updateTick(World par1World, int par2, int par3, int par4, Random par5Random)
    {
        int i = getFlowDecay(par1World, par2, par3, par4);
        byte byte0 = 1;

        if (blockMaterial == Material.lava && !par1World.worldProvider.isHellWorld)
        {
            byte0 = 2;
        }

        boolean flag = true;

        if (i > 0)
        {
            int j = -100;
            numAdjacentSources = 0;
            j = getSmallestFlowDecay(par1World, par2 - 1, par3, par4, j);
            j = getSmallestFlowDecay(par1World, par2 + 1, par3, par4, j);
            j = getSmallestFlowDecay(par1World, par2, par3, par4 - 1, j);
            j = getSmallestFlowDecay(par1World, par2, par3, par4 + 1, j);
            int k = j + byte0;

            if (k >= 8 || j < 0)
            {
                k = -1;
            }

            if (getFlowDecay(par1World, par2, par3 + 1, par4) >= 0)
            {
                int i1 = getFlowDecay(par1World, par2, par3 + 1, par4);

                if (i1 >= 8)
                {
                    k = i1;
                }
                else
                {
                    k = i1 + 8;
                }
            }

            if (numAdjacentSources >= 2 && blockMaterial == Material.water)
            {
                if (par1World.getBlockMaterial(par2, par3 - 1, par4).isSolid())
                {
                    k = 0;
                }
                else if (par1World.getBlockMaterial(par2, par3 - 1, par4) == blockMaterial && par1World.getBlockMetadata(par2, par3, par4) == 0)
                {
                    k = 0;
                }
            }

            if (blockMaterial == Material.lava && i < 8 && k < 8 && k > i && par5Random.nextInt(4) != 0)
            {
                k = i;
                flag = false;
            }

            if (k != i)
            {
                i = k;

                if (i < 0)
                {
                    par1World.setBlockWithNotify(par2, par3, par4, 0);
                }
                else
                {
                    par1World.setBlockMetadataWithNotify(par2, par3, par4, i);
                    par1World.scheduleBlockUpdate(par2, par3, par4, blockID, tickRate());
                    par1World.notifyBlocksOfNeighborChange(par2, par3, par4, blockID);
                }
            }
            else if (flag)
            {
                updateFlow(par1World, par2, par3, par4);
            }
        }
        else
        {
            updateFlow(par1World, par2, par3, par4);
        }

        if (liquidCanDisplaceBlock(par1World, par2, par3 - 1, par4))
        {
            if (blockMaterial == Material.lava && par1World.getBlockMaterial(par2, par3 - 1, par4) == Material.water)
            {
                par1World.setBlockWithNotify(par2, par3 - 1, par4, Block.stone.blockID);
                triggerLavaMixEffects(par1World, par2, par3 - 1, par4);
                return;
            }

            if (i >= 8)
            {
                par1World.setBlockAndMetadataWithNotify(par2, par3 - 1, par4, blockID, i);
            }
            else
            {
                par1World.setBlockAndMetadataWithNotify(par2, par3 - 1, par4, blockID, i + 8);
            }
        }
        else if (i >= 0 && (i == 0 || blockBlocksFlow(par1World, par2, par3 - 1, par4)))
        {
            boolean aflag[] = getOptimalFlowDirections(par1World, par2, par3, par4);
            int l = i + byte0;

            if (i >= 8)
            {
                l = 1;
            }

            if (l >= 8)
            {
                return;
            }

            if (aflag[0])
            {
                flowIntoBlock(par1World, par2 - 1, par3, par4, l);
            }

            if (aflag[1])
            {
                flowIntoBlock(par1World, par2 + 1, par3, par4, l);
            }

            if (aflag[2])
            {
                flowIntoBlock(par1World, par2, par3, par4 - 1, l);
            }

            if (aflag[3])
            {
                flowIntoBlock(par1World, par2, par3, par4 + 1, l);
            }
        }
    }

    private void flowIntoBlock(World par1World, int par2, int par3, int par4, int par5)
    {
        if (liquidCanDisplaceBlock(par1World, par2, par3, par4))
        {
            int i = par1World.getBlockId(par2, par3, par4);

            if (i > 0)
            {
                if (blockMaterial == Material.lava)
                {
                    triggerLavaMixEffects(par1World, par2, par3, par4);
                }
                else
                {
                    Block.blocksList[i].dropBlockAsItem(par1World, par2, par3, par4, par1World.getBlockMetadata(par2, par3, par4), 0);
                }
            }

            par1World.setBlockAndMetadataWithNotify(par2, par3, par4, blockID, par5);
        }
    }

    private int calculateFlowCost(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        int i = 1000;

        for (int j = 0; j < 4; j++)
        {
            if (j == 0 && par6 == 1 || j == 1 && par6 == 0 || j == 2 && par6 == 3 || j == 3 && par6 == 2)
            {
                continue;
            }

            int k = par2;
            int l = par3;
            int i1 = par4;

            if (j == 0)
            {
                k--;
            }

            if (j == 1)
            {
                k++;
            }

            if (j == 2)
            {
                i1--;
            }

            if (j == 3)
            {
                i1++;
            }

            if (blockBlocksFlow(par1World, k, l, i1) || par1World.getBlockMaterial(k, l, i1) == blockMaterial && par1World.getBlockMetadata(k, l, i1) == 0)
            {
                continue;
            }

            if (!blockBlocksFlow(par1World, k, l - 1, i1))
            {
                return par5;
            }

            if (par5 >= 4)
            {
                continue;
            }

            int j1 = calculateFlowCost(par1World, k, l, i1, par5 + 1, j);

            if (j1 < i)
            {
                i = j1;
            }
        }

        return i;
    }

    private boolean[] getOptimalFlowDirections(World par1World, int par2, int par3, int par4)
    {
        for (int i = 0; i < 4; i++)
        {
            flowCost[i] = 1000;
            int k = par2;
            int j1 = par3;
            int k1 = par4;

            if (i == 0)
            {
                k--;
            }

            if (i == 1)
            {
                k++;
            }

            if (i == 2)
            {
                k1--;
            }

            if (i == 3)
            {
                k1++;
            }

            if (blockBlocksFlow(par1World, k, j1, k1) || par1World.getBlockMaterial(k, j1, k1) == blockMaterial && par1World.getBlockMetadata(k, j1, k1) == 0)
            {
                continue;
            }

            if (!blockBlocksFlow(par1World, k, j1 - 1, k1))
            {
                flowCost[i] = 0;
            }
            else
            {
                flowCost[i] = calculateFlowCost(par1World, k, j1, k1, 1, i);
            }
        }

        int j = flowCost[0];

        for (int l = 1; l < 4; l++)
        {
            if (flowCost[l] < j)
            {
                j = flowCost[l];
            }
        }

        for (int i1 = 0; i1 < 4; i1++)
        {
            isOptimalFlowDirection[i1] = flowCost[i1] == j;
        }

        return isOptimalFlowDirection;
    }

    private boolean blockBlocksFlow(World par1World, int par2, int par3, int par4)
    {
        int i = par1World.getBlockId(par2, par3, par4);

        if (i == Block.doorWood.blockID || i == Block.doorSteel.blockID || i == Block.signPost.blockID || i == Block.ladder.blockID || i == Block.reed.blockID)
        {
            return true;
        }

        if (i == 0)
        {
            return false;
        }

        Material material = Block.blocksList[i].blockMaterial;

        if (material == Material.portal)
        {
            return true;
        }

        return material.blocksMovement();
    }

    protected int getSmallestFlowDecay(World par1World, int par2, int par3, int par4, int par5)
    {
        int i = getFlowDecay(par1World, par2, par3, par4);

        if (i < 0)
        {
            return par5;
        }

        if (i == 0)
        {
            numAdjacentSources++;
        }

        if (i >= 8)
        {
            i = 0;
        }

        return par5 >= 0 && i >= par5 ? par5 : i;
    }

    private boolean liquidCanDisplaceBlock(World par1World, int par2, int par3, int par4)
    {
        Material material = par1World.getBlockMaterial(par2, par3, par4);

        if (material == blockMaterial)
        {
            return false;
        }

        if (material == Material.lava)
        {
            return false;
        }
        else
        {
            return !blockBlocksFlow(par1World, par2, par3, par4);
        }
    }

    /**
     * Called whenever the block is added into the world. Args: world, x, y, z
     */
    public void onBlockAdded(World par1World, int par2, int par3, int par4)
    {
        super.onBlockAdded(par1World, par2, par3, par4);

        if (par1World.getBlockId(par2, par3, par4) == blockID)
        {
            par1World.scheduleBlockUpdate(par2, par3, par4, blockID, tickRate());
        }
    }
}
