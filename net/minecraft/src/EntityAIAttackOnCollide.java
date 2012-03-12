package net.minecraft.src;

import java.util.Random;

public class EntityAIAttackOnCollide extends EntityAIBase
{
    World worldObj;
    EntityLiving field_48267_b;
    EntityLiving entityTarget;
    int field_46091_d;
    float field_48266_e;
    boolean field_48264_f;
    PathEntity field_48265_g;
    Class field_48268_h;
    private int field_48269_i;

    public EntityAIAttackOnCollide(EntityLiving par1EntityLiving, Class par2Class, float par3, boolean par4)
    {
        this(par1EntityLiving, par3, par4);
        field_48268_h = par2Class;
    }

    public EntityAIAttackOnCollide(EntityLiving par1EntityLiving, float par2, boolean par3)
    {
        field_46091_d = 0;
        field_48267_b = par1EntityLiving;
        worldObj = par1EntityLiving.worldObj;
        field_48266_e = par2;
        field_48264_f = par3;
        setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLiving entityliving = field_48267_b.getAttackTarget();

        if (entityliving == null)
        {
            return false;
        }

        if (field_48268_h != null && !field_48268_h.isAssignableFrom(entityliving.getClass()))
        {
            return false;
        }
        else
        {
            entityTarget = entityliving;
            field_48265_g = field_48267_b.getNavigator().func_48679_a(entityTarget);
            return field_48265_g != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        EntityLiving entityliving = field_48267_b.getAttackTarget();

        if (entityliving == null)
        {
            return false;
        }

        if (!entityTarget.isEntityAlive())
        {
            return false;
        }

        if (!field_48264_f)
        {
            return !field_48267_b.getNavigator().noPath();
        }

        return field_48267_b.isWithinHomeDistance(MathHelper.floor_double(entityTarget.posX), MathHelper.floor_double(entityTarget.posY), MathHelper.floor_double(entityTarget.posZ));
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        field_48267_b.getNavigator().setPath(field_48265_g, field_48266_e);
        field_48269_i = 0;
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        entityTarget = null;
        field_48267_b.getNavigator().func_48672_f();
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        field_48267_b.getLookHelper().setLookPositionWithEntity(entityTarget, 30F, 30F);

        if ((field_48264_f || field_48267_b.func_48090_aM().canSee(entityTarget)) && --field_48269_i <= 0)
        {
            field_48269_i = 4 + field_48267_b.getRNG().nextInt(7);
            field_48267_b.getNavigator().func_48667_a(entityTarget, field_48266_e);
        }

        field_46091_d = Math.max(field_46091_d - 1, 0);
        double d = field_48267_b.width * 2.0F * (field_48267_b.width * 2.0F);

        if (field_48267_b.getDistanceSq(entityTarget.posX, entityTarget.boundingBox.minY, entityTarget.posZ) > d)
        {
            return;
        }

        if (field_46091_d > 0)
        {
            return;
        }
        else
        {
            field_46091_d = 20;
            field_48267_b.attackEntityAsMob(entityTarget);
            return;
        }
    }
}
