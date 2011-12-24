package com.heuristix.guns;

import net.minecraft.src.Entity;
import net.minecraft.src.Render;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/26/11
 * Time: 3:43 PM
 */
public class RenderRocketGrenade extends Render {

    public void doRenderRocketGrenade(EntityRocketGrenade entity, double x, double y, double z, float f, float f1) {

    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
        doRenderRocketGrenade((EntityRocketGrenade) entity, d, d1, d2, f, f1);
    }
}
