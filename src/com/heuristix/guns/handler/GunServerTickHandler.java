package com.heuristix.guns.handler;

import java.util.EnumSet;

import cpw.mods.fml.common.TickType;

public class GunServerTickHandler extends GunTickHandler {

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
	}

}
