package net.minecraft.src;

public class WorldProviderEnd extends WorldProvider
{
    public WorldProviderEnd()
    {
    }

    public void registerWorldChunkManager()
    {
        worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.sky, 0.5F, 0.0F);
        worldType = 1;
        hasNoSky = true;
        canSleepInWorld = true;
    }

    public IChunkProvider getChunkProvider()
    {
        return new ChunkProviderEnd(worldObj, worldObj.getRandomSeed());
    }

    public float calculateCelestialAngle(long l, float f)
    {
        return 0.0F;
    }

    public boolean canRespawnHere()
    {
        return false;
    }

    public boolean canCoordinateBeSpawn(int i, int j)
    {
        int k = worldObj.getFirstUncoveredBlock(i, j);
        if (k == 0)
        {
            return false;
        }
        else
        {
            return Block.blocksList[k].blockMaterial.getIsSolid();
        }
    }

    public ChunkCoordinates getEntrancePortalLocation()
    {
        return new ChunkCoordinates(100, 50, 0);
    }

    public int func_46119_e()
    {
        return 50;
    }
}
