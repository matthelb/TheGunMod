package com.heuristix.net;

import com.heuristix.ItemProjectileShooter;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.Packet230ModLoader;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/9/12
 * Time: 9:22 PM
 */
public class PacketFireProjectile extends Packet230ModLoader {

    public PacketFireProjectile() {
        super();
        this.packetType = 0;
    }
}
