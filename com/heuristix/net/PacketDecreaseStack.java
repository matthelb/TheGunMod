package com.heuristix.net;

import com.heuristix.Util;
import net.minecraft.src.Packet230ModLoader;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/22/12
 * Time: 12:22 AM
 */
public class PacketDecreaseStack extends Packet230ModLoader {

    public static final int PACKET_ID = 233;
    static {
        try {
            Util.setPacketId(PacketDecreaseStack.class, PACKET_ID, true, true);
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public PacketDecreaseStack(int slot, int amount) {
        this.packetType = 2;
        this.dataInt = new int[2];
        this.dataInt[0] = slot;
        this.dataInt[1] = amount;
    }
}