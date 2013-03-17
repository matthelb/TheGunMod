package com.heuristix.guns.client.render;

import java.util.EnumSet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.heuristix.ItemGun;
import com.heuristix.guns.client.handler.GunClientTickHandler;
import com.heuristix.guns.util.ReflectionFacade;

public class GunItemRenderer extends ItemRenderer implements IItemRenderer {
	
	private boolean currentlyRendering;
	private ItemGun lastGun;
	
	public GunItemRenderer(Minecraft minecraft) {
		super(minecraft);
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return !currentlyRendering && (item.getItem() instanceof ItemGun) && EnumSet.of(ItemRenderType.EQUIPPED).contains(type);
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		if (!GunClientTickHandler.isZoomed) {
			ItemGun gun = (ItemGun) item.getItem();
			if (gun.isReloading()) {
				GL11.glTranslatef(0, getReloadingTranslation(gun.getReloadPercent()), 0);
			}
			currentlyRendering = true;
			GL11.glTranslatef(0.5f, 0.5f, 0.5f);
			renderItem((EntityLiving) data[1], item, 0);
			currentlyRendering = false;
			lastGun = gun;
		}
	}
	
	public float getReloadingTranslation(float percent) {
		return (float) (4 * Math.pow(percent - 0.5f, 2)) - 1;
	}

	public static void setItemToRender(ItemStack item, ItemRenderer itemRenderer) {
		ReflectionFacade.getInstance().setFieldValue(ItemRenderer.class, itemRenderer, "itemToRender", item);
	}
	
}