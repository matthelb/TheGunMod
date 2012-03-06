package net.minecraft.src;

import java.util.Random;

public class EntityAIArrowAttack extends EntityAIBase
{
    World field_48183_a;
    EntityLiving field_48181_b;
    EntityLiving field_48182_c;
    int field_48179_d;
    float field_48180_e;
    int field_48177_f;
    int field_48178_g;
    int field_48184_h;

    public EntityAIArrowAttack(EntityLiving par1EntityLiving, float par2, int par3, int par4)
    {
        field_48179_d = 0;
        field_48177_f = 0;
        field_48181_b = par1EntityLiving;
        field_48183_a = par1EntityLiving.worldObj;
        field_48180_e = par2;
        field_48178_g = par3;
        field_48184_h = par4;
        func_46087_a(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLiving entityliving = field_48181_b.func_48331_as();

        if (entityliving == null)
        {
            return false;
        }
        else
        {
            field_48182_c = entityliving;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return shouldExecute() || !field_48181_b.func_48333_ak().func_46034_b();
    }

    public void resetTask()
    {
        field_48182_c = null;
    }

    public void updateTask()
    {
        double d = 100D;
        double d1 = field_48181_b.getDistanceSq(field_48182_c.posX, field_48182_c.boundingBox.minY, field_48182_c.posZ);
        boolean flag = field_48181_b.func_48318_al().func_48546_a(field_48182_c);

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
            field_48181_b.func_48333_ak().func_48652_a(field_48182_c, field_48180_e);
        }
        else
        {
            field_48181_b.func_48333_ak().func_48662_f();
        }

        field_48181_b.getLookHelper().setLookPositionWithEntity(field_48182_c, 30F, 30F);
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
            EntityArrow entityarrow = new EntityArrow(field_48183_a, field_48181_b, field_48182_c, 1.6F, 12F);
            field_48183_a.playSoundAtEntity(field_48181_b, "random.bow", 1.0F, 1.0F / (field_48181_b.getRNG().nextFloat() * 0.4F + 0.8F));
            field_48183_a.spawnEntityInWorld(entityarrow);
        }
        else if (field_48178_g == 2)
        {
            EntitySnowball entitysnowball = new EntitySnowball(field_48183_a, field_48181_b);
            double d = field_48182_c.posX - field_48181_b.posX;
            double d1 = (field_48182_c.posY + (double)field_48182_c.getEyeHeight()) - 1.1D - entitysnowball.posY;
            double d2 = field_48182_c.posZ - field_48181_b.posZ;
            float f = MathHelper.sqrt_double(d * d + d2 * d2) * 0.2F;
            entitysnowball.setThrowableHeading(d, d1 + (double)f, d2, 1.6F, 12F);
            field_48183_a.playSoundAtEntity(field_48181_b, "random.bow", 1.0F, 1.0F / (field_48181_b.getRNG().nextFloat() * 0.4F + 0.8F));
            field_48183_a.spawnEntityInWorld(entitysnowball);
        }
    }
}
