package com.heuristix;

import net.minecraft.src.*;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/1/11
 * Time: 10:12 AM
 */
public abstract class ModMP extends BaseModMp {

    public static final String CURRENT_VERSION = "1.1.0";


    public ModMP() {
    }

    protected <B extends Block & CustomEntity> void registerBlock(B block) {
        if(block.getBlockName() == null) {
            block.setBlockName(block.getName());
        }
        if(block.getCraftingRecipe() != null && block.getCraftingRecipe().length > 0) {
            ModLoader.AddRecipe(new ItemStack(block, block.getCraftingAmount()), block.getCraftingRecipe());
        }
        Item.itemsList[block.blockID] = new ItemBlock(block.blockID - 256);
    }

    public abstract String getVersion();


}
