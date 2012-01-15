package net.minecraft.src;

public class WorldProviderHell extends WorldProvider
{
    public WorldProviderHell()
    {
    }

    public void registerWorldChunkManager()
    {
        worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.hell, 1.0F, 0.0F);
        canSleepInWorld = true;
        isHellWorld = true;
        hasNoSky = true;
        worldType = -1;
    }

    protected void generateLightBrightnessTable()
    {
        float f = 0.1F;
        for (int i = 0; i <= 15; i++)
        {
            float f1 = 1.0F - (float)i / 15F;
            lightBrightnessTable[i] = ((1.0F - f1) / (f1 * 3F + 1.0F)) * (1.0F - f) + f;
        }
    }

    public IChunkProvider getChunkProvider()
    {
        return new ChunkProviderHell(worldObj, worldObj.getRandomSeed());
    }

    public boolean canCoordinateBeSpawn(int i, int j)
    {
        return false;
    }

    public float calculateCelestialAngle(long l, float f)
    {
        return 0.5F;
    }

    public boolean canRespawnHere()
    {
        return false;
    }
}
