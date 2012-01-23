package com.heuristix;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/1/11
 * Time: 10:08 AM
 */
public interface CustomEntity {

    String getName();

    String getIconPath();

    boolean hasWorkbenchRecipe();

    Object[] getCraftingRecipe();

    int getCraftingAmount();
}
