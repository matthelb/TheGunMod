package com.heuristix.guns;

import com.heuristix.CustomEntity;
import com.heuristix.Util;
import net.minecraft.src.*;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/18/12
 * Time: 6:04 PM
 */
public class BlockCraftGuns extends Block implements CustomEntity {

    public BlockCraftGuns(int id) {
        super(id, Material.iron);
    }

    @Override
    public boolean blockActivated(World world, int x, int y, int z, EntityPlayer player) {
        if(player instanceof EntityPlayerMP) {
            if(!world.singleplayerWorld) {
                ContainerCraftGuns container = new ContainerCraftGuns(player, ((EntityPlayerMP) player).itemInWorldManager.getGameType() == 1);
                ModLoader.OpenGUI(player, container.windowId, container.getInventory(), container);
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return "Armory";
    }

    public String getIconPath() {
        return null;
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
}
