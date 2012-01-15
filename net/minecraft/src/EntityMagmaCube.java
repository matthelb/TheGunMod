package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class EntityMagmaCube extends EntitySlime
{
    public EntityMagmaCube(World world)
    {
        super(world);
        texture = "/mob/lava.png";
        isImmuneToFire = true;
        landMovementFactor = 0.2F;
    }

    public boolean getCanSpawnHere()
    {
        return worldObj.difficultySetting > 0 && worldObj.checkIfAABBIsClear(boundingBox) && worldObj.getCollidingBoundingBoxes(this, boundingBox).size() == 0 && !worldObj.getIsAnyLiquid(boundingBox);
    }

    public int getTotalArmorValue()
    {
        return getSlimeSize() * 3;
    }

    public float getEntityBrightness(float f)
    {
        return 1.0F;
    }

    protected String func_40120_w()
    {
        return "flame";
    }

    protected EntitySlime func_40114_y()
    {
        return new EntityMagmaCube(worldObj);
    }

    protected int getDropItemId()
    {
        return Item.magmaCream.shiftedIndex;
    }

    protected void dropFewItems(boolean flag, int i)
    {
        int j = getDropItemId();
        if (j > 0 && getSlimeSize() > 1)
        {
            int k = rand.nextInt(4) - 2;
            if (i > 0)
            {
                k += rand.nextInt(i + 1);
            }
            for (int l = 0; l < k; l++)
            {
                dropItem(j, 1);
            }
        }
    }

    public boolean isBurning()
    {
        return false;
    }

    protected int func_40115_A()
    {
        return super.func_40115_A() * 4;
    }

    protected void func_40116_B()
    {
        field_40122_a = field_40122_a * 0.9F;
    }

    protected void jump()
    {
        motionY = 0.42F + (float)getSlimeSize() * 0.1F;
        isAirBorne = true;
    }

    protected void fall(float f)
    {
    }

    protected boolean func_40119_C()
    {
        return true;
    }

    protected int func_40113_D()
    {
        return super.func_40113_D() + 2;
    }

    protected String getHurtSound()
    {
        return "mob.slime";
    }

    protected String getDeathSound()
    {
        return "mob.slime";
    }

    protected String func_40118_E()
    {
        if (getSlimeSize() > 1)
        {
            return "mob.magmacube.big";
        }
        else
        {
            return "mob.magmacube.small";
        }
    }

    public boolean handleLavaMovement()
    {
        return false;
    }

    protected boolean func_40121_G()
    {
        return true;
    }
}
