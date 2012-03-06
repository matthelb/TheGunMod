package net.minecraft.src;

public abstract class EntityAIDoorInteract extends EntityAIBase
{
    protected EntityLiving field_48192_a;
    protected int field_48190_b;
    protected int field_48191_c;
    protected int field_48188_d;
    protected BlockDoor field_48189_e;
    boolean field_48186_f;
    float field_48187_g;
    float field_48193_h;

    public EntityAIDoorInteract(EntityLiving par1EntityLiving)
    {
        field_48192_a = par1EntityLiving;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (!field_48192_a.isCollidedHorizontally)
        {
            return false;
        }

        PathNavigate pathnavigate = field_48192_a.func_48333_ak();
        PathEntity pathentity = pathnavigate.func_48668_c();

        if (pathentity == null || pathentity.isFinished() || !pathnavigate.func_48657_b())
        {
            return false;
        }

        for (int i = 0; i < Math.min(pathentity.func_48423_e() + 2, pathentity.func_48424_d()); i++)
        {
            PathPoint pathpoint = pathentity.func_48429_a(i);
            field_48190_b = pathpoint.xCoord;
            field_48191_c = pathpoint.yCoord + 1;
            field_48188_d = pathpoint.zCoord;

            if (field_48192_a.getDistanceSq(field_48190_b, field_48192_a.posY, field_48188_d) > 2.25D)
            {
                continue;
            }

            field_48189_e = func_48185_a(field_48190_b, field_48191_c, field_48188_d);

            if (field_48189_e != null)
            {
                return true;
            }
        }

        field_48190_b = MathHelper.floor_double(field_48192_a.posX);
        field_48191_c = MathHelper.floor_double(field_48192_a.posY + 1.0D);
        field_48188_d = MathHelper.floor_double(field_48192_a.posZ);
        field_48189_e = func_48185_a(field_48190_b, field_48191_c, field_48188_d);
        return field_48189_e != null;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !field_48186_f;
    }

    public void func_46088_e()
    {
        field_48186_f = false;
        field_48187_g = (float)((double)((float)field_48190_b + 0.5F) - field_48192_a.posX);
        field_48193_h = (float)((double)((float)field_48188_d + 0.5F) - field_48192_a.posZ);
    }

    public void updateTask()
    {
        float f = (float)((double)((float)field_48190_b + 0.5F) - field_48192_a.posX);
        float f1 = (float)((double)((float)field_48188_d + 0.5F) - field_48192_a.posZ);
        float f2 = field_48187_g * f + field_48193_h * f1;

        if (f2 < 0.0F)
        {
            field_48186_f = true;
        }
    }

    private BlockDoor func_48185_a(int par1, int par2, int par3)
    {
        int i = field_48192_a.worldObj.getBlockId(par1, par2, par3);

        if (i != Block.doorWood.blockID)
        {
            return null;
        }
        else
        {
            BlockDoor blockdoor = (BlockDoor)Block.blocksList[i];
            return blockdoor;
        }
    }
}
