package net.minecraft.src;

public class EntityAITempt extends EntityAIBase
{
    private EntityCreature temptedEntity;
    private float field_48266_b;
    private double field_48267_c;
    private double field_48264_d;
    private double field_48265_e;
    private double field_48262_f;
    private double field_48263_g;
    private EntityPlayer field_48273_h;
    private int field_48274_i;
    private boolean field_48271_j;
    private int field_48272_k;
    private boolean scaredByPlayerMovement;
    private boolean field_48270_m;

    public EntityAITempt(EntityCreature par1EntityCreature, float par2, int par3, boolean par4)
    {
        field_48274_i = 0;
        temptedEntity = par1EntityCreature;
        field_48266_b = par2;
        field_48272_k = par3;
        scaredByPlayerMovement = par4;
        setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (field_48274_i > 0)
        {
            field_48274_i--;
            return false;
        }

        field_48273_h = temptedEntity.worldObj.getClosestPlayerToEntity(temptedEntity, 10D);

        if (field_48273_h == null)
        {
            return false;
        }

        ItemStack itemstack = field_48273_h.getCurrentEquippedItem();

        if (itemstack == null)
        {
            return false;
        }

        return itemstack.itemID == field_48272_k;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (scaredByPlayerMovement)
        {
            if (temptedEntity.getDistanceSqToEntity(field_48273_h) < 36D)
            {
                if (field_48273_h.getDistanceSq(field_48267_c, field_48264_d, field_48265_e) > 0.010000000000000002D)
                {
                    return false;
                }

                if (Math.abs((double)field_48273_h.rotationPitch - field_48262_f) > 5D || Math.abs((double)field_48273_h.rotationYaw - field_48263_g) > 5D)
                {
                    return false;
                }
            }
            else
            {
                field_48267_c = field_48273_h.posX;
                field_48264_d = field_48273_h.posY;
                field_48265_e = field_48273_h.posZ;
            }

            field_48262_f = field_48273_h.rotationPitch;
            field_48263_g = field_48273_h.rotationYaw;
        }

        return shouldExecute();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        field_48267_c = field_48273_h.posX;
        field_48264_d = field_48273_h.posY;
        field_48265_e = field_48273_h.posZ;
        field_48271_j = true;
        field_48270_m = temptedEntity.getNavigator().func_48649_a();
        temptedEntity.getNavigator().func_48656_a(false);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        field_48273_h = null;
        temptedEntity.getNavigator().func_48662_f();
        field_48274_i = 100;
        field_48271_j = false;
        temptedEntity.getNavigator().func_48656_a(field_48270_m);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        temptedEntity.getLookHelper().setLookPositionWithEntity(field_48273_h, 30F, temptedEntity.getVerticalFaceSpeed());

        if (temptedEntity.getDistanceSqToEntity(field_48273_h) < 6.25D)
        {
            temptedEntity.getNavigator().func_48662_f();
        }
        else
        {
            temptedEntity.getNavigator().func_48652_a(field_48273_h, field_48266_b);
        }
    }

    public boolean func_48261_f()
    {
        return field_48271_j;
    }
}
