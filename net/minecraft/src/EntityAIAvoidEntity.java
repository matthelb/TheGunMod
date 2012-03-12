package net.minecraft.src;

import java.util.List;

public class EntityAIAvoidEntity extends EntityAIBase
{
    private EntityCreature field_48237_a;
    private float field_48235_b;
    private float field_48236_c;
    private Entity field_48233_d;
    private float field_48234_e;
    private PathEntity field_48231_f;
    private PathNavigate field_48232_g;
    private Class field_48238_h;

    public EntityAIAvoidEntity(EntityCreature par1EntityCreature, Class par2Class, float par3, float par4, float par5)
    {
        field_48237_a = par1EntityCreature;
        field_48238_h = par2Class;
        field_48234_e = par3;
        field_48235_b = par4;
        field_48236_c = par5;
        field_48232_g = par1EntityCreature.getNavigator();
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (field_48238_h == (net.minecraft.src.EntityPlayer.class))
        {
            if ((field_48237_a instanceof EntityTameable) && ((EntityTameable)field_48237_a).isTamed())
            {
                return false;
            }

            field_48233_d = field_48237_a.worldObj.getClosestPlayerToEntity(field_48237_a, field_48234_e);

            if (field_48233_d == null)
            {
                return false;
            }
        }
        else
        {
            List list = field_48237_a.worldObj.getEntitiesWithinAABB(field_48238_h, field_48237_a.boundingBox.expand(field_48234_e, 3D, field_48234_e));

            if (list.size() == 0)
            {
                return false;
            }

            field_48233_d = (Entity)list.get(0);
        }

        if (!field_48237_a.func_48318_al().canSee(field_48233_d))
        {
            return false;
        }

        Vec3D vec3d = RandomPositionGenerator.func_48394_b(field_48237_a, 16, 7, Vec3D.createVector(field_48233_d.posX, field_48233_d.posY, field_48233_d.posZ));

        if (vec3d == null)
        {
            return false;
        }

        if (field_48233_d.getDistanceSq(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord) < field_48233_d.getDistanceSqToEntity(field_48237_a))
        {
            return false;
        }

        field_48231_f = field_48232_g.func_48650_a(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord);

        if (field_48231_f == null)
        {
            return false;
        }

        return field_48231_f.func_48426_a(vec3d);
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !field_48232_g.noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        field_48232_g.setPath(field_48231_f, field_48235_b);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        field_48233_d = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (field_48237_a.getDistanceSqToEntity(field_48233_d) < 49D)
        {
            field_48237_a.getNavigator().func_48654_a(field_48236_c);
        }
        else
        {
            field_48237_a.getNavigator().func_48654_a(field_48235_b);
        }
    }
}
