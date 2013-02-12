package com.heuristix.guns;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;

import com.heuristix.TheGunMod;
import com.heuristix.guns.client.Resources;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/18/12
 * Time: 6:04 PM
 */
public class BlockCraftGuns extends BlockCustom {

    public BlockCraftGuns(int id) {
        super(id, Material.iron);
        this.setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        player.openGui(TheGunMod._instance, ContainerCraftGuns.INVENTORY_TYPE, world, x, y, z);
        return true;
    }

    @Override
    public int getBlockTextureFromSide(int side) {
        if(side == 0) {
            return 2;
        } else if(side == 1) {
            return 0;
        } else {
            return 1;
        }
    }

    public String getName() {
        return "Armory";
    }

    public boolean hasWorkbenchRecipe() {
        return true;
    }

    public Object[] getCraftingRecipe() {
        return new Object[]
        {"DID",
         "IGI",
         "RIL",
         Character.valueOf('D'), Item.diamond,
         Character.valueOf('I'), Item.ingotIron,
         Character.valueOf('G'), Item.gunpowder,
         Character.valueOf('R'), Item.redstone,
         Character.valueOf('L'), Item.lightStoneDust};
    }

    public int getCraftingAmount() {
        return 1;
    }

    public boolean isShapelessRecipe() {
        return false;
    }

    @Override
    public String getTextureFile() {
    	return Resources.BLOCK_TEXTURES;
    }
}
