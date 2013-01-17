package com.heuristix.guns;

import net.minecraft.src.InventoryBasic;
import net.minecraft.src.ItemStack;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 8/19/12
 * Time: 8:39 PM
 */
public class InventoryCraftGuns extends InventoryBasic {

    public InventoryCraftGuns(String name, int size) {
        super(name, size);
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {
        return getStackInSlot(index);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        ItemStack stack = super.getStackInSlot(index);
        if (stack != null) {
            return stack.copy();
        }
        return null;
    }
}
