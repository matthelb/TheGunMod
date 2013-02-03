package com.heuristix.guns.handler;

import cpw.mods.fml.common.ITickHandler;

public abstract class GunTickHandler implements ITickHandler {

	@Override
	public String getLabel() {
		return "guns.tick";
	}

}
