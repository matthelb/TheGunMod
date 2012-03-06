package net.minecraft.src;

public interface IWorldAccess
{
    public abstract void markBlockNeedsUpdate(int i, int j, int k);

    public abstract void func_48414_b(int i, int j, int k);

    public abstract void markBlockRangeNeedsUpdate(int i, int j, int k, int l, int i1, int j1);

    /**
     * Plays the specified sound. Arg: x, y, z, soundName, unknown1, unknown2
     */
    public abstract void playSound(String s, double d, double d1, double d2, float f, float f1);

    /**
     * Spawns a particle. Arg: particleType, x, y, z, velX, velY, velZ
     */
    public abstract void spawnParticle(String s, double d, double d1, double d2, double d3, double d4, double d5);

    public abstract void obtainEntitySkin(Entity entity);

    /**
     * Decrement the reference counter for this entity's skin image data
     */
    public abstract void releaseEntitySkin(Entity entity);

    /**
     * Plays the specified record. Arg: recordName, x, y, z
     */
    public abstract void playRecord(String s, int i, int j, int k);

    /**
     * In all implementations, this method does nothing.
     */
    public abstract void doNothingWithTileEntity(int i, int j, int k, TileEntity tileentity);

    public abstract void playAuxSFX(EntityPlayer entityplayer, int i, int j, int k, int l, int i1);
}
