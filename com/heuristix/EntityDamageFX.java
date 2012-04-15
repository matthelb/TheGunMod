package com.heuristix;

import net.minecraft.src.*;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 3/17/12
 * Time: 2:16 PM
 */
public class EntityDamageFX extends EntityFX {

    private final String text;

    public EntityDamageFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ, int damage) {
        super(world, x, y, z, motionX, motionY, motionZ);
        if(damage > 0) {
            particleRed = 0;
            particleBlue = 0;
            particleGreen = 1;
        } else if( damage < 0) {
            particleRed = 1;
            particleBlue = 0;
            particleGreen = 0;
        }
        this.text = String.valueOf(damage / 2.0f);
    }

    @Override
    public void renderParticle(Tessellator t, float renderPartialTicks, float rotationX, float rotationXZ, float rotationZ, float rotationYZ, float rotationXY) {
        FontRenderer fr = RenderManager.instance.getFontRenderer();
        fr.drawString(text, -fr.getStringWidth(text) / 2, 0, getColor());
        super.renderParticle(t, renderPartialTicks, rotationX, rotationXZ, rotationZ, rotationYZ, rotationXY);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public int getColor() {
        return (int) (particleRed * 255) << 4 | (int) (particleGreen * 255) << 2 | (int) (particleBlue * 255);
    }
}
