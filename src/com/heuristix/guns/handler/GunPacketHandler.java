package com.heuristix.guns.handler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import com.heuristix.ItemGun;
import com.heuristix.ItemProjectileShooter;
import com.heuristix.TheGunMod;
import com.heuristix.guns.ContainerCraftGuns;
import com.heuristix.guns.util.Log;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class GunPacketHandler implements IPacketHandler {

	public static final int PACKET_FIRE = 0;
	public static final int PACKET_BURST = 1;
	public static final int PACKET_RELOAD = 2;
	public static final int PACKET_STOP_RELOADING = 3;

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(packet.data));
        try {
        	if (player instanceof EntityPlayer) {
	            EntityPlayer p = (EntityPlayer) player;
	            switch (packet.channel) {
	                case "TGMShootAction":
	                    ItemProjectileShooter shooter = TheGunMod.getEquippedShooter(p);
	                    if (shooter != null && shooter.itemID == in.readInt()) {
	                        switch (in.readInt()) {
	                            case PACKET_FIRE:
	                                shooter.fire(p.worldObj, p);
	                                break;
	                            case PACKET_BURST:
	                                shooter.burst(p.worldObj, p);
	                                break;
	                            case PACKET_RELOAD:
	                                if (shooter instanceof ItemGun) {
	                                    ((ItemGun) shooter).reload(p);
	                                }
	                                break;
	                            case PACKET_STOP_RELOADING:
	                                if (shooter instanceof ItemGun) {
	                                    ((ItemGun) shooter).stopReloading();
	                                }
	                                break;
	                        }
	                    }
	                    break;
	                case "TGMArrowClick":
	                    if (p.openContainer instanceof ContainerCraftGuns) {
	                        ContainerCraftGuns container = (ContainerCraftGuns) p.openContainer;
	                        container.onArrowClick(in.readInt());
	                    }
	                    break;
	            }
        	}
        } catch (IOException e) {
            Log.throwing(getClass(), "serverCustomPayload(NetServerHandler handler, Packet250CustomPayload packet)", e, TheGunMod.class);
        }
	}

	public static Packet250CustomPayload getShooterActionPacket(int shiftedIndex, int action) {
	    try {
	        return new Packet250CustomPayload("TGMShootAction", TheGunMod.writeIntsToByteArray(shiftedIndex, action));
	    } catch (IOException e) {
	        Log.throwing(TheGunMod.class, "getShooterActionPacket(int shiftedIndex, int action)", e, TheGunMod.class);
	    }
	    return null;
	}

}
