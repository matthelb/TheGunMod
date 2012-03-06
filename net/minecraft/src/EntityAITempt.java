package net.minecraft.src;

public class EntityAITempt extends EntityAIBase
{
    private EntityCreature field_48268_a;
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
    private boolean field_48269_l;
    private boolean field_48270_m;

    public EntityAITempt(EntityCreature par1EntityCreature, float par2, int par3, boolean par4)
    {
        field_48274_i = 0;
        field_48268_a = par1EntityCreature;
        field_48266_b = par2;
        field_48272_k = par3;
        field_48269_l = par4;
        func_46087_a(3);
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

        field_48273_h = field_48268_a.worldObj.getClosestPlayerToEntity(field_48268_a, 10D);

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
        if (field_48269_l)
        {
            if (field_48268_a.getDistanceSqToEntity(field_48273_h) < 36D)
            {
                if (field_48273_h.getDistanceSq(field_48267_c, field_48264_d, field_48265_e) > 0.01D)
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

    public void func_46088_e()
    {
        field_48267_c = field_48273_h.posX;
        field_48264_d = field_48273_h.posY;
        field_48265_e = field_48273_h.posZ;
        field_48271_j = true;
        field_48270_m = field_48268_a.func_48333_ak().func_48649_a();
        field_48268_a.func_48333_ak().func_48656_a(false);
    }

    public void resetTask()
    {
        field_48273_h = null;
        field_48268_a.func_48333_ak().func_48662_f();
        field_48274_i = 100;
        field_48271_j = false;
        field_48268_a.func_48333_ak().func_48656_a(field_48270_m);
    }

    public void updateTask()
    {
        field_48268_a.getLookHelper().setLookPositionWithEntity(field_48273_h, 30F, field_48268_a.getVerticalFaceSpeed());

        if (field_48268_a.getDistanceSqToEntity(field_48273_h) < 6.25D)
        {
            field_48268_a.func_48333_ak().func_48662_f();
        }
        else
        {
            field_48268_a.func_48333_ak().func_48652_a(field_48273_h, field_48266_b);
        }
    }

    public boolean func_48261_f()
    {
        return field_48271_j;
    }
}
