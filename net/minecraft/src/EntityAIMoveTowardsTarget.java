package net.minecraft.src;

public class EntityAIMoveTowardsTarget extends EntityAIBase
{
    private EntityCreature field_48336_a;
    private EntityLiving field_48334_b;
    private double field_48335_c;
    private double field_48332_d;
    private double field_48333_e;
    private float field_48330_f;
    private float field_48331_g;

    public EntityAIMoveTowardsTarget(EntityCreature par1EntityCreature, float par2, float par3)
    {
        field_48336_a = par1EntityCreature;
        field_48330_f = par2;
        field_48331_g = par3;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        field_48334_b = field_48336_a.getAttackTarget();

        if (field_48334_b == null)
        {
            return false;
        }

        if (field_48334_b.getDistanceSqToEntity(field_48336_a) > (double)(field_48331_g * field_48331_g))
        {
            return false;
        }

        Vec3D vec3d = RandomPositionGenerator.func_48620_a(field_48336_a, 16, 7, Vec3D.createVector(field_48334_b.posX, field_48334_b.posY, field_48334_b.posZ));

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            field_48335_c = vec3d.xCoord;
            field_48332_d = vec3d.yCoord;
            field_48333_e = vec3d.zCoord;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !field_48336_a.getNavigator().noPath() && field_48334_b.isEntityAlive() && field_48334_b.getDistanceSqToEntity(field_48336_a) < (double)(field_48331_g * field_48331_g);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        field_48334_b = null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        field_48336_a.getNavigator().func_48666_a(field_48335_c, field_48332_d, field_48333_e, field_48330_f);
    }
}
