package net.minecraft.src;

import java.util.Random;

public class EntityAIMoveIndoors extends EntityAIBase
{
    private EntityCreature entityObj;
    private VillageDoorInfo field_48173_b;
    private int field_48174_c;
    private int field_48172_d;

    public EntityAIMoveIndoors(EntityCreature par1EntityCreature)
    {
        field_48174_c = -1;
        field_48172_d = -1;
        entityObj = par1EntityCreature;
        setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (entityObj.worldObj.isDaytime() && !entityObj.worldObj.isRaining() || entityObj.worldObj.worldProvider.hasNoSky)
        {
            return false;
        }

        if (entityObj.getRNG().nextInt(50) != 0)
        {
            return false;
        }

        if (field_48174_c != -1 && entityObj.getDistanceSq(field_48174_c, entityObj.posY, field_48172_d) < 4D)
        {
            return false;
        }

        Village village = entityObj.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(entityObj.posX), MathHelper.floor_double(entityObj.posY), MathHelper.floor_double(entityObj.posZ), 14);

        if (village == null)
        {
            return false;
        }
        else
        {
            field_48173_b = village.findNearestDoorUnrestricted(MathHelper.floor_double(entityObj.posX), MathHelper.floor_double(entityObj.posY), MathHelper.floor_double(entityObj.posZ));
            return field_48173_b != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !entityObj.getNavigator().noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        field_48174_c = -1;

        if (entityObj.getDistanceSq(field_48173_b.getInsidePosX(), field_48173_b.posY, field_48173_b.getInsidePosZ()) > 256D)
        {
            Vec3D vec3d = RandomPositionGenerator.func_48395_a(entityObj, 14, 3, Vec3D.createVector((double)field_48173_b.getInsidePosX() + 0.5D, field_48173_b.getInsidePosY(), (double)field_48173_b.getInsidePosZ() + 0.5D));

            if (vec3d != null)
            {
                entityObj.getNavigator().func_48658_a(vec3d.xCoord, vec3d.yCoord, vec3d.zCoord, 0.3F);
            }
        }
        else
        {
            entityObj.getNavigator().func_48658_a((double)field_48173_b.getInsidePosX() + 0.5D, field_48173_b.getInsidePosY(), (double)field_48173_b.getInsidePosZ() + 0.5D, 0.3F);
        }
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        field_48174_c = field_48173_b.getInsidePosX();
        field_48172_d = field_48173_b.getInsidePosZ();
        field_48173_b = null;
    }
}
