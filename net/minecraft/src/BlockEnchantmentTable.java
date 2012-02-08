package net.minecraft.src;

public class BlockEnchantmentTable extends BlockContainer
{
    protected BlockEnchantmentTable(int i)
    {
        super(i, 166, Material.rock);
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.75F, 1.0F);
        setLightOpacity(0);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public int getBlockTextureFromSideAndMetadata(int i, int j)
    {
        return getBlockTextureFromSide(i);
    }

    public int getBlockTextureFromSide(int i)
    {
        if (i == 0)
        {
            return blockIndexInTexture + 17;
        }
        if (i == 1)
        {
            return blockIndexInTexture;
        }
        else
        {
            return blockIndexInTexture + 16;
        }
    }

    public TileEntity getBlockEntity()
    {
        return new TileEntityEnchantmentTable();
    }

    public boolean blockActivated(World world, int i, int j, int k, EntityPlayer entityplayer)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            entityplayer.displayGUIEnchantment(i, j, k);
            return true;
        }
    }
}
