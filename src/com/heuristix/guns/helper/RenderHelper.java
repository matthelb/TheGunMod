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

import org.lwjgl.opengl.GL11;

public class RenderHelper {

	private static final RenderBlocks RENDER_BLOCKS = new RenderBlocks();
	
	private RenderHelper() { }
	
	//TODO Copied method should be updated frequently
	public static void renderItemIntoGUI(RenderItem renderItem, FontRenderer fontRenderer,
			RenderEngine renderEngine, ItemStack itemStack, int x, int y, float scale) {
        int itemID = itemStack.itemID;
        int itemDamage = itemStack.getItemDamage();
        int iconIndex = itemStack.getIconIndex();
        int indexColor;
        float g;
        float b;
        float r;
        if (itemStack.getItem() instanceof ItemBlock && RenderBlocks.renderItemIn3d(Block.blocksList[itemStack.itemID].getRenderType())) {
            Block block = Block.blocksList[itemID];
            renderEngine.bindTexture(renderEngine.getTexture(block.getTextureFile()));
            GL11.glPushMatrix();
            GL11.glTranslatef((float)(x - 2), (float)(y + 3), -3 + renderItem.zLevel);
            GL11.glScalef(10, 10, 10);
            GL11.glTranslatef(1, 0.5f, 1);
            GL11.glScalef(1, 1, -1);
            GL11.glRotatef(210, 1, 0, 0);
            GL11.glRotatef(45, 0, 1, 0);
            indexColor = Item.itemsList[itemID].getColorFromItemStack(itemStack, 0);
            r = (float)(indexColor >> 16 & 255) / 255;
            g = (float)(indexColor >> 8 & 255) / 255;
            b = (float)(indexColor & 255) / 255;
            if (renderItem.field_77024_a) {
                GL11.glColor4f(r, g, b, 1);
            }
            GL11.glRotatef(-90, 0, 1, 0);
            RENDER_BLOCKS.useInventoryTint = renderItem.field_77024_a;
            RENDER_BLOCKS.renderBlockAsItem(block, itemDamage, 1);
            RENDER_BLOCKS.useInventoryTint = true;
            GL11.glPopMatrix();
        }
        else {
            int renderPasses;
            if (Item.itemsList[itemID].requiresMultipleRenderPasses()) {
                GL11.glDisable(GL11.GL_LIGHTING);
                renderEngine.bindTexture(renderEngine.getTexture(Item.itemsList[itemID].getTextureFile()));
                for (renderPasses = 0; renderPasses < Item.itemsList[itemID].getRenderPasses(itemDamage); ++renderPasses) {
                    indexColor = Item.itemsList[itemID].getIconIndex(itemStack, renderPasses);
                    int itemColor = Item.itemsList[itemID].getColorFromItemStack(itemStack, renderPasses);
                    r = (float)(itemColor >> 16 & 255) / 255;
                    g = (float)(itemColor >> 8 & 255) / 255;
                    b = (float)(itemColor & 255) / 255;
                    if (renderItem.field_77024_a) {
                        GL11.glColor4f(r, g, b, 1);
                    }
                    renderItem.renderTexturedQuad(x, y, itemColor % 16 * 16, itemColor / 16 * 16, (int) (16 * scale), (int) (16 * scale));
                }

                GL11.glEnable(GL11.GL_LIGHTING);
            }
            else if (iconIndex >= 0) {
                GL11.glDisable(GL11.GL_LIGHTING);
                renderEngine.bindTexture(renderEngine.getTexture(itemStack.getItem().getTextureFile()));
                renderPasses = Item.itemsList[itemID].getColorFromItemStack(itemStack, 0);
                r = (float)(renderPasses >> 16 & 255) / 255;
                g = (float)(renderPasses >> 8 & 255) / 255;
                b = (float)(renderPasses & 255) / 255;
                if (renderItem.field_77024_a) {
                    GL11.glColor4f(r, g, b, 1);
                }
                renderItem.renderTexturedQuad(x, y, iconIndex % 16 * 16, iconIndex / 16 * 16, (int) (16 * scale), (int) (16 * scale));
                GL11.glEnable(GL11.GL_LIGHTING);
            }
        }
        GL11.glEnable(GL11.GL_CULL_FACE);
    }
	
	//TODO Copied method should be updated frequently
	public static void renderItemOverlayIntoGUI(RenderItem renderItem, FontRenderer fontRenderer, RenderEngine renderEngine, ItemStack itemStack, int x, int y, float scale, int color) {
        if (itemStack != null) {
            if (itemStack.stackSize > 1) {
                String stackSize = String.valueOf(itemStack.stackSize);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                fontRenderer.drawStringWithShadow(stackSize, (int) (x + 17 * scale - fontRenderer.getStringWidth(stackSize)), (int) (y + 9 * scale), color);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
            }
            if (itemStack.isItemDamaged()) {
                int damageWidth = (int) (Math.round(13 - (double) itemStack.getItemDamageForDisplay() * 13 / itemStack.getMaxDamage()) * scale);
                int damageColor = (int) Math.round(255 - (double) itemStack.getItemDamageForDisplay() * 255 / itemStack.getMaxDamage());
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                Tessellator t = Tessellator.instance;
                int itemStackDamageColor2 = 255 - damageColor << 16 | damageColor << 8;
                int itemStackDamageColor = (255 - damageColor) / 4 << 16 | 16128;
                renderQuad(t, x + 2, y + 13, (int) (13 * scale), (int) (2 * scale), 0);
                renderQuad(t, x + 2, y + 13, (int) (12 * scale), (int) scale, itemStackDamageColor);
                renderQuad(t, x + 2, y + 13, damageWidth, (int) scale, itemStackDamageColor2);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glColor4f(1, 1, 1, 1);
            }
        }
    }
	
	//TODO Copied method should be updated frequently
	public static void renderQuad(Tessellator t, int x, int y, int width, int height, int colorOpaque) {
        t.startDrawingQuads();
        t.setColorOpaque_I(colorOpaque);
        t.addVertex(x, y, 0);
        t.addVertex(x, y + height, 0);
        t.addVertex(x + width, y + height, 0);
        t.addVertex(x + width, y, 0);
        t.draw();
    }

}
