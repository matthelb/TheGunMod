package net.minecraft.src;

public class EntityAIMoveTowardsTarget extends EntityAIBase
{
    private EntityCreature field_48223_a;
    private EntityLiving field_48221_b;
    private double field_48222_c;
    private double field_48219_d;
    private double field_48220_e;
    private float field_48217_f;
    private float field_48218_g;

    public EntityAIMoveTowardsTarget(EntityCreature par1EntityCreature, float par2, float par3)
    {
        field_48223_a = par1EntityCreature;
        field_48217_f = par2;
        field_48218_g = par3;
        func_46087_a(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        field_48221_b = field_48223_a.func_48331_as();

        if (field_48221_b == null)
        {
            return false;
        }

        if (field_48221_b.getDistanceSqToEntity(field_48223_a) > (double)(field_48218_g * field_48218_g))
        {
            return false;
        }

        Vec3D vec3d = RandomPositionGenerator.func_48395_a(field_48223_a, 16, 7, Vec3D.createVector(field_48221_b.posX, field_48221_b.posY, field_48221_b.posZ));

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            field_48222_c = vec3d.xCoord;
            field_48219_d = vec3d.yCoord;
            field_48220_e = vec3d.zCoord;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !field_48223_a.func_48333_ak().func_46034_b() && field_48221_b.isEntityAlive() && field_48221_b.getDistanceSqToEntity(field_48223_a) < (double)(field_48218_g * field_48218_g);
    }

    public void resetTask()
    {
        field_48221_b = null;
    }

    public void func_46088_e()
    {
        field_48223_a.func_48333_ak().func_48658_a(field_48222_c, field_48219_d, field_48220_e, field_48217_f);
    }
}
