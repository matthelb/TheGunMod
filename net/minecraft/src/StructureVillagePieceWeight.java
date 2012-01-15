package net.minecraft.src;

public class StructureVillagePieceWeight
{
    public Class villageComponentClass;
    public final int villagePieceWeight;
    public int villagePiecesSpawned;
    public int villagePiecesLimit;

    public StructureVillagePieceWeight(Class class1, int i, int j)
    {
        villageComponentClass = class1;
        villagePieceWeight = i;
        villagePiecesLimit = j;
    }

    public boolean canSpawnMoreVillagePiecesOfType(int i)
    {
        return villagePiecesLimit == 0 || villagePiecesSpawned < villagePiecesLimit;
    }

    public boolean canSpawnMoreVillagePieces()
    {
        return villagePiecesLimit == 0 || villagePiecesSpawned < villagePiecesLimit;
    }
}
