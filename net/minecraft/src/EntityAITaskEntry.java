package net.minecraft.src;

class EntityAITaskEntry
{
    public EntityAIBase field_46132_a;
    public int field_46130_b;
    final EntityAITasks field_46131_c;

    public EntityAITaskEntry(EntityAITasks entityaitasks, int i, EntityAIBase entityaibase)
    {
        field_46131_c = entityaitasks;

        field_46130_b = i;
        field_46132_a = entityaibase;
    }
}
