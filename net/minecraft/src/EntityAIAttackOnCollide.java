package net.minecraft.src;

public class EntityAIAttackOnCollide extends EntityAIBase
{
    World field_46099_a;
    EntityMob field_46097_b;
    EntityLiving field_46098_c;
    int field_46095_d;
    float field_46096_e;

    public EntityAIAttackOnCollide(EntityMob entitymob, World world, float f)
    {
        field_46095_d = 0;
        field_46097_b = entitymob;
        field_46099_a = world;
        field_46096_e = f;
        func_46087_a(3);
    }

    public boolean func_46090_a()
    {
        field_46098_c = func_46094_h();
        return field_46098_c != null;
    }

    public void func_46089_b()
    {
        field_46097_b.func_46023_ah().func_46035_a(field_46098_c, field_46097_b.func_46016_ar());
        field_46097_b.func_46021_ae().func_46058_a(field_46098_c, 30F, 30F);
        field_46095_d = Math.max(field_46095_d - 1, 0);
        double d = 4D;
        if (field_46097_b.getDistanceSqToEntity(field_46098_c) > d)
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
            field_46097_b.attackEntityAsMob(field_46098_c);
            return;
        }
    }

    private EntityLiving func_46094_h()
    {
        Object obj = field_46097_b.func_46020_aj();
        if (obj == null)
        {
            obj = field_46099_a.getClosestVulnerablePlayerToEntity(field_46097_b, field_46096_e);
        }
        if (obj == null)
        {
            return null;
        }
        if (((EntityLiving) (obj)).boundingBox.maxY <= field_46097_b.boundingBox.minY || ((EntityLiving) (obj)).boundingBox.minY >= field_46097_b.boundingBox.maxY)
        {
            return null;
        }
        if (!field_46097_b.canEntityBeSeen(((Entity) (obj))))
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
