package com.heuristix.net;

import com.heuristix.Util;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet230ModLoader;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/23/12
 * Time: 9:46 PM
 */
public class PacketCraftGunsArrowClick extends Packet230ModLoader {

    public static final int PACKET_ID = 234;
    static {
        Util.setPacketId(PacketCraftGunsArrowClick.class, PACKET_ID, true, true);
    }

    public PacketCraftGunsArrowClick(int arrow) {
        this.packetType = 2;
        this.dataInt = new int[1];
        this.dataInt[0] = arrow;
    }
}
