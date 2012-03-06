package net.minecraft.src;

public abstract class WorldSavedData
{
    /** The name of the map data nbt */
    public final String mapName;
    private boolean dirty;

    public WorldSavedData(String par1Str)
    {
        mapName = par1Str;
    }

    public abstract void readFromNBT(NBTTagCompound nbttagcompound);

    public abstract void writeToNBT(NBTTagCompound nbttagcompound);

    public void markDirty()
    {
        setDirty(true);
    }

    public void setDirty(boolean par1)
    {
        dirty = par1;
    }

    public boolean isDirty()
    {
        return dirty;
    }
}
