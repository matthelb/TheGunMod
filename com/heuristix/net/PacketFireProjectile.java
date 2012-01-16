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
    static {
        try {
            Util.setPacketId(PacketFireProjectile.class, PACKET_ID, true, true);
        } catch (InvocationTargetException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public PacketFireProjectile() {
        super();
        this.packetType = 0;
    }
}
