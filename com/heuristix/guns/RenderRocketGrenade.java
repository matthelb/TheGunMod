package com.heuristix.guns;

import net.minecraft.src.Entity;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 1/28/12
 * Time: 3:31 PM
 */
public class RenderRocketGrenade extends Render {

    public static final Vector3f BODY_SCALE = new Vector3f(0.03f, 0.03f, 0.06f);
    public static final Vector3f COLOR = new Vector3f(0.2f, 0.2f, 0.2f);
    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float pitch) {
        Util.renderCuboid(Tessellator.instance, x, y, z, yaw, pitch, BODY_SCALE.getX(), BODY_SCALE.getY(), BODY_SCALE.getZ(), COLOR.getX(), COLOR.getY(), COLOR.getZ(), 1);
    }
}
