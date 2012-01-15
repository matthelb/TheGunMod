package net.minecraft.src;

public abstract class WorldProvider
{
    public World worldObj;
    public EnumWorldType field_46120_b;
    public WorldChunkManager worldChunkMgr;
    public boolean canSleepInWorld;
    public boolean isHellWorld;
    public boolean hasNoSky;
    public float lightBrightnessTable[];
    public int worldType;
    private float colorsSunriseSunset[];

    public WorldProvider()
    {
        canSleepInWorld = false;
        isHellWorld = false;
        hasNoSky = false;
        lightBrightnessTable = new float[16];
        worldType = 0;
        colorsSunriseSunset = new float[4];
    }

    public final void registerWorld(World world)
    {
        worldObj = world;
        field_46120_b = world.getWorldInfo().func_46069_q();
        registerWorldChunkManager();
        generateLightBrightnessTable();
    }

    protected void generateLightBrightnessTable()
    {
        float f = 0.0F;
        for (int i = 0; i <= 15; i++)
        {
            float f1 = 1.0F - (float)i / 15F;
            lightBrightnessTable[i] = ((1.0F - f1) / (f1 * 3F + 1.0F)) * (1.0F - f) + f;
        }
    }

    protected void registerWorldChunkManager()
    {
        if (worldObj.getWorldInfo().func_46069_q() == EnumWorldType.FLAT)
        {
            worldChunkMgr = new WorldChunkManagerHell(BiomeGenBase.plains, 0.5F, 0.5F);
        }
        else
        {
            worldChunkMgr = new WorldChunkManager(worldObj);
        }
    }

    public IChunkProvider getChunkProvider()
    {
        if (field_46120_b == EnumWorldType.FLAT)
        {
            return new ChunkProviderFlat(worldObj, worldObj.getRandomSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
        }
        else
        {
            return new ChunkProviderGenerate(worldObj, worldObj.getRandomSeed(), worldObj.getWorldInfo().isMapFeaturesEnabled());
        }
    }

    public boolean canCoordinateBeSpawn(int i, int j)
    {
        int k = worldObj.getFirstUncoveredBlock(i, j);
        return k == Block.grass.blockID;
    }

    public float calculateCelestialAngle(long l, float f)
    {
        int i = (int)(l % 24000L);
        float f1 = ((float)i + f) / 24000F - 0.25F;
        if (f1 < 0.0F)
        {
            f1++;
        }
        if (f1 > 1.0F)
        {
            f1--;
        }
        float f2 = f1;
        f1 = 1.0F - (float)((Math.cos((double)f1 * 3.1415926535897931D) + 1.0D) / 2D);
        f1 = f2 + (f1 - f2) / 3F;
        return f1;
    }

    public boolean canRespawnHere()
    {
        return true;
    }

    public static WorldProvider getProviderForDimension(int i)
    {
        if (i == -1)
        {
            return new WorldProviderHell();
        }
        if (i == 0)
        {
            return new WorldProviderSurface();
        }
        if (i == 1)
        {
            return new WorldProviderEnd();
        }
        else
        {
            return null;
        }
    }

    public ChunkCoordinates getEntrancePortalLocation()
    {
        return null;
    }

    public int func_46119_e()
    {
        if (field_46120_b == EnumWorldType.FLAT)
        {
            return 4;
        }
        else
        {
            return worldObj.worldHeight / 2;
        }
    }
}
