package com.heuristix.guns;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockCustom extends Block implements CustomEntity {

	public BlockCustom(int id, Material material) {
		super(id, material);
	}

}
