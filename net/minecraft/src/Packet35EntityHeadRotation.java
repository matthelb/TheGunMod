package net.minecraft.src;

import java.io.*;

public class Packet35EntityHeadRotation extends Packet
{
    public int field_48115_a;
    public byte field_48114_b;

    public Packet35EntityHeadRotation()
    {
    }

    public Packet35EntityHeadRotation(int par1, byte par2)
    {
        field_48115_a = par1;
        field_48114_b = par2;
    }

    /**
     * Abstract. Reads the raw packet data from the data stream.
     */
    public void readPacketData(DataInputStream par1DataInputStream) throws IOException
    {
        field_48115_a = par1DataInputStream.readInt();
        field_48114_b = par1DataInputStream.readByte();
    }

    /**
     * Abstract. Writes the raw packet data to the data stream.
     */
    public void writePacketData(DataOutputStream par1DataOutputStream) throws IOException
    {
        par1DataOutputStream.writeInt(field_48115_a);
        par1DataOutputStream.writeByte(field_48114_b);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(NetHandler par1NetHandler)
    {
        par1NetHandler.func_48072_a(this);
    }

    /**
     * Abstract. Return the size of the packet (not counting the header).
     */
    public int getPacketSize()
    {
        return 5;
    }
}
