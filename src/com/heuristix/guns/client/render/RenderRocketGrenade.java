package com.heuristix.guns.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;

public class RenderRocketGrenade extends Render {
	
	private final ModelRocket modelRocket;
	
	public RenderRocketGrenade() {
		this.modelRocket = new ModelRocket();
	}
	
	@Override
	public void doRender(Entity entity, double var2, double var4, double var6, float var8, float var9) {
		GL11.glColor4f(0.2f, 0.2f, 0.2f, 1);
		modelRocket.render(entity, 0, 0, 0, 0, 0, 1);
	}
}
