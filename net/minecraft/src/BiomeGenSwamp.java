package net.minecraft.src;

import java.util.Random;

public class BiomeGenSwamp extends BiomeGenBase
{
    protected BiomeGenSwamp(int i)
    {
        super(i);
        biomeDecorator.treesPerChunk = 2;
        biomeDecorator.flowersPerChunk = -999;
        biomeDecorator.deadBushPerChunk = 1;
        biomeDecorator.mushroomsPerChunk = 8;
        biomeDecorator.reedsPerChunk = 10;
        biomeDecorator.clayPerChunk = 1;
        biomeDecorator.waterlilyPerChunk = 4;
        waterColorMultiplier = 0xe0ffae;
    }

    public WorldGenerator getRandomWorldGenForTrees(Random random)
    {
        return worldGenSwamp;
    }
}
