package com.heuristix.guns;

import com.heuristix.ItemProjectileBase;

/**
* Created by IntelliJ IDEA.
* User: Matt
* Date: 10/25/11
* Time: 5:53 PM
*/
public class ItemGrenade extends ItemProjectileBase {

    public ItemGrenade(int id) {
        super(id);
    }

    public String getName() {
        return "Grenade";
    }

    public String getIconPath() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object[] getCraftingRecipe() {
        return new Object[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getCraftingAmount() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
