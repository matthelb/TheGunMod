package com.heuristix;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 8/31/11
 * Time: 7:19 PM
 */
public abstract class ItemProjectile extends ItemCustom {

    protected final Class<? extends EntityProjectile> projectileClass;

    public ItemProjectile(int id, Class<? extends EntityProjectile> projectileClass) {
        super(id);
        this.projectileClass = projectileClass;
    }

    public Class<? extends EntityProjectile> getProjectileClass() {
        return projectileClass;
    }

    public EntityProjectile newProjectile(World world, EntityPlayer player) {
        try {
            return getProjectileClass().getDeclaredConstructor(World.class, EntityLiving.class).newInstance(world, player);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public ItemProjectile setIconCoord(int i, int j) {
        super.setIconCoord(i, j);
        return this;
    }

    public ItemProjectile setItemName(String s) {
        super.setItemName(s);
        return this;
    }
}
