package com.heuristix.net;

import com.heuristix.Util;
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

    public PacketDamageItem() {
        this.packetType = 1;
    }
}
