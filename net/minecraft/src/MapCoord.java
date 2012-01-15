package net.minecraft.src;

public class MapCoord
{
    public byte field_28202_a;
    public byte field_28201_b;
    public byte field_28205_c;
    public byte field_28204_d;
    final MapData field_28203_e;

    public MapCoord(MapData mapdata, byte byte0, byte byte1, byte byte2, byte byte3)
    {
        field_28203_e = mapdata;

        field_28202_a = byte0;
        field_28201_b = byte1;
        field_28205_c = byte2;
        field_28204_d = byte3;
    }
}
