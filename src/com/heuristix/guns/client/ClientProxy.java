package com.heuristix.guns.client;

import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraftforge.client.MinecraftForgeClient;

import com.heuristix.ItemGun;
import com.heuristix.TheGunMod;
import com.heuristix.guns.CommonProxy;
import com.heuristix.guns.EntityBullet;
import com.heuristix.guns.EntityFlame;
import com.heuristix.guns.EntityGrenade;
import com.heuristix.guns.EntityIncendiaryBullet;
import com.heuristix.guns.EntityRocketGrenade;
import com.heuristix.guns.client.handler.GunClientTickHandler;
import com.heuristix.guns.client.render.GunItemRenderer;
import com.heuristix.guns.client.render.RenderBullet;
import com.heuristix.guns.client.render.RenderFlame;
import com.heuristix.guns.client.render.RenderGrenade;
import com.heuristix.guns.client.render.RenderRocketGrenade;
import com.heuristix.guns.util.ReflectionFacade;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

public class ClientProxy extends CommonProxy {

	private GunItemRenderer renderer;
	
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
	
	@Override 
	public void registerTextures(TheGunMod mod) {
		//ReflectionFacade.getInstance().setFieldValue(RenderEngine.class, FMLClientHandler.instance().getClient().renderEngine, "imageData", GLAllocation.createDirectByteBuffer(1048576));
		mod.registerTextures();
		MinecraftForgeClient.preloadTexture(Resources.BLOCK_TEXTURES);
	}
	
	@Override 
	public void registerItemRenderer(ItemGun item) {
		if (renderer == null) {
			renderer = new GunItemRenderer(FMLClientHandler.instance().getClient());
		}
		MinecraftForgeClient.registerItemRenderer(item.itemID, renderer);
	}

	@Override
	public void registerObfuscatedNames() {
		super.registerObfuscatedNames();
		ReflectionFacade names = ReflectionFacade.getInstance();
        names.putField(EntityRenderer.class, "cameraZoom", "X");
        names.putField(RenderEngine.class, "imageData", "g");
        names.putField(ItemRenderer.class, "itemToRender", "c");
        names.putField(EntityPlayerSP.class, "mc", "c");
        names.putField(Entity.class, "hurtResistantTime", "ae");
        names.putField(RenderItem.class, "itemRenderBlocks", "h");
        names.putField(SoundManager.class, "options", "f");
	}
	
	
	
}