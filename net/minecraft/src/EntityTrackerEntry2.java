package net.minecraft.src;

public class EntityTrackerEntry2
{
    public int entityId;
    public boolean entityHasOwner;

    public EntityTrackerEntry2(int i, boolean flag)
    {
        entityId = -1;
        entityHasOwner = false;
        entityId = i;
        entityHasOwner = flag;
    }
}
