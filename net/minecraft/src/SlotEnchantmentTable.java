package net.minecraft.src;

class SlotEnchantmentTable extends InventoryBasic
{
    final ContainerEnchantment field_40088_a;

    SlotEnchantmentTable(ContainerEnchantment containerenchantment, String s, int i)
    {
        super(s, i);
        field_40088_a = containerenchantment;
    }

    public int getInventoryStackLimit()
    {
        return 1;
    }

    public void onInventoryChanged()
    {
        super.onInventoryChanged();
        field_40088_a.onCraftMatrixChanged(this);
    }
}
