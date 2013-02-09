package com.heuristix.guns;

import com.heuristix.TheGunMod;
import com.heuristix.guns.handler.GunServerTickHandler;
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

}
