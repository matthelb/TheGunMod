package net.minecraft.src;

public interface IRecipe
{
    public abstract boolean matches(InventoryCrafting inventorycrafting);

    public abstract ItemStack getCraftingResult(InventoryCrafting inventorycrafting);

    /**
     * Returns the size of the recipe area
     */
    public abstract int getRecipeSize();

    public abstract ItemStack getRecipeOutput();
}
