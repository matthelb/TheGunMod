package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet254ServerPing extends Packet
{
    public Packet254ServerPing()
    {
    }

    public void readPacketData(DataInputStream datainputstream)
    {
    }

    public void writePacketData(DataOutputStream dataoutputstream)
    {
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleServerPing(this);
    }

    public int getPacketSize()
    {
        return 0;
    }
}
