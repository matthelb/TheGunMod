package net.minecraft.src;

import java.util.Random;

public class EntityAILookIdle extends EntityAIBase
{
    /** The entity that is looking idle. */
    private EntityLiving idleEntity;
    private double field_46087_b;
    private double field_46088_c;

    /**
     * A decrementing tick that stops the entity from being idle once it reaches 0.
     */
    private int idleTime;

    public EntityAILookIdle(EntityLiving par1EntityLiving)
    {
        idleTime = 0;
        idleEntity = par1EntityLiving;
        setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return idleEntity.getRNG().nextFloat() < 0.02F;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return idleTime >= 0;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        double d = (Math.PI * 2D) * idleEntity.getRNG().nextDouble();
        field_46087_b = Math.cos(d);
        field_46088_c = Math.sin(d);
        idleTime = 20 + idleEntity.getRNG().nextInt(20);
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        idleTime--;
        idleEntity.getLookHelper().setLookPosition(idleEntity.posX + field_46087_b, idleEntity.posY + (double)idleEntity.getEyeHeight(), idleEntity.posZ + field_46088_c, 10F, idleEntity.getVerticalFaceSpeed());
    }
}
