package com.heuristix.guns.handler;

import com.heuristix.guns.ContainerCraftGuns;
import com.heuristix.guns.GuiCraftGuns;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GunGuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case ContainerCraftGuns.INVENTORY_TYPE:
				return createContainer(player);
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case ContainerCraftGuns.INVENTORY_TYPE:
				return new GuiCraftGuns(createContainer(player));
			default:
				return null;
		}
	}
	
	public static ContainerCraftGuns createContainer(EntityPlayer player) {
		return new ContainerCraftGuns(player, player.capabilities.isCreativeMode);
	}

}
