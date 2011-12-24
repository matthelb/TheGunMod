package net.minecraft.src;

import com.heuristix.*;
import com.heuristix.guns.*;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 10/14/11
 * Time: 5:03 PM
 */
public class mod_Test extends Mod {


    public mod_Test() {
        ItemRocketGrenade rocketGrenade = new ItemRocketGrenade(6001);
        ItemGuidedRocketLauncher gun = new ItemGuidedRocketLauncher(6000, rocketGrenade);
        rocketGrenade.putProjectileClass(gun, EntityGuidedRocketGrenade.class);
        registerItem(rocketGrenade);
        registerItem(gun);

        /*ItemGrenade grenade = new ItemGrenade(8001);
        ItemGrenadeLauncher gl = new ItemGrenadeLauncher(8000, grenade);
        grenade.putProjectileClass(gl, EntityGrenade.class);
        registerItem(grenade);
        registerItem(gl);*/

        ModLoader.SetInGameHook(this, true, false);
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public void load() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean OnTick(float tick, Minecraft minecraft) {
        /*if(minecraft.inGameHasFocus) {
            if(minecraft.thePlayer !=  null && minecraft.theWorld != null)
                renderRedDot(minecraft.thePlayer, minecraft.theWorld);
        }*/
        return true;
    }

    private static void renderRedDot(EntityPlayer player, World world) {
        Vec3D playerPos = player.getPosition(1);
        Vec3D projectedPos = Util.getProjectedPoint(playerPos, player.getLook(1), 1000);
        MovingObjectPosition rayTrace = world.rayTraceBlocks(playerPos, projectedPos);
        Vec3D vec = null;
        if(rayTrace != null)
            vec = rayTrace.hitVec;
        if(vec == null)
            vec = projectedPos;
        GL11.glPushMatrix();
        GL11.glDisable(3553 /*GL_TEXTURE_2D*/);
        GL11.glColor4f(1, 0, 0, 0.4f);
        Tessellator t = Tessellator.instance;
        t.setTranslationD(vec.xCoord, vec.yCoord, vec.zCoord);
        t.setNormal(0, 1, 0);
        t.startDrawingQuads();
        t.addVertexWithUV(-1, -1, 0, 0, 0);
        t.addVertexWithUV(1, -1, 0, 1, 0);
        t.addVertexWithUV(1, 1, 0, 1, 1);
        t.addVertexWithUV(-1, 1, 0, 0, 1);
        t.draw();
        GL11.glEnable(3553 /*GL_TEXTURE_2D*/);
        GL11.glPopMatrix();
    }

}
