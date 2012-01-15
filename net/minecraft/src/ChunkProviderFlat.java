package net.minecraft.src;

import java.util.List;
import java.util.Random;

public class ChunkProviderFlat
    implements IChunkProvider
{
    private World field_46046_a;
    private Random field_46044_b;
    private final boolean field_46045_c;
    private MapGenVillage field_46043_d;

    public ChunkProviderFlat(World world, long l, boolean flag)
    {
        field_46043_d = new MapGenVillage(1);
        field_46046_a = world;
        field_46045_c = flag;
        field_46044_b = new Random(l);
    }

    private void func_46042_a(byte abyte0[])
    {
        int i = abyte0.length / 256;
        for (int j = 0; j < 16; j++)
        {
            for (int k = 0; k < 16; k++)
            {
                for (int l = 0; l < i; l++)
                {
                    int i1 = 0;
                    if (l == 0)
                    {
                        i1 = Block.bedrock.blockID;
                    }
                    else if (l <= 2)
                    {
                        i1 = Block.dirt.blockID;
                    }
                    else if (l == 3)
                    {
                        i1 = Block.grass.blockID;
                    }
                    abyte0[j << 11 | k << 7 | l] = (byte)i1;
                }
            }
        }
    }

    public Chunk loadChunk(int i, int j)
    {
        return provideChunk(i, j);
    }

    public Chunk provideChunk(int i, int j)
    {
        byte abyte0[] = new byte[16 * field_46046_a.worldHeight * 16];
        Chunk chunk = new Chunk(field_46046_a, abyte0, i, j);
        func_46042_a(abyte0);
        if (field_46045_c)
        {
            field_46043_d.generate(this, field_46046_a, i, j, abyte0);
        }
        chunk.generateSkylightMap();
        return chunk;
    }

    public boolean chunkExists(int i, int j)
    {
        return true;
    }

    public void populate(IChunkProvider ichunkprovider, int i, int j)
    {
        field_46044_b.setSeed(field_46046_a.getRandomSeed());
        long l = (field_46044_b.nextLong() / 2L) * 2L + 1L;
        long l1 = (field_46044_b.nextLong() / 2L) * 2L + 1L;
        field_46044_b.setSeed((long)i * l + (long)j * l1 ^ field_46046_a.getRandomSeed());
        if (field_46045_c)
        {
            field_46043_d.generateStructuresInChunk(field_46046_a, field_46044_b, i, j);
        }
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
    {
        return true;
    }

    public boolean unload100OldestChunks()
    {
        return false;
    }

    public boolean canSave()
    {
        return true;
    }

    public List func_40181_a(EnumCreatureType enumcreaturetype, int i, int j, int k)
    {
        WorldChunkManager worldchunkmanager = field_46046_a.getWorldChunkManager();
        if (worldchunkmanager == null)
        {
            return null;
        }
        BiomeGenBase biomegenbase = worldchunkmanager.getBiomeGenAtChunkCoord(new ChunkCoordIntPair(i >> 4, k >> 4));
        if (biomegenbase == null)
        {
            return null;
        }
        else
        {
            return biomegenbase.getSpawnableList(enumcreaturetype);
        }
    }

    public ChunkPosition func_40182_a(World world, String s, int i, int j, int k)
    {
        return null;
    }
}
