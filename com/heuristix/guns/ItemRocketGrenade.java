package com.heuristix.guns;


import com.heuristix.ItemProjectileBase;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 11/21/11
 * Time: 6:31 PM
 */
public class ItemRocketGrenade extends ItemProjectileBase {

    public ItemRocketGrenade(int id) {
        super(id);
    }

    public String getName() {
        return "Rocket";
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
