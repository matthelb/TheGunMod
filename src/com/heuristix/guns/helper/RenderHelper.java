package com.heuristix.guns.helper;

import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderEngine;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;

public class RenderHelper {

	private static final RenderBlocks RENDER_BLOCKS = new RenderBlocks();

	private RenderHelper() {
	}

	// TODO Copied method should be updated frequently
	public static void renderItemIntoGUI(RenderItem itemRenderer, FontRenderer fontRenderer, RenderEngine renderEngine, ItemStack itemStack, int x, int y, float scale) {
		int k = itemStack.itemID;
		int l = itemStack.getItemDamage();
		Icon icon = itemStack.getIconIndex();
		float f;
		float f1;
		float f2;

		Block block = (k < Block.blocksList.length ? Block.blocksList[k] : null);
		if (itemStack.getItemSpriteNumber() == 0 && block != null && RenderBlocks.renderItemIn3d(Block.blocksList[k].getRenderType())) {
			renderEngine.bindTexture("/terrain.png");
			GL11.glPushMatrix();
			GL11.glTranslatef((float) (x - 2), (float) (y + 3), -3.0F + itemRenderer.zLevel);
			GL11.glScalef(10.0F, 10.0F, 10.0F);
			GL11.glTranslatef(1.0F, 0.5F, 1.0F);
			GL11.glScalef(1.0F, 1.0F, -1.0F);
			GL11.glRotatef(210.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			int i1 = Item.itemsList[k].getColorFromItemStack(itemStack, 0);
			f2 = (float) (i1 >> 16 & 255) / 255.0F;
			f = (float) (i1 >> 8 & 255) / 255.0F;
			f1 = (float) (i1 & 255) / 255.0F;
			if (itemRenderer.renderWithColor) {
				GL11.glColor4f(f2, f, f1, 1.0F);
			}
			GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);
			RENDER_BLOCKS.useInventoryTint = itemRenderer.renderWithColor;
			RENDER_BLOCKS.renderBlockAsItem(block, l, 1.0F);
			RENDER_BLOCKS.useInventoryTint = true;
			GL11.glPopMatrix();
		} else {
			int j1;
			if (Item.itemsList[k].requiresMultipleRenderPasses()) {
				GL11.glDisable(GL11.GL_LIGHTING);
				renderEngine.bindTexture(itemStack.getItemSpriteNumber() == 0 ? "/terrain.png" : "/gui/items.png");
				for (j1 = 0; j1 < Item.itemsList[k].getRenderPasses(l); ++j1) {
					Icon icon1 = Item.itemsList[k].getIcon(itemStack, j1);
					int k1 = Item.itemsList[k].getColorFromItemStack(itemStack, j1);
					f = (float) (k1 >> 16 & 255) / 255.0F;
					f1 = (float) (k1 >> 8 & 255) / 255.0F;
					float f3 = (float) (k1 & 255) / 255.0F;
					if (itemRenderer.renderWithColor) {
						GL11.glColor4f(f, f1, f3, 1.0F);
					}
					itemRenderer.renderIcon(x, y, icon1, (int) (16 * scale), (int) (16 * scale));
				}
				GL11.glEnable(GL11.GL_LIGHTING);
			} else {
				GL11.glDisable(GL11.GL_LIGHTING);
				if (itemStack.getItemSpriteNumber() == 0) {
					renderEngine.bindTexture("/terrain.png");
				} else {
					renderEngine.bindTexture("/gui/items.png");
				}
				if (icon == null) {
					icon = renderEngine.getMissingIcon(itemStack.getItemSpriteNumber());
				}
				j1 = Item.itemsList[k].getColorFromItemStack(itemStack, 0);
				float f4 = (float) (j1 >> 16 & 255) / 255.0F;
				f2 = (float) (j1 >> 8 & 255) / 255.0F;
				f = (float) (j1 & 255) / 255.0F;
				if (itemRenderer.renderWithColor) {
					GL11.glColor4f(f4, f2, f, 1.0F);
				}
				itemRenderer.renderIcon(x, y, icon, (int) (16 * scale), (int) (16 * scale));
				GL11.glEnable(GL11.GL_LIGHTING);
			}
		}

		GL11.glEnable(GL11.GL_CULL_FACE);
	}

	// TODO Copied method should be updated frequently
	public static void renderItemOverlayIntoGUI(FontRenderer fontRenderer, RenderEngine renderEngine, ItemStack itemStack, int x, int y, float scale, int color) {
		renderItemStack(fontRenderer, renderEngine, itemStack, x, y, scale, (String) null, color);
	}

	// TODO Copied method should be updated frequently
	public static void renderItemStack(FontRenderer fontRenderer, RenderEngine renderEngine, ItemStack itemStack, int x, int y, float scale, String str, int color) {
		if (itemStack != null) {
			if (itemStack.stackSize > 1 || str != null) {
				String s1 = str == null ? String
						.valueOf(itemStack.stackSize) : str;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				fontRenderer.drawStringWithShadow(s1, x + 19 - 2
						- fontRenderer.getStringWidth(s1), y + 6 + 3,
						color);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}

			if (itemStack.isItemDamaged()) {
				int k = (int) Math.round(13.0D
						- (double) itemStack.getItemDamageForDisplay()
						* 13.0D / (double) itemStack.getMaxDamage() * scale);
				int l = (int) Math.round(255.0D
						- (double) itemStack.getItemDamageForDisplay()
						* 255.0D / (double) itemStack.getMaxDamage());
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				Tessellator tessellator = Tessellator.instance;
				int i1 = 255 - l << 16 | l << 8;
				int j1 = (255 - l) / 4 << 16 | 16128;
				renderQuad(tessellator, x + 2, y + 13, (int) (13 * scale), (int) (2 * scale), 0);
				renderQuad(tessellator, x + 2, y + 13, (int) (12 * scale), (int) scale, j1);
				renderQuad(tessellator, x + 2, y + 13, k, (int) scale, i1);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			}
		}
	}

	// TODO Copied method should be updated frequently
	public static void renderQuad(Tessellator par1Tessellator, int par2, int par3, int par4, int par5, int par6) {
		par1Tessellator.startDrawingQuads();
		par1Tessellator.setColorOpaque_I(par6);
		par1Tessellator.addVertex((double) (par2 + 0), (double) (par3 + 0), 0.0D);
		par1Tessellator.addVertex((double) (par2 + 0), (double) (par3 + par5), 0.0D);
		par1Tessellator.addVertex((double) (par2 + par4), (double) (par3 + par5), 0.0D);
		par1Tessellator.addVertex((double) (par2 + par4), (double) (par3 + 0), 0.0D);
		par1Tessellator.draw();
	}

	public static void renderCuboid(Tessellator t, double x, double y, double z, float yaw, float pitch, float scaleX, float scaleY, float scaleZ, float r, float g, float b, float a) {
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glColor4f(r, g, b, a);
		GL11.glTranslated(x, y, z);
		GL11.glRotatef(pitch, -1, 0, 0);
		GL11.glRotatef(yaw, 0, -1, 0);
		t.startDrawingQuads();
		t.setNormal(0, 0, -1);
		t.addVertexWithUV(-scaleX, scaleY, -scaleZ, 0, 1);
		t.addVertexWithUV(scaleX, scaleY, -scaleZ, 1, 1);
		t.addVertexWithUV(scaleX, -scaleY, -scaleZ, 1, 0);
		t.addVertexWithUV(-scaleX, -scaleY, -scaleZ, 0, 0);
		t.setNormal(0, 0, 1);
		t.addVertexWithUV(-scaleX, -scaleY, scaleZ, 0, 1);
		t.addVertexWithUV(scaleX, -scaleY, scaleZ, 1, 1);
		t.addVertexWithUV(scaleX, scaleY, scaleZ, 1, 0);
		t.addVertexWithUV(-scaleX, scaleY, scaleZ, 0, 0);
		t.setNormal(0, -1, 0);
		t.addVertexWithUV(-scaleX, -scaleY, -scaleZ, 0, 1);
		t.addVertexWithUV(scaleX, -scaleY, -scaleZ, 1, 1);
		t.addVertexWithUV(scaleX, -scaleY, scaleZ, 1, 0);
		t.addVertexWithUV(-scaleX, -scaleY, scaleZ, 0, 0);
		t.setNormal(0, 1, 0);
		t.addVertexWithUV(-scaleX, scaleY, scaleZ, 0, 1);
		t.addVertexWithUV(scaleX, scaleY, scaleZ, 1, 1);
		t.addVertexWithUV(scaleX, scaleY, -scaleZ, 1, 0);
		t.addVertexWithUV(-scaleX, scaleY, -scaleZ, 0, 0);
		t.setNormal(-1, 0, 0);
		t.addVertexWithUV(-scaleX, -scaleY, scaleZ, 0, 1);
		t.addVertexWithUV(-scaleX, scaleY, scaleZ, 1, 1);
		t.addVertexWithUV(-scaleX, scaleY, -scaleZ, 1, 0);
		t.addVertexWithUV(-scaleX, -scaleY, -scaleZ, 0, 0);
		t.setNormal(1, 0, 0);
		t.addVertexWithUV(scaleX, -scaleY, -scaleZ, 0, 1);
		t.addVertexWithUV(scaleX, scaleY, -scaleZ, 1, 1);
		t.addVertexWithUV(scaleX, scaleY, scaleZ, 1, 0);
		t.addVertexWithUV(scaleX, -scaleY, scaleZ, 0, 0);
		t.draw();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glPopMatrix();
	}

}
