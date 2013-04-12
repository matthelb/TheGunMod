package com.heuristix.guns;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

import com.heuristix.TheGunMod;
import com.heuristix.guns.client.Resources;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/18/12
 * Time: 6:04 PM
 */
public class BlockCraftGuns extends BlockCustom {

	private Icon[] icons;
	
    public BlockCraftGuns(int id) {
        super(id, Material.iron);
        this.setCreativeTab(CreativeTabs.tabDecorations);
        this.icons = new Icon[2];
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        player.openGui(TheGunMod.instance, ContainerCraftGuns.INVENTORY_TYPE, world, x, y, z);
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Icon getBlockTextureFromSideAndMetadata(int side, int damage) {
        if(side == 0) {
            return icons[1];
        } else if(side == 1) {
            return blockIcon;
        } else {
            return icons[0];
        }
    }
    
    
    @Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		blockIcon = iconRegister.registerIcon("guns:craft_guns_top");
		icons[0] = iconRegister.registerIcon("guns:craft_guns_bottom");
		icons[1] = iconRegister.registerIcon("guns:craft_guns_side");
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

}
