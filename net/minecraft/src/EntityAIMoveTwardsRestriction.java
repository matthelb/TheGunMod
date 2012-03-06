package net.minecraft.src;

public class EntityAIMoveTwardsRestriction extends EntityAIBase
{
    private EntityCreature field_48152_a;
    private double field_48150_b;
    private double field_48151_c;
    private double field_48148_d;
    private float field_48149_e;

    public EntityAIMoveTwardsRestriction(EntityCreature par1EntityCreature, float par2)
    {
        field_48152_a = par1EntityCreature;
        field_48149_e = par2;
        func_46087_a(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (field_48152_a.func_48325_at())
        {
            return false;
        }

        ChunkCoordinates chunkcoordinates = field_48152_a.func_48323_au();
        Vec3D vec3d = RandomPositionGenerator.func_48395_a(field_48152_a, 16, 7, Vec3D.createVector(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ));

        if (vec3d == null)
        {
            return false;
        }
        else
        {
            field_48150_b = vec3d.xCoord;
            field_48151_c = vec3d.yCoord;
            field_48148_d = vec3d.zCoord;
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !field_48152_a.func_48333_ak().func_46034_b();
    }

    public void func_46088_e()
    {
        field_48152_a.func_48333_ak().func_48658_a(field_48150_b, field_48151_c, field_48148_d, field_48149_e);
    }
}
