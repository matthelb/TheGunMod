package com.heuristix;

import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;

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
            e.printStackTrace();
            return null;
        }
    }
}
