package com.heuristix.guns.client.render;

import com.heuristix.guns.helper.RenderHelper;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

public class RenderCube extends Render {

	private final float width, height, depth, r, g, b, a;
	
	public RenderCube(float width, float height, float depth, float r, float g, float b, float a) {
		this.width = width;
		this.height = height;
		this.depth = depth;
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {
		RenderHelper.renderCuboid(Tessellator.instance, x, y, z, yaw, pitch, width, height, depth, r, g, b, a);
	}

}
