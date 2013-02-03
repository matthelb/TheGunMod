package com.heuristix.guns.client;

import com.heuristix.guns.CommonProxy;
import com.heuristix.guns.EntityBullet;
import com.heuristix.guns.EntityFlame;
import com.heuristix.guns.EntityGrenade;
import com.heuristix.guns.EntityIncendiaryBullet;
import com.heuristix.guns.EntityRocketGrenade;
import com.heuristix.guns.client.handler.GunClientTickHandler;
import com.heuristix.guns.client.render.RenderBullet;
import com.heuristix.guns.client.render.RenderFlame;
import com.heuristix.guns.client.render.RenderGrenade;
import com.heuristix.guns.client.render.RenderRocketGrenade;

import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerKeyHandler(KeyHandler handler) {
		KeyBindingRegistry.registerKeyBinding(handler);
	}

	@Override
	public void registerTickHandler() {
		super.registerTickHandler();
		TickRegistry.registerTickHandler(new GunClientTickHandler(), Side.CLIENT);
	}
	
	@Override
	public void registerRenderers() {
		RenderBullet renderBullet = new RenderBullet();
		RenderingRegistry.registerEntityRenderingHandler(EntityBullet.class, renderBullet);
		RenderingRegistry.registerEntityRenderingHandler(EntityIncendiaryBullet.class, renderBullet);
		RenderingRegistry.registerEntityRenderingHandler(EntityFlame.class, new RenderFlame());
		RenderingRegistry.registerEntityRenderingHandler(EntityGrenade.class, new RenderGrenade());
		RenderingRegistry.registerEntityRenderingHandler(EntityRocketGrenade.class, new RenderRocketGrenade());
	}
	
	
}