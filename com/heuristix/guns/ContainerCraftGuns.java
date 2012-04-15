package com.heuristix.guns;

import com.heuristix.ItemCustom;
import com.heuristix.ItemGun;
import com.heuristix.Util;
import com.heuristix.util.Pair;
import net.minecraft.src.Container;
import net.minecraft.src.*;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/16/12
 * Time: 11:40 PM
 */
public class ContainerCraftGuns extends Container {

    public static final Point PIXEL_FIRST_INVENTORY_SLOT = new Point(7, 139);
    public static final Point PIXEL_FIRST_ACTION_SLOT = new Point(7,197);

    public static final Point PIXEL_CURRENT_GUN_SLOT = new Point(79, 28);
    public static final Point PIXEL_CURRENT_PROJECTILE_SLOT = new Point(79, 64);

    public static final List<ItemStack> GUN_ITEM_STACKS = new LinkedList<ItemStack>();

    public static final int COLUMNS = 9;
    public static final int SLOT_SIZE = 18;
    public static final int GUN_SLOTS = 2;

    private final InventoryBasic inventory;

    static {
        for(Item i : Item.itemsList) {
            if(i instanceof ItemGun) {
                GUN_ITEM_STACKS.add(new ItemStack(i));
            }
        }
    }

    private int index;

    public ContainerCraftGuns(EntityPlayer player, boolean creative) {
        this.inventory = new InventoryBasic("guns.crafting", 2);
        addSlot(new Slot(inventory, 0, PIXEL_CURRENT_GUN_SLOT.x, PIXEL_CURRENT_GUN_SLOT.y));
        addSlot(new Slot(inventory, 1, PIXEL_CURRENT_PROJECTILE_SLOT.x, PIXEL_CURRENT_PROJECTILE_SLOT.y));
        for (int i = 0; i < COLUMNS; i++) {
            addSlot(new Slot(player.inventory, i, PIXEL_FIRST_ACTION_SLOT.x + (i * SLOT_SIZE), PIXEL_FIRST_ACTION_SLOT.y));
        }
        //if(!creative) {
            for (int row = 0; row < 3; row++)  {
                for (int column = 0; column < COLUMNS; column++) {
                    addSlot(new Slot(player.inventory, column + (row * COLUMNS) + COLUMNS, PIXEL_FIRST_INVENTORY_SLOT.x + (column * SLOT_SIZE), PIXEL_FIRST_INVENTORY_SLOT.y + (row * SLOT_SIZE)));
                }
            }
        //}
        updateCurrentGun(index);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return !player.isUsingItem();
    }

    public void onArrowClick(int i) {
        index = (int) Util.limit(index + ((i == 0) ? -1 : 1), 0, GUN_ITEM_STACKS.size() - 1);
        updateCurrentGun(index);
    }

    public void updateCurrentGun(int index) {
        if(index < GUN_ITEM_STACKS.size()) {
            ItemStack gun = GUN_ITEM_STACKS.get(index);
            getSlot(0).putStack(gun);
            updateProjectile((ItemGun) gun.getItem());
        }
    }

    public void updateProjectile(ItemGun gun) {
        ItemStack projectile = new ItemStack(gun.getProjectile());
        projectile.stackSize = gun.getClipSize();
        getSlot(1).putStack(projectile);
    }

    public ItemGun getCurrentGun() {
        if(index < GUN_ITEM_STACKS.size()) {
            return (ItemGun) GUN_ITEM_STACKS.get(index).getItem();
        }
        return null;
    }

    public InventoryBasic getInventory() {
        return inventory;
    }

    @Override
    public ItemStack slotClick(int slotNumber, int mouseButton, boolean shiftHeld, EntityPlayer player) {
        if(slotNumber != -999) {
            InventoryPlayer inventoryPlayer = player.inventory;
            Slot slot = getSlot(slotNumber);
            if(slot != null) {
                if(slot.inventory == getInventory()) {
                    ItemStack stack = inventoryPlayer.getItemStack();
                    if(stack == null) {
                        stack = slot.getStack();
                        Object[] transferCost = ((ItemCustom) slot.getStack().getItem()).getCraftingRecipe();
                        List<Pair<Item,Integer>> validItems = new LinkedList<Pair<Item,Integer>>();
                        for(int i = 0; i < transferCost.length; i += 2) {
                            Item item = (Item) transferCost[i];
                            int amount = (int) Math.round(((Number) transferCost[i + 1]).doubleValue() * stack.stackSize);
                            if(Util.getCount(inventoryPlayer, item.shiftedIndex) < amount) {
                                return null;
                            }
                            validItems.add(new Pair<Item,Integer>(item, amount));
                            if(i == transferCost.length - 2) {
                                for(int j = 0; j < validItems.size(); j++) {
                                    if(!Util.remove(inventoryPlayer, validItems.get(j).getFirst().shiftedIndex, validItems.get(j).getSecond())) {
                                        return null;
                                    }
                                }
                            }
                        }
                        inventoryPlayer.setItemStack(ItemStack.copyItemStack(stack));
                    }
                } else {
                    ItemStack stack = getSlot(slot.slotNumber).getStack();
                    Util.getMinecraft((EntityPlayerSP) player).playerController.sendSlotPacket(stack, (slot.slotNumber - inventorySlots.size()) + ContainerCraftGuns.COLUMNS + 36);
                    return super.slotClick(slot.slotNumber, mouseButton, shiftHeld, player);
                }
            } else {
                if (inventoryPlayer.getItemStack() != null) {
                    if (mouseButton == 0) {
                        player.dropPlayerItem(inventoryPlayer.getItemStack());
                        Util.getMinecraft((EntityPlayerSP) player).playerController.func_35639_a(inventoryPlayer.getItemStack());
                        inventoryPlayer.setItemStack(null);
                    }
                    if (mouseButton == 1) {
                        ItemStack tempStack = inventoryPlayer.getItemStack().splitStack(1);
                        player.dropPlayerItem(tempStack);
                        Util.getMinecraft((EntityPlayerSP) player).playerController.func_35639_a(tempStack);
                        if (inventoryPlayer.getItemStack().stackSize == 0) {
                            inventoryPlayer.setItemStack(null);
                        }
                    }
                }
            }
        }
        return null;
    }
}
