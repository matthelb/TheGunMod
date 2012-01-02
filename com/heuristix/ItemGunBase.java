package com.heuristix;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/29/11
 * Time: 7:40 PM
 */
public class ItemGunBase extends ItemGun {

    public ItemGunBase(int id, ItemProjectile projectile) {
        super(id, projectile);
    }

    @Override
    public int getClipSize() {
        return 0;
    }

    @Override
    public int getReloadTime() {
        return 0;
    }

    @Override
    public int getRecoilY() {
        return 0;
    }

    @Override
    public int getRecoilX() {
        return 0;
    }

    @Override
    public float getZoom() {
        return 1;
    }

    @Override
    public int getScope() {
        return 0;
    }

    @Override
    public String getReloadSound() {
        return "";
    }

    @Override
    public int getShotsPerMinute() {
        return 0;
    }

    @Override
    public int getRoundsPerShot() {
        return 1;
    }

    @Override
    public int getRoundsPerMinute() {
        return getShotsPerMinute();
    }

    @Override
    public int getFireMode() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getShootSound() {
        return "";
    }

    public String getName() {
        return null;
    }

    public String getIconPath() {
        return null;
    }

    public Object[] getCraftingRecipe() {
        return new Object[0];
    }
}
