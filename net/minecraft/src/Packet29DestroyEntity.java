package net.minecraft.src;

import java.io.*;

public class Packet29DestroyEntity extends Packet
{
    public int entityId;

    public Packet29DestroyEntity()
    {
    }

    public Packet29DestroyEntity(int i)
    {
        entityId = i;
    }

    public void readPacketData(DataInputStream datainputstream)
    throws IOException
    {
        entityId = datainputstream.readInt();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
    throws IOException
    {
        dataoutputstream.writeInt(entityId);
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleDestroyEntity(this);
    }

    public int getPacketSize()
    {
        return 4;
    }
}
