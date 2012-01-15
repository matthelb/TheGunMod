package net.minecraft.src;

public class EntityDamageSourceIndirect extends EntityDamageSource
{
    private Entity damageSourceEntity2;

    public EntityDamageSourceIndirect(String s, Entity entity, Entity entity1)
    {
        super(s, entity);
        damageSourceEntity2 = entity1;
    }

    public Entity getSourceOfDamage()
    {
        return damageSourceEntity;
    }

    public Entity getEntity()
    {
        return damageSourceEntity2;
    }

    public String func_35075_a(EntityPlayer entityplayer)
    {
        return StatCollector.translateToLocalFormatted((new StringBuilder()).append("death.").append(damageType).toString(), new Object[]
                {
                    entityplayer.username, damageSourceEntity2.func_35150_Y()
                });
    }
}
