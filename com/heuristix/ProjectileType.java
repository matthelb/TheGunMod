package com.heuristix;


import com.heuristix.guns.EntityFlameBase;
import com.heuristix.guns.EntityGrenadeBase;
import com.heuristix.guns.EntityRocketGrenadeBase;

/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/27/11
 * Time: 5:34 PM
 */
public enum ProjectileType {
    BULLET("Bullet", com.heuristix.guns.EntityBulletBase.class),
    GRENADE("Grenade", EntityGrenadeBase.class),
    ROCKET_GRENADE("Rocket Grenade", EntityRocketGrenadeBase.class),
    FLAME("Flame", EntityFlameBase.class);

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
        for (ProjectileType t : values()) {
            if (t.getProjectileType().equals(clazz)) {
                return t;
            }
        }
        return null;
    }

    public String toString() {
        return desc;
    }

}
