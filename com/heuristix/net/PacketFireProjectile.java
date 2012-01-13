package com.heuristix.net;

import com.heuristix.ItemProjectileShooter;
import com.heuristix.Util;
import net.minecraft.src.Packet230ModLoader;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/9/12
 * Time: 9:22 PM
 */
public class PacketFireProjectile extends Packet230ModLoader {

    public static final int PACKET_ID = 231;

    public PacketFireProjectile() {
        super();
        this.packetType = 0;
    }
}
