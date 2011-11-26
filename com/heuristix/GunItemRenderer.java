package com.heuristix;

import net.minecraft.client.Minecraft;
import net.minecraft.src.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/2/11
 * Time: 10:16 PM
 */
public class GunItemRenderer extends ItemRenderer {

    private ItemGun prevGun;

    private float reloadProgress, prevReloadProgress;
    private final Map<String, String> obfuscatedFields;
    private Field prevEquippedProgressField, equippedProgressField, mcField, itemToRenderField, mapItemRendererField;

    private float getPrevEquippedProgress() {
        if(prevEquippedProgressField == null) {
            prevEquippedProgressField = Util.getField(ItemRenderer.class, "prevEquippedProgress", obfuscatedFields.get("prevEquippedProgress"));
            if(prevEquippedProgressField == null)
                return -1;
            else
                prevEquippedProgressField.setAccessible(true);
        }
        try {
            return (Float) prevEquippedProgressField.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private float getEquippedProgress() {
        if(equippedProgressField == null) {
            equippedProgressField = Util.getField(ItemRenderer.class, "equippedProgress", obfuscatedFields.get("equippedProgress"));
            if(equippedProgressField == null)
                return -1;
            else
                equippedProgressField.setAccessible(true);
        }
        try {
            return (Float) equippedProgressField.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private Minecraft getMC() {
        if(mcField == null) {
            mcField = Util.getField(ItemRenderer.class, "mc", obfuscatedFields.get("mc"));
            if(mcField == null)
                return null;
            else
                mcField.setAccessible(true);
        }
        try {
            return (Minecraft) mcField.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ItemStack getItemToRender() {
        if(itemToRenderField == null) {
            itemToRenderField = Util.getField(ItemRenderer.class, "itemToRender", obfuscatedFields.get("itemToRender"));
            if(itemToRenderField == null)
                return null;
            else
                itemToRenderField.setAccessible(true);
        }
        try {
            return (ItemStack) itemToRenderField.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    private MapItemRenderer getMapItemRenderer() {
        if(mapItemRendererField == null) {
            mapItemRendererField = Util.getField(ItemRenderer.class, "mapItemRenderer", obfuscatedFields.get("mapItemRenderer"));
            if(mapItemRendererField == null)
                return null;
            else
                mapItemRendererField.setAccessible(true);
        }
        try {
            return (MapItemRenderer) mapItemRendererField.get(this);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GunItemRenderer(Minecraft minecraft, Map<String, String> obfuscatedFields) {
        super(minecraft);
        this.obfuscatedFields = obfuscatedFields;
    }

    @Override
    public void renderItemInFirstPerson(float f) {
        float prevEquippedProgress = getPrevEquippedProgress();
        float equippedProgress = getEquippedProgress();
        Minecraft mc = getMC();

        float f1 = prevEquippedProgress + (equippedProgress - prevEquippedProgress) * f;
        if(equippedProgress == 1.0f && getGun() != null) {
            f1 = prevReloadProgress + (reloadProgress - prevReloadProgress) * f;
        }
        EntityPlayerSP entityplayersp = mc.thePlayer;
        float f2 = entityplayersp.prevRotationPitch + (entityplayersp.rotationPitch - entityplayersp.prevRotationPitch) * f;
        GL11.glPushMatrix();
        GL11.glRotatef(f2, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(entityplayersp.prevRotationYaw + (entityplayersp.rotationYaw - entityplayersp.prevRotationYaw) * f, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
        if(entityplayersp instanceof EntityPlayerSP)
        {
            EntityPlayerSP entityplayersp1 = entityplayersp;
            float f3 = entityplayersp1.prevRenderArmPitch + (entityplayersp1.renderArmPitch - entityplayersp1.prevRenderArmPitch) * f;
            float f5 = entityplayersp1.prevRenderArmYaw + (entityplayersp1.renderArmYaw - entityplayersp1.prevRenderArmYaw) * f;
            GL11.glRotatef((entityplayersp.rotationPitch - f3) * 0.1F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef((entityplayersp.rotationYaw - f5) * 0.1F, 0.0F, 1.0F, 0.0F);
        }
        ItemStack itemstack = getItemToRender();
        float f4 = mc.theWorld.getLightBrightness(MathHelper.floor_double(entityplayersp.posX), MathHelper.floor_double(entityplayersp.posY), MathHelper.floor_double(entityplayersp.posZ));
        f4 = 1.0F;
        int i = mc.theWorld.getLightBrightnessForSkyBlocks(MathHelper.floor_double(entityplayersp.posX), MathHelper.floor_double(entityplayersp.posY), MathHelper.floor_double(entityplayersp.posZ), 0);
        int k = i % 0x10000;
        int l = i / 0x10000;
        GL13.glMultiTexCoord2f(33985 /*GL_TEXTURE1_ARB*/, (float) k / 1.0F, (float) l / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if(itemstack != null)
        {
            int j = Item.itemsList[itemstack.itemID].getColorFromDamage(itemstack.getItemDamage());
            float f9 = (float)(j >> 16 & 0xff) / 255F;
            float f14 = (float)(j >> 8 & 0xff) / 255F;
            float f20 = (float)(j & 0xff) / 255F;
            GL11.glColor4f(f4 * f9, f4 * f14, f4 * f20, 1.0F);
        } else
        {
            GL11.glColor4f(f4, f4, f4, 1.0F);
        }
        if(itemstack != null && itemstack.itemID == Item.map.shiftedIndex)
        {
            GL11.glPushMatrix();
            float f6 = 0.8F;
            float f10 = entityplayersp.getSwingProgress(f);
            float f15 = MathHelper.sin(f10 * 3.141593F);
            float f21 = MathHelper.sin(MathHelper.sqrt_float(f10) * 3.141593F);
            GL11.glTranslatef(-f21 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f10) * 3.141593F * 2.0F) * 0.2F, -f15 * 0.2F);
            f10 = (1.0F - f2 / 45F) + 0.1F;
            if(f10 < 0.0F)
            {
                f10 = 0.0F;
            }
            if(f10 > 1.0F)
            {
                f10 = 1.0F;
            }
            f10 = -MathHelper.cos(f10 * 3.141593F) * 0.5F + 0.5F;
            GL11.glTranslatef(0.0F, (0.0F * f6 - (1.0F - f1) * 1.2F - f10 * 0.5F) + 0.04F, -0.9F * f6);
            GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(f10 * -85F, 0.0F, 0.0F, 1.0F);
            GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTextureForDownloadableImage(mc.thePlayer.skinUrl, mc.thePlayer.getEntityTexture()));
            for(f15 = 0; f15 < 2; f15++)
            {
                f21 = f15 * 2 - 1;
                GL11.glPushMatrix();
                GL11.glTranslatef(-0F, -0.6F, 1.1F * f21);
                GL11.glRotatef(-45 * f21, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(-90F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(59F, 0.0F, 0.0F, 1.0F);
                GL11.glRotatef(-65 * f21, 0.0F, 1.0F, 0.0F);
                Render render1 = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
                RenderPlayer renderplayer1 = (RenderPlayer)render1;
                float f32 = 1.0F;
                GL11.glScalef(f32, f32, f32);
                renderplayer1.drawFirstPersonHand();
                GL11.glPopMatrix();
            }

            f15 = entityplayersp.getSwingProgress(f);
            f21 = MathHelper.sin(f15 * f15 * 3.141593F);
            float f27 = MathHelper.sin(MathHelper.sqrt_float(f15) * 3.141593F);
            GL11.glRotatef(-f21 * 20F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f27 * 20F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f27 * 80F, 1.0F, 0.0F, 0.0F);
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
            MapData mapdata = Item.map.func_28012_a(itemstack, mc.theWorld);
            getMapItemRenderer().renderMap(mc.thePlayer, mc.renderEngine, mapdata);
            GL11.glPopMatrix();
        } else
        if(itemstack != null)
        {
            GL11.glPushMatrix();
            float f7 = 0.8F;
            if(entityplayersp.func_35205_Y() > 0) {
                EnumAction enumaction = itemstack.getItemUseAction();
                if(enumaction == EnumAction.eat) {
                    float f16 = ((float)entityplayersp.func_35205_Y() - f) + 1.0F;
                    float f28 = 1.0F - f16 / (float)itemstack.getMaxItemUseDuration();
                    float f30 = 1.0F - f28;
                    f30 = f30 * f30 * f30;
                    f30 = f30 * f30 * f30;
                    f30 = f30 * f30 * f30;
                    float f33 = 1.0F - f30;
                    GL11.glTranslatef(0.0F, MathHelper.abs(MathHelper.cos((f16 / 4F) * 3.141593F) * 0.1F) * (float)((double)f28 <= 0.20000000000000001D ? 0 : 1), 0.0F);
                    GL11.glTranslatef(f33 * 0.6F, -f33 * 0.5F, 0.0F);
                    GL11.glRotatef(f33 * 90F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(f33 * 10F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(f33 * 30F, 0.0F, 0.0F, 1.0F);
                }
            } else {
                float f11 = entityplayersp.getSwingProgress(f);
                float f17 = MathHelper.sin(f11 * 3.141593F);
                float f23 = MathHelper.sin(MathHelper.sqrt_float(f11) * 3.141593F);
                GL11.glTranslatef(-f23 * 0.4F, MathHelper.sin(MathHelper.sqrt_float(f11) * 3.141593F * 2.0F) * 0.2F, -f17 * 0.2F);
            }
            GL11.glTranslatef(0.7F * f7, -0.65F * f7 - (1.0F - f1) * 0.6F, -0.9F * f7);
            GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
            float f12 = entityplayersp.getSwingProgress(f);
            float f18 = MathHelper.sin(f12 * f12 * 3.141593F);
            float f24 = MathHelper.sin(MathHelper.sqrt_float(f12) * 3.141593F);
            GL11.glRotatef(-f18 * 20F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f24 * 20F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f24 * 80F, 1.0F, 0.0F, 0.0F);
            f12 = 0.4F;
            GL11.glScalef(f12, f12, f12);
            if(entityplayersp.func_35205_Y() > 0)
            {
                EnumAction enumaction1 = itemstack.getItemUseAction();
                if(enumaction1 == EnumAction.block)
                {
                    GL11.glTranslatef(-0.5F, 0.2F, 0.0F);
                    GL11.glRotatef(30F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-80F, 1.0F, 0.0F, 0.0F);
                    GL11.glRotatef(60F, 0.0F, 1.0F, 0.0F);
                } else
                if(enumaction1 == EnumAction.bow)
                {
                    GL11.glRotatef(-18F, 0.0F, 0.0F, 1.0F);
                    GL11.glRotatef(-12F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(-8F, 1.0F, 0.0F, 0.0F);
                    GL11.glTranslatef(-0.9F, 0.2F, 0.0F);
                    float f25 = (float)itemstack.getMaxItemUseDuration() - (((float)entityplayersp.func_35205_Y() - f) + 1.0F);
                    float f29 = f25 / 20F;
                    f29 = (f29 * f29 + f29 * 2.0F) / 3F;
                    if(f29 > 1.0F)
                    {
                        f29 = 1.0F;
                    }
                    if(f29 > 0.1F)
                    {
                        GL11.glTranslatef(0.0F, MathHelper.sin((f25 - 0.1F) * 1.3F) * 0.01F * (f29 - 0.1F), 0.0F);
                    }
                    GL11.glTranslatef(0.0F, 0.0F, f29 * 0.1F);
                    GL11.glRotatef(-335F, 0.0F, 0.0F, 1.0F);
                    GL11.glRotatef(-50F, 0.0F, 1.0F, 0.0F);
                    GL11.glTranslatef(0.0F, 0.5F, 0.0F);
                    float f31 = 1.0F + f29 * 0.2F;
                    GL11.glScalef(1.0F, 1.0F, f31);
                    GL11.glTranslatef(0.0F, -0.5F, 0.0F);
                    GL11.glRotatef(50F, 0.0F, 1.0F, 0.0F);
                    GL11.glRotatef(335F, 0.0F, 0.0F, 1.0F);
                }
            }
            if(itemstack.getItem().shouldRotateAroundWhenRendering())
            {
                GL11.glRotatef(180F, 0.0F, 1.0F, 0.0F);
            }
            renderItem(entityplayersp, itemstack);
            GL11.glPopMatrix();
        } else
        {
            GL11.glPushMatrix();
            float f8 = 0.8F;
            float f13 = entityplayersp.getSwingProgress(f);
            float f19 = MathHelper.sin(f13 * 3.141593F);
            float f26 = MathHelper.sin(MathHelper.sqrt_float(f13) * 3.141593F);
            GL11.glTranslatef(-f26 * 0.3F, MathHelper.sin(MathHelper.sqrt_float(f13) * 3.141593F * 2.0F) * 0.4F, -f19 * 0.4F);
            GL11.glTranslatef(0.8F * f8, -0.75F * f8 - (1.0F - f1) * 0.6F, -0.9F * f8);
            GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            GL11.glEnable(32826 /*GL_RESCALE_NORMAL_EXT*/);
            f13 = entityplayersp.getSwingProgress(f);
            f19 = MathHelper.sin(f13 * f13 * 3.141593F);
            f26 = MathHelper.sin(MathHelper.sqrt_float(f13) * 3.141593F);
            GL11.glRotatef(f26 * 70F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-f19 * 20F, 0.0F, 0.0F, 1.0F);
            GL11.glBindTexture(3553 /*GL_TEXTURE_2D*/, mc.renderEngine.getTextureForDownloadableImage(mc.thePlayer.skinUrl, mc.thePlayer.getEntityTexture()));
            GL11.glTranslatef(-1F, 3.6F, 3.5F);
            GL11.glRotatef(120F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(200F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-135F, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(1.0F, 1.0F, 1.0F);
            GL11.glTranslatef(5.6F, 0.0F, 0.0F);
            Render render = RenderManager.instance.getEntityRenderObject(mc.thePlayer);
            RenderPlayer renderplayer = (RenderPlayer)render;
            f26 = 1.0F;
            GL11.glScalef(f26, f26, f26);
            renderplayer.drawFirstPersonHand();
            GL11.glPopMatrix();
        }
        GL11.glDisable(32826 /*GL_RESCALE_NORMAL_EXT*/);
        RenderHelper.disableStandardItemLighting();
    }


    @Override
    public void updateEquippedItem() {
        super.updateEquippedItem();
        ItemGun gun = getGun();
        if(gun != null) {
            boolean finished = System.currentTimeMillis() >= gun.getReloadFinishTime();
            prevReloadProgress = reloadProgress;
            float f = 0.4f;
            float f1 = (finished) ? 1.0f : 0.0f;
            float change = f1 - reloadProgress;
            if(change < -f)
                change = -f;
            if(change > f)
                change = f;
            reloadProgress += change;
            if(prevGun != null && prevGun.isReloading() && !prevGun.equals(gun))
                prevGun.stopReloading();
            if(gun.isReloading() && finished)
                gun.finishReloading();
            prevGun = gun;
        } else if(prevGun != null && prevGun.isReloading())
            prevGun.stopReloading();
    }

    private ItemGun getGun() {
        Minecraft mc = getMC();
        ItemStack stack = mc.thePlayer.getCurrentEquippedItem();
        if(stack != null) {
            Item item = stack.getItem();
            if(item instanceof ItemGun)
                return (ItemGun) item;
        }
        return null;
    }

}
