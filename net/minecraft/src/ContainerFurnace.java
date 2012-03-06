package net.minecraft.src;

import java.util.List;

public class ContainerFurnace extends Container
{
    private TileEntityFurnace furnace;
    private int lastCookTime;
    private int lastBurnTime;
    private int lastItemBurnTime;

    public ContainerFurnace(InventoryPlayer par1InventoryPlayer, TileEntityFurnace par2TileEntityFurnace)
    {
        lastCookTime = 0;
        lastBurnTime = 0;
        lastItemBurnTime = 0;
        furnace = par2TileEntityFurnace;
        addSlot(new Slot(par2TileEntityFurnace, 0, 56, 17));
        addSlot(new Slot(par2TileEntityFurnace, 1, 56, 53));
        addSlot(new SlotFurnace(par1InventoryPlayer.player, par2TileEntityFurnace, 2, 116, 35));

        for (int i = 0; i < 3; i++)
        {
            for (int k = 0; k < 9; k++)
            {
                addSlot(new Slot(par1InventoryPlayer, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
            }
        }

        for (int j = 0; j < 9; j++)
        {
            addSlot(new Slot(par1InventoryPlayer, j, 8 + j * 18, 142));
        }
    }

    public void onCraftGuiOpened(ICrafting par1ICrafting)
    {
        super.onCraftGuiOpened(par1ICrafting);
        par1ICrafting.updateCraftingInventoryInfo(this, 0, furnace.furnaceCookTime);
        par1ICrafting.updateCraftingInventoryInfo(this, 1, furnace.furnaceBurnTime);
        par1ICrafting.updateCraftingInventoryInfo(this, 2, furnace.currentItemBurnTime);
    }

    /**
     * update the crafting matrix
     */
    public void updateCraftingResults()
    {
        super.updateCraftingResults();

        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting)crafters.get(i);

            if (lastCookTime != furnace.furnaceCookTime)
            {
                icrafting.updateCraftingInventoryInfo(this, 0, furnace.furnaceCookTime);
            }

            if (lastBurnTime != furnace.furnaceBurnTime)
            {
                icrafting.updateCraftingInventoryInfo(this, 1, furnace.furnaceBurnTime);
            }

            if (lastItemBurnTime != furnace.currentItemBurnTime)
            {
                icrafting.updateCraftingInventoryInfo(this, 2, furnace.currentItemBurnTime);
            }
        }

        lastCookTime = furnace.furnaceCookTime;
        lastBurnTime = furnace.furnaceBurnTime;
        lastItemBurnTime = furnace.currentItemBurnTime;
    }

    public boolean canInteractWith(EntityPlayer par1EntityPlayer)
    {
        return furnace.isUseableByPlayer(par1EntityPlayer);
    }

    /**
     * Called to transfer a stack from one inventory to the other eg. when shift clicking.
     */
    public ItemStack transferStackInSlot(int par1)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)inventorySlots.get(par1);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par1 == 2)
            {
                if (!mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }

                slot.func_48417_a(itemstack1, itemstack);
            }
            else if (par1 >= 3 && par1 < 30)
            {
                if (!mergeItemStack(itemstack1, 30, 39, false))
                {
                    return null;
                }
            }
            else if (par1 >= 30 && par1 < 39)
            {
                if (!mergeItemStack(itemstack1, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!mergeItemStack(itemstack1, 3, 39, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack(null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize != itemstack.stackSize)
            {
                slot.onPickupFromSlot(itemstack1);
            }
            else
            {
                return null;
            }
        }

        return itemstack;
    }
}
