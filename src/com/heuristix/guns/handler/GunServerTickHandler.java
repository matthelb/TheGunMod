package com.heuristix.guns.handler;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;

import com.heuristix.ItemProjectileShooter;

import cpw.mods.fml.common.TickType;

public class GunServerTickHandler extends GunTickHandler {

	private static Map<ItemProjectileShooter, EntityPlayer> burstingShooters = new HashMap<ItemProjectileShooter, EntityPlayer>();
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		Map<ItemProjectileShooter, EntityPlayer> stillBurstingShooters = new HashMap<ItemProjectileShooter, EntityPlayer>();
		for (Map.Entry<ItemProjectileShooter, EntityPlayer> entry : burstingShooters.entrySet()) {
			EntityPlayer p = entry.getValue();
			ItemProjectileShooter s = entry.getKey();
			if (s.equals(p.getCurrentEquippedItem().getItem()) && s.isBursting()) {
				s.burst(p.worldObj, p);
				stillBurstingShooters.put(s, p);
			}
		}
		burstingShooters = stillBurstingShooters;
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
	}
	
	public static void trackBurstingShooter(ItemProjectileShooter shooter, EntityPlayer owner) {
		burstingShooters.put(shooter, owner);
	}

}
