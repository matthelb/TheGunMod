package net.minecraft.src;

public class EntityDamageSource extends DamageSource
{
    protected Entity damageSourceEntity;

    public EntityDamageSource(String s, Entity entity)
    {
        super(s);
        damageSourceEntity = entity;
    }

    public Entity getEntity()
    {
        return damageSourceEntity;
    }

    public String func_35075_a(EntityPlayer entityplayer)
    {
        return StatCollector.translateToLocalFormatted((new StringBuilder()).append("death.").append(damageType).toString(), new Object[]
                {
                    entityplayer.username, damageSourceEntity.func_35150_Y()
                });
    }
}
