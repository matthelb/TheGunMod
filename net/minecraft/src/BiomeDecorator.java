package net.minecraft.src;

import java.util.Random;

public class BiomeDecorator
{
    /** The current world object. */
    protected World currentWorld;

    /** The Biome Decorator's random number generator. */
    protected Random randomGenerator;
    protected int chunk_X;
    protected int chunk_Z;

    /** The biome generator object. */
    protected BiomeGenBase biome;

    /** The clay generator. */
    protected WorldGenerator clayGen;

    /** The sand generator. */
    protected WorldGenerator sandGen;

    /** The gravel generator. */
    protected WorldGenerator gravelAsSandGen;

    /** The dirt generator. */
    protected WorldGenerator dirtGen;
    protected WorldGenerator gravelGen;
    protected WorldGenerator coalGen;
    protected WorldGenerator ironGen;
    protected WorldGenerator goldGen;
    protected WorldGenerator redstoneGen;
    protected WorldGenerator diamondGen;
    protected WorldGenerator lapisGen;
    protected WorldGenerator plantYellowGen;
    protected WorldGenerator plantRedGen;
    protected WorldGenerator mushroomBrownGen;
    protected WorldGenerator mushroomRedGen;

    /** Field that holds big mushroom generator */
    protected WorldGenerator bigMushroomGen;
    protected WorldGenerator reedGen;
    protected WorldGenerator cactusGen;
    protected WorldGenerator waterlilyGen;
    protected int waterlilyPerChunk;
    protected int treesPerChunk;
    protected int flowersPerChunk;
    protected int grassPerChunk;
    protected int deadBushPerChunk;
    protected int mushroomsPerChunk;
    protected int reedsPerChunk;
    protected int cactiPerChunk;

    /**
     * The number of sand patches to generate per chunk. Sand patches only generate when part of it is underwater.
     */
    protected int sandPerChunk;

    /**
     * The number of sand patches to generate per chunk. Sand patches only generate when part of it is underwater. There
     * appear to be two separate fields for this.
     */
    protected int sandPerChunk2;
    protected int clayPerChunk;

    /** Amount of big mushrooms per chunk */
    protected int bigMushroomsPerChunk;
    public boolean generateLakes;

    public BiomeDecorator(BiomeGenBase par1BiomeGenBase)
    {
        clayGen = new WorldGenClay(4);
        sandGen = new WorldGenSand(7, Block.sand.blockID);
        gravelAsSandGen = new WorldGenSand(6, Block.gravel.blockID);
        dirtGen = new WorldGenMinable(Block.dirt.blockID, 32);
        gravelGen = new WorldGenMinable(Block.gravel.blockID, 32);
        coalGen = new WorldGenMinable(Block.oreCoal.blockID, 16);
        ironGen = new WorldGenMinable(Block.oreIron.blockID, 8);
        goldGen = new WorldGenMinable(Block.oreGold.blockID, 8);
        redstoneGen = new WorldGenMinable(Block.oreRedstone.blockID, 7);
        diamondGen = new WorldGenMinable(Block.oreDiamond.blockID, 7);
        lapisGen = new WorldGenMinable(Block.oreLapis.blockID, 6);
        plantYellowGen = new WorldGenFlowers(Block.plantYellow.blockID);
        plantRedGen = new WorldGenFlowers(Block.plantRed.blockID);
        mushroomBrownGen = new WorldGenFlowers(Block.mushroomBrown.blockID);
        mushroomRedGen = new WorldGenFlowers(Block.mushroomRed.blockID);
        bigMushroomGen = new WorldGenBigMushroom();
        reedGen = new WorldGenReed();
        cactusGen = new WorldGenCactus();
        waterlilyGen = new WorldGenWaterlily();
        waterlilyPerChunk = 0;
        treesPerChunk = 0;
        flowersPerChunk = 2;
        grassPerChunk = 1;
        deadBushPerChunk = 0;
        mushroomsPerChunk = 0;
        reedsPerChunk = 0;
        cactiPerChunk = 0;
        sandPerChunk = 1;
        sandPerChunk2 = 3;
        clayPerChunk = 1;
        bigMushroomsPerChunk = 0;
        generateLakes = true;
        biome = par1BiomeGenBase;
    }

    /**
     * Decorates the world. Calls code that was formerly (pre-1.8) in ChunkProviderGenerate.populate
     */
    public void decorate(World par1World, Random par2Random, int par3, int par4)
    {
        if (currentWorld != null)
        {
            throw new RuntimeException("Already decorating!!");
        }
        else
        {
            currentWorld = par1World;
            randomGenerator = par2Random;
            chunk_X = par3;
            chunk_Z = par4;
            decorate();
            currentWorld = null;
            randomGenerator = null;
            return;
        }
    }

    /**
     * The method that does the work of actually decorating chunks
     */
    protected void decorate()
    {
        generateOres();

        for (int i = 0; i < sandPerChunk2; i++)
        {
            int i1 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k5 = chunk_Z + randomGenerator.nextInt(16) + 8;
            sandGen.generate(currentWorld, randomGenerator, i1, currentWorld.getTopSolidOrLiquidBlock(i1, k5), k5);
        }

        for (int j = 0; j < clayPerChunk; j++)
        {
            int j1 = chunk_X + randomGenerator.nextInt(16) + 8;
            int l5 = chunk_Z + randomGenerator.nextInt(16) + 8;
            clayGen.generate(currentWorld, randomGenerator, j1, currentWorld.getTopSolidOrLiquidBlock(j1, l5), l5);
        }

        for (int k = 0; k < sandPerChunk; k++)
        {
            int k1 = chunk_X + randomGenerator.nextInt(16) + 8;
            int i6 = chunk_Z + randomGenerator.nextInt(16) + 8;
            sandGen.generate(currentWorld, randomGenerator, k1, currentWorld.getTopSolidOrLiquidBlock(k1, i6), i6);
        }

        int l = treesPerChunk;

        if (randomGenerator.nextInt(10) == 0)
        {
            l++;
        }

        for (int l1 = 0; l1 < l; l1++)
        {
            int j6 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k10 = chunk_Z + randomGenerator.nextInt(16) + 8;
            WorldGenerator worldgenerator = biome.getRandomWorldGenForTrees(randomGenerator);
            worldgenerator.setScale(1.0D, 1.0D, 1.0D);
            worldgenerator.generate(currentWorld, randomGenerator, j6, currentWorld.getHeightValue(j6, k10), k10);
        }

        for (int i2 = 0; i2 < bigMushroomsPerChunk; i2++)
        {
            int k6 = chunk_X + randomGenerator.nextInt(16) + 8;
            int l10 = chunk_Z + randomGenerator.nextInt(16) + 8;
            bigMushroomGen.generate(currentWorld, randomGenerator, k6, currentWorld.getHeightValue(k6, l10), l10);
        }

        for (int j2 = 0; j2 < flowersPerChunk; j2++)
        {
            int l6 = chunk_X + randomGenerator.nextInt(16) + 8;
            int i11 = randomGenerator.nextInt(128);
            int l14 = chunk_Z + randomGenerator.nextInt(16) + 8;
            plantYellowGen.generate(currentWorld, randomGenerator, l6, i11, l14);

            if (randomGenerator.nextInt(4) == 0)
            {
                int i7 = chunk_X + randomGenerator.nextInt(16) + 8;
                int j11 = randomGenerator.nextInt(128);
                int i15 = chunk_Z + randomGenerator.nextInt(16) + 8;
                plantRedGen.generate(currentWorld, randomGenerator, i7, j11, i15);
            }
        }

        for (int k2 = 0; k2 < grassPerChunk; k2++)
        {
            int j7 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k11 = randomGenerator.nextInt(128);
            int j15 = chunk_Z + randomGenerator.nextInt(16) + 8;
            WorldGenerator worldgenerator1 = biome.func_48440_b(randomGenerator);
            worldgenerator1.generate(currentWorld, randomGenerator, j7, k11, j15);
        }

        for (int l2 = 0; l2 < deadBushPerChunk; l2++)
        {
            int k7 = chunk_X + randomGenerator.nextInt(16) + 8;
            int l11 = randomGenerator.nextInt(128);
            int k15 = chunk_Z + randomGenerator.nextInt(16) + 8;
            (new WorldGenDeadBush(Block.deadBush.blockID)).generate(currentWorld, randomGenerator, k7, l11, k15);
        }

        for (int i3 = 0; i3 < waterlilyPerChunk; i3++)
        {
            int l7 = chunk_X + randomGenerator.nextInt(16) + 8;
            int i12 = chunk_Z + randomGenerator.nextInt(16) + 8;
            int l15;

            for (l15 = randomGenerator.nextInt(128); l15 > 0 && currentWorld.getBlockId(l7, l15 - 1, i12) == 0; l15--) { }

            waterlilyGen.generate(currentWorld, randomGenerator, l7, l15, i12);
        }

        for (int j3 = 0; j3 < mushroomsPerChunk; j3++)
        {
            if (randomGenerator.nextInt(4) == 0)
            {
                int i8 = chunk_X + randomGenerator.nextInt(16) + 8;
                int j12 = chunk_Z + randomGenerator.nextInt(16) + 8;
                int i16 = currentWorld.getHeightValue(i8, j12);
                mushroomBrownGen.generate(currentWorld, randomGenerator, i8, i16, j12);
            }

            if (randomGenerator.nextInt(8) == 0)
            {
                int j8 = chunk_X + randomGenerator.nextInt(16) + 8;
                int k12 = chunk_Z + randomGenerator.nextInt(16) + 8;
                int j16 = randomGenerator.nextInt(128);
                mushroomRedGen.generate(currentWorld, randomGenerator, j8, j16, k12);
            }
        }

        if (randomGenerator.nextInt(4) == 0)
        {
            int k3 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k8 = randomGenerator.nextInt(128);
            int l12 = chunk_Z + randomGenerator.nextInt(16) + 8;
            mushroomBrownGen.generate(currentWorld, randomGenerator, k3, k8, l12);
        }

        if (randomGenerator.nextInt(8) == 0)
        {
            int l3 = chunk_X + randomGenerator.nextInt(16) + 8;
            int l8 = randomGenerator.nextInt(128);
            int i13 = chunk_Z + randomGenerator.nextInt(16) + 8;
            mushroomRedGen.generate(currentWorld, randomGenerator, l3, l8, i13);
        }

        for (int i4 = 0; i4 < reedsPerChunk; i4++)
        {
            int i9 = chunk_X + randomGenerator.nextInt(16) + 8;
            int j13 = chunk_Z + randomGenerator.nextInt(16) + 8;
            int k16 = randomGenerator.nextInt(128);
            reedGen.generate(currentWorld, randomGenerator, i9, k16, j13);
        }

        for (int j4 = 0; j4 < 10; j4++)
        {
            int j9 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k13 = randomGenerator.nextInt(128);
            int l16 = chunk_Z + randomGenerator.nextInt(16) + 8;
            reedGen.generate(currentWorld, randomGenerator, j9, k13, l16);
        }

        if (randomGenerator.nextInt(32) == 0)
        {
            int k4 = chunk_X + randomGenerator.nextInt(16) + 8;
            int k9 = randomGenerator.nextInt(128);
            int l13 = chunk_Z + randomGenerator.nextInt(16) + 8;
            (new WorldGenPumpkin()).generate(currentWorld, randomGenerator, k4, k9, l13);
        }

        for (int l4 = 0; l4 < cactiPerChunk; l4++)
        {
            int l9 = chunk_X + randomGenerator.nextInt(16) + 8;
            int i14 = randomGenerator.nextInt(128);
            int i17 = chunk_Z + randomGenerator.nextInt(16) + 8;
            cactusGen.generate(currentWorld, randomGenerator, l9, i14, i17);
        }

        if (generateLakes)
        {
            for (int i5 = 0; i5 < 50; i5++)
            {
                int i10 = chunk_X + randomGenerator.nextInt(16) + 8;
                int j14 = randomGenerator.nextInt(randomGenerator.nextInt(120) + 8);
                int j17 = chunk_Z + randomGenerator.nextInt(16) + 8;
                (new WorldGenLiquids(Block.waterMoving.blockID)).generate(currentWorld, randomGenerator, i10, j14, j17);
            }

            for (int j5 = 0; j5 < 20; j5++)
            {
                int j10 = chunk_X + randomGenerator.nextInt(16) + 8;
                int k14 = randomGenerator.nextInt(randomGenerator.nextInt(randomGenerator.nextInt(112) + 8) + 8);
                int k17 = chunk_Z + randomGenerator.nextInt(16) + 8;
                (new WorldGenLiquids(Block.lavaMoving.blockID)).generate(currentWorld, randomGenerator, j10, k14, k17);
            }
        }
    }

    /**
     * Standard ore generation helper. Generates most ores.
     */
    protected void genStandardOre1(int par1, WorldGenerator par2WorldGenerator, int par3, int par4)
    {
        for (int i = 0; i < par1; i++)
        {
            int j = chunk_X + randomGenerator.nextInt(16);
            int k = randomGenerator.nextInt(par4 - par3) + par3;
            int l = chunk_Z + randomGenerator.nextInt(16);
            par2WorldGenerator.generate(currentWorld, randomGenerator, j, k, l);
        }
    }

    /**
     * Standard ore generation helper. Generates Lapis Lazuli.
     */
    protected void genStandardOre2(int par1, WorldGenerator par2WorldGenerator, int par3, int par4)
    {
        for (int i = 0; i < par1; i++)
        {
            int j = chunk_X + randomGenerator.nextInt(16);
            int k = randomGenerator.nextInt(par4) + randomGenerator.nextInt(par4) + (par3 - par4);
            int l = chunk_Z + randomGenerator.nextInt(16);
            par2WorldGenerator.generate(currentWorld, randomGenerator, j, k, l);
        }
    }

    /**
     * facilitates the replacement of stone with ores during worldgen
     */
    protected void generateOres()
    {
        genStandardOre1(20, dirtGen, 0, 128);
        genStandardOre1(10, gravelGen, 0, 128);
        genStandardOre1(20, coalGen, 0, 128);
        genStandardOre1(20, ironGen, 0, 64);
        genStandardOre1(2, goldGen, 0, 32);
        genStandardOre1(8, redstoneGen, 0, 16);
        genStandardOre1(1, diamondGen, 0, 16);
        genStandardOre2(1, lapisGen, 16, 16);
    }
}
