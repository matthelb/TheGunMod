package net.minecraft.src;

public interface IBlockAccess
{
    public abstract int getBlockId(int i, int j, int k);

    public abstract TileEntity getBlockTileEntity(int i, int j, int k);

    public abstract int getBlockMetadata(int i, int j, int k);

    public abstract Material getBlockMaterial(int i, int j, int k);

    public abstract boolean isBlockNormalCube(int i, int j, int k);
}
