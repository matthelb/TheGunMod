package com.heuristix.test;

import com.heuristix.ItemProjectile;

/**
* Created by IntelliJ IDEA.
* User: Matt
* Date: 10/25/11
* Time: 5:53 PM
*/
public class ItemGrenade extends ItemProjectile {
    public ItemGrenade() {
        super(2223, EntityGrenade.class);
    }

    public String getName() {
            return "Grenade";
        }

        @Override
        public String getIconPath() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Object[] getCraftingRecipe() {
            return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public int getCraftingAmount() {
            return 0;  //To change body of implemented methods use File | Settings | File Templates.
        }
}
