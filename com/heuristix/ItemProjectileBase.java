package com.heuristix;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 9/29/11
 * Time: 7:40 PM
 */
public class ItemProjectileBase extends ItemProjectile {

    public ItemProjectileBase(int id, Class<? extends EntityProjectile> projectileClass) {
        super(id, projectileClass);
    }

    public String getName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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
