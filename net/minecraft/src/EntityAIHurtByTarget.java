package net.minecraft.src;

import java.util.Iterator;
import java.util.List;

public class EntityAIHurtByTarget extends EntityAITarget
{
    boolean field_48300_a;

    public EntityAIHurtByTarget(EntityLiving par1EntityLiving, boolean par2)
    {
        super(par1EntityLiving, 16F, false);
        field_48300_a = par2;
        func_46087_a(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return func_48284_a(field_48291_c.getAITarget(), true);
    }

    public void func_46088_e()
    {
        field_48291_c.func_48327_b(field_48291_c.getAITarget());

        if (field_48300_a)
        {
            List list = field_48291_c.worldObj.getEntitiesWithinAABB(field_48291_c.getClass(), AxisAlignedBB.getBoundingBoxFromPool(field_48291_c.posX, field_48291_c.posY, field_48291_c.posZ, field_48291_c.posX + 1.0D, field_48291_c.posY + 1.0D, field_48291_c.posZ + 1.0D).expand(field_48288_d, 4D, field_48288_d));
            Iterator iterator = list.iterator();

            do
            {
                if (!iterator.hasNext())
                {
                    break;
                }

                Entity entity = (Entity)iterator.next();
                EntityLiving entityliving = (EntityLiving)entity;

                if (field_48291_c != entityliving && entityliving.func_48331_as() == null)
                {
                    entityliving.func_48327_b(field_48291_c.getAITarget());
                }
            }
            while (true);
        }

        super.func_46088_e();
    }
}
