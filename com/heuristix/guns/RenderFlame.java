package com.heuristix.guns;

import com.heuristix.ModMP;
import net.minecraft.src.Entity;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.EXTRescaleNormal;
import org.lwjgl.opengl.GL11;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/29/11
 * Time: 11:38 AM
 */
public class RenderFlame extends Render {

    private static final float SCALE = 0.25f;
    private static final int TEXTURE_INDEX = 48;

    public void renderFlame(EntityFlame flame, double x, double y, double z, float yaw, float pitch) {
        loadTexture("/particles.png");
        GL11.glPushMatrix();
        GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
        GL11.glScalef(SCALE, SCALE, SCALE);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glTranslatef((float) x, (float) y, (float) z);
        GL11.glRotatef(180 - renderManager.playerViewY, 0, 1, 0);
        GL11.glRotatef(-renderManager.playerViewX, 1, 0, 0);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.setNormal(0, 1, 0);
        float corner0 = (TEXTURE_INDEX % ModMP.PIXELS_PER_ICON) / ModMP.PIXELS_PER_ICON;
        float corner1 = corner0 + 0.0624375f;
        float corner2 = (TEXTURE_INDEX / ModMP.PIXELS_PER_ICON) / ModMP.PIXELS_PER_ICON;
        float corner3 = corner2 + 0.0624375f;
        t.addVertexWithUV(-SCALE, -SCALE, 0, corner0, corner3);
        t.addVertexWithUV(SCALE, -SCALE, 0, corner1, corner3);
        t.addVertexWithUV(SCALE, SCALE, 0, corner1, corner2);
        t.addVertexWithUV(-SCALE, SCALE, 0, corner0, corner2);
        t.draw();
        GL11.glDisable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float f, float f1) {
        renderFlame((EntityFlame) entity, x, y, z, f, f1);
    }
}
