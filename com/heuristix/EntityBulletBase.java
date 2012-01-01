package com.heuristix;

import com.heuristix.guns.EntityBullet;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.World;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/27/11
 * Time: 4:32 PM
 */
public class EntityBulletBase extends EntityBullet {

    public EntityBulletBase(World world) {
        super(world);
    }

    public EntityBulletBase(World world, EntityLiving entityliving) {
        super(world, entityliving);
    }

    @Override
    public int getDamage() {
        return super.getDamage();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public float getEffectiveRange() {
        return super.getEffectiveRange();    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public float getSpread() {
        return super.getSpread();    //To change body of overridden methods use File | Settings | File Templates.
    }
}
