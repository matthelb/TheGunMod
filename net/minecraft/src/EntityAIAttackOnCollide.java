package net.minecraft.src;

public class EntityAIAttackOnCollide extends EntityAIBase
{
    World worldObj;
    EntityMob entity;
    EntityLiving entityTarget;
    int field_46095_d;
    float field_46096_e;

    public EntityAIAttackOnCollide(EntityMob entitymob, World world, float f)
    {
        field_46095_d = 0;
        entity = entitymob;
        worldObj = world;
        field_46096_e = f;
        func_46087_a(3);
    }

    public boolean func_46090_a()
    {
        entityTarget = func_46094_h();
        return entityTarget != null;
    }

    public void func_46089_b()
    {
        entity.func_46023_ah().func_46035_a(entityTarget, entity.getMoveSpeed());
        entity.getLookHelper().func_46058_a(entityTarget, 30F, 30F);
        field_46095_d = Math.max(field_46095_d - 1, 0);
        double d = 4D;
        if (entity.getDistanceSqToEntity(entityTarget) > d)
        {
            return;
        }
        if (field_46095_d > 0)
        {
            return;
        }
        else
        {
            field_46095_d = 20;
            entity.attackEntityAsMob(entityTarget);
            return;
        }
    }

    private EntityLiving func_46094_h()
    {
        Object obj = entity.func_46020_aj();
        if (obj == null)
        {
            obj = worldObj.getClosestVulnerablePlayerToEntity(entity, field_46096_e);
        }
        if (obj == null)
        {
            return null;
        }
        if (((EntityLiving) (obj)).boundingBox.maxY <= entity.boundingBox.minY || ((EntityLiving) (obj)).boundingBox.minY >= entity.boundingBox.maxY)
        {
            return null;
        }
        if (!entity.canEntityBeSeen(((Entity) (obj))))
        {
            return null;
        }
        else
        {
            return ((EntityLiving) (obj));
        }
    }

    public int func_46091_c()
    {
        return super.func_46091_c();
    }

    public void func_46087_a(int i)
    {
        super.func_46087_a(i);
    }

    public void func_46085_d()
    {
        super.func_46085_d();
    }

    public void func_46088_e()
    {
        super.func_46088_e();
    }

    public boolean func_46086_f()
    {
        return super.func_46086_f();
    }

    public boolean func_46092_g()
    {
        return super.func_46092_g();
    }
}
