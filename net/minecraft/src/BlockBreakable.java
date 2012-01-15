package net.minecraft.src;

public class BlockBreakable extends Block
{
    private boolean localFlag;

    protected BlockBreakable(int i, int j, Material material, boolean flag)
    {
        super(i, j, material);
        localFlag = flag;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }
}
