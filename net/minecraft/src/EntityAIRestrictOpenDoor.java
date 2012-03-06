package net.minecraft.src;

public class EntityAIRestrictOpenDoor extends EntityAIBase
{
    private EntityCreature field_48160_a;
    private VillageDoorInfo field_48159_b;

    public EntityAIRestrictOpenDoor(EntityCreature par1EntityCreature)
    {
        field_48160_a = par1EntityCreature;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (field_48160_a.worldObj.isDaytime())
        {
            return false;
        }

        Village village = field_48160_a.worldObj.field_48096_A.func_48632_a(MathHelper.floor_double(field_48160_a.posX), MathHelper.floor_double(field_48160_a.posY), MathHelper.floor_double(field_48160_a.posZ), 16);

        if (village == null)
        {
            return false;
        }

        field_48159_b = village.func_48533_b(MathHelper.floor_double(field_48160_a.posX), MathHelper.floor_double(field_48160_a.posY), MathHelper.floor_double(field_48160_a.posZ));

        if (field_48159_b == null)
        {
            return false;
        }
        else
        {
            return (double)field_48159_b.func_48486_b(MathHelper.floor_double(field_48160_a.posX), MathHelper.floor_double(field_48160_a.posY), MathHelper.floor_double(field_48160_a.posZ)) < 2.25D;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        if (field_48160_a.worldObj.isDaytime())
        {
            return false;
        }
        else
        {
            return !field_48159_b.field_48488_g && field_48159_b.func_48479_a(MathHelper.floor_double(field_48160_a.posX), MathHelper.floor_double(field_48160_a.posZ));
        }
    }

    public void func_46088_e()
    {
        field_48160_a.func_48333_ak().func_48663_b(false);
        field_48160_a.func_48333_ak().func_48655_c(false);
    }

    public void resetTask()
    {
        field_48160_a.func_48333_ak().func_48663_b(true);
        field_48160_a.func_48333_ak().func_48655_c(true);
        field_48159_b = null;
    }

    public void updateTask()
    {
        field_48159_b.func_48482_e();
    }
}
