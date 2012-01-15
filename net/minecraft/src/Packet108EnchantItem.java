package net.minecraft.src;

import java.io.*;

public class Packet108EnchantItem extends Packet
{
    public int windowId;
    public int enchantment;

    public Packet108EnchantItem()
    {
    }

    public void processPacket(NetHandler nethandler)
    {
        nethandler.handleEnchantItem(this);
    }

    public void readPacketData(DataInputStream datainputstream)
    throws IOException
    {
        windowId = datainputstream.readByte();
        enchantment = datainputstream.readByte();
    }

    public void writePacketData(DataOutputStream dataoutputstream)
    throws IOException
    {
        dataoutputstream.writeByte(windowId);
        dataoutputstream.writeByte(enchantment);
    }

    public int getPacketSize()
    {
        return 2;
    }
}
