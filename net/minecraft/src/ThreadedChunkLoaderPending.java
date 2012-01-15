package net.minecraft.src;

class ThreadedChunkLoaderPending
{
    public final ChunkCoordIntPair field_40613_a;
    public final NBTTagCompound field_40612_b;

    public ThreadedChunkLoaderPending(ChunkCoordIntPair chunkcoordintpair, NBTTagCompound nbttagcompound)
    {
        field_40613_a = chunkcoordintpair;
        field_40612_b = nbttagcompound;
    }
}
