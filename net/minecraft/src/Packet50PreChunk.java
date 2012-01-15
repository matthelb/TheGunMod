package net.minecraft.src;

import java.io.*;

public class Packet50PreChunk extends Packet
{
    public int xPosition;
    public int yPosition;
    public boolean mode;

    public Packet50PreChunk()
    {
        isChunkDataPacket = false;
    }

    public Packet50PreChunk(int i, int j, boolean flag)
    {
        isChunkDataPacket = false;
        xPosition = i;
        yPosition = j;
        mode = flag;
    }

    public void readPacketData(DataInputStream datainputstream)
    throws IOException
    {
        xPosition = datainputstream.readInt();
        yPosition = datainputstream.readInt();
        mode = datainputstream.read() != 0;
    }

    public void writePacketData(DataOutputStream dataoutputstream)
    throws IOException
    {
        dataoutputstream.writeInt(xPosition);
        dataoutputstream.writeInt(yPosition);
        dataoutputstream.write(mode ? 1 : 0);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handlePreChunk(this);
    }

    public int getPacketSize()
    {
        return 9;
    }
}
