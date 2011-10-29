package com.heuristix;

import net.minecraft.src.Item;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/1/11
 * Time: 10:08 AM
 */
public interface CustomItem {

    public String getName();

    public String getIconPath();

    public Object[] getCraftingRecipe();

    public int getCraftingAmount();
}
