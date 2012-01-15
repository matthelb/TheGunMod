package net.minecraft.src;

import java.io.*;

public class Packet250CustomPayload extends Packet
{
    public String field_44005_a;
    public int field_44003_b;
    public byte field_44004_c[];

    public Packet250CustomPayload()
    {
    }

    public void readPacketData(DataInputStream datainputstream)
    throws IOException
    {
        field_44005_a = readString(datainputstream, 16);
        field_44003_b = datainputstream.readShort();
        if (field_44003_b > 0 && field_44003_b < 32767)
        {
            field_44004_c = new byte[field_44003_b];
            datainputstream.read(field_44004_c);
        }
    }

    public void writePacketData(DataOutputStream dataoutputstream)
    throws IOException
    {
        writeString(field_44005_a, dataoutputstream);
        dataoutputstream.writeShort((short)field_44003_b);
        if (field_44004_c != null)
        {
            dataoutputstream.write(field_44004_c);
        }
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.func_44001_a(this);
    }

    public int getPacketSize()
    {
        return 2 + field_44005_a.length() * 2 + 2 + field_44003_b;
    }
}
