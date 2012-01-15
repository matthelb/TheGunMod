package net.minecraft.src;

import java.io.IOException;
import java.util.*;

public class ChunkProviderServer
    implements IChunkProvider
{
    private Set field_725_a;
    private Chunk dummyChunk;
    private IChunkProvider serverChunkGenerator;
    private IChunkLoader field_729_d;
    public boolean chunkLoadOverride;
    private LongHashMap id2ChunkMap;
    private List field_727_f;
    private WorldServer world;

    public ChunkProviderServer(WorldServer worldserver, IChunkLoader ichunkloader, IChunkProvider ichunkprovider)
    {
        field_725_a = new HashSet();
        chunkLoadOverride = false;
        id2ChunkMap = new LongHashMap();
        field_727_f = new ArrayList();
        dummyChunk = new EmptyChunk(worldserver, new byte[256 * worldserver.worldHeight], 0, 0);
        world = worldserver;
        field_729_d = ichunkloader;
        serverChunkGenerator = ichunkprovider;
    }

    public boolean chunkExists(int i, int j)
    {
        return id2ChunkMap.containsKey(ChunkCoordIntPair.chunkXZ2Int(i, j));
    }

    public void func_374_c(int i, int j)
    {
        if (world.worldProvider.canRespawnHere())
        {
            ChunkCoordinates chunkcoordinates = world.getSpawnPoint();
            int k = (i * 16 + 8) - chunkcoordinates.posX;
            int l = (j * 16 + 8) - chunkcoordinates.posZ;
            char c = '\200';
            if (k < -c || k > c || l < -c || l > c)
            {
                field_725_a.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)));
            }
        }
        else
        {
            field_725_a.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)));
        }
    }

    public void func_46041_c()
    {
        Chunk chunk;
        for (Iterator iterator = field_727_f.iterator(); iterator.hasNext(); func_374_c(chunk.xPosition, chunk.zPosition))
        {
            chunk = (Chunk)iterator.next();
        }
    }

    public Chunk loadChunk(int i, int j)
    {
        long l = ChunkCoordIntPair.chunkXZ2Int(i, j);
        field_725_a.remove(Long.valueOf(l));
        Chunk chunk = (Chunk)id2ChunkMap.getValueByKey(l);
        if (chunk == null)
        {
            chunk = func_4063_e(i, j);
            if (chunk == null)
            {
                if (serverChunkGenerator == null)
                {
                    chunk = dummyChunk;
                }
                else
                {
                    chunk = serverChunkGenerator.provideChunk(i, j);
                }
            }
            id2ChunkMap.add(l, chunk);
            field_727_f.add(chunk);
            if (chunk != null)
            {
                chunk.func_4053_c();
                chunk.onChunkLoad();
            }
            chunk.populateChunk(this, this, i, j);
        }
        return chunk;
    }

    public Chunk provideChunk(int i, int j)
    {
        Chunk chunk = (Chunk)id2ChunkMap.getValueByKey(ChunkCoordIntPair.chunkXZ2Int(i, j));
        if (chunk == null)
        {
            if (world.worldChunkLoadOverride || chunkLoadOverride)
            {
                return loadChunk(i, j);
            }
            else
            {
                return dummyChunk;
            }
        }
        else
        {
            return chunk;
        }
    }

    private Chunk func_4063_e(int i, int j)
    {
        if (field_729_d == null)
        {
            return null;
        }
        try
        {
            Chunk chunk = field_729_d.loadChunk(world, i, j);
            if (chunk != null)
            {
                chunk.lastSaveTime = world.getWorldTime();
            }
            return chunk;
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        return null;
    }

    private void func_375_a(Chunk chunk)
    {
        if (field_729_d == null)
        {
            return;
        }
        try
        {
            field_729_d.saveExtraChunkData(world, chunk);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void func_373_b(Chunk chunk)
    {
        if (field_729_d == null)
        {
            return;
        }
        try
        {
            chunk.lastSaveTime = world.getWorldTime();
            field_729_d.saveChunk(world, chunk);
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public void populate(IChunkProvider ichunkprovider, int i, int j)
    {
        Chunk chunk = provideChunk(i, j);
        if (!chunk.isTerrainPopulated)
        {
            chunk.isTerrainPopulated = true;
            if (serverChunkGenerator != null)
            {
                serverChunkGenerator.populate(ichunkprovider, i, j);
                ModLoader.PopulateChunk(serverChunkGenerator, i << 4, j << 4, world);
                chunk.setChunkModified();
            }
        }
    }

    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
    {
        int i = 0;
        for (int j = 0; j < field_727_f.size(); j++)
        {
            Chunk chunk = (Chunk)field_727_f.get(j);
            if (flag && !chunk.neverSave)
            {
                func_375_a(chunk);
            }
            if (!chunk.needsSaving(flag))
            {
                continue;
            }
            func_373_b(chunk);
            chunk.isModified = false;
            if (++i == 24 && !flag)
            {
                return false;
            }
        }

        if (flag)
        {
            if (field_729_d == null)
            {
                return true;
            }
            field_729_d.saveExtraData();
        }
        return true;
    }

    public boolean unload100OldestChunks()
    {
        if (!world.levelSaving)
        {
            for (int i = 0; i < 100; i++)
            {
                if (!field_725_a.isEmpty())
                {
                    Long long1 = (Long)field_725_a.iterator().next();
                    Chunk chunk = (Chunk)id2ChunkMap.getValueByKey(long1.longValue());
                    chunk.onChunkUnload();
                    func_373_b(chunk);
                    func_375_a(chunk);
                    field_725_a.remove(long1);
                    id2ChunkMap.remove(long1.longValue());
                    field_727_f.remove(chunk);
                }
            }

            if (field_729_d != null)
            {
                field_729_d.func_661_a();
            }
        }
        return serverChunkGenerator.unload100OldestChunks();
    }

    public boolean canSave()
    {
        return !world.levelSaving;
    }

    public String func_46040_d()
    {
        return (new StringBuilder()).append("ServerChunkCache: ").append(id2ChunkMap.func_46048_a()).append(" Drop: ").append(field_725_a.size()).toString();
    }

    public List func_40181_a(EnumCreatureType enumcreaturetype, int i, int j, int k)
    {
        return serverChunkGenerator.func_40181_a(enumcreaturetype, i, j, k);
    }

    public ChunkPosition func_40182_a(World world, String s, int i, int j, int k)
    {
        return serverChunkGenerator.func_40182_a(world, s, i, j, k);
    }
}
