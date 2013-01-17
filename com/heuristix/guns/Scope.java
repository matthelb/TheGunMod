package com.heuristix.guns;

import net.minecraft.client.Minecraft;

import com.heuristix.ItemGun;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 10/7/11
 * Time: 3:01 PM
 */
public enum Scope {
    NONE(""),
    SNIPER("%blur%/heuristix/sniper_scope.png"),
    HOLOGRAPHIC_SIGHT("%blur%/heuristix/holographic.png"),
    ADVANCED_COMBAT_OPTICAL_GUNSIGHT("%blur%/heuristix/acog.png"),
    CUSTOM(null);

    private String path;

    private Scope(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void renderOverlay(ItemGun gun, Minecraft minecraft) {
        int texture = (path == null) ? minecraft.renderEngine.allocateAndSetupTexture(gun.getCustomScope()) : minecraft.renderEngine.getTexture(getPath());
        Util.renderTexture(texture, 1, minecraft);
    }

    @Override
    public String toString() {
        return Util.normalize(name());
    }

}
