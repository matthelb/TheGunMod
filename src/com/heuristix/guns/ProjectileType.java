package com.heuristix.guns;


/**
 * Created by IntelliJ IDEA.
 * User: Matt
 * Date: 12/27/11
 * Time: 5:34 PM
 */
public enum ProjectileType {
    BULLET("Bullet", EntityBulletBase.class),
    GRENADE("Grenade", EntityGrenadeBase.class),
    ROCKET_GRENADE("Rocket Grenade", EntityRocketGrenadeBase.class),
    FLAME("Flame", EntityFlameBase.class),
    INCENDIARY_BULLET("Incendiary Bullet", EntityIncendiaryBulletBase.class);

    private final String desc;
    private final Class<?> clazz;

    private ProjectileType(String desc, Class<?> clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }

    public Class<?> getProjectileType() {
        return clazz;
    }

    public static ProjectileType forClass(Class<?> clazz) {
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
