package com.heuristix.guns.client.handler;

import java.util.EnumSet;

import com.heuristix.ItemGun;
import com.heuristix.ItemProjectileShooter;
import com.heuristix.TheGunMod;
import com.heuristix.guns.handler.GunPacketHandler;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.TickType;

public class GunKeyHandler extends KeyHandler {

	static {
		ModLoader.addLocalization("key.reload", "Reload");
        ModLoader.addLocalization("key.zoom", "Zoom");
	}
	
	public GunKeyHandler(int reloadKey, int zoomKey) {
		super(new KeyBinding[]{new KeyBinding("key.reload", reloadKey), new KeyBinding("key.zoom", zoomKey)});
		
	}

	@Override
	public String getLabel() {
		return "guns.keys";
	}

	@Override
	public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
		if (tickEnd) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
        if (mc != null && mc.inGameHasFocus) {
            EntityPlayer player = mc.thePlayer;
            if (player != null) {
            	ItemProjectileShooter shooter = TheGunMod.getEquippedShooter(player);
            	if (shooter instanceof ItemGun) {
            		ItemGun gun = (ItemGun) shooter;
	            	switch (kb.keyDescription) {
		            	case "key.reload":
		            		ModLoader.clientSendPacket(GunPacketHandler.getShooterActionPacket(gun.itemID, GunPacketHandler.PACKET_RELOAD));
	                        GunClientTickHandler.isZoomed = false;
		            		break;
		            	case "key.zoom":
		            		if (gun.getZoom() > 1.0f || gun.getScope() > 0) {
	                            GunClientTickHandler.isZoomed = !GunClientTickHandler.isZoomed;
	                            if (gun.isReloading()) {
	                                ModLoader.clientSendPacket(GunPacketHandler.getShooterActionPacket(gun.itemID, GunPacketHandler.PACKET_STOP_RELOADING));
	                            }
	                        }
		            		break;
		            	default:
		            			break;
	            	}
            	}
            }
        }
	}

	@Override
	public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

}
