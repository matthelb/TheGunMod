package net.minecraft.src;

import java.io.*;

public class Packet53BlockChange extends Packet
{
    public int xPosition;
    public int yPosition;
    public int zPosition;
    public int type;
    public int metadata;

    public Packet53BlockChange()
    {
        isChunkDataPacket = true;
    }

    public Packet53BlockChange(int i, int j, int k, World world)
    {
        isChunkDataPacket = true;
        xPosition = i;
        yPosition = j;
        zPosition = k;
        type = world.getBlockId(i, j, k);
        metadata = world.getBlockMetadata(i, j, k);
    }

    public void readPacketData(DataInputStream datainputstream)
    throws IOException
    {
        xPosition = datainputstream.readInt();
        yPosition = datainputstream.read();
        zPosition = datainputstream.readInt();
        type = datainputstream.read();
        metadata = datainputstream.read();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
    throws IOException
    {
        dataoutputstream.writeInt(xPosition);
        dataoutputstream.write(yPosition);
        dataoutputstream.writeInt(zPosition);
        dataoutputstream.write(type);
        dataoutputstream.write(metadata);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleBlockChange(this);
    }

    public int getPacketSize()
    {
        return 11;
    }
}
