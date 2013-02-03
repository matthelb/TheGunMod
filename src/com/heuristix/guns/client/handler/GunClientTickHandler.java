package com.heuristix.guns.client.handler;

import java.util.EnumSet;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.src.ModLoader;

import com.heuristix.ItemGun;
import com.heuristix.ItemProjectileShooter;
import com.heuristix.TheGunMod;
import com.heuristix.guns.Scope;
import com.heuristix.guns.client.render.GunItemRenderer;
import com.heuristix.guns.handler.GunPacketHandler;
import com.heuristix.guns.handler.GunTickHandler;
import com.heuristix.guns.util.Log;
import com.heuristix.guns.util.ReflectionFacade;

import cpw.mods.fml.common.TickType;

public class GunClientTickHandler extends GunTickHandler {

	public static final int MOUSE_LEFT = 0;
	public static final int MOUSE_RIGHT = 1;

	private boolean justAttemptedFire;
	private boolean reflectionInit;
	
	public static int recoilY, recoilX;
	public static float currentZoom;
	public static boolean isZoomed;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		Minecraft minecraft = Minecraft.getMinecraft();
		if (!reflectionInit) {
			initReflection(minecraft);
			reflectionInit = true;
		}
		ItemProjectileShooter shooter = TheGunMod.getEquippedShooter(minecraft.thePlayer);
		if (shooter != null) {
			if (minecraft.inGameHasFocus) {
				if (Mouse.isButtonDown(MOUSE_RIGHT)) {
					if ((shooter.getFireMode() == ItemProjectileShooter.FIRE_MODE_AUTO || !justAttemptedFire) && !shooter.isBursting()) {
						ModLoader.clientSendPacket(GunPacketHandler.getShooterActionPacket(shooter.itemID, GunPacketHandler.PACKET_FIRE));
						justAttemptedFire = true;
					}
				} else {
					justAttemptedFire = false;
				}
				if (shooter instanceof ItemGun) {
					applyRecoil(minecraft.thePlayer, (ItemGun) shooter);
				}
			}
			boolean in = (minecraft.gameSettings.thirdPersonView == 0) && (shooter != null) && (shooter instanceof ItemGun) && isZoomed;
			if (shooter instanceof ItemGun) {
				zoom(minecraft, (ItemGun) shooter, in);
				
			} else {
				currentZoom = 1.0f;
				isZoomed = false;
				zoom(minecraft, null, in);
			}
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	private void initReflection(Minecraft mc) {
		ReflectionFacade.getInstance().getField(EntityRenderer.class, "cameraZoom");
	}
	
	private static void applyRecoil(EntityPlayer player, ItemGun gun) {
        double y = 0;
        double y1 = recoilY;
        if (recoilY > 0) {
            y = Math.min(Math.max(recoilY * 0.1f, 0.5f), recoilY);
            recoilY -= y;
            player.rotationPitch -= y;
        }
        if (Math.abs(recoilX) > 0) {
            double x;
            if (recoilX > 0) {
                x = Math.min(Math.max(recoilX * 0.1f / 2, 0.25f), recoilX);
            } else {
                x = Math.max(Math.min(recoilX * 0.1f / 2, -0.25f), recoilX);
            }
            if (y != 0) {
                double d3 = y / y1 * recoilX;
                if (recoilX > 0) {
                    x = Math.min(d3, x);
                } else {
                    x = Math.max(d3, x);
                }
            }
            recoilX -= x;
            player.rotationYaw += x;
        }
    }
	
	private void zoom(Minecraft mc, ItemGun gun, boolean in) {
        float increment = 0.1f + (((gun != null) ? gun.getZoom() : 0) / 30f);
        GunClientTickHandler.currentZoom = (GunClientTickHandler.currentZoom + ((in) ? increment : -increment));
        GunClientTickHandler.currentZoom = (in) ? Math.min(gun.getZoom(), GunClientTickHandler.currentZoom) : Math.max(1, GunClientTickHandler.currentZoom);
        ReflectionFacade.getInstance().setFieldValue(EntityRenderer.class, mc.entityRenderer, "cameraZoom", (in && gun != null && gun.getZoom() > 1) ? Math.min(GunClientTickHandler.currentZoom, gun.getZoom()) : Math.max(GunClientTickHandler.currentZoom, 1));
        if (gun != null && gun.getScope() > 0 && (in || GunClientTickHandler.currentZoom != 1)) {
            Scope.values()[gun.getScope()].renderOverlay(gun, mc);
        }
    }


}
