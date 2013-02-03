package com.heuristix.guns;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;

import org.lwjgl.opengl.GL11;

import com.heuristix.ItemGun;
import com.heuristix.TheGunMod;
import com.heuristix.guns.helper.InventoryHelper;

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
                                int color = (InventoryHelper.getCount(mc.thePlayer.inventory, item.itemID) < amount ? COLOR_RED : COLOR_GREEN);
                                renderItemIcon(item, amount, (int) (xPoints[i] + (c * 16 * scale)), (int) (yPoint + (r * 16 * scale)), scale, color);
                            }
                        }
                    }
                }
            }
        }
    }

    private void renderItemIcon(Item item, int amount, int x, int y, float scale, int overlayTextColor) {
        ItemStack stack = new ItemStack(item, amount);
        itemRenderer.renderItemIntoGUI(fontRenderer, mc.renderEngine, stack, x, y);
        itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, stack, x, y);
        /*Util.drawItemIntoGui(Util.getBlockRender(itemRenderer), fontRenderer, mc.renderEngine, stack, x, y, itemRenderer.zLevel, true, scale);
        Util.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, stack, 1, x, y, overlayTextColor, scale);   */
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        int texture = mc.renderEngine.getTexture("/heuristix/guns/craftguns.png");
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
                if(mc.theWorld.isRemote) {
                    ModLoader.clientSendPacket(TheGunMod.getArrowClickPacket(i));
                }
            }
        }
    }

    /*protected void handleMouseClick(Slot slot, int par2, int par3, boolean par4) {
        super.handleMouseClick(slot, par2, par3, par4);
        ItemStack var6;
        InventoryPlayer var9;
        if (slot != null) {
            if (slot.inventory == container.getInventoryBasic()) {
                var9 = this.mc.thePlayer.inventory;
                var6 = var9.getItemStack();
                ItemStack var7 = slot.getStack();

                if (var6 != null && var7 != null && var6.isItemEqual(var7)) {
                    if (par3 == 0) {
                        if (par4) {
                            var6.stackSize = var6.getMaxStackSize();
                        } else if (var6.stackSize < var6.getMaxStackSize()) {
                            ++var6.stackSize;
                        }
                    } else if (var6.stackSize <= 1) {
                        var9.setItemStack((ItemStack) null);
                    } else {
                        --var6.stackSize;
                    }
                } else if (var7 != null && var6 == null) {
                    boolean var8 = false;

                    if (!var8) {
                        var9.setItemStack(ItemStack.copyItemStack(var7));
                        var6 = var9.getItemStack();

                        if (par4) {
                            var6.stackSize = var6.getMaxStackSize();
                        }
                    }
                } else {
                    var9.setItemStack((ItemStack) null);
                }
            } else {
                this.inventorySlots.slotClick(slot.slotNumber, par3, par4, this.mc.thePlayer);
                ItemStack var10 = this.inventorySlots.getSlot(slot.slotNumber).getStack();
                this.mc.playerController.sendSlotPacket(var10, slot.slotNumber - this.inventorySlots.inventorySlots.size() + 9 + 36);
            }
        } else {
            var9 = this.mc.thePlayer.inventory;

            if (var9.getItemStack() != null) {
                if (par3 == 0) {
                    this.mc.thePlayer.dropPlayerItem(var9.getItemStack());
                    this.mc.playerController.func_78752_a(var9.getItemStack());
                    var9.setItemStack((ItemStack) null);
                }

                if (par3 == 1) {
                    var6 = var9.getItemStack().splitStack(1);
                    this.mc.thePlayer.dropPlayerItem(var6);
                    this.mc.playerController.func_78752_a(var6);

                    if (var9.getItemStack().stackSize == 0) {
                        var9.setItemStack((ItemStack) null);
                    }
                }
            }
        }
    } */

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
