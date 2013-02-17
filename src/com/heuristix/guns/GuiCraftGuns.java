package com.heuristix.guns;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

import com.heuristix.ItemGun;
import com.heuristix.guns.client.Resources;
import com.heuristix.guns.handler.GunPacketHandler;
import com.heuristix.guns.helper.InventoryHelper;
import com.heuristix.guns.helper.RenderHelper;

import cpw.mods.fml.common.network.PacketDispatcher;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/16/12
 * Time: 11:57 PM
 */
public class GuiCraftGuns extends GuiContainer {

    public static final int FONT_COLOR = 0x404040;

    public static final Polygon[] ARROW = new Polygon[]{new Polygon(new int[]{43,51,51,69,69,51,51,43}, new int[]{54,46,50,50,69,69,63,55}, 8),
                                                        new Polygon(new int[]{106,124,124,132,132,124,124,106}, new int[]{50,50,46,54,55,63,59,59}, 8)};

    public static final Rectangle[] INVERTED_ARROW = new Rectangle[]{new Rectangle(0, 222, 27, 18), new Rectangle(27,222,27,18)};

    public static final Point PIXEL_GUN_COST_AREA = new Point(26, 95);
    public static final Point PIXEL_PROJECTILE_COST_AREA = new Point(98, 95);

    public static final int COLOR_GREEN = 0x00FF00;
    public static final int COLOR_RED = 0xFF0000;

    public static final String TITLE = "Gun Crafting";

    private final ContainerCraftGuns container;
    private boolean[] mouseOverArrow;

    public GuiCraftGuns(ContainerCraftGuns container) {
        super(container);
        this.container = container;
        this.mouseOverArrow = new boolean[2];
        this.ySize = 221;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        fontRenderer.drawString(TITLE, xSize / 2 - (fontRenderer.getStringWidth(TITLE) / 2), 8, FONT_COLOR);
        ItemGun gun = container.getCurrentGun();

        fontRenderer.drawString("Gun Cost:", ContainerCraftGuns.PIXEL_FIRST_INVENTORY_SLOT.x, ContainerCraftGuns.PIXEL_CURRENT_PROJECTILE_SLOT.y + ContainerCraftGuns.SLOT_SIZE + 2, FONT_COLOR);
        fontRenderer.drawString("Ammo Cost:", xSize / 2, ContainerCraftGuns.PIXEL_CURRENT_PROJECTILE_SLOT.y + ContainerCraftGuns.SLOT_SIZE + 2, FONT_COLOR);
        if(gun != null) {
            CustomEntity[] costables = new CustomEntity[]{gun, gun.getProjectile()};
            fontRenderer.drawString(gun.getName(), xSize / 2 - (fontRenderer.getStringWidth(gun.getName()) / 2), ContainerCraftGuns.PIXEL_CURRENT_GUN_SLOT.y - 8, FONT_COLOR);
            int available = 38;
            int[] xPoints = new int[]{PIXEL_GUN_COST_AREA.x + 1, PIXEL_PROJECTILE_COST_AREA.x + 1};
            int yPoint = ContainerCraftGuns.PIXEL_CURRENT_PROJECTILE_SLOT.y + ContainerCraftGuns.SLOT_SIZE + 12;
            for(int i = 0; i < costables.length; i++) {
                Object[] craftingCost = costables[i].getCraftingRecipe();
                int items = craftingCost.length / 2;
                int rows = (int) Math.ceil(Math.sqrt(items));
                int columns = (items == 2) ? 2 : rows;
                float scale = Math.min(1.0f, available / (16f * rows));
                int index = 0;
                for(int r = 0; r < rows; r++) {
                    for(int c = 0; c < columns; c++, index += 2) {
                        if(index >= craftingCost.length - 1) {
                            break;
                        }
                        Item item = (Item) craftingCost[index];
                        Slot slot = container.getSlot(i);
                        if (slot != null) {
                            ItemStack stack = slot.getStack();
                            if(stack != null) {
                                int amount = (int) (Math.max(1, Math.round(stack.stackSize * ((Number) craftingCost[index + 1]).doubleValue())));
                                renderItemIcon(item, amount, (int) (xPoints[i] + (c * 16 * scale)), (int) (yPoint + (r * 16 * scale)), scale,
                                		(InventoryHelper.getCount(mc.thePlayer.inventory, item.itemID) < amount ? COLOR_RED : COLOR_GREEN));
                            }
                        }
                    }
                }
            }
        }
    }

    private void renderItemIcon(Item item, int amount, int x, int y, float scale, int color) {
        ItemStack stack = new ItemStack(item, amount);
        RenderHelper.renderItemIntoGUI(itemRenderer, fontRenderer, mc.renderEngine, stack, x, y, scale);
        RenderHelper.renderItemOverlayIntoGUI(itemRenderer, fontRenderer, mc.renderEngine, stack, x, y, scale, color); 
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        int texture = mc.renderEngine.getTexture(Resources.CRAFT_GUNS_TEXTURE);
        GL11.glColor4f(1, 1, 1, 1);
        mc.renderEngine.bindTexture(texture);
        int x = getX();
        int y = getY();
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        for(int k = 0; k < ARROW.length; k++) {
            if(mouseOverArrow[k]) {
                drawTexturedModalRect(x + ARROW[k].getBounds().x, y + ARROW[k].getBounds().y, INVERTED_ARROW[k].x, INVERTED_ARROW[k].y, INVERTED_ARROW[k].width, INVERTED_ARROW[k].height);
            }
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int mouseButton) {
        super.mouseClicked(x, y, mouseButton);
        for(int i = 0; i < ARROW.length; i++) {
            if(ARROW[i].contains(x - getX(),y - getY())) {
                mouseOverArrow[i] = false;
                container.onArrowClick(i);
                PacketDispatcher.sendPacketToServer(GunPacketHandler.getArrowClickPacket(i));
            }
        }
    }
    
    @Override
    protected void mouseMovedOrUp(int x, int y, int mouseButton) {
        super.mouseMovedOrUp(x, y, mouseButton);
        for(int i = 0; i < ARROW.length; i++) {
            mouseOverArrow[i] = ARROW[i].contains(x - getX(),y - getY());
        }
    }

    public int getX() {
        return (width - xSize) / 2;
    }

    public int getY() {
        return  (height - ySize) / 2;
    }
}
