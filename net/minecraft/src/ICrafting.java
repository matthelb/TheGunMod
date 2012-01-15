package net.minecraft.src;

import java.util.List;

public interface ICrafting
{
    public abstract void updateCraftingInventory(Container container, List list);

    public abstract void updateCraftingInventorySlot(Container container, int i, ItemStack itemstack);

    public abstract void updateCraftingInventoryInfo(Container container, int i, int j);
}
