package com.heuristix.guns.render;

import com.heuristix.guns.Util;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/5/11
 * Time: 7:42 PM
 */
public class RenderBullet extends Render {

    private static final float SCALE = 0.02f;
    private static final float GRAY_LIGHTNESS = 0.4f;

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {
        Util.renderCuboid(Tessellator.instance, x, y, z, yaw, pitch, SCALE, SCALE, 2 * SCALE, GRAY_LIGHTNESS, GRAY_LIGHTNESS, GRAY_LIGHTNESS, 1);
    }

}
