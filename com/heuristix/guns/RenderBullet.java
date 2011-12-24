package com.heuristix.guns;

import net.minecraft.src.Entity;
import net.minecraft.src.Render;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.GL11;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/5/11
 * Time: 7:42 PM
 */
public class RenderBullet extends Render {

    public void doRenderBullet(EntityBullet bullet, double x, double y, double z, float f, float f1) {
        double scale = 0.025;
        GL11.glPushMatrix();
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(0.4f, 0.4f, 0.4f, 1);
        tessellator.startDrawingQuads();
        tessellator.setTranslationD(x, y, z);
        tessellator.setNormal(0, 0, -1);
        tessellator.addVertexWithUV(x - scale, y + scale, z - scale, 0, 1);
        tessellator.addVertexWithUV(x + scale, y + scale, z - scale, 1, 1);
        tessellator.addVertexWithUV(x + scale, y - scale, z - scale, 1, 0);
        tessellator.addVertexWithUV(x - scale, y - scale, z - scale, 0, 0);
        tessellator.setNormal(0, 0, 1);
        tessellator.addVertexWithUV(x - scale, y - scale, z + scale, 0, 1);
        tessellator.addVertexWithUV(x + scale, y - scale, z + scale, 1, 1);
        tessellator.addVertexWithUV(x + scale, y + scale, z + scale, 1, 0);
        tessellator.addVertexWithUV(x - scale, y + scale, z + scale, 0, 0);
        tessellator.setNormal(0, -1, 0);
        tessellator.addVertexWithUV(x - scale, y - scale, z - scale, 0, 1);
        tessellator.addVertexWithUV(x + scale, y - scale, z - scale, 1, 1);
        tessellator.addVertexWithUV(x + scale, y - scale, z + scale, 1, 0);
        tessellator.addVertexWithUV(x - scale, y - scale, z + scale, 0, 0);
        tessellator.setNormal(0, 1, 0);
        tessellator.addVertexWithUV(x - scale, y + scale, z + scale, 0, 1);
        tessellator.addVertexWithUV(x + scale, y + scale, z + scale, 1, 1);
        tessellator.addVertexWithUV(x + scale, y + scale, z - scale, 1, 0);
        tessellator.addVertexWithUV(x - scale, y + scale, z - scale, 0, 0);
        tessellator.setNormal(-1, 0, 0);
        tessellator.addVertexWithUV(x - scale, y - scale, z + scale, 0, 1);
        tessellator.addVertexWithUV(x - scale, y + scale, z + scale, 1, 1);
        tessellator.addVertexWithUV(x - scale, y + scale, z - scale, 1, 0);
        tessellator.addVertexWithUV(x - scale, y - scale, z - scale, 0, 0);
        tessellator.setNormal(1, 0, 0);
        tessellator.addVertexWithUV(x + scale, y - scale, z - scale, 0, 1);
        tessellator.addVertexWithUV(x + scale, y + scale, z - scale, 1, 1);
        tessellator.addVertexWithUV(x + scale, y + scale, z + scale, 1, 0);
        tessellator.addVertexWithUV(x + scale, y - scale, z + scale, 0, 0);
        tessellator.setTranslationD(0, 0, 0);
        tessellator.draw();
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2, float f, float f1) {
        doRenderBullet((EntityBullet) entity, d, d1, d2, f, f1);
    }
}
