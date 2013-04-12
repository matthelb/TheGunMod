package com.heuristix.guns.client.render;

import com.heuristix.EntityProjectile;
import com.heuristix.guns.helper.RenderHelper;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

public class RenderProjectile extends RenderCube {

	public RenderProjectile(float width, float height, float depth, float r, float g, float b, float a) {
		super(width, height, depth, r, g, b, a);
	}

	@Override
	public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {
		renderEntityProjectile((EntityProjectile) entity, x, y, z, yaw, pitch);
	}

	public void renderEntityProjectile(EntityProjectile entity, double x, double y, double z, float yaw, float pitch) {
		if (!entity.isWithinDistanceOfOwner(2)) {
			super.doRender(entity, x, y, z, yaw, pitch);
		}
	}

}
