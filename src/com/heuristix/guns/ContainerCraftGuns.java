package com.heuristix.guns;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.heuristix.ItemGun;
import com.heuristix.guns.helper.InventoryHelper;
import com.heuristix.guns.helper.MathHelper;
import com.heuristix.guns.util.Pair;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/16/12
 * Time: 11:40 PM
 */
public class ContainerCraftGuns extends Container {

    public static final Point PIXEL_FIRST_INVENTORY_SLOT = new Point(7, 139);
    public static final Point PIXEL_FIRST_ACTION_SLOT = new Point(7, 197);

    public static final Point PIXEL_CURRENT_GUN_SLOT = new Point(79, 28);
    public static final Point PIXEL_CURRENT_PROJECTILE_SLOT = new Point(79, 64);

    public static final List<ItemStack> GUN_ITEM_STACKS = new LinkedList<ItemStack>();

    public static final int COLUMNS = 9;
    public static final int SLOT_SIZE = 18;
    public static final int GUN_SLOTS = 2;

    public static final int INVENTORY_TYPE = 7;

    private final InventoryCraftGuns inventory;

    static {
        for (Item i : Item.itemsList) {
            if (i instanceof ItemGun) {
                GUN_ITEM_STACKS.add(new ItemStack(i));
            }
        }
    }

    private int index;

    public ContainerCraftGuns(EntityPlayer player, boolean creative) {
        this.inventory = new InventoryCraftGuns("guns.crafting", true, 2);
        addSlotToContainer(new Slot(inventory, 0, PIXEL_CURRENT_GUN_SLOT.x, PIXEL_CURRENT_GUN_SLOT.y));
        addSlotToContainer(new Slot(inventory, 1, PIXEL_CURRENT_PROJECTILE_SLOT.x, PIXEL_CURRENT_PROJECTILE_SLOT.y));
        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < COLUMNS; column++) {
                addSlotToContainer(new Slot(player.inventory, column + (row * COLUMNS) + COLUMNS, PIXEL_FIRST_INVENTORY_SLOT.x + (column * SLOT_SIZE), PIXEL_FIRST_INVENTORY_SLOT.y + (row * SLOT_SIZE)));
            }
        }
        for (int i = 0; i < COLUMNS; i++) {
            addSlotToContainer(new Slot(player.inventory, i, PIXEL_FIRST_ACTION_SLOT.x + (i * SLOT_SIZE), PIXEL_FIRST_ACTION_SLOT.y));
        }
        updateCurrentGun(index);
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return !player.isUsingItem();
    }

    public void onArrowClick(int i) {
        index = MathHelper.maxmin(index + ((i == 0) ? -1 : 1), GUN_ITEM_STACKS.size() - 1, 0);
        updateCurrentGun(index);
    }

    public void updateCurrentGun(int index) {
        if (index < GUN_ITEM_STACKS.size()) {
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
        if (index < GUN_ITEM_STACKS.size()) {
            return (ItemGun) GUN_ITEM_STACKS.get(index).getItem();
        }
        return null;
    }

    public InventoryBasic getInventoryBasic() {
        return inventory;
    }

    @Override
    public ItemStack slotClick(int slotNumber, int mouseButton, int shiftHeld, EntityPlayer player) {
        if (slotNumber != -999) {
            InventoryPlayer inventoryPlayer = player.inventory;
            Slot slot = getSlot(slotNumber);
            if (slot != null) {
                if (slot.inventory == getInventoryBasic()) {
                    ItemStack stack = inventoryPlayer.getItemStack();
                    if (stack == null) {
                        stack = slot.getStack();
                        Object[] transferCost = ((ItemCustom) slot.getStack().getItem()).getCraftingRecipe();
                        List<Pair<Item, Integer>> validItems = new LinkedList<Pair<Item, Integer>>();
                        for (int i = 0; i < transferCost.length; i += 2) {
                            Item item = (Item) transferCost[i];
                            int amount = (int) Math.max(1, Math.round(((Number) transferCost[i + 1]).doubleValue() * stack.stackSize));
                            if (InventoryHelper.getCount(inventoryPlayer, item.itemID) < amount) {
                                return null;
                            }
                            validItems.add(new Pair<Item, Integer>(item, amount));
                            if (i == transferCost.length - 2) {
                                for (int j = 0; j < validItems.size(); j++) {
                                    if (!InventoryHelper.remove(inventoryPlayer, validItems.get(j).getFirst().itemID, validItems.get(j).getSecond())) {
                                        return null;
                                    }
                                }
                            }
                        }

                    }
                }
                return super.slotClick(slotNumber, mouseButton, shiftHeld, player);
            }
        }
        return null;
    }

}
