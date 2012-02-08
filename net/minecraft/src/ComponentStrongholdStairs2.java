package net.minecraft.src;

import java.util.ArrayList;
import java.util.Random;

public class ComponentStrongholdStairs2 extends ComponentStrongholdStairs
{
    public StructureStrongholdPieceWeight field_35329_a;
    public ComponentStrongholdPortalRoom field_40317_b;
    public ArrayList field_35328_b;

    public ComponentStrongholdStairs2(int i, Random random, int j, int k)
    {
        super(0, random, j, k);
        field_35328_b = new ArrayList();
    }

    public ChunkPosition getCenter()
    {
        if (field_40317_b != null)
        {
            return field_40317_b.getCenter();
        }
        else
        {
            return super.getCenter();
        }
    }
}
