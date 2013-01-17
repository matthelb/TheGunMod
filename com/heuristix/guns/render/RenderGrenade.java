package com.heuristix.guns.render;

import com.heuristix.guns.Util;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/26/11
 * Time: 3:43 PM
 */
public class RenderGrenade extends Render {

    public static final float SCALE = 0.025f;

    public static final float R = 0.2f;
    public static final float G = 0.5f;
    public static final float B = 0.2f;

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {
        Util.renderCuboid(Tessellator.instance, x, y, z, yaw, pitch, SCALE, SCALE, SCALE, R, G, B, 1);
    }
}
