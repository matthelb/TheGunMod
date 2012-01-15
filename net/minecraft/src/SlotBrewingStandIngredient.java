package net.minecraft.src;

class SlotBrewingStandIngredient extends Slot
{
    final ContainerBrewingStand field_40267_a;

    public SlotBrewingStandIngredient(ContainerBrewingStand containerbrewingstand, IInventory iinventory, int i, int j, int k)
    {
        super(iinventory, i, j, k);
        field_40267_a = containerbrewingstand;
    }

    public boolean isItemValid(ItemStack itemstack)
    {
        if (itemstack != null)
        {
            return Item.itemsList[itemstack.itemID].isPotionIngredient();
        }
        else
        {
            return false;
        }
    }

    public int getSlotStackLimit()
    {
        return 64;
    }
}
