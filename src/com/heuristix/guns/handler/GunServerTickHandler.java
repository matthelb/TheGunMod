package com.heuristix.guns.handler;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;

import com.heuristix.ItemGun;
import com.heuristix.ItemProjectileShooter;

import cpw.mods.fml.common.TickType;

public class GunServerTickHandler extends GunTickHandler {

	private static Map<ItemProjectileShooter, EntityPlayer> burstingShooters = new HashMap<ItemProjectileShooter, EntityPlayer>();
	private static Map<ItemGun, EntityPlayer> reloadingGuns = new HashMap<ItemGun, EntityPlayer>();
	
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
		
		Map<ItemGun, EntityPlayer> stillReloadingGuns = new HashMap<ItemGun, EntityPlayer>();
		for (Map.Entry<ItemGun, EntityPlayer> entry : reloadingGuns.entrySet()) {
			EntityPlayer p = entry.getValue();
			ItemGun g = entry.getKey();
			if (g.equals(p.getCurrentEquippedItem().getItem()) && g.isReloading()) {
				if (System.currentTimeMillis() >= g.getReloadFinishTime()) {
					g.finishReloading(p.getCurrentEquippedItem() ,p);
				}
				stillReloadingGuns.put(g,  p);
			} else {
				g.stopReloading(p.getCurrentEquippedItem(), p);
			}	
		}
		reloadingGuns = stillReloadingGuns;
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
	
	public static void trackReloadingGun(ItemGun gun, EntityPlayer owner) {
		reloadingGuns.put(gun, owner);
	}

}
