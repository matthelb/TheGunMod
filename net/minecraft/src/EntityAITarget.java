package net.minecraft.src;

import java.util.Random;

public abstract class EntityAITarget extends EntityAIBase
{
    protected EntityLiving field_48291_c;
    protected float field_48288_d;
    protected boolean field_48289_e;
    private boolean field_48292_a;
    private int field_48290_b;
    private int field_48286_f;
    private int field_48287_g;

    public EntityAITarget(EntityLiving par1EntityLiving, float par2, boolean par3)
    {
        this(par1EntityLiving, par2, par3, false);
    }

    public EntityAITarget(EntityLiving par1EntityLiving, float par2, boolean par3, boolean par4)
    {
        field_48290_b = 0;
        field_48286_f = 0;
        field_48287_g = 0;
        field_48291_c = par1EntityLiving;
        field_48288_d = par2;
        field_48289_e = par3;
        field_48292_a = par4;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        EntityLiving entityliving = field_48291_c.func_48331_as();

        if (entityliving == null)
        {
            return false;
        }

        if (!entityliving.isEntityAlive())
        {
            return false;
        }

        if (field_48291_c.getDistanceSqToEntity(entityliving) > (double)(field_48288_d * field_48288_d))
        {
            return false;
        }

        if (field_48289_e)
        {
            if (!field_48291_c.func_48318_al().func_48546_a(entityliving))
            {
                if (++field_48287_g > 60)
                {
                    return false;
                }
            }
            else
            {
                field_48287_g = 0;
            }
        }

        return true;
    }

    public void func_46088_e()
    {
        field_48290_b = 0;
        field_48286_f = 0;
        field_48287_g = 0;
    }

    public void resetTask()
    {
        field_48291_c.func_48327_b(null);
    }

    protected boolean func_48284_a(EntityLiving par1EntityLiving, boolean par2)
    {
        if (par1EntityLiving == null)
        {
            return false;
        }

        if (par1EntityLiving == field_48291_c)
        {
            return false;
        }

        if (!par1EntityLiving.isEntityAlive())
        {
            return false;
        }

        if (par1EntityLiving.boundingBox.maxY <= field_48291_c.boundingBox.minY || par1EntityLiving.boundingBox.minY >= field_48291_c.boundingBox.maxY)
        {
            return false;
        }

        if (!field_48291_c.func_48336_a(par1EntityLiving.getClass()))
        {
            return false;
        }

        if ((field_48291_c instanceof EntityTameable) && ((EntityTameable)field_48291_c).func_48373_u_())
        {
            if ((par1EntityLiving instanceof EntityTameable) && ((EntityTameable)par1EntityLiving).func_48373_u_())
            {
                return false;
            }

            if (par1EntityLiving == ((EntityTameable)field_48291_c).func_48368_w_())
            {
                return false;
            }
        }
        else if ((par1EntityLiving instanceof EntityPlayer) && !par2 && ((EntityPlayer)par1EntityLiving).capabilities.disableDamage)
        {
            return false;
        }

        if (!field_48291_c.func_48328_e(MathHelper.floor_double(par1EntityLiving.posX), MathHelper.floor_double(par1EntityLiving.posY), MathHelper.floor_double(par1EntityLiving.posZ)))
        {
            return false;
        }

        if (field_48289_e && !field_48291_c.func_48318_al().func_48546_a(par1EntityLiving))
        {
            return false;
        }

        if (field_48292_a)
        {
            if (--field_48286_f <= 0)
            {
                field_48290_b = 0;
            }

            if (field_48290_b == 0)
            {
                field_48290_b = func_48285_a(par1EntityLiving) ? 1 : 2;
            }

            if (field_48290_b == 2)
            {
                return false;
            }
        }

        return true;
    }

    private boolean func_48285_a(EntityLiving par1EntityLiving)
    {
        field_48286_f = 10 + field_48291_c.getRNG().nextInt(5);
        PathEntity pathentity = field_48291_c.func_48333_ak().func_48661_a(par1EntityLiving);

        if (pathentity == null)
        {
            return false;
        }

        PathPoint pathpoint = pathentity.func_48425_c();

        if (pathpoint == null)
        {
            return false;
        }
        else
        {
            int i = pathpoint.xCoord - MathHelper.floor_double(par1EntityLiving.posX);
            int j = pathpoint.zCoord - MathHelper.floor_double(par1EntityLiving.posZ);
            return (double)(i * i + j * j) <= 2.25D;
        }
    }
}
