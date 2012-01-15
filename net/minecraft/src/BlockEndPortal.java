package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class BlockEndPortal extends BlockContainer
{
    public static boolean bossDefeated = false;

    protected BlockEndPortal(int i, Material material)
    {
        super(i, 0, material);
        setLightValue(1.0F);
    }

    public TileEntity getBlockEntity()
    {
        return new TileEntityEndPortal();
    }

    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        float f = 0.0625F;
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, f, 1.0F);
    }

    public void getCollidingBoundingBoxes(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, ArrayList arraylist)
    {
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public int quantityDropped(Random random)
    {
        return 0;
    }

    public void onEntityCollidedWithBlock(World world, int i, int j, int k, Entity entity)
    {
        if (entity.ridingEntity == null && entity.riddenByEntity == null && (entity instanceof EntityPlayer) && !world.singleplayerWorld)
        {
            ((EntityPlayer)entity).func_40107_e(1);
        }
    }

    public int getRenderType()
    {
        return -1;
    }

    public void onBlockAdded(World world, int i, int j, int k)
    {
        if (bossDefeated)
        {
            return;
        }
        if (world.worldProvider.worldType != 0)
        {
            world.setBlockWithNotify(i, j, k, 0);
            return;
        }
        else
        {
            return;
        }
    }
}
