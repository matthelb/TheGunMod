package net.minecraft.src;

import java.util.Random;

public abstract class EntityTameable extends EntityAnimal
{
    protected EntityAISit field_48374_a;

    public EntityTameable(World par1World)
    {
        super(par1World);
        field_48374_a = new EntityAISit(this);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(16, Byte.valueOf((byte)0));
        dataWatcher.addObject(17, "");
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);

        if (func_48367_A() == null)
        {
            par1NBTTagCompound.setString("Owner", "");
        }
        else
        {
            par1NBTTagCompound.setString("Owner", func_48367_A());
        }

        par1NBTTagCompound.setBoolean("Sitting", func_48371_v_());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        String s = par1NBTTagCompound.getString("Owner");

        if (s.length() > 0)
        {
            func_48372_a(s);
            func_48366_b(true);
        }

        field_48374_a.func_48210_a(true);
    }

    protected void func_48370_a(boolean par1)
    {
        String s = "heart";

        if (!par1)
        {
            s = "smoke";
        }

        for (int i = 0; i < 7; i++)
        {
            double d = rand.nextGaussian() * 0.02D;
            double d1 = rand.nextGaussian() * 0.02D;
            double d2 = rand.nextGaussian() * 0.02D;
            worldObj.spawnParticle(s, (posX + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, posY + 0.5D + (double)(rand.nextFloat() * height), (posZ + (double)(rand.nextFloat() * width * 2.0F)) - (double)width, d, d1, d2);
        }
    }

    public boolean func_48373_u_()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 4) != 0;
    }

    public void func_48366_b(boolean par1)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);

        if (par1)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 4)));
        }
        else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -5)));
        }
    }

    public boolean func_48371_v_()
    {
        return (dataWatcher.getWatchableObjectByte(16) & 1) != 0;
    }

    public void func_48369_c(boolean par1)
    {
        byte byte0 = dataWatcher.getWatchableObjectByte(16);

        if (par1)
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 | 1)));
        }
        else
        {
            dataWatcher.updateObject(16, Byte.valueOf((byte)(byte0 & -2)));
        }
    }

    public String func_48367_A()
    {
        return dataWatcher.getWatchableObjectString(17);
    }

    public void func_48372_a(String par1Str)
    {
        dataWatcher.updateObject(17, par1Str);
    }

    public EntityLiving func_48368_w_()
    {
        return worldObj.getPlayerEntityByName(func_48367_A());
    }
}
