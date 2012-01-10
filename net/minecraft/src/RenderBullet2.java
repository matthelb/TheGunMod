// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;

import org.lwjgl.opengl.GL11;

// Referenced classes of package net.minecraft.src:
//            Render, Entity, Tessellator

public class RenderBullet2 extends Render
{

    private float scale;

    public RenderBullet2()
    {
        scale = 1.0F;
    }

    public RenderBullet2(float f)
    {
        scale = f;
    }

    public void renderArrow(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
        loadTexture("/item/bullet.png");
        GL11.glPushMatrix();
        GL11.glTranslatef((float)d, (float)d1, (float)d2);
        GL11.glRotatef((entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * f1) - 90F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.instance;
        int i = 0;
        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = (float)(0 + i * 10) / 32F;
        float f5 = (float)(5 + i * 10) / 32F;
        float f6 = 0.0F;
        float f7 = 0.15625F;
        float f8 = (float)(5 + i * 10) / 32F;
        float f9 = (float)(10 + i * 10) / 32F;
        float f10 = 0.05625F;
        GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glRotatef(45F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(f10 * scale, f10 * scale, f10 * scale);
        GL11.glTranslatef(-4F, 0.0F, 0.0F);
        GL11.glNormal3f(f10, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-2D, -1D, -1D, f6, f8);
        tessellator.addVertexWithUV(-2D, -1D, 1.0D, f7, f8);
        tessellator.addVertexWithUV(-2D, 1.0D, 1.0D, f7, f9);
        tessellator.addVertexWithUV(-2D, 1.0D, -1D, f6, f9);
        tessellator.draw();
        GL11.glNormal3f(-f10, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-0D, 1.0D, -1D, f6, f8);
        tessellator.addVertexWithUV(-0D, 1.0D, 1.0D, f7, f8);
        tessellator.addVertexWithUV(-0D, -1D, 1.0D, f7, f9);
        tessellator.addVertexWithUV(-0D, -1D, -1D, f6, f9);
        tessellator.draw();
        for(int j = 0; j < 4; j++)
        {
            GL11.glRotatef(90F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, -f10);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-0D, -1D, 1.0D, f2, f4);
            tessellator.addVertexWithUV(-0D, 1.0D, 1.0D, f2, f5);
            tessellator.addVertexWithUV(-2D, 1.0D, 1.0D, f3, f5);
            tessellator.addVertexWithUV(-2D, -1D, 1.0D, f3, f4);
            tessellator.draw();
        }

        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        GL11.glPopMatrix();
    }

    public void doRender(Entity entity, double d, double d1, double d2, 
            float f, float f1)
    {
        renderArrow(entity, d, d1, d2, f, f1);
    }
}
