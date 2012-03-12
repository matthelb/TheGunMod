package net.minecraft.src;

public class EntityAIFollowOwner extends EntityAIBase
{
    private EntityTameable field_48247_d;
    private EntityLiving field_48248_e;
    World field_48251_a;
    private float field_48245_f;
    private PathNavigate field_48246_g;
    private int field_48252_h;
    float field_48249_b;
    float field_48250_c;
    private boolean field_48253_i;

    public EntityAIFollowOwner(EntityTameable par1EntityTameable, float par2, float par3, float par4)
    {
        field_48247_d = par1EntityTameable;
        field_48251_a = par1EntityTameable.worldObj;
        field_48245_f = par2;
        field_48246_g = par1EntityTameable.getNavigator();
        field_48250_c = par3;
        field_48249_b = par4;
        setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLiving entityliving = field_48247_d.getOwner();

        if (entityliving == null)
        {
            return false;
        }

        if (field_48247_d.isSitting())
        {
            return false;
        }

        if (field_48247_d.getDistanceSqToEntity(entityliving) < (double)(field_48250_c * field_48250_c))
        {
            return false;
        }
        else
        {
            field_48248_e = entityliving;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !field_48246_g.noPath() && field_48247_d.getDistanceSqToEntity(field_48248_e) > (double)(field_48249_b * field_48249_b) && !field_48247_d.isSitting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        field_48252_h = 0;
        field_48253_i = field_48247_d.getNavigator().func_48649_a();
        field_48247_d.getNavigator().func_48656_a(false);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        field_48248_e = null;
        field_48246_g.func_48662_f();
        field_48247_d.getNavigator().func_48656_a(field_48253_i);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        field_48247_d.getLookHelper().setLookPositionWithEntity(field_48248_e, 10F, field_48247_d.getVerticalFaceSpeed());

        if (field_48247_d.isSitting())
        {
            return;
        }

        if (--field_48252_h > 0)
        {
            return;
        }

        field_48252_h = 10;

        if (field_48246_g.func_48652_a(field_48248_e, field_48245_f))
        {
            return;
        }

        if (field_48247_d.getDistanceSqToEntity(field_48248_e) < 144D)
        {
            return;
        }

        int i = MathHelper.floor_double(field_48248_e.posX) - 2;
        int j = MathHelper.floor_double(field_48248_e.posZ) - 2;
        int k = MathHelper.floor_double(field_48248_e.boundingBox.minY);

        for (int l = 0; l <= 4; l++)
        {
            for (int i1 = 0; i1 <= 4; i1++)
            {
                if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && field_48251_a.isBlockNormalCube(i + l, k - 1, j + i1) && !field_48251_a.isBlockNormalCube(i + l, k, j + i1) && !field_48251_a.isBlockNormalCube(i + l, k + 1, j + i1))
                {
                    field_48247_d.setLocationAndAngles((float)(i + l) + 0.5F, k, (float)(j + i1) + 0.5F, field_48247_d.rotationYaw, field_48247_d.rotationPitch);
                    field_48246_g.func_48662_f();
                    return;
                }
            }
        }
    }
}
