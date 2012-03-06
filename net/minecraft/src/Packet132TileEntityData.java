package net.minecraft.src;

import java.io.*;

public class Packet132TileEntityData extends Packet
{
    public int field_48122_a;
    public int field_48120_b;
    public int field_48121_c;
    public int field_48118_d;
    public int field_48119_e;
    public int field_48116_f;
    public int field_48117_g;

    public Packet132TileEntityData()
    {
        isChunkDataPacket = true;
    }

    public Packet132TileEntityData(int par1, int par2, int par3, int par4, int par5)
    {
        isChunkDataPacket = true;
        field_48122_a = par1;
        field_48120_b = par2;
        field_48121_c = par3;
        field_48118_d = par4;
        field_48119_e = par5;
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        field_48122_a = par1DataInputStream.readInt();
        field_48120_b = par1DataInputStream.readShort();
        field_48121_c = par1DataInputStream.readInt();
        field_48118_d = par1DataInputStream.readByte();
        field_48119_e = par1DataInputStream.readInt();
        field_48116_f = par1DataInputStream.readInt();
        field_48117_g = par1DataInputStream.readInt();
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(field_48122_a);
        par1DataOutputStream.writeShort(field_48120_b);
        par1DataOutputStream.writeInt(field_48121_c);
        par1DataOutputStream.writeByte((byte)field_48118_d);
        par1DataOutputStream.writeInt(field_48119_e);
        par1DataOutputStream.writeInt(field_48116_f);
        par1DataOutputStream.writeInt(field_48117_g);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.func_48071_a(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 25;
    }
}
