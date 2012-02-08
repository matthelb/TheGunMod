package net.minecraft.src;

import java.io.IOException;
import java.util.*;

public class ChunkProviderServer
    implements IChunkProvider
{
    private Set droppedChunksSet;
    private Chunk dummyChunk;
    private IChunkProvider serverChunkGenerator;
    private IChunkLoader chunkLoader;
    public boolean chunkLoadOverride;
    private LongHashMap id2ChunkMap;
    private List field_727_f;
    private WorldServer world;

    public ChunkProviderServer(WorldServer worldserver, IChunkLoader ichunkloader, IChunkProvider ichunkprovider)
    {
        droppedChunksSet = new HashSet();
        chunkLoadOverride = false;
        id2ChunkMap = new LongHashMap();
        field_727_f = new ArrayList();
        dummyChunk = new EmptyChunk(worldserver, new byte[256 * worldserver.worldHeight], 0, 0);
        world = worldserver;
        chunkLoader = ichunkloader;
        serverChunkGenerator = ichunkprovider;
    }

    public boolean chunkExists(int i, int j)
    {
        return id2ChunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(i, j));
    }

    public void dropChunk(int i, int j)
    {
        if (world.worldProvider.canRespawnHere())
        {
            ChunkCoordinates chunkcoordinates = world.getSpawnPoint();
            int k = (i * 16 + 8) - chunkcoordinates.posX;
            int l = (j * 16 + 8) - chunkcoordinates.posZ;
            char c = '\200';
            if (k < -c || k > c || l < -c || l > c)
            {
                droppedChunksSet.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)));
            }
        }
        else
        {
            droppedChunksSet.add(Long.valueOf(ChunkCoordIntPair.chunkXZ2Int(i, j)));
        }
    }

    public void unloadAllChunks()
    {
        Chunk chunk;
        for (Iterator iterator = field_727_f.iterator(); iterator.hasNext(); dropChunk(chunk.xPosition, chunk.zPosition))
        {
            chunk = (Chunk)iterator.next();
        }
    }

    public Chunk loadChunk(int i, int j)
    {
        long l = ChunkCoordIntPair.chunkXZ2Int(i, j);
        droppedChunksSet.remove(Long.valueOf(l));
        Chunk chunk = (Chunk)id2ChunkMap.getValueByKey(l);
        if (chunk == null)
        {
            chunk = loadChunkFromFile(i, j);
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
            if (world.findingSpawnPoint || chunkLoadOverride)
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

    private Chunk loadChunkFromFile(int i, int j)
    {
        if (chunkLoader == null)
        {
            return null;
        }
        try
        {
            Chunk chunk = chunkLoader.loadChunk(world, i, j);
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

    private void saveChunkExtraData(Chunk chunk)
    {
        if (chunkLoader == null)
        {
            return;
        }
        try
        {
            chunkLoader.saveExtraChunkData(world, chunk);
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    private void saveChunkData(Chunk chunk)
    {
        if (chunkLoader == null)
        {
            return;
        }
        try
        {
            chunk.lastSaveTime = world.getWorldTime();
            chunkLoader.saveChunk(world, chunk);
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
                saveChunkExtraData(chunk);
            }
            if (!chunk.needsSaving(flag))
            {
                continue;
            }
            saveChunkData(chunk);
            chunk.isModified = false;
            if (++i == 24 && !flag)
            {
                return false;
            }
        }

        if (flag)
        {
            if (chunkLoader == null)
            {
                return true;
            }
            chunkLoader.saveExtraData();
        }
        return true;
    }

    public boolean unload100OldestChunks()
    {
        if (!world.levelSaving)
        {
            for (int i = 0; i < 100; i++)
            {
                if (!droppedChunksSet.isEmpty())
                {
                    Long long1 = (Long)droppedChunksSet.iterator().next();
                    Chunk chunk = (Chunk)id2ChunkMap.getValueByKey(long1.longValue());
                    chunk.onChunkUnload();
                    saveChunkData(chunk);
                    saveChunkExtraData(chunk);
                    droppedChunksSet.remove(long1);
                    id2ChunkMap.remove(long1.longValue());
                    field_727_f.remove(chunk);
                }
            }

            if (chunkLoader != null)
            {
                chunkLoader.chunkTick();
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
        return (new StringBuilder()).append("ServerChunkCache: ").append(id2ChunkMap.getNumHashElements()).append(" Drop: ").append(droppedChunksSet.size()).toString();
    }

    public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k)
    {
        return serverChunkGenerator.getPossibleCreatures(enumcreaturetype, i, j, k);
    }

    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k)
    {
        return serverChunkGenerator.findClosestStructure(world, s, i, j, k);
    }
}
