package com.heuristix;

import com.heuristix.guns.EntityBullet;
import com.heuristix.guns.EntityFlame;
import com.heuristix.guns.EntityGrenade;
import com.heuristix.guns.EntityRocketGrenade;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/27/11
 * Time: 5:34 PM
 */
public enum ProjectileType {
    BULLET("Bullet", EntityBullet.class),
    GRENADE("Grenade", EntityGrenade.class),
    ROCKET_GRENADE("Rocket Grenade", EntityRocketGrenade.class),
    FLAME("Flame", EntityFlame.class);

    private final String desc;
    private final Class clazz;

    private ProjectileType(String desc, Class clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }

    public Class getProjectileType() {
        return clazz;
    }

    public static ProjectileType forClass(Class clazz) {
        for(ProjectileType t : values()) {
            if(t.getProjectileType().equals(clazz))
                return t;
        }
        return null;
    }

    public String toString() {
        return desc;
    }

}
