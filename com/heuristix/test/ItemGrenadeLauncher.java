package com.heuristix.test;

import com.heuristix.FireMode;
import com.heuristix.ItemGun;

/**
* Created by IntelliJ IDEA.
* User: Matt
* Date: 10/25/11
* Time: 5:53 PM
*/
public class ItemGrenadeLauncher extends ItemGun {
    public ItemGrenadeLauncher(ItemGrenade grenade) {
        super(2222, grenade);
    }

    @Override
        public int getClipSize() {
            return 100;
        }

        @Override
        public int getReloadTime() {
            return 500;
        }

        @Override
        public int getRecoilY() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public int getRecoilX() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public float getZoom() {
            return 1.0f;
        }

        @Override
        public int getScope() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public int getShotsPerMinute() {
            return 120;
        }

        @Override
        public int getRoundsPerShot() {
            return 1;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public int getRoundsPerMinute() {
            return 120;
        }

        @Override
        public int getFireMode() {
            return FireMode.SINGLE.ordinal();
        }

        @Override
        public String getShootSound() {
            return "guns.hit";
        }

        @Override
        public String getName() {
            return "Grenade Launcher";
        }

        @Override
        public String getIconPath() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object[] getCraftingRecipe() {
            return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
        }
}
