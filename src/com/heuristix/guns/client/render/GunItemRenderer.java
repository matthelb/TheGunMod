package com.heuristix.guns.client.render;

import java.util.EnumSet;

import org.lwjgl.opengl.GL11;

import com.heuristix.ItemGun;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class GunItemRenderer extends ItemRenderer implements IItemRenderer {

	private ItemGun lastGun;
	
	public GunItemRenderer(Minecraft minecraft) {
		super(minecraft);
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return (item.getItem() instanceof ItemGun) && EnumSet.of(ItemRenderType.EQUIPPED, ItemRenderType.INVENTORY).contains(type);
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch (type) {
			case EQUIPPED:
				ItemGun gun = (ItemGun) item.getItem();
				if (gun.isReloading()) {
					GL11.glTranslatef(0, getReloadingTranslation(), 0);
				}
				break;
			case INVENTORY:
				break;
			default:
				break;
		}
	}
	
	public float getReloadingTranslation() {
		return 0;
	}
	
}