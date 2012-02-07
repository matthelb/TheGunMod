package com.heuristix.net;

import com.heuristix.Util;
import net.minecraft.src.Packet230ModLoader;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/13/12
 * Time: 9:53 PM
 */
public class PacketDamageItem extends Packet230ModLoader {

    public static final int PACKET_ID = 232;
    static {

        Util.setPacketId(PacketDamageItem.class, PACKET_ID, true, true);
    }

    public PacketDamageItem(int id, int slot, int damage) {
        this.packetType = 1;
        this.dataInt = new int[3];
        dataInt[0] = id;
        dataInt[1] = slot;
        dataInt[2] = damage;
    }
}
