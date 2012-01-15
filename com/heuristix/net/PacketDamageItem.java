package com.heuristix.net;

import com.heuristix.Util;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet230ModLoader;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/13/12
 * Time: 9:53 PM
 */
public class PacketDamageItem extends Packet230ModLoader {

    public static final int PACKET_ID = 232;
    static {
        try {
            Util.setPacketId(PacketDamageItem.class, PACKET_ID, true, true);
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
    public PacketDamageItem(int id, int slot, int damage) {
        this.packetType = 1;
        this.dataInt = new int[3];
        dataInt[0] = id;
        dataInt[1] = slot;
        dataInt[2] = damage;
    }
}
