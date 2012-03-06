package net.minecraft.src;

import java.util.Random;

public class EntityAIVillagerMate extends EntityAIBase
{
    private EntityVillager field_48310_b;
    private EntityVillager field_48311_c;
    private World field_48308_d;
    private int field_48309_e;
    Village field_48312_a;

    public EntityAIVillagerMate(EntityVillager par1EntityVillager)
    {
        field_48309_e = 0;
        field_48310_b = par1EntityVillager;
        field_48308_d = par1EntityVillager.worldObj;
        func_46087_a(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (field_48310_b.func_48351_J() != 0)
        {
            return false;
        }

        if (field_48310_b.getRNG().nextInt(500) != 0)
        {
            return false;
        }

        field_48312_a = field_48308_d.field_48096_A.func_48632_a(MathHelper.floor_double(field_48310_b.posX), MathHelper.floor_double(field_48310_b.posY), MathHelper.floor_double(field_48310_b.posZ), 0);

        if (field_48312_a == null)
        {
            return false;
        }

        if (!func_48305_f())
        {
            return false;
        }

        Entity entity = field_48308_d.func_48085_a(net.minecraft.src.EntityVillager.class, field_48310_b.boundingBox.expand(8D, 3D, 8D), field_48310_b);

        if (entity == null)
        {
            return false;
        }

        field_48311_c = (EntityVillager)entity;
        return field_48311_c.func_48351_J() == 0;
    }

    public void func_46088_e()
    {
        field_48309_e = 300;
        field_48310_b.func_48356_a(true);
    }

    public void resetTask()
    {
        field_48312_a = null;
        field_48311_c = null;
        field_48310_b.func_48356_a(false);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return field_48309_e >= 0 && func_48305_f() && field_48310_b.func_48351_J() == 0;
    }

    public void updateTask()
    {
        field_48309_e--;
        field_48310_b.getLookHelper().setLookPositionWithEntity(field_48311_c, 10F, 30F);

        if (field_48310_b.getDistanceSqToEntity(field_48311_c) > 2.25D)
        {
            field_48310_b.func_48333_ak().func_48652_a(field_48311_c, 0.25F);
        }
        else if (field_48309_e == 0 && field_48311_c.func_48355_A())
        {
            func_48306_i();
        }

        if (field_48310_b.getRNG().nextInt(35) == 0)
        {
            func_48307_a(field_48310_b);
        }
    }

    private boolean func_48305_f()
    {
        int i = (int)((double)(float)field_48312_a.func_48525_c() * 0.35D);
        return field_48312_a.func_48521_e() < i;
    }

    private void func_48306_i()
    {
        EntityVillager entityvillager = new EntityVillager(field_48308_d);
        field_48311_c.func_48350_c(6000);
        field_48310_b.func_48350_c(6000);
        entityvillager.func_48350_c(-24000);
        entityvillager.func_48357_f_(field_48310_b.getRNG().nextInt(5));
        entityvillager.setLocationAndAngles(field_48310_b.posX, field_48310_b.posY, field_48310_b.posZ, 0.0F, 0.0F);
        field_48308_d.spawnEntityInWorld(entityvillager);
        func_48307_a(entityvillager);
    }

    private void func_48307_a(EntityLiving par1EntityLiving)
    {
        Random random = par1EntityLiving.getRNG();

        for (int i = 0; i < 5; i++)
        {
            double d = random.nextGaussian() * 0.02D;
            double d1 = random.nextGaussian() * 0.02D;
            double d2 = random.nextGaussian() * 0.02D;
            field_48308_d.spawnParticle("heart", (par1EntityLiving.posX + (double)(random.nextFloat() * par1EntityLiving.width * 2.0F)) - (double)par1EntityLiving.width, par1EntityLiving.posY + 1.0D + (double)(random.nextFloat() * par1EntityLiving.height), (par1EntityLiving.posZ + (double)(random.nextFloat() * par1EntityLiving.width * 2.0F)) - (double)par1EntityLiving.width, d, d1, d2);
        }
    }
}
