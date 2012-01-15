package net.minecraft.src;

import java.io.*;

public class Packet9Respawn extends Packet
{
    public long mapSeed;
    public int respawnDimension;
    public int difficultySetting;
    public int worldHeight;
    public int creativeMode;
    public EnumWorldType field_46002_f;

    public Packet9Respawn()
    {
    }

    public Packet9Respawn(byte byte0, byte byte1, long l, EnumWorldType enumworldtype, int i, int j)
    {
        respawnDimension = byte0;
        difficultySetting = byte1;
        mapSeed = l;
        worldHeight = i;
        creativeMode = j;
        field_46002_f = enumworldtype;
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleRespawn(this);
    }

    public void readPacketData(DataInputStream datainputstream)
    throws IOException
    {
        respawnDimension = datainputstream.readByte();
        difficultySetting = datainputstream.readByte();
        creativeMode = datainputstream.readByte();
        worldHeight = datainputstream.readShort();
        mapSeed = datainputstream.readLong();
        String s = readString(datainputstream, 16);
        field_46002_f = EnumWorldType.func_46049_a(s);
        if (field_46002_f == null)
        {
            field_46002_f = EnumWorldType.DEFAULT;
        }
    }

    public void writePacketData(DataOutputStream dataoutputstream)
    throws IOException
    {
        dataoutputstream.writeByte(respawnDimension);
        dataoutputstream.writeByte(difficultySetting);
        dataoutputstream.writeByte(creativeMode);
        dataoutputstream.writeShort(worldHeight);
        dataoutputstream.writeLong(mapSeed);
        writeString(field_46002_f.name(), dataoutputstream);
    }

    public int getPacketSize()
    {
        return 13 + field_46002_f.name().length();
    }
}
