package net.minecraft.src;

import java.util.*;

public class ComponentNetherBridgeStartPiece extends ComponentNetherBridgeCrossing3
{
    public StructureNetherBridgePieceWeight field_40296_a;
    public List field_40294_b;
    public List field_40295_c;
    public ArrayList field_40293_d;

    public ComponentNetherBridgeStartPiece(Random random, int i, int j)
    {
        super(random, i, j);
        field_40293_d = new ArrayList();
        field_40294_b = new ArrayList();
        StructureNetherBridgePieceWeight astructurenetherbridgepieceweight[] = StructureNetherBridgePieces.func_40536_a();
        int k = astructurenetherbridgepieceweight.length;
        for (int l = 0; l < k; l++)
        {
            StructureNetherBridgePieceWeight structurenetherbridgepieceweight = astructurenetherbridgepieceweight[l];
            structurenetherbridgepieceweight.field_40654_c = 0;
            field_40294_b.add(structurenetherbridgepieceweight);
        }

        field_40295_c = new ArrayList();
        astructurenetherbridgepieceweight = StructureNetherBridgePieces.func_40535_b();
        k = astructurenetherbridgepieceweight.length;
        for (int i1 = 0; i1 < k; i1++)
        {
            StructureNetherBridgePieceWeight structurenetherbridgepieceweight1 = astructurenetherbridgepieceweight[i1];
            structurenetherbridgepieceweight1.field_40654_c = 0;
            field_40295_c.add(structurenetherbridgepieceweight1);
        }
    }
}
