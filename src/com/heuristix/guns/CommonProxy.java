package com.heuristix.guns;

import net.minecraft.entity.player.EntityPlayerMP;
import com.heuristix.ItemGun;
import com.heuristix.TheGunMod;
import com.heuristix.guns.handler.GunServerTickHandler;
import com.heuristix.guns.util.ReflectionFacade;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy {

	public void registerKeyHandler(KeyHandler handler) {
		
	}
	
	public void registerTickHandler() {
		TickRegistry.registerTickHandler(new GunServerTickHandler(), Side.SERVER);
	}
	
	public void registerRenderers() {
		
	}
	
	public void registerTextures(TheGunMod mod) {
		
	}

	public void registerItemRenderer(ItemGun item) {	
	}

	public void registerObfuscatedNames() {
        ReflectionFacade.getInstance().putField(EntityPlayerMP.class, "currentWindowId", "ct");
	}

}
