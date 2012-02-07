package com.heuristix;

import com.heuristix.util.ReflectionFacade;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.ARBVertexBlend;
import org.lwjgl.opengl.EXTRescaleNormal;
import org.lwjgl.opengl.GL11;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/2/11
 * Time: 10:16 PM
 */
public class GunItemRenderer extends ItemRenderer {

    private ItemGun prevGun;

    private float reloadProgress, prevReloadProgress;

    private float getPrevEquippedProgress() {
        return (Float) ReflectionFacade.getInstance().getFieldValue(ItemRenderer.class, this, "prevEquippedProgress");
    }

    private float getEquippedProgress() {
        return (Float) ReflectionFacade.getInstance().getFieldValue(ItemRenderer.class, this, "equippedProgress");
    }

    private RenderBlocks getRenderBlocksInstance() {
        return (RenderBlocks) ReflectionFacade.getInstance().getFieldValue(ItemRenderer.class, this, "renderBlocksInstance");
    }

    private Minecraft getMC() {
        return (Minecraft) ReflectionFacade.getInstance().getFieldValue(ItemRenderer.class, this, "mc");
    }

    private ItemStack getItemToRender() {
        return (ItemStack) ReflectionFacade.getInstance().getFieldValue(ItemRenderer.class, this, "itemToRender");
    }

    private MapItemRenderer getMapItemRenderer() {
        return (MapItemRenderer) ReflectionFacade.getInstance().getFieldValue(ItemRenderer.class, this, "mapItemRenderer");
    }

    public GunItemRenderer(Minecraft minecraft) {
        super(minecraft);
    }

    public void renderItem(EntityLiving entity, ItemStack stack, int i) {
        ItemGun gun = getGun();
        if(gun == null) {
            super.renderItem(entity, stack, i);
            return;
        }
        Minecraft mc = getMC();
        GL11.glPushMatrix();
        if (stack.itemID < 256 && RenderBlocks.renderItemIn3d(Block.blocksList[stack.itemID].getRenderType())) {
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain.png"));
            getRenderBlocksInstance().renderBlockAsItem(Block.blocksList[stack.itemID], stack.getItemDamage(), 1);
        } else {
            if (stack.itemID < 256) {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain.png"));
            }
            else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/items.png"));
            }
            Tessellator t = Tessellator.instance;
            int iconIndex = entity.getItemIcon(stack, i);
            float x1 = (((iconIndex % 16) * 16) + 0) / 256f;
            float x2 = (((iconIndex % 16) * 16) + 15.99f) / 256;
            float y1 = (((iconIndex / 16) * 16) + 0) / 256f;
            float y2 = (((iconIndex / 16) * 16) + 15.99f) / 256;
            GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
            GL11.glRotatef(0, 1, 0, 0);
            GL11.glRotatef(45, 0, 1, 0);
            GL11.glRotatef(-25, 0, 0, 1);
            GL11.glTranslatef(-1.3f, -0.35f, 0);

            float scale = 1.5f;
            GL11.glScalef(scale, scale, scale);
            addItemTextureVertices(t, x2, y1, x1, y2);
            if (stack != null && stack.func_40713_r() && i == 0) {
                GL11.glDepthFunc(GL11.GL_EQUAL);
                GL11.glDisable(GL11.GL_LIGHTING);
                mc.renderEngine.bindTexture(mc.renderEngine.getTexture("%blur%/misc/glint.png"));
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(768, 1);
                float f7 = 0.76F;
                GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
                GL11.glMatrixMode(GL11.GL_TEXTURE);
                GL11.glPushMatrix();
                float glintScale = 0.125F;
                GL11.glScalef(glintScale, glintScale, glintScale);
                float f9 = ((float)(System.currentTimeMillis() % 3000L) / 3000F) * 8F;
                GL11.glTranslatef(f9, 0.0F, 0.0F);
                GL11.glRotatef(-50F, 0.0F, 0.0F, 1.0F);
                addItemTextureVertices(t, 0.0F, 0.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
                GL11.glPushMatrix();
                GL11.glScalef(glintScale, glintScale, glintScale);
                f9 = ((float)(System.currentTimeMillis() % 4873L) / 4873F) * 8F;
                GL11.glTranslatef(-f9, 0.0F, 0.0F);
                GL11.glRotatef(10F, 0.0F, 0.0F, 1.0F);
                addItemTextureVertices(t, 0.0F, 0.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
                GL11.glMatrixMode(ARBVertexBlend.GL_MODELVIEW10_ARB);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
            }
            GL11.glDisable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
        }
        GL11.glPopMatrix();
    }

    private void addItemTextureVertices(Tessellator tessellator, float f, float f1, float f2, float f3) {
        float f4 = 1.0F;
        float f5 = 0.0625F;
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, f, f3);
        tessellator.addVertexWithUV(f4, 0.0D, 0.0D, f2, f3);
        tessellator.addVertexWithUV(f4, 1.0D, 0.0D, f2, f1);
        tessellator.addVertexWithUV(0.0D, 1.0D, 0.0D, f, f1);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, -1F);
        tessellator.addVertexWithUV(0.0D, 1.0D, 0.0F - f5, f, f1);
        tessellator.addVertexWithUV(f4, 1.0D, 0.0F - f5, f2, f1);
        tessellator.addVertexWithUV(f4, 0.0D, 0.0F - f5, f2, f3);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0F - f5, f, f3);
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(-1F, 0.0F, 0.0F);
        for (int i = 0; i < 16; i++) {
            float f6 = (float)i / 16F;
            float f10 = (f + (f2 - f) * f6) - 0.001953125F;
            float f14 = f4 * f6;
            tessellator.addVertexWithUV(f14, 0.0D, 0.0F - f5, f10, f3);
            tessellator.addVertexWithUV(f14, 0.0D, 0.0D, f10, f3);
            tessellator.addVertexWithUV(f14, 1.0D, 0.0D, f10, f1);
            tessellator.addVertexWithUV(f14, 1.0D, 0.0F - f5, f10, f1);
        }
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        for (int j = 0; j < 16; j++) {
            float f7 = (float)j / 16F;
            float f11 = (f + (f2 - f) * f7) - 0.001953125F;
            float f15 = f4 * f7 + 0.0625F;
            tessellator.addVertexWithUV(f15, 1.0D, 0.0F - f5, f11, f1);
            tessellator.addVertexWithUV(f15, 1.0D, 0.0D, f11, f1);
            tessellator.addVertexWithUV(f15, 0.0D, 0.0D, f11, f3);
            tessellator.addVertexWithUV(f15, 0.0D, 0.0F - f5, f11, f3);
        }
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        for (int k = 0; k < 16; k++) {
            float f8 = (float)k / 16F;
            float f12 = (f3 + (f1 - f3) * f8) - 0.001953125F;
            float f16 = f4 * f8 + 0.0625F;
            tessellator.addVertexWithUV(0.0D, f16, 0.0D, f, f12);
            tessellator.addVertexWithUV(f4, f16, 0.0D, f2, f12);
            tessellator.addVertexWithUV(f4, f16, 0.0F - f5, f2, f12);
            tessellator.addVertexWithUV(0.0D, f16, 0.0F - f5, f, f12);
        }
        tessellator.draw();
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1F, 0.0F);
        for (int l = 0; l < 16; l++) {
            float f9 = (float)l / 16F;
            float f13 = (f3 + (f1 - f3) * f9) - 0.001953125F;
            float f17 = f4 * f9;
            tessellator.addVertexWithUV(f4, f17, 0.0D, f2, f13);
            tessellator.addVertexWithUV(0.0D, f17, 0.0D, f, f13);
            tessellator.addVertexWithUV(0.0D, f17, 0.0F - f5, f, f13);
            tessellator.addVertexWithUV(f4, f17, 0.0F - f5, f2, f13);
        }
        tessellator.draw();
    }

    @Override
    public void renderItemInFirstPerson(float f) {
        float prevEquippedProgress = getPrevEquippedProgress();
        float equippedProgress = getEquippedProgress();
        Minecraft mc = getMC();

        float f1 = prevEquippedProgress + (equippedProgress - prevEquippedProgress) * f;
        if (equippedProgress == 1 && getGun() != null) {
            f1 = prevReloadProgress + (reloadProgress - prevReloadProgress) * f;
        }
        EntityPlayerSP entityplayersp = mc.thePlayer;
        float f2 = entityplayersp.prevRotationPitch + (entityplayersp.rotationPitch - entityplayersp.prevRotationPitch) * f;
        GL11.glPushMatrix();
        GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(entityplayersp.prevRotationYaw + (entityplayersp.rotationYaw - entityplayersp.prevRotationYaw) * f, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        if (entityplayersp instanceof EntityPlayerSP) {
            float f3 = entityplayersp.prevRenderArmPitch + (entityplayersp.renderArmPitch - entityplayersp.prevRenderArmPitch) * f;
            float f5 = entityplayersp.prevRenderArmYaw + (entityplayersp.renderArmYaw - entityplayersp.prevRenderArmYaw) * f;
            GL11.glRotatef((entityplayersp.rotationPitch - f3) * 0.1F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef((entityplayersp.rotationYaw - f5) * 0.1F, 0.0F, 1.0F, 0.0F);
        }
        ItemStack itemstack = getItemToRender();
        float f4 = mc.theWorld.getLightBrightness(MathHelper.floor_double(((EntityPlayer) (entityplayersp)).posX), MathHelper.floor_double(((EntityPlayer) (entityplayersp)).posY), MathHelper.floor_double(((EntityPlayer) (entityplayersp)).posZ));
        f4 = 1.0F;
        int i = mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(((EntityPlayer) (entityplayersp)).posX), MathHelper.floor_double(((EntityPlayer) (entityplayersp)).posY), MathHelper.floor_double(((EntityPlayer) (entityplayersp)).posZ), 0);
        int k = i % 0x10000;
        int l = i / 0x10000;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapEnabled, (float) k / 1.0F, (float) l / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        //Set color
        if (itemstack != null) {
            int j = Item.itemsList[itemstack.itemID].getColorFromDamage(itemstack.getItemDamage(), 0);
            float f9 = (float)(j >> 16 & 0xff) / 255F;
            float f14 = (float)(j >> 8 & 0xff) / 255F;
            float f20 = (float)(j & 0xff) / 255F;
            GL11.glColor4f(f4 * f9, f4 * f14, f4 * f20, 1.0F);
        } else {
            GL11.glColor4f(f4, f4, f4, 1.0F);
        }
        //Map item rendering
        if (itemstack != null && itemstack.itemID == Item.map.shiftedIndex) {
            GL11.glPushMatrix();
            float f6 = 0.8F;
            float f10 = entityplayersp.getSwingProgress(f);
            float f15 = MathHelper.sin(f10 * com.heuristix.Util.PI);
            float f21 = MathHelper.sin(MathHelper.sqrt_float(f10) * com.heuristix.Util.PI);
            GL11.glTranslatef(-f21 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f10) * com.heuristix.Util.PI * 2.0F) * 0.2F, -f15 * 0.2F);
            f10 = (1.0F - f2 / 45F) + 0.1F;
            if (f10 < 0.0F)
            {
                f10 = 0.0F;
            }
            if (f10 > 1.0F)
            {
                f10 = 1.0F;
            }
            f10 = -MathHelper.cos(f10 * com.heuristix.Util.PI) * 0.5F + 0.5F;
            GL11.glTranslatef(0.0F, (0.0F * f6 - (1.0F - f1) * 1.2F - f10 * 0.5F) + 0.04F, -0.9F * f6);
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(f10 * -85F, 0.0F, 0.0F, 1.0F);
            GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTextureForDownloadableImage(mc.thePlayer.skinUrl, mc.thePlayer.getEntityTexture()));
            for (f15 = 0; f15 < 2; f15++)
            {
                f21 = f15 * 2 - 1;
                GL11.glPushMatrix();
                GL11.glTranslatef(-0F, -0.6F, 1.1F * (float)f21);
                GL11.glRotatef(-45 * f21, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-90F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(59F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-65 * f21, 0.0F, 1.0F, 0.0F);
                Render render1 = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
                RenderPlayer renderplayer1 = (RenderPlayer)render1;
                float f35 = 1.0F;
                GL11.glScalef(f35, f35, f35);
                renderplayer1.drawFirstPersonHand();
                GL11.glPopMatrix();
            }

            f15 = entityplayersp.getSwingProgress(f);
            f21 = MathHelper.sin(f15 * f15 * com.heuristix.Util.PI);
            float f28 = MathHelper.sin(MathHelper.sqrt_float(f15) * com.heuristix.Util.PI);
            GL11.glRotatef(-f21 * 20F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f28 * 20F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f28 * 80F, 1.0F, 0.0F, 0.0F);
            f15 = 0.38F;
            GL11.glScalef(f15, f15, f15);
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(-1F, -1F, 0.0F);
            f21 = 0.015625F;
            GL11.glScalef(f21, f21, f21);
            mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/misc/mapbg.png"));
            Tessellator tessellator = Tessellator.instance;
            GL11.glNormal3f(0.0F, 0.0F, -1F);
            tessellator.startDrawingQuads();
            byte byte0 = 7;
            tessellator.addVertexWithUV(0 - byte0, 128 + byte0, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(128 + byte0, 128 + byte0, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(128 + byte0, 0 - byte0, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(0 - byte0, 0 - byte0, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            MapData mapdata = Item.map.getMapData(itemstack, mc.theWorld);
            getMapItemRenderer().renderMap(getMC().thePlayer, mc.renderEngine, mapdata);
            GL11.glPopMatrix();
        }
        else if (itemstack != null) {
            GL11.glPushMatrix();
            float f7 = 0.8F;
            if (entityplayersp.func_35205_Y() > 0) {
                EnumAction enumaction = itemstack.getItemUseAction();
                if (enumaction == EnumAction.eat || enumaction == EnumAction.drink) {
                    float f16 = ((float)entityplayersp.func_35205_Y() - f) + 1.0F;
                    float f22 = 1.0F - f16 / (float)itemstack.getMaxItemUseDuration();
                    float f29 = f22;
                    float f32 = 1.0F - f29;
                    f32 = f32 * f32 * f32;
                    f32 = f32 * f32 * f32;
                    f32 = f32 * f32 * f32;
                    float f36 = 1.0F - f32;
                    GL11.glTranslatef(0.0F, MathHelper.abs(MathHelper.cos((f16 / 4F) * com.heuristix.Util.PI) * 0.1F) * (float)((double)f29 <= 0.20000000000000001D ? 0 : 1), 0.0F);
                    GL11.glTranslatef(f36 * 0.6F, -f36 * 0.5F, 0.0F);
                    GL11.glRotatef(f36 * 90F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(f36 * 10F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(f36 * 30F, 0.0F, 0.0F, 1.0F);
                }
            }
            else {
                float f11 = entityplayersp.getSwingProgress(f);
                float f17 = MathHelper.sin(f11 * com.heuristix.Util.PI);
                float f23 = MathHelper.sin(MathHelper.sqrt_float(f11) * com.heuristix.Util.PI);
                GL11.glTranslatef(-f23 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f11) * com.heuristix.Util.PI * 2.0F) * 0.2F, -f17 * 0.2F);
            }
            GL11.glTranslatef(0.7F * f7, -0.65F * f7 - (1.0F - f1) * 0.6F, -0.9F * f7);
            GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
            float swingProgress = entityplayersp.getSwingProgress(f);
            float f18 = MathHelper.sin(swingProgress * swingProgress * com.heuristix.Util.PI);
            float f24 = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * com.heuristix.Util.PI);
            GL11.glRotatef(-f18 * 20F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f24 * 20F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f24 * 80F, 1.0F, 0.0F, 0.0F);
            swingProgress = 0.4F;
            GL11.glScalef(swingProgress, swingProgress, swingProgress);
            if (entityplayersp.func_35205_Y() > 0) {
                EnumAction enumaction1 = itemstack.getItemUseAction();
                if (enumaction1 == EnumAction.block) {
                    GL11.glTranslatef(-0.5F, 0.2F, 0.0F);
                    GL11.glRotatef(30F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-80F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(60F, 0.0F, 1.0F, 0.0F);
                }
                else if (enumaction1 == EnumAction.bow) {
                    GL11.glRotatef(-18F, 0.0F, 0.0F, 1.0F);
                    GL11.glRotatef(-12F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-8F, 1.0F, 0.0F, 0.0F);
                    GL11.glTranslatef(-0.9F, 0.2F, 0.0F);
                    float f25 = (float)itemstack.getMaxItemUseDuration() - (((float)entityplayersp.func_35205_Y() - f) + 1.0F);
                    float f30 = f25 / 20F;
                    f30 = (f30 * f30 + f30 * 2.0F) / 3F;
                    if (f30 > 1.0F) {
                        f30 = 1.0F;
                    }
                    if (f30 > 0.1F) {
                        GL11.glTranslatef(0.0F, MathHelper.sin((f25 - 0.1F) * 1.3F) * 0.01F * (f30 - 0.1F), 0.0F);
                    }
                    GL11.glTranslatef(0.0F, 0.0F, f30 * 0.1F);
                    GL11.glRotatef(-335F, 0.0F, 0.0F, 1.0F);
                    GL11.glRotatef(-50F, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslatef(0.0F, 0.5F, 0.0F);
                    float f33 = 1.0F + f30 * 0.2F;
                    GL11.glScalef(1.0F, 1.0F, f33);
                    GL11.glTranslatef(0.0F, -0.5F, 0.0F);
                    GL11.glRotatef(50F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(335F, 0.0F, 0.0F, 1.0F);
                }
            }
            if (itemstack.getItem().shouldRotateAroundWhenRendering()) {
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
            }
            if (itemstack.getItem().func_46058_c()) {
                super.renderItem(entityplayersp, itemstack, 0);
                int i1 = Item.itemsList[itemstack.itemID].getColorFromDamage(itemstack.getItemDamage(), 1);
                float f26 = (float)(i1 >> 16 & 0xff) / 255F;
                float f31 = (float)(i1 >> 8 & 0xff) / 255F;
                float f34 = (float)(i1 & 0xff) / 255F;
                GL11.glColor4f(f4 * f26, f4 * f31, f4 * f34, 1.0F);
                super.renderItem(entityplayersp, itemstack, 1);
            }
            else {
                super.renderItem(entityplayersp, itemstack, 0);
            }
            GL11.glPopMatrix();
        }
        else {
            GL11.glPushMatrix();
            float f8 = 0.8F;
            float f13 = entityplayersp.getSwingProgress(f);
            float f19 = MathHelper.sin(f13 * com.heuristix.Util.PI);
            float scale = MathHelper.sin(MathHelper.sqrt_float(f13) * com.heuristix.Util.PI);
            GL11.glTranslatef(-scale * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f13) * com.heuristix.Util.PI * 2.0F) * 0.4F, -f19 * 0.4F);
            GL11.glTranslatef(0.8F * f8, -0.75F * f8 - (1.0F - f1) * 0.6F, -0.9F * f8);
            GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
            f13 = entityplayersp.getSwingProgress(f);
            f19 = MathHelper.sin(f13 * f13 * com.heuristix.Util.PI);
            scale = MathHelper.sin(MathHelper.sqrt_float(f13) * com.heuristix.Util.PI);
            GL11.glRotatef(scale * 70F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f19 * 20F, 0.0F, 0.0F, 1.0F);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTextureForDownloadableImage(mc.thePlayer.skinUrl, mc.thePlayer.getEntityTexture()));
            GL11.glTranslatef(-1F, 3.6F, 3.5F);
            GL11.glRotatef(120F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(200F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glTranslatef(5.6F, 0.0F, 0.0F);
            Render render = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
            RenderPlayer renderplayer = (RenderPlayer)render;
            scale = 1.0F;
            GL11.glScalef(scale, scale, scale);
            renderplayer.drawFirstPersonHand();
            GL11.glPopMatrix();
        }
        GL11.glDisable(EXTRescaleNormal.GL_RESCALE_NORMAL_EXT);
        RenderHelper.disableStandardItemLighting();
    }


    @Override
    public void updateEquippedItem(){
        ItemGun gun = getGun();
        Minecraft mc = getMC();
        if(gun != null) {
            if(gun.equals(prevGun)) {
                ReflectionFacade.getInstance().setFieldValue(ItemRenderer.class, this, "itemToRender", mc.thePlayer.getCurrentEquippedItem());
            }
        }
        super.updateEquippedItem();
        if (gun != null) {
            boolean finished = System.currentTimeMillis() >= gun.getReloadFinishTime();
            prevReloadProgress = reloadProgress;
            float limit = 0.4f;
            float change = ((finished) ? 1 : 0) - reloadProgress;
            if (change < -limit)
                change = -limit;
            if (change > limit)
                change = limit;
            reloadProgress += change;
            if (prevGun != null && prevGun.isReloading() && !prevGun.equals(gun))
                prevGun.stopReloading(mc);
            if (gun.isReloading() && finished)
                gun.finishReloading(mc);
            prevGun = gun;
        } else if (prevGun != null && prevGun.isReloading())
            prevGun.stopReloading(mc);
    }

    private ItemGun getGun() {
        Minecraft mc = getMC();
        if(mc != null) {
            ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
            if (stack != null) {
                Item item = stack.getItem();
                if (item instanceof ItemGun)
                    return (ItemGun) item;
            }
        }
        return null;
    }

}
