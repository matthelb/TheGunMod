// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

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
