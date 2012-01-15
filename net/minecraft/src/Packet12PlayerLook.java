package net.minecraft.src;

import java.io.*;

public class Packet12PlayerLook extends Packet10Flying
{
    public Packet12PlayerLook()
    {
        rotating = true;
    }

    public void readPacketData(DataInputStream datainputstream)
    throws IOException
    {
        yaw = datainputstream.readFloat();
        pitch = datainputstream.readFloat();
        super.readPacketData(datainputstream);
    }

    public void writePacketData(DataOutputStream dataoutputstream)
    throws IOException
    {
        dataoutputstream.writeFloat(yaw);
        dataoutputstream.writeFloat(pitch);
        super.writePacketData(dataoutputstream);
    }

    public int getPacketSize()
    {
        return 9;
    }
}
