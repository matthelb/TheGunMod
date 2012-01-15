package net.minecraft.src;

public class GenLayerDownfallMix extends GenLayer
{
    private GenLayer field_35035_b;
    private int field_35036_c;

    public GenLayerDownfallMix(GenLayer genlayer, GenLayer genlayer1, int i)
    {
        super(0L);
        parent = genlayer1;
        field_35035_b = genlayer;
        field_35036_c = i;
    }

    public int[] getInts(int i, int j, int k, int l)
    {
        int ai[] = parent.getInts(i, j, k, l);
        int ai1[] = field_35035_b.getInts(i, j, k, l);
        int ai2[] = IntCache.getIntCache(k * l);
        for (int i1 = 0; i1 < k * l; i1++)
        {
            ai2[i1] = ai1[i1] + (BiomeGenBase.biomeList[ai[i1]].getIntRainfall() - ai1[i1]) / (field_35036_c + 1);
        }

        return ai2;
    }
}
