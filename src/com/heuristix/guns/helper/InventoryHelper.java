package com.heuristix.guns.helper;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

public class InventoryHelper {
	
	private InventoryHelper() { }
	
	public static boolean remove(InventoryPlayer inventory, int id, int amount) {
        while (amount > 0) {
            int slot = getItemSlot(inventory, id);
            if (slot == -1)
                return false;
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack != null) {
                int reduce = Math.min(stack.stackSize, amount);
                if (reduce > amount)
                    reduce = amount;
                inventory.decrStackSize(slot, amount);
                if ((amount -= reduce) == 0)
                    break;
            }
        }
        return true;
    }

    public static int getItemSlot(InventoryPlayer inventory, int id) {
        for (int i = 0; i < inventory.mainInventory.length; i++) {
            if (inventory.mainInventory[i] != null && inventory.mainInventory[i].itemID == id) {
                return i;
            }
        }
        return -1;
    }

    public static int getCount(InventoryPlayer inventory, int id) {
        int count = 0;
        ItemStack[][] stacks = new ItemStack[][]{inventory.mainInventory, inventory.armorInventory};
        for (int i = 0; i < stacks.length; i++) {
            for (int j = 0; j < stacks[i].length; j++) {
                ItemStack is = stacks[i][j];
                if (is != null && is.itemID == id) {
                    count += (is.stackSize == 0) ? 1 : is.stackSize;
                }
            }
        }
        return count;
    }

}
