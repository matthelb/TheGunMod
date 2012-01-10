// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) braces deadcode fieldsfirst 

package net.minecraft.src;


public class NetClientHandlerEntity
{

    public Class entityClass;
    public boolean entityHasOwner;

    public NetClientHandlerEntity(Class class1, boolean flag)
    {
        entityClass = null;
        entityHasOwner = false;
        entityClass = class1;
        entityHasOwner = flag;
    }
}
