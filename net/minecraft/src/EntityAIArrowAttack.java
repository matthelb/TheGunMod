package net.minecraft.src;

import java.util.Random;

public class EntityAIArrowAttack extends EntityAIBase
{
    World field_48183_a;

    /** The entity the AI instance has been applied to */
    EntityLiving entityHost;
    EntityLiving attackTarget;
    int field_48179_d;
    float field_48180_e;
    int field_48177_f;
    int field_48178_g;
    int field_48184_h;

    public EntityAIArrowAttack(EntityLiving par1EntityLiving, float par2, int par3, int par4)
    {
        field_48179_d = 0;
        field_48177_f = 0;
        entityHost = par1EntityLiving;
        field_48183_a = par1EntityLiving.worldObj;
        field_48180_e = par2;
        field_48178_g = par3;
        field_48184_h = par4;
        setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLiving entityliving = entityHost.getAttackTarget();

        if (entityliving == null)
        {
            return false;
        }
        else
        {
            attackTarget = entityliving;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return shouldExecute() || !entityHost.getNavigator().noPath();
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        attackTarget = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        double d = 100D;
        double d1 = entityHost.getDistanceSq(attackTarget.posX, attackTarget.boundingBox.minY, attackTarget.posZ);
        boolean flag = entityHost.func_48318_al().canSee(attackTarget);

        if (flag)
        {
            field_48177_f++;
        }
        else
        {
            field_48177_f = 0;
        }

        if (d1 > d || field_48177_f < 20)
        {
            entityHost.getNavigator().func_48652_a(attackTarget, field_48180_e);
        }
        else
        {
            entityHost.getNavigator().func_48662_f();
        }

        entityHost.getLookHelper().setLookPositionWithEntity(attackTarget, 30F, 30F);
        field_48179_d = Math.max(field_48179_d - 1, 0);

        if (field_48179_d > 0)
        {
            return;
        }

        if (d1 > d || !flag)
        {
            return;
        }
        else
        {
            func_48176_f();
            field_48179_d = field_48184_h;
            return;
        }
    }

    private void func_48176_f()
    {
        if (field_48178_g == 1)
        {
            EntityArrow entityarrow = new EntityArrow(field_48183_a, entityHost, attackTarget, 1.6F, 12F);
            field_48183_a.playSoundAtEntity(entityHost, "random.bow", 1.0F, 1.0F / (entityHost.getRNG().nextFloat() * 0.4F + 0.8F));
            field_48183_a.spawnEntityInWorld(entityarrow);
        }
        else if (field_48178_g == 2)
        {
            EntitySnowball entitysnowball = new EntitySnowball(field_48183_a, entityHost);
            double d = attackTarget.posX - entityHost.posX;
            double d1 = (attackTarget.posY + (double)attackTarget.getEyeHeight()) - 1.1000000238418579D - entitysnowball.posY;
            double d2 = attackTarget.posZ - entityHost.posZ;
            float f = MathHelper.sqrt_double(d * d + d2 * d2) * 0.2F;
            entitysnowball.setThrowableHeading(d, d1 + (double)f, d2, 1.6F, 12F);
            field_48183_a.playSoundAtEntity(entityHost, "random.bow", 1.0F, 1.0F / (entityHost.getRNG().nextFloat() * 0.4F + 0.8F));
            field_48183_a.spawnEntityInWorld(entitysnowball);
        }
    }
}
