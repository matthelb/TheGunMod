package com.heuristix.guns;

import com.heuristix.CustomEntity;
import com.heuristix.ItemCustom;
import com.heuristix.ItemGun;
import com.heuristix.Util;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;

import java.awt.*;

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

    protected void drawGuiContainerForegroundLayer() {
        fontRenderer.drawString(TITLE, xSize / 2 - (fontRenderer.getStringWidth(TITLE) / 2), 8, FONT_COLOR);
        ItemGun gun = container.getCurrentGun();
        fontRenderer.drawString(gun.getName(), xSize / 2 - (fontRenderer.getStringWidth(gun.getName()) / 2), ContainerCraftGuns.PIXEL_CURRENT_GUN_SLOT.y - 8, FONT_COLOR);
        fontRenderer.drawString("Gun Cost:", ContainerCraftGuns.PIXEL_FIRST_INVENTORY_SLOT.x, ContainerCraftGuns.PIXEL_CURRENT_PROJECTILE_SLOT.y + ContainerCraftGuns.SLOT_SIZE + 2, FONT_COLOR);
        fontRenderer.drawString("Ammo Cost:", xSize / 2, ContainerCraftGuns.PIXEL_CURRENT_PROJECTILE_SLOT.y + ContainerCraftGuns.SLOT_SIZE + 2, FONT_COLOR);
        CustomEntity[] costables = new CustomEntity[]{gun, gun.getProjectile()};
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
                    int amount = (int) (container.getSlot(i).getStack().stackSize * ((Number) craftingCost[index + 1]).doubleValue());
                    int color = (Util.getItemSlot(mc.thePlayer.inventory, item.shiftedIndex) == -1 ? COLOR_RED : COLOR_GREEN);
                    renderItemIcon(item, amount, (int) (xPoints[i] + (c * 16 * scale)), (int) (yPoint + (r * 16 * scale)), scale, color);
                }
            }
        }
    }

    private void renderItemIcon(Item item, int amount, int x, int y, float scale, int overlayTextColor) {
        ItemStack stack = new ItemStack(item, amount);
        Util.drawItemIntoGui(Util.getBlockRender(itemRenderer), fontRenderer, mc.renderEngine, stack.itemID, stack.getItemDamage(), stack.getIconIndex(), x, y, itemRenderer.zLevel, true, scale);
        Util.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, stack, 1, x, y, overlayTextColor, scale);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        int texture = mc.renderEngine.getTexture("/heuristix/craftguns.png");
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

    public void handleMouseClick(Slot slot, int slotNumber, int mouseButton, boolean shiftHeld) {
        InventoryPlayer inventoryPlayer = mc.thePlayer.inventory;
        if(slot != null && slotNumber != -999) {
            if(slot.inventory == container.getInventory()) {
                ItemStack stack = inventoryPlayer.getItemStack();
                if(stack == null) {
                    stack = slot.getStack();
                    Object[] transferCost = ((ItemCustom) slot.getStack().getItem()).getCraftingRecipe();
                    for(int i = 0; i < transferCost.length; i += 2) {
                        Item item = (Item) transferCost[i];
                        int amount = (int) Math.round(((Number) transferCost[i + 1]).doubleValue() * stack.stackSize);
                        if(Util.getCount(inventoryPlayer, item.shiftedIndex) < amount) {
                            return;
                        }
                        if(i == transferCost.length - 2) {
                            for(int j = 0; j < transferCost.length; j++) {
                                if(!Util.remove(inventoryPlayer, item.shiftedIndex, amount)) {
                                    return;
                                }
                            }
                        }
                    }
                    inventoryPlayer.setItemStack(ItemStack.copyItemStack(stack));
                }
            } else {
                inventorySlots.slotClick(slot.slotNumber, mouseButton, shiftHeld, mc.thePlayer);
                ItemStack stack = inventorySlots.getSlot(slot.slotNumber).getStack();
                mc.playerController.func_35637_a(stack, (slot.slotNumber - inventorySlots.inventorySlots.size()) + ContainerCraftGuns.COLUMNS + 36);
            }
        } else {
            if (inventoryPlayer.getItemStack() != null) {
                if (mouseButton == 0) {
                    mc.thePlayer.dropPlayerItem(inventoryPlayer.getItemStack());
                    mc.playerController.func_35639_a(inventoryPlayer.getItemStack());
                    inventoryPlayer.setItemStack(null);
                }
                if (mouseButton == 1) {
                    ItemStack tempStack = inventoryPlayer.getItemStack().splitStack(1);
                    mc.thePlayer.dropPlayerItem(tempStack);
                    mc.playerController.func_35639_a(tempStack);
                    if (inventoryPlayer.getItemStack().stackSize == 0) {
                        inventoryPlayer.setItemStack(null);
                    }
                }
            }
        }
    }

    public int getX() {
        return (width - xSize) / 2;
    }

    public int getY() {
        return  (height - ySize) / 2;
    }
}
