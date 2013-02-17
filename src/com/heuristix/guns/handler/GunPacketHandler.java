package com.heuristix.guns.handler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.heuristix.ItemGun;
import com.heuristix.ItemProjectileShooter;
import com.heuristix.TheGunMod;
import com.heuristix.guns.ContainerCraftGuns;
import com.heuristix.guns.FireMode;
import com.heuristix.guns.client.handler.GunClientTickHandler;
import com.heuristix.guns.client.render.GunItemRenderer;
import com.heuristix.guns.helper.IOHelper;
import com.heuristix.guns.util.Log;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class GunPacketHandler implements IPacketHandler {

	public static final int PACKET_FIRE = 0;
	public static final int PACKET_RELOAD = 1;
	public static final int PACKET_STOP_RELOADING = 2;
	
	public static final int PACKET_FIRE_SUCCESS = 4;

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(packet.data));
        try {
        	if (player instanceof EntityPlayer) {
	            EntityPlayer p = (EntityPlayer) player;
	            switch (FMLCommonHandler.instance().getEffectiveSide()) {
	            	case SERVER:
			            switch (packet.channel) {
			                case "TGMShootAction":
			                    ItemProjectileShooter shooter = TheGunMod.getEquippedShooter(p);
			                    if (shooter != null && shooter.itemID == in.readInt()) {
			                        switch (in.readInt()) {
			                            case PACKET_FIRE:
			                                shooter.fire(p.worldObj, p);
			                                if (FireMode.values()[shooter.getFireMode()] == FireMode.BURST) {
			                                	GunServerTickHandler.trackBurstingShooter(shooter, p);
			                                }
			                                break;
			                            case PACKET_RELOAD:
			                                if (shooter instanceof ItemGun) {
			                                    ((ItemGun) shooter).reload(p);
			                                    GunServerTickHandler.trackReloadingGun((ItemGun) shooter, p);
			                                }
			                                break;
			                            case PACKET_STOP_RELOADING:
			                                if (shooter instanceof ItemGun) {
			                                    ((ItemGun) shooter).stopReloading(p.getCurrentEquippedItem(), p);
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
			                default:
			                	break;
			            }
			            break;
	            	case CLIENT:
	            		switch (packet.channel){
	            			case "TGMInfo":
	            				switch (in.readInt()) {
	            					case PACKET_FIRE_SUCCESS:
	            						ItemProjectileShooter shooter = TheGunMod.getEquippedShooter(p);
	    			                    if (shooter != null && shooter instanceof ItemGun) {
		            						double factor = Math.max(0.75, Math.min(com.heuristix.guns.helper.MathHelper.nextGaussian() + 1, 1.25));
		            				        GunClientTickHandler.recoilY += Math.min(factor * ((ItemGun) shooter).getRecoilY(), p.rotationPitch + 90.0F);
		            				        GunClientTickHandler.recoilX += factor * ((ItemGun) shooter).getRecoilX();
		            				    }
	            						break;
	            					default:
	            						break;
	            				}
	            				break;
	            			default:
	            				break;
	            		}
	            		break;
	            	default:
	            		break;
	            }
        	}
        } catch (IOException e) {
            Log.throwing(getClass(), "serverCustomPayload(NetServerHandler handler, Packet250CustomPayload packet)", e, TheGunMod.class);
        }
	}

	public static Packet250CustomPayload getArrowClickPacket(int arrow) {
	    try {
	        return new Packet250CustomPayload("TGMArrowClick", IOHelper.writeIntsToByteArray(arrow));
	    } catch (IOException e) {
	        Log.throwing(TheGunMod.class, "getArrowClickPacket(int arrow)", e, TheGunMod.class);
	    }
	    return null;
	}

	public static Packet250CustomPayload getShooterActionPacket(int shiftedIndex, int action) {
	    try {
	        return new Packet250CustomPayload("TGMShootAction", IOHelper.writeIntsToByteArray(shiftedIndex, action));
	    } catch (IOException e) {
	        Log.throwing(TheGunMod.class, "getShooterActionPacket(int shiftedIndex, int action)", e, TheGunMod.class);
	    }
	    return null;
	}
	
	public static Packet250CustomPayload getShooterInfoPacket(int... info) {
		try {
	        return new Packet250CustomPayload("TGMInfo", IOHelper.writeIntsToByteArray(info));
	    } catch (IOException e) {
	        Log.throwing(TheGunMod.class, "getShooterActionPacket(int shiftedIndex, int action)", e, TheGunMod.class);
	    }
	    return null;
	}

}
