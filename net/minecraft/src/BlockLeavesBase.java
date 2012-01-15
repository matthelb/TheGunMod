package net.minecraft.src;

public class BlockLeavesBase extends Block
{
    protected boolean graphicsLevel;

    protected BlockLeavesBase(int i, int j, Material material, boolean flag)
    {
        super(i, j, material);
        graphicsLevel = flag;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }
}
