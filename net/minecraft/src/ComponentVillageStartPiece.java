package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class ComponentVillageStartPiece extends ComponentVillageWell
{
    public WorldChunkManager worldChunkMngr;
    public int field_35390_b;
    public StructureVillagePieceWeight structVillagePieceWeight;
    public ArrayList structureVillageWeightedPieceList;
    public ArrayList field_35389_e;
    public ArrayList field_35387_f;

    public ComponentVillageStartPiece(WorldChunkManager worldchunkmanager, int i, Random random, int j, int k, ArrayList arraylist, int l)
    {
        super(0, random, j, k);
        field_35389_e = new ArrayList();
        field_35387_f = new ArrayList();
        worldChunkMngr = worldchunkmanager;
        structureVillageWeightedPieceList = arraylist;
        field_35390_b = l;
    }

    public WorldChunkManager getWorldChunkMngr()
    {
        return worldChunkMngr;
    }
}
