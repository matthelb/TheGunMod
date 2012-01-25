package com.heuristix;

import com.heuristix.util.Log;
import net.minecraft.src.*;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 8/31/11
 * Time: 7:19 PM
 */
public abstract class ItemProjectile extends ItemCustom {

    private final Map<ItemProjectileShooter, Class<? extends EntityProjectile>> classes;

    public ItemProjectile(int id) {
        super(id);
        this.classes = new HashMap<ItemProjectileShooter, Class<? extends EntityProjectile>>();
    }

    public Class<? extends EntityProjectile> getProjectileClass(ItemProjectileShooter shooter) {
        return classes.get(shooter);
    }

    public void putProjectileClass(ItemProjectileShooter shooter, Class<? extends EntityProjectile> clazz) {
        classes.put(shooter, clazz);
    }

    public EntityProjectile newProjectile(World world, EntityPlayer player, ItemProjectileShooter shooter) {
        try {
            return getProjectileClass(shooter).getDeclaredConstructor(World.class, EntityLiving.class).newInstance(world, player);
        } catch (Exception e) {
            Log.throwing(getClass(), "newProjectile(World world, EntityPlayer player, ItemProjectileShooter shooter)", e, mod_Guns.class);
            return null;
        }
    }

    public boolean hasWorkbenchRecipe() {
        return false;
    }

    public Object[] getCraftingRecipe() {
        return new Object[]{Item.ingotIron, 1 / 8f, Item.gunpowder, 1 / 16f};
    }

}
