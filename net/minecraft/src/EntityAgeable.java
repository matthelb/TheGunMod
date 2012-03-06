package net.minecraft.src;

public abstract class EntityAgeable extends EntityCreature
{
    public EntityAgeable(World par1World)
    {
        super(par1World);
    }

    protected void entityInit()
    {
        super.entityInit();
        dataWatcher.addObject(12, new Integer(0));
    }

    public int func_48351_J()
    {
        return dataWatcher.getWatchableObjectInt(12);
    }

    public void func_48350_c(int par1)
    {
        dataWatcher.updateObject(12, Integer.valueOf(par1));
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        par1NBTTagCompound.setInteger("Age", func_48351_J());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        func_48350_c(par1NBTTagCompound.getInteger("Age"));
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    public void onLivingUpdate()
    {
        super.onLivingUpdate();
        int i = func_48351_J();

        if (i < 0)
        {
            i++;
            func_48350_c(i);
        }
        else if (i > 0)
        {
            i--;
            func_48350_c(i);
        }
    }

    /**
     * If Animal, checks if the age timer is negative
     */
    public boolean isChild()
    {
        return func_48351_J() < 0;
    }
}
